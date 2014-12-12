package web


def host = 'localhost'


def ports = [8025,8026,8027,8028,8029]

println "Starting Server...ip = $host, port = $ports"

def sinks = []

for(port in ports){

    sinks.add(new SocketStreamSink(port))

    }

(0..(ports.size()-1)).each {number ->
        def sinkSocket = "Sink#$number"
        Thread.start(sinkSocket) {
            println("trying to connect to socket number ${sinks[number]}")
            sinks[number].start()
        }
}



