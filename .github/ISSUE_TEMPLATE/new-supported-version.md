---
name: New supported version
about: Create a new fork from an Eclipse EDC version.
title: "Establish a new supported version"
labels: [ "scope/core" ]
assignees: ""
---

# New Fork

This procedure details how to fork a new version of the core EDC and migrate the changes of the previous version to that new version.

This procedure was updated for the last time for the **0.11.1** fork.
Keep in mind that any new EDC version comes with novelties and that this procedure may need adjustments.

## Work Breakdown

As a general rule, look at the early diffs of the previous forks to see what was added or removed.

In this procedure, we will work with the following repositories

| component ⬇ source ➡ | Eclipse EDC (upstream)                       | sovity (fork)                             |
|----------------------|----------------------------------------------|-------------------------------------------|
| core-edc        | https://github.com/eclipse-edc/Connector     | https://github.com/sovity/core-edc        |
| actions         | https://github.com/eclipse-edc/.github       | https://github.com/sovity/core-edc-github |
| gradle plugin   | https://github.com/eclipse-edc/GradlePlugins |                                           |

### Steps

- [Make it work locally](#make-it-work-locally)
- [Make it work in the CI](#make-it-work-in-the-ci)

### Bonus

Helpful git commands. Just a few tips about the commands that could help. AI can probably help too. It will be mentioned later when these will be useful.

#### Find the common ancestor

For instance to know from which commit on the main branch a pinned version comes from.

`git merge-base A B` finds the last common commit between the references `A` and `B`.

```mermaid
gitGraph
    commit
    commit id: "merge-base(A,B)" tag: "base"
    branch foo
    commit
    commit tag: "B"
    commit
    checkout main
    commit
    commit tag: "A"
    commit
```

#### Show a colored simplified graph

This will hide all the intermediate commits and show only the one that are key to understand the repo's tree structure: tags, branches with author, date, hash.

`git log --graph --simplify-by-decoration --abbrev-commit --decorate --format=format:'%C(bold blue)%h%C(reset) - %C(bold green)(%ar)%C(reset) %C(white)%s%C(reset) %C(dim white)- %an%C(reset)%C(bold yellow)%d%C(reset)' --all`

#### Show a different history

Shows the changes for a specific file that is on another branch.

`git log ..VERSION -- FILE` or `tig ..VERSION -- FILE`

e.g.

```shell
git log ..v0.7.2.2 -- extensions/common/iam/identity-trust/identity-trust-core/src/main/java/org/eclipse/edc/iam/identitytrust/core/IdentityTrustTransformExtension.java
```

```
commit fcb9c94c97dfe4f93de6335b3fd842fe8cbe7b11
Author: Christophe Loiseau <christophe.loiseau@sovity.de>
Date:   Tue Jan 28 16:30:48 2025 +0100

    fix: Remove file separator to access classpath (#27)
```

#### Cherry-picking

Select a change that was made in some commit with `git cherry-pick`

```shell
git cherry-pick fcb9c94c97dfe4f93de6335b3fd842fe8cbe7b11
```

```
Auto-merging CHANGELOG.md
CONFLICT (content): Merge conflict in CHANGELOG.md
Auto-merging docs/developer/fork/0.7.2.X.md
error: could not apply fcb9c94c9... fix: Remove file separator to access classpath (#27)
hint: After resolving the conflicts, mark them with
hint: "git add/rm <pathspec>", then run
hint: "git cherry-pick --continue".
hint: You can instead skip this commit with "git cherry-pick --skip".
hint: To abort and get back to the state before "git cherry-pick",
hint: run "git cherry-pick --abort".
hint: Disable this message with "git config set advice.mergeConflict false"
```

Alternatively, you may try a diff+apply+merge between the commits of the previous fork may be enough to port the change `git apply <(git diff vP1.P2... vP1.P2...)`.

#### Fix the picking conflict

Open the merge diff in your prefered tool

```shell
git mergetool
```

#### Preview older revision

See what a folder looked like without checking it out

`git ls-tree REVISION -- path/to/thing`

e.g.

`git ls-tree v0.7.2.2 -- .github/workflows/`

```
100644 blob 8c608ecf4a50801383ae4c4cc2bfbc7a102de43d	.github/workflows/bump-version.yaml
100644 blob b01c4ee4fc90ee8b21b56b6d197fad5da52c2c3e	.github/workflows/codeql-analysis.yml
100644 blob 97b0a49f8bb20dc7e2f2a243fbe72091f967ca30	.github/workflows/dependency-check.yml
100644 blob 745094a5b41c0791cffbf14353be2bd3b04168b2	.github/workflows/prepare-release.yml
100644 blob 16975738f31c76af3eac2ddf44a5f86b390b4e46	.github/workflows/publish-autodoc.yml
100644 blob 10026258b05c42b7cf40142c7aa0dff885b5c577	.github/workflows/publish-context.yaml
100644 blob 4b2b84f1ad9ed7bcff7a27dd52d338d96d68e698	.github/workflows/publish-openapi-ui.yml
100644 blob 79cdaa9302521db18649f29a004aedc72eb14f09	.github/workflows/scan-pull-request.yaml
100644 blob 73de289ad0121fc25455a95f482412f17f623f88	.github/workflows/stale-bot.yml
100644 blob aec1c09b1d5dc558811cff69cf73a369b186e9da	.github/workflows/triage-issue.yml
100644 blob 2b6e3710e6feddc8fabf4067e5fde3bf4fa77bdd	.github/workflows/trigger_snapshot.yml
100644 blob 5159efdc6b9c1fee67eb4bdf66c03ea37a4333e2	.github/workflows/verify-openapi.yml
100644 blob 14251b11131019388e2e4e7b76a2de81d79b798e	.github/workflows/verify.yaml
```

#### Interactive git

https://jonas.github.io/tig/

## Configuration

This procedure uses templates for the version.
It is advised to replace all the template strings with the version that you want to migrate for more clarity

- [ ] Identify the versions that you migrate
- `NEXT` is the `upstream` version that you want to fork from. e.g **0.11.1**. It is made of 3 parts following parts:
    - `N1` is `0`
    - `N2` is `11`
    - `N3` is `1`
- `PREVIOUS` is the version that you will copy the fork changes from. e.g. **0.7.2.2**. It is made of 4 parts:
    - `P1` is `0`
    - `P2` is `7`
    - `P3` is `2`
    - `P4` is `2`

This documentation uses the `N` and `P` placeholders. You can replace them with the real digits to read version more easily:

- [ ] (Optional) Copy this markdown file locally and change the version strings.
    - Copy the command below and update the 3rd part of each replacement operation with the appropriate version with the version part number that you identified here above. e.g. `s/P2/7/g` : the `7` should be another number now. The rest remains identical.
    - `sed --in-place=.backup 's/N1/0/g; s/N2/11/g; s/N3/1/g; s/P1/0/g; s/P2/7/g; s/P3/2/g; s/P4/2/g' /path/to/new-supported-version.md`


# Let's fork!

## Set up

Each version that needs forking has its own branch in the sovity fork.

Here we have an old fork that we want to get the features from named `vP1.P2.P3.x` and a new fork named `vN1.N2.N3.x` into which we want to port the changes made in `vP1.P2.P3.x` up to `vP1.P2.P3.P4`

Note: the branch names here are to be seen as references, so they should really be `remotes/...`.

```mermaid
---
title: core-edc
---
%%{init: { 'logLevel': 'debug', 'gitGraph': {'mainBranchName': 'upstream/main' }} }%%
gitGraph
    commit tag: "v0.0.1"
    commit tag: "vP1.P2.0"
    branch upstream/bugfix/P1.P2.P3
    checkout upstream/bugfix/P1.P2.P3
    commit
    commit tag: "vP1.P2.P3"
    branch fork/sovity/P1.P2.P3
    commit
    commit
    commit tag: "vP1.P2.P3.P4"
    checkout upstream/bugfix/P1.P2.P3
    checkout upstream/main
    commit
    commit
    commit tag: "vN1.N2.0"
    branch upstream/bugfix/N1.N2.N3
    checkout upstream/bugfix/N1.N2.N3
    commit tag: "vN1.N2.1"
    commit tag: "vN1.N2.2"
    commit tag: "vN1.N2.N3" id: "to fork"
    branch fork/sovity/N1.N2.N3
    commit id: "new fork"
    commit tag: "vN1.N2.N3.1"
    checkout upstream/main
    commit
    commit
```

> [!IMPORTANT]
> Note: the upstream branch name may vary:
> - `remotes/upstream/release/0.10.0`
> - `remotes/upstream/bugfix/0.7.2`
> - `remotes/upstream/v0.6.5`
> - or just nothing, then use the tag to start a branch.

## Make it work locally

This section describes the steps to prepare the fork and make it run locally.

### Setup a repository

- [ ] Clone the `fork` `core-edc`
    - `git clone --origin fork git@github.com:sovity/core-edc.git`
- [ ] Add the upstream repository
    - `git remote add upstream git@github.com:eclipse-edc/Connector.git`
- [ ] Fetch the upstream tags
    - `git fetch --all --tags`

### Setup example

Your repository now looks like this:

`git remote -v`

```
fork	git@github.com:sovity/core-edc.git (fetch)
fork	git@github.com:sovity/core-edc.git (push)
upstream	git@github.com:eclipse-edc/Connector.git (fetch)
upstream	git@github.com:eclipse-edc/Connector.git (push)
```

`git branch -a`

`# ...` are comments to describe the situation and no part of the output.

```
remotes/fork/default               # the replacement for upstream/main use in the sovity fork
remotes/fork/main                  # not used in the fork, deplaced by main
remotes/fork/sovity/O12.O3         # the previous fork branch
remotes/upstream/bugfix/N1.N2.N3   # the branch we want to work
... more                           # and many other branches
```

`git tag -l`

The output will vary based on the version that have already been forked. Here is the output where **0.2.1** exists and **0.7.2** will be forked.

`# ...` are comments to describe the situation and no part of the output.

```
v0.2.1    # the upstream previously forked version
v0.2.1.2  # v0.2.1.1 is missing because it was never published correctly and was replaced by 0.2.1.2
v0.2.1.3
v0.2.1.4  # the latest sovity fork version of v0.2.1
v0.7.2    # the version we want to fork
... more
```

### Establish the branches

- [ ] In the fork, find the tag `vN1.N2.N3` of the branch that you want to fork from.
  - `git tag --list`
- [ ] Create a new branch `sovity/N1.N2.N3` from that tag.
    - `git checkout -b sovity/N1.N2.N3 vN1.N2.N3`
- [ ] Push this branch to the fork. It will be our new `main` for this fork version.
    - `git push fork sovity/N1.N2.N3:sovity/N1.N2.N3`
- [ ] Checkout a new branch `N1.N2.N3-fork-setup` on the same commit as `sovity/N1.N2.N3.x`.
    - `git checkout sovity/N1.N2.N3` (Optional, should already be on the correct branch)
    - `git checkout -b N1.N2.N3-fork-setup`
- [ ] In `gradle.properties`, set the correct branch version: `N1.N2.N3.1`.

`git remote show fork`

```
* remote fork
  Fetch URL: git@github.com:sovity/core-edc.git
  Push  URL: git@github.com:sovity/core-edc.git
  HEAD branch: doesntMatter
  Remote branches:
    sovity/P1.P2.P3        tracked
    sovity/N1.N2.N3         tracked
    ... more
  Local branches configured for 'git pull':
    sovity/N1.N2.N3 merges with remote sovity/N1.N2.N3
    ... more
  Local refs configured for 'git push':
    sovity/N1.N2.N3 pushes to sovity/N1.N2.N3 (up to date)
    ... more
```

### Update the project

- [ ] Before editing the code, check that your IDE has the correct editor settings (imports single classes, indents, ...). There is an `resources/edc-codestyle.editorconfig` file in the project.
- [ ] Run the tests locally and check that they all work. If not:
    - [ ] Try to fix the test if it's relevant for the changes that we want to do in the fork.
    - [ ] Consider adding `@Disabled` on the tests that we will likely not use or that would be hard to fix.
    - e.g. outdated certificates would be hard to regenerate and fix and could be disabled as long as our fork doesn't touch the parts of the code that use them.
- [ ] Add a changelog `CHANGELOG.md` and the docs `docs/developer/fork/*` from the previous version `P1.P2.P3.P4`
    - `git checkout vP1.P2.P3.P4 -- CHANGELOG.md docs/developer/fork/\*`
    - [ ] Remove the previous releases from the `CHANGELOG.md` and keep the template.
    - [ ] Rename the old doc file `docs/developer/fork/vP1.P2.P3.P4.md`
- [ ] Update the CI
  - [ ] Remove the unwanted github CI actions
    - publishing to the core EDC repo
    - discord notification
    - ...
  - [ ] Add missing details
    - Javadoc, see main build.gradle.kts from a previous version: `java { withSourcesJar() }`
    - Add sovity publishing from `.github/workflows/verify.yaml`, tasks: `Publish-Artifacts` and `Promote-Artifacts`
      - Copy the tasks
      - Check that the task's dependencies still exists
      - Maybe a new dependency should be added?
- [ ] Add the publishing elements from the previous version in the top-level `gradle.build.kts`
  - `maven-publish`
  - Azure repos
- [ ] Add a way to read the gradle plugin dependencies in the `settings.kts`
- [ ] Here would be a great moment to commit the above changes as `Migration baseline` as we're now going in uncharted territories... 😉
- [ ] For each of the former change in the `CHANGELOG.md`
    - Check the bonus section above to find helpful commands. 
    - [ ] Evaluate whether the change needs to be ported
        - Reasons for not porting may include
            - The code that the fork used no longer exist
            - The change or an equivalent was implemented upstream in a version before or including `N1.N2.N3`.
            - The change is no longer desired
            - ...
    - [ ] Port the change if needed.
    - [ ] Document the change in the `CHANGELOG.md`.
    - [ ] Detail the change in the current documentation `docs/developer/fork/vN1.N2.N3.X.md`. Check out the previous fork documentation for how to structure the document.
- [ ] Implement the new changes that apply to this fork.
- [ ] Delete the previous `docs/developer/fork/vP1.P2.P3.P4.md` notes after each element is either migrated or discarded.

## Make it work in the CI

### Find the correct GitHub action.

The EDC, as of `0.7.x`, uses actions that are located in a separate repository. That repository [is forked](https://github.com/sovity/core-edc-github).

Because the EDC used the `@main` version in its CI, it is certain that the scripts that are the current `@main` ones were not the ones that were originally used for the Eclipse EDC release you're forking, and it is likely that the current scripts will fail, be renamed, have a different behaviour, ...

In a best-effort attempt to restore the CI, we need to pin the versions that were used and maybe update them to work on the current GitHub.

If we pin down the old version, a lot of updates may be needed.

If we pin down the new version, scripts may be missing.

The strategy here is to pin down the latest from main and fix the missing scripts individually and later update them.

## Set up a repository

- [ ] Clone the `fork` `actions` repository
    - `git clone --origin fork git@github.com:sovity/core-edc-github.git`
- [ ] Add the `upstream` `actions` repository
    - `git remote add upstream git@github.com:eclipse-edc/.github.git`
- [ ] Update
    - `git fetch --all --tags`

### Result

`git remote -v`

```
fork	git@github.com:sovity/core-edc-github.git (fetch)
fork	git@github.com:sovity/core-edc-github.git (push)
upstream	git@github.com:eclipse-edc/.github.git (fetch)
upstream	git@github.com:eclipse-edc/.github.git (push)
```

### Overview

Here is the example forking scenario.

```mermaid
---
title: actions
---
%%{init: { 'logLevel': 'debug', 'gitGraph': {'mainBranchName': 'upstream/main' }} }%%
gitGraph
    commit id: "obsolete"
    commit
    commit id: "core-edc fork tag date" tag: "N1.N2.N3_LASTOK_1"
    branch fork/pinned/N1.N2.N3_LASTOK
    commit tag: "N1.N2.N3_LASTOK_2"
    commit tag: "N1.N2.N3_LASTOK_3"
    checkout upstream/main
    commit
    commit
    commit
    commit id: "latest" tag: "N1.N2.N3_LATEST_1"
    branch fork/pinned/N1.N2.N3_LATEST
    commit tag: "N1.N2.N3_LATEST_2"
    commit tag: "N1.N2.N3_LATEST_3"
```

`LATEST` and `LASTOK` are iso dates `YYYY-MM-DD`

The naming scheme for the tagged and updated actions is `fork version` _ `date at which the action was working` _ `-version`.

e.g. `N1.N2.N3_2025-06-07_1` for the latest main commit.
e.g. `N1.N2.N3_2022.03.04_3` for the commit that was used during releasing, 3rd revision. Here the `_3` is related to the `actions` `fork` scripts and is independent of the 4th digit in the `core-edc` `fork`.
You will need potentially many revisions as you will need to push the tag each time you make a change to let the CI use that version, then retry if it failed.

This was tested to work quite well in version **0.7.2** and is detail below.

For the cases that can't be covered with the best effort approach, a fork strategy is detailed below,
see [Lost action](#lost-action)

If you need more than 1 branch for a single date, be creative in the name, either use DATE+TIME or DATE+suffix. In any case, that scenario is unlikely to happen.

#### Finding LATEST and LASTOK

`LATEST` is the last commit on the `main` `upstream` branch.

- [ ] Replace `LATEST` in this file by the date you find. The date part should be enough.
- `git show --no-patch --format=%ci upstream/main`
- `sed --in-place 's/LATEST/2025-06-07/g' fork.md`

`LASTOK` is optional and will be defined later.

### Pin the action versions

- Pin the latest version
    - [ ] Create a branch to track the `LATEST` `main` commit from `upstream`
        - `git checkout upstream/main`
        - `git checkout -b pinned/N1.N2.N3_LATEST`
    - [ ] Push the branch to the fork
        - `git push fork`
    - [ ] Tag the latest commit in the action set with the label `core edc fork branch` _ `today` _ `1`
        - `git tag N1.N2.N3_LATEST_1 pinned/N1.N2.N3_LATEST`
    - [ ] Push that new tag to the sovity core edc github fork
        - `git push fork tag N1.N2.N3_LATEST_1`
    - [ ] Change all the action's `@main` version for the `N1.N2.N3_LATEST_1` version
    - [ ] Change all the `eclipse-edc/.github` for `sovity/core-edc-github`
        - ❌ `eclipse-edc/.github/.github/workflows/task.yml@main`
        - ✔ `sovity/core-edc-github/.github/workflows/task.yml@N1.N2.N3_LATEST_1`
        - [ ] IDE: replace `eclipse-edc/.github/(.*)@main` with `sovity/core-edc-github/$1@N1.N2.N3_LATEST_1`
    - [ ] Run the CI via a PR and check for errors

From here we have:

- An action works -> done
- An action doesn't work
    - Is the action useless? -> remove it
        - [ ] Discord webhook
        - [ ] First interaction
        - [ ] Others...
        - [ ] Disable failing tests that don't matter
            - [ ] failing because of outdated certificates
    - An action has an obsolete dependency / needs adjustments -> see [Adjust the pinned main](#adjust-the-pinned-main)
    - An action is missing: we need to find it in the history -> see [Lost action](#lost-action)
    - Something else that was not encountered yet: be creative and update this guide after you found the solution.

### Adjust the pinned main

- Update the code in the `pinned/N1.N2.N3-LATEST` branch
- Commit and tag your new version as `N1.N2.N3_LATEST_(N+1)` in the `actions` repo
- Push the tag to the sovity fork repo
- Update all the `@N1.N2.N3_LATEST_N` to `N1.N2.N3_LATEST_(N+1)`
- Repeat until fixed

### Lost action

This part describes how to find where a missing action was and how to make it work again.

- [ ] Identify the date `LASTOK` when the `core-edc` version was released either with:
    - [ ] Alternative 1: locate the file in the git history.
        - `git log --oneline --follow -- '.github/actions/THEACTIONNAME'`
    - [ ] Alternative 2: Use the tag date.
        - `git show --no-patch --format=%ci vN1.N2.N3` in the `core-edc` repo. e.g `2022-03-04`
        - Note: the time may be important. By specifying only the date, we will get all the commit of that day until midnight.
- [ ] (Optional) Replace `LASTOK` with the date you found.
- [ ] Find in the `fork` `action` repository the *commit* on the main branch that happened right before the time
  the tagged commit in the core EDC was created.
    - `git log --date=iso -n LINES --before="LASTOK"`
    - e.g. `git log --date=iso -n 2 --before="2022-03-04"`
    - Note: the time may be important, increase the value of `LINES` to show more than 1 line.
- [ ] Create a new branch `pinned/P1.P2.P3_LASTOK` from this older commit
- [ ] Push this branch to the fork
- [ ] Tag this new version as `P1.P2.P3_LASTOK_1` e.g. `P1.P2.P2_2022-03-04_1`
- [ ] Push this tag to the `actions` fork
    - e.g. `git push fork tag P1.P2.P3_LASTOK_1`
- [ ] Change the missing dependency versions in the `core-edc` `fork` from the pinned latest to that new version.
- [ ] Run in the CI and go to [adjusting](#adjust-the-pinned-main) if needs be, but this time using this new branch.

## Publishing

- [ ] Steal the publishing and promoting tasks from a previous fork
    - `0.7.2.x`: publishing and promoting
        - `git checkout sovity/0.7.2 -- .github/workflows/verify.yml build.gradle.kts`
    - `0.2.1.x`: publishing
        - `git checkout sovity/0.2.1 -- .github/workflows/publish.yml build.gradle.kts`
- [ ] Adapt the publishing and promoting actions to the new `core-edc`'s actions.
    - [ ] Branches starting with `sovity/` must be published on the `AzureTest` instance.
    - [ ] Tags starting with `v` must be published on the `Azure` (non-test) instance.
- [ ] Disable the jar signing
    - `v0.2.1` / `v0.7.2`
        - The signing is managed in the EDC gradle plugin.
        - [ ] Add `skip.signing=true` to the `gradle.properties`
- [ ] Test the publishing on the Azure Test instance, by publishing a branch starting by `sovity/`.
    - [ ] Create a PR with some changes to target the `core-edc` fork on a `sovity/` branch and merge it.
    - [ ] Check that the artifacts have been deployed to the [Azure Test repo](https://dev.azure.com/sovity/Test/_artifacts/feed/test)
- [ ] Update the `release.md` on the default branch.
- [ ] You can now create PRs against `sovity/N1.N2.N3` with your desired changes, see [the patch procedure](https://github.com/sovity/core-edc/issues/new?template=new-patch.md)

### Finalization

- [ ] Update this procedure with new hints after forking an EDC version.
- [ ] Update the default branch's README with a new entry for this fork.
