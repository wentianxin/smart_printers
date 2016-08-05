const WIDTH = 128;
const HEIGHT = 128;
const PATH = 'http://localhost:8080/user/image/1';
var logo = {
    oFReader: null, // 文件流
    getFileFullPath: function() {
        var _this = this;
        $('#logo_file').bind('change',function() {
            var files = document.getElementById('logo_file').files;
            // 获取图片
            if (files.length === 0) {
                alert("you don't upload files!");
                return;
            } else if(document.getElementsByTagName('canvas').length > 0){
                _this.oFReader = null;
                _this.initoFR(_this);
                document.getElementById('logo_clip_area').removeChild(document.getElementById('clip_area'));

            }
            logo.oFReader.readAsDataURL(files[0]);
            $('#save_file').css({'visibility': 'visible'});
        });
    },
    _handlePicture: function(_this, canvas){
        var img = new Image(),
            imgURL = canvas.toDataURL("image/png"),
            context = canvas.getContext('2d'),
            iw, ih, multiple, deviation, length;
            // length是按倍数伸缩之后的大小。

        // 获取图片来源
        img.src = _this.oFReader.result;
        console.log('img width: '+ img.width);
        console.log('img height: '+ img.height);

        iw = img.width;
        ih = img.height;

        if(iw > ih){
            multiple = iw / 128;
            length = ih/multiple;
            deviation = (128 - length)/2;
            context.drawImage(img, 0, deviation, 128, length);
        }else{
            multiple = ih / 128;
            length = iw/multiple;
            deviation = (128 - length)/2;
            context.drawImage(img, deviation , 0, length, 128);
        }
    },
    // 第2步：前端获取图片文件放在canvas里面,和创建遮罩去遮盖元素
    createCanvas: function(_this) {
        var context,
            canvas = document.createElement('canvas');
            // 裁剪路径
            canvas.id = "clip_area";

        if (canvas.getContext) {
            canvas.width = 128;
            canvas.height = 128;
            
            // 对图片进行处理
            _this._handlePicture(_this, canvas);
            
            // 将图片插入到canvas里面
            document.getElementById('logo_clip_area').appendChild(canvas);
            _this.canvas = canvas;
        }
    },
    // 第3步： 导出元素
    createClip: function(_this) {
        var strDataURI = _this.canvas.toDataURL('image/jpeg'),
            data = strDataURI.split(',')[1],
            ia,
            // blob,
            form_obj,
            file;
        data = window.atob(data);
        ia = new Uint8Array(data.length);
        for (var i = 0; i < data.length; i++) {
            ia[i] = data.charCodeAt(i);
        };

        // blob = new Blob([ia], { type: "image/png" });
        form_obj = new FormData(document.getElementById('#upload_form'));
        file = new File([ia] , "foo.jpg", {type:"image/jpeg"})
        form_obj.append('file',file);

        // $.ajax({
        //     data: form_obj,
        //     url: 'user/image/1',
        //     dataType: 'json',
        //     processData: false,  // 告诉jQuery不要去处理发送的数据
        //     contentType: "multipart/form-data",   // 告诉jQuery不要去设置Content-Type请求头
        //     type: 'post',
        //     success: function(data) {
        //         alert('success');
        //     },
        //     error: function(data) {
        //         alert('error');
        //     }

        // });
        var xhr = new XMLHttpRequest();
        xhr.open('post',PATH, true);
        xhr.onreadystatechange = function () {
            if(xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
                console.log(xhr.responseText);
                alert('success');
            }
        };
        xhr.send(form_obj);
    },
    initoFR: function(_this){
        _this.oFReader = new FileReader();
        // 加载了图片之后执行这个函数
        _this.oFReader.onload = function(event) {
            _this.createCanvas(_this);

        }
    },
    init: function() {
        var _this = this;
        this.initoFR(_this);
        this.getFileFullPath();
        document.getElementById('save_file').addEventListener('click', function(event) {
            logo.createClip(_this);
        });

    }
}
logo.init();
