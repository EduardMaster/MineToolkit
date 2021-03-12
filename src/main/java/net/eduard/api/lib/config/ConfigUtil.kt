package net.eduard.api.lib.config

object ConfigUtil {
    const val EMPTY_LIST = "[]"
    const val EMPTY_SECTION = "{}"
    const val EMPTY = ""
    const val NO_BUG = "_"
    const val COMMENT = "#"
    const val SECTION = ":"
    const val SECTION_SEPARATOR = "."
    const val LINE_SEPARATOR = "\n"
    const val STR1 = "\""
    const val STR2 = "\'"
    const val LIST_ITEM = "-"


    fun isComment(line: String): Boolean {
        return line.trimStart().startsWith(COMMENT)
    }

    fun isList(line: String): Boolean {
        return line.trimStart().startsWith(LIST_ITEM)
    }

    fun isSection(line: String): Boolean {
        return !isList(line) && !isComment(line) && line.contains(SECTION)
    }

    fun getComment(line: String): String {
        return line.trimStart().replace(COMMENT, EMPTY)
    }

    /**
     * Conta quantos espaços até encontrar uma Letra que não seja espaço
     */
    fun getSpaceAmount(line: String): Int {
        // "         texto"
        var spaceAmount = 0
        for (char in line){
            if (char != ' ')break
            spaceAmount++
        }
        return spaceAmount
    }

    /**
     * Usar builder porque da menos lag
     *
     * @param amount Quantidade
     * @return Texto com varios espaços
     */
    fun getSpace(amount: Int): String {
        if (amount<=0){
            return "";
        }
        return "  ".repeat(amount)
    }

    fun getList(line: String): String {
        return line.trimStart().replace("$LIST_ITEM ", EMPTY)
    }

    fun getKey(currentLine: String): String {
        val line = currentLine
        return  line.trimStart().split(SECTION).toTypedArray()[0]
    }


    fun removeQuotes(text: String): String {
        return text.removeSurrounding(STR1).removeSurrounding(STR2)
    }

    fun getPath(currentPath: String): String {
        var path = currentPath
        if (path.startsWith(COMMENT)) {
            path = path.replace(COMMENT, NO_BUG)
        }
        if (path.startsWith(LIST_ITEM)) {
            path = path.replace(LIST_ITEM, NO_BUG)
        }
        if (path.contains(SECTION)) {
            path = path.replace(SECTION, NO_BUG)
        }
        return path
    }
}