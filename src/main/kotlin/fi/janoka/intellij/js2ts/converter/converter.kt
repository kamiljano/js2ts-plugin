package fi.janoka.intellij.js2ts.converter

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

fun convertToTs(project: Project, file: VirtualFile) {

    file.rename(project, file.name.replace(Regex("\\.js$"), ".ts"))

}
