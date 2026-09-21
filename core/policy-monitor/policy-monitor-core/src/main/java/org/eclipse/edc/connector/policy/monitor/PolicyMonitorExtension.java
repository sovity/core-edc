/*
 *  Copyright (c) 2023 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.connector.policy.monitor;

import org.eclipse.edc.connector.controlplane.policy.contract.ContractExpiryCheckFunction;
import org.eclipse.edc.connector.controlplane.services.spi.contractagreement.ContractAgreementService;
import org.eclipse.edc.connector.controlplane.services.spi.transferprocess.TransferProcessService;
import org.eclipse.edc.connector.controlplane.transfer.spi.event.TransferProcessStarted;
import org.eclipse.edc.connector.policy.monitor.manager.PolicyMonitorManagerImpl;
import org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorContext;
import org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorManager;
import org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorStore;
import org.eclipse.edc.connector.policy.monitor.subscriber.StartMonitoring;
import org.eclipse.edc.policy.engine.spi.PolicyEngine;
import org.eclipse.edc.policy.engine.spi.RuleBindingRegistry;
import org.eclipse.edc.policy.model.Permission;
import org.eclipse.edc.runtime.metamodel.annotation.Configuration;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Inject;
import org.eclipse.edc.runtime.metamodel.annotation.Provides;
import org.eclipse.edc.runtime.metamodel.annotation.Setting;
import org.eclipse.edc.runtime.metamodel.annotation.SettingContext;
import org.eclipse.edc.spi.event.EventRouter;
import org.eclipse.edc.spi.system.ExecutorInstrumentation;
import org.eclipse.edc.spi.system.ServiceExtension;
import org.eclipse.edc.spi.system.ServiceExtensionContext;
import org.eclipse.edc.spi.telemetry.Telemetry;
import org.eclipse.edc.statemachine.StateMachineConfiguration;

import java.time.Clock;
import java.time.Duration;
import java.time.format.DateTimeParseException;

import static org.eclipse.edc.connector.controlplane.policy.contract.ContractExpiryCheckFunction.CONTRACT_EXPIRY_EVALUATION_KEY;
import static org.eclipse.edc.connector.policy.monitor.PolicyMonitorExtension.NAME;
import static org.eclipse.edc.connector.policy.monitor.spi.PolicyMonitorContext.POLICY_MONITOR_SCOPE;
import static org.eclipse.edc.jsonld.spi.PropertyAndTypeNames.ODRL_USE_ACTION_ATTRIBUTE;

@Extension(value = NAME)
@Provides({ PolicyMonitorManager.class })
public class PolicyMonitorExtension implements ServiceExtension {

    public static final String NAME = "Policy Monitor";
    public static final String DEFAULT_CHECK_PERIOD = "PT1H";
    public static final String CHECK_PERIOD_KEY = "edc.policy.monitor.period";

    @SettingContext("edc.policy.monitor")
    @Configuration
    private StateMachineConfiguration stateMachineConfiguration;

    @Setting(description = "Minimum period between two policy checks of the same monitored transfer, as ISO-8601 duration", defaultValue = DEFAULT_CHECK_PERIOD, key = CHECK_PERIOD_KEY)
    private String checkPeriod;

    @Inject
    private ExecutorInstrumentation executorInstrumentation;

    @Inject
    private Telemetry telemetry;

    @Inject
    private Clock clock;

    @Inject
    private EventRouter eventRouter;

    @Inject
    private ContractAgreementService contractAgreementService;

    @Inject
    private PolicyEngine policyEngine;

    @Inject
    private TransferProcessService transferProcessService;

    @Inject
    private PolicyMonitorStore policyMonitorStore;

    @Inject
    private RuleBindingRegistry ruleBindingRegistry;

    private PolicyMonitorManager manager;

    @Override
    public void initialize(ServiceExtensionContext context) {

        policyEngine.registerScope(POLICY_MONITOR_SCOPE, PolicyMonitorContext.class);
        ruleBindingRegistry.bind(ODRL_USE_ACTION_ATTRIBUTE, POLICY_MONITOR_SCOPE);
        ruleBindingRegistry.bind(CONTRACT_EXPIRY_EVALUATION_KEY, POLICY_MONITOR_SCOPE);
        policyEngine.registerFunction(PolicyMonitorContext.class, Permission.class, CONTRACT_EXPIRY_EVALUATION_KEY, new ContractExpiryCheckFunction<>());

        manager = PolicyMonitorManagerImpl.Builder.newInstance()
                .clock(clock)
                .batchSize(stateMachineConfiguration.batchSize())
                .waitStrategy(stateMachineConfiguration.iterationWaitExponentialWaitStrategy())
                .executorInstrumentation(executorInstrumentation)
                .monitor(context.getMonitor())
                .telemetry(telemetry)
                .contractAgreementService(contractAgreementService)
                .policyEngine(policyEngine)
                .transferProcessService(transferProcessService)
                .store(policyMonitorStore)
                .entityRetryProcessConfiguration(stateMachineConfiguration.entityRetryProcessConfiguration())
                .checkPeriod(parseCheckPeriod(context))
                .build();

        context.registerService(PolicyMonitorManager.class, manager);

        eventRouter.registerSync(TransferProcessStarted.class, new StartMonitoring(manager));
    }

    private Duration parseCheckPeriod(ServiceExtensionContext context) {
        try {
            return Duration.parse(checkPeriod);
        } catch (DateTimeParseException e) {
            context.getMonitor().warning("Invalid value '%s' for setting %s, expected an ISO-8601 duration. Falling back to default %s"
                    .formatted(checkPeriod, CHECK_PERIOD_KEY, DEFAULT_CHECK_PERIOD));
            return Duration.parse(DEFAULT_CHECK_PERIOD);
        }
    }

    @Override
    public void start() {
        manager.start();
    }

    @Override
    public void shutdown() {
        if (manager != null) {
            manager.stop();
        }
    }

}
