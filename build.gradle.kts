plugins {
    kotlin("js") version "1.7.10"
}

group = "me.shalaga44"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.narbase.kunafa:core:0.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(npm("firebase", "9.9.4"))

    testImplementation(kotlin("test"))
}
kotlin {
    js(LEGACY) {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
}

tasks.register("stage") {
    dependsOn("build")
}