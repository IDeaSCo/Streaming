package Spike

def ports = [8025,8026,8027,8028,8029]
for (port in ports){
    println("port number outside thread = $port")
    Thread.start {
        println("port number inside thread = $port")
    }
}