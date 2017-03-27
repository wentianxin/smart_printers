package com.qg.smpt.share;

import com.qg.smpt.printer.PrinterProcessor;
import com.qg.smpt.web.model.*;
import com.qg.smpt.web.repository.OrderMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.Reader;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 全局生命周期的共享对象
 * // TODO 何时注销这些对象呢?
 */
public final class ShareMem {

	public static Integer currentOrderNum = 0;


    public static ConcurrentHashMap<Integer, User> userIdMap = null;                      // 用户id-用户, 用户登陆, 打印机初次发送请求, 订单初次生成
                                                                                            // 都要向该变量中添加 id - user
    // TODO 编码严重漏洞，共享变量未实现共享
    public static ConcurrentHashMap<Integer, Printer> printerIdMap = null;                // 打印机id-打印机, 只有当打印机发送请求连接时才建立该对象


    static {
        // 初始化订单ID
        initOrderId();

        userIdMap = new ConcurrentHashMap<Integer, User>();

        printerIdMap = new ConcurrentHashMap<Integer, Printer>();
    }


    private static void initOrderId() {
        String resource = "mybatis/mybatis-config.xml";
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader(resource);
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        try {
            currentOrderNum = orderMapper.selectMaxOrderId();
        }finally {
            sqlSession.close();
        }
    }
}
