# Changelog

## [0.14.0.x] - UNRELEASED

### Overview

#### Changes

#### Details

#### Compatibility

#### Resolution plan

---

## [0.14.0.4.2] - 2026-11-13

### Overview

Security fixes for the validation of credentials and JSON-LD documents, backported from Eclipse EDC `0.18.2`.

#### Changes

- Security: Linked Data Proof credentials are only verified with the key from the issuer's DID document ([eclipse-edc/Connector#6147](https://github.com/eclipse-edc/Connector/pull/6147))
- Security: JSON-LD contexts are no longer loaded from `file:` or `jar:` URLs ([eclipse-edc/Connector#6109](https://github.com/eclipse-edc/Connector/pull/6109))
- Security: The issuer of a credential is checked before its revocation status is fetched ([eclipse-edc/Connector#6150](https://github.com/eclipse-edc/Connector/pull/6150))

#### Compatibility

The following is now rejected:

- Linked Data Proof credentials that bring their own key, or whose key cannot be found in the issuer's DID document.
- JSON-LD documents that reference a context via a `file:` or `jar:` URL. Contexts configured via `edc.jsonld.document.*` still work.

---

## [0.14.0.4.1] - 2026-09-02

### Overview

Improve observability of HTTP data plane transfers.

#### Changes

- `HttpDataSource` now logs a warning when the data source returns a non-2xx status code

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.4] - 2026-05-08

### Overview

Performance improvements in the data flow state machine.

#### Changes

- Remove the `updateFlowLease` function from DataPlaneManagerImpl to reduce lease queries

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

---

## [0.14.0.3] - 2026-02-04

### Overview

Fix further compatibility issues with Jupiter EDCs.

#### Changes

- Support Legacy fields in PresentationResponseMessage JSON-LD
  - Enables the EDC to parse responses from DIM-Wallets compatible with both Saturn- and Jupiter-EDCs

#### Compatibility

Should be compatible without any modification with a `0.14.0` version.

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
