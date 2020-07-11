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
        var result: String? = null
        var result1: String? = null
        var result2: String? = null
        var result3: String? = null
        var result4: String? = null
        var result5: String? = null
        var result6: String? = null
        var result7: String? = null
        var result8: String? = null
        while(string?.let { Regex(MARKDOWN_GROUPS).containsMatchIn(it) }!!) {
             result1 =  Regex(UNORDERED_LIST_ITEM_GROUP).replace(string, "")
             result2 = Regex(HEADER_GROUP).replace(result1, "")
             result3 = Regex(QUOTE_GROUP).replace(result2, "")
             result4 = Regex(ITALIC_GROUP).replace(result3, "")
             result5 = Regex(BOLD_GROUP).replace(result4, "")
             result6 = Regex(STRIKE_GROUP).replace(result5, "")
             result7 = Regex(RULE_GROUP).replace(result6, "")
             result8 = Regex(INLINE_GROUP).replace(result7, "")
            result = Regex(LINK_GROUP).replace(result8, "")
        }
        return result
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