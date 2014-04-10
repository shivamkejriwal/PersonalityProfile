
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Node {

	String id;
	String mid;
	String name;
	boolean isWord = false;
	boolean hasInformation;
	double score = 0;
	ArrayList<Node> AdjacentNodes;
	int state; 
	
	public Node() throws Exception{
		this.state = 0;
		this.AdjacentNodes = new ArrayList<Node>();
	}
	
	public Node(String mid,String name){
		this.mid  = mid;
		this.name  = name;
		this.score = 1;
		this.id = "";
		this.state = 0;
		this.AdjacentNodes = new ArrayList<Node>();
	}
	
	public boolean getInfo(String keyword) throws Exception{
		JSONArray result = null;
		if(isWord){
			result = (JSONArray) wordnetGet(keyword).get("result");
		}
		else{
			result = (JSONArray) freeBaseGet(keyword).get("result");
		}
		if(result.length()==0){
			//System.out.println(keyword + "::: failed");
			//System.out.println("result: " + result.toString());
			hasInformation = false;
			return false;
		}
		JSONObject obj =  result.getJSONObject(0);
		//JSONObject obj = null;
		//if(result.has("name"))obj.
		//System.out.println(obj.toString());
		this.mid = obj.getString("mid").toString();
		this.name = obj.getString("name").toString();
		this.score = obj.getDouble("score");
		//this.score = 1 ; //first keywords needs to be one in terms of relevance
		if(obj.has("id")){
			this.id =  obj.getString("id").toString();
		}else if(obj.has("notable")){
				JSONObject notable = (JSONObject) obj.get("notable");
				this.name = notable.getString("name").toString();
				this.id = notable.getString("id").toString();
		}else{
			this.id = "";
		}
		hasInformation = true;
		return true;
	}
	
	public void getAdjacentNodes() throws Exception{
		JSONObject wordnetResults = wordnetGet(name);
		JSONObject freebaseResults = freeBaseGet(name);
		AdjacentNodes.addAll(getResultList(wordnetResults));
		AdjacentNodes.addAll(getResultList(freebaseResults));
		
	}
	
	public ArrayList<Node> getResultList(JSONObject root) throws Exception{
		ArrayList<Node> nodeList = new ArrayList<Node>();
		JSONArray result = (JSONArray) root.get("result");
		for(int i=0;i<result.length();i++){
			JSONObject obj = result.getJSONObject(i);
			String nodeMID = obj.getString("mid").toString();
			String nodeName = obj.getString("name").toString();
			Node nearbyNode = new Node(nodeMID,nodeName);
			//nearbyNode.score = obj.getDouble("score");
			nearbyNode.score = (this.score+obj.getDouble("score"))/2;
			if(obj.has("id")){
				nearbyNode.id =  obj.getString("id").toString();
			}else if(obj.has("notable")){
					JSONObject notable = (JSONObject) obj.get("notable");
					nearbyNode.name = notable.getString("name").toString();
					nearbyNode.id = notable.getString("id").toString();
			}else {
				continue;
			}
			//System.out.println(nearbyNode.toString());
			if(nearbyNode.name!=null && !nearbyNode.name.equals(""))
				nearbyNode.hasInformation=true;
			nodeList.add(nearbyNode);
		}
		return nodeList;
	}

	private JSONObject freeBaseGet(String query) throws Exception{
		String searchQuery = URLEncoder.encode(query, "UTF-8");
		String url = "https://www.googleapis.com/freebase/v1/search?query="+searchQuery+"&key=AIzaSyD0G812-8jJf1qSVwFju1um4cPZy9fW2gY";		
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String entityContents = EntityUtils.toString(entity);
		JSONObject root = new JSONObject(entityContents);
		//System.out.println("query"+root.toString());
		return root;
	}
	
	private JSONObject wordnetGet(String query) throws Exception{
		String searchQuery = URLEncoder.encode(query, "UTF-8");
		String type = "&type=/base/wordnet/word";
		String url = "https://www.googleapis.com/freebase/v1/search?query="+searchQuery+"&key=AIzaSyD0G812-8jJf1qSVwFju1um4cPZy9fW2gY";
		url=url+type;
		HttpGet request = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		HttpResponse response = httpClient.execute(request);
		HttpEntity entity = response.getEntity();
		String entityContents = EntityUtils.toString(entity);
		JSONObject root = new JSONObject(entityContents);
		//System.out.println("query"+root.toString());
		return root;
	}
	
	private JSONObject topicGet(String query,String filter) throws Exception{
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
	
	@Override
	public String toString() {
		return "Node [id=" + id + ", mid=" + mid + ", name=" + name
				+ ", score=" + score + ", #AdjacentNodes=" + AdjacentNodes.size()
				+ ", state=" + state + "]";
	}

	@Override
	public boolean equals(Object obj) {
		return this.mid.equals(((Node)obj).mid);
	}

	
	
	
}
