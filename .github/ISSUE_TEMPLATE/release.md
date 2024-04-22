---
name: Release
about: Create an issue to track a release process.
title: "Release x.x.x"
labels: [ "task/release", "scope/core" ]
assignees: ""
---

# Release

## Work Breakdown

Feel free to edit this release checklist in-progress depending on what tasks need to be done:

- [ ] Check that the forked branch uses the latest revision of the branch version
  from [Eclipse EDC's Connector](https://github.com/eclipse-edc/Connector).
- [ ] Decide a release version. The version must be the eclipse EDC version `X.Y.Z` appended with the sovity fork
- [ ] Update the `version` in [gradle.properties](..%2F..%2Fgradle.properties)
  version `.W`. eg `0.2.1.1`.
- [ ] Update this issue's title to the new version.
- [ ] `release-prep` PR:
    - [ ] Update the CHANGELOG.md.
        - [ ] Add a clean `Unreleased` version.
        - [ ] Add the version to the old section.
        - [ ] Add the current date to the old version.
        - [ ] Check the commit history for commits that might be product-relevant and thus should be added to the
          changelog. Maybe they were forgotten.
        - [ ] Write or review the `Deployment Migration Notes` section, check the commit history for changed / added
          configuration properties.
        - [ ] Write or review a release summary.
        - [ ] Write or review the compatible versions section.
        - [ ] Remove empty sections from the patch notes.
    - [ ] Update the Postman Collection if required.
    - [ ] Merge the `release-prep` PR.
- [ ] Wait for the main branch to be green. You can check the status in
  GH [actions](https://github.com/sovity/edc-extensions/actions).
- [ ] [Create a release](https://github.com/sovity/edc-extensions/releases/new)
    - [ ] In `Choose the tag`, type your new release version in the format `vx.y.z.w` (for instance `v1.2.3.4`) then
      click `+Create new tag vx.y.z.w on release`.
    - [ ] Re-use the changelog section as release description, and the version as title.
- [ ] Check if the pipeline built the release versions in the Actions-Section (or you won't see it).
- [ ] Revisit the changed list of tasks and compare it
  with [.github/ISSUE_TEMPLATE/release.md](https://github.com/sovity/edc-extensions/blob/main/.github/ISSUE_TEMPLATE/release.md).
  Propose changes where it makes sense.
- [ ] Close this issue.
