new Vue({
	el: '#order_factory',
	data: {
	    arr: [
			{
				count : 5,
				length: 1,
				status: "false"
			},
            {
                count : 5,
                length: 1,
                status: "false"
            }
	    ]
	}
});
function submit_factory(){
	var formData = new FormData(document.getElementById('order_factory'));
	// $.ajax({
	// 	url: 'xxx.php',
	// 	type: 'post',
	// 	data: formData,
	// 	success: function(data){
 //            debugger;
 //            console.log('chenggong');
	// 	}
	// });
    function success(text) {
        console.log('cg');
    }

    function fail(code) {
        console.log('shibai');
    }

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
    request.open('POST', '/api/categories');
    request.send(formData);
}