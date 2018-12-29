<#--引入render宏-->
<#import env().comm + "/render.ftl" as render>
<#--
包装两列的Layout宏
@param layout 
@page  page
-->
<#macro wrap layout page>
<#--指定layout的ID及类型-->
<div id="${layout.id}" tp="layout" style="width:780px; float:left;" pid="${layout.id}">
<#--
drop：元素是否可以拖放到此处
column：列号
-->
<div tp="layout" column="0" style="width:380px; float:left">
<#--将子元素放置到一个合适的位置-->
<#if layout.child[0]??>
<@render.childRender child=layout.child[0] page =page></@render.childRender>
</#if>
<#--放置完毕-->
</div>
<div style="width:20px; float:left">---------------华丽的分割线----------------</div>
<div tp="layout" column="1" style="width:380px; float:left">
<#--将子元素放置到一个合适的位置-->
<#if layout.child[1]??>
<@render.childRender child=layout.child[1] page =page></@render.childRender>
</#if>
<#--放置完毕-->
</div>
</div>
</#macro>