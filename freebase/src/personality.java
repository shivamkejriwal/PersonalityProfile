import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class personality {

	/**
	 * @param args
	 */
	static List<String> Openness_to_experience = Arrays.asList(new String[]{"inventive","curious"});
	static List<String> Conscientiousness = Arrays.asList(new String[]{"efficient","organized"});
	static List<String> Extraversion = Arrays.asList(new String[]{"outgoing","energetic"});
	static List<String> Agreeableness = Arrays.asList(new String[]{"friendly","compassionate"});
	static List<String> Neuroticism = Arrays.asList(new String[]{"sensitive","nervous"});
	
	static List<String> VS_Openness_to_experience = Arrays.asList(new String[]{"consistent","cautious"});// 1 - Ave[]
	static List<String> VS_Conscientiousness = Arrays.asList(new String[]{"easy-going","careless"});// 1 - Ave[]
	static List<String> VS_Extraversion = Arrays.asList(new String[]{"solitary","reserved"});// 1 - Ave[]
	static List<String> VS_Agreeableness = Arrays.asList(new String[]{"analytical","detached"});// 1 - Ave[]
	static List<String> VS_Neuroticism = Arrays.asList(new String[]{"secure","confident"});// 1 - Ave[]
	
	
	public static void main(String[] args) throws Exception {
		long StartTime = System.currentTimeMillis();
		//Approx 6 min per
		//String entity = "Lady Gaga";
		//List<String> testcases = getTestCase();
		List<String> facebookLikes = getFacebookLikes("Shivam");
		HashMap<String,Double> personalityProfile = getPersonalityProfile(facebookLikes);
		print(personalityProfile,"Shivam");
		long EndTime = System.currentTimeMillis();
		System.out.println( "Done in "+(EndTime-StartTime)+" msec.");
		//		//===========================
		//		facebookLikes = getFacebookLikes("Pratham");
		//		personalityProfile = getPersonalityProfile(facebookLikes);
		//		print(personalityProfile,"Pratham");
		//		EndTime = System.currentTimeMillis();
		//		System.out.println( "Done in "+(EndTime-StartTime)+" msec.");
	}
	
	private static void print(HashMap<String,Double> personalityProfile , String Person){
		System.out.println("==============================================");
		System.out.println("Personality Profile for "+Person);
		System.out.println("==============================================");
		Double Openness_to_experience = personalityProfile.get("Openness_to_experience") *100;
		Double Conscientiousness = personalityProfile.get("Conscientiousness")*100;
		Double Extraversion = personalityProfile.get("Extraversion")*100;
		Double Agreeableness = personalityProfile.get("Agreeableness")*100;
		Double Neuroticism = personalityProfile.get("Neuroticism")*100;
		
		System.out.println("Openness_to_experience: "+Openness_to_experience+"% [inventive and curious] vs "
												+ (100-Openness_to_experience)+"% [consistent and cautious]");
		System.out.println("Conscientiousness: "+Conscientiousness+"% [efficient and organized] vs "
												+ (100-Conscientiousness)+"% [easy-going and careless]");
		System.out.println("Extraversion: "+Extraversion+"% [outgoing and energetic] vs "
												+ (100-Extraversion)+"% [solitary and reserved]");
		System.out.println("Agreeableness: "+Agreeableness+"% [friendly and compassionate] vs "
												+ (100-Agreeableness)+"% [analytical and detached]");
		System.out.println("Neuroticism: "+Neuroticism+"% [sensitive and nervous] vs "
												+ (100-Neuroticism)+"% [secure and confident]");
		System.out.println("----------------------------------------------");
	}
	
	private static List<String> getFacebookLikes(String file) throws Exception{
		List<String> facebookLikes = new LinkedList<String>();
		JSONObject jsonObject = new JSONObject(readFile("./data/"+file+".json"));
		JSONArray jsonarray = jsonObject.getJSONArray("likes");
		//System.out.println(jsonarray.toString());
		for(int i=0;i<jsonarray.length();i++){
			JSONObject obj = jsonarray.getJSONObject(i);
			String name = "";
			if(obj.has("name")){
				name = obj.getString("name");
			}else if(obj.has("notable")){
					JSONObject notable = (JSONObject) obj.get("notable");
					name = notable.getString("name").toString();
			}else{
				//System.out.println(obj.toString());
			}
			//System.out.println(name);
			facebookLikes.add(name);
		}
		System.out.println(facebookLikes.size()+" #No of Likes");
		return facebookLikes;
	}
	
	private static String readFile(String file){
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		try {
 
			String sCurrentLine;
 
			br = new BufferedReader(new FileReader(file));
 
			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				sb.append(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private static List<String> getTestCase(){
		List entities = Arrays.asList(new String[]{"TasteKid","Hotel Transylvania","Hnmovies.com"
				,"Chaai,Paani,Biskuut etc","Facebook Engineering","NDTV"
				,"UIUC M.A.T.R.I.X. Math Club","Angie's","La Martiniere For Boys"
				,"Indian Political Cartoons (IPC)","YUVA REVOLUTION","Youth Democracy"
				,"Debanjan Sen Foundation","ThisYaThat.com","University of Illinois at Urbana-Champaign"
				,"CS 225 UIUC","Atal Bihari Vajpayee","Narendra Modi","We Hate Rahul Gandhi"
				,"LMB tips","Citizen Of INDIA","Nevada State High School"
				,"Sugam Park (Asansol) by Sugam Homes", "A Beautiful Mind"
				,"This page is absolutely CRAP","Mario","The Last Samurai"
				,"A Beautiful Mind","World of Warcraft","Civilization IV"
				,"Unreal Tournament 2004","The Big Bang Theory","Prince of Persia"
				,"How I Met Your Mother","Entourage","Inception","Dexter","Smallville"
				,"Good Will Hunting","V for Vendetta","Fight Club"
				,"Indian Rupee", "Kamath Circle(K.C)","Nature Club Of La Martiniere For Boys"
				, "La Martiniere Calcutta","Animation","Graphic Design","Programming"
				,"Acting Stupid","Dancing"});
		List shortList = Arrays.asList(new String[]{"Hotel Transylvania","Facebook Engineering","NDTV"
				,"YUVA REVOLUTION","Youth Democracy","University of Illinois at Urbana-Champaign"
				,"Atal Bihari Vajpayee","Narendra Modi","Citizen Of INDIA","Nevada State High School"
				,"Mario","V For Vendetta","A Beautiful Mind","Smallville"
				,"World of Warcraft","Civilization IV","Unreal Tournament 2004","The Big Bang Theory"
				,"Prince of Persia","How I Met Your Mother","Entourage","Inception","Dexter"
				,"The Last Samurai","Good Will Hunting","V for Vendetta","Fight Club"
				,"Indian Rupee", "La Martiniere Calcutta","Animation","Graphic Design","Programming"
				,"Acting Stupid","Dancing"});
		
		List basicTestCase = Arrays.asList(new String[]{"Hotel Transylvania","Facebook Engineering","NDTV"
				,"Graphic Design","Programming","V For Vendetta","Dancing"});
		List TestCase = Arrays.asList(new String[]{"Smallville","Programming","Hotel Transylvania","Dancing"});
		
		return TestCase;
	}
	
	public static HashMap<String,Double> getPersonalityProfile(List<String> entities) throws Exception{
		HashMap<String,Double> personalityProfile = new HashMap<String,Double>();
		double completion = 0;
		double count = 0;
		Node tester = new Node();
		for(String entity:entities){
			if(!tester.getInfo(entity)){
				System.out.println(entity + "::: No Information");
				continue;
			}
			
			double percent = 100*completion/entities.size();
			double timeRemaining = (entities.size()-count)*6;
			System.out.println("~~ "+percent+"% Done , Approx Time Remaining is "+timeRemaining+" minutes");
			HashMap<String,Double> profile = getPersonalityProfile(entity);
			double total = 0.0;
			if(personalityProfile.isEmpty())
				personalityProfile.putAll(profile);
			for(Entry entry:profile.entrySet()){
				double TotalValue = personalityProfile.get(entry.getKey());
				double LocalValue = profile.get(entry.getKey());
				total+=TotalValue+LocalValue;
				//System.out.println(TotalValue+" : "+LocalValue);
				personalityProfile.put(entry.getKey().toString(), TotalValue+LocalValue);
				//System.out.println(entry.getKey()+" : "+entry.getValue());
			}
			if(total>0)
				count++;
			completion++;
		}
		Set<Entry<String, Double>> entrySet = personalityProfile.entrySet();
		for(Entry entry:entrySet){
			double value = ((Double) entry.getValue())/count;
			personalityProfile.put((String) entry.getKey(), value);
		}
		System.out.println("~~ 100% Done");
		return personalityProfile;
	}
	
	public static HashMap<String,Double> getPersonalityProfile(String entity){
		System.out.println(":: Analyzing Personality Profile of "+entity);
		
		
		HashMap<String,Double> personalityProfile = new HashMap<String,Double>();
	
		personalityProfile.put("Openness_to_experience",getRelatedness(Openness_to_experience,VS_Openness_to_experience,entity));
		personalityProfile.put("Conscientiousness",getRelatedness(Conscientiousness,VS_Conscientiousness,entity));
		personalityProfile.put("Extraversion",getRelatedness(Extraversion,VS_Extraversion,entity));
		personalityProfile.put("Agreeableness",getRelatedness(Agreeableness,VS_Agreeableness,entity));
		personalityProfile.put("Neuroticism",getRelatedness(Neuroticism,VS_Neuroticism,entity));
		print(personalityProfile,entity);
//		System.out.println("-------------------");
//		for(Entry entry:personalityProfile.entrySet()){
//			System.out.println(entry.getKey()+" : "+entry.getValue());
//		}
//		System.out.println("-------------------");
		return personalityProfile;
	}
	
	private static double getRelatedness(List<String> featureList,List<String> VS_featureList, String entity ){
		System.out.print(":: Analyzing "+entity+" in regards to "+featureList + " VS "+ VS_featureList);
		double aveRelatedness = 0;
		double count = 0;
		for(String feature:featureList){
			
			try {
				double relatedness=util.fullyConnectedSimilarity(feature, entity);
				//System.out.println("relatedness: "+relatedness);
				if(relatedness>0){
					aveRelatedness+=1-relatedness;
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(String feature:VS_featureList){
			
			try {
				double relatedness=util.fullyConnectedSimilarity(feature, entity);
				if((1-relatedness)<0){
					relatedness = Double.MIN_NORMAL;
				}
				if(relatedness>0){
					aveRelatedness+=1-relatedness;
					count++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(aveRelatedness>0)
			aveRelatedness=aveRelatedness/count;
		System.out.println(" ::: "+aveRelatedness);
		return aveRelatedness;
	}
}
