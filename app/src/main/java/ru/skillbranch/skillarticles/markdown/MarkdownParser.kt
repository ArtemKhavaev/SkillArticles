package ru.skillbranch.skillarticles.markdown

import java.util.regex.Pattern

object MarkdownParser {
    private val LINE_SEPARATOR = System.getProperty("line.separator") ?: "\n"

    // group regex
    private const val UNORDERED_LIST_ITEM_GROUP = "(^[*+-] .+$)"
    private const val HEADER_GROUP = "(^#{1,6} .+?$)"
    private const val QUOTE_GROUP = "(^> .+?$)"
    private const val ITALIC_GROUP = "((?<!\\*)\\*[^*].*?[^*]?\\*(?!\\*)|(?<!_)_[^_].*?[^_]?_(?!_))"
    private const val BOLD_GROUP = "((?<!\\*)\\*{2}[^*].*?[^*]?\\*{2}(?!\\*)|(?<!_)_{2}[^_].*?[^_]?_{2}(?!_))"
    private const val STRIKE_GROUP = "((?<!~)~{2}[^~].*?[^~]?~{2}(?!~))"
    private const val RULE_GROUP = "(^[-_*]{3}$)"
    private const val INLINE_GROUP = "((?<!`)`[^`\\s].*?[^`\\s]?`(?!`))"
    private const val LINK_GROUP = "(\\[[^\\[\\]]*?]\\(.+?\\)|^\\[*?]\\(.*?\\))"

    // result regex
    private const val MARKDOWN_GROUPS = "$UNORDERED_LIST_ITEM_GROUP|$HEADER_GROUP|$QUOTE_GROUP" +
            "|$ITALIC_GROUP|$BOLD_GROUP|$STRIKE_GROUP|$RULE_GROUP|$INLINE_GROUP|$LINK_GROUP"

    private val elementsPattern by lazy { Pattern.compile(MARKDOWN_GROUPS, Pattern.MULTILINE) }

    /**
     * parse markdown text to elements
     */
    fun parse(string: String):MarkdownText{
        val elements = mutableListOf<Element>()
        elements.addAll(findElements(string))
        return MarkdownText(elements)
    }

    /**
     * clear markdown text to string without markdown characters
     */
    fun clear(string: String?): String?{
        var str = string
        while(str?.let { Regex("""#""").containsMatchIn(it) }!!){
            str =  Regex("""#""").replace(str, "")
        }
        var str1 = str
        while(str1?.let { Regex("""_""").containsMatchIn(it) }!!){
            str1 =  Regex("""_""").replace(str1, "")
        }
        var str2 = str1
        while(str2?.let { Regex("""\*""").containsMatchIn(it) }!!){
            str2 =  Regex("""\*""").replace(str2, "")
        }
        var str3 = str2
        while(str3?.let { Regex("""`""").containsMatchIn(it) }!!){
            str3 =  Regex("""`""").replace(str3, "")
        }
        var str4 = str3
        while(str4?.let { Regex("""~""").containsMatchIn(it) }!!){
            str4 =  Regex("""~""").replace(str4, "")
        }
        var str5 = str4
        while(str5?.let { Regex("""> """).containsMatchIn(it) }!!){
            str5 =  Regex("""> """).replace(str5, "")
        }
        var str6 = str5
        while(str6?.let { Regex("""\+""").containsMatchIn(it) }!!){
            str6 =  Regex("""\+""").replace(str6, "")
        }
        var str7 = str6
        while(str7?.let { Regex("""-""").containsMatchIn(it) }!!){
            str7 =  Regex("""-""").replace(str7, "")
        }
        var str8 = str7
        while(str8?.let { Regex("""\[""").containsMatchIn(it) }!!){
            str8 =  Regex("""\[""").replace(str8, "")
        }
        var str9 = str8
        while(str9?.let { Regex("""]""").containsMatchIn(it) }!!){
            str9 =  Regex("""]""").replace(str9, "")
        }
        var str10 = str9
        while(str10?.let { Regex("""\)""").containsMatchIn(it) }!!){
            str10 =  Regex("""\)""").replace(str10, "")
        }
        var str11 = str10
        while(str11?.let { Regex("""\(""").containsMatchIn(it) }!!){
            str11 =  Regex("""\(""").replace(str11, "")
        }

        return str11
    }

    /**
     * find markdown elements in markdown text
     */
    private fun  findElements(string: CharSequence): List<Element>{
        val parents = mutableListOf<Element>()
        val matcher = elementsPattern.matcher(string)
        var lastStartIndex = 0

        loop@while (matcher.find(lastStartIndex)){
            val startIndex = matcher.start()
            val endIdex = matcher.end()

            // if something is found then everything before - TEXT
            if(lastStartIndex < startIndex){
                parents.add(Element.Text(string.subSequence(lastStartIndex, startIndex)))
            }

            // found text
            val text : CharSequence

            // groups range for iterate by groups
            val groups = 1..9
            var group = -1
            for (gr in groups){
                if(matcher.group(gr) != null){
                    group = gr
                    break
                }
            }

            when(group){
                // NOT FOUND -> BREAK
                -1 -> break@loop
                // UNORDERED LIST
                1 -> {
                    // text without "*. "
                    text = string.subSequence(startIndex.plus(2), endIdex)
                    // find inner elements
                    val subs = findElements(text)
                    val element = Element.UnorderedListItem(text, subs)
                    parents.add(element)

                    //next find start from position "endIndex" (last regex character)
                    lastStartIndex = endIdex
                }

                // HEADERS
                2 -> {
                    val reg = "^#{1,6}".toRegex().find(string.subSequence(startIndex, endIdex))
                    val level = reg!!.value.length
                    //text without "(#) "
                    text = string.subSequence(startIndex.plus(level.inc()), endIdex)

                    val element = Element.Header(level, text)
                    parents.add(element)
                    lastStartIndex = endIdex
                }

                //QUOTE
                3 -> {
                    //text without "> "
                    text = string.subSequence(startIndex.plus(2), endIdex)
                    val subelements = findElements(text)
                    val element = Element.Quote(text, subelements)
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                //ITALIC
                4 -> {
                    //text without "*[]* "
                    text = string.subSequence(startIndex.inc(), endIdex.dec())
                    val subelements = findElements(text)
                    val element = Element.Italic(text, subelements)
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                //BOLD
                5 -> {
                    //text without "**[]**"
                    text = string.subSequence(startIndex.plus(2), endIdex.plus(-2))
                    val subelements = findElements(text)
                    val element = Element.Bold(text, subelements)
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                //STRIKE
                6 -> {
                    //text without "~~[]~~"
                    text = string.subSequence(startIndex.plus(2), endIdex.plus(-2))
                    val subelements = findElements(text)
                    val element = Element.Strike(text, subelements)
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                // RULE
                7 -> {
                    //text without "***" insert empty character
                    val element = Element.Rule()
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                // INLINE CODE
                8 -> {
                    //text without "`{ }`" insert empty character
                    text = string.subSequence(startIndex.inc(), endIdex.dec())
                    val element = Element.InlineCode(text)
                    parents.add(element)
                    lastStartIndex = endIdex
                }
                // LINK
                9 -> {
                    //full text for regex
                    text = string.subSequence(startIndex, endIdex)
                    val (title:String, link:String) = "\\[(.*)]\\((.*)\\)".toRegex().find(text)!!.destructured
                    val element = Element.Link(link, title)
                    parents.add(element)
                    lastStartIndex = endIdex
                }


            }

        }

        if(lastStartIndex < string.length) {
            val text = string.subSequence(lastStartIndex, string.length)
            parents.add(Element.Text(text))
        }

        return parents
    }
}


data class MarkdownText(val elements: List<Element>)

sealed class Element() {
    abstract val text: CharSequence
    abstract val elements: List<Element>

    data class Text(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class UnorderedListItem(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Header(
        val level: Int = 1,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Quote(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Italic(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Bold(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Strike(
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Rule(
        override val text: CharSequence = " ", //for insert span
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class InlineCode(
        override val text: CharSequence, //for insert span
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class Link(
        val link: String,
        override val text: CharSequence, //for insert span
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class OrderedListItem(
        val order: String,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element()

    data class BlockCode(
        val type: Type = Type.MIDDLE,
        override val text: CharSequence,
        override val elements: List<Element> = emptyList()
    ) : Element() {
        enum class Type { START, END, MIDDLE, SINGLE }
    }
}