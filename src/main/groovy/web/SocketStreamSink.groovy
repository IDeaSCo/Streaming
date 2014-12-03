package web

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
class SocketStreamSink {

    def sock

    public SocketStreamSink(def sock){
        this.sock = sock
    }

    public void start(){
        ServerSocket serverSocket = new ServerSocket(sock)
        def count
        while (true) {
            Socket socket = null
            try{
                socket = serverSocket.accept()
                socket.setReuseAddress(true)
            }catch(java.net.SocketException e){
                System.out.println("Socket is closed!");
                return
            }
            byte[] buffer = new byte[520];
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream())

            while ((count = bufferedInputStream.read(buffer, 0, 520)) > 0) {
                def line = new String(buffer, "UTF-8");

                if(line.contains("Total")){
                    println "length = ${line.length()}, count=$count"
                    println line
                }

            }
            bufferedInputStream.close()


        }
    }
    public void close(){

    }
}
