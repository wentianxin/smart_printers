

$('#logo_upload').click(function(){
	var files = document.getElementById('logo_file').files;
	if(files.length === 0){
		alert("you don't upload files!");
		return ;
	}else{
         	// 上传表单
     		$.ajax({
			url: 'user/image/1',
			type: 'post',
			cache: false,
			data: new FormData($('#upload_form')[0]),
			contentType: false,
			processData: false,
			dataType: 'json',
			success: function(date){
				// clip(data);
				alert('success');
			},
			error: function(date){
				alert('error');
			}
		});
	}
});

// function clip(data){
// 	// 第一步：读取到图片的资源，然后在去加载他
// 	var src="file:///C:/Users/st/Pictures/QQ%E5%9B%BE%E7%89%8720160703193602.jpg";
// 	var ele = new Image();
//     var oEndBtn = document.getElementById("clip_sure");
    
//     oEndBtn.addEventListener('click',function(){
//         var x = document.getElementById("cropPosX").value, y = document.getElementById("cropPosY").value, w = document.getElementById("cropImageWidth").value, h = document.getElementById("cropImageHeight").value, angle = document.getElementById("zxxRotAngle").value;
//         if(angle === ""){
//             angle = 0;    
//         }
//         document.getElementById('test').style.backgroundPosition = x + "px  "+ y + "px";
//         alert("角度："+angle+"\n剪裁横坐标："+x+"\n剪裁纵坐标："+y+"\n剪裁宽度："+w+"\n剪裁高度："+h);
//     });

// 	ele.src = src;
// 	ele.id="clip_area";
// 	document.getElementById('clip_picture').insertBefore(ele, document.getElementById('clip_picture').firstChild);
// 	// 判定时间
// 	window.onload = function(){
// 	    document.getElementById("cropBeginBtn").onclick = function(){
// 	        fnImageCropRot(ele);
// 	        oEndBtn.style.display = "inline-block";
// 	    };    
// 	}
// }
  // jQuery(function($){

  //   // Create variables (in this scope) to hold the API and image size
  //   var jcrop_api,
  //       boundx,
  //       boundy,

  //       // Grab some information about the preview pane
  //       $preview = $('#preview-pane'),
  //       $pcnt = $('#preview-pane .preview-container'),
  //       $pimg = $('#preview-pane .preview-container img'),

  //       xsize = $pcnt.width(),
  //       ysize = $pcnt.height();
    
  //   console.log('init',[xsize,ysize]);
  //   $('#target').Jcrop({
  //     onChange: updatePreview,
  //     onSelect: updatePreview,
  //     aspectRatio: xsize / ysize
  //   },function(){
  //     // Use the API to get the real image size
  //     var bounds = this.getBounds();
  //     boundx = bounds[0];
  //     boundy = bounds[1];
  //     // Store the API in the jcrop_api variable
  //     jcrop_api = this;

  //     // Move the preview into the jcrop container for css positioning
  //     $preview.appendTo(jcrop_api.ui.holder);
  //   });

  //   function updatePreview(c)
  //   {
  //     if (parseInt(c.w) > 0)
  //     {
  //       var rx = xsize / c.w;
  //       var ry = ysize / c.h;

  //       $pimg.css({
  //         width: Math.round(rx * boundx) + 'px',
  //         height: Math.round(ry * boundy) + 'px',
  //         marginLeft: '-' + Math.round(rx * c.x) + 'px',
  //         marginTop: '-' + Math.round(ry * c.y) + 'px'
  //       });
  //     }
  //   };

  // });
