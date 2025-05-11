plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "app.vitune.compose.routing"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xcontext-receivers",
            "-Xsuppress-warning=CONTEXT_RECEIVERS_DEPRECATED"
        )
    }
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.activity)
    implementation(libs.compose.foundation)
    implementation(libs.compose.animation)

    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
}
