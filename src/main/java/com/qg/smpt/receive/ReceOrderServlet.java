package com.qg.smpt.receive;

import com.qg.smpt.printer.Constants;
import com.qg.smpt.printer.PrinterProcessor;
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
        List<Printer> printers =  ShareMem.userListMap.get(userId);
        if (printers == null) {
            LOGGER.log(Level.DEBUG, "内存缓冲中暂未商家 [{1}] 对应的打印机 , 需查询数据库", userId);

            User user = null;

            if (ShareMem.userIdMap.get(userId) == null) {
                LOGGER.log(Level.DEBUG, "内存中暂无商家信息 [{0}]", userId);
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuild.getSqlSessionFactory();
                SqlSession sqlSession = sqlSessionFactory.openSession();
                UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
                try {
                    user = userMapper.selectUserPrinter(userId);
                } finally {
                    sqlSession.close();
                }
                if (user == null) {
                    LOGGER.log(Level.WARN, "数据库中无该商家 [{0}]", userId);
                    return ;
                }
                LOGGER.log(Level.DEBUG, "已将商家信息 id:[{0}], name [{1}], address [{2}], store [{3}] 放入内存中", user.getId(), user.getUserName(),
                        user.getUserAddress(), user.getUserStore());

                ShareMem.userIdMap.put(userId, user);
            }

            if ( user.getPrinters() == null || user.getPrinters().size() < 1) {
                LOGGER.log(Level.WARN, "商家暂时未购买打印机 [{0}]", userId);
                return;
            } else {
                printers = user.getPrinters();
                LOGGER.log(Level.DEBUG, "建立商家 [{0}]-打印机列表 [{1}] 关系", user.getId(), user.getPrinters());
                ShareMem.userListMap.put(user.getId(), user.getPrinters());

                for(Printer p : user.getPrinters()) {
                    LOGGER.log(Level.DEBUG, "建立打印机id [{0}]-打印机对象关系", p.getId());
                    ShareMem.printerIdMap.put(p.getId(), p);
                }
            }
        }


        if (printers != null && printers.size() > 0) {
            if (ShareMem.priBufferMapList != null) {

                // TODO 缺少智能分发算法
                Printer printer = printers.get(0);

                LOGGER.log(Level.DEBUG, "分发的打印机id: [{0}], 商家: [{1}]", printer.getId(), userId);

                List<BulkOrder> bOrdersList = ShareMem.priBufferMapList.get(printer);
                if (bOrdersList == null) {
                    LOGGER.log(Level.DEBUG, "初始化打印机-打印机缓存批次订单对象 priBufferMapList 关系");
                    bOrdersList = new ArrayList<BulkOrder>();

                    // 创建批次订单容器
                    ShareMem.priBufferMapList.put(printer, bOrdersList);
                    BulkOrder bOrders = new BulkOrder(new ArrayList<BOrder>());
                    bOrders.setId(printer.getCurrentBulk()+1);
                    printer.setCurrentBulk(printer.getCurrentOrder()+1);
                    bOrdersList.add(bOrders);

                }
                if (bOrdersList.size() == 0) {
                    bOrdersList.add(new BulkOrder(new ArrayList<BOrder>()));
                }


                LOGGER.log(Level.DEBUG, "当前打印机缓存批次订单队列长度：[{0}], 获取第[{1}] 个批次订单", bOrdersList.size(), bOrdersList.size());
                // 获取容器中最后一个批次订单
                BulkOrder bOrders = bOrdersList.get(bOrdersList.size() - 1);

                LOGGER.log(Level.DEBUG, "订单数据转化打印机发送数据");
                BOrder bOrder = order.orderToBOrder((short) printer.getCurrentBulk(), (short) bOrders.getbOrders().size());

                if (bOrders.getDataSize() + bOrder.size > Constants.MAX_TRANSFER_SIZE) {
                    if (bOrders.getbOrders().size() == 0) {
                        LOGGER.log(Level.WARN, "设置阈值与订单数据大小有误， 请重新检查");
                        return ;
                    }
                    // 需要将该订单分发给下一个批次订单
                    LOGGER.log(Level.DEBUG, "打印机[{0}] 批次容量 [{1}]已满足", printer.getId(), bOrders.getId());
                    bOrders = new BulkOrder(new ArrayList<BOrder>());

                    bOrders.getbOrders().add(bOrder);
                    bOrders.getOrders().add(order);
                    bOrders.setDataSize(bOrder.size + bOrders.getDataSize());
                    bOrders.setId(printer.getCurrentBulk()+1);
                    printer.setCurrentBulk(printer.getCurrentBulk()+1);

                    bOrder.inNumber = (short) 0x1;

                    bOrdersList.add(bOrders);

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
                    bOrder.inNumber = (short) 1;
                    order.setMpu(printer.getId());
                    LOGGER.log(Level.DEBUG, "不满足唤醒打印机 [{0}] 已睡眠线程的条件, 打印机缓冲队列 [{1}]，" +
                            "批次订单数 [{2}], 最后批次订单号 [{3}], 最后批次订单容量 [{4}] byte" , printer.getId(),
                            bOrdersList.size(), bOrders.getId(), bOrders.getDataSize());
                }
            }
        }

    }

}

