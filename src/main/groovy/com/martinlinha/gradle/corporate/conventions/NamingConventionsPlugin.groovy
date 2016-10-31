package com.martinlinha.gradle.corporate.conventions

import com.github.zafarkhaja.semver.ParseException
import com.github.zafarkhaja.semver.Version
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

public class NamingConventionsPlugin implements Plugin<Project> {
    private static final PROJECT_NAME_PREFIX = 'corporate-'
    private static final PROJECT_GROUP = 'com.corporate'

    @Override
    public void apply(Project project) {
        project.allprojects { proj ->
            proj.afterEvaluate {
                checkProjectGroup(proj)
                checkProjectName(proj)
                checkProjectVersion(proj)
            }
        }
    }

    private void checkProjectGroup(Project project) {
        if (!PROJECT_GROUP.equals(project.group)) {
            throw new GradleException("$project: Project group must be $PROJECT_GROUP.")
        }
    }

    private void checkProjectName(Project project) {
        if (!project.name.startsWith(PROJECT_NAME_PREFIX)) {
            throw new GradleException("$project: Project name must start with $PROJECT_NAME_PREFIX.")
        }
    }

    private void checkProjectVersion(Project project) {
        try {
            project.version = Version.valueOf(project.version)
        }
        catch (ParseException pe) {
            throw new GradleException("$project: Project's version $project.version does not follow semver rules.", pe)
        }
    }
}