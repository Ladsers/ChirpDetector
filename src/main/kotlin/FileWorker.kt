import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object FileWorker {

    fun read(filepath: String): MutableList<IQsample>? {
        val debugUtilities = DebugUtilities("readFile")
        debugUtilities.timeStart()

        val res = readerBinary(filepath)

        debugUtilities.timeStop()
        return res
    }

    fun write(samples: List<IQsample>, filepath: String): Boolean {
        val debugUtilities = DebugUtilities("writeFile")
        debugUtilities.timeStart()

        val res = writerBinary(samples, filepath)

        debugUtilities.timeStop()
        return res
    }

    private fun readerBinary(filepath: String): MutableList<IQsample>? {
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

            val samples = mutableListOf<IQsample>()
            for (i in 0..floatsArr.lastIndex step 2) {
                samples.add(IQsample(floatsArr[i], floatsArr[i + 1]))
            }

            return samples
        }
        catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return null
        }
    }

    private fun writerBinary(samples: List<IQsample>, filepath: String): Boolean {
        try {
            val floatsArr = FloatArray(samples.size * 2)
            var i = 0
            for (s in samples) {
                floatsArr[i++] = s.i
                floatsArr[i++] = s.q
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