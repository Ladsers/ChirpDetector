import uk.me.berndporr.iirj.Butterworth

object Filter {
    fun bandPass(signal: IQSamples, additive: Float = 0f, center: Float = 0f): IQSamples? {
        val debugUtilities = DebugUtilities("Filter.bandPass")
        debugUtilities.timeStart()

        var result: IQSamples? = null
        try {

            val widthFreq = CHANNEL_WIDTH.toDouble() + additive.toDouble()
            val butterworth = Butterworth()
            butterworth.bandPass(4, SAMPLE_RATE.toDouble(), center.toDouble(), widthFreq)

            val outI = FloatArray(signal.i.size)
            val outQ = FloatArray(signal.q.size)

            for (k in 0..signal.lastIndex) {
                outI[k] = butterworth.filter(signal.i[k].toDouble()).toFloat()
                outQ[k] = butterworth.filter(signal.q[k].toDouble()).toFloat()
            }

            result = IQSamples(outI, outQ)
        } catch (e: Exception) {
        }

        debugUtilities.timeStop()
        return result
    }

}