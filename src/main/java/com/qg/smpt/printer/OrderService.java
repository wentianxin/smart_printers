//package com.qg.smpt.printer;
//
//import com.qg.smpt.printer.model.BOrder;
//import com.qg.smpt.share.ShareMem;
//import com.qg.smpt.web.model.BulkOrder;
//import com.qg.smpt.web.model.Order;
//import com.qg.smpt.web.model.Printer;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Queue;
//
///**
// * @author lee_cong
// * <pre>该类用于定义组装订单传输数据给打印机</pre>
// */
//public final class OrderServiceImpl {
//
//
//    /**
//     * 发送批次订单, synchronized
//     */
//    public void sendBatchOrder(Printer p) {
//        //获取打印机与他的订单集合
//        Queue<Order> os = ShareMem.priBufferQueueMap.get(p);
//
//        //判断是否满足打印机可接收数据和我有数据的条件
//        if((os != null && os.size() <= 0) || !p.isCanAccpet()) {
//            return;
//        }
//
//
//        doSend(p,os);
//
//    }
//
//    /**
//     * 执行发送操作,准备订单数据,并 依次组装批次
//     * @param printer   待接收数据的打印机
//     * @param orders    订单缓存队列
//     */
//    private void doSend(Printer printer, Queue<Order> orders) {
//        try {
//            //准备新的一个批次
//            int bulkId = printer.getCurrentBulk() + 1;
//            printer.setCurrentBulk(bulkId);
//            BulkOrder bulk = new BulkOrder(bulkId);
//
//            //为批次准备订单,获取该批次的订单数为多少个
//            prepareOrder(printer, orders, bulk);
//
//            //发送数据
//            send(bulk.getBulkOrderB(), printer);
//            printer.setCanAccpet(false);
//            Queue<BulkOrder> bulks = ShareMem.priSentQueueMap.get(printer);
//            bulks.add(bulk);
//
//        }catch(Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 准备批次订单,遍历订单集合取出订单组装成批次
//     * @param   printer     打印机
//     * @param orders        订单集合
//     * @return 当前为批次准备了多少份订单
//     * @throws Exception    暂定异常全抛,之后逻辑设计后再根据具体情况在里面抓获具体的异常
//     */
//    private void prepareOrder(Printer printer, Queue<Order> orders, BulkOrder bulk) throws Exception{
//        //创建批次
//        int bulkId = printer.getCurrentBulk();
//
//        //遍历订单缓存队列,组装批次,发送窗口大小不能超过最大值MAX_TRANSFER_SIZE
//        List<Order> os = new ArrayList<Order>();
//
//        short i = 1;        //用于保存当前已经存储了多少个订单到批次中
//        int currSize = 0;   //用于记录当前已经解析的订单有多少容量
//        long lastSendTime = printer.getLastSendTime();   //获取上一次批次的发送时间，是用于基于时间的发送批次使用
//
//
//        //基于批次定量的检测
//        while(currSize < Constants.MAX_TRANSFER_SIZE){
//
//            synchronized (orders) {
//
//
//                while (orders.size() > 0) {
//
//                    //解析订单，转换成字节数组
//                    Order o = orders.peek();
//                    BOrder oB = o.orderToBOrder((short) bulkId, i);
//                    byte[] orderB = BOrder.bOrderToBytes(oB);
//                    o.setData(orderB);
//                    int length = orderB.length;
//
//                    //判断已解析订单的大小加上当前解析的订单是否超过传输的阈值，超过则不继续组装
//                    if ((currSize + length) >= Constants.MAX_TRANSFER_SIZE)
//                        break;
//
//                    currSize += length;
//                    orders.poll();
//                    os.add(o);
//
//                }
//            }
//
//            if(currSize >= Constants.MAX_TRANSFER_SIZE)
//                break;
//
//            //判断当前时间与上一次发送时间是否已经超过发送批次的规定间隔5000ms
//            //不超过,则睡眠500ms,再次判断是否有订单数据,再次组装
//            //超过,则立刻将批次发送
//            long currTime = System.currentTimeMillis();
//            if(currTime - lastSendTime < Constants.SEND_INTERVAL){
//                //thread.sleep(500);
//                continue;
//
//            }else{
//                break;
//            }
//
//        }
//
//        bulk.setOrders(os);
//        bulk.setDataSize(currSize);
//
//    }
//
//    private void send(byte[] data, Printer printer) {
////       try {
////           //建立并转化批次数据
////           ByteBuffer buff = ByteBuffer.wrap(data);
////
////           //通过 socketChenal 发送数据
////           printer.setLastSendTime(System.currentTimeMillis());
//////           SocketChannel socketChannel = ShareMem.priLinkSocketMap.get(printer);
//////           socketChannel.write(buff);
////           Socket socket = ShareMem.printerSocket.get(printer);
////           BufferedOutputStream buffer = new BufferedOutputStream(socket.getOutputStream());
////           buffer.write(data);
////           buffer.flush();
////           System.out.println("发送数据成功");
////
////       }catch (IOException e) {
////           e.printStackTrace();
////       }
//
//    }
//
//
//    public void handleSuccessfulBulk(int printerId, int bulkId) {
//        //获取打印机的已发送批次队列
//        Printer printer = ShareMem.printerIdMap.get(printerId);
//        Queue<BulkOrder> bulks = ShareMem.priSentQueueMap.get(printer);
//
//        //找出对应的批次
//        BulkOrder bulk = findBulk(bulks, bulkId);
//
//        //如果找不到，则不操作
//        if(bulk == null)
//            return;
//
//        //若找到了，则从队列中移除，并将数据持久保存
//        bulks.remove(bulk);
//
//
//    }
//
//    public void handleFailBulk(int printerId, int bulkId) {
//        //获取打印机的已发送批次队列
//        Printer printer = ShareMem.printerIdMap.get(printerId);
//        Queue<BulkOrder> bulks = ShareMem.priSentQueueMap.get(printer);
//
//        //找出对应的批次
//        BulkOrder bulk = findBulk(bulks, bulkId);
//
//        //如果找不到，则不操作
//        if(bulk == null)
//            return;
//
//        //若找到了，则重新发送该批次
//        send(bulk.getBulkOrderB(), printer);
//    }
//
//
//
//    private BulkOrder findBulk(Queue<BulkOrder> bulks, int bulkId ) {
//        int i = 0;
//
//        BulkOrder bulk = null;
//        while(i < bulks.size()) {
//            bulk = bulks.peek();
//            if(bulk.getId() == bulkId) {
//                return bulk;
//            }
//            bulks.remove(bulk);
//            bulks.add(bulk);
//        }
//
//        return null;
//    }
//
//    public void handleFailOrder(int printerId, int bulkId, int index) {
//        //获取打印机的已发送批次队列
//        Printer printer = ShareMem.printerIdMap.get(printerId);
//        Queue<BulkOrder> bulks = ShareMem.priSentQueueMap.get(printer);
//
//        //找出对应的批次
//        BulkOrder bulk = findBulk(bulks, bulkId);
//
//        //若不存在该订单，则不操作
//        if(bulk == null || bulk.getOrders().size() < index)
//            return;
//
//        //取出订单，包装成加急批次进行重传
//        //当前加急批次订单使用订单号是上一次批次的订单号,不加进发送队列,打印机只需对
//        Order o = bulk.getOrders().get(index - 1);
//        BulkOrder newBulk = new BulkOrder(bulkId);
//        newBulk.setDataSize(o.getData().length);
//        newBulk.setBulkType((short)1);
//        List<Order> orders = new ArrayList<Order>(1);
//        orders.add(o);
//        newBulk.setOrders(orders);
//
//        byte[] buff = newBulk.getBulkOrderB();
//        send(buff,printer);
//    }
//
//
//}
