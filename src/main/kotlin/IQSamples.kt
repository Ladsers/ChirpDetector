class IQSamples(val i: FloatArray, val q: FloatArray) {
    fun getIQ(index: Int) = Pair(i[index], q[index])
    fun getArrays() = Pair(i, q)

    val size = i.size
    val lastIndex = i.lastIndex
}