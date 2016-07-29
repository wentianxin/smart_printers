package com.qg.smpt.lwc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.sql.PseudoColumnUsage;

import javax.xml.crypto.Data;

import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.util.BytesConvert;

public class Client {
	
	
	
	public static void main(String[] args) throws Exception{ 
			
			
			Socket socket = new Socket("127.0.0.1", 30000);
	
			BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
			long time = System.currentTimeMillis();
			
			
			BufferedOutputStream buff = new BufferedOutputStream(socket.getOutputStream());
			while(socket.isConnected()) {
				if((System.currentTimeMillis() - time) >= 5000) {
					time = System.currentTimeMillis();
					System.out.println("正在发送批次订单异常数据");
					
					byte[] data = createOKRequest();
//					byte[] data = createOrderErrorStatus();
//					byte[] data = createBulkStatus();
					
					
					System.out.println("当前要发送的数据为");
					for (int i = 0; i < data.length; i++)
			        {
			            String hex = Integer.toHexString(data[i] & 0xFF);
			            if (hex.length() == 1)
			            {
			                hex = '0' + hex;
			            }
			            System.out.print(hex.toUpperCase() + " ");
			        }
					System.out.println();
					
					buff.write(data);
					buff.flush();
					new DoPrint(socket).start();
//					
//					buff.write(data);
//					buff.flush();
//					new DoPrint(socket).start();
				}
//				
				
				
			}
		
	}
	
	
	private static byte[] createBulkStatus() {
		byte[] bytes = new byte[20];
		int pos = 0;
		
		pos = BytesConvert.fillShort(BConstants.statusEnd, bytes, pos);
		
		bytes[pos++] = BConstants.bulkStatus;
		bytes[pos++] = BConstants.bulkFail;
		
		pos = BytesConvert.fillInt(1, bytes, pos);
		pos = BytesConvert.fillInt((int)System.currentTimeMillis(), bytes, pos);
		
		pos = BytesConvert.fillShort((short)1, bytes, pos);
		pos = BytesConvert.fillShort((short)3, bytes, pos);
				
		
		pos = BytesConvert.fillShort((short)0, bytes, pos);
		pos = BytesConvert.fillShort(BConstants.orderEnd, bytes, pos);
		
		return bytes;
	}
	
	private static byte[] createOrderErrorStatus() {
		byte[] bytes = new byte[20];
		int pos = 0;
		
		pos = BytesConvert.fillShort(BConstants.statusEnd, bytes, pos);
		
		bytes[pos++] = BConstants.orderStatus;
		bytes[pos++] = BConstants.orderFail;
		
		pos = BytesConvert.fillInt(1, bytes, pos);
		pos = BytesConvert.fillInt((int)System.currentTimeMillis(), bytes, pos);
		
		pos = BytesConvert.fillShort((short)1, bytes, pos);
		pos = BytesConvert.fillShort((short)3, bytes, pos);
				
		
		pos = BytesConvert.fillShort((short)0, bytes, pos);
		pos = BytesConvert.fillShort(BConstants.orderEnd, bytes, pos);
		
		return bytes;
	}
	
	private static byte[] createOKRequest() {
		byte[] bytes = new byte[20];
		
		int pos = 0;
		
		pos = BytesConvert.fillShort(BConstants.statusEnd, bytes, pos);
		
		bytes[pos++] = BConstants.okStatus;
		pos++;
		
		pos = BytesConvert.fillInt(1, bytes, pos);
		pos = BytesConvert.fillInt((int)System.currentTimeMillis(), bytes, pos);
		pos = BytesConvert.fillInt(0, bytes, pos);
		
		pos = BytesConvert.fillShort((short)0, bytes, pos);
		pos = BytesConvert.fillShort(BConstants.statusEnd, bytes, pos);
		
		return bytes;
		
	}
	
}

class DoPrint extends Thread {
	private Socket socket = null;
	private BufferedInputStream buff = null;

	public DoPrint(Socket socket) throws Exception {
		this.socket = socket;
		buff = new BufferedInputStream(socket.getInputStream());
	}

	@Override
	public void run() {
		byte[] data = new byte[20];
		data = readFromServer(data);
		byte[] lengthB = new byte[2];
		System.arraycopy(data, 6, lengthB, 0, 2);
//		for(int i = 0; i < 20; i++) {
//			System.out.println(data[i]);
//		}
		int length = byteToInt(lengthB);
		System.out.println("当前批次的数据长度为 " + length);
		data = new byte[length - 20];
		data = readFromServer(data);
		try {
			System.out.println(new String(data, "gb2312"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private int  byteToInt(byte[] a) {
//		int v0 = (a[0] & 0xff) << 24;//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位 
//		int v1 = (a[1] & 0xff) << 16; 
		int v2 = (a[0] & 0xff) << 8; 
		int v3 = (a[1] & 0xff) ; 
		return  v2 + v3; 
	}

	public byte[] readFromServer(byte[] data) {
		try {
			buff.read(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;

	}
}
	
	
	
	


