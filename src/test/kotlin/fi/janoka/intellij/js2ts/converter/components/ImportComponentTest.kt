package fi.janoka.intellij.js2ts.converter.components

import fi.janoka.intellij.js2ts.testutils.projects.TestProjectManager
import org.assertj.core.api.Assertions
import org.junit.Test
import java.nio.file.Path

private val component = ImportComponent()

internal class ImportComponentTest {

    @Test
    fun testSimpleImport() {
        val file = TestProjectManager("one-file-project-with-simple-import")
            .loadProjectFile(Path.of("imports"))

        val convertedJs = component.replace(file.js)

        Assertions.assertThat(convertedJs).isEqualTo(file.ts)
    }

    @Test
    fun testImportExecution() {
        val file = TestProjectManager("one-file-project-with-import-execution")
            .loadProjectFile(Path.of("imports"))

        val convertedJs = component.replace(file.js)

        Assertions.assertThat(convertedJs).isEqualTo(file.ts)
    }

    @Test
    fun testImportExecutionInPath() {
        val file = TestProjectManager("one-file-project-with-import-execution-path")
            .loadProjectFile(Path.of("imports"))

        val convertedJs = component.replace(file.js)

        Assertions.assertThat(convertedJs).isEqualTo(file.ts)
    }

}
