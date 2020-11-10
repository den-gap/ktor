pluginManagement {
    repositories {
        mavenCentral()
        maven(url = "https://kotlin.bintray.com/kotlinx")
        google()
        gradlePluginPortal()
        jcenter()
    }
    resolutionStrategy {
        val kotlin_version: String by settings
        val atomicfu_version: String by settings
        val benchmarks_version: String by settings

        eachPlugin {
            when (requested.id.id) {
                "kotlinx-atomicfu" -> useModule("org.jetbrains.kotlinx:atomicfu-gradle-plugin:$atomicfu_version")
            }
        }

        plugins {
            kotlin("multiplatform") version kotlin_version
            kotlin("plugin.serialization") version kotlin_version
            kotlin("plugin.allopen") version kotlin_version
            id("kotlinx-atomicfu") version atomicfu_version
            id("kotlinx.benchmark") version benchmarks_version
        }
    }
}

rootProject.name = "ktor"
enableFeaturePreview("GRADLE_METADATA")

val native_targets_enabled = true
val currentJdk = 11
//ext.native_targets_enabled = properties["disable_native_targets"] == null

// copied from versions.gradle
//val versionComponents = System.getProperty("java.version")?.split("\\.")?.take(2)?.findAll { !it.isBlank() }?.collect { Integer.parseInt(it) } ?: [1]

//ext.currentJdk = versionComponents[0] == 1 ? versionComponents[1] : versionComponents[0]

include(":ktor-server:ktor-server-core")
include(":ktor-server:ktor-server-tests")
include(":ktor-server:ktor-server-host-common")
include(":ktor-server:ktor-server-test-host")
include(":ktor-server:ktor-server-test-suites")
include(":ktor-server:ktor-server-jetty")
include(":ktor-server:ktor-server-jetty:ktor-server-jetty-test-http2")
include(":ktor-server:ktor-server-servlet")
include(":ktor-server:ktor-server-tomcat")
include(":ktor-server:ktor-server-netty")
include(":ktor-server:ktor-server-cio")
include(":ktor-server:ktor-server-benchmarks")
include(":ktor-server")
include(":ktor-client")
include(":ktor-client:ktor-client-core")
include(":ktor-client:ktor-client-tests")
include(":ktor-client:ktor-client-apache")
include(":ktor-client:ktor-client-android")
include(":ktor-client:ktor-client-cio")
if (native_targets_enabled) {
    include(":ktor-client:ktor-client-curl")
    include(":ktor-client:ktor-client-ios")
}
if (currentJdk >= 11) {
    include(":ktor-client:ktor-client-java")
}
include(":ktor-client:ktor-client-jetty")
include(":ktor-client:ktor-client-js")
include(":ktor-client:ktor-client-mock")
include(":ktor-client:ktor-client-okhttp")
//include(":ktor-client:ktor-client-benchmarks")
include(":ktor-client:ktor-client-features")
include(":ktor-client:ktor-client-features:ktor-client-json")
include(":ktor-client:ktor-client-features:ktor-client-json:ktor-client-json-tests")
include(":ktor-client:ktor-client-features:ktor-client-json:ktor-client-gson")
include(":ktor-client:ktor-client-features:ktor-client-json:ktor-client-jackson")
include(":ktor-client:ktor-client-features:ktor-client-json:ktor-client-serialization")
include(":ktor-client:ktor-client-features:ktor-client-auth")
include(":ktor-client:ktor-client-features:ktor-client-auth-basic")
include(":ktor-client:ktor-client-features:ktor-client-logging")
include(":ktor-client:ktor-client-features:ktor-client-encoding")
include(":ktor-client:ktor-client-features:ktor-client-websockets")
//include ":ktor-client:ktor-client-features:ktor-client-tracing"
//include ":ktor-client:ktor-client-features:ktor-client-tracing:ktor-client-tracing-stetho"
include(":ktor-features:ktor-freemarker")
include(":ktor-features:ktor-mustache")
include(":ktor-features:ktor-thymeleaf")
include(":ktor-features:ktor-velocity")
include(":ktor-features:ktor-pebble")
include(":ktor-features:ktor-gson")
include(":ktor-features:ktor-jackson")
include(":ktor-features:ktor-metrics")
include(":ktor-features:ktor-metrics-micrometer")
include(":ktor-features:ktor-server-sessions")
include(":ktor-features:ktor-locations")
include(":ktor-features:ktor-websockets")
include(":ktor-features:ktor-html-builder")
include(":ktor-features:ktor-auth")
include(":ktor-features:ktor-auth-ldap")
include(":ktor-features:ktor-auth-jwt")
include(":ktor-features:ktor-webjars")
include(":ktor-features:ktor-serialization")
include(":ktor-features")
include(":ktor-http")
include(":ktor-http:ktor-http-cio")
include(":ktor-io")
include(":ktor-utils")
include(":ktor-network")
include(":ktor-network:ktor-network-tls")
include(":ktor-network:ktor-network-tls:ktor-network-tls-certificates")
include(":ktor-bom")
include(":ktor-test-dispatcher")
