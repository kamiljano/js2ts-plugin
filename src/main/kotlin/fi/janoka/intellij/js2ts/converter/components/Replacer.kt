package fi.janoka.intellij.js2ts.converter.components

private val INDENTATION_REGEX = Regex("^(\\s+).+$")

data class FileData(val lineSeparator: String, val indentation: String)

class Replacer(private val regex: Regex, private val function: (matchResult: MatchResult, data: FileData) -> String) {

    private fun findLineSeparator(str: String): String {
        if (str.contains("\r\n")) {
            return "\r\n"
        }
        return "\n"
    }

    private fun findIndentation(str: String): String {
        return str.split("\n").map { line ->
            val match = INDENTATION_REGEX.find(line) ?: return@map null
            match.groupValues[1]
        }
            .filterNotNull().minBy { indentation ->
                indentation.length
            } ?: ""
    }

    fun replace(str: String): String {
        val data = FileData(
            lineSeparator = findLineSeparator(str),
            indentation = findIndentation(str)
        )
        var result = str
        while(regex.findAll(result).count() > 0) {
            result = result.replace(regex) { match -> function(match, data) }
        }
        return result
    }
}
