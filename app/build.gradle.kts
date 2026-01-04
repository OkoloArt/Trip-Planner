plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrainsKotlinKsp)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "com.death.goplan"
//    compileSdk {
//        version = release(36)
//    }

    compileSdk = 36

    defaultConfig {
        applicationId = "com.death.goplan"
        minSdk = 24
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
//    kotlinOptions {
//        jvmTarget = "11"
//    }
    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
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

    implementation(libs.compose.material3)
    implementation(libs.compose.icons.extended)

    //Navigation
    implementation(libs.navigation.compose)

    //Hilt
    implementation (libs.hilt.android)
    ksp(libs.hilt.ext.compiler)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    //Coil
    implementation(libs.coil.compose)

    //Coroutines
    implementation(libs.kotlinx.coroutines)

    //Compose Lifecycle
    implementation(libs.runtime.livedata)
    implementation(libs.lifecycle.compose)

    //Kotlinx-serialization
    implementation(libs.kotlinx.serialization)

    //ktor
    implementation(libs.client.android)
    implementation(libs.client.logging)
    implementation(libs.client.auth)
    implementation(libs.client.serialization)
    implementation(libs.client.content.negotiation)
    implementation(libs.client.cio)
    implementation(libs.ktor.serialization)
}