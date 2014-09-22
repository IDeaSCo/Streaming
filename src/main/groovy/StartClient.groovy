import client.DataFetcher
import client.StreamSource

import java.nio.channels.NotYetConnectedException

def cli = new CliBuilder(usage:'client -s <serverUrl> -d <databaseUrl> [--dbUser=someUser] [--dbPwd=somePwd] [--dbDriver=someDriver]')
cli.with {
    s  args:1, argName: 'server',longOpt:'serverUrl','REQUIRED, Server Url', optionalArg:false
    d  args:1, argName: 'db',longOpt:'dburl','OPTIONAL,DB Url', optionalArg:true
    _  args:1, argName: 'dbUser',longOpt:'dbUser','OPTIONAL,DB User,Usage Eg: --dbUser=someUser', optionalArg:true
    _  args:1, argName: 'dbPwd',longOpt:'dbPwd','OPTIONAL,DB Password, Usage Eg: --dbPwd=somePwd', optionalArg:true
    _  args:1, argName: 'dbDriver',longOpt:'dbDriver','OPTIONAL,DB Driver,Usage Eg: --dbDriver=dbDriver', optionalArg:true
    t  args:0, argName: 'test', longOpt:'test', 'OPTIONAL,Activate Test Mode (ignores Database Connection)', optionalArg:true
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

def serverUrl = options.s
println "$serverUrl"

def uri = new URL(serverUrl).toURI()

def dbConfig = [url:options.d, user:options.dbUser, password:options.dbPwd, driver:options.dbDriver]
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
        println "Sending message...${row}"
        source.send("${row}")
    }
    println('Message has been delivered to Sink..')
} catch (NotYetConnectedException nye){
    println(nye.message)
} finally {
    println ('Closing Connection with Server...')
    source.close()
}
