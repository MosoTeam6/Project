# Project

#build.gradle
'''
plugins {
id("com.android.application")
}

android {
namespace = "com.example.project"
compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project"
        minSdk = 26
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
}

dependencies {
implementation ("androidx.core:core-ktx:1.12.0")
implementation ("androidx.appcompat:appcompat:1.6.1")
implementation ("com.google.android.material:material:1.10.0")
implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
testImplementation ("junit:junit:4.13.2")
androidTestImplementation ("androidx.test.ext:junit:1.1.5")
androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
implementation ("com.github.bumptech.glide:glide:4.13.0")
annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
implementation ("jp.wasabeef:glide-transformations:4.3.0")
implementation ("net.sourceforge.jexcelapi:jxl:2.6.12")
implementation ("androidx.recyclerview:recyclerview:1.3.2")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
implementation("androidx.lifecycle:lifecycle-runtime:2.6.2")
implementation("androidx.room:room-ktx:2.6.1")
annotationProcessor("androidx.room:room-compiler:2.6.1")
implementation ("com.prolificinteractive:material-calendarview:1.4.3")
}
'''
#gradle.properties

'''
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
# When configured, Gradle will run in incubating parallel mode.
# This option should only be used with decoupled projects. More details, visit
# http://www.gradle.org/docs/current/userguide/multi_project_builds.html#sec:decoupled_projects
# org.gradle.parallel=true
# AndroidX package structure to make it clearer which packages are bundled with the
# Android operating system, and which are packaged with your app's APK
# https://developer.android.com/topic/libraries/support-library/androidx-rn
android.useAndroidX=true
# Enables namespacing of each library's R class so that its R class includes only the
# resources declared in the library itself and none from the library's dependencies,
# thereby reducing the size of the R class for that library
android.nonTransitiveRClass=true
android.enableJetifier=true

'''