---
name: New Workaround
about: How to add a workaround on an existing fork.
title: "Add a workaround"
labels: [ "scope/core" ]
assignees: ""
---

# About

Describes how to add a patch on a forked Eclipse EDC.

- [ ] Identify the version to patch
  - e.g VERSION=`0.7.2`
- [ ] Checkout that version from the fork
  - `git checkout sovity/VERSION`
- [ ] Checkout a new branch
  - `git checkout my-crucial-patch`
- [ ] Implement the workaround as needed
- [ ] Open a PR with
  - base repository: `sovity/core-edc`
  - base: `sovity/VERSION`
  - compare: `my-crucial-patch`
