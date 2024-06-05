# Changelog

## [x.x.x.x] - UNRELEASED

### Overview

#### Requirements

#### Implementation

#### Compatibility

#### Resolution plan

## [0.2.1.3] - 2024-06-05

### Overview

Backports a security flaw fix from `0.5` https://github.com/eclipse-edc/Connector/pull/3719/files

#### Requirements

Fix the security flaw in `0.2.1` that was patched in `0.5`.

#### Implementation

Imported the patch from `0.5` into `0.2.1`.

#### Compatibility

Only the log's output was changed, removing potentially sensitive data.
Expect no other change in behavior.

#### Resolution plan

Update to `0.5+`.

## [0.2.1.2] - 2024-04-24

### Overview

Re-release of `0.2.1-sovity1.0` as an official version following the established branches and version conventions.

## [0.2.1-sovity1.0] - 2024-04-12

### Overview

Adds a workaround to preserve the asset parameterization feature from pre-0.1 releases in a *provider push* scenario.

#### Requirements

Eclipse EDC before version `0.1` used a different communication protocol that was capable of sending the parameterization data to the provider.

This is not possible anymore due to the current IDS specification for [transfer messages](https://docs.internationaldataspaces.org/ids-knowledgebase/v/dataspace-protocol/transfer-process/transfer.process.protocol#21-transfer-request-message).

This feature is needed by one of our clients.

#### Implementation

The only field (as of IDS version `2024-01`) that lets us send data in the concerned message is the `DataAddress`'s properties. This is the field, in combination with specific properties, that we use in this workaround to transfer the missing information to the provider.
This extra information is extracted on the provider's side and put back where it used to be, in the `properties` of the `DataFlowRequest`.

The workaround happens at this location:

`org.eclipse.edc.connector.transfer.dataplane.flow.ProviderPushTransferDataFlowController.createRequest`

With this workaround, a parameterized asset can be requested with the following query:

```json
{
  "@type": "https://w3id.org/edc/v0.0.1/ns/TransferRequest",
  "https://w3id.org/edc/v0.0.1/ns/assetId": "{{ASSET_ID}}",
  "https://w3id.org/edc/v0.0.1/ns/contractId": "{{CONTRACT_ID}}",
  "https://w3id.org/edc/v0.0.1/ns/connectorAddress": "https://{{PROVIDER_EDC_FQDN}}/api/dsp",
  "https://w3id.org/edc/v0.0.1/ns/connectorId": "{{PROVIDER_EDC_PARTICIPANT_ID}}",
  "https://w3id.org/edc/v0.0.1/ns/dataDestination": {
    "https://w3id.org/edc/v0.0.1/ns/type": "HttpData",
    "https://w3id.org/edc/v0.0.1/ns/baseUrl": "{{DATA_SINK_URL}}",
    "https://sovity.de/workaround/proxy/param/pathSegments": "{{PARAMETERIZATION_PATH}}",
    "https://sovity.de/workaround/proxy/param/method": "{{PARAMETERIZATION_METHOD}}",
    "https://sovity.de/workaround/proxy/param/queryParams": "{{PARAMETERIZATION_QUERY}}",
    "https://sovity.de/workaround/proxy/param/mediaType": "{{PARAMETERIZATION_CONTENTTYPE}}",
    "https://sovity.de/workaround/proxy/param/body": "{{PARAMETERIZATION_BODY}}"
  },
  "https://w3id.org/edc/v0.0.1/ns/privateProperties": {},
  "https://w3id.org/edc/v0.0.1/ns/protocol": "dataspace-protocol-http",
  "https://w3id.org/edc/v0.0.1/ns/managedResources": false
}
```

Where the `https://sovity.de/workaround/proxy/param/*` carry the parameterization data.

#### Resolution plan

There is a ticket open on the [IDS side](https://github.com/International-Data-Spaces-Association/ids-specification/discussions/262)

The goal is to
* have this feature standardized
* have it implemented in core EDC
* use the new core EDC version

#### Compatibility

This change is targeting the *provider push* use-case only. The *consumer pull* use-case is not affected.

The parameterization feature must work between 2 EDCs that use this forked version.

The parameterization must work from an unpatched Consumer EDC, targeting a patched EDC provider, using the request mentioned in the Implementation section.

The parametrization will not work if the provider EDC is not using this patched version.

Expecting no other incompatibilities with core EDC 0.2.1.
