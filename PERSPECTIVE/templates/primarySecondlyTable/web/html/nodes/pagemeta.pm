<?xml version="1.0" encoding='UTF-8'?>
<PageMeta i18nName="w_cp_psndoc-000093" langDir="cpb_nodes" caption="人员档案" controllerClazz="nc.uap.cpb.psn.PsnMainCtr" id="cp_psndoc" sourcePackage="cpb/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="modeorg" refId="../pubview_modeConfigOrg">
            <Attributes>
                <Attribute>
                    <Key>pk_fun</Key>
                    <Value>0001z01000000base204</Value>
                    <Desc>
                    </Desc>
                </Attribute>
                <Attribute>
                    <Key>global</Key>
                    <Value>false</Value>
                    <Desc>
                    </Desc>
                </Attribute>
                <Attribute>
                    <Key>needGroup</Key>
                    <Value>false</Value>
                    <Desc>
                    </Desc>
                </Attribute>
                <Attribute>
                    <Key>filterSecurity</Key>
                    <Value>true</Value>
                    <Desc>
                    </Desc>
                </Attribute>
            </Attributes>
        </Widget>
        <Widget id="simplequery" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
        <Widget id="dept" refId="dept">
        </Widget>
        <Widget id="editjob" refId="editjob">
        </Widget>
        <Widget id="editpsndoc" refId="editpsndoc">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$QueryTemplate</Key>
            <Value>
            </Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <Events>
        <Event async="true" jsEventClaszz="nc.uap.lfw.core.event.conf.PageListener" methodName="sysWindowClosed" name="onClosed" onserver="true">
            <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
            </SubmitRule>
            <Params>
                <Param>
                    <Name>event</Name>
                    <Value>
                    </Value>
                    <Desc>                        <![CDATA[nc.uap.lfw.core.event.PageEvent]]>
                    </Desc>
                </Param>
            </Params>
            <Action>
            </Action>
        </Event>
    </Events>
    <Connectors>
        <Connector id="deptchangeplugin" pluginId="dept_plugin" plugoutId="deptselect_plugout" source="dept" target="main">
        </Connector>
        <Connector id="modeorgplugin" pluginId="modeorg_pluginin" plugoutId="orgout" source="modeorg" target="dept">
            <Maps>
                <Map inValue="orgValue" outValue="orgValue">
                    <outValue>orgValue</outValue>
                    <inValue>orgValue</inValue>
                </Map>
            </Maps>
        </Connector>
        <Connector id="editjobplugin" pluginId="edit_job_plugin" plugoutId="edit_psndoc_plugout" source="editjob" target="main">
        </Connector>
        <Connector id="psnjobplugin" pluginId="edit_psn_plugin" plugoutId="edit_plugout" source="editpsndoc" target="main">
        </Connector>
        <Connector id="simpleQuery_to_main" pluginId="simpleQuery_plugin" plugoutId="qryout" source="simplequery" target="main">
            <Maps>
                <Map inValue="row" outValue="">
                    <outValue>
                    </outValue>
                    <inValue>row</inValue>
                </Map>
            </Maps>
        </Connector>
    </Connectors>
</PageMeta>