import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;
import org.apache.jena.vocabulary.ReasonerVocabulary;

import java.awt.GridBagLayout;
import java.io.File;
import java.util.List;

import javax.swing.JScrollPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextArea;

public class analysis extends JFrame {
	
	protected static String show="";
	private JPanel contentPane;
	private JTextArea textArea;

	
	public static Boolean isFileExists(String path,int r){
		File file=new File(path);
		if(file.exists())
		{
			return true;
		}
		
		else {
			show=show+"规则"+r+"的程序不存在!!!路径为："+path+"\n";
			//textArea.append(show+"\n");
			return false;
		}
			
	}
	public void tuiLi() {
		 Model model = ModelFactory.createDefaultModel();
		 model.read("file:F:/TestSpace/T0/NewWindowsFirewallPolicyOntology.owl");
		 List rules = Rule.rulesFromURL("file:F:/TestSpace/T0/WFP.rules");

		 GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		 reasoner.setOWLTranslation(true);
		 reasoner.setDerivationLogging(true);
		 reasoner.setTransitiveClosureCaching(true);
		 OntModel om = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_RULE_INF,  //这里使用OWL_DL_MEM效果也一样
		     model);
		 Resource configuration = om.createResource();
		 configuration.addProperty(ReasonerVocabulary.PROPruleMode, "hybrid");

		 InfModel inf = ModelFactory.createInfModel(reasoner, om);
		 //StmtIterator stmtIter = inf.listStatements(a, null, b);
	}

	/**
	 * Create the frame.
	 */
	public analysis() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("结果显示");
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.append(show);
		scrollPane.setViewportView(textArea);
	}

}
