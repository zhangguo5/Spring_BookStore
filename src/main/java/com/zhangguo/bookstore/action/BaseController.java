package com.zhangguo.bookstore.action;

import java.io.IOException;
import java.lang.reflect.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet基类
 * 自定义控制器基类
 */
public class BaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

		// 获得要执行的方法名
		String act = request.getParameter("act");

		// 如果用户没有提供方法名
		if (act == null || act.equals("")) {
			// 默认方法
			act = "execute";
		}

		// 根据方法名获得方法信息获得方法信息
		Method method;
		try {
			// 在对象中获得类型信息,在类型中获得方法通过方法名，与参数类型
			method = this.getClass().getMethod(act, HttpServletRequest.class, HttpServletResponse.class);
			// 调用方法,在当前对象中调用，传递参数request与response,获得返回结果
			String targetUri = method.invoke(this, request, response) + "";
			// 如果返回的url是以redirect开始，则是重定向
			if (targetUri.startsWith("redirect:")) {
				response.sendRedirect(targetUri.substring(9, targetUri.length()));
			} else {
				// 转发
				request.getRequestDispatcher(targetUri).forward(request, response);
			}
		} catch (Exception e) {
			response.sendError(400, e.getMessage());
			e.printStackTrace();
		}
	}

	public void execute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendError(400, "请使用参数act指定您要访问的方法");
	}
}
