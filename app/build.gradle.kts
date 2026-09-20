import java.util.Properties



plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices)
}

android {
    namespace = "medyo.com"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "medyo.com"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //buildConfigField("String", "GEMINI_API_KEY", getGeminiKeys())
    }

    buildTypes {

        debug {
            versionNameSuffix = "-dev"
            applicationIdSuffix = ".dev"
            manifestPlaceholders["appName"] = "Medyo dev"
        }
        release {
            manifestPlaceholders["appName"] = "Medyo"
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
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.designSystem)
    implementation(projects.core.ui)
    implementation(projects.core.aiLogic)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.navigation)
    implementation(projects.logger.api)
    implementation(projects.logger.impl)
    implementation(projects.feature.scanner.api)
    implementation(projects.feature.scanner.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.bioScan.api)
    implementation(projects.feature.bioScan.impl)
    implementation(projects.feature.settings.api)
    implementation(projects.feature.settings.impl)
    implementation(projects.feature.detail.api)
    implementation(projects.feature.detail.impl)
    implementation(projects.feature.expiryDashboard.api)
    implementation(projects.feature.expiryDashboard.impl)
    implementation(projects.core.notification)
    implementation(projects.core.expiryAlert)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    
    // Accompanist Permissions
    implementation(libs.accompanist.permissions)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ai)
    implementation(libs.firebase.appcheck.playintegrity)
    debugImplementation(libs.firebase.appcheck.debug)

    implementation(libs.androidx.core.ktx)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.icons.extended)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.jakewharton.timber)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)
    ksp(libs.hilt.ext.compiler)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    testImplementation(libs.junit)
    implementation(libs.androidx.core.splashscreen)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

fun getGeminiKeys(): String {
    if (project.rootProject.file("local.properties").canRead()) {
        val localProperties = readProperties(project.rootProject.file("local.properties"))
        val geminiApiKey: String = localProperties.getProperty("GEMINI_API_KEY")
        return "\"$geminiApiKey\""
    }
    return ""
}


fun readProperties(propertiesFile: File) = Properties().apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}