package fi.janoka.intellij.js2ts.converter.components

class UnparsableComponent : Component() {
    override val replacers: Set<Replacer>
        get() = emptySet()

    override fun matches(str: String): Boolean = true

}
