import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("org.jetbrains.compose")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "me.brisson"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    api(compose.materialIconsExtended)

    // Network
    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.11.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Dependency Injection
    implementation("io.insert-koin:koin-core:3.5.0")

    // Navigator
    implementation("cafe.adriel.voyager:voyager-navigator:1.0.0-rc10")
    implementation("cafe.adriel.voyager:voyager-koin:1.0.0-rc10")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "guillhama"
            packageVersion = "1.0.0"
        }
    }
}
