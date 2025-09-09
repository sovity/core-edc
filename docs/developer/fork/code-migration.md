Migration notes about the problems that have been seen in the `0.7.2.2` -> `0.11.1` forking.

# Code migration

## Refusing to compile when setting a new version

The EDC gradle plugin now seems to be in sync with the EDC version. Good upgrade, it was previously versioned separately. But we don't have (and don't want) a fork for this, so we keep the original fork version.

Symptom:

```
A problem occurred configuring root project 'connector'.
> Could not resolve all artifacts for configuration ':classpath'.
> Could not find org.eclipse.edc.edc-build:org.eclipse.edc.edc-build.gradle.plugin:0.11.1.1.
```

Solution: force

`classpath("org.eclipse.edc.edc-build:org.eclipse.edc.edc-build.gradle.plugin:$version")`

to stay at version `0.11.1`

`classpath("org.eclipse.edc.edc-build:org.eclipse.edc.edc-build.gradle.plugin:0.11.1")`


force

`edc-runtime-metamodel = { module = "org.eclipse.edc:runtime-metamodel", version.ref = "edc" }`

to stay on `0.11.1`. Note: `version`, not `version.ref`.

`edc-runtime-metamodel = { module = "org.eclipse.edc:runtime-metamodel", version = "0.11.1" }`

## Wrong plugin version

The EDC uses a custom plugin. In `0.11.1`, it tries to use the same plugin version as the EDC version.

This is fine as long as you control both sides but blows up when forking as there is no `0.11.1.X` version.

Which results in a missing maven dependency warning:

```
> Task :spi:common:boot-spi:compileJava FAILED
Resource missing. [HTTP GET: https://oss.sonatype.org/content/repositories/snapshots/org/eclipse/edc/autodoc-processor/0.11.1.1/autodoc-processor-0.11.1.1.pom]
Resource missing. [HTTP GET: https://repo.maven.apache.org/maven2/org/eclipse/edc/autodoc-processor/0.11.1.1/autodoc-processor-0.11.1.1.pom]

FAILURE: Build completed with 5 failures.

1: Task failed with an exception.
-----------
* What went wrong:
Execution failed for task ':core:common:lib:util-lib:compileJava'.
> Could not resolve all files for configuration ':core:common:lib:util-lib:annotationProcessor'.
   > Could not resolve all dependencies for configuration ':core:common:lib:util-lib:annotationProcessor'.
      > Could not find org.eclipse.edc:autodoc-processor:0.11.1.1.
        Searched in the following locations:
          - file:/home/uuh/.m2/repository/org/eclipse/edc/autodoc-processor/0.11.1.1/autodoc-processor-0.11.1.1.pom
          - https://oss.sonatype.org/content/repositories/snapshots/org/eclipse/edc/autodoc-processor/0.11.1.1/autodoc-processor-0.11.1.1.pom
          - https://repo.maven.apache.org/maven2/org/eclipse/edc/autodoc-processor/0.11.1.1/autodoc-processor-0.11.1.1.pom
        Required by:
            project :core:common:lib:util-lib
```

Fix that by forcing the plugin's dependency to the forked version:

```kotlin
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.eclipse.edc" && requested.name == "autodoc-processor") {
            useVersion("0.11.1")
        }
    }
}
```



## Switch to UUIDv7

Same drill each time. Cherry-picking is not enough because the UUID will be used it more locations.

- Check that the folder structure is still the same:
    - `ls spi/common/core-spi/src/main/java/org/eclipse/edc/spi/`
- Copy the time-based UUID generator from a previous version.
    - `git checkout v0.7.2.2 -- spi/common/core-spi/src/main/java/org/eclipse/edc/spi/uuid/UuidGenerator.java`
- Add the dependency
    - in `spi/common/core-spi/build.gradle.kts`
        - implementation(libs.uuid)
    - in `gradle/libs.versions.toml`
        - `uuid = { module = "com.fasterxml.uuid:java-uuid-generator", version.ref = "uuid" }`
        - `uuid = "5.1.0"` (May change)
- Replace in the whole project:
    - `UUID.randomUUID()` -> `UuidGenerator.INSTANCE.generate()`
    - `import java.util.UUID;` -> `import org.eclipse.edc.spi.uuid.UuidGenerator;`


# CI migration

Publishing (`gradle publishToMavenLocal`) results in

```
* What went wrong:
Execution failed for task ':core:control-plane:control-plane-aggregate-services:signMavenPublication'.
> Cannot perform signing task ':core:control-plane:control-plane-aggregate-services:signMavenPublication' because it has no configured signatory
```

In the root `build.gradle.kts`, add

```kotlin
allprojects {
    // ...
    tasks.withType(Sign::class.java).configureEach {
        onlyIf { false }
    }
}
```
