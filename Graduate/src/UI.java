import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class UI extends JFrame {

	/**
	 * 
	 */
	private JPanel contentPane;
	private JTextField txtPath;
	private JTable table;
	// 读写本体的静态变量
	// static FileOutputStream fos;
	protected static OntModel m;

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public void fromFile(File file) throws IOException {// 文件读取
		long startTime = System.currentTimeMillis();
		long midTime = 0, endTime;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File("src/owlAndRules/NewWindowsFirewallPolicyOntology.owl"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		m = ModelFactory.createOntologyModel();
		m.read("src/owlAndRules/WindowsFirewallPolicyOntology.owl");
		DefaultTableModel dtm = new DefaultTableModel();
		BufferedReader br = new BufferedReader(reader);
		String line = br.readLine();
		String[] head = { "编号", "名称","配置域" , "操作", "程序", "本地地址", "远程地址", "协议", "本地端口", "远程端口" };
		dtm.setColumnIdentifiers(head);
		while (line != null) {
			line = br.readLine(); //
			if (line != null) {
				String[] row = null;
				if(line.contains("\t"))
					row = line.split("\t");
				else 
					row = line.split(",");
				try {
					policy p = new policy(row);
					p.createOntology(m);
					dtm.addRow(p.tableRow());
				}catch(Exception e){
					JOptionPane.showMessageDialog(null, "请载入正确的规则列表", "提示", JOptionPane.ERROR_MESSAGE);
					dtm=new DefaultTableModel();
					policy.xiGou();
					break;
				}	
			} else {
				midTime = System.currentTimeMillis();
				RDFWriter rdfWriter = m.getWriter("RDF/XML");
				rdfWriter.write(m, fos, "RDF/XML");
				policy.xiGou();
			}
		}
		table.setModel(dtm);
		//释放三个操作流
		br.close();fos.close();reader.close();
		endTime = System.currentTimeMillis();
		float seconds0 = (endTime - startTime) / 1000F, seconds1 = (midTime - startTime) / 1000F;
		System.out.println("本体生成总用时：" + Float.toString(seconds0) + "seconds \n本体操作：" + Float.toString(seconds1) + "seconds");
		System.out.println("文件操作："+Float.toString(seconds0-seconds1) +"seconds");
	}

	/**
	 * Create the frame.
	 */
	public UI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("安全策略分析工具");
		setBounds(100, 100, 800, 600);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton("载入");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				analysis.show_process = "";
				analysis.show_sql = "";
				JFileChooser chooser = new JFileChooser("C:\\Users\\74330\\Desktop\\毕业设计\\聂冠雄"); // 创建选择文件对象
				chooser.setDialogTitle("请选择文件");// 设置标题
				chooser.setMultiSelectionEnabled(true); // 设置只能选择文件
				FileNameExtensionFilter filter = new FileNameExtensionFilter("txt,csv", "txt","csv");// 定义可选择文件类型
				chooser.setFileFilter(filter); // 设置可选择文件类型
				chooser.showOpenDialog(null); // 打开选择文件对话框,null可设置为你当前的窗口JFrame或Frame
				try {
					File file = chooser.getSelectedFile();
					txtPath.setText(file.getPath());
					fromFile(file);
				}
				catch (Exception e) {
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
				if (table.getRowCount() == 0) {
					JOptionPane.showMessageDialog(null, "请载入有效的规则文件", "警告", JOptionPane.ERROR_MESSAGE);
				} else {
					analysis.show_sql = "";
					EventQueue.invokeLater(new Runnable() {// 显示分析结果
						public void run() {
							try {
								analysis frame = new analysis();
								frame.setVisible(true);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(null,e.getMessage(),"提示", JOptionPane.ERROR_MESSAGE);
							}
						}
					});
				}
			}
		});
		button.setBounds(256, 0, 93, 36);
		contentPane.add(button);

		txtPath = new JTextField();
		txtPath.setText("NULL");
		txtPath.setBounds(103, 0, 143, 36);
		contentPane.add(txtPath);
		txtPath.setColumns(10);
		txtPath.setEditable(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 46, 764, 505);
		contentPane.add(scrollPane);
		table = new JTable() {
			public String getToolTipText(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				int col = table.columnAtPoint(e.getPoint());
				String tiptextString = null;
				if (row > -1 && col > -1) {
					Object value = table.getValueAt(row, col);
					if (null != value && !"".equals(value))
						tiptextString = value.toString();// 悬浮显示单元格内容
				}
				return tiptextString;
			}
		};
		// table = new JTable();
		table.setAutoCreateRowSorter(true);
		scrollPane.setViewportView(table);
	}
}
