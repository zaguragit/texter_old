package posidon.texter.backend

import posidon.texter.Window
import java.io.EOFException
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception
import java.net.BindException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket
import kotlin.system.exitProcess

object InstanceManager {

    private var running = true

    fun start(args: Array<String>) {
        Thread(Runnable {
            try {
                val socket = ServerSocket(23948, 0, InetAddress.getByName("localhost"))
                while (running) {
                    val input = ObjectInputStream(socket.accept().getInputStream())
                    val arguments = input.readObject()
                    if (arguments is Array<*>) {
                        Window.bringToFront()
                        for (string in arguments) if (string is String) {
                            val file = File(string)
                            if (file.isDirectory) Window.folder = string
                            else Window.openFile(string)
                        }
                    }
                    input.close()
                }
                socket.close()
            } catch (e: BindException) {
                val socket = Socket(InetAddress.getByName("localhost"), 23948)
                ObjectOutputStream(socket.getOutputStream()).writeObject(args)
                exitProcess(0)
            }
        }).start()
    }

    fun kill() { running = false }
}