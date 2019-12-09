package fi.janoka.intellij.js2ts.converter.components

class UseStrictComponent : Component() {

    override val replacers = setOf(
        Replacer(
            Regex("\\s*['\"]use strict['\"];*[\\s]")
        ) { _, _ -> "" }
    )
}
