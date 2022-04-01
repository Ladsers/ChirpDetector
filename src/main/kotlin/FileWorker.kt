import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

object FileWorker {

    enum class FileType {
        BINARY,
        TEXT_TABLE /* i-sample'\t'q-sample'\n' */,
        TEXT_TABLE_I_SAMPLES /* i-sample'\n' */,
        TEXT_TABLE_Q_SAMPLES /* q-sample'\n' */
    }

    fun read(type: FileType, filepath: String): IQSamples? {
        val debugUtilities = DebugUtilities("FileWorker.readFile")
        debugUtilities.timeStart()

        val res = when (type) {
            FileType.BINARY -> readerBinary(filepath)
            FileType.TEXT_TABLE -> readerTextTable(filepath)
            FileType.TEXT_TABLE_I_SAMPLES, FileType.TEXT_TABLE_Q_SAMPLES -> readerTextTable(filepath) // for writing only
        }

        debugUtilities.timeStop()
        return res
    }

    fun write(type: FileType, samples: IQSamples, filepath: String): Boolean {
        val debugUtilities = DebugUtilities("FileWorker.writeFile")
        debugUtilities.timeStart()

        val res = when (type) {
            FileType.BINARY -> writerBinary(samples, filepath)
            FileType.TEXT_TABLE, FileType.TEXT_TABLE_I_SAMPLES, FileType.TEXT_TABLE_Q_SAMPLES ->
                writerTextTable(type, samples, filepath)
        }

        debugUtilities.timeStop()
        return res
    }

    fun writeResults(data: FloatArray, filepath: String, separator: String = "\n"): Boolean {
        val debugUtilities = DebugUtilities("FileWorker.writeResults")
        debugUtilities.timeStart()

        val dataList =
            data.map { if (COMMA_INSTEAD_DOT) it.toString().replace('.', ',') else it.toString() }
        val res = writerResults(dataList, filepath, separator)

        debugUtilities.timeStop()
        return res
    }

    fun writeResults(data: IntArray, filepath: String, separator: String = "\n"): Boolean {
        val debugUtilities = DebugUtilities("FileWorker.writeResults")
        debugUtilities.timeStart()

        val dataList = data.map { it.toString() }
        val res = writerResults(dataList, filepath, separator)

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
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return false
        }
    }

    private fun readerTextTable(filepath: String): IQSamples? {
        try {
            val lines = File(filepath).bufferedReader().readLines()
            val samplesI = FloatArray(lines.size)
            val samplesQ = FloatArray(lines.size)

            for ((k, line) in lines.withIndex()) {
                val values = line.split("\t")
                samplesI[k] = values[0].replace(',', '.').toFloat()
                samplesQ[k] = values[1].replace(',', '.').toFloat()
            }

            return IQSamples(samplesI, samplesQ)
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return null
        }
    }

    private fun writerTextTable(type: FileType, samples: IQSamples, filepath: String): Boolean {
        try {
            val outStr: String
            when (type) {
                FileType.TEXT_TABLE -> {
                    val strsI =
                        samples.i.map { if (COMMA_INSTEAD_DOT) it.toString().replace('.', ',') else it.toString() }
                    val strsQ =
                        samples.q.map { if (COMMA_INSTEAD_DOT) it.toString().replace('.', ',') else it.toString() }
                    outStr = buildString {
                        for (k in 0..strsI.lastIndex) append(strsI[k] + '\t' + strsQ[k] + '\n')
                    }
                }
                FileType.TEXT_TABLE_I_SAMPLES -> {
                    val strsI =
                        samples.i.map { if (COMMA_INSTEAD_DOT) it.toString().replace('.', ',') else it.toString() }
                    outStr = buildString {
                        for (k in 0..strsI.lastIndex) append(strsI[k] + '\n')
                    }
                }
                FileType.TEXT_TABLE_Q_SAMPLES -> {
                    val strsQ =
                        samples.q.map { if (COMMA_INSTEAD_DOT) it.toString().replace('.', ',') else it.toString() }
                    outStr = buildString {
                        for (k in 0..strsQ.lastIndex) append(strsQ[k] + '\n')
                    }
                }
                else -> return false
            }
            File(filepath).bufferedWriter().use { out -> out.write(outStr.dropLast(1)) }

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            return false
        }
    }

    private fun writerResults(dataList: List<String>, filepath: String, separator: String): Boolean {
        return try {
            val outStr = buildString {
                for (k in 0..dataList.lastIndex) append(dataList[k] + separator)
            }
            File(filepath).bufferedWriter().use { out -> out.write(outStr.dropLast(1)) }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO: сделать логи
            false
        }
    }
}