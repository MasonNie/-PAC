import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter; 
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
public class test {
/*	Individual host1;//本地主机或网络
	Individual host2;//远程主机或网络
	Individual process1;//本地进程
	Individual process2;//远程进程
	Individual control1;//控制
	//Individual channeloperate1;//协议操作
	//Individual rule1;//规则
	//OntModel m;//本体

	public static void creatRule(string Name,string UserGroup,string ConfigurationFiled,string OnOrOff,string Operation, LocalAddress,string LocalProgram,string LocalPort,string Protocol,RemoteAddress,string RemoteProgram,string RemotePort,string PermitedUser, string PermitedComputer )
	{
		String filePath = "d:\\ONTOLOGY\\NewNetworkAccessControlOntology.owl";
		File f = new File(filePath);
		FileOutputStream file = null;
		try 
		{
			file = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OntModel m = ModelFactory.createOntologyModel();
		m.read("d:\\ONTOLOGY\\NetworkAccessControlOntology.owl");
		OntModel m1 = ModelFactory.createOntologyModel();
		m1.read("d:\\ONTOLOGY\\NetworkAccessControlOntology.owl");
		String NS="http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#";
		String queryString;
		if (LocalAddress不带掩码&&(LocalAddress!="任何"))
		{
			OntClass host = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Host");
			i++;
			Individual host1=host.createIndividual(NS+"Host"+i);//注意i从1开始递加，要定义成全局变量，因为每新生成一个新的主机节点，都要加1.
			DatatypeProperty obp=m.getDatatypeProperty(NS+"ipAddressIs");
			m1.add(host1,obp,LocalAddress);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			queryString = "SELECT ?x WHERE { ?x :ipAddressIs  LocalAddress }";
		}
		if (LocalAddress带掩码)
		{
			OntClass network = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Network");
			j++;
			Individual network1=network1.createIndividual(NS+"Networkj");//注意j从1开始递加，要定义成全局变量，因为每新生成一个新的网络节点，都要加1.
			DatatypeProperty obp1=m1.getDatatypeProperty(NS+"networkNumberIs");
			m1.add(host1,obp1,LocalAddress的网络号部分);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			obp1=m1.getObjectProperty(NS+"maskIs");
			m1.add(host1,obp1,LocalAddress的网络号部分);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			queryString = "SELECT ?x WHERE { ?x :networkNumberIs  LocalAddress的网络号部分.
							?x :maskIs  LocalAddress的网络号部分. }";
		}
		Query query = QueryFactory.create(queryString);
 		QueryExecution qe = QueryExecutionFactory.create(query, m);
 		ResultSet results = qe.execSelect();
		if (results!=Null)
		{
			RDFWriter rdfWriter= m.getWriter("RDF/XML");
			rdfWriter.write(m1, file, "RDF/XML");	
		}
		if ((LocalProgram!="任何")||（LocalPort!="任何"）)
		{
			OntClass process = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Process");
			k++;
			Individual process1=network1.createIndividual(NS+"Processk");//注意k从1开始递加，要定义成全局变量，因为每新生成一个新的网络节点，都要加1.
			ObjectProperty obp1=m1.getObjectProperty(NS+"runningOn");
			m1.add(process1,obp1,host1);	
		}
		if (LocalProgram!="任何")
		{
			DatatypeProperty obp1=m1.getDatatypeProperty(NS+"programPathIs");
			m1.add(host1,process1,LocalProgram);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
		}
		if (LocalPort!="任何")
		{
			DatatypeProperty obp1=m1.getDatatypeProperty(NS+"portIs");
			m1.add(host1,process1,LocalPort);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
		}

		if ((LocalProgram!="任何")&&(LocalPort=="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :programPathIs LocalProgram. ?x :runningon host1.}";
		}
		if ((LocalProgram=="任何")&&(LocalPort!="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :portIs LocalPort. ?x :runningon host1. }";
		}
		if ((LocalProgram=="任何")&&(LocalPort!="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :programPathIs LocalProgram.
						?x :portIs LocalPort. ?x :runningon host1. }";
		}
		if((LocalProgram==!"任何")||(LocalPort!="任何"))
		{
			Query query = QueryFactory.create(queryString);
 			QueryExecution qe = QueryExecutionFactory.create(query, m);
 			ResultSet results = qe.execSelect();
			if (results!=Null)
			{
				RDFWriter rdfWriter= m1.getWriter("RDF/XML");
				rdfWriter.write(m1, file, "RDF/XML");	
			}
		}


		//创建远程节点和掩码
		if (RemoteAddress不带掩码&&(RemoteAddress!="任何"))
		{
			OntClass host = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Host");
			i++;
			Individual host2=host.createIndividual(NS+"Hosti");//注意i从1开始递加，要定义成全局变量，因为每新生成一个新的主机节点，都要加1.
			DatatypeProperty obp=m.getDatatypeProperty(NS+"ipAddressIs");
			m1.add(host2,obp,LocalAddress);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			queryString = "SELECT ?x WHERE { ?x :ipAddressIs  RemoteAddress }";
		}
		if (RemoteAddress带掩码)
		{
			OntClass network = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Network");
			j++;
			Individual network2=network.createIndividual(NS+"Networkj");//注意j从1开始递加，要定义成全局变量，因为每新生成一个新的网络节点，都要加1.
			DatatypeProperty obp1=m1.getDatatypeProperty(NS+"networkNumberIs");
			m1.add(network2,obp1,RemoteAddress的网络号部分);//不确定RemoteAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			obp1=m1.getObjectProperty(NS+"maskIs");
			m1.add(network2,obp1,RemoteAddress的网络号部分);//不确定RemoteAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
			queryString = "SELECT ?x WHERE { ?x :networkNumberIs  RemoteAddress的网络号部分.
							?x :maskIs  RemoteAddress的网络号部分. }";
		}
		Query query = QueryFactory.create(queryString);
 		QueryExecution qe = QueryExecutionFactory.create(query, m);
 		ResultSet results = qe.execSelect();
		if (results!=Null)
		{
			RDFWriter rdfWriter= m1.getWriter("RDF/XML");
			rdfWriter.write(m1, file, "RDF/XML");	
		}
		if ((RemoteProgram!="任何")||（RemotePort!="任何"）)
		{
			OntClass process = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Process");
			k++;
			Individual process2=process.createIndividual(NS+"Processk");//注意k从1开始递加，要定义成全局变量，因为每新生成一个新的网络节点，都要加1.
			ObjectProperty obp1=m1.getObjectProperty(NS+"runningOn");
			m1.add(process2,obp1,host2);	
		}
		if (RemoteProgram!="任何")
		{
			DatatypeProperty obp1=m.getDatatypeProperty(NS+"programPathIs");
			m1.add(host2,process2,RemoteProgram);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
		}
		if (RemotePort!="任何")
		{
			DatatypeProperty obp1=m.getDatatypeProperty(NS+"portIs");
			m1.add(host2,process1,RemotePort);//不确定LocalAddress这个参数这样写对不对，要确认一下add()这个函数的接口。
		}

		if ((RemoteProgram!="任何")&&(RemotePort=="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :programPathIs LocalProgram. ?x :runningon host1.}";
		}
		if ((RemoteProgram=="任何")&&(RemotePort!="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :portIs LocalPort. ?x :runningon host1. }";
		}
		if ((RemoteProgram=="任何")&&(RemotePort!="任何"))
		{
				queryString = "SELECT ?x WHERE { ?x :programPathIs LocalProgram.?x :portIs LocalPort. ?x :runningon host1. }";
		}
		if((RemoteProgram==!"任何")||(RemotePort!="任何"))
		{
			Query query = QueryFactory.create(queryString);
 			QueryExecution qe = QueryExecutionFactory.create(query, m);
 			ResultSet results = qe.execSelect();
			if (results!=null)
			{
				RDFWriter rdfWriter1= m.getWriter("RDF/XML");
				rdfWriter1.write(m, file, "RDF/XML");	
			}
		}

		
		//创建操作个体
		OntModel m1 = ModelFactory.createOntologyModel();
		OntClass ChannelOperate = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#ChannelOperate");
		l++;
		Individual channeloperate1=rule.createIndividual(NS+"ChannelOperatem");//注意m从1开始递加，要定义成全局变量，因为每新生成一个新的节点，都要加1.
		DatatypeProperty obp1=m1.getDatatypeProperty(NS+"protocolIs");
		m1.add(channeloperate1,obp1,protocol);
		queryString = "SELECT ?x WHERE { ?x :protocolIs LocalProgram.}";
		Query query = QueryFactory.create(queryString);
 		QueryExecution qe = QueryExecutionFactory.create(query, m);
 		ResultSet results = qe.execSelect();
		if (results!=Null)
		{
			RDFWriter rdfWriter= m1.getWriter("RDF/XML");
			rdfWriter.write(m1, file, "RDF/XML");	
		}	


		//创建控制个体
  		if(control=="安全连接")
		{	
			queryString = "SELECT ?x WHERE { ?x :protocolIs LocalProgram.}";
			Query query = QueryFactory.create(queryString);
 			QueryExecution qe = QueryExecutionFactory.create(query, m);
 			ResultSet results = qe.execSelect();
			if (results==Null)
			{
				OntModel m1 = ModelFactory.createOntologyModel();
				OntClass Control = m.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Control");
				n++;
				Individual control1=rule.createIndividual(NS+"Controln");//注意n从1开始递加，要定义成全局变量，因为每新生成一个新的节点，都要加1.
				DatatypeProperty obp1=m.getDatatypeProperty(NS+"userNameIs");
				m1.add(channeloperate1,obp1,UserName);
				DatatypeProperty obp1=m.getDatatypeProperty(NS+"computerIs");
				m1.add(channeloperate1,obp1,Computer);
				RDFWriter rdfWriter= m.getWriter("RDF/XML");
				rdfWriter.write(m, file, "RDF/XML");	
			}
		}	

		//创建规则个体
		OntModel m1 = ModelFactory.createOntologyModel();
		OntClass rule = m1.getOntClass("http://www.semanticweb.org/administrator/ontologies/2018/3/untitled-ontology-27#Rule");
		l++;
		Individual rule1=rule.createIndividual(NS+"Rulel");//注意l从1开始递加，要定义成全局变量，因为每新生成一个新的网络节点，都要加1.
		ObjectProperty obp1=m1.getObjectProperty(NS+"isSubjectOf");	
		if (LocalAddress不带掩码&&(RemoteAddress!="任何")&&(RemoteProgram=="任何")&&(RemotePort=="任何"))
		{
			m1.add(host2,obp1,rule1);	
		}
		else
		{
			if （(LocalAddress=="任何")&&(RemoteProgram=="任何")&&(RemotePort=="任何")）
			{
				Individual entityall=m.getIndividual( NS + "AllEntities" );
				m1.add(entityall,obp1,rule1);	
				
			}
			if （(LocalAddress带掩码)&&(RemoteProgram=="任何")&&(RemotePort=="任何")）
			{
				m1.add(network2,obp1,rule1);
			}
			if ((LocalProgram!=="任何")||(RemotePort!=="任何"))
			{
				m1.add(process2,obp1,rule1);
			}
			
		}
		ObjectProperty obp1=m.getObjectProperty(NS+"isObjectOf");	
		if (RemoteAddress不带掩码&&(RemoteAddress!="任何")&&(RemoteProgram=="任何")&&(RemotePort=="任何"))
		{
			m1.add(host2,obp1,rule1);	
		}
		else
		{
			if （(RemoteAddress=="任何")&&(RemoteProgram=="任何")&&(RemotePort=="任何")）
			{
				Individual entityall=m.getIndividual( NS + "AllEntities" );
				m1.add(entityall,obp1,rule1);	
				
			}
			if （(RemoteAddress带掩码)&&(RemoteProgram=="任何")&&(RemotePort=="任何")）
			{
				m1.add(network2,obp1,rule1);
			}
			if ((RemoteProgram!=="任何")||(RemotePort!=="任何"))
			{
				m1.add(process2,obp1,rule1);
			}
			
		}
		DatatypeProperty obp1=m1.DatatypeProperty(NS+"NameIs");
		m1.add(rule1,obp1,Name);
		DatatypeProperty obp1=m1.DatatypeProperty(NS+"configurationFileIs");
		m1.add(rule1,obp1,ConfigurationFile);
		DatatypeProperty obp1=m1.DatatypeProperty(NS+"userGroupIs");
		m1.add(rule1,obp1,UserGroup);
		DatatypeProperty obp1=m1.DatatypeProperty(NS+"onOrOffIs");
		m1.add(rule1,obp1,OnOrOff);
		RDFWriter rdfWriter= m1.getWriter("RDF/XML");
		rdfWriter.write(m1, file, "RDF/XML");			

	}*/
	}

