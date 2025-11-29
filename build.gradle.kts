plugins {
    kotlin("jvm") version "2.2.20"
    id("antlr")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    antlr("org.antlr:antlr4:4.13.1")
    implementation("org.antlr:antlr4-runtime:4.13.1")

    testImplementation(kotlin("test"))
}

val generatedAntlrMainDir = layout.buildDirectory.dir("generated-src/antlr/main/gen")
val generatedAntlrTestDir = layout.buildDirectory.dir("generated-src/antlr/test/gen")

tasks.named<org.gradle.api.plugins.antlr.AntlrTask>("generateGrammarSource") {
    // keep generated classes in the gen package to match imports
    arguments = arguments + listOf("-package", "gen", "-visitor", "-long-messages")
    outputDirectory = generatedAntlrMainDir.get().asFile
}

tasks.named<org.gradle.api.plugins.antlr.AntlrTask>("generateTestGrammarSource") {
    // tests, if any, get their own output folder to avoid interfering with main
    arguments = arguments + listOf("-package", "gen", "-visitor", "-long-messages")
    outputDirectory = generatedAntlrTestDir.get().asFile
}

tasks.named("compileKotlin") {
    dependsOn("generateGrammarSource")
}

tasks.named("compileJava") {
    dependsOn("generateGrammarSource")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

// Чтобы Kotlin спокойно видел сгенерённые классы
sourceSets {
    named("main") {
        java.srcDir(generatedAntlrMainDir)
    }
    named("test") {
        java.srcDir(generatedAntlrTestDir)
    }
}
