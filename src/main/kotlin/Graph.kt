class Graph {
    private val listOfLetters = listOf("а", "б", "в", "г", "д", "е", "ж", "з", "и", "й", "к", "л", "м", "н", "о", "п",
            "р", "с", "т", "у", "ф", "х", "ц", "ч", "ш", "щ", "ъ", "ы", "ь", "э", "ю", "я")

    data class Vertex(val name: String, var letter: String) {
        val neighbors = mutableSetOf<Vertex>()
    }

    data class FoundWord(val word: String) {
        var visitedVertex = mutableSetOf<Vertex>()
    }

    private val vertices = mutableMapOf<String, Vertex>()

    operator fun get(name: String) = vertices[name] ?: throw IllegalArgumentException()

    fun addVertex(name: String, letter: String) {
        vertices[name] = Vertex(name, letter)
    }

    private fun connect(first: Vertex, second: Vertex) {
        first.neighbors.add(second)
        second.neighbors.add(first)
    }

    fun connect(first: String, second: String) = connect(this[first], this[second])

    fun neighbors(name: String) = vertices[name]?.neighbors?.map { it.name to it.letter }
            ?: listOf()

    fun setMainWord(word: String) {
        word.forEachIndexed { index, c ->
            this["cell2$index"].letter = c.toString()
        }
    }

    fun findAllWords(dictionary: PrefixTree, invDictionary: PrefixTree): Map<String, Pair<String, String>> {
        // <Word,<Cell, Letter>>
        val setOfFoundWords = mutableMapOf<String, Pair<String, String>>()

        vertices.forEach { vertexName, vertex ->
            if (vertex.letter.isBlank()) {
                listOfLetters.forEach { newLetter ->
                    vertex.letter = newLetter
                    wordsSearch(vertexName, dictionary, invDictionary).forEach { setOfFoundWords[it] = vertexName to newLetter }
                    vertex.letter = " "
                }
            }
        }
        return setOfFoundWords
    }

    private fun wordsSearch(start: String, dictionary: PrefixTree, invDictionary: PrefixTree): Set<String> {
        val setOfWords = mutableSetOf<String>()
        val foundInvWords = findStartOfWords(this[start], mutableSetOf(), StringBuilder(this[start].letter), mutableSetOf(), mutableSetOf(), invDictionary)
        foundInvWords.add(FoundWord(this[start].letter))
        foundInvWords.forEach {
            if (dictionary.contains(it.word.reversed())) setOfWords.add(it.word.reversed())
            val foundWords = findEndOfWords(this[start], it.visitedVertex, StringBuilder(""), mutableSetOf(), it, dictionary)
            foundWords.forEach { foundWord -> setOfWords.add(foundWord) }
        }
        return setOfWords
    }

    private fun findStartOfWords(start: Vertex, visited: MutableSet<Vertex>, word: StringBuilder,
                                 setOfWords: MutableSet<FoundWord>, visitedForWord: MutableSet<Vertex>, invDictionary: PrefixTree): MutableSet<FoundWord> {
        val min = start.neighbors.filter { it !in visited }
        visited.add(start)
        visitedForWord.add(start)
        min.forEach {
            word.append(it.letter)
            if (invDictionary.contains(word.toString())) {
                val foundWord = FoundWord(word.toString())
                visitedForWord.add(it)
                foundWord.visitedVertex = visitedForWord
                setOfWords.add(foundWord)
            }
            if (!invDictionary.isWordHaveEnding(word.toString())) {
                if (word.isNotEmpty()) word.setLength(word.length - 1)
                return@forEach
            }
            findStartOfWords(it, visited, word, setOfWords, visitedForWord, invDictionary)
        }
        visited.remove(start)
        if (word.isNotEmpty()) word.setLength(word.length - 1)
        return setOfWords
    }

    private fun findEndOfWords(start: Vertex, visited: MutableSet<Vertex>, word: StringBuilder,
                               setOfWords: MutableSet<String>, usedWord: FoundWord, dictionary: PrefixTree): Set<String> {
        val min = start.neighbors.filter { it !in visited }
        visited.add(start)
        min.forEach {
            word.append(it.letter)
            val wordToCheck = usedWord.word.reversed() + word.toString()
            if (dictionary.contains(wordToCheck)) {
                setOfWords.add(wordToCheck)
            }
            if (!dictionary.isWordHaveEnding(wordToCheck)) {
                if (word.isNotEmpty()) word.setLength(word.length - 1)
                return@forEach
            }
            findEndOfWords(it, visited, word, setOfWords, usedWord, dictionary)
        }
        visited.remove(start)
        if (word.isNotEmpty()) word.setLength(word.length - 1)
        return setOfWords
    }
}