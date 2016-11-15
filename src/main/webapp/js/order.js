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

console.log(id);
if(!parseInt(id)){
    alert('你还没登录');
    window.location.href="../html/user_login.html";
}

var ORDER_TYPING = '/order/typing/' + id;  // 正在打印的订单路径接口
var PRINTER_TYPING = '/printer/' + id;// 打印机状态接口
var ORDER_TYPED = '/order/typed/' + id; // 打印完的订单路径接口
// var PRINTER_TYPING = 'printer'; // 打印机状态接口
// var ORDER_TYPED = 'orders/typed'; // 打印完的订单路径接口


/*正式开发要把这个放在一个配置文件那里*/
var ptst =  {
    1: '切刀错误',
    2: '机盒打开',
    3: '纸将用尽',
    4: '正在进纸',
    5: '机芯高温',
    6: '正常状态',
    7: '待定',
    8: '待定',
    9: '待定',
    10: '待定',
    11: '待定',
    12: '普通缓冲区满',
    13: '紧急缓冲区满',
    14: '健康状态',
    15: '亚健康状态',
    16: '异常状态'
};
var odst =  {
    0: '打印成功',
    1: '打印失败',
    2: '进入打印队列',
    3: '开始打印',
    4: '数据错误',
    5: '打印成功-之前的异常订单',
    6: '异常单打印出错',
    7: '异常单进入打印队列',
    8: '异常单开始打印',
    9: '异常单数据错误',
    10: '订单准备发送到打印机',
    11: '订单已发放到打印机'
};
var main = {
    choice: ORDER_TYPING,
    renderOrder: function(datas) {
        var html = template('order_template', datas);
        var str = '<div class="order head">' + '<p class="order_number">订单编号</p>' + '<p class="order_status">状态</p>' + '</div>';
        var _this = this;
        document.getElementsByClassName('typing_up_main')[0].innerHTML = str + html;
        // tets = setTimeout('main.call()', 1000 * 15);
    },
    renderPrinter: function(datas) {
        var html = template('printer_template', datas);
        document.getElementsByClassName('sb_printer')[0].innerHTML = html;
        // tets2 = setTimeout('main.printf()', 1000 * 15);

    },
    changeChoice: function(str) {
        console.info(str);
        if (str !== main.choice) {
            if (str === ORDER_TYPING) {
                this.choice = ORDER_TYPING;
            } else {
                this.choice = ORDER_TYPED;
            }
            this.call();
        }
    },
    call: function() {
        var _this = this;
        $.ajax({
            url: _this.choice,
            type: 'get',
            data: '',
            contentType: 'json',
            dataType: 'json',
            success: function(data) {
                console.log(data);
                if(data.data === "[]"){
                    data.data = [];
                }
                main.renderOrder(data);
            },
            error: function(data) {
                console.info("[system error]ajax发送错误");
            }
        });
    },
    printf: function(){
        $.ajax({
            url: PRINTER_TYPING,
            type: 'get',
            data: '',
            contentType: 'json',
            dataType: 'json',
            success: function(data) {
                main.renderPrinter(data);
            },
            error: function(data) {
                console.info("[system error]ajax发送错误");
            }
        });
    }
};
/* 转换打印机状态码*/
template.helper('ptstatus', function(data) {
    var format = 'printer_healthy';
    switch (data) {
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
        case 14:
            format = ' printer_healthy';
            break;
        case 1:
        case 2:
        case 5:
            format = ' printer_abnormal';
            break;
        case 3:
        case 4:
        case 12:
        case 13:
        case 15:
            format = ' printer_subhealthy';
            break;
        default:
            console.info('other status');
            break;
    }
    return format;
});
template.helper('odstatus', function(data) {
    var format = '';
    switch (data) {
        case 0:
        case 2:
        case 3:
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
        case 10:
        case 11:
            format = '';
            break;
        case 1:
        case 4:
            format = 'od_abnormal';
            break;
        default:
            console.info('other status');
            break;
    }
    return format;
});
template.helper('ptstatusFormat', function(data) {
    var format = '系统错误';
    if(data > 0 && data < 16){
        format = ptst[data];
    }
    return format;
});
template.helper('odstatusFormat', function(data, format) {
    if(data >= 0 && data < 12){
        format = odst[data];
    }else{
        format = '系统异常';
    }
    return format;
});

(function() {
    $('#ab_od_typing').click(function() {
        main.changeChoice(ORDER_TYPING);
    });
    $('#ab_od_typed').click(function() {
        main.changeChoice(ORDER_TYPED);
    });
    main.call();
    main.printf();
})();
