package fi.janoka.intellij.js2ts.converter.components

abstract class Component {

    protected abstract val replacers: Set<Replacer>

    protected fun getMatchingReplacer(str: String): Replacer? {
        return replacers.firstOrNull { replacer -> replacer.matches(str) }
    }

    open fun matches(str: String): Boolean = getMatchingReplacer(str) != null
    open fun replace(str: String): String = getMatchingReplacer(str)?.replace(str) ?: str
}
