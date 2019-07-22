package org.example;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.HttpConnection;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyServer extends AbstractHandler
{
    public static String readRequestContent(HttpServletRequest req) throws IOException {
        String line;
        StringBuffer requestBody = new StringBuffer(1024 * 5);
        BufferedReader reader = req.getReader();
        while((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        return requestBody.toString();
    }

    @SuppressWarnings("serial")
    public static class HelloServlet extends HttpServlet
    {
        @Override
        protected void doGet( HttpServletRequest request,
                               HttpServletResponse response ) throws ServletException,
                IOException
        {
            String rBody = readRequestContent(request);
            System.out.println(rBody);


            HttpConnection connection = HttpConnection.getCurrentConnection();
            System.out.println(connection);

            response.setContentType("text/html");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello from HelloServlet</h1>");
        }
    }


    public static void main( String[] args ) throws Exception
    {
        Server server = new Server(80);

        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);

        handler.addServletWithMapping(HelloServlet.class, "/*");

        server.start();

        server.join();
    }
}