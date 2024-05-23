/*
 * Copyright (c) 2021 EITCO GmbH
 * All rights reserved.
 *
 * Created on 16.08.2021
 *
 */
package de.eitco.cicd.build.utilities;


import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;

/**
 * This goal sets the artifact file of the current project. This is intended for highly customized builds where maven is
 * unable to deduce the builds main artifact by itself.
 */
@Mojo(name = "set-artifact")
public class SetProjectArtifactMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * The file to attach to the project.
     */
    @Parameter(defaultValue = "${project.build.directory}/${project.artifactId}.${project.packaging}", required = true, property = "artifact.file")
    private File file;


    @Override
    public void execute() {

        project.getArtifact().setFile(file);
    }
}
