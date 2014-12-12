package web;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;

  import org.eclipse.jetty.server.Request;
  import org.eclipse.jetty.server.handler.AbstractHandler;

  public class HelloHandler extends AbstractHandler
  {
              final String _greeting;
              final String _body;
              Map<String, Long> startTime;
                Map<String, Long> endTime;
                int numberOfClients;
      int clientsEngaged;

              public HelloHandler()
              {
                  _greeting="Hello World";
                  startTime = new HashMap<String,Long>();
                  endTime = new HashMap<String,Long>();
                  numberOfClients = 0;
                  clientsEngaged = 0;
                  _body=null;
             }

              public HelloHandler(String greeting)
              {
                  _greeting=greeting;
                  _body=null;
              }

              public HelloHandler(String greeting,String body)
              {
                  _greeting=greeting;
                  _body=body;
              }

              public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
              {
                  response.setContentType("text/html;charset=utf-8");
                  response.setStatus(HttpServletResponse.SC_OK);
                  baseRequest.setHandled(true);
                  if(request.getParameter("TotalMessagesDelivered")!=null){
                      if (request.getParameter("Sender").equalsIgnoreCase("Initiator")){
                          startTime = new HashMap<String,Long>();
                          endTime = new HashMap<String,Long>();
                          numberOfClients = Integer.parseInt(request.getParameter("NumberOfClients"));
                          clientsEngaged = 0;
                      }else if (clientsEngaged!=numberOfClients){
                          System.out.println("Sender :" + request.getParameter("Sender") + " Total Time = " + request.getParameter("Data"));
                          endTime.put(request.getParameter("Sender"), System.currentTimeMillis());
                          clientsEngaged++;
                      }
                      if (clientsEngaged==numberOfClients){
                          List<String> list = new ArrayList<String>(startTime.keySet());

                          Collections.reverse(list);
                          for(String e:list){
                              System.out.println(e+" Server side time = "+((endTime.get(e)-startTime.get(e))/1000.0)+" sec");
                          }
                      }

                  }else if (request.getParameter("Count")!=null){
                      if(Integer.parseInt(request.getParameter("Count"))==0){
                          startTime.put(request.getParameter("Sender"), System.currentTimeMillis());
                      }
                  }
                  //+request.getParameter("Data")+
                  response.getWriter().println("<h1>"+"</h1>");
                  if (_body!=null)
                      response.getWriter().println(_body);
              }
          }