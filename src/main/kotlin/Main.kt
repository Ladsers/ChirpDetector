fun main(args: Array<String>) {
    println("Program arguments: ${args.joinToString()}")

    TestRoom.run() //remove line

    /*if (args.isNotEmpty()) {
        when (args[0]) {
            "-cut" -> cut(args)
            "-translate" -> translate(args)
            "-test" -> TestRoom.run()
            else -> {
                //TODO
            }
        }
    } else {
        //TODO
    }*/
}

fun cut(args: Array<String>) {
    try {
        val pathExisting = args[1]
        val pathNew = args[2]
        val cutFrom = args[3].replace(',', '.').toFloat()
        val cutTo = if (args[4] != "end") args[4].replace(',', '.').toFloat() else 0.0f

        val data = FileWorker.read(FileWorker.FileType.BINARY, pathExisting) ?: throw Exception()
        val newData = if (args[4] != "end") DataProcessor.cutFragment(data, cutFrom, cutTo) ?: throw Exception()
        else DataProcessor.cutFrom(data, cutFrom) ?: throw Exception()
        if (!FileWorker.write(FileWorker.FileType.BINARY, newData, pathNew)) throw Exception()
    } catch (e: Exception) {
        println("Cutter: An error occurred while performing the operation")
    }
}

fun translate(args: Array<String>) {
    //TODO
}