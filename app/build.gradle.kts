@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.denisdev.rmcalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.denisdev.rmcalculator"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        // Enables Jetpack Compose for this module
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
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
    testOptions.unitTests.isIncludeAndroidResources = true
    testOptions.animationsDisabled = true
    
    kotlin {
        jvmToolchain(17)
    }
}

kapt { correctErrorTypes = true }

dependencies {
    implementation(project(":domain"))
    implementation(project(":repository"))
    implementation(libs.appcompat)
    implementation(libs.material)

    implementation(libs.bundles.compose)
    implementation(libs.bundles.navigation)

    implementation(libs.coroutines)
    testImplementation(libs.testCoroutines)
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.junit.ktx)
    implementation(libs.hilt)
    kapt(libs.hiltKapt)

    androidTestImplementation(libs.composeTest)
    debugImplementation(libs.composeTestRule)
}