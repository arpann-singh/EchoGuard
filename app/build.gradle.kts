plugins {
    // Applying via ID instead of alias() resolves the "extension already registered" conflict
    // in complex Kotlin 2.0 multi-module setups.
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.echoguard"

    // Updated to 36 to satisfy latest AndroidX library requirements (AAR metadata).
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.echoguard"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0.4-forensic"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("boolean", "IS_TACTICAL_RELEASE", "true")
        }
        debug {
            // Suffix removed to ensure package matches 'com.example.echoguard' in google-services.json
            isDebuggable = true
            buildConfigField("boolean", "IS_TACTICAL_RELEASE", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
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
    implementation("androidx.compose.material:material-icons-extended")

    // Navigation & Data
    implementation("androidx.navigation:navigation-compose:2.8.8")
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    // Firebase (Latest Stable)
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")

    // SOS Infrastructure & AI
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.tensorflow:tensorflow-lite-task-audio:0.4.4")
    implementation("androidx.lifecycle:lifecycle-service:2.8.7")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}