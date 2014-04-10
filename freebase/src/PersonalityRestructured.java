import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;


public class PersonalityRestructured {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long StartTime = System.currentTimeMillis();
		System.out.println("Initializing.");
		PersonalityRestructured pr = new PersonalityRestructured();
		System.out.println("Starting Analysis.");
		pr.getPersonalityProfile();
		pr.getBig5Personality();
		long EndTime = System.currentTimeMillis();
		double timeTaken = (EndTime-StartTime)/60000;
		System.out.println( "Done in "+timeTaken+" minutes.");
		
		System.out.println("Printing Result.");
		for(Entry entry:pr.personalityProfile.entrySet()){
			System.out.println(entry.getKey().toString()+" : "+entry.getValue().toString());
		}
		
		pr.print("Shivam");
	}

	HashMap<String,Double> personalityProfile;
	HashMap<String,Double> personalityBig5;
	List<Node> likes;
	List<Node> traits;
	
	private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
                    new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db), 
                    new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
                    };
    List<String> Openness_to_experience,Conscientiousness,Extraversion,Agreeableness,Neuroticism;
	List<String> VS_Openness_to_experience,VS_Conscientiousness,VS_Extraversion,VS_Agreeableness,VS_Neuroticism ;
	List<String> completeTraitList = null;
	
	public PersonalityRestructured() {
		
		super();
		WS4JConfiguration.getInstance().setMFS(true);
		this.Openness_to_experience = Arrays.asList(new String[]{"inventive","curious"});
		this.Conscientiousness = Arrays.asList(new String[]{"efficient","organized"});
		this.Extraversion = Arrays.asList(new String[]{"outgoing","energetic"});
		this.Agreeableness = Arrays.asList(new String[]{"friendly","compassionate"});
		this.Neuroticism = Arrays.asList(new String[]{"sensitive","nervous"});
		
		this.VS_Openness_to_experience = Arrays.asList(new String[]{"consistent","cautious"});// 1 - Ave[]
		this.VS_Conscientiousness = Arrays.asList(new String[]{"easy going","careless"});// 1 - Ave[]
		this.VS_Extraversion = Arrays.asList(new String[]{"solitary","reserved"});// 1 - Ave[]
		this.VS_Agreeableness = Arrays.asList(new String[]{"analytical","detached"});// 1 - Ave[]
		this.VS_Neuroticism = Arrays.asList(new String[]{"secure","confident"});// 1 - Ave[]
		setCompleteTraitList();
		
		this.personalityProfile = new HashMap<String,Double>();
		this.personalityBig5 = new HashMap<String,Double>();
		this.likes = new ArrayList<Node>();
		this.traits =  new ArrayList<Node>();
		try {
			System.out.println("Getting Facebook Likes.");
			likes = getFacebookLikes("Shivam");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Getting List of Taits.");
		traits = getTraits();
		System.out.println("Expanding Data.");
		expand();
	}

	private void setCompleteTraitList(){
//		List<String> complete = Arrays.asList(new String[]{"inventive","curious","efficient","organized","outgoing","energetic"
//				,"friendly","compassionate","sensitive","nervous","consistent","cautious"
//				,"easy going","careless","solitary","reserved","analytical","detached"
//				,"secure","confident"});
		completeTraitList = new ArrayList<String>();
		completeTraitList.addAll(Conscientiousness);
		completeTraitList.addAll(Extraversion);
		completeTraitList.addAll(Agreeableness);
		completeTraitList.addAll(Neuroticism);
		completeTraitList.addAll(VS_Openness_to_experience);
		completeTraitList.addAll(VS_Conscientiousness);
		completeTraitList.addAll(VS_Extraversion);
		completeTraitList.addAll(VS_Agreeableness);
		completeTraitList.addAll(VS_Neuroticism);
	}
	
	private void print(String Person){
		System.out.println("==============================================");
		System.out.println("Personality Profile for "+Person);
		System.out.println("==============================================");
		Double Openness_to_experience = personalityBig5.get("Openness_to_experience") *100;
		Double Conscientiousness = personalityBig5.get("Conscientiousness")*100;
		Double Extraversion = personalityBig5.get("Extraversion")*100;
		Double Agreeableness = personalityBig5.get("Agreeableness")*100;
		Double Neuroticism = personalityBig5.get("Neuroticism")*100;
		
		System.out.println("Openness_to_experience: "+Openness_to_experience+"% [inventive and curious] vs "
												+ (100-Openness_to_experience)+"% [consistent and cautious]");
		System.out.println("Conscientiousness: "+Conscientiousness+"% [efficient and organized] vs "
												+ (100-Conscientiousness)+"% [easy going and careless]");
		System.out.println("Extraversion: "+Extraversion+"% [outgoing and energetic] vs "
												+ (100-Extraversion)+"% [solitary and reserved]");
		System.out.println("Agreeableness: "+Agreeableness+"% [friendly and compassionate] vs "
												+ (100-Agreeableness)+"% [analytical and detached]");
		System.out.println("Neuroticism: "+Neuroticism+"% [sensitive and nervous] vs "
												+ (100-Neuroticism)+"% [secure and confident]");
		System.out.println("----------------------------------------------");
	}
	
	private static List<Node> getFacebookLikes(String file) throws Exception{
		List<Node> facebookLikes = new LinkedList<Node>();
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
			
			Node node = new Node();
			if(node.getInfo(name))
				facebookLikes.add(node);
		}
		System.out.println("::: No of Likes: "+facebookLikes.size());
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
	
	private List<Node> getTraits(){
		
		List<Node> traits = new LinkedList<Node>();
		for(String trait:completeTraitList){
			Node node;
			try {
				node = new Node();
				node.isWord = true;
				if(node.getInfo(trait))
					traits.add(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		return traits;
	}

	private void expand(){
		for(Node node:likes){
			try {
				node.getAdjacentNodes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for(Node node:traits){
			try {
				node.getAdjacentNodes();
				//if(node.AdjacentNodes.size()==0)
				//	node.AdjacentNodes.add(node);
				//System.out.println(":::::"+node.name+" : "+node.AdjacentNodes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void getPersonalityProfile(){
		//HashMap<String,Pair> tempProfile = new HashMap<String,Pair>();
		for(Node trait:traits){
			System.out.println("Analysing Trait : "+trait.name);
			double total = 0;
	        int count = 0;
			for(Node like:likes){
				System.out.println("::: Analysing Like : "+like.name);
				double relatedness=GetSimilarity(trait, like);
				//System.out.println("relatedness: "+relatedness);
				if(relatedness>0){
					total+=relatedness;
					count++;
				}
			}
			double ave = total/count;
			if(Double.isNaN(ave) || ave<0)
				ave=0;
			else if(Double.isInfinite(ave))
				ave=1;
			personalityProfile.put(trait.name,ave);
		}
	}
	
	private void getBig5Personality(){
		setBig5Trait("Openness_to_experience",Openness_to_experience,VS_Openness_to_experience);
		setBig5Trait("Conscientiousness",Conscientiousness,VS_Conscientiousness);
		setBig5Trait("Extraversion",Extraversion,VS_Extraversion);
		setBig5Trait("Agreeableness",Agreeableness,VS_Agreeableness);
		setBig5Trait("Neuroticism",Neuroticism,VS_Neuroticism);
	}
	
	private void setBig5Trait(String trait ,List<String> featureList,List<String> VS_featureList){
		System.out.println(":: setBig5Trait: "+trait+" in regards to "+featureList + " VS "+ VS_featureList);
		double aveRelatedness = 0;
		double count = 0;
		for(String feature:featureList){
				Double relatedness = personalityProfile.get(feature);
				if(relatedness==null || !personalityProfile.containsKey(feature)){
					System.out.println("feature not found:"+feature+", relatedness: "+relatedness);
					continue;
				}
				//System.out.println("relatedness: "+relatedness);
				if(relatedness>0){
					aveRelatedness+=1-relatedness;
					count++;
				}
		}
		for(String feature:VS_featureList){
				Double relatedness = personalityProfile.get(feature);
				if(relatedness==null || !personalityProfile.containsKey(feature)){
					System.out.println("feature not found:"+feature+", relatedness: "+relatedness);
					continue;
				}
				if((1-relatedness)<0){
					relatedness = Double.MIN_NORMAL;
				}
				if(relatedness>0){
					aveRelatedness+=1-relatedness;
					count++;
				}
		}
		if(aveRelatedness>0)
			aveRelatedness=aveRelatedness/count;
		if(Double.isNaN(aveRelatedness) || aveRelatedness<0){
			aveRelatedness=0;
		}
		else if(Double.isInfinite(aveRelatedness)){
			aveRelatedness=1;
		}
		personalityBig5.put(trait, aveRelatedness);
		//System.out.println(" ::: "+aveRelatedness);
		//return aveRelatedness;
	}

	private static double GetSimilarity( Node node1, Node node2 ){
		double total = 0;
        int count = 0;
		for(Node nodeA:node1.AdjacentNodes){
			for(Node nodeB:node2.AdjacentNodes){
				if(nodeA.hasInformation && nodeB.hasInformation){
					double similarity = GetSimilarity(nodeA.name, nodeB.name);
					if(similarity>0){
						total+=similarity;
						count++;
					}
				}
					
			}
		}
		double ave = total/count;
		if(Double.isNaN(ave) || ave<0){
			ave=0;
		}
		else if(Double.isInfinite(ave)){
			ave=1;
		}
		return ave;
	}
	
	private static double GetSimilarity( String word1, String word2 ) {
        double total = 0;
        int count = 0;
        for ( RelatednessCalculator rc : rcs ) {
                double similarity = rc.calcRelatednessOfWords(word1, word2);
                if (Double.isNaN(similarity) ||similarity <=0){
                	similarity=0;
        		}
                else if (Double.isInfinite(similarity)){
                	similarity=1;
        		}
                if(similarity>0){
                	count++;
                	total+=similarity;
                	//System.out.println( rc.getClass().getName()+"\t"+similarity );
                }    
        }
        double ave = total/count;
		if(Double.isNaN(ave) || ave<0){
			ave=0;
		}
		else if(Double.isInfinite(ave)){
			ave=1;
		}
		return ave;
	}
	
}


/*
private class Pair{
	double total;
	double count;
	public Pair(){
		this.total=0;
		this.count=0;
	}
	public void add(double value){
		if(value<=0)return;
		count++;
		total+=value;
	}
}
*/