package web

import groovy.util.logging.Slf4j
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

@Slf4j
class DataStreamSource {
    private HTTPBuilder http

    DataStreamSource(URI uri) {
        http = new HTTPBuilder(uri.toURL().toString())

    }

    public void send(Map payload) {
        http.request(POST) {
            //uri.path = '/'
            requestContentType = URLENC
            body = payload

            response.success = { resp ->
                log.debug "POST response status: ${resp.statusLine}"
                assert resp.statusLine.statusCode == 200
            }
        }
    }


}
