# eitco maven build utilities

[![License](https://img.shields.io/github/license/eitco/bom-maven-plugin.svg?style=for-the-badge)](https://opensource.org/license/mit)
[![Build status](https://img.shields.io/github/actions/workflow/status/eitco/maven-build-utilities/deploy.yaml?branch=main&style=for-the-badge&logo=github)](https://github.com/eitco/eitco-oss-parent/actions/workflows/deploy.yaml)
[![Maven Central Version](https://img.shields.io/maven-central/v/de.eitco.cicd/build-utilities-maven-plugin?style=for-the-badge&logo=apachemaven)](https://central.sonatype.com/artifact/de.eitco.cicd/build-utilities-maven-plugin)

This maven plugin contains some simple build utilities. This plugin works as a collection of simple maven goals that
only execute simple code. More complex code should be placed in its own plugin.

# usage

Add the following plugin to your build:

```xml
<project>
    ...
    <build>
        ...
        <plugins>
            ...
            <plugin>
                <groupId>de.eitco.cicd</groupId>
                <artifactId>build-utilities-maven-plugin</artifactId>
                <version>0.0.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```
Check for the latest version [here](https://central.sonatype.com/artifact/de.eitco.cicd/build-utilities-maven-plugin). This enables the following goals: 

## dependencies-version-properties

This goal generates properties, containing the version and file system path of every direct dependency the current
project has. For every dependency, two properties are generated: 
    
* 'maven.dependency.<dependency-conflict-id>.file' set to the absolute file system path of the dependency 
* 'maven.dependency.<dependency-conflict-id>.version' set to the version of the dependency.

The dependency conflict id is constructed as follows: &lt;groupId&gt;:&lt;artifactId&gt;:&lt;type&gt;[&lt;classifier&gt;]

## list-properties

This goal prints all properties and their value currently available. This is mostly thought of for debugging purposes. 

## set-artifact

This goal sets the artifact file of the current project. This is intended for highly customized builds where maven 
is unable to deduce the builds main artifact by itself.

Use the parameter `file` to specify the file to attach.

## write-file

This goal generates a text file. The following parameters are available:

### targetFile
property: `target-file`

The name of the file to generate

### content
property: `file.content`

The content to write to the file

### append
property: `append.file`

Whether content should be appended to the file - or the file should be overridden

### skip
property: `write.file.skip`

Whether the execution should be skipped

