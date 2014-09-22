package server

import server.StreamSink

def cli = new CliBuilder(usage:'server [-h <ip or hostname>] [-p <number>]')
cli.with {
    s  args:1, argName: 'serverUrl', longOpt:'serverUrl', 'OPTIONAL, Server IP/URL, default is localhost', optionalArg:true
    p  args:1, argName: 'port', longOpt:'port', 'OPTIONAL, Server Port, default is 9080', optionalArg:true
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

if(options.s) {
    host = options.s
}

def port = 9080
if(options.port) {
    port = Integer.parseInt(options.port)
}
println "Starting Server...ip = $host, port = $port"

def sink = new StreamSink(host, port)
println 'Ready to accept websocket connections.'
sink.start()

