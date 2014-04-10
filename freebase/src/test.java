


public class test {
 
	public static void main(String[] args) throws Exception {
		Node node = new Node();
		node.getInfo("smallville");
		System.out.println(node.toString());
		node.getAdjacentNodes();
		System.out.println("=========================");
		System.out.println(node.toString());
		System.out.println("=========== nodeX ==============");
		Node nodeX = node.AdjacentNodes.get(0);
		nodeX.getAdjacentNodes();
		System.out.println(nodeX.toString());
	}
	
}