import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.firebaseCrashlytics)
    id("kotlin-kapt")
}

val majorVersion = 1
val minorVersion = 0
val patchVersion = 100

fun versionComposition() = listOf(majorVersion, minorVersion, patchVersion)
fun versionName() = versionComposition().joinToString(".")
fun versionCode() = versionComposition().joinToString("").toInt()

android.applicationVariants.all {
    outputs.forEach { output ->
        if (output is BaseVariantOutputImpl) {
            output.outputFileName =
                "RmCalculator-${name}-${versionName}.${output.outputFile.extension}"
        }
    }
}

android {
    namespace = "com.denisdev.rmcalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.denisdev.rmcalculator"
        minSdk = 29
        targetSdk = 34
        versionCode = versionCode()
        versionName = versionName()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    val propertiesFile = rootProject.file("secrets.properties")
    val properties = Properties().apply { load(propertiesFile.inputStream()) }

    signingConfigs {
        maybeCreate("release").apply {
            //Properties from local.properties
            storePassword = properties["STOREPASSWORD"].toString()
            keyPassword = properties["KEYPASSWORD"].toString()
            keyAlias = properties["KEYALIAS"].toString()
            storeFile = file(properties["KEYSTOREPATH"].toString())
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            signingConfig = signingConfigs["release"]
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
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

    implementation(platform(libs.firebaseBOM))
    implementation(libs.firebaseCrashlytics)

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