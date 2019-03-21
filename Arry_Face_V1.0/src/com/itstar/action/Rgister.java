package com.itstar.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itstar.util.DbUtil;


@WebServlet("/register")
public class Rgister extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String UserBase = request.getParameter("imgBase64");
		String sql = "insert into user(base64) values(?)";
		int update = DbUtil.update(sql, UserBase);
		if (update>0) {
			request.getSession().setAttribute("message", "注册成功!");
		}else {
			request.getSession().setAttribute("message", "你长得太丑了，计算机不想把你的丑脸保存起来！");
		}
		response.sendRedirect("face.jsp");
	}
	
}
