plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.skincare"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.skincare"
        minSdk = 24
        targetSdk = 35
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
}

dependencies {
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //Import the Firebase BoM (Manages Firebase versions autmomatically)
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))

    //Firebase Realtime Database
    implementation("com.google.firebase:firebase-database-ktx")

    // Firebase UI for RecyclerView (Important for Firebase data binding)
    implementation("com.firebaseui:firebase-ui-database:8.0.2")

    // RecyclerView (Ensures proper list display)
    implementation("androidx.recyclerview:recyclerview:1.3.2")


    //Material Components
    implementation ("com.google.android.material:material:1.9.0")

    //AndroidX dependencies
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity:1.10.1")
    implementation(libs.material)
    implementation(libs.constraintlayout)


    //Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}