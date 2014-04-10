import java.util.HashSet;


public class Graph {

	private HashSet<Graph> childs = new HashSet<Graph>();
	private Node rootNode;
	
	// initialization of graph
	public Graph(Node node) {
		this.rootNode = node;
	}
	
	// get connected graphs/nodes for iteration
	public HashSet<Graph> getChildSet() {
		return childs;
	}
	
	/* Adding graph to graph */
	public void addChild(Graph g) {
		childs.add(g);
	}
	
	// Adding Node to graph
	public void addChild(Node node){
		Graph g = new Graph(node);
		childs.add(g);
	}
	
	public Node getRootNode(){
		return rootNode;
	}
	
	// returns number sof childs for a graph
	public int count() {
		return childs.size();
	}
	
	public String toString(){
		String temp = rootNode.mid+" => {";
		for(Graph child : childs){
			temp += child.toString()+", ";
		}
		temp += "}";
		return temp;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
