package com.qg.smpt.receive;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 接收外卖订单数据的Servlet
 * Created by tisong on 7/21/16.
 */
@WebServlet(name = "orderservlet")
public class ReceOrderServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /**
     * 将订单数据写入(保存到)打印机-缓存队列（加读写锁写入避免线程竞争）
     * @param request
     */
    private void saveToBuffQueue(HttpServletRequest request) {

    }
}
