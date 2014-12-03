package web

import client.DataFetcherForOracle

def cli = new CliBuilder(usage:'httpclient -s <serverUrl> -d <databaseUrl> [--dbUser=someUser] [--dbPwd=somePwd] [--dbDriver=someDriver] [--dbName=dbName]')
cli.with {
    s  args:1, argName: 'server',longOpt:'serverUrl','OPTIONAL, Server Url, default is localhost', optionalArg:true
    d  args:1, argName: 'db',longOpt:'dburl','OPTIONAL,DB Url', optionalArg:true
    _  args:1, argName: 'dbUser',longOpt:'dbUser','OPTIONAL,DB User,Usage Eg: --dbUser=someUser', optionalArg:true
    _  args:1, argName: 'dbPwd',longOpt:'dbPwd','OPTIONAL,DB Password, Usage Eg: --dbPwd=somePwd', optionalArg:true
    _  args:1, argName: 'dbDriver',longOpt:'dbDriver','OPTIONAL,DB Driver,Usage Eg: --dbDriver=dbDriver', optionalArg:true
    _  args:1, argName: 'dbName',longOpt:'dbName','OPTIONAL,DB Name,Usage Eg: --dbName=dbName', optionalArg:true
    t  args:0, argName: 'test', longOpt:'test', 'OPTIONAL,Activate Test Mode (ignores Database Connection)', optionalArg:true
    c  args:1, argName: 'numberOfClients',longOpt:'numberOfClients','OPTIONAL,Number of Client Connection to Spawn, Default is 1, Usage Eg: --numberOfClients=2', optionalArg:true
    n  args:1, argName: 'collationCount',longOpt:'collationCount','OPTIONAL,Number of records to collate before send, Default is 1, Usage Eg: --collationCount=100', optionalArg:true
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

def serverUrl = options.s? options.s : 'localhost'
println "Server URL : $serverUrl"

def numberOfClients = options.c ? Integer.parseInt(options.c) : 1
println("Number of connections to spawn : ${numberOfClients}")
def uri = new URL(serverUrl).toURI()

def collationCount = options.n? Integer.parseInt(options.n) :1
println("Number of records to collate before sending : ${collationCount}")

def dbConfig = [url:options.d, user:options.dbUser, password:options.dbPwd, driver:options.dbDriver]
def dbName = options.dbName
println("Using DB : $dbName")

def pushTestData = options.t
Runtime runtime = Runtime.getRuntime();

long toMegabytes(long bytes) {
    bytes / (1024L * 1024L)
}
def usedMemory(runtime) {
    runtime.totalMemory() - runtime.freeMemory()
}

def pushData(senderName, dbConfig, dbName, serverUrl, shouldPushTestData, runtime, collationCount) {
    def dataFetcher = shouldPushTestData ? new DataFetcherForOracle() : new DataFetcherForOracle(dbConfig, dbName)
    long maxMemoryUsed = usedMemory(runtime)
    def source = new SocketStreamSource(8025,'172.26.122.106')
    println("$senderName Connecting to Server")

    def count = 0
    def startTime = System.currentTimeMillis()
    println ("Before fetching memory : ${usedMemory(runtime)}")
    def collatedData = new StringBuilder()
    def recordCount = 0;
    dataFetcher.fetchEachRowOracle { row ->
        collatedData << row
        recordCount ++
        if(recordCount == collationCount) {
            source.send("Sender: $senderName,Count:$count,  Data: ${collatedData}")
            collatedData = new StringBuilder()
            recordCount = 0
        }
        count++
        if(count % 1000 == 0){
            maxMemoryUsed = Math.max(maxMemoryUsed, usedMemory(runtime));
        }
    }
    def endTime = System.currentTimeMillis()
    def memory = ['memTotal': toMegabytes(runtime.totalMemory()), 'memUsed': toMegabytes(maxMemoryUsed)]
    source.send("Total $count Messages delivered to Sink from Sender: $senderName,\n Data: Total data transfer time : ${(endTime - startTime) / 1000} sec,  \nMemory (MB): $memory")
    source.close()
}



(1..numberOfClients).each { number ->
    def sourceClientName = "SourceClient#$number"
    Thread.start(sourceClientName) {
        pushData(sourceClientName, dbConfig, dbName, uri, pushTestData, runtime, collationCount)
    }
}
