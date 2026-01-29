# Changelog

## [0.14.0.x] - UNRELEASED

### Overview

#### Changes

#### Details

#### Compatibility

#### Resolution plan

---

## [0.14.0.2] - 2026-01-29

### Overview

Fix compatibility issues with Jupiter EDCs.

#### Changes

- JSON-LD is no longer compacted in JsonLdRemoteMessages
- Conditionally add Bearer prefix to authorization header in DSP requests

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.1] - 2025-09-29

### Overview

Initial migration of changes from `0.11.1.3` into `0.14.0`.

#### Changes

- Port the previous changes
    - Replacement of java.util.UUID with UUIDv7
    - Catalog performance
    - Add caching for data plane OAuth requests (`8df1baceb`)
    - Add nullability correctness

- Changes that were not ported
  - Force `/` in IdentityTrustTransformExtension
    - The mistake is not present anymore

#### Details

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

#### Resolution plan

---

## [0.11.1.3] - 2025-08-12

### Overview

Code safety improvements.

#### Changes

- Correctly mark nullable elements

#### Details

- Any element that is comment as `or null` now has the appropriate `@Nullable` annotation.

#### Compatibility

- May add compiler warnings (Java) or errors (Kotlin), for the developer's sake.

---

## [0.11.1.2] - UNRELEASED

### Overview

Add OAuth tokens caching.

#### Changes

- Add caching for data plane OAuth requests

#### Details

- Add `TokenCache`ing in `Oauth2HttpRequestParamsDecorator`.
    - The minimum time to live for the token (how long before it expires it should be renewed) can be configured with `sovity.edc.dataplane.oauth2.cache.minimum.time.to.live`.

#### Compatibility

No breaking expected.

#### Resolution plan

None.

---

## [0.11.1.1] - 2025-04-25

### Overview

Initial migration of changes from `0.7.2.2` into `0.11.1`.

#### Changes

- Port the previous changes
    - Force `/` in IdentityTrustTransformExtension
    - Replacement of java.util.UUID with UUIDv7
    - Catalog performance (commit `f7e46c618b276664b579cc884d5c94dc87a9f6b0`)

#### Compatibility

Compatible with core EDC `0.11.1`.

---
