# Changelog

## [0.7.2.X] - UNRELEASED

### Overview

#### Changes

#### Details

#### Compatibility

#### Resolution plan

---

## [0.7.2.2] - 2025-02-05

### Overview

* Performance optimizations for the catalog request
* Migrate additional random UUIDs to time-based UUIDs

#### Compatibility

No breaking change expected.

---

## [0.7.2.1] - 2025-02-05

### Overview

Migration from the previous fork and first fix.

#### Changes

* Re-establish a working fork on top of v0.7.2
* Migrate previous changes
  * Switch to UUIDv7
* Discarded changes
  * Add assets parametrization for provider push scenario
    * The code to support it got removed
  * Fix security flaw in logging
    * Already fixed since `0.5`
* Fix file separator usage in `IdentityTrustTransformExtension`

#### Details

* Disabled tests that failed because of outdated certificates.

#### Compatibility

No breaking change expected.

#### Resolution plan

