/*
 * Copyright (c) 2021 EITCO GmbH
 * All rights reserved.
 *
 * Created on 01.06.2021
 *
 */
package de.eitco.cicd.build.utilities;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.*;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilderException;
import org.apache.maven.shared.dependency.graph.DependencyNode;
import org.apache.maven.shared.dependency.graph.traversal.DependencyNodeVisitor;
import org.apache.maven.shared.transfer.artifact.ArtifactCoordinate;
import org.apache.maven.shared.transfer.artifact.DefaultArtifactCoordinate;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolver;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResolverException;
import org.apache.maven.shared.transfer.artifact.resolve.ArtifactResult;

import java.io.File;
import java.util.List;

/**
 * This goal generates properties, containing the version and file system path of every direct dependency the
 * current project has. For every dependency, two properties are generated: 'maven.dependency.&lt;dependency-conflict-id&gt;.file'
 * set to the absolute file system path of the dependency and 'maven.dependency.&lt;dependency-conflict-id&gt;.version'
 * set to the version of the dependency.
 * The dependency conflict id is constructed as follows: &lt;groupId&gt;:&lt;artifactId&gt;:&lt;type&gt;[&lt;classifier&gt;]
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

    @Parameter(property = "dependencies-version-properties.add-files", defaultValue = "false")
    private boolean addFiles;

    @Parameter(defaultValue = "${session}", readonly = true, required = true)
    private MavenSession session;

    @Parameter(defaultValue = "${project.remoteArtifactRepositories}", readonly = true, required = true)
    private List<ArtifactRepository> remoteRepositories;

    @Component(hint = "default")
    private DependencyGraphBuilder dependencyGraphBuilder;

    @Component
    private ArtifactResolver artifactResolver;

    @Override
    public void execute() throws MojoExecutionException {

        if (skip) {

            getLog().info("skipping dependencies version properties ");
            return;
        }

        ProjectBuildingRequest buildingRequest =
                new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());

        buildingRequest.setProject(project);

        try {

            DependencyNode dependencyNode = dependencyGraphBuilder.buildDependencyGraph(buildingRequest, null);

            dependencyNode.accept(new DependencyNodeVisitor() {
                @Override
                public boolean visit(DependencyNode node) {

                    getLog().debug("setting properties maven.dependency." + node.getArtifact().getDependencyConflictId() + ".[file/version]");

                    if (addFiles) {

                        File file = node.getArtifact().getFile();

                        if (file == null) {

                            ProjectBuildingRequest buildingRequest =
                                    new DefaultProjectBuildingRequest(session.getProjectBuildingRequest());

                            buildingRequest.setRemoteRepositories(remoteRepositories);

                            ArtifactCoordinate coordinate = getArtifactCoordinate(node);

                            try {

                                ArtifactResult artifactResult = artifactResolver.resolveArtifact(buildingRequest, coordinate);

                                file = artifactResult.getArtifact().getFile();

                            } catch (ArtifactResolverException e) {

                                getLog().warn("unable to resolve artifact " + coordinate, e);
                            }
                        }

                        if (file != null) {

                            project.getProperties().setProperty("maven.dependency." + node.getArtifact().getDependencyConflictId() + ".file", file.getAbsolutePath());
                        }
                    }
                    project.getProperties().setProperty("maven.dependency." + node.getArtifact().getDependencyConflictId() + ".version", node.getArtifact().getVersion());


                    return true;
                }

                @Override
                public boolean endVisit(DependencyNode node) {
                    return true;
                }
            });


        } catch (DependencyGraphBuilderException e) {

            throw new MojoExecutionException(e);
        }

    }

    private static ArtifactCoordinate getArtifactCoordinate(DependencyNode node) {

        DefaultArtifactCoordinate coordinate = new DefaultArtifactCoordinate();
        coordinate.setGroupId(node.getArtifact().getGroupId());
        coordinate.setArtifactId(node.getArtifact().getArtifactId());
        coordinate.setVersion(node.getArtifact().getVersion());
        coordinate.setClassifier(node.getArtifact().getClassifier());
        coordinate.setExtension(node.getArtifact().getType());

        return coordinate;
    }
}
