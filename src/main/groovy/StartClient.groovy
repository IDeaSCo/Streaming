import client.DataFetcher
import client.StreamSource

import java.nio.channels.NotYetConnectedException

def cli = new CliBuilder(usage:'client -u <url>')
cli.with {
    u  args:1, argName: 'url', longOpt:'url', 'REQUIRED, Server Url', optionalArg:false
    a  args:2, argName: 'dburl', longOpt:'dburl', 'REQUIRED , DB Url', optionalArg:false
    b  args:3, argName: 'dbuser', longOpt:'dbuser', 'REQUIRED , DB User', optionalArg:false
    c  args:4, argName: 'dbpswd', longOpt:'dbpswd', 'REQUIRED , DB Password', optionalArg:false
    d  args:5, argName: 'dbdriver', longOpt:'dbdriver', 'REQUIRED , DB Driver', optionalArg:false
    t  args:1, argName: 'test', longOpt:'test', 'OPTIONAL, Activate Test Mode (ignores Database Connection)', optionalArg:true
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

def url = options.u
println "$url"

def uri = new URL(url).toURI()

def dbConfig = [url:options.a, user:options.b, password:options.c, driver:options.d]
//if you pass dbConfig in DataFetcher, it will start talking to database
//and if you don't then it will not connect to DB and instead just send
//10 messages...purely for debug purposes (to avoid connection to DB)
def dataFetcher = options.t? new DataFetcher() : new DataFetcher(dbConfig)
def source = new StreamSource(uri)
println ('Connecting to Server...')
source.connect()
println('Connected')
try {
    //Giving the thread in connect() some time to start...problem solved for now!
    Thread.sleep(1000)
    println ('Connected')
    dataFetcher.fetchEachRow { row ->
        println "Sending message...${row.toRowResult().values().toString()}"
        source.send(row.toRowResult().values().toString())
    }
    println('Message has been delivered to Sink..')
} catch (NotYetConnectedException nye){
    println(nye.message)
} finally {
    println ('Closing Connection with Server...')
    source.close()
}
