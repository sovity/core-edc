Migration notes about the problems that have been seen in the `0.11.1.3` -> `0.14.0` forking.

## Refusing to compile when setting a new version

The EDC gradle plugin now seems to be in sync with the EDC version.
Good upgrade, it was previously versioned separately.
But we don't have (and don't want) a fork for this, so we keep the original fork version.

Symptom:

When running `./gradlew check`

```
A problem occurred configuring root project 'connector'.
> Could not resolve all artifacts for configuration ':classpath'.
   > Could not find org.eclipse.edc.autodoc:org.eclipse.edc.autodoc.gradle.plugin:0.14.0.1.
```

Solution: force

`classpath("org.eclipse.edc.autodoc:org.eclipse.edc.autodoc.gradle.plugin:$version")`

to stay at version `0.11.1`

`classpath("org.eclipse.edc.autodoc:org.eclipse.edc.autodoc.gradle.plugin:0.14.0")`

force

`edc-runtime-metamodel = { module = "org.eclipse.edc:runtime-metamodel", version.ref = "edc" }`

to stay on `0.11.1`. Note: `version`, not `version.ref`.

`edc-runtime-metamodel = { module = "org.eclipse.edc:runtime-metamodel", version = "0.14.0" }`

## Wrong plugin version

The EDC uses a custom plugin. In `0.14.0`, it tries to use the same plugin version as the EDC version.

This is fine as long as you control both sides but blows up when forking as there is no `0.14.0.X` version.

Which results in a missing maven dependency warning:

```
Execution failed for task ':core:common:lib:util-lib:compileJava'.
> Could not resolve all files for configuration ':core:common:lib:util-lib:annotationProcessor'.
   > Could not find org.eclipse.edc:autodoc-processor:0.14.0.1.
     Searched in the following locations:
       - https://central.sonatype.com/repository/maven-snapshots/org/eclipse/edc/autodoc-processor/0.14.0.1/autodoc-processor-0.14.0.1.pom
       - file:/home/uuh/.m2/repository/org/eclipse/edc/autodoc-processor/0.14.0.1/autodoc-processor-0.14.0.1.pom
       - https://repo.maven.apache.org/maven2/org/eclipse/edc/autodoc-processor/0.14.0.1/autodoc-processor-0.14.0.1.pom
       - https://oss.sonatype.org/content/repositories/snapshots/org/eclipse/edc/autodoc-processor/0.14.0.1/autodoc-processor-0.14.0.1.pom
     Required by:
         project :core:common:lib:util-lib
```

Fix that by forcing the plugin's dependency to the forked version in the root `build.gradle.kts`:

```kotlin
allprojects {
    // ...
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.eclipse.edc" && requested.name == "autodoc-processor") {
                useVersion("0.14.0")
            }
        }
    }
}
```


## Switch to UUIDv7

Same drill each time. Cherry-picking is not enough because the UUID will be used in more locations.

- Check that the folder structure is still the same:
    - `ls spi/common/core-spi/src/main/java/org/eclipse/edc/spi/` should exist.
- Copy the time-based UUID generator from a previous version.
    - `git checkout v0.7.2.2 -- spi/common/core-spi/src/main/java/org/eclipse/edc/spi/uuid/UuidGenerator.java`
- Add the dependency
    - in `spi/common/core-spi/build.gradle.kts`
        - implementation(libs.uuid)
    - in `gradle/libs.versions.toml`
        - `uuid = { module = "com.fasterxml.uuid:java-uuid-generator", version.ref = "uuid" }`
        - `uuid = "5.1.0"` (May change, check out [maven](https://central.sonatype.com/artifact/com.fasterxml.uuid/java-uuid-generator))
- Replace the UUID usage in the whole project:
    - `UUID.randomUUID()` -> `UuidGenerator.INSTANCE.generate()`
    - `import java.util.UUID;` -> `import org.eclipse.edc.spi.uuid.UuidGenerator;`
    - You probably replaced the 2 lines above. Restore them by copy-pasting what's below and replacing the `_` with `.`:
      - `UUID_randomUUID()` -> `UuidGenerator_INSTANCE_generate()`
      - `import java_util_UUID;` -> `import org_eclipse_edc_spi_uuid_UuidGenerator;`
    - You probably replace the imports in the `UuidGenerator` itself:
      - In `spi/common/core-spi/src/main/java/org/eclipse/edc/spi/uuid/UuidGenerator.java`, use add old import: `java.util.UUID`.
    - Add the module dependency `implementation(project(":spi:common:core-spi"))` where needed:
      - `/home/uuh/dev/sovity/core-edc/core/common/boot/build.gradle.kts`
    - Re-order the headers to checkstyle doesn't complain:
      - With IJ: `Ctrl+Alt+O` on the root.
    - There instances of `UUID.fromString()` that need the import of `java.util.UUID`. Fix them.
- Run `gradle check` and fix.
