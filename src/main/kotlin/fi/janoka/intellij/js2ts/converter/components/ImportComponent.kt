package fi.janoka.intellij.js2ts.converter.components

class ImportComponent : Component() {

    override val replacers = setOf(

        // const abc = require("lol.js");
        Replacer(
            Regex("(const|let|var) ([a-zA-Z0-9_$]+)[\\s\\t]*=[\\s\\t]*require[\\s\\t]*\\([\\s\\t]*(['\"][a-zA-Z0-9@/+-_\\.]+['\"])[\\s\\t]*\\)[\\s\\t;]*")
        )  { match -> "import " + match.groups[2]!!.value + " from " + match.groups[3]!!.value + ";" },

        // const abc = require("lol").abc
        // const abc = require("lol").default
        Replacer(
            Regex("(const|let|var) ([a-zA-Z0-9_$]+)[\\s\\t]*=[\\s\\t]*require[\\s\\t]*\\([\\s\\t]*(['\"][a-zA-Z0-9@/+-_\\.]+['\"])[\\s\\t]*\\)[\\s\\t]*([a-zA-Z0-9_$\\s\\t\\.]+)[\\s\\t;]*")
        )  { match ->
            val path = match.groups[4]!!.value.replaceFirst(".", "")
            if (path == "default") {
                return@Replacer "import " + match.groups[2]!!.value + " from " + match.groups[3]!!.value + ";"
            }
            "import { " + path + " as " + match.groups[2]!!.value + " } from " + match.groups[3]!!.value + ";"
        }
    )
}
