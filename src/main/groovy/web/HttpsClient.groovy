package web;

import java.io.*;
import java.net.*;
import java.security.KeyStore;
import javax.net.ssl.*;
public class HttpsClient {
    public static void main(String[] args) {
        PrintStream out = System.out;
        URI KEYSTORE = Thread.currentThread().getContextClassLoader().getResource('keystore.jks').toURI();
        char[] ksPass = "changeit".toCharArray();
        char[] ctPass = "changeit".toCharArray();

        try {
           KeyStore ks = KeyStore.getInstance("JKS")
            ks.load(new FileInputStream(new File(KEYSTORE)), ksPass)
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509")
            kmf.init(ks, ctPass)
            SSLContext sc = SSLContext.getInstance("TLS")
            sc.init(kmf.getKeyManagers(), null, null)
           SSLSocketFactory f = sc.getSocketFactory()

            // Getting the default SSL socket factory
            SSLSocket c =
                    (SSLSocket) f.createSocket("localhost", 8888);
            printSocketInfo(c);
            c.startHandshake();
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
                    c.getOutputStream()));
            BufferedReader r = new BufferedReader(new InputStreamReader(
                    c.getInputStream()));
            w.write("GET / HTTP/1.0");
            w.newLine();
            w.newLine(); // end of HTTP request
            w.flush();
            String m = null;
            while ((m=r.readLine())!= null) {
                out.println(m);
            }
            w.close();
            r.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    private static void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: "+s.getClass());
        System.out.println("   Remote address = "
                +s.getInetAddress().toString());
        System.out.println("   Remote port = "+s.getPort());
        System.out.println("   Local socket address = "
                +s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                +s.getLocalAddress().toString());
        System.out.println("   Local port = "+s.getLocalPort());
        System.out.println("   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = "+ss.getCipherSuite());
        System.out.println("   Protocol = "+ss.getProtocol());
    }
}
