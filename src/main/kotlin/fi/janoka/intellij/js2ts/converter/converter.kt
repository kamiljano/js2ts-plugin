package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.components.Component
import fi.janoka.intellij.js2ts.converter.components.ImportComponent
import fi.janoka.intellij.js2ts.converter.components.UnparsableComponent
import org.apache.commons.io.IOUtils
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val ALL_COMPONENTS = setOf<Component>(
    ImportComponent()
)
private val UNPARSABLE_COMPONENT = UnparsableComponent()

private fun getNewLineSeparator(content: String): String {
    if (content.contains("\r\n")) {
        return "\r\n";
    }
    return "\n";
}

private fun getComponent(line: String): Component {
    val result = ALL_COMPONENTS.find { component -> component.matches(line) }
    if (result != null) {
        return result
    }
    return UNPARSABLE_COMPONENT
}

private fun getTsContent(inputContent: String): String {
    val newLineSeparator = getNewLineSeparator(inputContent)
    return inputContent.split(newLineSeparator).map { line ->
        getComponent(line).replace(line)
    }.joinToString(newLineSeparator)
}

fun convertToTs(project: Project, file: VirtualFile) {
    file.rename(project, file.name.replace(Regex("\\.js$"), ".ts"))
    val inputContent = InputStreamReader(file.inputStream).use { reader -> IOUtils.toString(reader) }
    val tsContent = getTsContent(inputContent)
    OutputStreamWriter(file.getOutputStream(Object())).use { writer -> IOUtils.write(tsContent, writer) }

}
