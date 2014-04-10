


import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class pathFind {

	
	public static void main(String[] args) throws Exception {
		new pathFind("Lady Gaga","Openness");
	}
	
	ConcurrentMap<String,Integer> outerMap ;
	public pathFind(String keyword1, String keyword2){
		Finder forward = new Finder(keyword1,keyword2,1);
		Finder backward = new Finder(keyword2,keyword1,2);
		outerMap = new ConcurrentHashMap<String,Integer>();
		forward.start();
		backward.start();
	}
	
	static class NodeSort implements Comparator<Node> {
		public int compare(Node one, Node two) {
			Double d1 = one.score;
			Double d2 = two.score;
			return Double.compare(d2, d1);
		}
	}
	
	static class RandomSort implements Comparator<Node> {
		public int compare(Node one, Node two) {
			int ret = new Random().nextInt(3);
			if(ret==0)return -1;
			if(ret==1)return 0;
			return 1;
		}
	}
	
	static class mixedSort implements Comparator<Node> {
		
		private int nodeSort(Node one, Node two) {
			Double d1 = one.score;
			Double d2 = two.score;
			return Double.compare(d2, d1);
		}
		private int randomSort(Node one, Node two) {
			int ret = new Random().nextInt(3);
			if(ret==0)return -1;
			if(ret==1)return 0;
			return 1;
		}
		public int compare(Node one, Node two) {
			double prob = new Random().nextDouble();
			if(prob<.7)return nodeSort(one,two);
			return randomSort(one,two);
		}
	}
	
	private class Finder extends Thread 
	{          
		String sourcePhrase, targetPhrase;
		int id = 0;
		public Finder(String sourcePhrase, String targetPhrase, int id){
			this.sourcePhrase = sourcePhrase;
			this.targetPhrase = targetPhrase;
			this.id = id;
		}
	        public void run()                       
	        {              
	        	try {
					FindPath();
				} catch (Exception e) {
					e.printStackTrace();
				}
	        }
	        private void FindPath() throws Exception{
	    		Node source = new Node();
	    		Node target = new Node();
	    		source.getInfo(sourcePhrase);
	    		target.getInfo(targetPhrase);
	    		PriorityQueue<Node> queue = new PriorityQueue<Node>(10, new NodeSort());
	    		if(source.equals(target)){
	    			System.out.println("Match");
	    			return;
	    		}
	    		queue.add(source);
	    		while(!queue.isEmpty()){
	    			Node top = queue.poll();
	    			if(top.equals(target)){
	    				System.out.println("Found at connection:"+(top.state+1));
	    				System.out.println("Current score:"+top.score);
	    				return;
	    			}
	    			if(top.state>3) continue;
    				//System.out.println(id +" :::: "+top.toString());
	    			top.getAdjacentNodes();
	    			for(Node neighbour:top.AdjacentNodes){
	    				if(!outerMap.containsKey(neighbour.mid)){
	    					neighbour.state = top.state+1;
	    					outerMap.put(neighbour.mid, id);
	    					//if(neighbour.score>50)
	    						queue.add(neighbour);
	    				}else if(outerMap.get(neighbour.mid)!=id){
	        					System.out.print("clash at "+neighbour.name + " with score " +neighbour.score);
	        					System.out.println(":::: outerMap size "+ outerMap.size());
	        				}
	    				
	    			}
	    		}
	    	}
	}
}
