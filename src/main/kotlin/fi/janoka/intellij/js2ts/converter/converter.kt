package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.components.ExportComponent
import fi.janoka.intellij.js2ts.converter.components.ImportComponent
import org.apache.commons.io.IOUtils
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val ALL_COMPONENTS = setOf(
    ImportComponent(),
    ExportComponent()
)

private fun getTsContent(inputContent: String): String {
    var result = inputContent;
    ALL_COMPONENTS.forEach { component ->
        result = component.replace(result)
    }
    return result;
}

fun convertToTs(project: Project, file: VirtualFile) {
    file.rename(project, file.name.replace(Regex("\\.js$"), ".ts"))
    val inputContent = InputStreamReader(file.inputStream).use { reader -> IOUtils.toString(reader) }
    val tsContent = getTsContent(inputContent)
    OutputStreamWriter(file.getOutputStream(Object())).use { writer -> IOUtils.write(tsContent, writer) }
}
