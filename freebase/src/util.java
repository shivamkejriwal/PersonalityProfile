import java.util.ArrayList;

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

public class util {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		String keyword1 = "candy";
		String keyword2 = "confectionery";
		double Similarity = fullyConnectedSimilarity(keyword1,keyword2);
		System.out.println("Similarity:"+Similarity);
		/*
		double FinalSimilarity1 = getSimilarity(keyword1,keyword2);
		double FinalSimilarity2 = getSimilarity(keyword2,keyword1);
		System.out.println("Final Similarity 1 :"+FinalSimilarity1);
		System.out.println("Final Similarity 2 :"+FinalSimilarity2);
		*/
	}
	
	public static double fullyConnectedSimilarity(String keyword1,String keyword2) throws Exception{
		//System.out.println("::fullyConnectedSimilarity:");
		ArrayList<Node> list1 = expand(keyword1);
		ArrayList<Node> list2 = expand(keyword2);
		if(list1==null || list2==null){
			return 0;
		}
		
		double simForward = getSimilarity(list1,list2);
		double simBackward = getSimilarity(list2,list1);
		double Similarity = (simForward+simBackward)/2;
		//System.out.println("simForward:"+simForward);
		//System.out.println("simBackward:"+simBackward);
		if(Double.isNaN(Similarity) || Similarity<0){
			Similarity = 0;
		}else if (Double.isInfinite(Similarity)){
			Similarity = 1.0;
		}
		//System.out.println("::fullyConnectedSimilarity:"+keyword1+" , "+keyword2+" = "+Similarity);
		return Similarity;
		
	}
	
	public static double getSimilarity(ArrayList<Node> list1,ArrayList<Node> list2) throws Exception{
		double sum = 0;
		int count=0;
		for(Node node:list2){
			String phrase = node.name;
			Double Similarity = GetSimilarity(list1,phrase);
			//System.out.println("____Similarity:"+Similarity);
			if (Double.isInfinite(Similarity)){
				Similarity=1.0;
			}
			if(!Double.isNaN(Similarity) && Similarity>0){
				sum+=Similarity;
				count++;
			}
		}
		return sum/count;
	}
	
	public static double getSimilarity(String keyword1,String keyword2) throws Exception{
		double FinalSimilarity = 0;
		double Similarity = GetSimilarity(keyword1,keyword2);
		ArrayList<Node> adj = expand(keyword1);
		double expandedSimilarity = GetSimilarity(adj,keyword2);
		if (Double.isInfinite(Similarity)){
			Similarity=1;
		}
		if(Double.isNaN(Similarity) || Similarity<=0){
			FinalSimilarity = expandedSimilarity;
		}
		else{
			FinalSimilarity = (Similarity+expandedSimilarity)/2;
		}
		//System.out.println("Initial Similarity:"+Similarity);
		//System.out.println("Expanded Similarity:"+expandedSimilarity);
		return FinalSimilarity;
	}
	
	// Idea Expansion
	// ===================
	private static ArrayList<Node> expand(String keyword) throws Exception{
		Node obj = new Node();
		if(!obj.getInfo(keyword)){
			return null;
		}
		obj.getAdjacentNodes();
		return expand(obj.AdjacentNodes);
	}
	
	private static ArrayList<Node> expand(ArrayList<Node> list, int currentLevel) throws Exception{
		if(currentLevel<=0)return null;
		ArrayList<Node> childList = expand(list);
		ArrayList<Node> next = expand(childList,currentLevel-1);
		childList.addAll(next);
		return childList;
	}
	
	private static ArrayList<Node> expand (ArrayList<Node> list) throws Exception{
		ArrayList<Node> ret = new ArrayList<Node>();
		for(Node node:list){
			node.getAdjacentNodes();
			ret.addAll(node.AdjacentNodes);
		}
		return list;	
	}
	
	
	// Similarity Testing
	// ===================
	private static ILexicalDatabase db = new NictWordNet();
	
    private static RelatednessCalculator[] rcs = {
                    new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db), 
                    new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
                    };
	
	private static double GetSimilarity( String word1, String word2 ) {
        WS4JConfiguration.getInstance().setMFS(true);
        double ave = 0;
        int count = 0;
        for ( RelatednessCalculator rc : rcs ) {
                double similarity = rc.calcRelatednessOfWords(word1, word2);
                if (Double.isInfinite(similarity)){
                	similarity=1;
        		}
                if(similarity>0){
                	count++;
                	ave+=similarity;
                	//System.out.println( rc.getClass().getName()+"\t"+similarity );
                }    
        }
        return ave/count;
	}
	
	private static double GetSimilarity(ArrayList<Node> list, String word){
		double ave = 0;
        int count = 0;
		for(Node node:list){
			double similarity = GetSimilarity( node.name,word);
			if (Double.isInfinite(similarity)){
            	similarity=1;
    		}
			if(!Double.isNaN(similarity)){
            	count++;
            	ave+=similarity;
            	//System.out.println( rc.getClass().getName()+"\t"+similarity );
            } 
			//System.out.print(node.name+" , ");
		}
		//System.out.println("Similarity = "+ave/count);
		return ave/count;
	}

}
