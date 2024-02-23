pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Until Then"
include(":app")
include(":feature:countdown")
include(":core:design")
include(":core:common")
include(":data:countdown")
include(":feature:widget")
include(":test:app")
