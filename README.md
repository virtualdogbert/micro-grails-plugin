micro-grails
============
This plugin for [Gradle](http://www.gradle.org/)  brings some of the convention over configuration Grails has to Micronaut projects. It also
brings along the groovy console form the gradle-console plugin:

https://plugins.gradle.org/plugin/net.carlosgsouza.console

![alt text](readme-files/groovy_console.png "Groovy Console")





## Installation
Add one of the following snippets to your build.gradle file according to the version of Gradle you are using:
### Gradle >= 2.1
```groovy
plugins {
    id "micro.grails.plugin" version "1.0.M1"
}
```

### Gradle < 2.1
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
Where you put the apply can have an effect on if the plugin will work, I would put it after all the existing applys.

## Usage

### Micro Grails
see the [Micro Grails library](https://github.com/virtualdogbert/micro-grails/) 

### console
Invoke the <code>console</code> task using gradle
You will get a console window with all your runtime dependencies loaded for you. This is useful in case you need to interact your project dependencies or your source code in a fast and flexible way.

```
gradle console
```

## About this project
This is a side project of mine just to play around with adding similar conventions over configuration, like Grails to Micronaut, using my 
Micro Grails library. This may be completely supplanted by the fact that Grails 4 will use Microanut for its main context. However because this
is lighter weight, it may appeal to some, and also demonstrates how Grails works with simpler/more constrained code.
 
## Acknowledgement
This plugin was created based on [this plugin](https://plugins.gradle.org/plugin/net.carlosgsouza.console) by Carlos Souza. I wanted the Groovy
console form my project, and the plugin was simple enough I could use it as a base to make my own.
