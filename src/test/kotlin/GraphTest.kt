import org.junit.Assert.assertEquals
import org.junit.Test

import org.junit.Before

class GraphTest {

    lateinit var field: Graph
    lateinit var listOfWords: List<String>
    lateinit var dictionary: PrefixTree
    lateinit var invDictionary: PrefixTree

    @Before
    fun init() {
        field = FieldCreator.createField(5)
        //read dictionary from files
        val input = javaClass.classLoader.getResourceAsStream("singular.txt")
        listOfWords = input.bufferedReader().use { it.readLines() }
        //create PrefixTree with read dictionary
        dictionary = PrefixTree().addDictionary(listOfWords)
        invDictionary = PrefixTree().addInvDictionary(listOfWords)
    }

    @Test
    fun wordsSearch() {
        field.setMainWord("балда")
        val setOfFoundWords = field.findAllWords(dictionary, invDictionary)
        assertEquals(mapOf("балда" to Pair("cell33", "а"), "калда" to Pair("cell31", "к"),
                "фалда" to Pair("cell31", "ф")) ,
                setOfFoundWords.filter { it.key.length == 5 })
    }
}