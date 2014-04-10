package misc;
import org.openrdf.model.URI;

import slib.sglib.io.conf.GDataConf;
import slib.sglib.io.loader.GraphLoaderGeneric;
import slib.sglib.io.util.GFormat;
import slib.sglib.model.graph.G;
import slib.sglib.model.impl.graph.memory.GraphMemory;
import slib.sglib.model.impl.repo.URIFactoryMemory;
import slib.sglib.model.repo.URIFactory;
import slib.sml.sm.core.engine.SM_Engine;
import slib.sml.sm.core.metrics.ic.utils.IC_Conf_Topo;
import slib.sml.sm.core.metrics.ic.utils.ICconf;
import slib.sml.sm.core.utils.SMConstants;
import slib.sml.sm.core.utils.SMconf;
import slib.utils.ex.SLIB_Ex_Critic;


public class simTest {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		
		URIFactory factory = URIFactoryMemory.getSingleton();
        URI graph_uri = factory.createURI("http://graph/");
        G graph = new GraphMemory(graph_uri);
        String fpath = System.getProperty("user.dir")+"/src/resources/graph_test.nt";
        GDataConf graphconf = new GDataConf(GFormat.NTRIPLES, fpath);
        GraphLoaderGeneric.populate(graphconf, graph);
        SM_Engine engine = new SM_Engine(graph);
		
		
		 // First we define the information content (IC) we will use
        ICconf icConf = new IC_Conf_Topo("Sanchez", SMConstants.FLAG_ICI_SANCHEZ_2011);
        
        // Then we define the Semantic measure configuration
        SMconf smConf = new SMconf("Lin", SMConstants.FLAG_SIM_PAIRWISE_DAG_NODE_LIN_1998);
        smConf.setICconf(icConf);
        
        // Finally, we compute the similarity between the concepts Horse and Whale
        URI horse = factory.createURI("http://graph/class/Horse");
        URI whale = factory.createURI("http://graph/class/Whale");
        double sim = engine.computePairwiseSim(smConf, whale, horse);
        System.out.println("Sim Whale/Horse: "+sim);
        System.out.println("Sim Horse/Horse: "+engine.computePairwiseSim(smConf, horse, horse));
	}

}
