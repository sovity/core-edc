# Changelog

## [0.7.2.1] - UNRELEASED

### Overview

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

#### Resolution plan

