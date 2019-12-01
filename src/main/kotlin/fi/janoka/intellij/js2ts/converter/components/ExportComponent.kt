package fi.janoka.intellij.js2ts.converter.components

class ExportComponent : Component() {

    override val replacers: Set<Replacer>
        get() = setOf(
            // module.exports =
            Replacer(
                Regex("\\s*module\\s*\\.\\s*exports\\s*=\\s*"),
                this::exportDefault
            ),
            // exports =
            Replacer(
                Regex("\\s*exports\\s*=\\s*"),
                this::exportDefault
            ),

            // module.exports.x =
            Replacer(
                Regex("\\s*module\\s*\\.\\s*exports\\s*\\.([\\w_\\$]+)\\s*=\\s*"),
                this::exportConst
            ),
            // exports.x =
            Replacer(
                Regex("\\s*exports\\s*\\.([\\w_\\$]+)\\s*=\\s*"),
                this::exportConst
            )
        )

    private fun exportDefault(match: MatchResult, data: FileData): String = findLineWhiteBeginning(match) + "export default "
    private fun exportConst(match: MatchResult, data: FileData): String = findLineWhiteBeginning(match) + "export const " + match.groups[1]!!.value + " = "
}
