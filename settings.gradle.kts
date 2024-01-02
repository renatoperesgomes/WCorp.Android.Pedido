pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven{ url = uri("https://github.com/pagseguro/PlugPagServiceWrapper/raw/master")}
    }
}

rootProject.name = "W_Corp.Android.Pedido"
include(":app")
