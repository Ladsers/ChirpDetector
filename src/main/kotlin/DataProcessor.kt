import kotlin.math.abs
import kotlin.math.ceil

object DataProcessor {

    fun cutFrom(signal: IQSamples, secs: Float): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutFrom")
        debugUtilities.timeStart()

        val index = findIndexByTime(if (secs < 0.0f) 0.0f else secs)
        val res = cutter(signal, index, signal.lastIndex)

        debugUtilities.timeStop()
        return res
    }

    fun cutTo(signal: IQSamples, secs: Float): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutTo")
        debugUtilities.timeStart()

        val res = cutterByTime(signal, 0.0f, secs)

        debugUtilities.timeStop()
        return res
    }

    fun cutFragment(signal: IQSamples, secsFrom: Float, secsTo: Float): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutFragment")
        debugUtilities.timeStart()

        val res = cutterByTime(signal, secsFrom, secsTo)

        debugUtilities.timeStop()
        return res
    }

    fun cutFrom(signal: IQSamples, index: Int): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutFrom")
        debugUtilities.timeStart()

        val res = cutter(signal, if (index < 0) 0 else index, signal.lastIndex)

        debugUtilities.timeStop()
        return res
    }

    fun cutTo(signal: IQSamples, index: Int): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutTo")
        debugUtilities.timeStart()

        val res = cutter(signal, 0, index)

        debugUtilities.timeStop()
        return res
    }

    fun cutFragment(signal: IQSamples, from: Int, to: Int): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.cutFragment")
        debugUtilities.timeStart()

        val res = cutter(signal, from, to)

        debugUtilities.timeStop()
        return res
    }

    fun findIndexByTime(secs: Float): Int {
        return (secs * SAMPLE_RATE).toInt()
    }

    fun findTimeByIndex(index: Int): Float {
        return index / SAMPLE_RATE.toFloat()
    }

    fun getDuration(signal: IQSamples): Float {
        return signal.size / SAMPLE_RATE.toFloat()
    }

    fun downsample(signal: IQSamples, newSamleRate: Int): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.downsample")
        debugUtilities.timeStart()

        if (SAMPLE_RATE % newSamleRate != 0) {
            println("Downsample: Fail! Frequencies must be multiples of each other!")
            return null
        }

        val step = SAMPLE_RATE / newSamleRate
        val size = ceil(signal.size / step.toFloat()).toInt()
        val newI = FloatArray(size)
        val newQ = FloatArray(size)

        for (k in 0..newI.lastIndex) {
            newI[k] = signal.i[k * step]
            newQ[k] = signal.q[k * step]
        }

        SAMPLE_RATE = newSamleRate

        debugUtilities.timeStop()
        return IQSamples(newI, newQ)
    }

    fun normalize(signal: IQSamples, max: Float = 1f, min: Float = -max): IQSamples? {
        val debugUtilities = DebugUtilities("DataProcessor.normalize")
        debugUtilities.timeStart()

        val iMax = signal.i.maxOrNull() ?: return null
        val qMax = signal.q.maxOrNull() ?: return null
        val iMin = signal.i.minOrNull() ?: return null
        val qMin = signal.q.minOrNull() ?: return null

        val signalMax = if (iMax > qMax) iMax else qMax
        val signalMim = if (iMin < qMin) iMin else qMin

        val newI = FloatArray(signal.size)
        val newQ = FloatArray(signal.size)

        for (k in 0..signal.lastIndex) {
            newI[k] = (signal.i[k] - signalMim) / (signalMax - signalMim) * abs(min - max) + min
            newQ[k] = (signal.q[k] - signalMim) / (signalMax - signalMim) * abs(min - max) + min
        }

        debugUtilities.timeStop()
        return IQSamples(newI, newQ)
    }

    private fun cutterByTime(signal: IQSamples, secsFrom: Float, secsTo: Float): IQSamples? {
        val from = findIndexByTime(secsFrom)
        val to = findIndexByTime(secsTo)
        return cutter(signal, from, to)
    }

    private fun cutter(signal: IQSamples, from: Int, to: Int): IQSamples? {
        return try {
            val newI = signal.i.copyOfRange(from, to)
            val newQ = signal.q.copyOfRange(from, to)
            IQSamples(newI, newQ)
        } catch (e: Exception) {
            println("Cutter: An error occurred while performing the operation")
            null
        }
    }
}