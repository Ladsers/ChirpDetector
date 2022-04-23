import kotlin.math.abs
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

    fun getHistogram(modelMidpoints: IntArray, signalMidpoints: IntArray, precision: Int = 10): Histogram {
        if (modelMidpoints.size != signalMidpoints.size) throw Exception("The size of midpoints arrays must be the same")

        val difference = IntArray(modelMidpoints.size)
        for (i in 0..modelMidpoints.lastIndex) {
            difference[i] = signalMidpoints[i] - modelMidpoints[i]
        }

        val differenceMs =
            difference.map { DataProcessor.findTimeByIndex(it * 1000 * precision).roundToInt() }.toIntArray()
        val differenceMsAbs = differenceMs.map { abs(it) }.toIntArray()
        val max = differenceMsAbs.maxOrNull()!!

        val timeList = mutableListOf<Float>()
        val probabilitiesList = mutableListOf<Float>()

        for (ms in -max..max) {
            var counter = 0
            for (diff in differenceMs) {
                if (diff == ms) counter++
            }
            timeList.add(ms / precision.toFloat())
            probabilitiesList.add(counter / modelMidpoints.size.toFloat())
        }

        return Histogram(timeList.toFloatArray(), probabilitiesList.toFloatArray())
    }
}