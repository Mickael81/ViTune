plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
}

android {
    namespace = "app.vitune.core.data"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
    }

    sourceSets.all {
        kotlin.srcDir("src/$name/kotlin")
    }
}

kotlin {
    jvmToolchain(libs.versions.jvm.get().toInt())

    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xcontext-receivers",
            "-Xnon-local-break-continue",
            "-Xconsistent-data-class-copy-visibility",
            "-Xsuppress-warning=CONTEXT_RECEIVERS_DEPRECATED"
        )
    }
}

dependencies {
    implementation(libs.core.ktx)

    api(libs.kotlin.datetime)

    detektPlugins(libs.detekt.compose)
    detektPlugins(libs.detekt.formatting)
}
