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
