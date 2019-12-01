package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.application.ApplicationManager
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import fi.janoka.intellij.js2ts.testutils.projects.TestProjectDiffAssertj.Companion.assertThat
import fi.janoka.intellij.js2ts.testutils.projects.TestProjectManager
import java.nio.file.Path

internal class ConverterKtTest : BasePlatformTestCase() {

    private fun testConversion(projectName: String, fileToConvert: Path) {
        val projectManager = TestProjectManager(projectName, Path.of(project.basePath))
        projectManager.deployProject()
        ApplicationManager.getApplication().runWriteAction {
            val file = UsefulTestCase.refreshAndFindFile(Path.of(project.basePath, fileToConvert.toString()).toFile())
            convertToTs(project, file!!)
        }

        assertThat(projectManager.deployedProject)
            .matches(projectManager.expectedProject)
    }

    fun testConvertToTs_singleFileProject_shouldConvertImports() {
        testConversion("one-file-project-with-imports", Path.of("imports.js"))
    }

    fun testConvertToTs_singleFileProject_shouldConvertAllExports() {
        testConversion("one-file-project-with-multiple-exports-of-different-types", Path.of("various-exports.js"))
    }

    fun testConvertToTs_singleFileProject_shouldConvertAllImportsAndExports() {
        testConversion("one-file-project-with-multiple-imports-and-exports", Path.of("various-exports-and-imports.js"))
    }
}
