micro-grails
============
The Micro Grails plugin for [Gradle](http://www.gradle.org/)  brings some of the convention over configuration Grails has to Micronaut projects. 
It also brings along the groovy console form the gradle-console plugin:

https://plugins.gradle.org/plugin/net.carlosgsouza.console

![alt text](readme-files/groovy_console.png "Groovy Console")


## Installation
Add the plugin your build.gradle file like this:

```groovy
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.virtualdogbert:micro-grails-plugin:1.0.M1'
    }
}
apply plugin: "micro.grails.plugin"
```
Where you put the apply can have an effect on if the plugin will work, I would put it after all the existing applies.

After run the `setupConventions` Gradle task to get the default conventions.groovy, and the default file structure. After that you can customize things,
but be cognisant of the package you put files under, as it has been found to  cause errors in Micronaut if things aren't under the right package structure.

## Usage

### Micro Grails
see the [Micro Grails library](https://github.com/virtualdogbert/micro-grails/) 

### console
Invoke the <code>console</code> task using gradle
You will get a console window with all your runtime dependencies loaded for you. This is useful in case you need to interact your project dependencies or your source code in a fast and flexible way.

```
gradle console
```

Note if you have dependencies that bring in groovy, this may conflict with the console task. In that case for the offending dependency you may have to do something like this:

```
testCompile ("org.gebish:geb-spock:2.1"){
    exclude group: "org.codehaus.groovy", module: "groovy-all"
}
```

Even a testCompile dependency can cause this issue.

### ymlToGroovyConfig
This task allows you to convert your yml config, to a map based Groovy Config. This makes use of the Groovy Config Writer. The writer is
set to use the map based syntax over the closure syntax, as it works better with the Micronaut dashersized names. To run the task you will
need to set the yml file, and the output file like this

```
gradle ymlToGroovyConfig -PymlFile=src/main/resources/application.yml -PoutputFile=src/main/resources/application.groovy
```

If you omit the outputFile the config will be written to standard out.


## About this project
This is a side project of mine just to play around with adding similar conventions over configuration, like Grails to Micronaut, using my 
Micro Grails library. This may be completely supplanted by the fact that Grails 4 will use Microanut for its main context. However because this
is lighter weight, it may appeal to some, and also demonstrates how Grails works with simpler/more constrained code.
 
## Acknowledgement
This plugin was created based on [this plugin](https://plugins.gradle.org/plugin/net.carlosgsouza.console) by Carlos Souza. I wanted the Groovy
console for my project, and the plugin was simple enough I could use it as a base to make my own.
