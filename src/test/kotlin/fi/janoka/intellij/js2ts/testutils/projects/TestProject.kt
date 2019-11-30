package fi.janoka.intellij.js2ts.testutils.projects

import fi.janoka.intellij.js2ts.testutils.projects.files.TestProjectFile
import java.nio.file.Path
import java.util.stream.Collectors
import java.util.stream.Collectors.toList

data class TestProject(private val projectRoot: Path, private val files: Collection<TestProjectFile>) {

    val relativeToRootFiles: Map<Path, TestProjectFile>
        get() = files.stream().collect(Collectors.toMap(
            TestProjectFile::relativePath,
            { file -> file }
        ))

    /**
     * Returns a list of all files that that {@code comparisonProject} contains and this project doesn't
     */
    fun listMissingFiles(comparisonProject: TestProject): List<TestProjectFile> {
        val thisProjectFiles = relativeToRootFiles
        return comparisonProject.relativeToRootFiles.entries.stream()
            .filter { entry -> !thisProjectFiles.containsKey(entry.key)}
            .map { entry -> entry.value }
            .collect(toList())
    }
}
