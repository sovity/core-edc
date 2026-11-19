# Changelog

## [0.17.0.x] - UNRELEASED

### Overview

#### Changes

- Replace UUID Generation to use time-based UUIDs ([#92](https://github.com/sovity/core-edc/issues/92))
- Improve performance of catalog requests ([#93](https://github.com/sovity/core-edc/issues/93))
- Add configurable OAuth2 token caching ([#94](https://github.com/sovity/core-edc/issues/94))
- Add @Nullable markers ([#95](https://github.com/sovity/core-edc/issues/95))
- Omit JSON-LD compaction for remote messages ([#68](https://github.com/sovity/core-edc/issues/68))
- Remove unnecessary data flow updates in state machine ([#97](https://github.com/sovity/core-edc/issues/97))
- Remove GET secrets endpoint from Secrets API ([#98](https://github.com/sovity/core-edc/issues/98))
- Log Non-2xx responses from HTTP data source to data plane ([#100](https://github.com/sovity/core-edc/issues/100))
- The build resolves dependencies without `mavenLocal()` unless `-PuseMavenLocal` is set, so the dependency verification of a build that includes core-edc sees the same artifacts on every machine ([#116](https://github.com/sovity/core-edc/issues/116))
- Security: stricter validation of credentials, presentations and JSON-LD documents, backported from Eclipse EDC `0.18.2` ([#123](https://github.com/sovity/core-edc/issues/123))
- VCDM 2.0 presentations secured as a plain `vp+jwt` are accepted in addition to enveloped presentations ([#123](https://github.com/sovity/core-edc/issues/123))
- New setting `edc.iam.presentation.binding.required` (default `false`): rejects presentations that are not bound to an audience and an expiry ([#123](https://github.com/sovity/core-edc/issues/123))

#### Compatibility

Credentials, presentations and JSON-LD documents that fail the stricter validation are now rejected, see [#123](https://github.com/sovity/core-edc/issues/123). A VCDM 2.0 presentation must have a `holder` property and be signed with a key of that holder.

---
