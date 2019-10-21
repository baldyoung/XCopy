import java.io.*;
import java.util.*;
import java.io.File;
import java.util.Date;
import javax.swing.*;
class XCell{
	File sfile, //
		tfile;  //
	int operation;
}
class MiniBackupX{
	static LinkedList<XCell> Addaction = new LinkedList<XCell>();
	static LinkedList<XCell> Updateaction = new LinkedList<XCell>();
	static LinkedList<XCell> Deleteaction = new LinkedList<XCell>();
	//整合绝对路径
	static String toAbsolutePath(File file){
		//
		if(file.isDirectory()) return new String("");
		String str = file.getAbsolutePath();
		str = str.substring(0, str.length()-file.getName().length()-1);
		return str;
	}
	//创建路径
	static int createPath(File file){
		//自动为文件创建路径
		if(file.exists()) return 1;
		String str=file.getAbsolutePath();
		//System.out.println(str);
		int i=str.length();
		boolean flag=true, t=false;
		for(i--;i>=0;i--){
			if(flag && '.'==str.charAt(i)){
				flag=false; t=true;
			}
			else if('\\'==str.charAt(i)){
				flag=false; 
				if(!t){
					createPath(new File(str.substring(0, i)));
					file.mkdir();
					break;
				}
				else t=false;
			}
		}
		return 0;
	}
	//单个文件的复制操作
	static int XCopy(File sourcefile, File targetfile){
		//sourcefile 是文件; targetfile 是目录;
		if(targetfile.exists() && !targetfile.isDirectory()) targetfile=new File(toAbsolutePath(targetfile));
		createPath(targetfile);
		InputStream input=null;
		OutputStream output=null;
		try{
			input = new FileInputStream(sourcefile);
			File tempfile = new File(targetfile.getAbsolutePath()+"/"+sourcefile.getName());
			output = new FileOutputStream(tempfile);
			byte[] buf = new byte[1024];
			int bytesRead;
			while((bytesRead=input.read(buf))>0){
				output.write(buf,0,bytesRead);
			}
			if(input!=null) input.close();
			if(output!=null) output.close();
			tempfile.setLastModified(sourcefile.lastModified());
		}
		catch(IOException e){
			return -1;
		}
		return 0;
	}
	//递归算法完成一个文件夹里所以文件的更新核查
	static int XCheck(File sfile, File tfile){
		//
		File files[], afile;
		XCell xcell;
		int i;
		if(!sfile.exists()){//first DG must done;
			files=tfile.listFiles();
			for(i=0;i<files.length;i++){
				if(files[i].isDirectory())
					XCheck(new File(sfile.getAbsolutePath()+"/"+files[i].getName()), files[i]);
				else{
					xcell = new XCell(); xcell.sfile=sfile; xcell.tfile=files[i]; xcell.operation=3;
					Deleteaction.add(xcell);
				}
			}
		}
		else if(!tfile.exists()){
			files=sfile.listFiles();
			for(i=0;i<files.length;i++){
				if(files[i].isDirectory())
					XCheck(files[i], new File(tfile.getAbsolutePath()+"/"+files[i].getName()));
				else{
					xcell=new XCell(); xcell.sfile=files[i]; xcell.tfile=tfile; xcell.operation=1;
					Addaction.add(xcell);
				}
			}
		}
		else{
			files=tfile.listFiles();
			//System.out.println("flag0");
			for(i=0;i<files.length;i++){
				afile = new File(sfile.getAbsolutePath()+"/"+files[i].getName());
				//System.out.println(afile.getAbsolutePath());
				if(!afile.exists()){
					
					if(!files[i].isDirectory()){
						xcell = new XCell(); xcell.sfile=sfile; xcell.tfile=files[i]; xcell.operation=3;
						Deleteaction.add(xcell);
					}
					else 
						XCheck(new File(sfile.getAbsolutePath()+"/"+files[i].getName()), files[i]);
				}
			}
			//System.out.println("flagB");
			files=sfile.listFiles();
			for(i=0;i<files.length;i++){
				afile = new File(tfile.getAbsolutePath()+"/"+files[i].getName());
				//System.out.println("flagA");
				if(!afile.exists()){
					if(!files[i].isDirectory()){
						xcell = new XCell(); xcell.sfile=files[i]; xcell.tfile=tfile; xcell.operation=1;
						Addaction.add(xcell);
					}
					else
						XCheck(files[i], new File(tfile.getAbsolutePath()+"/"+files[i].getName()));
				}
				else{
					//System.out.println("flagC");
					if(!files[i].isDirectory()){
						if(files[i].lastModified()>afile.lastModified()){
							xcell=new XCell(); xcell.sfile=files[i]; xcell.tfile=afile; xcell.operation=2;
							Updateaction.add(xcell);
						}
					}
					else
						XCheck(files[i], afile);
				}
			}
		}
		return 0;
	}
	//同步操作
	static int XUpdate(int ac){
		int i;
		XCell cell;
		
		if((ac & 1)>0) {
			if((ac & 8)>0) System.out.println("*************************Addaction:******************");
			for(i=0;i<Addaction.size();i++){
				cell=Addaction.get(i);
				XCopy(cell.sfile, cell.tfile);
				if((ac & 8)>0) System.out.println(i+"\t"+cell.sfile.getAbsolutePath()+"\t"+cell.tfile.getAbsolutePath()+"\t"+cell.operation);
			}
		}
		
		if((ac & 2)>0){
			if((ac & 8)>0) System.out.println("#######################Updateaction:##################");
			for(i=0;i<Updateaction.size();i++){
				cell=Updateaction.get(i);
				XCopy(cell.sfile, cell.tfile);
				if((ac & 8)>0) System.out.println(i+"\t"+cell.sfile.getAbsolutePath()+"\t"+cell.tfile.getAbsolutePath()+"\t"+cell.operation);
			}
		}
		
		if((ac & 4)>0){
			if((ac & 8)>0) System.out.println("$$$$$$$$$$$$$$$$$$$$$$$Deleteaction:$$$$$$$$$$$$$$$$$$$");
			for(i=0;i<Deleteaction.size();i++){
				cell=Deleteaction.get(i);
				cell.tfile.delete();
				if((ac & 8)>0) System.out.println(i+"\t"+cell.sfile.getAbsolutePath()+"\t"+cell.tfile.getAbsolutePath()+"\t"+cell.operation);
			}
		}
		Addaction.clear();
		Updateaction.clear();
		Deleteaction.clear();
		return 0;
	}
	static int XUpdateUI(JFrame jf, int tac){
		
		new Thread(){
			public void run(){
				XProgressBar xpb = new XProgressBar(jf, tac);
			}
		}.start();
		
		return 0;
	}
}


	
	
