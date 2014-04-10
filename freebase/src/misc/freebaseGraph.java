package misc;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


public class freebaseGraph {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws IOException 
	 */
	public static void main(String[] args){
		graphSearch("Conscientiousness");
	}
	
	public static void graphSearch(String searchTerm){
		try {
			freeBaseSearch(searchTerm);
			System.out.println("=======================================================");
			topicSearch(searchTerm,"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void topicSearch(String searchTerm,String filter) throws Exception{
		JSONObject result =  (JSONObject) topicGet(searchTerm,filter);//.get("property");
		if(result.has("error")){
			JSONObject error = (JSONObject) result.get("error");
			System.out.println(error.getString("message"));
			return;
		}
		Iterator it = result.keys();
		while(it.hasNext()){
			String key = it.next().toString();
			String value = result.getJSONObject(key).getJSONArray("values").getJSONObject(0).getString("text")
						.split(" - ")[0];
			System.out.println(key+":"+value);
		}
	}
	
	public static JSONObject topicGet(String query,String filter) throws Exception, IOException{
		String topic = query.replace(" ", "_");
		//String filter = "people";
		if(!filter.isEmpty()){
			filter="filter=/".concat(filter);
		}
		String url = "https://www.googleapis.com/freebase/v1/topic/en/"+topic+"?"+filter;
		
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String entityContents = EntityUtils.toString(entity);
		JSONObject root = new JSONObject(entityContents);
		//System.out.println(root.toString());
		return root;
	}
	
	public static void freeBaseSearch(String searchTerm) throws Exception{
			JSONArray result = (JSONArray) freeBaseGet(searchTerm).get("result");
			for(int i=0;i<result.length();i++){
				JSONObject obj = result.getJSONObject(i);
				String value = obj.getString("name");
				if(value.isEmpty())continue;
				String key = "";
				if(obj.has("notable"))
				 key = obj.getJSONObject("notable").getString("name");
				System.out.println(key+":"+value);
				//System.out.println(result.getJSONObject(i).toString());
			}
	}
	
	public static JSONObject freeBaseGet(String query) throws Exception{
		String searchQuery = URLEncoder.encode(query, "UTF-8");
		String url = "https://www.googleapis.com/freebase/v1/search?query="+searchQuery+"&key=AIzaSyD0G812-8jJf1qSVwFju1um4cPZy9fW2gY";
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String entityContents = EntityUtils.toString(entity);
		JSONObject root = new JSONObject(entityContents);
		System.out.println("query"+root.toString());
		return root;
	}

}
