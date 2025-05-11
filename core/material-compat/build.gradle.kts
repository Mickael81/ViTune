plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.google.android.material"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    sourceSets.all {
        kotlin.srcDir("src/$name/kotlin")
    }

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xcontext-receivers",
            "-Xsuppress-warning=CONTEXT_RECEIVERS_DEPRECATED"
        )
    }
}

dependencies {
    implementation(projects.core.ui)

    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())
}
