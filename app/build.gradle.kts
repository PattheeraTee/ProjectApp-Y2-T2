plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.hantanjai_app_proj"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hantanjai_app_proj"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.ui:ui-android:1.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-database:20.3.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // recycleview for hanmoney_step1
    //implementation("androidx.recycleview:recyclerview:1.2.0")

    // roundedImageview for hanmoney_step1
//    implementation("com.makeramen:roundedimageview:2.3.0")

    implementation ("androidx.recyclerview:recyclerview:1.3.0")
    implementation ("androidx.recyclerview:recyclerview-selection:1.1.0")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    //Dialog
    implementation ("com.google.android.material:material:1.4.0")
    implementation ("androidx.appcompat:appcompat:1.4.0")
    implementation ("androidx.core:core-ktx:1.7.0")


}