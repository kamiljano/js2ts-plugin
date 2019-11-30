package fi.janoka.intellij.js2ts.testutils.projects.files

import org.apache.commons.lang.builder.HashCodeBuilder
import java.nio.file.Path
import kotlin.reflect.full.isSubclassOf

abstract class TestProjectFile(private val projectRoot: Path, val path: Path, val isFile: Boolean) {

    val isDirectory get() = !this.isFile

    val relativePath: Path
        get() = Path.of(path.toString().replaceFirst(projectRoot.toString(), ""))

    override fun toString(): String {
        return path.toString()
    }

    abstract val content: String

    override fun equals(other: Any?): Boolean {
        return other != null
                && other::class.isSubclassOf(TestProjectFile::class)
                && this.isFile == (other as TestProjectFile).isFile
                && this.relativePath == other.relativePath
                && (!this.isFile || this.content == other.content)
    }

    override fun hashCode(): Int {
        val builder = HashCodeBuilder(12, 237)
            .append(isFile)
            .append(relativePath)

        if (isFile) {
            builder.append(content)
        }
        return builder.toHashCode()
    }
}
