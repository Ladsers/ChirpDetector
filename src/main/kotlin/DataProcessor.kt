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

    fun getDuration(signal: IQSamples): Float {
        return signal.size / SAMPLE_RATE.toFloat()
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