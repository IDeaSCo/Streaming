package client

import java.nio.channels.NotYetConnectedException

def cli = new CliBuilder(usage:'client -s <serverUrl> -d <databaseUrl> [--dbUser=someUser] [--dbPwd=somePwd] [--dbDriver=someDriver] [--dbName=dbName]')
cli.with {
    s  args:1, argName: 'server',longOpt:'serverUrl','OPTIONAL, Server Url, default is localhost', optionalArg:true
    d  args:1, argName: 'db',longOpt:'dburl','OPTIONAL,DB Url', optionalArg:true
    _  args:1, argName: 'dbUser',longOpt:'dbUser','OPTIONAL,DB User,Usage Eg: --dbUser=someUser', optionalArg:true
    _  args:1, argName: 'dbPwd',longOpt:'dbPwd','OPTIONAL,DB Password, Usage Eg: --dbPwd=somePwd', optionalArg:true
    _  args:1, argName: 'dbDriver',longOpt:'dbDriver','OPTIONAL,DB Driver,Usage Eg: --dbDriver=dbDriver', optionalArg:true
    _  args:1, argName: 'dbName',longOpt:'dbName','OPTIONAL,DB Name,Usage Eg: --dbName=dbName', optionalArg:true
    t  args:0, argName: 'test', longOpt:'test', 'OPTIONAL,Activate Test Mode (ignores Database Connection)', optionalArg:true
    c  args:1, argName: 'numberOfClients',longOpt:'numberOfClients','OPTIONAL,Number of Client Connection to Spawn, Default is 1, Usage Eg: --numberOfClients=2', optionalArg:true
}

def options = cli.parse(args)

if(!options) {
    return
}

if(options.arguments()){
    println "Cannot understand ${options.arguments()}"
    cli.usage()
    return
}

if(options.t == false && options.d == false) {
    cli.usage()
    return
}

def serverUrl = options.s? options.s : 'http://localhost:9080'
println "Server URL : $serverUrl"

def numberOfClients = options.c ? Integer.parseInt(options.c) : 1
println("Number of connections to spawn : ${numberOfClients}")
def uri = new URL(serverUrl).toURI()

def dbConfig = [url:options.d, user:options.dbUser, password:options.dbPwd, driver:options.dbDriver]
def dbName = options.dbName
println("Using DB : $dbName")

def pushTestData = options.t

def pushData(senderName, dbConfig, dbName, serverUrl, shouldPushTestData) {
    def dataFetcher = shouldPushTestData ? new DataFetcher() : new DataFetcher(dbConfig, dbName)
    def source = new StreamSource(serverUrl)
    println("$senderName Connecting to Server")
    source.connect()
    try {
        Thread.sleep(1000)
        def count = 0
        def startTime = System.currentTimeMillis()
        dataFetcher.fetchEachRow { row ->
            source.send("Sender: $senderName, Data: ${row}")
            count++
        }
        def endTime = System.currentTimeMillis()
        source.send("Total $count Messages delivered to Sink from Sender: $senderName, Data: Total data transfer time : ${(endTime - startTime) / 1000} sec ")
    } catch (NotYetConnectedException nye) {
        println(nye.message)
    } finally {
        println("$senderName closing Connection with Server...")
        source.close()
    }
}

(1..numberOfClients).each { number ->
    def sourceClientName = "SourceClient#$number"
    Thread.start(sourceClientName) {
        pushData(sourceClientName, dbConfig, dbName, uri, pushTestData)
    }
}
