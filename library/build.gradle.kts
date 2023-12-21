plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    `maven-publish`
}

android {
    namespace = "dev.tonycode.views"

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        resourceConfigurations.addAll(listOf("en"))
        consumerProguardFile("consumer-rules.pro")
    }

    compileSdk = libs.versions.compileSdk.get().toInt()
    buildToolsVersion = libs.versions.buildTools.get()

    sourceSets {
        all {
            kotlin.srcDir("src/$name/kotlin")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
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
            isMinifyEnabled = false
        }

        release {
            signingConfig = signingConfigs.getByName("release")
            // https://developer.android.com/studio/build/shrink-code
            //isMinifyEnabled = true
            //proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    //// Core
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.androidx.core.ktx)
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.tonycode"
            artifactId = rootProject.name
            version = "0.2.0-SNAPSHOT"

            configurePom()

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

fun MavenPublication.configurePom() {
    pom {
        name = rootProject.name
        description = "Android ui-component that displays progress as an Arc"
        url = "https://github.com/tonycode/ArcProgressView"

        licenses {
            license {
                name = "MIT License"
                url = "https://github.com/tonycode/ArcProgressView/blob/main/LICENSE"
            }
        }

        developers {
            developer {
                id = "tonycode"
                name = "Anton Vasilev"
                email = "opensource@tonycode.dev"
                organization = "tonycode"
                organizationUrl = "https://tonycode.dev/"
            }
        }

        scm {
            connection = "scm:git:git://github.com/tonycode/ArcProgressView.git"
            developerConnection = "scm:git:git@github.com:tonycode/ArcProgressView.git"
            url = "https://github.com/tonycode/ArcProgressView"
        }
    }
}
