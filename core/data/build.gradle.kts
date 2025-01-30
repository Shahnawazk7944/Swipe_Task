plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    alias(libs.plugins.ksp.annotation.processor) version libs.versions.kspAnnotationProcessor
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.room)
}

android {
    namespace = "dev.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    //Project Modules
    implementation(project(":core:models"))

    //Arrow core lib
    implementation(libs.arrow.core)

    //Coroutines and Serialization
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp3)
    implementation(libs.okhttp3.logging)

    //Room
    implementation(libs.room)
    implementation(libs.roomKTX)
    ksp(libs.roomKSP)

    // Dagger Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)   //KSP Annotation Processor
    implementation(libs.androidx.hilt.navigation.compose) //hilt for navigation

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}