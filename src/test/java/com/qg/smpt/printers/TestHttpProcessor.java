package com.qg.smpt.printers;

import com.qg.smpt.printer.model.AbstactStatus;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.util.DebugUtil;

import java.io.*;
import java.net.Socket;

/**
 * Created by tisong on 7/28/16.
 */
public class TestHttpProcessor {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 8086);

        OutputStream outputStream = socket.getOutputStream();

        InputStream inputStream = socket.getInputStream();


        while (true) {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            byte[] bytes;

            String s = br.readLine();
            if (s.equals("connection")) {
                bytes = buildAbstractStatus((short)((BConstants.connectStatus << 8) & 0xFFFF) );
                outputStream.write(bytes);
                outputStream.flush();
            }
            if (s.equals("ok")) {
                bytes = buildAbstractStatus( (short) ( (BConstants.okStatus << 8) & 0xFFFF) );
                outputStream.write(bytes);
                outputStream.flush();
            }

            DataInputStream input = new DataInputStream(socket.getInputStream());


        }
    }

    private static byte[] buildAbstractStatus(short flag) {
        AbstactStatus abstactStatus = new AbstactStatus();

        abstactStatus.flag =  flag;

        abstactStatus.line1 = (int)0x1;

        abstactStatus.line2 = 0x0;

        abstactStatus.line3 = 0x0;

        abstactStatus.checkSum = 0x0;

        byte[] bytes = AbstactStatus.abstratcStatusToBytes(abstactStatus);


        DebugUtil.printBytes(bytes);

        return bytes;
    }



}
