import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class policy {
	private static int A=0,H = 0, N = 0, P = 0, CO = 0, C = 0, R = 0;
	private String 名称;
	private String 组;
	private String 配置文件;
	private String 已启用;
	private String 操作;
	private String 替代;
	private String 程序;
	private String 本地地址;
	private String 远程地址;
	private String 协议;
	private String 本地端口;
	private String 远程端口;
	private String 授权的用户;
	private String 授权的计算机;
	private String 授权的本地主体;
	private String 本地用户所有者;

	public policy(String[] str) {
		int i = 0;
		this.名称 = str[i++];
		this.组 = str[i++];
		this.配置文件 = str[i++];
		this.已启用 = str[i++];
		this.操作 = str[i++];
		this.替代 = str[i++];
		this.程序 = str[i++].replaceAll("\\\\", "/");
		this.本地地址 = str[i++];
		this.远程地址 = str[i++];
		this.协议 = str[i++];
		this.本地端口 = str[i++];
		this.远程端口 = str[i++];
		this.授权的用户 = str[i++];
		this.授权的计算机 = str[i++];
		this.授权的本地主体 = str[i++];
		this.本地用户所有者 = str[i++];
		//System.out.println(this.协议);
	}
	public static void xiGou() {
		A=H=N=P=CO=C=R=0;
	}
	public Object[] tableRow() {
		Object [] showRow= {R,this.名称,this.已启用,this.操作,this.程序,this.本地地址,this.远程地址,this.协议,this.本地端口,this.远程端口};
		return showRow;
	}

	public void createOntology(OntModel m) {
		Individual host1 = null;// 本地主机或网络
		Individual host2 = null;// 远程主机或网络
		Individual process1 = null;// 本地进程
		Individual process2 = null;// 远程进程
		Individual control1;// 控制
		Individual channeloperate1 = null;// 协议操作
		Individual rule1;// 规则
		/*
		 * OntModel m = ModelFactory.createOntologyModel();//本体 String filePath =
		 * "F:\\TestSpace\\T0\\"; File f = new
		 * File(filePath+"NewWindowsFirewallPolicyOntology.owl"); FileOutputStream file
		 * = null; try { file = new FileOutputStream(f);//覆盖式的输出流操作，是否会有影响。 } catch
		 * (FileNotFoundException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } m.read(filePath+"WindowsFirewallPolicyOntology.owl");
		 */
		String NS = "http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#";
		String wfp = "PREFIX wfp: <" + NS + ">";// 声明查询语句中的命名空间WindowsFirewallPolicy
		String LocalAddress = this.本地地址;
		String queryString = "";
	// 创建规则个体
		OntClass rule = m.getOntClass(NS + "Rule");
		rule1 = rule.createIndividual(NS + "Rule" + ++R);
		System.out.println("规则计数："+R);
		ObjectProperty obpsub = m.getObjectProperty(NS + "isSubjectOf");
		if (!this.本地地址.contains("/") && (!this.远程地址.equals("任何")) && (this.远程端口.equals("任何"))) {
			OntClass host = m.getOntClass(NS + "Host");
			host2=host.createIndividual(NS+"Host"+ ++H);
			m.add(host2, obpsub, rule1);
		} 
		else {
			if ((this.本地地址.equals("任何")) && (this.远程端口.equals("任何"))) {
				Individual entityall = m.getIndividual(NS + "AllEntities");
				m.add(entityall, obpsub, rule1);
			}
			if (this.本地地址.contains("/") && (this.远程地址.equals("任何")) && (this.远程端口.equals("任何"))) {
				OntClass network = m.getOntClass(NS + "Network");
				Individual network2 = network.createIndividual(NS + "Network" + ++N);
				m.add(network2, obpsub, rule1);
			}
			if ((!this.程序.equals("任何")) || (!this.远程端口.equals("任何"))) {
				//m.add(process2, obp11, rule1);
			}
		}
		//创建网络域
		if (!LocalAddress.contains("/") && !LocalAddress.equals("任何")) {
			OntClass host = m.getOntClass(NS + "Host");
			DatatypeProperty obp = m.getDatatypeProperty(NS + "ipAddressIs");
			queryString =wfp+ "SELECT ?x WHERE { ?x wfp:ipAddressIs ?y\r\n"+
			                  ".FILTER regex(?y,(\"" + LocalAddress + "\") }";
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			if (!results.hasNext()) {
				host1 = host.createIndividual(NS + "Host" + H++);
				m.add(host1, obp, LocalAddress);
			}

		}
		if (LocalAddress.contains("/"))// 有掩码
		{
			OntClass host = m.getOntClass(NS + "Host");
			OntClass network = m.getOntClass(NS + "Network");
			String netNumber = LocalAddress.substring(0, LocalAddress.indexOf("/"));// 网络号
			int netmask=Integer.parseInt(LocalAddress.substring(LocalAddress.indexOf("/")+1));
			Individual network1 = network.createIndividual(NS + "Network" + N++);// 
			DatatypeProperty obp1 = m.getDatatypeProperty(NS + "networkNumberIs");
			obp1 = m.getDatatypeProperty(NS + "maskIs");
			queryString = wfp+"SELECT ?x WHERE { ?x wfp:networkNumberIs \"" + netNumber + 
					          "\".?x wfp:maskIs \"" + netmask + "\"}";//有问题
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			if (!results.hasNext()) {
				host1 = host.createIndividual(NS + "Host" + H++);
				m.add(host1, obp1, netNumber);
			}
				
		}

		/*
		 * if ((this.程序!="任何")&&(this.本地端口!="任何")) { OntClass process =
		 * m.getOntClass(NS+"Process");
		 * process1=process.createIndividual(NS+"Process"+P++);//
		 * 因为每新生成一个新的网络节点，都要加1. ObjectProperty obp1=m.getObjectProperty(NS+"runningOn");
		 * m.add(process1,obp1,host1); }
		 */

		if (!this.程序.equals("任何")) {
			OntClass process = m.getOntClass(NS + "Process");
			ObjectProperty obp1 = m.getObjectProperty(NS + "runningOn");
			DatatypeProperty dtp1 = m.getDatatypeProperty(NS + "programPathIs");
			if (host1 == null) {
				Individual entityall = m.getIndividual(NS + "AllEntities");
				queryString = wfp + "SELECT ?x WHERE { ?x wfp:programPathIs ?y"+
						".?x wfp:runningOn wfp:AllEntities \r\n"+
						".FILTER regex(?y,\""+this.程序+"\")}";
				Query query = QueryFactory.create(queryString);
				QueryExecution qe = QueryExecutionFactory.create(query, m);
				ResultSet results = qe.execSelect();
				if (!results.hasNext()) {
					process1 = process.createIndividual(NS + "Process" + ++P);
					m.add(process1, obp1, entityall);
					m.add(process1, dtp1, this.程序);
				}
			} else {
				queryString = wfp + " SELECT ?x WHERE { ?x wfp:programPathIs ?y" + 
						 ".?x wfp:runningOn wfp:Host" + H + "\r\n"+
						 ".FILTER regex(?y,\""+this.程序+"\")}";
				Query query = QueryFactory.create(queryString);
				QueryExecution qe = QueryExecutionFactory.create(query, m);
				ResultSet results = qe.execSelect();
				if (!results.hasNext()) {
					process1 = process.createIndividual(NS + "Process" + ++P);
					m.add(process1, obp1, host1);
				}

			}
		}
		if (!this.本地端口.equals("任何")) {
			/*
			 * OntClass process = m.getOntClass(NS+"Process");
			 * process1=process.createIndividual(NS+"Process"+ ++P);
			 */
			DatatypeProperty obp1 = m.getDatatypeProperty(NS + "portIs");
			if (host1 == null) {
				// queryString = wfp+" SELECT ?x WHERE { ?x wfp:portIs \""+this.本地端口+"\".?x
				// wfp:runningon \"AllEntities\"}";

				Individual entityall = m.getIndividual(NS + "AllEntities");
				m.add(entityall, obp1, this.本地端口);// host1没有初始化，不能使用，使用预定义的entityall代替host
			} else
				m.add(host1, obp1, this.本地端口);
		}
		/*
		 * if((!this.程序.equals("任何"))||(!this.本地端口.equals("任何"))) {
		 * if(!queryString.isEmpty()) {
		 * //!(queryString.isEmpty()||queryString==null)非空也不是null Query query =
		 * QueryFactory.create(queryString); QueryExecution qe =
		 * QueryExecutionFactory.create(query, m); ResultSet results = qe.execSelect();
		 * if (results!=null) { RDFWriter rdfWriter= m.getWriter("RDF/XML");
		 * rdfWriter.write(m, file, "RDF/XML"); } } }
		 */
		// 创建操作个体

		OntClass ChannelOperate = m.getOntClass(NS + "ChannelOperate");
		DatatypeProperty obp1 = m.getDatatypeProperty(NS + "protocolIs");
		ObjectProperty obpOp=m.getObjectProperty(NS+"isOperateOf");
		queryString = wfp + "SELECT ?x WHERE { ?x wfp:protocolIs ?y\r\n"+
		                    ".FILTER regex(?y,\"" + this.协议 + "\")}";
		if (!queryString.isEmpty()) {
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			if (!results.hasNext()) {
				channeloperate1 = ChannelOperate.createIndividual(NS + "ChannelOperate" + ++CO);
				m.add(channeloperate1, obp1, this.协议);
				m.add(channeloperate1, obpOp, rule1);
			}
		}
		
		//创建Access关系，将进程和实体与规则绑定起来
		boolean en=host1==null;
		String entity=en?"AllEntities":"Host"+H;
		OntClass access = m.getOntClass(NS + "Access");
		queryString=wfp+"SELECT ?x WHERE {?x wfp:isObjectOf ?y. ?z wfp:isSubjectOf ?y\r\n"
				+ ".FILTER regex(?x,\""+this.程序+"\")\r\n"
				+ ".FILTER (?z=wfp:"+entity+")}";
		if (!queryString.isEmpty()) {
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			ObjectProperty obpAces = m.getObjectProperty(NS + "isAccessOf");
			if (!results.hasNext()) {
				Individual access1=access.createIndividual(NS+"Access"+ ++A);
				ObjectProperty obpOb = m.getObjectProperty(NS + "isObjectOf");
				ObjectProperty obpSub = m.getObjectProperty(NS + "isSubjectOf");
				if(!this.程序.isEmpty()&&this.equals("任何")) {
					if(process1==null) {
					Query query1 = QueryFactory.create(wfp+"SELECT ?x WHERE{?x wfp:programPathIs ?y \r\n"
							+ ".FILTER regex(?y,\""+this.程序+"\")}");
					QueryExecution qe1 = QueryExecutionFactory.create(query1, m);
					ResultSet results1 = qe1.execSelect();
					QuerySolution QS=results1.nextSolution();
		 		    String x=QS.get("x").toString();
		 		    Individual process_t=m.getIndividual(x);
		 		    m.add(process_t, obpOb, access1);
					}
					else m.add(process1, obpOb, access1);
		 		    if(host1==null) 
						m.add(m.getIndividual(NS + "AllEntities"), obpSub, access1);
					else 
						m.add(host1, obpSub, access1);
				}
				m.add(access1, obpAces, rule1);
			}
			else {
				QuerySolution result=results.nextSolution();
	 		    String x=result.get("x").toString();
	 		    Individual access1=m.getIndividual(x);
	 		    m.add(access1, obpAces, rule1);
			}
		}
		
		ObjectProperty obp111 = m.getObjectProperty(NS + "isObjectOf");
		if (this.远程地址.contains("/") && (!this.远程地址.equals("任何")) && (this.远程端口.equals("任何"))) {
			m.add(host2, obp111, rule1);
		} else {
			if ((this.远程地址 == "任何") && (this.远程端口 == "任何")) {
				Individual entityall = m.getIndividual(NS + "AllEntities");
				m.add(entityall, obp111, rule1);

			}
			if ((this.远程地址.contains("/")) && (this.远程端口 == "任何")) {
				OntClass network = m.getOntClass(NS + "Network");
				Individual network2 = network.createIndividual(NS + "Network" + ++N);
				m.add(network2, obp111, rule1);
			}
			/*if ((!this.远程端口.equals("任何"))) {
				m.add(process2, obp111, rule1);
			}*/
		}
		DatatypeProperty dtp1 = m.getDatatypeProperty(NS + "nameIs");
		m.add(rule1, dtp1, this.名称);
		dtp1 = m.getDatatypeProperty(NS + "configurationFileIs");
		m.add(rule1, dtp1, this.配置文件);
		dtp1 = m.getDatatypeProperty(NS + "userGroupIs");
		m.add(rule1, dtp1, this.本地用户所有者);
		dtp1 = m.getDatatypeProperty(NS + "onOrOffIs");
		m.add(rule1, dtp1, this.已启用);

		// 创建控制个体
		if (this.操作 == "安全连接") {
			queryString = wfp + "SELECT ?x WHERE { ?x wfp:protocolIs ?y\r\n"+
		                        ".FILTER regex(?y,\"" + this.协议 + "\").}";
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			if (!results.hasNext()) {
				OntClass Control = m.getOntClass(
						"http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Control");
				control1 = Control.createIndividual(NS + "Control" + ++C);// 注意n从1开始递加，要定义成全局变量，因为每新生成一个新的节点，都要加1.
				DatatypeProperty obp2 = m.getDatatypeProperty(NS + "userNameIs");
				m.add(channeloperate1, obp2, this.授权的用户);
				obp1 = m.getDatatypeProperty(NS + "computerIs");
				m.add(channeloperate1, obp2, this.授权的计算机);
			}
		}
		else{
			String op=this.操作.equals("允许")?"Permit":"Deny";
			queryString = wfp + "SELECT ?x WHERE { ?x wfp:isControlOf wfp:Rule"+R+ ".}";
			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, m);
			ResultSet results = qe.execSelect();
			if (!results.hasNext()) {
				/*OntClass Control = m.getOntClass(
						"http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#"+op);
				control1 = Control.createIndividual(NS + op + ++C);*/
				control1 = m.getIndividual(NS + op);
				ObjectProperty obp=m.getObjectProperty(NS+"isControlOf");
				m.add(control1, obp, rule1);
			}
		}
	}

}
