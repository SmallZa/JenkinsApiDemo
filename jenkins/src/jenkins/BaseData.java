package jenkins;
/**
 * 
 * @ClassName:     BaseData.java
 * @Description:   基础数据类
 * 
 * @author         zhaihuayang
 * @version        V1.0  
 * @Date           2015年1月29日 上午10:35:15
 */
public class BaseData {
	public static String USERNAME = "YOUR_USERNAME";
	public static String APITOKEN = "YOUR_APITOKEN";
	public static String JENKINSURL = "http://quad.ecom.jenkins.baidu.com/";
	
	/**获取job的视图方式，json和xml两种*/
	public static String JSON = "json";
	public static String XML = "xml";
	
	/**job列表可选的job属性，目前仅支持三个属性*/
	public static String JOBNAME = "name";
	public static String JOBURL = "url";
	public static String JOBCOLOR = "color";
	
	/**job列表中job的数量*/
	public static int RANGEBOTTOM = -1;
	public static int RANGETOP = -1;
}

 