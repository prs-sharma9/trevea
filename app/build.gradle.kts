plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

//    Navigation - START
    val nav_version = "2.9.8"
// Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:${nav_version}")
// Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
// JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
//    Navigation - END

//    Retrofit - START
// Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
// Retrofit with Scalar Converter
//    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//    Convertor - https://github.com/square/moshi/
//    implementation("com.squareup.moshi:moshi-kotlin:2.0.0-alpha.1")
//    OkHttp Logging interceptor - https://github.com/lysine-dev/okhttp/blob/main/okhttp-logging-interceptor/README.md
    implementation("com.squareup.okhttp3:logging-interceptor:5.4.0")
//    Retrofit - END

//     ViewModel - START
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
//     ViewModel - END
}