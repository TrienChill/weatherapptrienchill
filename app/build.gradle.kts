plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 34
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.preference.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Scalalble Size Unit (upport for different screen sizes)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    // navigation component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // koin (dependency injection framework)
    implementation(libs.koin.android)

    // retrofit
    implementation(libs.converter.gson)
    implementation(libs.retrofit)

    // viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx.v262)

    // coil
    implementation(libs.coil)

    //location
    implementation(libs.play.services.location)

    //Swipe Refresh Layout
    implementation(libs.androidx.swiperefreshlayout)

    //widget cho các view
    implementation (libs.androidx.glance) // Thêm nếu sử dụng Glance (công cụ mới để xây dựng widget)

    //hiệu ứng
    implementation (libs.androidx.cardview)


}