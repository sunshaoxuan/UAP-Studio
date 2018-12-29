<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<#--系统渲染组件 可继承或者重写-->
<#import env().comm+"/render.ftl" as render>

<#--系统渲染组件 可继承或者重写-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="${href("/css.css")}" rel="stylesheet" type="text/css">
<script src="${href("/js/jquery-1.4.2.min.js")}"></script>
<script src="${href("/js/portal_util.js")}"></script>
<script src="${href("/js/popbaseball.js")}"></script>
<script src="${href("/js/portal.js")}"></script>
<link rel="stylesheet" href="${href("/css/ui-lightness/jquery-ui-1.8.7.custom.css")}">
<script src="${href("/js/jquery-ui-1.8.7.custom.min.js")}"></script>
<#--引入常量-->
 <@render.importConstant constants=ptconstants/>
<title>${env().title}</title>
</head>
<body>

     <!--导航开始-->
    <div>
		<#import env().comm + "/navigation.ftl" as nav>
		<@nav.menu pages=userPages currPage=page></@nav.menu>
    </div>
    <!--导航结束-->
    
    <!--布局开始-->
    <div>
		<@render.layout pageLayout=page.layout page=page/>
    </div>
    <!--布局结束-->
    
    <!--页脚开始-->
    <div tp="foot">
		<#include env().comm + "/foot.ftl">
    </div>
    <!--页脚结束-->
    <!--Portal工具条-->
	<#include env().comm + "/tipspanel.ftl">
	<!-- pserver -->
</body>
</html>