object PhaseCounter {
    fun calcAboveBelow(phase: FloatArray): IntArray {
        val debugUtilities = DebugUtilities("PhaseCounter.calcAboveBelow")
        debugUtilities.timeStart()

        val list = mutableListOf<Int>()
        var counter = 1

        for (i in 1..phase.lastIndex) {
            val above = phase[i - 1] > 0 && phase[i] > 0
            val below = phase[i - 1] < 0 && phase[i] < 0
            if (above || below) counter++
            else {
                list.add(counter)
                counter = 1
            }

            if (i == phase.lastIndex)  list.add(counter)
        }

        debugUtilities.timeStop()
        return list.toIntArray()
    }
}