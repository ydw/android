package my.webtext;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * @author yang
 * 
 */
public class MessageSender {

	private HttpClient httpclient;
	private HttpResponse response;
	private HttpEntity entity;
	private String token;
	private boolean loginFlg;

	public MessageSender() {
		httpclient = new DefaultHttpClient();
	}

	public boolean isLogin() {
		return loginFlg;
	}

	public void sendMessage() {
		try {
			getMessagePage();
			postFormContent();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}

	public void login() throws ClientProtocolException, IOException {
		HttpPost http = new HttpPost("https://www.vodafone.ie/myv/services/login/Login.shtml?username=0871709194&password=3848749618");
		System.out.println("executing request " + http.getURI());
		// Execute the request
		response = httpclient.execute(http);
		// Examine the response status
		System.out.println(response.getStatusLine());
		// Get hold of the response entity
		entity = response.getEntity();
		if (entity != null) {
			// to make sure the connection can be re-used
			entity.consumeContent();
		}
		loginFlg = true;
		System.out.println("----------------------------------------");
	}

	public void getMessagePage() throws ClientProtocolException, IOException {
		// go to the message page
		HttpGet httpget = new HttpGet("https://www.vodafone.ie/myv/messaging/webtext/index.jsp");
		System.out.println("executing request " + httpget.getURI());
		// Create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String responseBody = httpclient.execute(httpget, responseHandler);
		// hidden value to send message and avoid re-send by user
		System.out.println(response.getStatusLine());
		token = parseTokenValue(responseBody);
		// Get hold of the response entity
		entity = response.getEntity();
		if (entity != null) {
			entity.consumeContent();
		}
		System.out.println("----------------------------------------");

	}

	public void postFormContent() throws ClientProtocolException, IOException {
		String send_message_string = "org.apache.struts.taglib.html.TOKEN=" + token
				+ "&message=laope&num=$num&recipients[0]=0871709194&recipients[1]=&recipients[2]=&recipients[3]=&recipients[4]=&sendnow=Send+Now&futuredate=false&futuretime=false";
		HttpPost httppost = new HttpPost("https://www.vodafone.ie/myv/messaging/webtext/Process.shtml?" + send_message_string);
		System.out.println("executing request " + httppost.getURI());
		response = httpclient.execute(httppost);
		System.out.println(response.getStatusLine());
		entity = response.getEntity();
		if (entity != null) {
			entity.consumeContent();
		}
	}

	private String parseTokenValue(String htmlbody) {
		String strToken = "";
		int position = htmlbody.indexOf("TOKEN");
		int start = position + 14;
		int end = start + 32;
		strToken = htmlbody.substring(start, end);
		return strToken;
	}
}
