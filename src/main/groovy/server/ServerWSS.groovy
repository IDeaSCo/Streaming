package server

import org.java_websocket.server.DefaultSSLWebSocketServerFactory

import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.security.KeyStore

def cli = new CliBuilder(usage:'server [-h <ip or hostname>] [-p <number>]')
cli.with {
    s  args:1, argName: 'serverUrl', longOpt:'serverUrl', 'OPTIONAL, Server IP/URL, default is localhost', optionalArg:true
    p  args:1, argName: 'port', longOpt:'port', 'OPTIONAL, Server Port, default is 9080', optionalArg:true
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

def host = '172.26.122.106'

if(options.s) {
    host = options.s
}

def port = 9080
if(options.port) {
    port = Integer.parseInt(options.port)
}
println "Starting WSS Server...ip = $host, port = $port"

def server = new StreamSinkWS(host,port)

// load up the key store
String STORETYPE = "JKS";
URI KEYSTORE = Thread.currentThread().getContextClassLoader().getResource('keystore.jks').toURI()
String STOREPASSWORD = "changeit";
String KEYPASSWORD = "changeit";

KeyStore ks = KeyStore.getInstance( STORETYPE );
File kf = new File( KEYSTORE );
ks.load( new FileInputStream( kf ), STOREPASSWORD.toCharArray() );

KeyManagerFactory kmf = KeyManagerFactory.getInstance( "SunX509" );
kmf.init( ks, KEYPASSWORD.toCharArray() );
TrustManagerFactory tmf = TrustManagerFactory.getInstance( "SunX509" );
tmf.init( ks );

SSLContext sslContext = null;
sslContext = SSLContext.getInstance( "TLS" );
sslContext.init( kmf.getKeyManagers(), tmf.getTrustManagers(), null );

server.setWebSocketFactory( new DefaultSSLWebSocketServerFactory( sslContext ) );

server.start();

println "Server started..."

