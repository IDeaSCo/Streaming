import server.StreamSink

def cli = new CliBuilder(usage:'server [-h <ip or hostname>] [-p <number>]')
cli.with {
    h  args:1, argName: 'host', longOpt:'host', 'OPTIONAL, Server IP/Host, default is localhost', optionalArg:true
    p  args:1, argName: 'port', longOpt:'port', 'OPTIONAL, Server Port, default is 8080', optionalArg:true
}

def options = cli.parse(args)

if(!options) {
    return
}

PrintWriter console = new PrintWriter(System.out,true)

if(options.arguments()){
    console.println "Cannot understand ${options.arguments()}"
    cli.usage()
    return
}

def host = 'localhost'

if(options.h) {
    host = options.h
}

def port = 9080
if(options.port) {
    port = Integer.parseInt(options.port)
}
println "Starting Server...ip = $host, port = $port"

def sink = new StreamSink(host, port)
println 'Ready to accept websocket connections...'
sink.start()

