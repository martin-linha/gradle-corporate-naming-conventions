package com.martinlinha.gradle.corporate.conventions

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * Created by martinlinha on 31.10.16.
 */
class NamingConventionsPluginTest extends Specification {

    @Rule
    final TemporaryFolder projectDir = new TemporaryFolder()
    File buildFile
    File settingsFile

    def testCheckProjectGroup() {
        buildFile = projectDir.newFile('build.gradle')

        given:
        buildFile << """
                plugins {
                     id 'corporate-naming-conventions'
                }
                   """
        when:
        GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withArguments('build')
                .withPluginClasspath()
                .build()
        then:
        UnexpectedBuildFailure ube = thrown()
        ube.message.contains('Project group must be')
    }

    def testCheckProjectVersion() {
        buildFile = projectDir.newFile('build.gradle')
        settingsFile = projectDir.newFile('settings.gradle')

        given:
        settingsFile << """
           rootProject.name = 'corporate-test-project'
        """

        buildFile << """
                plugins {
                     id 'corporate-naming-conventions'
                }
                group = 'com.corporate'

                version = '1.0x.0'
                   """
        when:
        GradleRunner.create()
                .withProjectDir(projectDir.root)
                .withArguments('build')
                .withPluginClasspath()
                .build()
        then:
        UnexpectedBuildFailure ube = thrown()
        ube.message.contains('does not follow semver rules')
    }
}
