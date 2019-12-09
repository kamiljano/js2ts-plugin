package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.components.ExportComponent
import fi.janoka.intellij.js2ts.converter.components.ImportComponent
import fi.janoka.intellij.js2ts.converter.components.UseStrictComponent

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
    val document = FileDocumentManager.getInstance().getDocument(file)!!
    val inputContent = document.text
    val tsContent = getTsContent(inputContent)
    document.setText(tsContent)
    FileDocumentManager.getInstance().saveAllDocuments()
}
