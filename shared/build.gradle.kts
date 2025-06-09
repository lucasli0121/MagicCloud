import com.android.build.api.dsl.Packaging
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    // dagger-hilt不支持跨平台不用了
//    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.devtools.ksp)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }

    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    //noinspection WrongGradleMethod
    ).forEach { target ->
        target.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {

        val commonMain by getting  {
            dependencies {
                //put your multiplatform dependencies here
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(libs.bundles.kable)
                implementation(libs.bundles.koin)
                implementation(libs.napier)
                implementation(libs.uuid)
                implementation(libs.bundles.netty)
                implementation(libs.kotlin.test)
            }
        }
        val androidMain by getting {
            dependsOn(commonMain)
            dependencies {
                implementation(libs.bundles.android.main)
                implementation(libs.bundles.google.permissions)
                implementation(compose.components.uiToolingPreview)
            }
        }
        val iosArm64Main by getting {
            dependsOn(commonMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(commonMain)
        }
        val iosX64Main by getting {
            dependsOn(commonMain)
        }
    }
}

configurations.all {
    resolutionStrategy {
        // Force all Netty components to use the same version
        force("io.netty:netty-buffer:4.2.0.RC1")
        force("io.netty:netty-codec:4.2.0.RC1")
        force("io.netty:netty-transport:4.2.0.RC1")
        force("io.netty:netty-resolver:4.2.0.RC1")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()

    // Exclude duplicate entries for test tasks
    jvmArgs = listOf(
        "--add-opens=java.base/java.util=ALL-UNNAMED",
        "--add-opens=java.base/java.lang=ALL-UNNAMED"
    )
}

android {
    namespace = "com.magiccloud"
    compileSdk = 35
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            excludes.add("META-INF/INDEX.LIST")
            excludes.add("META-INF/io.netty.versions.properties")
            excludes.add("META-INF/*")
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}
dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}
