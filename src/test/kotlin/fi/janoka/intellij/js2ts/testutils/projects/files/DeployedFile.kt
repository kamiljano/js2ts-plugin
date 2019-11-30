package fi.janoka.intellij.js2ts.testutils.projects.files

import com.intellij.util.io.isFile
import org.apache.commons.io.IOUtils
import java.io.FileInputStream
import java.io.InputStreamReader
import java.nio.file.Path

class DeployedFile(projectRoot: Path, path: Path) : TestProjectFile(projectRoot, path, path.isFile()) {

    override val content: String
        get() = IOUtils.toString(InputStreamReader(FileInputStream(path.toFile())))

}
