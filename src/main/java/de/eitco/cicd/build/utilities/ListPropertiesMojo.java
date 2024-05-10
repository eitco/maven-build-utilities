/*
 * Copyright (c) 2021 EITCO GmbH
 * All rights reserved.
 *
 * Created on 01.06.2021
 *
 */
package de.eitco.cicd.build.utilities;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.util.Map;

@Mojo(name = "list-properties", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class ListPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;


    @Override
    public void execute() throws MojoExecutionException {


        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Object, Object> property : project.getProperties().entrySet()) {

            builder.append(property.getKey()).append(" = ").append(property.getValue()).append("\n");
        }

        getLog().info("project properties : \n" + builder);

    }
}
