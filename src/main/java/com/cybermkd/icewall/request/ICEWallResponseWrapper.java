package com.cybermkd.icewall.request;

import com.cybermkd.icewall.ZipStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ICEWallResponseWrapper extends HttpServletResponseWrapper {

    private HttpServletResponse response;

    private ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private PrintWriter pw;

    private String encoding = "UTF-8";

    public ICEWallResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    public byte[] getOldBytes() {
        if (pw != null) {
            pw.close();
        }
        return baos.toByteArray();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new ZipStream(baos);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(baos, encoding));
        return pw;
    }
}
