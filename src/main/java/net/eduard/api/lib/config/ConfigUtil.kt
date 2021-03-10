package net.eduard.api.lib.config

object ConfigUtil {
    const val EMPTY_LIST = "[]"
    const val EMPTY_SECTION = "{}"
    const val SPACE = " "
    const val EMPTY = ""
    const val DOLLAR = "$"
    const val COMMENT = "#"
    const val SECTION = ":"
    const val SECTION_SEPARATOR = "."
    const val LINE_SEPARATOR = "\n"
    const val STR1 = "\""
    const val STR2 = "\'"
    const val LIST_ITEM = "- "


    fun isComment(line: String): Boolean {
        return line.replace(SPACE, EMPTY).startsWith(COMMENT)
    }

    fun isList(line: String): Boolean {
        return line.replace(SPACE, EMPTY).startsWith(LIST_ITEM)
    }

    fun isSection(line: String): Boolean {
        return !isList(line) && !isComment(line) && line.contains(SECTION)
    }

    fun getComment(line: String): String {
        return line.replace(COMMENT, EMPTY)
    }

    fun getSpaceAmount(line: String): Int {
        return line.count { it == ' ' }
    }
     fun getValue(currentLine: String, space: String): String {
        var line = currentLine
        line = line.replaceFirst(space, EMPTY)
        if (line.endsWith(SECTION)) {
            return ""
        }
        return line.split(SECTION)[1]
     }
    /**
     * Usar builder porque da menos lag
     *
     * @param amount Quantidade
     * @return Texto com varios espaços
     */
    fun getSpace(amount: Int): String {
        return "  ".repeat(amount)
    }

    fun getList(line: String): String {
        return line.replaceFirst(LIST_ITEM, EMPTY)
    }

    fun getKey(currentLine: String, space: String): String {
        var line = currentLine
        line = line.replaceFirst(space, EMPTY)
        return line.split(SECTION).toTypedArray()[0]
    }


    fun removeQuotes(text: String): String {
        var message = text
        if (message.startsWith(STR1)) {
            message = message.replaceFirst(STR1, "")
        }
        if (message.startsWith(STR2)) {
            message = message.replaceFirst(STR2, "")
        }
        if (message.endsWith(STR1) || message.endsWith(STR2)) {
            message = message.substring(0, message.length - 1)
        }
        return message
    }

    fun getPath(currentPath: String): String {
        var path = currentPath
        if (path.startsWith(COMMENT)) {
            path = path.replaceFirst(COMMENT, DOLLAR)
        }
        if (path.startsWith(LIST_ITEM)) {
            path = path.replaceFirst(LIST_ITEM.toRegex(), DOLLAR)
        }
        if (path.contains(SECTION)) {
            path = path.replace(SECTION, DOLLAR)
        }
        return path
    }
}