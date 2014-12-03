package web


def host = 'localhost'


def ports = [8025, 8026, 8027]

println "Starting Server...ip = $host, port = $ports"

def sink1 = new SocketStreamSink(ports[0])
def sink2 = new SocketStreamSink(ports[1])
def sink3 = new SocketStreamSink(ports[2])
println "Ready to accept data over $ports."
sink1.start()
sink2.start()


