plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://repo.gradle.org/gradle/libs-releases") }
}

dependencies {
    implementation(project(":project"))
    implementation(project(":project", configuration = "customConfiguration"))
    implementation("org.gradle:gradle-tooling-api:+")
    runtimeOnly("org.slf4j:slf4j-simple:+")
}

application {
    mainClass.set("org.example.MainKt")
}
tasks.register("graphApis") {
    val classpath = project.configurations["compileClasspath"]
    val dependencies = classpath.incoming.resolutionResult.allDependencies.filterIsInstance<ResolvedDependencyResult>()
    val projectDependencies = dependencies.filter {it.resolvedVariant.owner is ProjectComponentIdentifier}
    projectDependencies.forEach {
        // At this level, a more granular signal that would let us match to a source set (instead of a project) would be
        // enough to figure out the correct dependency structure.
        println(it.resolvedVariant.displayName)
        println(it.resolvedVariant.attributes)
        println(it.resolvedVariant.capabilities)
        println()
    }
}