 function getCookis(name){
    var cookie_name = encodeURIComponent(name) + "=",
        cookie_start = document.cookie.indexOf(cookie_name),
        cookie_value = null,
        cookie_end = null;
    
    if(cookie_start > -1){
        cookie_end = document.cookie.indexOf(';', cookie_start);
        if(cookie_end == -1){
            cookie_end = document.cookie.length;
        }
        cookie_value = decodeURIComponent(document.cookie.substring(cookie_start + cookie_name.length, cookie_end));
    }
    
    return cookie_value;
}

var id = getCookis('user_id') || window.localStorage.getItem('smart_printer');
if(!parseInt(id)){
    alert('你还没登录');
    window.location.href="../html/user_login.html";

}else{
    $('#search').click(function(event){
        var str = document.getElementById('order_id').value;
        if(str.match(/[0-9]/g)){
            send_order(str);
        }
    });
    function send_order(str){
        $.ajax({
            url: '/order/' + id + '/' + str,
            type: 'get',
            dataType: 'json',
            success: function(data){
                // 成功处理时间
                success(data);
            },
            error: function(data){
                alert('系统出错');
            }
        });
    }
    function success(data){
        var str = "",
            order = data.order;
        if(order instanceof Array){
            for(var i = 0; i < order.length; i++){
                str = str + '<div class="one_status"><h1>订单ID：' + order[i].id +'</h1>' +
                            '<p>是否加急：' + (order[i].orderType === 0?'非加急':'加急') +'</p>' +
                            '<p>所在批次ID：' + order[i].bulkId + '</p>' +
                            '<p>所在批次编号：' + order[i].bulkIndex + '</p>' +
                            '<p>订单发送时间：' + order[i].sendTime + '</p>' +
                            '<p>订单接受时间：' + order[i].acceptTime + '</p>' +
                            '<p>订单进入打印队列时间：' + order[i].enterQueueTime + '</p>' +
                            '<p>订单打印结果时间：' + order[i].startPrintTime + '</p>' +
                            '<p>错误订单重传时间：' + order[i].execSendTime + '</p>' +
                            '<p>错误订单接收时间：' + order[i].execAcceptTime + '</p>' +
                            '<p>错误订单进入打印队列时间：' + order[i].execEnterQueueTime + '</p>' +
                            '<p>错误订单开始打印时间：' + order[i].execStartPrintTime + '</p>' +
                            '<p>错误订单打印结果时间：' + order[i].execPrintResultTime + '</p>' +
                            '</div>';
            }
        }else{
            str = '<div class="one_status"><h1>订单ID：' + order.id +'</h1>' +
                    '<p>是否加急：' + (order.orderType === 0?'非加急':'加急') +'</p>' +
                    '<p>所在批次ID：' + order.bulkId + '</p>' +
                    '<p>所在批次编号：' + order.bulkIndex + '</p>' +
                    '<p>订单发送时间：' + order.sendTime + '</p>' +
                    '<p>订单接受时间：' + order.acceptTime + '</p>' +
                    '<p>订单进入打印队列时间：' + order.enterQueueTime + '</p>' +
                    '<p>订单打印结果时间：' + order.startPrintTime + '</p>' +
                    '<p>错误订单重传时间：' + order.execSendTime + '</p>' +
                    '<p>错误订单接收时间：' + order.execAcceptTime + '</p>' +
                    '<p>错误订单进入打印队列时间：' + order.execEnterQueueTime + '</p>' +
                    '<p>错误订单开始打印时间：' + order.execStartPrintTime + '</p>' +
                    '<p>错误订单打印结果时间：' + order.execPrintResultTime + '</p>' +
                    '</div>';
        }
        $('#order_show').html(str);
    }

}