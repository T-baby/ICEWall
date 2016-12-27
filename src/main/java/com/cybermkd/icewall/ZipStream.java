package com.cybermkd.icewall;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 创建人:T-baby
 * 创建日期: 2016/12/27
 * 文件描述:
 */
public class ZipStream extends ServletOutputStream {
    private OutputStream baos;

    public ZipStream(OutputStream baos) {
        this.baos = baos;
    }

    @Override
    public void write(int b) throws IOException {
        baos.write(b);
    }


    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }
}
