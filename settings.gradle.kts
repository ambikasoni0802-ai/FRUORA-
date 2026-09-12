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
        maven { url = uri("https://maven.google.com") }
        maven { url = uri("https://api.razorpay.com/maven") } // not required but harmless
        maven { url = uri("https://jitpack.io") } // required for Razorpay checkout SDK
    }
}

rootProject.name = "FruitApp"
include(":app")
