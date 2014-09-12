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
try {
    println ('Connecting to Server...')
    source.connect()
    println ('Connected')
    source.send('hello from source...')
//dataFetcher.forEachRowFetch { row ->
//    source.send(row.toString())
//}

} catch (Throwable t) {
    println("Problem while Connecting...$t.message")
    t.printStackTrace()
} finally {
    println ('Closing Connection with Server...')
    source.close()
}

