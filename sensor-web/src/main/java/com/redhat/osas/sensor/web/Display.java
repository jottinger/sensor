package com.redhat.osas.sensor.web;

import com.redhat.osas.sensor.data.DataPoint;
import org.infinispan.Cache;
import org.infinispan.manager.CacheContainer;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/display")
public class Display extends HttpServlet {
    @Resource(lookup = "java:comp/env/sensorData")
    private CacheContainer container;
    private Cache<String, DataPoint> cache;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cache = container.getCache("dataPoints");

        response.setContentType("text/html");
        response.setBufferSize(8192);

        PrintWriter out = response.getWriter();
        out.println("<html><head></head>");
        out.println("<body>");
        out.println("<table>");
        for (String key : cache.keySet()) {
            out.print("<tr><td>");
            out.print(key);
            out.println("</td>");
            out.print("<td width='" + cache.get(key).getLevel() + "' bgcolor='black'>FOO</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("</body></html>");
    }
}
