package fi.janoka.intellij.js2ts.testutils.projects

import org.codehaus.plexus.util.IOUtil
import java.io.BufferedReader
import java.io.FileOutputStream
import java.util.ArrayList
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Path


class ProjectDeployer(private val targetDir: Path) {

    private fun listResourceFiles(root: Path): List<TestProjectFile> {
        val files = ArrayList<TestProjectFile>()

        getResourceAsStream(root.toString()).use { input ->
            BufferedReader(InputStreamReader(input)).use { br ->
                var resource: String?

                while (true) {
                    resource = br.readLine()
                    if (resource == null) break

                    val file = TestProjectFile(Path.of(root.toString(), resource))
                    files.add(file)

                    if (file.isDirectory) {
                        files.addAll(listResourceFiles(file.path))
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

    fun deployProject(projectName: String) {
        val projectResourceRoot = Path.of("/projects", projectName, "project")
        val projectFiles = listResourceFiles(projectResourceRoot)
        deployFiles(projectResourceRoot, projectFiles)
    }

}
