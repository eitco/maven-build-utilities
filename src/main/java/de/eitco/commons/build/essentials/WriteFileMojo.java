/*
 * Copyright (c) 2020 EITCO GmbH
 * All rights reserved.
 *
 * Created on 06.05.2020
 *
 */
package de.eitco.commons.build.essentials;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Mojo(name = "write-file", requiresProject = false)
public class WriteFileMojo extends AbstractMojo {

    @Parameter(required = true, property = "target.file")
    public File targetFile;

    @Parameter(defaultValue = "false", property = "append.file")
    public boolean append;

    @Parameter(defaultValue = "false", property = "build.essentials.skip")
    public boolean skip;

    @Parameter(required = true, property = "file.content")
    public String content;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        if (skip) {

            getLog().info("skipping");

            return;
        }

        try {

            FileUtils.forceMkdir(targetFile.getParentFile());

            String prefix = "";

            if (append && targetFile.isFile()) {

                prefix = Files.readString(targetFile.toPath());
            }

            try (PrintWriter target = new PrintWriter(targetFile, StandardCharsets.UTF_8)) {

                target.print(prefix);
                target.print(content);
            }

        } catch (IOException e) {

            throw new MojoFailureException(e.getMessage(), e);
        }
    }
}
