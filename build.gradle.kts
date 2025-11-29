import org.gradle.api.plugins.antlr.AntlrTask


plugins {
    kotlin("jvm") version "2.2.20"
    id("antlr")
    id("application")
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

tasks.named<AntlrTask>("generateGrammarSource") {
    // main-грамматика
    arguments = arguments + listOf("-package", "gen", "-visitor", "-long-messages")
    outputDirectory = generatedAntlrMainDir.get().asFile
}

tasks.named<AntlrTask>("generateTestGrammarSource") {
    // тестовая грамматика (если понадобится)
    arguments = arguments + listOf("-package", "gen", "-visitor", "-long-messages")
    outputDirectory = generatedAntlrTestDir.get().asFile
}

// чтобы сначала генерить грамматику, потом компилировать
tasks.named("compileKotlin") {
    dependsOn("generateGrammarSource")
}
tasks.named("compileJava") {
    dependsOn("generateGrammarSource", "generateTestGrammarSource")
}

tasks.test {
    useJUnitPlatform()
}

// JDK 21 как toolchain (это ок при твоём JAVA_HOME=21)
kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("org.example.FsmMainKt")
}

// чтобы Kotlin/Java видели сгенерированные классы
sourceSets {
    named("main") {
        java.srcDir(generatedAntlrMainDir)
    }
    named("test") {
        java.srcDir(generatedAntlrTestDir)
    }
}

// отдельная задача для явного запуска демо FSM
tasks.register<JavaExec>("runFsm") {
    group = "application"
    description = "Run FsmMainKt demo"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("org.example.FsmMainKt")
    dependsOn("build")
}
