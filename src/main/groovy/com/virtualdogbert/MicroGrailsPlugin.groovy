package com.virtualdogbert

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec

/**
 * This plugin adds the Micro Grails library, source sets to match the config for Micro Grails, and the Groovy console which is good for
 * testing and debugging.
 */
class MicroGrailsPlugin implements Plugin<Project> {

    static final String conventionsFile = "conventions.groovy"

    void apply(Project project) {

        //Adds Groovy console task from original plugin
        project.rootProject.task('console', dependsOn: 'classes', type: JavaExec) {
            logger.debug("Opening Gradle Console")

            main = 'groovy.ui.Console'

            project.apply plugin: "groovy"

            Configuration consoleRuntime = project.configurations.create("consoleRuntime")
            consoleRuntime.dependencies.add(project.dependencies.localGroovy())

            classpath = project.sourceSets.main.runtimeClasspath + project.sourceSets.test.runtimeClasspath + project.files(consoleRuntime.asPath)
            logger.debug("Gradle Console classpath=$classpath")
        }

        ConfigSlurper configSlurper = new ConfigSlurper()
        ConfigObject config = configSlurper.parse(new File(conventionsFile).toURL()).conventions

        //Add source setts based on conventions file.
        project.rootProject.sourceSets {
            main {
                groovy {
                    srcDirs "$config.rootPath/$config.commandPath"
                    srcDirs "$config.rootPath/$config.controllerPath"
                    srcDirs "$config.rootPath/$config.domainPath"
                    srcDirs "$config.rootPath/$config.servicePath"
                }
            }
        }


        //Adds Micro Grails library to the project
        project.dependencies {
            delegate.compile("com.virtualdogbert:micro-grails:1.0.M1")
        }
    }
}
