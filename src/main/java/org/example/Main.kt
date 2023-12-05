package org.example

import org.gradle.tooling.GradleConnector
import org.gradle.tooling.model.idea.IdeaModuleDependency
import org.gradle.tooling.model.idea.IdeaProject
import java.io.File

fun main() {
    val project2 = Project2()
    // Below resolution works with the latest EAP IntelliJ, was broken in some IntelliJ/Studio versions
    val project2Custom = Project2Custom()


    val connection = GradleConnector.newConnector()
        .forProjectDirectory(File("."))
        .connect();
    val ideaProject = connection.getModel(IdeaProject::class.java)
    // Model below only contains project dependency info, even though we have
    // the following in the build script too.
    // implementation(project(":project", configuration = "custom"))

    // Since each source set corresponds to an IDEA module, we need source set level granularity
    // My basic understanding is that if we had this info here,
    // this should be enough for us to set up the IDEA library table/dependencies properly.
    // BUT we still need a strong signal from the Gradle APIs to be able to map
    // resolved variant results to source sets within the current implementation.
    // That said we can revisit the implementation, though I have to evaluate how much of the info
    // produced by our dependency resolution models can also be provided by IDEA Project model.

    // P.S. It seems to work here, but for whatever reason within Android projects this IDEA project model
    // doesn't contain any project dependencies. It could be that somehow it only works for Java projects.
    println(ideaProject)
    ideaProject.children
        .flatMap { module ->
            module.dependencies.filterIsInstance<IdeaModuleDependency>().map { module to it }
        }
        .forEach { (module, dependency) ->
            println("${module.gradleProject.name} depends on ${dependency.targetModuleName}")
        }

    connection.close()
}
