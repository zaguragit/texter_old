package posidon.texter

import posidon.texter.backend.Settings

fun main() {
    Settings.init()
    Thread(Main()).start()
}
class Main : Runnable { override fun run() { Window.init() }}

object AppInfo {
    const val NAME = "texter"
    const val INIT_WIDTH = 820
    const val INIT_HEIGHT = 620
    const val MIN_WIDTH = 620
    const val MIN_HEIGHT = 480
}