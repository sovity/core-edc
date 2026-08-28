# Changes Implemented in `0.14.0`'s fork

## 0.14.0.6

- When an HTTP data source returns a non-2xx status code, the `HttpDataSource` now logs it as a warning. Previously the error was swallowed by the EDC or only surfaced in the response, so it could not be seen without reproducing the request.

## 0.14.0.5

- Remove the `/v3/secrets/{secretId}` endpoint from the secrets API. This endpoint makes it possible to read secrets from the vault as soon as a user has access to the management API. This is a security risk since it allows a user to read secrets that they should not have access to, in particular for the test EDCs on Sirius with unsecure API protection.

## 0.14.0.4

- Remove the `updateFlowLease` function from the State Machine in the DataPlaneManagerImpl.java. This function is not needed since we only have a single data-plane and only negatively impacts the performance. Removing it reduces the number of lease queries.

## 0.14.0.3

- The names of the fields in the PresentationResponseMessage JSON-LD changed in between the Jupiter and Saturn Version. To allow a Saturn EDC to be able to communicate with both a Jupiter and Saturn compatible DIM-Wallet, the old field names should still be supported.
  - Otherwise, `edc.dcp.v08.forced` would need to be set to `true` to allow communication with older versions of the DIM-Wallet.
  - This is in particular a problem with the DIM-Stub-Wallet on staging used for testing. Here, for Saturn-EDCs, the old version needs to be used to have correct DIDs being returned. This fix allows the above property to be set to `false` even when using this old Stub-Wallet.

## 0.14.0.2

- Omit Compaction of JSON-LD payloads to avoid missing context for Jupiter EDCs.
- Conditionally add Bearer prefix to authorization header in DSP requests.

## 0.14.0.1

### Changes from `0.11.1.1`

#### Force `/`

Discarded. The problem seems to be gone.

#### Switch to UUIDv7

Identical to the previous version. Followed the forking procedure.

#### Catalog performance

Added from `f7e46c618b276664b579cc884d5c94dc87a9f6b0`.

###### Affected files

- `core/common/lib/util-lib/build.gradle.kts`
  - Added guava dependency
- `core/common/lib/util-lib/src/main/java/org/eclipse/edc/util/reflection/PathItem.java`
  - Added caching
- `core/common/lib/util-lib/src/main/java/org/eclipse/edc/util/reflection/ReflectionUtil.java`
  - Remove regex and performance optimizations
- `core/control-plane/control-plane-core/src/main/java/org/eclipse/edc/connector/controlplane/query/asset/AssetPropertyLookup.java`
  - String manipulation optimizations

#### OAuth caching

Added from `8df1bacebf45c9a00b01fe65fa92d229f442afd9`

#### Code safety improvements.

Added from `59c7798e5d915e82d8ab845efd14a7638cfdc3af`

## Previous changes from `0.11.1`'s fork

### 0.11.1.2

Add data plane OAuth tokens caching to offload keycloak.

### 0.11.1.1

---

#### Force `/`

In [`org.eclipse.edc.iam.identitytrust.core.IdentityTrustTransformExtension`](../../../extensions/common/iam/identity-trust/identity-trust-core/src/main/java/org/eclipse/edc/iam/identitytrust/core/IdentityTrustTransformExtension.java),
the separator should not be `File.separator` as it causes an error on windows where the separator is `\` but the path built this way is a classpath, requiring `/`.

---

#### Switch to UUIDv7

Identical to [0.2.1.X.md](0.2.1.X.md#switch-to-uuidv7)'s or [0.7.2.X.md](0.7.2.X.md#switch-to-uuidv7)'s UUID switch.

### Catalog performance

Changes to improve the performance of the catalog request
* Stop re-compiling a regex on each use and eventually ttop relying on that regex
* Add caching for frequently used `PathItem`s.
* Avoid needless String formatting.

### Force shutdown

Not migrated. The tests shut down fine now.
