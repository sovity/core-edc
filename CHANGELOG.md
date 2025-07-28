# Changelog

## [0.11.1.x] - UNRELEASED

### Overview

#### Changes

#### Details

#### Compatibility

#### Resolution plan

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
  - Catalog performance

#### Compatibility

Compatible with core EDC `0.11.1`.

---
