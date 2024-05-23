/*
 * Copyright (c) 2021 EITCO GmbH
 * All rights reserved.
 *
 * Created on 01.06.2021
 *
 */
package de.eitco.cicd.build.utilities;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.util.Set;

/**
 * This goal generates properties, containing the version and file system path of every direct dependency the
 * current project has. For every dependency, two properties are generated: 'maven.dependency.&lt;dependency-conflict-id&gt;.file'
 * set to the absolute file system path of the dependency and 'maven.dependency.&lt;dependency-conflict-id&gt;.version'
 * set to the version of the dependency.
 * The dependency conflict id is constructed as follows: &lt;groupId&gt;:&lt;artifactId&gt;:&lt;type&gt;[&lt;classifier&gt;]
 *
 */
@Mojo(name = "dependencies-version-properties", requiresDependencyResolution = ResolutionScope.TEST, defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class DependenciesVersionPropertiesMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private MavenProject project;

    /**
     * Skip plugin execution completely.
     */
    @Parameter(property = "dependencies-version-properties.skip", defaultValue = "false")
    private boolean skip;

    @Override
    public void execute() {

        if (skip) {

            getLog().info("skipping dependencies version properties ");
            return;
        }

        Set<Artifact> artifacts = project.getDependencyArtifacts();

        for (Artifact artifact : artifacts) {

            getLog().debug("setting properties maven.dependency." + artifact.getDependencyConflictId() + ".[file/version]");

            project.getProperties().setProperty("maven.dependency." + artifact.getDependencyConflictId() + ".file", artifact.getFile().getAbsolutePath());
            project.getProperties().setProperty("maven.dependency." + artifact.getDependencyConflictId() + ".version", artifact.getVersion());
        }
    }
}
