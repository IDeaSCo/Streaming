package runners

import client.Client
import server.Server

def scriptName = args[0]
Binding context = new Binding()
context.setVariable('args', args[1..<args.length])
if (scriptName == 'client') {
    script = new Client(context)
} else if (scriptName == 'server') {
    script = new Server(context)
} else {
    throw new IllegalArgumentException("Don't know how to process: $scriptName")
}

script.run()