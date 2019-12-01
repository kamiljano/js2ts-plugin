package fi.janoka.intellij.js2ts.converter.components

import fi.janoka.intellij.js2ts.testutils.projects.TestProjectManager
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Path

private val component = ExportComponent()

internal class ExportComponentTest {

    @Test
    fun testDefaultExportObject() {
        val file = TestProjectManager("one-file-project-with-module-default-export")
            .loadProjectFile(Path.of("module-default.export"))

        val convertedJs = component.replace(file.js)

        assertThat(convertedJs).isEqualTo(file.ts)
    }

    @Test
    fun testDefaultExportObjectWithoutModule() {
        val file = TestProjectManager("one-file-project-with-direct-export")
            .loadProjectFile(Path.of("default.export"))

        val convertedJs = component.replace(file.js)

        assertThat(convertedJs).isEqualTo(file.ts)
    }

    @Test
    fun testDefaultExportFunction() {
        val file = TestProjectManager("one-file-project-with-default-export-as-function")
            .loadProjectFile(Path.of("module-default.export"))

        val convertedJs = component.replace(file.js)

        assertThat(convertedJs).isEqualTo(file.ts)
    }

    @Test
    fun testConstExports() {
        val file = TestProjectManager("one-file-project-with-multiple-parameter-exports")
            .loadProjectFile(Path.of("module.export"))

        val convertedJs = component.replace(file.js)

        assertThat(convertedJs).isEqualTo(file.ts)
    }

}
