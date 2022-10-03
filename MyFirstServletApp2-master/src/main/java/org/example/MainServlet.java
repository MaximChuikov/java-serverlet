package org.example;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Arrays;

@WebServlet("/")
public class MainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String currentPath = req.getParameter("path");
        if (currentPath == null || !currentPath.startsWith("D:")) {
            currentPath = System.getProperty("os.name").toLowerCase().startsWith("win") ? "D:" : "/opt/tomcat/";
            resp.sendRedirect("?path=" + currentPath);
        }
        else {
            File file = new File(currentPath);
            if (file.isFile()) {

                String filePath = currentPath;
                File downloadFile = new File(filePath);
                FileInputStream inStream = new FileInputStream(downloadFile);

                // obtains ServletContext
                ServletContext context = getServletContext();

                // gets MIME type of the file
                String mimeType = context.getMimeType(filePath);
                if (mimeType == null) {
                    // set to binary type if MIME mapping not found
                    mimeType = "application/octet-stream";
                }

                // modifies response
                resp.setContentType(mimeType);
                resp.setContentLength((int) downloadFile.length());

                // forces download
                String headerKey = "Content-Disposition";
                String headerValue = String.format("attachment; filename=\"%s\"", downloadFile.getName());
                resp.setHeader(headerKey, headerValue);

                // obtains response's output stream
                OutputStream outStream = resp.getOutputStream();

                byte[] buffer = new byte[4096];
                int bytesRead = -1;

                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inStream.close();
                outStream.close();

            }
            showFiles(req, new File(currentPath).listFiles(), currentPath);
            req.setAttribute("currentPath", currentPath);
            req.getRequestDispatcher("FileManager.jsp").forward(req, resp);
        }

    }

    private void showFiles(HttpServletRequest req, File[] files, String currentPath) {
        StringBuilder attrFolders = new StringBuilder();
        StringBuilder attrFiles = new StringBuilder();
        for (File file : files) {
            if (file.isDirectory()) {
                attrFolders.append("<li><a href=\"?path=").append(currentPath).append("/").append(file.getName())
                        .append("\">")
                        .append(file.getName())
                        .append("</a></li>");
            } else {
                attrFiles.append("<li><a href=\"?path=").append(currentPath).append("/").append(file.getName())
                        .append("\">")
                        .append(file.getName())
                        .append("</a></li>");
            }
        }
        req.setAttribute("folders", attrFolders);
        req.setAttribute("files", attrFiles);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String previousPath = req.getQueryString();
        String[] splintedPath = previousPath.split("/");
        String[] newPathArr = Arrays.copyOf(splintedPath, splintedPath.length - 1);
        String newPath = String.join("/", newPathArr);
        resp.sendRedirect("?" + newPath);
    }
}