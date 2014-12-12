package web

import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import groovyx.net.http.HTTPBuilder

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.HEAD

import java.security.KeyStore
import org.apache.http.conn.scheme.Scheme
import org.apache.http.conn.ssl.SSLSocketFactory

class HttpsStreamSource{

    private HTTPBuilder http

    HttpsStreamSource(URI uri) {
        http = new HTTPBuilder(uri.toURL().toString())

    }

    public void send(Map payload) {

        def keyStore = KeyStore.getInstance( KeyStore.defaultType )

        getClass().getResource( "/keystore.jks" ).withInputStream {
            keyStore.load( it, "changeit".toCharArray() )
        }

        http.client.connectionManager.schemeRegistry.register(
                new Scheme("https", new SSLSocketFactory(keyStore), 443) )

        http.request(POST) {

            //uri.path = '/'
            requestContentType = URLENC
            body = payload

            response.success = { resp ->
                //log.debug "POST response status: ${resp.statusLine}"
                assert resp.statusLine.statusCode == 200
            }
        }
    }


    /*def http = new HTTPBuilder('https://localhost:8443/')
//http.ignoreSSLIssues()

    def keyStore = KeyStore.getInstance( KeyStore.defaultType )

    getClass().getResource( "/keystore.jks" ).withInputStream {
        keyStore.load( it, "changeit".toCharArray() )
    }

    http.client.connectionManager.schemeRegistry.register(
    new Scheme("https", new SSLSocketFactory(keyStore), 443) )

    http.request( POST ) {
        uri.path = '/'
        requestContentType = URLENC
        body =  [name: 'bob', title: 'construction worker']

        response.success = { resp ->
            println "POST response status: ${resp.statusLine}"
            assert resp.statusLine.statusCode == 200
        }
    }*/
}
