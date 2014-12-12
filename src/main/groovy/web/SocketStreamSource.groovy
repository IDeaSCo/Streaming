package web

class SocketStreamSource {

    def port
    def destination
    Socket socket
    OutputStream out
    public SocketStreamSource(def port, def destination){
        this.port = port
        this.destination = destination
        try {
            println "trying to connect to port number $port"
            socket = new Socket(destination, port)
            println("connected to port number $port")
        }catch (Exception e){
           e.printStackTrace()
        }
         out = socket.getOutputStream()
    }

 public void send(def payload){

     //println "Payload=$payload"


         byte[] buffer = new byte[520]
         buffer = payload.getBytes("UTF-8")
         out.write(buffer, 0, payload.length())
         out.flush()


 }
    public void close()
    {
        this.out.close()
        this.socket.close()
    }

}
