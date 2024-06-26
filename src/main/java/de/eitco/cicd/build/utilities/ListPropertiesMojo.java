/*
 * Copyright (c) 2021 EITCO GmbH
 * All rights reserved.
 *
 * Created on 01.06.2021
 *
 */
package de.eitco.cicd.build.utilities;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

/**
 * This goal prints all properties and their value currently available. This is mostly thought of for debugging purposes.
 */
@Mojo(name = "list-properties", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class ListPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * This parameter specifies a prefix. If set, only properties beginning with this prefix will be listed.
     */
    @Parameter
    private String prefix;

    /**
     * This parameter sets the file to write the properties to. If not set the properties will simply be logged.
     */
    @Parameter
    private File outputFile;

    @Override
    public void execute() throws MojoExecutionException {


        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Object, Object> property : project.getProperties().entrySet()) {

            if (prefix != null && !property.getKey().toString().startsWith(prefix)) {

                continue;
            }

            builder.append(property.getKey()).append(" = ").append(property.getValue()).append("\n");
        }

        if (outputFile != null) {

            try {

                FileUtils.forceMkdir(outputFile.getParentFile());

                Files.writeString(outputFile.toPath(), builder.toString());

            } catch (IOException e) {

                throw new MojoExecutionException(e);
            }


        } else {

            getLog().info("project properties : \n" + builder);
        }

    }
}
