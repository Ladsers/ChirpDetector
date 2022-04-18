import kotlin.math.atan2

class IQSamples(val i: FloatArray, val q: FloatArray) {

    init {
        if (i.size != q.size) throw Exception("The size of IQ signal component arrays must be the same")
    }

    fun getIQ(index: Int) = Pair(i[index], q[index])
    fun getArrays() = Pair(i, q)

    val size = i.size
    val lastIndex = i.lastIndex

    fun getPhase(): FloatArray {
        val debugUtilities = DebugUtilities("IQSamples.getPhase")
        debugUtilities.timeStart()

        val phase = FloatArray(size)
        for (k in 0..lastIndex) phase[k] = atan2(i[k], q[k])

        debugUtilities.timeStop()
        return phase
    }

    fun removeI() {
        for (k in 0..lastIndex) i[k] = 0f
    }

    fun removeQ() {
        for (k in 0..lastIndex) q[k] = 0f
    }

    fun clone() = IQSamples(i.clone(), q.clone())
}