/*
 * Copyright (c) 2020 EITCO GmbH
 * All rights reserved.
 *
 * Created on 06.05.2020
 *
 */
package de.eitco.cicd.build.utilities;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * This goal generates a text file.
 */
@Mojo(name = "write-file", requiresProject = false)
public class WriteFileMojo extends AbstractMojo {

    /**
     * The name of the file to generate.
     */
    @Parameter(required = true, property = "target.file")
    public File targetFile;

    /**
     * Whether content should be appended to the file - or the file should be overridden.
     */
    @Parameter(defaultValue = "false", property = "append.file")
    public boolean append;

    /**
     * Whether the execution should be skipped.
     */
    @Parameter(defaultValue = "false", property = "write.file.skip")
    public boolean skip;

    /**
     * The content to write to the file.
     */
    @Parameter(required = true, property = "file.content")
    public String content;

    @Override
    public void execute() throws MojoFailureException {

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
