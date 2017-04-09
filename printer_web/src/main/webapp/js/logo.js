var WIDTH = 128;
var HEIGHT = 128;
var id = getCookis('user_id') || window.localStorage.getItem('smart_printer');
var PATH = '/user/image/' + id;


if(!parseInt(id)){
    alert('你还没登录');
    window.location.href="../html/user_login.html";
}

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
var logo = (function(){
    var oFReader =  null, // 文件流
        type =  /((.png)$)|((.jpg)$)|((.jpeg)$)|((.gif)$)/gi,
        canvas;

    function getFileFullPath() {
        var textEle = document.getElementsByClassName('beautify')[0].getElementsByTagName('p')[0],
            fileEle = document.getElementById('logo_file');
            fileEle.addEventListener('change', function(event) {
                var files = document.getElementById('logo_file').files;
                // 获取图片
                if (files.length === 0) {
                    alert("you don't upload files!");
                    return;
                } else if(oFReader != null){
                    console.log(files[0].name.match(type));
                    if(files[0].name.match(type)){
                        document.getElementById('show_area').getElementsByTagName('p')[0].innerHTML = '你选择的文件是<span>' + files[0].name + '</span>';
                        oFReader = null;
                        initoFR();
                        if(document.getElementById('clip_area')){
                            document.getElementById('show_area').removeChild(document.getElementById('clip_area'));
                        }
                    }else{
                        return ;
                    }
                }
                oFReader.readAsDataURL(files[0]);
            });
        // 文字模拟出发表单提交事件。因为文字挡住了input file的位置，阻止事件的发生。
        textEle.addEventListener('click', function(){
            var event = document.createEvent("MouseEvents");
            event.initMouseEvent('click', true, true, document.defaultView, 0, 0, 0, 0, 0,
                                false, false, false, false, 0, null);
            fileEle.dispatchEvent(event);
        });
    }
    function _handlePicture(canvas){
        var img = new Image(),
            imgURL = canvas.toDataURL("image/png"),
            context = canvas.getContext('2d'),
            iw, ih, multiple, deviation, length;
            // length是按倍数伸缩之后的大小。

        // 获取图片来源
        img.src = oFReader.result;

        img.onload = function(event){
            document.getElementById('save_file').style.visibility = 'visible';
        }

        iw = img.width;
        ih = img.height;

        if( (iw/128) > (ih/120) ){
            multiple = iw / 128;
            length = ih / multiple;
            deviation = (120 - length)/2;
            context.drawImage(img, 0, deviation, 128, length);
        }else{
            multiple = ih / 120;
            length = iw / multiple;
            deviation = (128 - length)/2;
            context.drawImage(img, deviation , 0, length, 120);
        }
    }
    // 第2步：前端获取图片文件放在canvas里面,和创建遮罩去遮盖元素
    function createCanvas(_this) {
        var context;
            canvas = document.createElement('canvas');
            // 裁剪路径
            canvas.id = "clip_area";

        if (canvas.getContext) {
            canvas.width = 128;
            canvas.height = 120;
            
            // 对图片进行处理
            _handlePicture(canvas);
            
            // 将图片插入到canvas里面
            document.getElementById('show_area').appendChild(canvas);
            this.canvas = canvas;

        }
    }
    // 第3步： 导出元素
    function createClip() {
        var strDataURI = canvas.toDataURL('image/jpeg'),
            data = strDataURI.split(',')[1],
            ia,
            form_obj,
            file;
        data = window.atob(data);
        ia = new Uint8Array(data.length);
        for (var i = 0; i < data.length; i++) {
            ia[i] = data.charCodeAt(i);
        };

        form_obj = new FormData(document.getElementById('#upload_form'));
        file = new File([ia] , "foo.jpg", {type:"image/jpeg"})
        form_obj.append('file',file);

        var xhr = new XMLHttpRequest();
        xhr.open('post',PATH, true);
        xhr.onreadystatechange = function () {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                console.log(xhr.responseText);
                alert('success');
            }
        };
        xhr.send(form_obj);
    }
    function initoFR(){
        oFReader = new FileReader();
        // 加载了图片之后执行这个函数
        oFReader.onload = function(event) {
            createCanvas();
        }
    }

    return function() {
        initoFR();
        getFileFullPath();
        document.getElementById('save_file').addEventListener('click', function(event) {
            createClip();
        });
    }
})();
logo();
