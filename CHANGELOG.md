# Changelog

## [0.17.0.x] - UNRELEASED

### Overview

#### Changes

- The build resolves dependencies without `mavenLocal()` unless `-PuseMavenLocal` is set, so the dependency verification of a build that includes core-edc sees the same artifacts on every machine ([#116](https://github.com/sovity/core-edc/issues/116))

#### Compatibility

Should be compatible without any modification with a `0.17.0` version.

---