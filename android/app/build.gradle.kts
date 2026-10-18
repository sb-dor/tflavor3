import java.util.Properties
import java.io.FileInputStream


plugins {
    id("com.android.application")
    // The Flutter Gradle Plugin must be applied after the Android and Kotlin Gradle plugins.
    id("dev.flutter.flutter-gradle-plugin")
}

// Each brand signs its release with its own upload key: android/key-<flavor>.properties.
// Avera also accepts the original android/key.properties. A brand without a key file
// builds an unsigned release, which Google Play rejects, so it can never be signed
// with another brand's key by accident.
val brandFlavors = listOf("production", "staging")

fun keystoreFor(flavor: String): Properties? {
    val file = rootProject.file("key-$flavor.properties")
    if (!file.exists()) return null
    return Properties().apply { FileInputStream(file).use { load(it) } }
}

android {
    namespace = "com.example.tflavors3"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    defaultConfig {
        // TODO: Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html).
        applicationId = "com.example.tflavors3"
        // You can update the following values to match your application needs.
        // For more information, see: https://flutter.dev/to/review-gradle-config.
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        // Uses the version code from pubspec.yaml. When using split APKs, 1000 * ABI_VERSION
        // is added automatically by Flutter. (https://developer.android.com/studio/build/configure-apk-splits#configure-APK-versions)
        // You can force using the value of versionCode by specifying the `-P force-version-code-ignoring-abi=true`
        // flag during build.
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    buildTypes {
        release {
            // TODO: Add your own signing config for the release build.
            // Signing with the debug keys for now, so `flutter run --release` works.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    signingConfigs {
        brandFlavors.forEach { flavor ->
            val p = keystoreFor(flavor) ?: return@forEach
            create("${flavor}Release") {
                keyAlias = p["keyAlias"] as String?
                keyPassword = p["keyPassword"] as String?
                storeFile = p["storeFile"]?.let { file(it) }
                storePassword = p["storePassword"] as String?
            }
        }
    }

    flavorDimensions += "default"

    productFlavors {
        create("staging") {
            dimension = "default"
            applicationId = "com.flavor.staging"
            // signingConfig = signingConfigs.findByName("")
        }
        create("production") {
            dimension = "default"
            applicationId = "com.flavor.production"
            // signingConfig = signingConfigs.findByName("")
        }
    }
    
     buildTypes {
        release {
            // no signingConfig here
            
            // if necessary
            // isMinifyEnabled = true
            // isShrinkResources = true
            
            // if the obfuscation is necessary
            // proguardFiles(
            //     getDefaultProguardFile("proguard-android-optimize.txt"),
            //     "proguard-rules.pro",
            // )
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

flutter {
    source = "../.."
}
