import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
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
	
	protected static String show_process="";
	protected static String show_sql="";
	private JPanel contentPane;
	private JTextArea textArea;

	
	public static Boolean isFileExists(String path,int r){
		File file=new File(path);
		if(file.exists())
		{
			return true;
		}
		
		else {
			show_process=show_process+"规则"+r+"的程序路径无效!!!路径为："+path+"\n";
			//textArea.append(show+"\n");
			return false;
		}
			
	}
	public void fenXi() {//简易查询型推理
		OntModel om = ModelFactory.createOntologyModel();
		om.read("src/owlAndRules/NewWindowsFirewallPolicyOntology.owl");//读取模型
		String NS = "http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#";//命名空间
		String wfp = "PREFIX wfp: <" + NS + ">";//SPARQL命名空间
		//冲突推理
		String queryString = wfp + "SELECT ?rule1 ?rule2 \r\n" +   
				" WHERE { ?a wfp:isAccessOf ?rule1.?a wfp:isAccessOf ?rule2\r\n" + 
				".?rule1 wfp:configurationFileIs ?cfgFile.?rule2 wfp:configurationFileIs ?cfgFile\r\n"+
				".?op wfp:isOperateOf ?rule1.?op wfp:isOperateOf ?rule2\r\n" + 
				".?con1 wfp:isControlOf ?rule1.?con2 wfp:isControlOf ?rule2\r\n"+
                ".FILTER (?rule1!=?rule2).FILTER (?con1!=?con2)}"; 
		Query query = QueryFactory.create(queryString);//推理配置域相同，程序和作用域相同，协议相同，操作不同的冲突
		QueryExecution qe = QueryExecutionFactory.create(query, om);
		ResultSet results = qe.execSelect();
		while(results.hasNext()) {//循环整合输出结果
			QuerySolution QS=results.next();
			String rule1=QS.get("rule1").toString().replace(NS, ""),
				   rule2=QS.get("rule2").toString().replaceAll(NS, "");
			//过滤输出类似Rule1与Rule2冲突。Rule2与Rule1冲突的输出
			if(Integer.parseInt(rule1.replace("Rule", ""))<Integer.parseInt(rule2.replace("Rule", ""))) {
				show_sql=show_sql+"冲突："+rule1+"，"+rule2+";冲突类型：操作冲突\n";
				System.out.println("冲突："+rule1+","+rule2);
			}
		}
		//端口范围性冲突推理
		 queryString = wfp + "SELECT  ?rule1 ?rule2\r\n" + 
		 		"WHERE\r\n" + 
		 		"  { ?a1   wfp:isAccessOf ?rule1 .?p1 wfp:isObjectOf ?a1.\r\n" + 
		 		"     ?a2 wfp:isAccessOf ?rule2 .?p2 wfp:isObjectOf ?a2. \r\n" + 
		 		"    ?rule1  wfp:configurationFileIs  ?cfgFile .\r\n" + 
		 		"    ?rule2  wfp:configurationFileIs  ?cfgFile .\r\n" + 
		 		"    ?op     wfp:isOperateOf ?rule1 ; wfp:isOperateOf ?rule2 .\r\n" + 
		 		"    ?con1   wfp:isControlOf ?rule1 .\r\n" + 
		 		"    ?con2   wfp:isControlOf ?rule2.\r\n" + 
		 		" ?p1 wfp:portIsNotLessThan ?s1 ;wfp:portIsNotLargerThan ?b1 .\r\n" + 
		 		" ?p2 wfp:portIsNotLessThan ?s2 ; wfp:portIsNotLargerThan ?b2 .\r\n" + 
		 		"    FILTER ( ?b1>=?s2 ) FILTER ( ?b2>=?s1) FILTER (?p1!=?p2)\r\n" + 
		 		"    FILTER ( ?rule1 != ?rule2 ) FILTER ( ?con1 != ?con2 )\r\n" + 
		 		"  }"; 
		//推理两程序中端口号范围关系重叠的，R1：[S1,B1],R2[S2,B2];!(B1<S2||B2<S1)
		 query = QueryFactory.create(queryString);//推理配置域相同，程序和作用域相同，协议相同，操作不同的冲突
		 qe = QueryExecutionFactory.create(query, om);
		 results = qe.execSelect();
		while(results.hasNext()) {//循环整合输出结果
			QuerySolution QS=results.next();
			String rule1=QS.get("rule1").toString().replace(NS, ""),rule2=QS.get("rule2").toString().replaceAll(NS, "");
			//过滤输出类似Rule1与Rule2冲突。Rule2与Rule1冲突的输出
			if(Integer.parseInt(rule1.replace("Rule", ""))<Integer.parseInt(rule2.replace("Rule", ""))) {
				show_sql=show_sql+"冲突："+rule1+"，"+rule2+";冲突类型：范围重叠冲突\n";
				System.out.println("冲突："+rule1+","+rule2);
			}
		}
		//冗余推理
		queryString=wfp+"SELECT ?rule1 ?rule2 \r\n" + 
				"WHERE { ?a wfp:isAccessOf ?rule1.?a wfp:isAccessOf ?rule2\r\n" + 
				".?rule1 wfp:configurationFileIs ?cfgFile.?rule2 wfp:configurationFileIs ?cfgFile\r\n"+
				".?op wfp:isOperateOf ?rule1.?op wfp:isOperateOf ?rule2\r\n" + 
				".?con wfp:isControlOf ?rule1.?con wfp:isControlOf ?rule2\r\n" + 
				".FILTER (?rule1!=?rule2)}";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, om);
		results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution QS=results.next();
			String rule1=QS.get("rule1").toString().replace(NS, ""),rule2=QS.get("rule2").toString().replaceAll(NS, "");
			//过滤输出类似Rule1与Rule2冲突。Rule2与Rule1的输出
			if(Integer.parseInt(rule1.replace("Rule", ""))<Integer.parseInt(rule2.replace("Rule", ""))) {
				show_sql=show_sql+"冗余："+rule1+"，"+rule2+";规则冗余\n";
				System.out.println("冗余："+rule1+","+rule2);
			}
		}
		//范围性冗余推理
		queryString=wfp+"SELECT  ?rule1 ?rule2\r\n" + 
		 		"WHERE\r\n" + 
		 		"  { ?a1   wfp:isAccessOf ?rule1 .?p1 wfp:isObjectOf ?a1.\r\n" + 
		 		"    ?a2 wfp:isAccessOf ?rule2 .?p2 wfp:isObjectOf ?a2. \r\n" + 
		 		"    ?rule1  wfp:configurationFileIs  ?cfgFile .\r\n" + 
		 		"    ?rule2  wfp:configurationFileIs  ?cfgFile .\r\n" + 
		 		"    ?op     wfp:isOperateOf ?rule1 ; wfp:isOperateOf ?rule2 .\r\n" + 
		 		"    ?con    wfp:isControlOf ?rule1 ; wfp:isControlOf ?rule2.\r\n" + 
		 		" ?p1 wfp:portIsNotLessThan ?s1 ;wfp:portIsNotLargerThan ?b1 .\r\n" + 
		 		" ?p2 wfp:portIsNotLessThan ?s2 ; wfp:portIsNotLargerThan ?b2 .\r\n" + 
		 		"    FILTER ( ?b1>=?s2 ) FILTER ( ?b2>=?s1) FILTER (?p1!=?p2)\r\n" + 
		 		"    FILTER ( ?rule1 != ?rule2 )\r\n" + 
		 		"  }";
		//推理两程序中端口号范围关系重叠的，R1：[S1,B1],R2[S2,B2];!(B1<S2||B2<S1)
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, om);
		results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution QS=results.next();
			String rule1=QS.get("rule1").toString().replace(NS, ""),rule2=QS.get("rule2").toString().replaceAll(NS, "");
			//过滤输出类似Rule1与Rule2冲突。Rule2与Rule1的输出
			if(Integer.parseInt(rule1.replace("Rule", ""))<Integer.parseInt(rule2.replace("Rule", ""))) {
				show_sql=show_sql+"冗余："+rule1+"，"+rule2+";范围重叠冗余\n";
				System.out.println("冗余："+rule1+","+rule2);
			}
		}
		
	}
	/*public void tuiLi() {
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
	}*/

	/**
	 * Create the frame.
	 */
	public analysis() {
		fenXi();//获取分析结果
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setTitle("结果显示");
		setLocationRelativeTo(null);//居中
		//setAlwaysOnTop(true);总在最前
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
		textArea.append(show_process);
		textArea.append(show_sql);
		if((show_sql+show_process).equals("")||(show_sql+show_process).isEmpty())
			textArea.append("无异常");
		scrollPane.setViewportView(textArea);
	}

}
