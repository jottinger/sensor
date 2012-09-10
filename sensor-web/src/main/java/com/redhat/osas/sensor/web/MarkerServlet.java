package com.redhat.osas.sensor.web;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.*;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/marker")
public class MarkerServlet extends HttpServlet {
    Image transparentImage;

    public MarkerServlet() {
        transparentImage = buildTransparentImage();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int stroke = parseInteger(request, "stroke", 0);
        int maxLevel = parseInteger(request, "maxLevel", 255);
        int size = parseInteger(request, "size", 20);

        if (size > 799) {
            System.err.println("Size of image has been truncated to 800x800");
            size = 799;
        }

        float shade = stroke / (maxLevel * 1.0f);

        BufferedImage image = drawCircle(size, shade);

        response.setContentType("image/png");
        OutputStream out = response.getOutputStream();
        ImageIO.write(image, "png", out);
        out.close();
    }

    private BufferedImage drawCircle(int size, float shade) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.drawImage(transparentImage, 0, 0, null);

        // we're going for yellows here
        g2.setColor(new Color(shade, shade, 0));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillOval(0, 0, size, size);
        return image;
    }

    private static int parseInteger(HttpServletRequest request,
                                    String name,
                                    int defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(name));
        } catch (Exception ignored) {
            // falling through will return the default value
        }

        return defaultValue;
    }

    private static Image buildTransparentImage() {
        BufferedImage image = new BufferedImage(800, 800,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        // force the background to be white...
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, 800, 800);

        return makeColorTransparent(image, Color.WHITE);
    }


    private static Image makeColorTransparent(final BufferedImage im,
                                              final Color color) {
        final ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for (white)...
            // Alpha bits are set to opaque
            public final int markerRGB = 0xFFFFFFFF;

            public final int filterRGB(final int x,
                                       final int y,
                                       final int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(im.getSource(),
                filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }
}