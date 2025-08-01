
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}
android {
    buildFeatures.buildConfig = true
    namespace = "com.allfreeapps.theballgame"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.allfreeapps.theballgame"
        minSdk = 30
        targetSdk = 35
        versionCode = 2
        versionName = "2.6"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField( "int", "GRID_SIZE", "9" )
        buildConfigField( "int", "BALL_LIMIT_TO_REMOVE", "5" )
    }

    signingConfigs {
        create("release") { // Use create to define a signing config
            storeFile = file(providers.gradleProperty("MYAPP_RELEASE_STORE_FILE").get())
            storePassword = providers.gradleProperty("MYAPP_RELEASE_STORE_PASSWORD").get()
            keyAlias = providers.gradleProperty("MYAPP_RELEASE_KEY_ALIAS").get()
            keyPassword = providers.gradleProperty("MYAPP_RELEASE_KEY_PASSWORD").get()
        }
    }

    buildTypes {

        getByName("release") { // Use getByName to configure an existing build type
            // Correct way to assign the signing configuration
            signingConfig = signingConfigs.getByName("release")

            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}
dependencies {

    // Core Android & Jetpack
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx) // For Lifecycle-aware components
    implementation(libs.androidx.activity.compose)     // For Jetpack Compose in Activities

    // Jetpack Compose
    implementation(platform(libs.androidx.compose.bom)) // Compose Bill of Materials - manages versions
    implementation(libs.androidx.ui)
    implementation(libs.androidx.navigation.compose)
    // Hilt-Dagger
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)   // For Composable previews in Android Studio
    implementation(libs.androidx.material3)            // Material Design 3 components

    // Room (Persistence)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)             // For Coroutines and Flow support with Room
    ksp(libs.androidx.room.compiler)                   // Room annotation processor (KSP)

    // Data store
    implementation(libs.androidx.datastore.preferences)
    // WorkManager (Background Processing)
    implementation(libs.androidx.work.runtime.ktx)

    // Unit Testing (test source set - runs on your local JVM)
    testImplementation(libs.junit)                     // JUnit 4 for unit tests
    testImplementation(libs.truth)                     // Google's Truth assertion library
    testImplementation(libs.kotlinx.coroutines.test)   // For testing Coroutines
    testImplementation(libs.mockito.kotlin)            // Mockito for Kotlin
    testImplementation(libs.mockito.inline)            // Mockito inline mock maker
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.common)

    // Instrumented Testing (androidTest source set - runs on an Android device/emulator)
    androidTestImplementation(libs.androidx.junit)     // AndroidX Test extensions for JUnit
    androidTestImplementation(libs.androidx.espresso.core) // Espresso for UI testing
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Ensure Compose test versions align
    androidTestImplementation(libs.androidx.ui.test.junit4)    // For testing Jetpack Compose UIs

    // Debug Only (debug source set - only included in debug builds)
    debugImplementation(libs.androidx.ui.tooling)      // Compose tooling for layout inspection, etc.
    debugImplementation(libs.androidx.ui.test.manifest) // For Compose test manifest generation

}