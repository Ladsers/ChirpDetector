class DebugUtilities(private val tag: String) {
    private var stopwatchStart = 0L

    fun timeStart() {
        stopwatchStart = System.nanoTime()
    }

    fun timeStop() {
        if (DEBUG_MODE) {
            val elapsedTimeNs = System.nanoTime() - stopwatchStart
            val elapsedTimeSec = elapsedTimeNs * 1e-9
            val elapsedTimeStr = String.format("%.3f", elapsedTimeSec)
            println("[DEBUG][$tag] The operation took $elapsedTimeStr s")
        }
    }
}