package fi.janoka.intellij.js2ts.converter.components

private val LINE_WHITE_ENDING = Regex("(\\s)*$")
private val LINE_WHITE_BEGINNING = Regex("^(\\s)*")

abstract class Component {

    protected abstract val replacers: Set<Replacer>

    protected fun findLineWhiteEnding(match: MatchResult): String = LINE_WHITE_ENDING.find(match.value)?.value ?: ""
    protected fun findLineWhiteBeginning(match: MatchResult): String = LINE_WHITE_BEGINNING.find(match.value)?.value ?: ""

    fun replace(str: String): String {
        var result = str

        replacers.forEach { replacer -> result = replacer.replace(result) }

        return result
    }
}
