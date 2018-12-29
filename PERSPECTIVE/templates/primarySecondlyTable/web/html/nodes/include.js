ReferenceTextComp.prototype.beforeOpenRefPage = function() {
	
	var simpleEvent = {
			"obj" : this
		};
	//TODO event改名
	this.doEventFunc("beforeOpenReference", ReferenceTextListener.listenerType, simpleEvent);
	if(this.nodeInfo instanceof SelfRefNodeInfo){
		if(this.nodeInfo.url.indexOf("addressBook")>=0){
			var editwidget = pageUI.getWidget("editpsndoc");
			var dspsndoc = editwidget.getDataset("dspsndoc");
			var selectrow = dspsndoc.getSelectedRow();
			var addr_pk = selectrow.getCellValue(dspsndoc.nameToIndex('addr'));
			if(addr_pk)
				return addr_pk;
			
		}
	}
	return true;	
};
