# Fork

## Changes implemented in `0.11.1`'s fork

### 0.11.1.2

Add data plane OAuth tokens caching to offload keycloak.

### 0.11.1.1

---

#### Force `/`

Discarded. The problem seems to be gone.

---

#### Switch to UUIDv7

Identical to the previous version. Followed the forking procedure.

### Catalog performance

Changes to improve the performance of the catalog request from `f7e46c618b276664b579cc884d5c94dc87a9f6b0`
* Stop re-compiling a regex on each use and eventually ttop relying on that regex
* Add caching for frequently used `PathItem`s. 
* Avoid needless String formatting.

### Force shutdown

Not migrated. The tests shut down fine now.

### Affected files

- `core/common/lib/util-lib/build.gradle.kts`
  - Added guava dependency
- `core/common/lib/util-lib/src/main/java/org/eclipse/edc/util/reflection/PathItem.java`
  - Added caching
- `core/common/lib/util-lib/src/main/java/org/eclipse/edc/util/reflection/ReflectionUtil.java`
  - Remove regex and performance optimizations
- `core/control-plane/control-plane-core/src/main/java/org/eclipse/edc/connector/controlplane/query/asset/AssetPropertyLookup.java`
  - String manipulation optimizations
- 