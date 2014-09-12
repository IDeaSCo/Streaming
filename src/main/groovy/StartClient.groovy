import client.DataFetcher
import client.StreamSource

def cli = new CliBuilder(usage:'client -u <url>')
cli.with {
    u  args:1, argName: 'url', longOpt:'url', 'REQUIRED, Server Url', optionalArg:false
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
//def dataFetcher = new DataFetcher(dbConfig)
def source = new StreamSource(uri)

    println ('Connecting to Server...')
    source.connect()
    println('Connected')
    sleep(10000)
    try {
        source.send('hello')
        println('Message has been delivered to Sink..')
    }catch (java.nio.channels.NotYetConnectedException nye){
        println(nye.message)
    } finally {
        println ('Closing Connection with Server...')
        source.close()
    }

 //dataFetcher.forEachRowFetch { row ->
//    source.send(row.toString())
//}

