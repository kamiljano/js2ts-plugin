package fi.janoka.intellij.js2ts.testutils.projects

import java.nio.file.Path

data class TestProjectFile(val path: Path) {

    val isFile get() = this.path.fileName.toString().contains(".")
    val isDirectory get() = !this.isFile
}
