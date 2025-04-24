# Fork

## Changes implemented in `0.11.1`'s fork

---

### Force `/`

In [`org.eclipse.edc.iam.identitytrust.core.IdentityTrustTransformExtension`](../../../extensions/common/iam/identity-trust/identity-trust-core/src/main/java/org/eclipse/edc/iam/identitytrust/core/IdentityTrustTransformExtension.java),
the separator should not be `File.separator` as it causes an error on windows where the separator is `\` but the path built this way is a classpath, requiring `/`.

---

### Switch to UUIDv7

Identical to [0.2.1.X.md](0.2.1.X.md#switch-to-uuidv7)'s or [0.7.2.X.md](0.7.2.X.md#switch-to-uuidv7)'s UUID switch.

## Catalog performance

Changes to improve the performance of the catalog request
* Stop re-compiling a regex on each use and eventually ttop relying on that regex
* Add caching for frequently used `PathItem`s.
* Avoid needless String formatting.

## Force shutdown

Not migrated. The tests shut down fine now.
