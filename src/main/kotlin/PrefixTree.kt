class PrefixTree() {
    private var root = Node()

    data class Node(var isWord: Boolean = false, val letter: Char? = null) {
        val neighbors = mutableMapOf<Char, Node>()
    }

    fun insert(word: String) {
        var neighbors = root.neighbors

        word.forEachIndexed { i, char ->
            val isWord = i == word.length - 1

            if (neighbors.contains(char)) {
                val node = neighbors[char]!!
                if (isWord) node.isWord = isWord
                neighbors = node.neighbors
            } else {
                val node = Node(isWord, char)
                neighbors[char] = node
                neighbors = node.neighbors
            }
        }
    }

    fun addDictionary(dictionary: List<String>): PrefixTree {
        dictionary.forEach { word ->
            this.insert(word)
        }
        return this
    }

    fun addInvDictionary(invDictionary: List<String>): PrefixTree {
        invDictionary.forEach { word ->
            word.forEachIndexed { index, _ ->
                this.insert(word.substring(0, word.length - index).reversed())
            }
        }
        return this
    }

    fun isWordHaveEnding(word: String): Boolean {
        var neighbors = root.neighbors
        if (neighbors.isEmpty()) {
            return false
        }
        word.forEach { char ->
            if (!neighbors.contains(char)) {
                return false
            } else {
                val node = neighbors[char]
                neighbors = node!!.neighbors
            }
        }
        return true
    }

    fun contains(word: String): Boolean = nodeSearch(word) != null

    private fun nodeSearch(word: String): Node? {
        var neighbors = root.neighbors
        if (neighbors.isEmpty() || word.isBlank()) {
            return null
        }

        word.forEachIndexed { i, char ->
            val isLeaf = i == word.length - 1
            // add new node
            if (!neighbors.contains(char)) {
                return null
            } else {
                // exist, so traverse current path, ending if is last value, and is leaf node
                val node = neighbors[char]!!
                if (isLeaf) {
                    return if (node.isWord) {
                        node
                    } else {
                        null
                    }
                }
                // not at end, continue traversing
                neighbors = node.neighbors
            }
        }
        throw IllegalStateException("Should not get here")
    }
}