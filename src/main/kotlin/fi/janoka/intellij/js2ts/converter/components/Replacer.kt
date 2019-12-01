package fi.janoka.intellij.js2ts.converter.components

data class FileData(val lineSeparator: String)

class Replacer(private val regex: Regex, private val function: (matchResult: MatchResult, data: FileData) -> String) {

    private fun findLineSeparator(str: String): String {
        if (str.contains("\r\n")) {
            return "\r\n"
        }
        return "\n"
    }

    fun replace(str: String): String {
        val data = FileData(lineSeparator = findLineSeparator(str))
        var result = str
        while(regex.findAll(result).count() > 0) {
            result = result.replace(regex) { match -> function(match, data) }
        }
        return result
    }
}
