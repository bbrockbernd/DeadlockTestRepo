package altered

class AllTestExecutor {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            TestRunner().collectTestResults()
        }
    }
}