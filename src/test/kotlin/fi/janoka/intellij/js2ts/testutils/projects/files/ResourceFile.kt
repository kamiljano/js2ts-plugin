package fi.janoka.intellij.js2ts.testutils.projects.files

import org.apache.commons.io.IOUtils
import java.io.InputStreamReader
import java.nio.file.Path

class ResourceFile(projectRoot: Path, path: Path) : TestProjectFile(projectRoot, path, path.fileName.toString().contains(".")) {

    override val content: String
        get() = IOUtils.toString(InputStreamReader(ResourceFile::class.java.getResourceAsStream(path.toString())))

}
