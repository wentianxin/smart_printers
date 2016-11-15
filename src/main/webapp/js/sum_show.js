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

// 这里获取的id是打印机的id
var search = window.location.search;
var id = search.slice(search.indexOf('=') +1 );
search = null;

    $.ajax({
        url: '/printer/status/' + id,
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

    function success(data){
        var str = "",
            printer = data.printer;
        if(printer instanceof Array){
            for(var i = 0; i < printer.length; i++){
                str = str + '<div class="one_status"><h1>打印机ID：' + printer[i].id +'</h1>' +
                            '<p>订单数量：' + printer[i].orderNum +'</p>' +
                            '<p>已发送订单数量：' + printer[i].sendedOrderNum + '</p>' +
                            '<p>未发送订单数量：' + printer[i].unsendedOrderNum + '</p>' +
                            '<p>打印成功数量：' + printer[i].printSuccessNum + '</p>' +
                            '<p>打印出错数量：' + printer[i].printErrorNum + '</p>' +
                            '<p>重打印率：' + printer[i].successRate + '</p></div>';
            }
        }else{
            str = '<div class="one_status"><h1>打印机ID：' + printer.id +'</h1>' +
                    '<p>订单数量：' + printer.orderNum +'</p>' +
                    '<p>已发送订单数量：' + printer.sendedOrderNum + '</p>' +
                    '<p>未发送订单数量：' + printer.unsendedOrderNum + '</p>' +
                    '<p>打印成功数量：' + printer.printSuccessNum + '</p>' +
                    '<p>打印出错数量：' + printer.printErrorNum + '</p>' +
                    '<p>重打印率：' + printer.successRate + '</p></div>';
        }
        $('#order_sum_show').html(str);
    }

    $('#clear_resord').click(function(event){
        $.ajax({
            url: '/printer/'+ id,
            method: 'delete',
            dataType: 'json',
            success: function(data){
                if(data.status.toLocaleUpperCase() === 'success'){
                    $.ajax({
                        url: '/printer/status/' + id,
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
                }else{
                    alert('清除失败');
                }
            },
            error: function(data){

            }
        });
    });
