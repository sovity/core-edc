# Changelog

## [0.14.0.x] - UNRELEASED

### Overview

#### Changes

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.6] - 2026-08-28

### Overview

Improve observability of HTTP data plane transfers.

#### Changes

- Log Non-2xx responses from HTTP data source to data plane ([#100](https://github.com/sovity/core-edc/issues/100))

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.5] - 2026-08-10

### Overview

Remove endpoint for reading Vault secrets.

#### Changes

- Remove GET secrets endpoint from the Secrets API ([#98](https://github.com/sovity/core-edc/issues/98))

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.4] - 2026-05-08

### Overview

Performance improvements in the data flow state machine.

#### Changes

- Remove unnecessary data flow updates in the state machine ([#97](https://github.com/sovity/core-edc/issues/97))

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.3] - 2026-02-04

### Overview

Fix further compatibility issues with Jupiter EDCs.

#### Changes

- Support legacy fields in PresentationResponseMessage JSON-LD

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.2] - 2026-01-29

### Overview

Fix compatibility issues with Jupiter EDCs.

#### Changes

- Omit JSON-LD compaction for remote messages ([#68](https://github.com/sovity/core-edc/issues/68))
- Conditionally add Bearer prefix to authorization header in DSP requests ([#66](https://github.com/sovity/core-edc/issues/66))

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.1] - 2025-09-29

### Overview

Initial migration of changes from `0.11.1.3` into `0.14.0`.

#### Changes

- Replace UUID generation to use time-based UUIDs ([#92](https://github.com/sovity/core-edc/issues/92))
- Improve performance of catalog requests ([#93](https://github.com/sovity/core-edc/issues/93))
- Add configurable OAuth2 token caching ([#94](https://github.com/sovity/core-edc/issues/94))
- Add @Nullable markers ([#95](https://github.com/sovity/core-edc/issues/95))

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

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
