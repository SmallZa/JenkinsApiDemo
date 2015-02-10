package jenkins;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;

/**
 * 
 * @ClassName:     UrlUtil.java
 * @Description:   url工具类，返回jenkins调用api的url以及权限设置、配置文件读取等操作
 * 
 * @author         zhaihuayang
 * @version        V1.0  
 * @Date           2015年1月29日 上午10:30:45
 */
public class UrlUtil {
	final String username = BaseData.USERNAME;
    final String password = BaseData.APITOKEN;
	private String jenkinsUrl = BaseData.JENKINSURL;//jenkins地址
	private String view = BaseData.JSON;//获取job列表视图方式:xm/json(默认)l
	private List<String> attributes = new ArrayList<String>(){//获取job列表中job的属性，默认只显示job名称；目前仅支持name，url和color三种属性			
				/**  
				 * @Fields serialVersionUID : TODO
				 */  
				private static final long serialVersionUID = 1L;

				{
					add(BaseData.JOBNAME);
				}
			};
	private int rangeBottom = BaseData.RANGEBOTTOM;//获取job列表的起始index
	private int rangeTop = BaseData.RANGETOP;//获取job列表的终止index
	
	/**
	 * 
	 * @Title: setJenkinsUrl
	 * @Description: jenkins地址设置 
	 * @return: void
	 */
	public void setJenkinsUrl(String jenkinsUrl){
		this.jenkinsUrl = jenkinsUrl;
	}
	
	/**
	 * 
	 * @Title: setView
	 * @Description: job列表视图选择：json/xml 
	 * @return: void
	 */
	public void setView(String view){
		if(view.equalsIgnoreCase("json")){
			this.view = BaseData.JSON;
		}else if(view.equalsIgnoreCase("xml")){
			this.view = BaseData.XML;
		}
	}
	
	/**
	 * 
	 * @Title: setAttributes
	 * @Description: job属性选择：name,url,color 
	 * @return: void
	 */
	public void setAttributes(List<String> attributes){
		if(attributes.contains(BaseData.JOBURL)){
			this.attributes.add(BaseData.JOBURL);
		}
		if(attributes.contains(BaseData.JOBCOLOR)){
			this.attributes.add(BaseData.JOBCOLOR);
		}
		
	}
	
	/**
	 * 
	 * @Title: setRangeBottom
	 * @Description: job列表下限index设置 
	 * @return: void
	 */
	public void setRangeBottom(int rangeBottom){
		this.rangeBottom = rangeBottom;
	}
	
	/**
	 * 
	 * @Title: setRangeTop
	 * @Description: job列表上限index设置 
	 * @return: void
	 */
	public void setRangeTop(int rangeTop){
		this.rangeTop = rangeTop;
	}
	
	/**
	 * 
	 * @Title: getAllJobsUrl
	 * @Description: 获取所有job的url 
	 * @return: String
	 */
	public String getJobsUrl(){
		if(this.rangeBottom == -1 && this.rangeTop == -1){
			return this.jenkinsUrl + "api/"+ this.view + "?tree=jobs" + "[" +getAttributeListString() + "]";
		}
		if(this.rangeBottom == -1 && this.rangeTop != -1){
			return this.jenkinsUrl + "api/"+ this.view + "?tree=jobs" + "[" +getAttributeListString() + "]" + "{," + this.rangeTop + "}";
		}
		if(this.rangeBottom != -1 && this.rangeTop == -1){
			return this.jenkinsUrl + "api/"+ this.view + "?tree=jobs" + "[" +getAttributeListString() + "]" + "{" + this.rangeBottom + ",}";
		}
		else{
			return this.jenkinsUrl + "api/"+ this.view + "?tree=jobs" + "[" +getAttributeListString() + "]" + "{" + this.rangeBottom + "," + this.rangeTop + "}";
		}	
	}
	/**
	 * 
	 * @Title: getAttributeListString
	 * @Description: 将属性的ArrayList转化为jenkinsApi标准的属性urlString 
	 * @return: String
	 */
	private String getAttributeListString(){
		String attributesList = "";
		Iterator<String> it = attributes.iterator();
		int i = attributes.size();
		while(it.hasNext()){
			attributesList += it.next().toString();
			i--;
			if(i>0){
				attributesList += ",";
			}
		}
		this.attributes =new ArrayList<String>(){//每次指定job属性，执行完查询之后要还原默认值，即属性列表只包含jobName
			/**  
			 * @Fields serialVersionUID : TODO
			 */  
			private static final long serialVersionUID = 1L;

			{
				add(BaseData.JOBNAME);
			}
		};
		return attributesList;
	}
	
	/**
	 * 
	 * @Title: getJobCreateUrl
	 * @Description: 获取创建job的url 
	 * @return: String
	 */
	public String getJobCreateUrl(String jobName){
		return this.jenkinsUrl + "createItem?name=" + jobName;
	}
	
	public String getJobCopyUrl(String newJobName, String fromJobName){
		return this.jenkinsUrl + "createItem?name=" + newJobName +"&mode=copy&from=" + fromJobName;
	}
	/**
	 * 
	 * @Title: getBuildUrl
	 * @Description: 获取构建job的url 
	 * @return: String
	 */
	public String getJobBuildUrl(String jobName){
		return this.jenkinsUrl + "job/" + jobName + "/build";
	}
	
	/**
	 * 
	 * @Title: getJobDeleteUrl
	 * @Description: 获取删除job的url 
	 * @return: String
	 */
	public String getJobDeleteUrl(String jobName){
		return this.jenkinsUrl + "job/" + jobName + "/doDelete";
	}
	
	/**
     * 
     * @Title: generateHttpGetAuth
     * @Description: 获取权限,产生httpGet请求 
     * @return: void
     * @throws MalformedURLException 
     * @throws URISyntaxException 
     */
	public void generateHttpGetAuth(String urlString, JenkinsAccessor ja) throws MalformedURLException, URISyntaxException{
    	URL url = new URL(urlString);
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		ja.setHost(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()));
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(ja.getHost(), basicAuth);
		ja.setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build());
		ja.setHttpGet(new HttpGet(uri)); 
		// Add AuthCache to the execution context
		ja.setLocalContext(HttpClientContext.create()); 
		ja.getLocalContext().setAuthCache(authCache);
    }
    
    /**
     * 
     * @Title: generateHttpPostAuth
     * @Description: 获取权限，产生httpPost请求  
     * @return: void
     * @throws MalformedURLException 
     * @throws URISyntaxException 
     */
	public void generateHttpPostAuth(String urlString, JenkinsAccessor ja) throws MalformedURLException, URISyntaxException{
    	URL url = new URL(urlString);
		URI uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
		ja.setHost(new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()));
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
		// Create AuthCache instance
		AuthCache authCache = new BasicAuthCache();
		// Generate BASIC scheme object and add it to the local auth cache
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(ja.getHost(), basicAuth);
		ja.setHttpClient(HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build());
		ja.setHttpPost(new HttpPost(uri));
		// Add AuthCache to the execution context
		ja.setLocalContext(HttpClientContext.create()); 
		ja.getLocalContext().setAuthCache(authCache);
    }
	
	/**
	 * 
	 * @Title: setHttpGetHead
	 * @Description: 设置Get请求头 
	 * @return: void
	 */
	public void setHttpGetHead(String name, String value, JenkinsAccessor ja){
		ja.getHttpGet().setHeader(name, value);
	}
	
	/**
	 * 
	 * @Title: setHttpPostHead
	 * @Description: 设置Post请求头 
	 * @return: void
	 */
	public void setHttpPostHead(String name, String value, JenkinsAccessor ja){
		ja.getHttpPost().setHeader(name, value);
	}
	
	/**
	 * 
	 * @Title: setHttpPostConfix
	 * @Description: 设置Post 配置文件  
	 * @return: void
	 * @throws IOException 
	 */
	public void setHttpPostConfig(String configFilePath, JenkinsAccessor ja) throws IOException{
		String configData = this.getConfigString(configFilePath);
		System.out.println(configData);
		StringEntity entity = new StringEntity(configData);
		ja.getHttpPost().setEntity(entity);
	}
	
	/**
	 * 
	 * @Title: getConfigString
	 * @Description: 读取配置文件内容，并转换位String类型 
	 * @return: String
	 */
	private String getConfigString(String configFilePath) throws IOException{
		StringBuffer sb = new StringBuffer();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(configFilePath)));
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
               sb.append(line);               
	        }
	        br.close();
		return sb.toString();
	}
}

 