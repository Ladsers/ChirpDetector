import kotlin.math.roundToInt

object Analyzer {
    fun findPeakIndexesInPhaseCounter(phaseCounterResult: IntArray, numberOfPulses: Int): IntArray? {
        val max = phaseCounterResult.maxOrNull()
        if (max == null) {
            println("Failed to find peak indexes")
            return null
        }
        val firstLvl = (max * 0.5).roundToInt()
        val secondLvl = (max * 0.7).roundToInt()

        val firstList = mutableListOf<Int>()
        val secondList = mutableListOf<Int>()

        for (i in 0..phaseCounterResult.lastIndex) {
            if (phaseCounterResult[i] >= firstLvl) firstList.add(i)
            if (phaseCounterResult[i] >= secondLvl) secondList.add(i)
        }

        return if (firstList.size == numberOfPulses) {
            firstList.toIntArray()
        } else if (secondList.size == numberOfPulses) {
            secondList.toIntArray()
        } else {
            println("Failed to find peak indexes")
            null
        }
    }

    fun calcMidpointsByPeaks(phaseCounterResult: IntArray, peakIndexes: IntArray): IntArray {
        val list = mutableListOf<Int>()

        for (peakIndex in peakIndexes) {
            var counter = 0
            for (i in 0 until peakIndex) counter += phaseCounterResult[i]
            counter += (phaseCounterResult[peakIndex] / 2F).roundToInt()
            list.add(counter)
        }

        return list.toIntArray()
    }
}