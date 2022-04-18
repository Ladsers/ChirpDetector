import kotlin.math.roundToInt

object FcchInfo {
    fun getNumberOfPulses(signalTimeSecs: Float): Int {
        val precision = 1e6
        var counter = 0
        var allTime = (signalTimeSecs * precision).toInt()
        val duration = (FCCH_DURATION_SECS * precision).toInt()
        val pause = (FCCH_PAUSE_SECS * precision).toInt()

        while (true) {
            allTime -= duration
            if (allTime < 0F) break
            counter++
            allTime -= pause
        }

        return counter
    }

    fun getPulseMidpoints(signalTimeSecs: Float): IntArray {
        val pulses = getNumberOfPulses(signalTimeSecs)
        val duration = DataProcessor.findIndexByTime(FCCH_DURATION_SECS)
        val middle = (duration / 2F).roundToInt()
        val pause = DataProcessor.findIndexByTime(FCCH_PAUSE_SECS)

        val result = IntArray(pulses)
        var counter = 0

        for (i in 0 until pulses) {
            result[i] = counter + middle
            counter += duration + pause
        }

        return result
    }

    fun applyFreqShift(midpoints: IntArray, freqShift: Float) {
        val fcchCounts = DataProcessor.findIndexByTime(FCCH_DURATION_SECS)
        val countsPerHz = fcchCounts / FCCH_FREQUENCY_RANGE.toFloat()
        val countShift = (countsPerHz * freqShift).roundToInt()

        for (i in 0..midpoints.lastIndex) midpoints[i] -= countShift
    }
}