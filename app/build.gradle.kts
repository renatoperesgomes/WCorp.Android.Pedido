plugins {
    id("com.android.application")
}

android {
    namespace = "com.wcorp.w_corpandroidpedido"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.wcorp.w_corpandroidpedido"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = true
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("androidx.concurrent:concurrent-futures:1.1.0")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.0.0")
    implementation ("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("br.com.uol.pagseguro.plugpagservice.wrapper:wrapper:1.26.1")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.0")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}