//package com.qg.smpt.lwc;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.net.Socket;
//import java.nio.ByteBuffer;
//import java.nio.channels.SocketChannel;
//
//import com.qg.smpt.printer.OrderService;
//import com.qg.smpt.printer.OrderServiceClone;
//import com.qg.smpt.printer.model.BBulkStatus;
//import com.qg.smpt.printer.model.BConstants;
//import com.qg.smpt.printer.model.BOrderStatus;
//import com.qg.smpt.printer.model.BPrinterStatus;
//import com.qg.smpt.printer.model.BRequest;
//import com.qg.smpt.share.ShareMem;
//import com.qg.smpt.util.BytesConvert;
//import com.qg.smpt.web.model.Printer;
//
//public class HandleClient extends Thread{
//	private Socket socket;
//	private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//	private BufferedInputStream reader;
//	
//	public HandleClient(Socket socket)throws Exception {
//		this.socket = socket;
//		reader = new BufferedInputStream(socket.getInputStream());
//	}
//	
//	
//	@Override
//	public void run() {
//		while(socket.isConnected()) {
//			try {
////				long time = System.currentTimeMillis();
////				OrderService orderService = new OrderService();
////	            
////				while(true) {
////					if((System.currentTimeMillis() - time) == 5000) {
////						time = System.currentTimeMillis();
////						Printer p = Server.printer;
////						p.setCanAccpet(true);
////						orderService.sendBatchOrder(p);
////					}
////				}
//				parseData();
//			} catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		System.out.println("连接断开了");
//		try {
//			socket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	
//	 /**
//     * 解析数据
//	 * @throws Exception 
//     */
//    private void parseData() throws Exception{
//        try {
//        	byte[] bytes =  readFromClient();
//        	
//        	for (int i = 0; i < bytes.length; i++)
//	        {
//	            String hex = Integer.toHexString(bytes[i] & 0xFF);
//	            if (hex.length() == 1)
//	            {
//	                hex = '0' + hex;
//	            }
//	            System.out.print(hex.toUpperCase() + " ");
//	        }
//        	System.out.println("客户端发送的数据为");
//          
//
//            if (bytes[0] == (byte)0xCF && bytes[1] == (byte)0xFC) {
//                switch (bytes[2]) {
//                    case BConstants.connectStatus :
//
//                    case BConstants.okStatus:
//                    	parseOkStatus(bytes);
//                    	System.out.println("客户端发送过来可以请求数据");
//                    	break;
//
//                    case BConstants.orderStatus:
//                    	System.out.println("客户端发送过来订单状态数据");
//                    	parseOrderStatus(bytes);
//                    	break;
//
//                    case BConstants.bulkStatus:
//                    	parseBulkStatus(bytes);
//                    	System.out.println("客户端发送过来批次状态数据");
//                    	break;
//
//                    case BConstants.printStatus:
//
//                    default:
//                    	System.out.println("什么都没发");
//                }
//            }
//        } catch (IOException e) {
//
//        }
//
//    }
//    
//    private byte[] readFromClient() throws Exception{
//    	byte[] data = new byte[20];
//    	reader.read(data);
//    	return data;
//    }
//
//    private void parseConnectStatus(byte[] bytes) {
//        BRequest bRequest = BRequest.bytesToRequest(bytes);
//
//        int printerId = bRequest.printerId;
//
//        // 建立用户-printer 关系
//
//
//        if (ShareMem.printerIdMap.get(printerId) == null) {
//            synchronized (ShareMem.printerIdMap) {
//                ShareMem.printerIdMap.put(printerId, new Printer(printerId));
//            }
//        }
//    }
//
//    private void parseOkStatus(byte[] bytes) {
//        // 解析OK请求
//        
//        BRequest request = BRequest.bytesToRequest(bytes);
//
////        byte[] printerIdB = new byte[4];
////        System.arraycopy(bytes, 4, printerIdB, 0, 4);
//        
//        // 获取打印机主控板id,获取打印机
//        int printerId = request.printerId;
////        int printerId = BytesConvert.bytesToInt(printerIdB);
//        
//        Printer p = ShareMem.printerIdMap.get(printerId);
////        Printer p = Server.printer;
//        p.setCanAccpet(true);
//
//        //执行发送数据
//        try {
//
//            OrderServiceClone orderService = new OrderServiceClone();
//            orderService.sendBatchOrder(p,socket);
//
//        }catch(Exception e){
//            
//        }
//    }
//
//    private void parseOrderStatus(byte[] bytes) {
//        BOrderStatus bOrderStatus = BOrderStatus.bytesToOrderStatusLWC(bytes);
//
//        if ( (byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderSucc) {
//
//
//        }else if((byte)((bOrderStatus.flag >> 8) & 0xFF ) == (byte) BConstants.orderFail) {
//            // 订单异常 需要重新发送订单
//        	OrderServiceClone orderService = new OrderServiceClone();
//            orderService.handleFailOrder(1, 1, 3, socket);
//
//        }
//
//
//    }
//
//    private void parseBulkStatus(byte[] bytes) {
//        BBulkStatus bBulkStatus = BBulkStatus.bytesToBulkStatuslWC(bytes);
//        OrderServiceClone orderService = new OrderServiceClone();
////
////        if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
////            // 批次订单成功
////            // 将已发队列中数据装填到数据库中，并清除已发队列
//            orderService.handleSuccessfulBulk(1, 1);
////
////        } else  if ( (byte)((bBulkStatus.flag >> 8) & 0xFF) == (byte) BConstants.bulkSucc) {
//            // 批次订单失败 忽略失败信息-bug
////            orderService.handleFailBulk(1,1);
////        }
//    }
//
//    private void parsePrintStatus(byte[] bytes) {
//        BPrinterStatus bPrinterStatus = BPrinterStatus.bytesToPrinterStatus(bytes);
//
//        //TODO 状态待分析
//    }
//}
//
