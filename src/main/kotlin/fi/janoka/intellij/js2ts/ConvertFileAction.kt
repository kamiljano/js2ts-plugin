package fi.janoka.intellij.js2ts

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import fi.janoka.intellij.js2ts.converter.convertToTs
import java.util.*

private const val JAVA_SCRIPT_EXTENSION = "js"

class ConvertFileAction : AnAction() {

    override fun update(e: AnActionEvent) {
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = virtualFile?.extension?.toLowerCase()?.equals(JAVA_SCRIPT_EXTENSION) ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        //TODO: support for CommonDataKeys.VIRTUAL_FILE_ARRAY
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) as VirtualFile
        var project = e.getData(CommonDataKeys.PROJECT) as Project
        CommandProcessor.getInstance().executeCommand(project, {
            ApplicationManager.getApplication().runWriteAction {
                convertToTs(project, virtualFile)
            }
        }, "Convert to TypeScript", UUID.randomUUID().toString())
    }
}
