package fi.janoka.intellij.js2ts

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.convertToTs

class ConvertFileAction : AnAction() {

    private companion object {
        const val JAVA_SCRIPT_EXTENSION = "js";
    }

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        this.templatePresentation.isEnabledAndVisible = virtualFile.extension?.toLowerCase()?.equals(JAVA_SCRIPT_EXTENSION) ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        //TODO: support for CommonDataKeys.VIRTUAL_FILE_ARRAY
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        var project = e.getData(CommonDataKeys.PROJECT) as Project
        convertToTs(project, virtualFile)
    }
}
