package posidon.texter

fun main(args: Array<String>) {
    Main()
}

object appInfo {
    const val NAME = "texter"
    const val INIT_WIDTH = 820
    const val INIT_HEIGHT = 620
    const val MIN_WIDTH = 620
    const val MIN_HEIGHT = 480
}

class Main {
    companion object {
        lateinit var window: Window
    }
    init {
        window = Window()
    }
}