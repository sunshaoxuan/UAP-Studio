<?xml version="1.0" encoding='UTF-8'?>
<Application TagName="Application" caption="${application_caption}" controllerClazz="${application_Controller}" defaultWindowId="${winId}" id="${appId}" sourcePackage="${location}">
    <PageMetas>
        <PageMeta caption="${winCaption}" id="${winId}">
        </PageMeta>
         <PageMeta caption="${winId}_edit" id="${winId}_edit">
        </PageMeta>
    </PageMetas>
    <Connectors>
        <Connector pluginId="editin" plugoutId="editout" source="main" sourceWindow="${winId}_edit" target="main" targetWindow="${winId}">
            <Maps>
                <Map>
                </Map>
            </Maps>
        </Connector>
    </Connectors>
    
</Application>