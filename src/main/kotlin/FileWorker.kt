import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object FileWorker {

    fun read(filepath: String): IQSamples? {
        val debugUtilities = DebugUtilities("readFile")
        debugUtilities.timeStart()

        val res = readerBinary(filepath)

        debugUtilities.timeStop()
        return res
    }

    fun write(samples: IQSamples, filepath: String): Boolean {
        val debugUtilities = DebugUtilities("writeFile")
        debugUtilities.timeStart()

        val res = writerBinary(samples, filepath)

        debugUtilities.timeStop()
        return res
    }

    private fun readerBinary(filepath: String): IQSamples? {
        try {
            val fileInput = FileInputStream(filepath).channel
            val dataSize = fileInput.size().toInt()
            val byteBuffer = ByteBuffer.allocate(dataSize)
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)
            fileInput.read(byteBuffer)
            byteBuffer.flip()

            val floatsArr = FloatArray(dataSize / Float.SIZE_BYTES)
            val floatBuffer = byteBuffer.asFloatBuffer()
            floatBuffer.get(floatsArr)

            val samplesI = FloatArray(floatsArr.size / 2)
            val samplesQ = FloatArray(floatsArr.size / 2)
            for (i in 0..samplesI.lastIndex) {
                samplesI[i] = floatsArr[i * 2]
                samplesQ[i] = floatsArr[i * 2 + 1]
            }

            return IQSamples(samplesI, samplesQ)
        }
        catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return null
        }
    }

    private fun writerBinary(samples: IQSamples, filepath: String): Boolean {
        try {
            val floatsArr = FloatArray(samples.size * 2)
            for (i in 0..samples.lastIndex) {
                floatsArr[i * 2] = samples.i[i]
                floatsArr[i * 2 + 1] = samples.q[i]
            }

            val dataSize = floatsArr.size * Float.SIZE_BYTES
            val byteBuffer = ByteBuffer.allocate(dataSize)
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN)

            val floatBuffer = byteBuffer.asFloatBuffer()
            floatBuffer.put(floatsArr)
            val out = FileOutputStream(filepath).channel
            out.write(byteBuffer)
            out.close()

            return true
        }
        catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return false
        }
    }
}