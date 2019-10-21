import javax.swing.*; 
import javax.swing.table.DefaultTableModel;
import java.awt.*; 
import java.net.URL; 
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JLabel;
import java.io.*;

class MiniXCopy_GUI{
	JFrame mainJF;
	JPanel jpa, jpa1, jpa2, 
			jpb, jpb1, jpb2, jpb3,
			jpc, jpc1, jpc2, jpc3;
	JButton buttonA, buttonB, //B部
		buttonC, buttonD;//A部
	JTextArea txtA, txtB;
	JTextField txtC, txtD;
	JTable tableA, tableB, tableC;
	String[] columnNamesA = {"文件名"},    //定义表格列明数组
		columnNamesB, columnNamesC;
        //定义表格数据数组
    String[][] tableValuesA ,
		tableValuesB, tableValuesC;
	DefaultTableModel update_tableA, update_tableB, update_tableC;
	JLabel jla, jlb, jlc;
	Timer time;
	JCheckBox jcb1, jcb2, jcb3;
	void cleanPage(){
		tableValuesA=tableValuesB=tableValuesC=null;
		update_tableC.setDataVector(tableValuesC, null);
		update_tableB.setDataVector(tableValuesB, null);
		update_tableA.setDataVector(tableValuesA, null);
		
		txtA.setText("");
		buttonA.setText("检索");
	}
	void showData(){
		tableValuesA=new String [MiniBackupX.Addaction.size()][1];
		tableValuesB=new String [MiniBackupX.Updateaction.size()][1];
		tableValuesC=new String [MiniBackupX.Deleteaction.size()][1];
		int i;
		String str="新增文件:"+MiniBackupX.Addaction.size()+"个\n"+
					"变动文件:"+MiniBackupX.Updateaction.size()+"个\n"+
					"移除文件:"+MiniBackupX.Deleteaction.size()+"个\n"+
					"涉及总数:"+(MiniBackupX.Deleteaction.size()+MiniBackupX.Updateaction.size()+MiniBackupX.Addaction.size())+"个\n";
		for(i=0;i<MiniBackupX.Addaction.size();i++)
			tableValuesA[i][0]=new String("" + MiniBackupX.Addaction.get(i).sfile.getAbsolutePath());
		update_tableA.setDataVector(tableValuesA, columnNamesA);
		for(i=0;i<MiniBackupX.Updateaction.size();i++)
			tableValuesB[i][0]=new String("" + MiniBackupX.Updateaction.get(i).tfile.getAbsolutePath());
		update_tableB.setDataVector(tableValuesB, columnNamesA);
		for(i=0;i<MiniBackupX.Deleteaction.size();i++)
			tableValuesC[i][0]=new String("" + MiniBackupX.Deleteaction.get(i).tfile.getAbsolutePath());
		update_tableC.setDataVector(tableValuesC, columnNamesA);
		txtA.setText(str);
	}
	String SelectFolder(){
		String str=null;
		JFileChooser jfc=new JFileChooser();
		FileSystemView fsv=FileSystemView.getFileSystemView();
		jfc.setCurrentDirectory(fsv.getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "请选择文件夹");
		
		File file=jfc.getSelectedFile();
		if(file!=null) str=file.getAbsolutePath();
		mainJF.setEnabled(true);
		mainJF.toFront();
		return str;
	}
	void init_size(){
		buttonC.setPreferredSize(new Dimension(70, 70)); buttonD.setPreferredSize(new Dimension(70, 70)); 
		txtB.setPreferredSize(new Dimension(400, 105)); 
		jpa1.setPreferredSize(new Dimension(200, 105)); jpa2.setPreferredSize(new Dimension(200, 105)); 
		jpb3.setPreferredSize(new Dimension(400, 30));
		buttonA.setPreferredSize(new Dimension(160, 80)); buttonB.setPreferredSize(new Dimension(160, 80));
		jpb1.setPreferredSize(new Dimension(550, 180));
		txtA.setPreferredSize(new Dimension(400, 170)); 
		jpb2.setPreferredSize(new Dimension(420, 180));
		jpc1.setPreferredSize(new Dimension(323, 250));
		jpc2.setPreferredSize(new Dimension(323, 250));
		jpc3.setPreferredSize(new Dimension(323, 250));
	}
	void init_structure(){
		mainJF=new JFrame("MiniXBackup"); 
		jpa=new JPanel(); jpa1=new JPanel(); jpa2=new JPanel(); 
		jpb=new JPanel(); jpb1=new JPanel(); jpb2=new JPanel(); jpb3=new JPanel();
		jpc=new JPanel();
		jpc1=new JPanel(); jpc2=new JPanel(); jpc3=new JPanel();
		jla=new JLabel("新增文件", JLabel.CENTER); jlb=new JLabel("变动文件", JLabel.CENTER); jlc=new JLabel("移除文件", JLabel.CENTER);
		Icon img = new ImageIcon("FolderA.png"); //必须是原生的png图片
		Icon img2 = new ImageIcon("FolderB.png");
		buttonA=new JButton("检索"); buttonB=new JButton("复原");
		buttonC=new JButton(img); buttonD=new JButton(img2);
		txtA=new JTextArea(3, 8); txtB=new JTextArea(3, 8);
		txtC=new JTextField(); txtD=new JTextField();
		jcb1=new JCheckBox("备份新增文件");  jcb2=new JCheckBox("更新变动文件"); jcb3=new JCheckBox("同步删除操作");
		
		FlowLayout flawb3=new FlowLayout(); 
		FlowLayout flaw=new FlowLayout(); flaw.setAlignment(FlowLayout.LEFT);
		FlowLayout flawb=new FlowLayout(); flawb.setHgap(85); flawb.setVgap(20);
		FlowLayout flawc=new FlowLayout(); flawc.setAlignment(FlowLayout.LEFT); flawc.setHgap(60); 
		/***** A部 ******/
		jpa.setLayout(flawc); 
		jpa1.setLayout(new BorderLayout()); jpa2.setLayout(new BorderLayout());
		jpa1.add(buttonC, BorderLayout.NORTH); jpa1.add(txtC, BorderLayout.CENTER);
		jpa1.add(new JLabel("源路径"),BorderLayout.WEST);
		jpa2.add(buttonD, BorderLayout.NORTH); jpa2.add(txtD, BorderLayout.CENTER);
		jpa2.add(new JLabel("目标路径"),BorderLayout.WEST);
		txtB.setEditable(false); 
		jpa.add(jpa1); jpa.add(jpa2); jpa.add(txtB);
		/***** B部 ******/
		
		jpb3.setLayout(flawb3);
		jpb3.add(jcb1); jpb3.add(jcb2); jpb3.add(jcb3);
		
		jpb.setLayout(flaw); 
		
		jpb1.setLayout(flawb); 
		jpb1.add(buttonA); jpb1.add(buttonB); jpb1.add(jpb3);
		txtA.setEditable(false); 
		jpb2.add(txtA);
		
		jpb.add(jpb1); jpb.add(jpb2);
		/***** C部 ******/
		jpc.setLayout(flaw); 
		//part C1
		jpc1.setLayout(new BorderLayout());
		update_tableA= new DefaultTableModel(tableValuesA, columnNamesB);
        tableA = new JTable(update_tableA);
		JScrollPane scrollPaneA = new JScrollPane(tableA);
		jpc1.add(jla, BorderLayout.NORTH);
        jpc1.add(scrollPaneA, BorderLayout.CENTER);
		
		//partC2
		jpc2.setLayout(new BorderLayout());
		update_tableB= new DefaultTableModel(tableValuesB, columnNamesB);
        tableB = new JTable(update_tableB);
		JScrollPane scrollPaneB = new JScrollPane(tableB);
		jpc2.add(jlb, BorderLayout.NORTH);
        jpc2.add(scrollPaneB, BorderLayout.CENTER);
		
		//partC3
		jpc3.setLayout(new BorderLayout());
		update_tableC= new DefaultTableModel(tableValuesC, columnNamesB);
        tableC = new JTable(update_tableC);
		JScrollPane scrollPaneC = new JScrollPane(tableC);
		jpc3.add(jlc, BorderLayout.NORTH);
        jpc3.add(scrollPaneC, BorderLayout.CENTER);
		
		jpc.add(jpc1); jpc.add(jpc2); jpc.add(jpc3);
		/****    *****/
		mainJF.add(jpa, BorderLayout.NORTH);
		mainJF.add(jpb, BorderLayout.CENTER);
		mainJF.add(jpc, BorderLayout.SOUTH);
	}
	void init_color(){
		
		jpa.setBackground(new java.awt.Color(70, 110, 90));
		buttonC.setBackground(new java.awt.Color(70, 110, 90)); buttonC.setBorderPainted(false); 
		buttonC.setPressedIcon(new ImageIcon("actionA.png")); //buttonC.setOpaque(false); //设置按钮透明
		buttonD.setBackground(new java.awt.Color(70, 110, 90));buttonD.setBorderPainted(false);
		buttonD.setPressedIcon(new ImageIcon("actionB.png")); //按钮按下时的图标
		txtC.setFont(new java.awt.Font("SimSun", 1, 15)); txtD.setFont(new java.awt.Font("SimSun", 1, 15));
		txtB.setBackground(new java.awt.Color(70, 110, 90)); txtB.setFont(new java.awt.Font("SimSun", 1, 26));
		txtB.setForeground(new java.awt.Color(255, 153, 18));
		jpb.setBackground(new java.awt.Color(70, 110, 90)); 
		//改变字体大小及颜色
		buttonA.setFont(new java.awt.Font("SimSun", 1, 36)); buttonA.setForeground(new java.awt.Color(200, 0, 0));
		buttonB.setFont(new java.awt.Font("SimSun", 1, 36)); buttonB.setForeground(new java.awt.Color(200, 0, 0));
		jpb1.setBackground(new java.awt.Color(255, 165, 90));
		txtA.setBackground(new java.awt.Color(0, 110, 90));  txtA.setFont(new java.awt.Font("SimSun", 1, 22));
		txtA.setForeground(new java.awt.Color(0, 200, 20));
		jpb2.setBackground(new java.awt.Color(255, 165, 90));
		jpc.setBackground(new java.awt.Color(70, 110, 90));
	}
	void init(){
		UIManager.put("Button.select", new java.awt.Color(70, 110, 90));
		init_structure();
		init_size();
		init_color();
		mainJF.setIconImage((new ImageIcon("title.png").getImage()));
		mainJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainJF.setSize(1000, 600); // 设置窗口的大小
		mainJF.setLocationRelativeTo(null);
		mainJF.setVisible(true);
		mainJF.setResizable(false);
	}
	void addListener(){
		class Listener_button implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(time==e.getSource()){
					txtB.setText("\n  "+(new SimpleDateFormat()).format(new Date()));
				}
				else if(buttonA==e.getSource()){
					if(!txtC.getText().equals("") && !txtD.getText().equals("")){
						if(txtC.getText().equals(txtD.getText()))
							JOptionPane.showMessageDialog(null, "路径相同!!!", "TIP", JOptionPane.WARNING_MESSAGE);
						else{
							if(buttonA.getText().equals("备份")){
								String str=txtA.getText();
								int a=0; 
								if(jcb1.isSelected()) a=a | 1;
								if(jcb2.isSelected()) a=a | 2;
								if(jcb3.isSelected()) a=a | 4;
								if((a | 0)==0){
									JOptionPane.showMessageDialog(null, "未选择操作类型", "TIP", JOptionPane.WARNING_MESSAGE);
									
									return ;
								}
								txtA.setText(str+"\n...");
								MiniBackupX.XUpdateUI(mainJF, a);
								buttonA.setText("检索");
								return ;
							}
							File files=new File(txtC.getText()), filet=new File(txtD.getText());
							if(!files.exists())
								JOptionPane.showMessageDialog(null, "源路径无效!!!", "TIP", JOptionPane.WARNING_MESSAGE);
							else if(!filet.exists())
								JOptionPane.showMessageDialog(null, "目标路径无效!!!", "TIP", JOptionPane.WARNING_MESSAGE);
							else if(files.isDirectory() && filet.isDirectory()){
								
								MiniBackupX.XCheck(files, filet);
								buttonA.setText("备份");
								showData();
							}
							else
								JOptionPane.showMessageDialog(null, "无效路径", "TIP", JOptionPane.WARNING_MESSAGE);
						}
					}
				}
				else if(buttonB==e.getSource()){
					if(txtC.getText().equals("") && txtD.getText().equals("")) return;
					int n=JOptionPane.showConfirmDialog(null, "恢复备份?\n(恢复备份可能删除源路径部分文件)", "TIP", JOptionPane.YES_NO_OPTION);
						if(n==JOptionPane.YES_OPTION){
							File files=new File(txtC.getText()), filet=new File(txtD.getText());
							if(!files.exists())
								JOptionPane.showMessageDialog(null, "源路径无效!!!", "TIP", JOptionPane.WARNING_MESSAGE);
							else if(!filet.exists())
								JOptionPane.showMessageDialog(null, "目标路径无效!!!", "TIP", JOptionPane.WARNING_MESSAGE);
							else if(files.isDirectory() && filet.isDirectory()){
								if(txtC.getText().equals(txtD.getText())){
									JOptionPane.showMessageDialog(null, "路径相同!!!", "TIP", JOptionPane.WARNING_MESSAGE);
									
									return;
								}
								String str=txtA.getText();
								txtA.setText(str+"\n...");
								cleanPage();
								MiniBackupX.Addaction.clear();
								MiniBackupX.Updateaction.clear();
								MiniBackupX.Deleteaction.clear();
								MiniBackupX.XCheck(filet, files);
								showData();
								MiniBackupX.XUpdateUI(mainJF, 7);
								
							}
							else
								JOptionPane.showMessageDialog(null, "无效路径", "TIP", JOptionPane.WARNING_MESSAGE);
						}
					//mainJF.setEnabled(true);
				}
				else if(buttonC==e.getSource()){
					mainJF.setEnabled(false);
					buttonA.setText("检索"); cleanPage();
					MiniBackupX.Addaction.clear();
					MiniBackupX.Updateaction.clear();
					MiniBackupX.Deleteaction.clear();
					String str=SelectFolder();
					if(str!=null) txtC.setText(str);
				}
				else if(buttonD==e.getSource()){
					mainJF.setEnabled(false);
					buttonA.setText("检索"); cleanPage();
					MiniBackupX.Addaction.clear();
					MiniBackupX.Updateaction.clear();
					MiniBackupX.Deleteaction.clear();
					String str=SelectFolder();
					if(str!=null) txtD.setText(str);
				}
			}
		}
		Listener_button Lis = new Listener_button();
		time = new Timer(200, Lis);
		time.start();
		buttonA.addActionListener(Lis);
		buttonB.addActionListener(Lis);
		buttonC.addActionListener(Lis);
		buttonD.addActionListener(Lis);
	}
			
	
	
	
	
	public MiniXCopy_GUI(){
		init();
		addListener();
	}
}
public class tPro_GUI2{
	public static void main(String args[]){
		new MiniXCopy_GUI();
	}
}