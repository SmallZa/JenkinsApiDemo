package jenkins;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @ClassName:     JenkinsAccessor.java
 * @Description:   jenkinsApi调用类
 * 
 * @author         zhaihuayang
 * @version        V1.0  
 * @Date           2015年1月29日 上午10:36:08
 */
public class JenkinsAccessor {
	
	private CloseableHttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private HttpClientContext localContext;
    private HttpHost host;
    private UrlUtil uu = new UrlUtil();
    
    public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(CloseableHttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public HttpGet getHttpGet() {
		return httpGet;
	}

	public void setHttpGet(HttpGet httpGet) {
		this.httpGet = httpGet;
	}

	public HttpPost getHttpPost() {
		return httpPost;
	}

	public void setHttpPost(HttpPost httpPost) {
		this.httpPost = httpPost;
	}

	public HttpClientContext getLocalContext() {
		return localContext;
	}

	public void setLocalContext(HttpClientContext localContext) {
		this.localContext = localContext;
	}

	public HttpHost getHost() {
		return host;
	}

	public void setHost(HttpHost host) {
		this.host = host;
	}
  
    /**
     * 
     * @Title: getAllJobs
     * @Description: 获取所有job列表 
     * @return: String
     * @throws URISyntaxException 
     */
    public String getAllJobs() throws ClientProtocolException, IOException, URISyntaxException{
    	this.uu.generateHttpGetAuth(this.uu.getJobsUrl(), this);
    	HttpResponse response = httpClient.execute(host, httpGet, localContext);
		return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: getJobs
     * @Description: 获取某个区间内的job 
     * @return: String
     */
    public String getJobs(int bottom, int top) throws URISyntaxException, ClientProtocolException, IOException{
		uu.setRangeBottom(bottom);
		uu.setRangeTop(top);
		String jobsUrl = uu.getJobsUrl();
    	this.uu.generateHttpGetAuth(jobsUrl, this);
    	HttpResponse response = httpClient.execute(host, httpGet, localContext);
		return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: getJobs
     * @Description: 获取job不同属性 
     * @return: String
     */
    public String getJobs(ArrayList<String> attributes) throws URISyntaxException, ClientProtocolException, IOException{
    	uu.setAttributes(attributes);
    	String jobsUrl = uu.getJobsUrl();
    	this.uu.generateHttpGetAuth(jobsUrl, this);
    	HttpResponse response = httpClient.execute(host, httpGet, localContext);
		return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: getJobs
     * @Description: 获取特定区间内含有不同属性的job列表 
     * @return: String
     */
    public String getJobs(ArrayList<String> attributes,int bottom, int top) throws URISyntaxException, ClientProtocolException, IOException{
    	uu.setRangeBottom(bottom);
		uu.setRangeTop(top);
		uu.setAttributes(attributes);
		String jobsUrl = uu.getJobsUrl();
    	this.uu.generateHttpGetAuth(jobsUrl, this);
    	HttpResponse response = httpClient.execute(host, httpGet, localContext);
		return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: createJob
     * @Description: 创建job 
     * @return: String
     */
    public String createJob(String jobName, String configFilePath)throws URISyntaxException, ClientProtocolException, IOException{
    	String createUrl = uu.getJobCreateUrl(jobName);
    	this.uu.generateHttpPostAuth(createUrl, this);
    	this.uu.setHttpPostHead("Content-Type", "application/xml", this);
    	this.uu.setHttpPostConfig(configFilePath, this);
    	HttpResponse response = httpClient.execute(host, httpPost, localContext);
    	return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: copyJob
     * @Description: 复制job 
     * @return: String
     */
    public String copyJob(String newJobName, String fromJobName) throws URISyntaxException, ClientProtocolException, IOException{
    	String copyUrl = uu.getJobCopyUrl(newJobName, fromJobName);
    	this.uu.generateHttpPostAuth(copyUrl, this);
    	this.uu.setHttpPostHead("Content-Type", "application/xml", this);
    	HttpResponse response = httpClient.execute(host, httpPost, localContext);
    	return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: buildJob
     * @Description: 构建指定job 
     * @return: String
     */
    public String buildJob(String jobName) throws URISyntaxException, ClientProtocolException, IOException{
    	String buildUrl = uu.getJobBuildUrl(jobName);
    	this.uu.generateHttpPostAuth(buildUrl, this);
    	HttpResponse response = httpClient.execute(host, httpPost, localContext);
    	return EntityUtils.toString(response.getEntity());
    }
    
    /**
     * 
     * @Title: deleteJob
     * @Description: 删除指定job 
     * @return: String
     */
    public String deleteJob(String jobName) throws URISyntaxException, ClientProtocolException, IOException{
    	String deleteUrl = uu.getJobDeleteUrl(jobName);
    	this.uu.generateHttpPostAuth(deleteUrl, this);
    	HttpResponse response = httpClient.execute(host, httpPost, localContext);
    	return EntityUtils.toString(response.getEntity());
    }
}

 