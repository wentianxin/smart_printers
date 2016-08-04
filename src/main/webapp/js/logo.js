const WIDTH = 128;
const HEIGHT = 128;
var logo = {
    oFReader: null, // 文件流
    canvas: null, // canvas对象
    form: null, // 获取图片表单的对象
    left: 0,
    top: 0,
    strDataURI: 0,
    getFileFullPath: function() {
        $('#logo_upload_button').click(function() {
            var files = document.getElementById('logo_file').files;
            // 获取图片
            if (files.length === 0) {
                alert("you don't upload files!");
                return;
            } else {
                logo.oFReader.readAsDataURL(files[0]);
            }
        });
    },
    _getEleLeft: function(element) {　　　　
        var actualLeft = element.offsetLeft;　　　　
        var current = element.offsetParent;　　　　
        while (current !== null) {　　　　　　 actualLeft += current.offsetLeft;　　　　　　
            current = current.offsetParent;　　　　 }　　　　
        return actualLeft;　　 },
    _getEleTop: function(element) {　　　　
        var actualTop = element.offsetTop;　　　　
        var current = element.offsetParent;　　　　
        while (current !== null) {　　　　　　 actualTop += current.offsetTop;　　　　　　
            current = current.offsetParent;　　　　 }　　　　
        return actualTop;　　 },
    // 第2步：前端获取图片文件放在canvas里面,和创建遮罩去遮盖元素
    createCanvas: function(_this) {
        var imgURL,
            img,
            canvas = _this.canvas,
            context,
            drag,
            left,
            top,
            canvas = document.createElement('canvas');
        canvas.id = "clip_area";
        var c = document.createElement('canvas');
        var area = document.getElementById('logo_clip_area');
        if (canvas.getContext) {
            imgURL = canvas.toDataURL("image/png");
            img = new Image();
            // 获取图片来源
            img.src = _this.oFReader.result;
            canvas.width = img.width;
            canvas.height = img.height;
            context = canvas.getContext('2d');
            // 将图片插入到canvas里面
            context.drawImage(img, 0, 0);

            area.addEventListener('dragover', function(event) {
                // 允许元素放置
                event.preventDefault();
            });
            area.addEventListener('drop', function(event) {
                var target = event.target;
                event.preventDefault();
                event.stopPropagation(); // 兼容ff
                // debugger;
                _this.left = drag.style.left = (event.pageX - left - document.getElementById('logo_clip_area').offsetLeft) + 'px';
                _this.top = drag.style.top = (event.pageY - top - document.getElementById('logo_clip_area').offsetTop) + 'px';
                ctx.drawImage(img, parseInt(logo.left), parseInt(logo.top), WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT); //重绘
            });

            drag = document.createElement('div');
            drag.id = "canvas_clip_area";
            drag.draggable = "true";
            // div拖动事件,拖动什么
            drag.addEventListener('drag', function(event) {
                var target = event.target;

                var dt = event.dataTransfer;
                dt.dropAllowed = 'move';
                dt.dropEffect = 'move';
                ctx.drawImage(img, parseInt(logo.left), parseInt(logo.top), WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT); //重绘
            });
            drag.addEventListener('dragstart', function(event) {
                // 允许元素放置
                // event.preventDefault();
                left = event.pageX - _this._getEleLeft(event.target);
                top = event.pageY - _this._getEleTop(event.target);
            });
            // div 放到何处
            drag.addEventListener('dragover', function(event) {
                // 允许元素放置
                event.preventDefault();
            });
            drag.addEventListener('dragenter', function(event) {
                // 允许元素放置
                event.preventDefault();
            });
            // 用户鼠标移除之前调用了dragenter的元素时，浏览器会触发dragleave事件
            drag.addEventListener('dragleave', function(event) {

            });
            c.width = 300;
            c.height = 300;
            c.id = "show_area";
            ctx = c.getContext('2d');
            ctx.drawImage(img, parseInt(logo.left), parseInt(logo.top), WIDTH, HEIGHT, 0, 0, WIDTH, HEIGHT); //重绘

            document.getElementById('logo_clip_area').appendChild(canvas);
            document.getElementById('logo_clip_area').appendChild(drag);
            document.getElementById('logo_clip_area').appendChild(c);
        }
    },
    // 第3步： 导出元素
    createClip: function() {
        var canvas = document.getElementById('show_area');
        logo.strDataURI = canvas.toDataURL('image/png');
        var data = logo.strDataURI.split(',')[1];
        data = window.atob(data);
        var ia = new Uint8Array(data.length);
        for (var i = 0; i < data.length; i++) {
            ia[i] = data.charCodeAt(i);
        };
        var blob = new Blob([ia], { type: "image/png" });
        var form_obj = new FormData();
        form_obj.append('file',blob);

        $.ajax({
            data: form_obj,
            url: 'user/image/1',
            dataType: 'json',
            processData: false,
            contentType: 'application/octet-stream',
            type: 'post',
            success: function(data) {
                alert('success');
            },
            error: function(data) {
                alert('error');
            }

        });
    },
    init: function() {
        var _this = this;
        this.oFReader = new FileReader();
        // 加载了图片之后执行这个函数
        this.oFReader.onload = function(event) {
            _this.createCanvas(_this);

        }
        this.getFileFullPath();
        document.getElementById('save_file').addEventListener('click', function(event) {
            logo.createClip();
        });

    }
}
logo.init();
