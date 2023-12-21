import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "dev.tonycode.views.apvdemo"

    defaultConfig {
        applicationId = "dev.tonycode.views.apvdemo"
        versionCode = 4
        versionName = "0.1.1"
        archivesName = "ArcProgressView-demo-v$versionName-build_$versionCode"

        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        resourceConfigurations.addAll(listOf("en"))
        vectorDrawables.useSupportLibrary = true
    }

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    sourceSets {
        all {
            kotlin.srcDir("src/$name/kotlin")
        }
    }
    compileOptions {
        //isCoreLibraryDesugaringEnabled = true  // requires multidex
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    signingConfigs {
        create("release") {
            storeFile = file("../keystore/demo.jks")
            storePassword = "demo12345"
            keyAlias = "key0"
            keyPassword = "demo12345"
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            // https://developer.android.com/studio/build/shrink-code
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    //// Core
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.core.ktx)

    //// UI
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)
    implementation(libs.androidx.constraintlayout)

    implementation(libs.tonycode.stepperviews)
    implementation(libs.dmfs.colorpicker)
    implementation(libs.dmfs.retentionmagic)  // required for "dmfs/color-picker"

    //// library
    implementation(projects.library)
}
