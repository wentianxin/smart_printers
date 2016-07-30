package com.qg.smpt.printers;

import com.qg.smpt.printer.model.AbstactStatus;
import com.qg.smpt.printer.model.BBulkOrder;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.util.BytesConvert;
import com.qg.smpt.util.DebugUtil;
import com.qg.smpt.web.model.BulkOrder;

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
            else if (s.equals("ok")) {
                bytes = buildAbstractStatus( (short) ( (BConstants.okStatus << 8) & 0xFFFF) );
                outputStream.write(bytes);
                outputStream.flush();


            }
            else if (s.equals("bulk")) {
                bytes = buildAbstractStatus( (short) ( ( (BConstants.bulkStatus << 8) & 0xFFFF) | 0x1 ));
                outputStream.write(bytes);
                outputStream.flush();

            }
            else {

                byte[] byte4 = new byte[4];
                int i = 0;

                byte[] t = new byte[1024];
                inputStream.read(t);
//
//                while (inputStream.read(byte4) != -1) {
//                    StringBuffer stringBuffer = new StringBuffer();
//                    for (int j = 0; j < 4; j++)
//                        stringBuffer.append(Integer.toHexString(byte4[j] & 0xFF) + " | ");
//                    System.out.println("第" + i + "字节 ： " + stringBuffer.toString());
//                    i++;
//                }



                System.out.println("接收数据完成");
                i = 0;
                //DebugUtil.printBytes(byte4);
            }

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
