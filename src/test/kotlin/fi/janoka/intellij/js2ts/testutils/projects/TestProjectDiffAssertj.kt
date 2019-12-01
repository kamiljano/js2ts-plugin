package fi.janoka.intellij.js2ts.testutils.projects

import fi.janoka.intellij.js2ts.testutils.projects.files.TestProjectFile
import org.assertj.core.api.AbstractAssert

class TestProjectDiffAssertj(actual: TestProject) :
    AbstractAssert<TestProjectDiffAssertj, TestProject>(actual, TestProjectDiffAssertj::class.java) {

    companion object {
       fun assertThat(actual: TestProject): TestProjectDiffAssertj {
           return TestProjectDiffAssertj(actual)
       }
    }

    private fun compareFileContent(expectedProject: TestProject, convertedProject: TestProject): List<TestProjectFileDiff> {
        val convertedProjectFiles = convertedProject.relativeToRootFiles
        val result = ArrayList<TestProjectFileDiff>()
        expectedProject.relativeToRootFiles.forEach { entry ->
            if (convertedProjectFiles.containsKey(entry.key) && !entry.value.equals(convertedProjectFiles[entry.key])) {
                result.add(TestProjectFileDiff(convertedProjectFiles[entry.key]!!, entry.value))
            }
        }
        return result
    }

    private fun generateDiff(expectedProject: TestProject, convertedProject: TestProject): TestProjectDiff {
        return TestProjectDiff(
            convertedProject.listMissingFiles(expectedProject),
            expectedProject.listMissingFiles(convertedProject),
            compareFileContent(expectedProject, convertedProject)
        )
    }

    private fun toFileLine(file: TestProjectFile) = "\t* " + file.relativePath + "\r\n"

    private fun fileContentToMessagePart(file: TestProjectFile): String {
        return file.relativePath.toString() + ":\r\n\t\t" + file.content.replace("\r\n", "\n")
            .replace("\n", "\n\t\t")
            .replace("\n", "\r\n")
    }

    fun matches(expected: TestProject): TestProjectDiffAssertj {
        val diff = generateDiff(expected, actual)

        var result = ""
        if (diff.missingFiles.isNotEmpty()) {
            result = "The following files are missing from the converted project:\r\n"
            diff.missingFiles.forEach { file -> result += toFileLine(file) }
        }

        if (diff.unexpectedFiles.isNotEmpty()) {
            result += "The following files were found but were not expected:\r\n"
            diff.unexpectedFiles.forEach { file -> result += toFileLine(file) }
        }

        if (diff.unequalFiles.isNotEmpty()) {
            result += "The following files had non-matching contents:\r\n"
            diff.unequalFiles.forEach { fileDiff ->
                result += "\tThe actual file - " + fileContentToMessagePart(fileDiff.actual) + "\r\n\tThe expected file - " + fileContentToMessagePart(fileDiff.expected)
            }
        }

        if (result.isNotBlank()) {
            failWithMessage(result)
        }

        return this
    }
}
