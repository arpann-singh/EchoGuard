// Root build.gradle.kts
// This file only defines the plugins; it does not apply them.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Define Google Services version here for global consistency.
    id("com.google.gms.google-services") version "4.4.2" apply false
}