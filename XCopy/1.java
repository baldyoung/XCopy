import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;


class XProgressBar{
	JFrame mainJF, jfx;
	JProgressBar progressBar;
	JTextArea jta;
	JButton jb;
	boolean run=true;
	
	XProgressBar(JFrame jf, int ac){
		jf.setEnabled(false);
		jfx=jf;
		int MaxNum=0;
		if((ac & 1)>0) MaxNum+=MiniBackupX.Addaction.size();
		if((ac & 2)>0) MaxNum+=MiniBackupX.Updateaction.size();
		if((ac & 4)>0) MaxNum+=MiniBackupX.Deleteaction.size();
		mainJF=new JFrame("进度");
		mainJF.setLayout(null);
		mainJF.setBackground(new java.awt.Color(70, 110, 90));
		
		progressBar = new JProgressBar(0, MaxNum); //进度条
		progressBar.setStringPainted(true);
		progressBar.setValue(0);
		
		jta = new JTextArea(); jta.setEditable(false); //文本区
		JScrollPane jsp = new JScrollPane(jta);
		
		jb = new JButton("取消"); //中止按钮
		jb.setBackground(new java.awt.Color(255, 165, 90));
		
		jb.setBounds(310, 2, 90, 25);
		progressBar.setBounds(3, 2, 300, 25);
		jsp.setBounds(3, 30, 400, 270);
		mainJF.add(progressBar); mainJF.add(jsp); mainJF.add(jb);
		
		addListener();
		mainJF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainJF.setBounds(jfx.getBounds().x+50, jfx.getBounds().y, 406, 300); // 设置窗口的大小
		mainJF.setLocationRelativeTo(null);
		mainJF.setType(Window.Type.UTILITY);
		mainJF.setUndecorated(true);
		mainJF.setAlwaysOnTop(true);
		mainJF.setVisible(true);
		mainJF.setResizable(false);
		mainJF.revalidate();
		
		int t=0, i;
		XCell cell;
		JScrollBar scrollBar=jsp.getVerticalScrollBar();
		if((ac & 1)>0) {
			for(i=0;run && i<MiniBackupX.Addaction.size();i++){
				cell=MiniBackupX.Addaction.get(i);
				MiniBackupX.XCopy(cell.sfile, cell.tfile);
				t++;
				progressBar.setString(""+t+"/"+MaxNum);
				progressBar.setValue(t);
				jta.setText(jta.getText()+"\nADD:"+cell.sfile.getAbsolutePath());
				scrollBar.setValue(scrollBar.getMaximum());
				
			}
		}
		if((ac & 2)>0) {
			for(i=0;run && i<MiniBackupX.Updateaction.size();i++){
				cell=MiniBackupX.Updateaction.get(i);
				MiniBackupX.XCopy(cell.sfile, cell.tfile);
				t++;
				progressBar.setString(""+t+"/"+MaxNum);
				progressBar.setValue(t);
				jta.setText(jta.getText()+"\nUPD:"+cell.sfile.getAbsolutePath());
			}
		}
		if((ac & 4)>0) {
			for(i=0;run && i<MiniBackupX.Deleteaction.size();i++){
				cell=MiniBackupX.Deleteaction.get(i);
				cell.tfile.delete();
				t++;
				progressBar.setString(""+t+"/"+MaxNum);
				progressBar.setValue(t);
				jta.setText(jta.getText()+"\nDEL:"+cell.sfile.getAbsolutePath());
			}
		}
		if(run) jb.setText("完成");
		else jb.setText("已取消");
		MiniBackupX.Addaction.clear();
		MiniBackupX.Updateaction.clear();
		MiniBackupX.Deleteaction.clear();
		
	}
	void addListener(){
		class Listener_button implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(jb==e.getSource()){
					if(jb.getText().equals("完成")){
						jfx.setEnabled(true);
						jfx.toFront();
						mainJF.dispose();
					}
					if(run) run=false;
					else{
						jfx.setEnabled(true);
						jfx.toFront();
						mainJF.dispose();
					}
				}
			}
		}
		Listener_button lis = new Listener_button();
		jb.addActionListener(lis);
	}
}



	
	
	
	
