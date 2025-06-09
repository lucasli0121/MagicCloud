plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.magiccloud.android"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.magiccloud.android"
        minSdk = 29
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/io.netty.versions.properties")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.components.resources)
    implementation(compose.preview)
    implementation(compose.components.uiToolingPreview)
    implementation(projects.shared)
    implementation(libs.bundles.google.permissions)
    implementation(libs.bundles.android.main)
    implementation(libs.bundles.kable)
    implementation(libs.bundles.koin)
    implementation(libs.koin.android)
    implementation(libs.napier)
    implementation(libs.bundles.androidx.test)
}