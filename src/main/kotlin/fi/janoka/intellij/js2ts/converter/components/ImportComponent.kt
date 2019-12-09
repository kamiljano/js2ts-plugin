package fi.janoka.intellij.js2ts.converter.components

private const val MAX_VARIABLES_IN_ONE_LINE = 3;
private val DESTRUCTURED_DEFAULT = Regex("[\\W]default[\\s]*:\\s*([\\w_\\$]+)")

class ImportComponent : Component() {

    override val replacers = setOf(

        // const abc = require("lol.js");
        Replacer(
            Regex("(const|let|var)\\s+([\\w_\\$]+)\\s*=\\s*require\\s*\\(\\s*(['\"][\\w@/+-_\\.]+['\"])\\s*\\)[\\s;]*\n")
        )  { match, _ -> "import " + match.groups[2]!!.value + " from " + match.groups[3]!!.value + ";" + findLineWhiteEnding(match) },

        // const abc = require("lol").abc
        // const abc = require("lol").default
        Replacer(
            Regex("(const|let|var)[\\s]+([\\w_\\$]+)[\\s]*=[\\s]*require[\\s]*\\([\\s]*(['\"][\\w@/+-_\\.]+['\"])[\\s]*\\)[\\s]*([\\w_\\$\\s\\.]+)\\s*([\\w\\(\\)\"'`+\\-_\$\\{\\}\\s]*)[\\s;]*\n")
        )  { match, data ->
            val path = match.groups[4]!!.value.replaceFirst(".", "")
            if (path == "default") {
                return@Replacer "import " + match.groups[2]!!.value + " from " + match.groups[3]!!.value + ";" + findLineWhiteEnding(match)
            }
            val parameters = match.groups[5]?.value ?: ""
            if (!path.contains(".") && parameters.isEmpty()) {
                return@Replacer "import { " + path + " as " + match.groups[2]!!.value + " } from " + match.groups[3]!!.value + ";" + findLineWhiteEnding(match)
            }
            val pathComponents = path.split(".").map { it.trim() }
            return@Replacer "import { " + pathComponents[0] + " } from " + match.groups[3]!!.value + ";" + data.lineSeparator + "const " + match.groups[2]!!.value + " = " + path + parameters + ";" + findLineWhiteEnding(match)
        },

        // const {dep4} = require ( 'dep4' )
        // const {a, b, c} = require ( 'dep4' )
        // const {default: a, b, lol: c} = require ( 'dep4' )
        Replacer(
            Regex("(const|let|var)[\\s]+\\{([:\\w_\\$,\\s]+)\\}[\\s]*=[\\s]*require[\\s]*\\([\\s]*(['\"][\\w@/+-_\\.]+['\"])[\\s]*\\)[\\s;]*\n")
        ) { match, data ->
            val variables = match.groups[2]!!.value
            val dependency = match.groups[3]!!.value
            val defaultVariableMatch = DESTRUCTURED_DEFAULT.find(variables)
            val importVariablesList = variables.split(",")
                .filter { variable -> DESTRUCTURED_DEFAULT.find(variable) == null }
                .map { variable -> variable.trim()}

            var result = "import "
            if (defaultVariableMatch != null) {
                result += defaultVariableMatch.groups[1]!!.value
                if (importVariablesList.isNotEmpty()) {
                    result += ", "
                } else {
                    result += " "
                }
            }

            result + formatVariableList(importVariablesList, data) + "from " + dependency + ";" + findLineWhiteEnding(match)
        }
    )

    private fun formatVariableList(variableList: List<String>, data: FileData): String {
        if (variableList.isEmpty()) return ""

        if (variableList.size < MAX_VARIABLES_IN_ONE_LINE) {
            return "{ " + variableList.map {variable -> formatDestructuredVariable(variable)} .joinToString(", ") + " } "
        }

        return "{" + data.lineSeparator + variableList.map { variable ->
            data.indentation + formatDestructuredVariable(variable)
        }.joinToString("," + data.lineSeparator) + data.lineSeparator + "} "
    }

    private fun formatDestructuredVariable(variable: String): String {
        if (variable.contains(":")) {
            val parts = variable.split(":").map { it.trim() }
            return parts[0] + " as " + parts[1]
        }
        return variable.trim()
    }
}
