package com.virtualdogbert

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.JavaExec
import org.yaml.snakeyaml.Yaml

/**
 * This plugin adds the Micro Grails library, source sets to match the config for Micro Grails, and the Groovy console which is good for
 * testing and debugging.
 */
class MicroGrailsPlugin implements Plugin<Project> {

    static final  String      conventionsFile = "conventions.groovy.template"
    static final  String      conventionsOut  = "conventions.groovy"
    private final ClassLoader loader          = getClass().getClassLoader()

    void apply(Project project) {

        //Adds Groovy console task from original plugin
        project.task('console', dependsOn: 'classes', type: JavaExec) {
            group = 'micro grails'

            logger.debug("Opening Gradle Console")

            main = 'groovy.ui.Console'

            Configuration consoleRuntime = project.configurations.create("consoleRuntime")
            consoleRuntime.dependencies.add(project.dependencies.localGroovy())

            classpath = project.sourceSets.main.runtimeClasspath + project.sourceSets.test.runtimeClasspath + project.files(consoleRuntime.asPath)
            logger.debug("Gradle Console classpath=$classpath")
        }

        //Adds yml to groovy config task using map based config, which seems to work better with the dashersized names.
        project.task('ymlToGroovyConfig') {
            group = 'micro grails'

            doLast {
                String quotedValuesIn = 'Default'
                String ymlFile = ''
                String outputFile =''

                if (project.hasProperty('quotedValuesIn')) {
                    quotedValuesIn = project['quotedValuesIn']
                }

                if (project.hasProperty('ymlFile')) {
                    ymlFile = project['ymlFile']
                }

                if (project.hasProperty('outputFile')) {
                    outputFile = project['outputFile']
                }

                if (!ymlFile) {
                    println "parameter -PymlFile required"
                    return
                }

                println ymlFile
                println outputFile

                String indent = ' ' * 4
                List<String> quotedValues = quotedValuesIn.split(',').toList()

                File config = new File(ymlFile)
                String configText = config.newDataInputStream().getText()
                List<String> docs = configText.split('---\n')
                GroovyConfigWriter configWriter

                if (outputFile) {
                    configWriter = new com.virtualdogbert.GroovyConfigWriter(outputFile, null, indent, quotedValues, false)
                } else {
                    configWriter = new com.virtualdogbert.GroovyConfigWriter(asClosure: false)
                }

                Yaml yaml = new Yaml()

                docs.findResults {
                    configWriter.writeToGroovy(yaml.load(it))
                }

                configWriter.close()
            }
        }


        //Adds a task to setup the Micro Grails conventions, coping over the default conventions.groovy and setting up the directories
        //with packages
        project.task('setupConventions') {
            group = 'micro grails'

            doLast {
                String conventionsIn = loader.getResourceAsStream(conventionsFile).text
                File conventionsOut = new File("$project.projectDir/$conventionsOut")

                if (!conventionsOut.exists()) {
                    conventionsOut.append(conventionsIn)
                }

                ConfigSlurper configSlurper = new ConfigSlurper()
                ConfigObject config = configSlurper.parse(conventionsIn).conventions

                //TODO add command in M2
                //File commandDirectory = new File("$config.rootPath/$config.commandPath")
                File controllerDirectory = new File("$config.rootPath/$config.controllerPath/$project.group")
                File domainDirectory = new File("$config.rootPath/$config.domainPath/$project.group")
                File serviceDirectory = new File("$config.rootPath/$config.servicePath/$project.group")

//                //TODO add command in M2
//                if (!commandDirectory.exists()) {
//                    commandDirectory.mkdirs()
//                }

                if (!controllerDirectory.exists()) {
                    controllerDirectory.mkdirs()
                }

                if (!domainDirectory.exists()) {
                    domainDirectory.mkdirs()
                }

                if (!serviceDirectory.exists()) {
                    serviceDirectory.mkdirs()
                }

                File urlMappings = new File("$config.rootPath/$config.controllerPath/$project.group/UrlMappings.groovy")

                if(!urlMappings.exists()) {
                    urlMappings.append("package $project.group")
                }
            }
        }

        File conventions = new File(conventionsOut)

        if (conventions.exists()) {
            ConfigSlurper configSlurper = new ConfigSlurper()
            ConfigObject config = configSlurper.parse(conventions.toURI().toURL()).conventions

            //Add source setts based on conventions file.
            project.sourceSets {
                main {
                    groovy {
                        //TODO add command in M2
                        //srcDirs "$config.rootPath/$config.commandPath"
                        srcDirs "$config.rootPath/$config.controllerPath"
                        srcDirs "$config.rootPath/$config.domainPath"
                        srcDirs "$config.rootPath/$config.servicePath"
                    }
                }
            }
        }

        //Adds Micro Grails library to the project
        project.dependencies {
            delegate.compile('com.virtualdogbert:micro-grails:1.0.M1')
        }
    }
}
