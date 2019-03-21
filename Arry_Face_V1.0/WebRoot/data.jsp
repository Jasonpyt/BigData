<%@ page language="java" import="java.util.*,com.itstar.action.*" pageEncoding="utf-8"%>
<%
	String imgBase64 = request.getParameter("imgBase64");
	boolean bol = FaceQueryUtil.getFaceInfo(imgBase64);
	out.print(bol);
%>