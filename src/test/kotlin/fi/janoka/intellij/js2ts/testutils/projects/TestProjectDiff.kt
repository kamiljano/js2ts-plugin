package fi.janoka.intellij.js2ts.testutils.projects

import fi.janoka.intellij.js2ts.testutils.projects.files.TestProjectFile

data class TestProjectDiff(
    val missingFiles: List<TestProjectFile>,
    val unexpectedFiles: List<TestProjectFile>,
    val unequalFiles: List<TestProjectFileDiff>
)
