package com.qg.smpt.printer.util.database;

import com.qg.smpt.printer.OrderToPrinter;
import com.qg.smpt.printer.model.BConstants;
import com.qg.smpt.printer.util.exception.DataNotFoundException;
import com.qg.smpt.share.ShareMem;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.SqlSessionFactoryBuild;
import com.qg.smpt.web.model.BulkOrder;
import com.qg.smpt.web.model.Order;
import com.qg.smpt.web.model.Printer;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.repository.OrderMapper;
import com.qg.smpt.web.repository.PrinterMapper;
import com.qg.smpt.web.repository.UserMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by tisong on 3/27/17.
 */
public class DatabaseUtil {

    private final static SqlSessionFactory sqlSessionFactory;

    static {
        String resource = "mybatis/mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
    }


    private static User selectUser(int userId) throws DataNotFoundException{
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuild.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        try {
            User user = userMapper.selectUserPrinter(userId);
            if (user == null) {
                throw new DataNotFoundException("数据库中无该商家 " + userId);
            }
            return user;
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }

    }

    /**
     * 保证所有线程拿到的user对象是唯一的
     * @param userId
     * @return
     * @throws DataNotFoundException
     */
    public static User getSafeUser(int userId) throws DataNotFoundException {
        User user = ShareMem.userIdMap.get(userId) ;
        if (user == null) {
            ShareMem.userIdMap.putIfAbsent(userId, DatabaseUtil.selectUser(userId));
            user = ShareMem.userIdMap.get(userId);
            if (user == null) {
                throw new DataNotFoundException("商家信息未找到" + userId);
            }
        }
        return user;
    }


    public static void initUser(int userId) throws DataNotFoundException {
        User user = getSafeUser(userId);
        synchronized (user) {
            if (user.isInited()) {
                return ;
            }

            Thread thread = new Thread(user.getOrderToPrinter().get());
            thread.start();
            user.setOrderToPrinterThread(thread);
            user.setInited(true);
        }
    }

    public static int getUserId(int printerId) throws DataNotFoundException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuild.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            PrinterMapper printerMapper = sqlSession.getMapper(PrinterMapper.class);
            Integer userId = null;
            if ((userId = printerMapper.selectUserIdByPrinter(printerId)) == null) {
                throw new DataNotFoundException("打印机id无法匹配到商家信息: " + printerId);
            }
            return userId;
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }

    public static Printer getPrinter(int printerId) throws DataNotFoundException {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuild.getSqlSessionFactory();
        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            PrinterMapper printerMapper = sqlSession.getMapper(PrinterMapper.class);
            Printer printer = printerMapper.selectPrinter(printerId);
            if (printer == null) {
                throw new DataNotFoundException("未在数据库中找到打印机: " + printerId);
            }
            return printer;
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }


    public static void insertBulkOrder(BulkOrder bulkOrder) {
        // 将订单保存到数据库
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        try {
            Order o = null;
            String succ = Integer.valueOf(BConstants.orderSucc).toString();
//            for (int i = 0; i < bulkOrder.getbOrders().size(); i++) {
//                o = bulkOrder.getOrders().get(i);
//                if (o.getOrderStatus().equals(succ)) {
//                    orderMapper.insert(o);
//                    orderMapper.insertUserOrder(o);
//                }
//            }
            for (Order order : bulkOrder.getOrders()) {
                if (o.getOrderStatus().equals(succ)) {
                    orderMapper.insert(o);
                    orderMapper.insertUserOrder(o);
                }
            }
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }
}
