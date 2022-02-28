fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")

    val data = FileWorker.read("""D:\ThesisData\1531156000""") ?: return
    /*data?.let {
        FileWorker.write(it, """D:\ThesisData\1531156000r1""")
    }*/
    SAMPLE_RATE = 1500000
    println(DataProcessor.getDuration(data))
}