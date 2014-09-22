import client.DataFetcher
import client.StreamSource

import java.nio.channels.NotYetConnectedException

def cli = new CliBuilder(usage:'client -u <url>')
cli.with {
    u  args:1, argName: 'url', longOpt:'url', 'REQUIRED, Server Url', optionalArg:false
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

//todo: make cmd args for dbConfig later...
def dbConfig = [url:'jdbc:sqlserver://localhost:1433', user:'sa', password:'', driver:'com.microsoft.sqlserver.jdbc.SQLServerDriver']
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
        println "Sending message...$row"
        source.send(row.toString())
    }
    println('Message has been delivered to Sink..')
} catch (NotYetConnectedException nye){
    println(nye.message)
} finally {
    println ('Closing Connection with Server...')
    source.close()
}
