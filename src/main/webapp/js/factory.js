function getCookis(name){
    var cookie_name = encodeURIComponent(name) + "=",
        cookie_start = document.cookie.indexOf(cookie_name),
        cookie_value = null,
        cookie_end = null;
    
    if(cookie_start > -1){
        cookie_end = document.cookie.indexOf(';', cookie_start);
        if(cookie_end === -1){
            cookie_end = document.cookie.length;
        }
        cookie_value = decodeURIComponent(document.cookie.substring(cookie_start + cookie_name.length, cookie_end));
    }
    
    return cookie_value;
}
// var id = getCookis('user_id') || window.localStorage.getItem('smart_printer');
// if(!parseInt(id)){
//     alert('你还没登录');
//     window.location.href="../html/user_login.html";
// }

var vm = new Vue({
	el: '#order_factory',
	data: {
	    arr: [
			{
				number : 5,
				size: 1,
				orderType: 0
			}
	    ]
	},
    methods:{
        addItem: function(index){
            this.arr.push({
                number : 0,
                size: 1,
                orderType: 0
            });
        },
        delectItem: function(index){
            if(this.arr.length === 1){
                alert('不能删除最后一个 ^-^');
                return ;
            }
            this.arr.splice(index, 1);
        },
        submit_factory: function(){

            var data_object = vm.$get('arr'),
                data = [],
                id = getCookis('user_id');

            // 把数据弄进去
            for(var i = 0; i < data_object.length; i++){
                data.push({
                    'number': data_object[i].number,
                    'size': data_object[i].size,
                    'orderType': data_object[i].orderType
                });
            }
            data = JSON.stringify(data);
            function success(text) {
                alert('成功');
            }
            function fail(code) {
                alert('失败');
            }
            // $.ajax({
            //     url :'/orders/' + id,
            //     type: 'post',
            //     data: data,
            //     contentType: 'application/json; charset=UTF-8',
            //     dataType: 'json',
            //     success: success,
            //     error: fail
            //
            //     }
            // );
            var request = new XMLHttpRequest(); // 新建XMLHttpRequest对象

            request.onreadystatechange = function () { // 状态发生变化时，函数被回调
                if (request.readyState === 4) { // 成功完成
                    // 判断响应结果:
                    if (request.status === 200) {
                        // 成功，通过responseText拿到响应的文本:
                        return success(request.responseText);
                    } else {
                        // 失败，根据响应码判断失败原因:
                        return fail(request.status);
                    }
                } else {
                    // HTTP请求还在继续...
                }
            }
            // 发送请求:修改下面的路劲的路径 -->
            request.open('POST', '/orders/' + id, true);
            request.setRequestHeader('content-Type','application/json;charset=utf-8');
            request.send(data);
        }
    }
});

	