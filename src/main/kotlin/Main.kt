fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")

    val data = FileWorker.read("""D:\ThesisData\1531156000""")
    data?.let {
        FileWorker.write(it, """D:\ThesisData\1531156000r1""")
    }
}