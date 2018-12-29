<?xml version="1.0" encoding="UTF-8"?>
<page template="adminonerow" version="1"  isdefault="false" skin="webclassic" level="${level}"  linkgroup="${menuCategory}"  ordernum="15">
	<title>${menuGroupName}</title>
	<layout id="l1" name="simpleLayout" sizes="100%">
		<layout id="l2" name="paddingLayout" sizes="100%">
			<portlet id="p3" name="pserver:NavigationPortlet" theme="clean" i18nname="admin-00002" title="导航条" column="0" />
			<portlet id="p2" name="pmng:MgrContentPortlet" theme="defaultround" i18nname="admin-00003"  title="管理内容" column="0" />
		</layout>
		<layout id="l3" name="simpleLayout" sizes="100%">
			<portlet id="p4" name="pserver:CopyRightPortlet" theme="clean" i18nname="admin-00004"  title="版权" column="0" />
		</layout>
		
		<portlet id="p1" name="pmng:GrpMgrPortlet" theme="float" title="基础配置管理" i18nname="admin-00005" column="0" />
	</layout>
</page>
