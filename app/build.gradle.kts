plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.learn.android.trevea"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.learn.android.trevea"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.text.google.fonts)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

//    Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.jetbrains.serialization.json)
    androidTestImplementation(libs.android.navigation.test)

//    Retrofit
    implementation(libs.squareup.retirfit2)
    implementation(libs.squareup.retrofit2.gson)
    implementation(libs.squareup.okhttp3)

//    Viewmodel
    implementation(libs.androidx.lifecycle.view.model)

//    Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.android.room.ktx)
    ksp(libs.androidx.room.compiler)

//    Icons
    implementation("androidx.compose.material:material-icons-extended")

//    Datastore
    implementation(libs.androidx.datastore.preferences)

//    Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}