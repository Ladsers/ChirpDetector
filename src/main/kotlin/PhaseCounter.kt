import kotlin.math.abs

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

            if (i == phase.lastIndex) list.add(counter)
        }

        debugUtilities.timeStop()
        return list.toIntArray()
    }

    fun calcEqual(phase: FloatArray, error: Float = 0F): IntArray {
        val debugUtilities = DebugUtilities("PhaseCounter.calcEqual")
        debugUtilities.timeStart()

        val list = mutableListOf<Int>()
        var counter = 1

        for (i in 1..phase.lastIndex) {
            if (abs(phase[i - 1] - phase[i]) <= error) counter++
            else {
                list.add(counter)
                counter = 1
            }

            if (i == phase.lastIndex) list.add(counter)
        }

        debugUtilities.timeStop()
        return list.toIntArray()
    }

    fun cutOffTop(data: IntArray, threshold: Int): IntArray {
        val debugUtilities = DebugUtilities("PhaseCounter.cutOffTop")
        debugUtilities.timeStart()

        val res = data.clone()
        for (i in 0..res.lastIndex) if (res[i] > threshold) res[i] = 0

        debugUtilities.timeStop()
        return res
    }
}