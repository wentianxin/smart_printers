const WIDTH = 128;
const HEIGHT = 128;
// const PATH = 'user/image/1';
const PATH = 'http://baidu.com'
var logo = {
    oFReader: null, // 文件流
    type: /((.png)$)|((.jpg)$)|((.jpeg)$)|((.gif)$)/gi,
    getFileFullPath: function() {
        var _this = this,
            textEle = document.getElementsByClassName('beautify')[0].getElementsByTagName('p')[0],
            fileEle = document.getElementById('logo_file');
        fileEle.addEventListener('change', function(event) {
            var files = document.getElementById('logo_file').files;
            // 获取图片
            if (files.length === 0) {
                alert("you don't upload files!");
                return;
            } else if(_this.oFReader != null){
                console.log(files[0].name.match(_this.type));
                if(files[0].name.match(_this.type)){
                    document.getElementById('show_area').getElementsByTagName('p')[0].innerHTML = '你选择的文件是<span>' + files[0].name + '</span>';
                    _this.oFReader = null;
                    _this.initoFR(_this);
                    if(document.getElementById('clip_area')){
                        document.getElementById('show_area').removeChild(document.getElementById('clip_area'));
                    }
                }else{
                    return ;
                }
            }
            logo.oFReader.readAsDataURL(files[0]);
        });
        // 文字模拟出发表单提交事件。
        textEle.addEventListener('click', function(){
            var event = document.createEvent("MouseEvents");
            event.initMouseEvent('click', true, true, document.defaultView, 0, 0, 0, 0, 0,
                                false, false, false, false, 0, null);
            fileEle.dispatchEvent(event);
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
        img.onload = function(event){
            document.getElementById('save_file').style.visibility = 'visible';
        }

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
            document.getElementById('show_area').appendChild(canvas);
            _this.canvas = canvas;
        }
    },
    // 第3步： 导出元素
    createClip: function(_this) {
        var strDataURI = _this.canvas.toDataURL('image/png'),
            data = strDataURI.split(',')[1],
            ia,
            blob,
            form_obj,
            file;
        data = window.atob(data);
        ia = new Uint8Array(data.length);
        for (var i = 0; i < data.length; i++) {
            ia[i] = data.charCodeAt(i);
        };

        form_obj = new FormData(document.getElementById('#upload_form'));
        file = new File([ia] , "foo.png", {type:"image/png"})
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
        xhr.upload.onprogress = function (event) {
    　　　　if (event.lengthComputable) {
    　　　　　　var complete = (event.loaded / event.total * 100 | 0);
    　　　　　　var progress = document.getElementById('uploadprogress');
    　　　　　　progress.value = progress.innerHTML = complete;
    　　　　}
    　　};
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
