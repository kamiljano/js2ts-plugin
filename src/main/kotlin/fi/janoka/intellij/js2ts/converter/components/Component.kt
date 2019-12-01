package fi.janoka.intellij.js2ts.converter.components

abstract class Component {

    protected abstract val replacers: Set<Replacer>

    fun replace(str: String): String {
        var result = str

        replacers.forEach { replacer -> result = replacer.replace(result) }

        return result
    }
}
