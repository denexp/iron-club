@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.denisdev.repository"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlin {
        jvmToolchain(17)
    }
}

dependencies {
    implementation(libs.hilt)
    kapt(libs.hiltKapt)
    implementation(project(":domain"))
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    implementation(libs.coroutines)
    testImplementation(libs.testCoroutines)
    androidTestImplementation(libs.testCoroutines)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.junit.ktx)
}