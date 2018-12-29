package nc.uap.ctrl.tpl.qry.operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.uap.ctrl.tpl.qry.meta.IQueryConstants;

@SuppressWarnings("serial")
public class OperatorFactory implements Serializable{
	
	private static OperatorFactory factory= new OperatorFactory();
	private Map<String, IOperator> code_operator_map = new HashMap<String, IOperator>(); 
	
	public static OperatorFactory getInstance(){
		return factory;
	}
	
	public OperatorFactory() {
		super();
		registerOperator(EqOperator.getInstance());
		registerOperator(Eq2Operator.getInstance());
		registerOperator(NeqOperator.getInstance());
	
		//registerOperator(LikeOperator.getInstance());
		registerOperator(IsnotnullOperator.getInstance());
		registerOperator(IsnullOperator.getInstance());
		registerOperator(IsnullIncludeSpaceOperator.getInstance());
		registerOperator(IsnotnullIncludeSpaceOperator.getInstance());
		registerOperator(GetOperator.getInstance());
		registerOperator(LetOperator.getInstance());
		registerOperator(LtOperator.getInstance());
		registerOperator(GtOperator.getInstance());
		registerOperator(BetweenOperator.getInstance());
		
		registerOperator(IOperatorConstants.BETWEENIC,BetweenICOperator.getInstance());
		
		registerOperator(IOperatorConstants.IN,EqOperator.getInstance());
		registerOperator(IOperatorConstants.NIN,NeqOperator.getInstance());
		registerOperator(IOperatorConstants.NEQ2,NeqOperator.getInstance());
		
		registerOperator(IOperatorConstants.EQIC,EqICOperator.getInstance());
		registerOperator(IOperatorConstants.EQ2IC,Eq2ICOperator.getInstance());
		registerOperator(IOperatorConstants.NEQIC,NeqICOperator.getInstance());
		registerOperator(IOperatorConstants.INIC,EqICOperator.getInstance());
		registerOperator(IOperatorConstants.NINIC,NeqICOperator.getInstance());
		registerOperator(IOperatorConstants.NEQ2IC,NeqICOperator.getInstance());
		
		
		registerOperator(IOperatorConstants.NLIKE,NotLikeOperator.getInstance());
		registerOperator(IOperatorConstants.LIKE,LikeOperatorDecarator.getInstance(IOperatorConstants.LIKE));
		registerOperator(IOperatorConstants.LLIKE,LikeOperatorDecarator.getInstance(IOperatorConstants.LLIKE));
		registerOperator(IOperatorConstants.RLIKE,LikeOperatorDecarator.getInstance(IOperatorConstants.RLIKE));
		registerOperator(IOperatorConstants.LIKEIC,LikeOperatorDecarator.getInstance(IOperatorConstants.LIKEIC));
		registerOperator(IOperatorConstants.LLIKEIC,LikeOperatorDecarator.getInstance(IOperatorConstants.LLIKEIC));
		registerOperator(IOperatorConstants.RLIKEIC,LikeOperatorDecarator.getInstance(IOperatorConstants.RLIKEIC));
		
	}

	private void registerOperator(String code,IOperator operator)
	{
		code_operator_map.put(code,operator);
	}
	public void registerOperator(IOperator operator) {
		code_operator_map.put(operator.getOperatorCode(),operator);
	}

	public IOperator getOperator(String code)
	{
		return code_operator_map.get(code);
	}
	
	
	/**
	 * 根据desc得到对应的多个操作符. 
	 * @param desc 操作符的描述串.沿用查询模板的习惯采用形如"<=@<@="的编码方式
	 * @return
	 */
	//TODO 也许应该提供方式, 让其允许产品组设置自己的操作符名称
	public IOperator[] getOperators(String desc)
	{
		List<IOperator> result = new ArrayList<IOperator>();
		if(desc!=null)
		{
			String[] codes = desc.split("@");
			for (String code : codes)
			{
				IOperator operator = getOperator(code);
				if(operator!=null)
					result.add(operator);
			}
		}
		return (IOperator[]) result.toArray(new IOperator[result.size()]);
	}
	
	/**
	 * 根据数据类型，得到默认的操作符
	 * @param datatype
	 * @return
	 */
	public static IOperator[] getDefaultOperatorsByDataType(int datatype) {
		switch (datatype) {
		case IQueryConstants.INTEGER:
		case IQueryConstants.DECIMAL:
		case IQueryConstants.DATE:
		case IQueryConstants.TIME:
		case IQueryConstants.LITERALDATE:
			return OperatorFactory.getInstance().getOperators("=@>@>=@<@<=@between@");
		case IQueryConstants.STRING:
			return OperatorFactory.getInstance().getOperators("=@like@left like@right like@");
		case IQueryConstants.UFREF:
		case IQueryConstants.MULTILANG:
			return OperatorFactory.getInstance().getOperators("=@like@left like@right like@");
		case IQueryConstants.USERCOMBO:
		case IQueryConstants.BOOLEAN:
		default:
			return OperatorFactory.getInstance().getOperators("=@like@");
		}
		
	}
}
