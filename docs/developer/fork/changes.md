# Changes Implemented in `0.14.0`'s fork

## 0.14.0.3

- Support Legacy fields in PresentationResponseMessage JSON-LD for Jupiter EDC compatibility.

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
