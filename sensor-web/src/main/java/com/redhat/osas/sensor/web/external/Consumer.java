package com.redhat.osas.sensor.web.external;

import com.redhat.osas.sensor.data.DataPoint;
import org.codehaus.jackson.map.ObjectMapper;
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

@WebServlet("/sensor")
public class Consumer extends HttpServlet {
    @Resource(lookup = "java:comp/env/sensorData")
    private CacheContainer container;
    private Cache<String, DataPoint> cache;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        cache = container.getCache("dataPoints");

        String[] data = request.getParameterValues("data");
        String datum = "";
        if (null != data) {
            datum = data[0];
            ObjectMapper mapper = new ObjectMapper();
            DataPoint dataPoint = mapper.readValue(datum, DataPoint.class);
            cache.put(dataPoint.getDeviceId(), dataPoint);
            System.out.println(dataPoint);
        }
        response.setContentType("text/html");
        response.setBufferSize(8192);

        PrintWriter out = response.getWriter();
        out.println("<html><body><form method=\"POST\">");
        out.println("<textarea name=\"data\" rows=\"3\" cols=\"40\">");
        out.println(datum);
        out.println("</textarea><br>");
        out.println("<input type=\"submit\">");
        out.println("</form>");
        out.println("</body></html>");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
