package fi.janoka.intellij.js2ts.testutils.projects

import fi.janoka.intellij.js2ts.testutils.projects.files.DeployedFile
import fi.janoka.intellij.js2ts.testutils.projects.files.ResourceFile
import fi.janoka.intellij.js2ts.testutils.projects.files.TestProjectFile
import org.codehaus.plexus.util.IOUtil
import java.io.*
import java.util.ArrayList
import java.nio.file.Path


class TestProjectManager(private val projectName: String, private val targetDir: Path? = null) {

    private fun listResourceFiles(projectRoot: Path, root: Path): List<TestProjectFile> {
        val files = ArrayList<TestProjectFile>()

        getResourceAsStream(root.toString()).use { input ->
            BufferedReader(InputStreamReader(input)).use { br ->
                var resource: String?

                while (true) {
                    resource = br.readLine()
                    if (resource == null) break

                    val file = ResourceFile(
                        projectRoot,
                        Path.of(root.toString(), resource)
                    )
                    files.add(file)

                    if (file.isDirectory) {
                        files.addAll(listResourceFiles(projectRoot, file.path))
                    }
                }
            }
        }

        return files
    }

    private fun getResourceAsStream(resource: String): InputStream {
        val input = getContextClassLoader().getResourceAsStream(resource)

        return input ?: javaClass.getResourceAsStream(resource)
    }

    private fun getContextClassLoader(): ClassLoader {
        return Thread.currentThread().contextClassLoader
    }

    private fun deployFiles(projectResourceRoot: Path, files: List<TestProjectFile>) {
        files.forEach { file ->
            if (file.isFile) {
                val targetFilePath = Path.of(this.targetDir.toString(), file.path.toString().replace(projectResourceRoot.toString(), ""))
                targetFilePath.parent.toFile().mkdirs()
                FileOutputStream(targetFilePath.toFile()).use { outputFile ->
                    getResourceAsStream(file.path.toString()).use { inputFile ->
                        IOUtil.copy(inputFile, outputFile)
                    }
                }
            }
        }
    }

    private val sourceProjectPath: Path
        get() = Path.of("/projects", projectName, "js")

    private val expectedProjectPath: Path
        get() = Path.of("/projects", projectName, "ts")

    fun deployProject() {
        targetDir?.toFile()?.deleteRecursively()
        val projectFiles = listResourceFiles(sourceProjectPath, sourceProjectPath)
        deployFiles(sourceProjectPath, projectFiles)
    }

    private fun listDeployedFiles(root: File): List<TestProjectFile> {
        val result = ArrayList<TestProjectFile>()
        root.listFiles().forEach { file ->
            if (file.isFile) {
                result.add(
                    DeployedFile(
                        root.toPath(),
                        file.toPath()
                    )
                )
            } else if (file.isDirectory) {
                result.addAll(listDeployedFiles(file))
            }
        }
        return result;
    }

    val deployedProject: TestProject
        get() = TestProject(targetDir!!, listDeployedFiles(targetDir.toFile()))

    val expectedProject: TestProject
        get() = TestProject(expectedProjectPath, listResourceFiles(expectedProjectPath, expectedProjectPath))

    private fun loadResourceFile(filePath: Path): String =
        InputStreamReader(getResourceAsStream(filePath.toString())).use { reader -> IOUtil.toString(reader) }

    fun loadProjectFile(filePath: Path): TestProjectFileLoaded {
        return TestProjectFileLoaded(
            js = loadResourceFile(Path.of(sourceProjectPath.toString(), filePath.toString() + ".js")),
            ts = loadResourceFile(Path.of(expectedProjectPath.toString(), filePath.toString() + ".ts"))
        )
    }
}
