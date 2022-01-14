package net.eduard.api.lib.config

import java.lang.StringBuilder

object ConfigUtil {
    const val CHAR_TAB = '\t'
    const val EMPTY_LIST = "[]"
    const val EMPTY_SECTION = "{}"
    const val CHAR_UNDERLINE = '_'
    const val CHAR_COMMENT = '#'
    const val CHAR_SECTION = ':'
    const val SECTION_SEPARATOR = "."
    const val LINE_SEPARATOR = "\n"
    const val STR1 = "\""
    const val STR2 = "\'"
    const val CHAR_LIST_ITEM = '-'
    const val CHAR_SPACE = ' '

    fun isComment(line: String): Boolean {
        return line.trimStart().startsWith(CHAR_COMMENT)
    }

    fun isList(line: String): Boolean {
        for (char in line) {
            if (char == CHAR_LIST_ITEM) {
                return true
            }
            if (!(char == CHAR_SPACE ||
                char == CHAR_LIST_ITEM)) {
                return false
            }
        }
        return false
    }

    fun isSection(line: String): Boolean {
        return !isList(line) &&
                !isComment(line) &&
                line.contains(CHAR_SECTION)
    }

    fun getComment(line: String): String {
        return line.trimStart()
            .removeChar(CHAR_COMMENT)
    }

    fun String.removeChar(char: Char): String {
        val newString = StringBuilder()
        var removed = false
        for (character in this.toCharArray()) {
            if (character != char) {
                newString.append(character)
            } else if (removed) {
                newString.append(character)
            } else {
                removed = true
            }
        }
        return newString.toString()
    }

    /**
     * Conta quantos espaços até encontrar uma Letra que não seja espaço
     */
    fun getSpaceAmount(line: String): Int {
        var spaceAmount = 0
        for (char in line) {
            if (char != ' ') break
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
        if (amount <= 0) {
            return "";
        }
        return "  ".repeat(amount)
    }

    fun getList(line: String): String {
        val text = StringBuilder()
        var prefixRead = false
        var firstSpaceIgnored = false
        for (char in line) {
            if (!prefixRead && char == CHAR_SPACE) {
                continue
            }
            if (!prefixRead && char == CHAR_LIST_ITEM) {
                prefixRead = true
                continue
            }
            if (!firstSpaceIgnored && char == CHAR_SPACE) {
                firstSpaceIgnored = true
                continue
            }
            if (char == CHAR_TAB){
                text.append(CHAR_SPACE)
                continue
            }
            text.append(char)
        }
        return text.toString()
    }


    fun getKey(currentLine: String): String {
        val line = currentLine
        return line.trimStart().split(CHAR_SECTION).toTypedArray()[0]
    }


    fun removeQuotes(text: String): String {
        return text.removeSurrounding(STR1)
            .removeSurrounding(STR2)
    }

    fun getPath(currentPath: String): String {
        var path = currentPath
        if (path.startsWith(CHAR_COMMENT, false)) {
            path = path.replace(CHAR_COMMENT, CHAR_UNDERLINE, false)
        }
        if (path.startsWith(CHAR_LIST_ITEM, false)) {
            path = path.replace(CHAR_LIST_ITEM, CHAR_UNDERLINE, false)
        }
        if (path.contains(CHAR_SECTION, false)) {
            path = path.replace(CHAR_SECTION, CHAR_UNDERLINE, false)
        }
        return path
    }

    fun hasOnlySpaces(tempData: String): Boolean {
        for (char in tempData){
            if (char != CHAR_SPACE){
                return false;
            }
        }
        return true;
    }
}