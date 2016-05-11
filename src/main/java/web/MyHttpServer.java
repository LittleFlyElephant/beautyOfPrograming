package web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import calculate.*;
import calculate.Test;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;  
import com.sun.net.httpserver.HttpServer;  
import com.sun.net.httpserver.spi.HttpServerProvider;

import com.sun.net.httpserver.HttpContext;
import dataservice.DataImpl;
import dataservice.DataService;

/**
* beautyOfPrograming/web/MyHttpServer.java
* @author cxworks
* 2016年5月11日 上午9:57:28
*/

public class MyHttpServer {
    public  void httpserverService() throws IOException {  
        HttpServerProvider provider = HttpServerProvider.provider();  
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(8080), 100);
        httpserver.createContext("/", new MyHttpHandler());   
        httpserver.setExecutor(null);  
        httpserver.start();  
    }  
    //Http请求处理类  
    static class MyHttpHandler implements HttpHandler {  
        public void handle(HttpExchange httpExchange) throws IOException {  
            String param=httpExchange.getRequestURI().getQuery();
            StringTokenizer stringTokenizer=new StringTokenizer(param, "=&");
            stringTokenizer.nextToken();
            String id1=stringTokenizer.nextToken();
            stringTokenizer.nextToken();
            String id2=stringTokenizer.nextToken();
//            CalService calService = new CalImpl();
            DataService service = new DataImpl();
            calculate.Test main = new Test(service);
            System.out.println(id1+" "+id2);
            String ans=main.calculate(id1, id2);
            //
            httpExchange.sendResponseHeaders(200, ans.length());
            	OutputStream outputStream=httpExchange.getResponseBody();
            	outputStream.write(ans.getBytes());
            	outputStream.flush();
            	httpExchange.close();
        }  
    }
}
