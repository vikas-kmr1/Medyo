pluginManagement {
   // includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "Medyo"
include(":app")
include(":core:ui")
include(":core:design-system")
//include(":build-logic:convention")
include(":core:data")
include(":core:domain")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:utils")
include(":core:notification")
include(":core:navigation")
include(":core:network")
include(":core:work-manager")
include(":core:ai-logic")
include(":logger:api")
include(":logger:impl")
include(":feature:scanner:api")
include(":feature:scanner:impl")
