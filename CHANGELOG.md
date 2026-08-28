# Changelog

## [0.17.0.x] - UNRELEASED

### Overview

#### Changes

#### Compatibility

Should be compatible without any modification with a `0.17.0` version.

---

## [0.17.0.0-rc1] - 2026-08-28

### Overview

Port Core-EDC to Version 0.17.0 of Eclipse EDC

#### Changes

- Replace UUID Generation to use time-based UUIDs ([#92](https://github.com/sovity/core-edc/issues/92))
- Improve performance of catalog requests ([#93](https://github.com/sovity/core-edc/issues/93))
- Add configurable OAuth2 token caching ([#94](https://github.com/sovity/core-edc/issues/94))
- Add @Nullable markers ([#95](https://github.com/sovity/core-edc/issues/95))
- Omit JSON-LD compaction for remote messages ([#68](https://github.com/sovity/core-edc/issues/68))
- Remove unnecessary data flow updates in state machine ([#97](https://github.com/sovity/core-edc/issues/97))
- Remove GET secrets endpoint from Secrets API ([#98](https://github.com/sovity/core-edc/issues/98))
- Log Non-2xx responses from HTTP data source to data plane ([#100](https://github.com/sovity/core-edc/issues/100))

#### Compatibility

Should be compatible without any modification with a `0.17.0` version.

---