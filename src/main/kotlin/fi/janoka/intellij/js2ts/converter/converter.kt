package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.components.ExportComponent
import fi.janoka.intellij.js2ts.converter.components.ImportComponent
import fi.janoka.intellij.js2ts.converter.components.UseStrictComponent
import org.apache.commons.io.IOUtils
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private val ALL_COMPONENTS = setOf(
    UseStrictComponent(),
    ImportComponent(),
    ExportComponent()
)

private fun getTsContent(inputContent: String): String {
    var result = inputContent;
    ALL_COMPONENTS.forEach {
        result = it.replace(result)
    }
    return result;
}

fun convertToTs(project: Project, file: VirtualFile) {
    file.rename(project, file.name.replace(Regex("\\.js$"), ".ts"))
    val inputContent = InputStreamReader(file.inputStream).use { IOUtils.toString(it) }
    val tsContent = getTsContent(inputContent)
    OutputStreamWriter(file.getOutputStream(Object())).use { IOUtils.write(tsContent, it) }
}
