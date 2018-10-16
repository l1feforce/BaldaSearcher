object FieldCreator {
    fun createField(size: Int): Graph {
        val field = Graph()

        for (x in 0 until size) {
            for (y in 0 until size) {
                field.addVertex("cell$y$x", " ")
            }
        }

        for (x in 0 until size) {
            for (y in 0 until size) {
                //upper cell
                if (y - 1 >= 0) field.connect("cell$y$x", "cell${y - 1}$x")
                //right cell
                if (x + 1 <= 4) field.connect("cell$y$x", "cell$y${x + 1}")
                //bottom cell
                if (y + 1 <= 4) field.connect("cell$y$x", "cell${y + 1}$x")
                //left cell
                if (x - 1 >= 0) field.connect("cell$y$x", "cell$y${x - 1}")
            }
        }
        return field
    }
}