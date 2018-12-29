package uap.lfw.smartmw.mw;


/**
 * 
 * 中间件配置类
 *  
 */
public interface IMiddleProperty {

    public ServiceProp[] getAllExternService();
 
    MiddleParameter getMiddleParameter();

}