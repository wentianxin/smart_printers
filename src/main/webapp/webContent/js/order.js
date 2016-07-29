var ORDER_TYPING = 'http://localhost:8080/smart_printers/orders/typing?userId=1';  // 正在打印的订单路径接口
var PRINTER_TYPING = 'http://localhost:8080/smart_printers/printer?userId=1';// 打印机状态接口
var ORDER_TYPED = 'http://localhost:8080/smart_printers/orders/typed?userId=1'; // 打印完的订单路径接口
// var PRINTER_TYPING = 'printer'; // 打印机状态接口
// var ORDER_TYPED = 'orders/typed'; // 打印完的订单路径接口

// 1. 封装一个Ajax使用对象
function Ajax() {}

Ajax.prototype._createXhr = function() {
    // 创建一个XMLHttpRequest对象
    return new XMLHttpRequest();
};
Ajax.prototype._handleData = function(_this, text) {
    if (_this.dataType === 'json') {
        _this.success(JSON.parse(text));
    }

};
Ajax.prototype._addUrl = function(_this) {
    var data = _this.data;
    var url = _this.url;
    if (url.indexOf('?') === -1) {
        url = url + '?';
        for (var name in data) {
            url = url + window.encodeURI(name) + '=' + window.encodeURI(data[name]) + '&';
        }
        return url.slice(0, url.length - 1);
    } else {
        for (var name in data) {
            url = url + '&' + window.encodeURI(name) + '=' + window.encodeURI(data[name]);
        }
        return url;
    }
};
Ajax.prototype.get = function() {
    var _this = this;

    _this.url = _this._addUrl(_this);
    _this.xhr.open(_this.type, _this.url, true);
    _this.xhr.send(null);
};
Ajax.prototype.post = function() {
    var _this = this;
    var data = null;

    this.xhr.open(_this.type, _this.url, true);

    if (typeof this.contentType === 'undefined') {

    } else if (this.contentType === 'json') {
        data = JSON.stringify(_this.data);
    }
    this.xhr.send(data);
};

Ajax.prototype.init = function(data) {
    var xhr = null;
    this.url = data.url;
    this.type = data.type;
    this.data = data.data;
    this.contentType = data.contentType;
    this.dataType = data.dataType;
    this.success = data.success;
    xhr = this.xhr = this._createXhr();
    var _this = this;
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
            if (xhr.status >= 200 && xhr.status < 300 || xhr.status === 304) {
                _this._handleData(_this, xhr.responseText);
            }
        }
    }
}

var main = {
    choice: ORDER_TYPING,
    renderOrder: function(datas) {
        var html = template('order_template', datas);
        var str = '<div class="order head">' + '<p class="order_number">订单编号</p>' + '<p class="order_status">状态</p>' + '</div>';
        var _this = this;
        document.getElementsByClassName('typing_up_main')[0].innerHTML = str + html;
       // tets = setTimeout('main.call()', 5000);
    },
    renderPrinter: function(datas) {
        var html = template('printer_template', datas);
        document.getElementsByClassName('sb_printer')[0].innerHTML = html;
       // tets2 = setTimeout('printf()', 5000);
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
                main.renderOrder(data);
            },
            error: function(data) {
                console.info("[system error]ajax发送错误");
            }
        });
    }
};
template.config("escape", true);
template.helper('ptstatus', function(data) {
    var format = '';
    switch (data) {
        case 100:
            format = ' printer_healthy';
            break;
        case 110:
            format = ' printer_abnormal';
            break;
        case 120:
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
    var str = data;
   if(str.toString().indexOf('110') === -1){
    format = '';
   }else{
    format = 'od_abnormal';
   }
    return format;
});

template.helper('ptstatusFormat', function(data) {
    var format = '';
    switch (data) {
        case 100:
            format = ' 健康';
            break;
        case 110:
            format = '';
            break;
        case 120:
            format = '亚健康';
            break;
        default:
            console.info('other status');
            break;
    }
    return format;
});
template.helper('odstatusFormat', function(data, format) {
    switch (data) {
        case 100:
            {
                format = '打印完';
                break;
            }
        case 120:
            {
                format = '正在打印';
                break;
            }
        case 130:
            {
                format = '未打印';
                break;
            }
        default:
            console.info('other status');
            break;
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
})();

function printf() {
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
main.call();
printf();
