package fi.janoka.intellij.js2ts.converter.components

class Replacer(private val regex: Regex, private val function: (matchResult: MatchResult) -> String) {
    fun matches(str: String): Boolean = regex.matches(str)
    fun replace(str: String): String = function(regex.matchEntire(str)!!)
}
