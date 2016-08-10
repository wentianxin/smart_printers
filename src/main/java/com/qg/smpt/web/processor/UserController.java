package com.qg.smpt.web.processor;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.qg.smpt.util.JsonUtil;
import com.qg.smpt.util.Level;
import com.qg.smpt.util.Logger;
import com.qg.smpt.web.model.Constant;
import com.qg.smpt.web.model.User;
import com.qg.smpt.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Controller
@RequestMapping("user")
public class UserController {
	private static Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private UserService userService;

	@RequestMapping(value="image/{userId}", method=RequestMethod.POST, produces="application/json;charset=utf-8")
    @ResponseBody
	public String springUpload(@PathVariable int userId, HttpServletRequest request) throws IllegalStateException, IOException {
        LOGGER.log(Level.DEBUG,"正在上传logo图片");

		String status = Constant.ERROR;
		int retcode = Constant.FALSE;

		// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				request.getSession().getServletContext());

		// 检查form中是否有enctype="multipart/form-data"
		if (multipartResolver.isMultipart(request)) {

			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			// 获取multiRequest 中所有的文件名
			Iterator iter = multiRequest.getFileNames();

			while (iter.hasNext()) {
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(iter.next().toString());
				if (file != null) {
					String path = request.getServletContext().getRealPath("/") + "WEB-INF"
                            + File.separator + "image";

					LOGGER.log(Level.DEBUG, "正在查找图片保存文件夹 [{0}]" ,path);

                    File catalog = new File(path);
                    if(!catalog.exists()) {
                        LOGGER.log(Level.DEBUG, "文件夹不存在，正在试图创建文件夹");
                        boolean isCreated = catalog.mkdir();
                        LOGGER.log(Level.DEBUG, "文件夹创建结果为[{0}]", isCreated);
                    }

                    String originalFileName = file.getOriginalFilename();
                    String suffix = originalFileName.lastIndexOf(".") != -1 ? originalFileName.substring(originalFileName.lastIndexOf(".")) : "";
                    String filename = "" + userId + UUID.randomUUID() + suffix;
                    path = path + File.separator + filename;
                    LOGGER.log(Level.DEBUG, "图片文件路径为 [{0}]" ,path);

                    // 执行上传
					try {
						file.transferTo(new File(path));
					}catch (IOException e) {
						LOGGER.log(Level.ERROR, "上传文件失败");
						return JsonUtil.jsonToMap(new String[]{"retcode","status"}, new Object[]{retcode,status});
					}

                    // 将数据保存到数据库
                    try {
                        status = userService.updateLogo(path.substring(path.indexOf("image")), userId);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
					retcode = status.equals(Constant.SUCCESS) ? Constant.TRUE : Constant.FALSE;
                }
			}
		}
        return JsonUtil.jsonToMap(new String[]{"retcode","status"}, new Object[]{retcode,status});
    }

}
