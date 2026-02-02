import com.android.build.api.dsl.CompileSdkVersion

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.maven.publish)
}

android {
    namespace = "fr.beltech.networklibrary"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            //withJavadocJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    api(libs.ktor.client.core)
    api(libs.ktor.client.serialization)
    api(libs.ktor.client.android)
    api(libs.ktor.serialization.kotlinx.json)
    api(libs.ktor.client.content.negotiation)
    api(libs.ktor.client.logging.jvm)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.Fredbellano"
            artifactId = "android-network-library"
            version = "1.0.4"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
    repositories {
        maven {
            url = uri("https://jitpack.io")
        }
    }
}
