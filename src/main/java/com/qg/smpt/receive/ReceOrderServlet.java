package com.qg.smpt.receive;

import com.qg.smpt.printer.Constants;
import com.qg.smpt.printer.PrinterProcessor;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.model.BOrder;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.util.SqlSessionFactoryBuild;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.PrinterMapper;
import com.qg.smpt.web.repository.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.border.Border;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接收外卖订单数据的Servlet
 */
@WebServlet(name = "orderservlet")
public class ReceOrderServlet extends HttpServlet {
    private static final Logger LOGGER =  Logger.getLogger(ReceOrderServlet.class);

    public void start() {
        Order order = new Order();

        order.setId(1);

    }
    public void doGet(Integer userId, Order order) throws ServletException, IOException {
        // 获取商家id
//        Integer userId = null;
//        try {
//            userId = Integer.valueOf(request.getParameter("userId"));
//        } catch (NumberFormatException e) {
//
//        }
        // 根据商家id获取商家的打印机信息
	  	
        LOGGER.log(Level.DEBUG, "商家 [{0}] 接收订单数据 [{1}]", userId, order.toString());

        /* 获取商家信息 ： 检查内存中是否含有商家信息 */
        User user = getUser(userId);
        if (user == null) { return ; }
        /* 获取用户购买的打印机信息 */
//        List<Printer> printers = getPrinters(userId, user);
//        if (printers == null || printers.size() <= 0) { return ; }

        order.setUserId(userId);

        Printer printer = selectPrinter(user.getPrinters());
        if (printer == null) {
            synchronized (ShareMem.userOrderBufferMap) {
                List<Order> orders = ShareMem.userOrderBufferMap.get(userId);
                if (orders == null) {
                    orders = new ArrayList<>();
                    ShareMem.userOrderBufferMap.put(userId, orders);
                }
                orders.add(order);
            }
            return ;
        }

        /* 获取打印机对应缓冲队列　添加数据, 判断是否满足线程唤醒的条件　*/
        setOrderData(printer, userId, order);

        // 添加打印机订单总数量
        // 添加打印机订单未发送数量
        synchronized (printer) {
            printer.setOredrsNum(printer.getOredrsNum() + 1);
            printer.setUnsendedOrdersNum(printer.getSendedOrdersNum() + 1);
        }
    }

    private User getUser(int userId) {
        User user = ShareMem.userIdMap.get(userId) ;
        synchronized (ShareMem.userIdMap) {
            if (user == null) {
                LOGGER.log(Level.DEBUG, "内存中暂无商家信息 [{0}]", userId);

                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuild.getSqlSessionFactory();
                SqlSession sqlSession = sqlSessionFactory.openSession();
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                try {
                    user = userMapper.selectUserPrinter(userId);
                } finally {
                    sqlSession.commit();
                    sqlSession.close();
                }
                if (user == null) {
                    LOGGER.log(Level.WARN, "数据库中无该商家 [{0}]", userId);
                    return null;
                }
                LOGGER.log(Level.DEBUG, "已将商家信息 id:[{0}], name [{1}], address [{2}], store [{3}] 放入内存中", user.getId(), user.getUserName(),
                        user.getUserAddress(), user.getUserStore());

                ShareMem.userIdMap.put(userId, user);

                if (user.getPrinters() == null || user.getPrinters().size() < 1) {
                    LOGGER.log(Level.WARN, "商家 [{0}] 未购买 或 登记打印机信息", userId);
                    return null;
                }

                for (Printer p : user.getPrinters()) {
                    p.setConnected(false);
                }
            }
        }
        return user;
    }

    private List<Printer> getPrinters(int userId, User user) {
        List<Printer> printers =  ShareMem.userIdMap.get(userId).getPrinters();

//        if (printers == null) {
//            /* 查询数据库中商家的打印机信息 */
//            LOGGER.log(Level.DEBUG, "内存缓冲中暂未商家 [{0}] 对应的打印机 , 需查询数据库", userId);
//
//            if ( user.getPrinters() == null || user.getPrinters().size() < 1) {
//                LOGGER.log(Level.WARN, "商家暂时未购买打印机 [{0}]", userId);
//                return null;
//            } else {
//                printers = user.getPrinters();
//                LOGGER.log(Level.DEBUG, "建立商家 [{0}]-打印机列表 [{1}] 关系", user.getId(), user.getPrinters());
//                ShareMem.userListMap.put(user.getId(), user.getPrinters());
//                // 锁住打印机列表对象
//                synchronized (ShareMem.printerIdMap) {
//                    for (Printer p : user.getPrinters()) {
//                        LOGGER.log(Level.DEBUG, "建立打印机id [{0}]-打印机对象关系", p.getId());
//                        ShareMem.printerIdMap.put(p.getId(), p);
//                        p.setConnected(false);
//                        // TODO 漏洞，打印机未连接时，而是开启其他打印机
//                    }
//                }
//            }
//        }

        for (Printer p : printers) {
            p.setConnected(false);
        }

        return printers;
    }

    private void setOrderData(Printer printer, int userId, Order order) {

        LOGGER.log(Level.DEBUG, "分发的打印机id: [{0}], 商家: [{1}]", printer.getId(), userId);

        order.setMpu(printer.getId());

        /* 获取打印机对应批次订单容器 */
        List<BulkOrder> bOrdersList = getBulkBuffer(printer);
        
        BulkOrder bOrders = null;
        if (bOrdersList.size() > 0) {
            bOrders = bOrdersList.get(bOrdersList.size() - 1); // 获取容器中最后一个批次订
        } else {
            bOrders = new BulkOrder(new ArrayList<BOrder>());
            printer.increaseBulkId();
            bOrders.setId(printer.getCurrentBulk());
            bOrdersList.add(bOrders);
        }

        LOGGER.log(Level.DEBUG, "订单数据转化打印机发送数据");


        BOrder bOrder = order.orderToBOrder((short) printer.getCurrentBulk(), (short) bOrders.getbOrders().size());


        if (bOrders.getDataSize() + bOrder.size > Constants.MAX_TRANSFER_SIZE) {
            if (bOrders.getbOrders().size() == 0) {
                LOGGER.log(Level.WARN, "设置阈值与订单数据大小有误， 请重新检查");
                return ;
            }
            // 需要将该订单分发给下一个批次订单
            LOGGER.log(Level.DEBUG, "打印机[{0}] 第[{1}]批次容量已满足", printer.getId(), bOrders.getId());

            order.setMpu(printer.getId());

            bOrders = new BulkOrder(new ArrayList<BOrder>());
            bOrders.getbOrders().add(bOrder);
            bOrders.getOrders().add(order);
            bOrders.setDataSize(bOrder.size + bOrders.getDataSize());
            printer.increaseBulkId();
            bOrders.setId(printer.getCurrentBulk());

            bOrdersList.add(bOrders);

            bOrder.inNumber = (short) 0x1;
            bOrder.bulkId = (short)bOrders.getId();

            LOGGER.log(Level.DEBUG, "订单的批次id: [{0}], 批次内序号 [{1}]", bOrder.bulkId, bOrder.inNumber);

            // 如果打印机可以接收数据，唤醒线程
            if (printer.isCanAccept()) {
                LOGGER.log(Level.INFO, "打印机 [{0}] 已发送阈值状态", printer.getId());
                PrinterProcessor processor = ShareMem.priPriProcessMap.get(printer);
                if (processor == null) {
                    LOGGER.log(Level.ERROR, "printerProcessor is null, send data if failed");
                    return ;
                }
                LOGGER.log(Level.INFO, "获取打印机 [{0}] 已睡眠printerProcessor 线程 [{1}], 唤醒该线程", printer.getId(), processor.getId());
                processor.notifyOK();
            } else {
                LOGGER.log(Level.INFO, "打印机 [{0}] 未发送阈值状态", printer.getId());
            }
        } else {
            bOrders.getbOrders().add(bOrder);
            bOrders.getOrders().add(order);
            bOrders.setDataSize( bOrders.getDataSize() + bOrder.size);
            bOrder.bulkId = (short)bOrders.getId();
            bOrder.inNumber = (short)bOrders.getOrders().size();
            order.setMpu(printer.getId());
            LOGGER.log(Level.DEBUG, "订单的批次id: [{0}], 批次内序号 [{1}]", bOrder.bulkId, bOrder.inNumber);

            LOGGER.log(Level.DEBUG, "不满足唤醒打印机 [{0}] 已睡眠线程的条件, 打印机缓冲队列 [{1}]，" +
                            "批次订单数 [{2}], 最后批次订单号 , 最后批次订单容量 [{3}] byte" , printer.getId(),
                    bOrdersList.size(), bOrders.getId(), bOrders.getDataSize());

        }
    }

    /**
     * 当因打印机未连接而将订单数据存放到 userOrderBufferMap 对象中时, 在打印机重新建立连接时重新发送该对象中数据
     * @param p
     * @param userId
     */
    public void sendUserOrderBuffer(Printer p, int userId) {

        List<Order> orders = ShareMem.userOrderBufferMap.get(userId);

        ShareMem.userOrderBufferMap.remove(userId);

        for (Order o : orders) {
            setOrderData(p, userId, o);
        }
    }

    private Printer selectPrinter(List<Printer> printers) {
        // TODO 缺少智能分发算法
        for (Printer p : printers) {
            if (p.isConnected() ) {
                return p;
            }
        }

        return  null;
    }

    private List<BulkOrder> getBulkBuffer(Printer printer) {
        List<BulkOrder> bOrdersList = ShareMem.priBufferMapList.get(printer);
        if (bOrdersList == null) {
            LOGGER.log(Level.DEBUG, "初始化打印机-打印机缓存批次订单对象 priBufferMapList 关系");
            bOrdersList = new ArrayList<BulkOrder>();

            // 创建批次订单容器
            ShareMem.priBufferMapList.put(printer, bOrdersList);

        }

        LOGGER.log(Level.DEBUG, "当前打印机缓存批次订单队列长度：[{0}], 获取第[{1}] 个批次订单", bOrdersList.size(), bOrdersList.size());

        return bOrdersList;
    }
}

