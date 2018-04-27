import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class UI extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField txtPath;
	//读写本体的静态变量
	//static FileOutputStream fos;
	protected static OntModel m;

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public void fromFile(File file) throws IOException  {//文件读取
		long startTime=System.currentTimeMillis();
		long endTime;
		FileOutputStream fos=null;
        try {
			fos = new FileOutputStream(new File("F:\\TestSpace\\T0\\NewWindowsFirewallPolicyOntology.owl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        InputStreamReader reader= new InputStreamReader(new FileInputStream(file));
		m = ModelFactory.createOntologyModel();
		m.read("F:/TestSpace\\T0\\WindowsFirewallPolicyOntology.owl");
        BufferedReader br = new BufferedReader(reader) ;
        String line = br.readLine();
        while (line != null) {
        	line = br.readLine(); //
        	if(line!=null) {
        		String[] str = line.split("\t");
        		policy p = new policy(str);
        		p.createOntology(m);
        	}
        	else {
        		RDFWriter rdfWriter = m.getWriter("RDF/XML");
        		rdfWriter.write(m, fos, "RDF/XML");
        		policy.xiGou();
        	}
             
        }
        br.close();
        endTime=System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println("用时："+Float.toString(seconds)+" seconds");
       
	}
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI frame = new UI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public UI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TEST");
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("载入");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser("C:\\Users\\74330\\Desktop\\毕业设计\\聂冠雄"); //创建选择文件对象
				  chooser.setDialogTitle("请选择文件");//设置标题
				  chooser.setMultiSelectionEnabled(true);  //设置只能选择文件
				  FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");//定义可选择文件类型
				  chooser.setFileFilter(filter); //设置可选择文件类型
				  chooser.showOpenDialog(null); //打开选择文件对话框,null可设置为你当前的窗口JFrame或Frame
				  File file = chooser.getSelectedFile();
				  txtPath.setText(file.getPath());
				  try {
					fromFile(file);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
			}
		});
		btnNewButton.setBounds(0, 0, 93, 36);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("分析");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		button.setBounds(0, 46, 93, 36);
		contentPane.add(button);
		
		txtPath = new JTextField();
		txtPath.setText("NULL");
		txtPath.setBounds(103, 0, 143, 36);
		contentPane.add(txtPath);
		txtPath.setColumns(10);
		txtPath.setEditable(false);
	}
}
