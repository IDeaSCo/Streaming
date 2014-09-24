package runners

import client.Client

def dbNames = ['K2000']
//def dbNames = ['K1', 'K10', 'K100', 'K1000', 'K2000']
Thread.sleep(10000)
dbNames.each { dbName ->
    Binding context = new Binding()
    def newArgs = args as List
    newArgs << "--dbName=$dbName"
    context.setVariable('args', newArgs[0..<newArgs.size()])
    script = new Client(context)
    script.run()
}
