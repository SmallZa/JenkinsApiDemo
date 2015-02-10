package jenkins;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.http.client.ClientProtocolException;

public class JenkinsDemo {
	public static void main(String []args) throws ClientProtocolException, IOException, URISyntaxException{
		JenkinsAccessor ja = new JenkinsAccessor();
		//String allJobsUrl = "http://quad.ecom.jenkins.baidu.com/api/xml?tree=jobs[name,color]";
		System.out.println(ja.getAllJobs());//getAllJobs
		
		ArrayList<String> attributes = new ArrayList<String>();
		attributes.add("name");
		attributes.add("color");
		attributes.add("url");
		System.out.println(ja.getJobs(attributes));//getJobsWithAttributes
		
		int bottom = 0;
		int top = 5;
		System.out.println(ja.getJobs(bottom, top));//getLimitedJobs
		
		System.out.println(ja.getJobs(attributes, bottom, top));//getLimitedJobsWithAttributes
		
		String configFilePath = "D://config.xml";
		System.out.println(ja.createJob("create_test", configFilePath));//create a new job
		
		System.out.println(ja.copyJob("copy_test", "create_test"));
		
		String buildJobName = "build_test";
		System.out.println(ja.buildJob(buildJobName));//build a job
		
		String deleteJobName = "delete_test";
		System.out.println(ja.deleteJob(deleteJobName));//delete a job
	}
}

 