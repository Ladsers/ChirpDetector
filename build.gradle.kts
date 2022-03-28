import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "com.ladsers"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("uk.me.berndporr:iirj:1.5")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}