package com.swipecard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.swipecard.util.PropertyUtil;
/**  
* 说明： CheckCurrentVersion时若有最新版本，则下载远程服务器上自动更新程式，update.jar为AutoUpdateUtil打包的jar
*  
* @author  
* @version 创建时间：2018-11-28
*/
public class AutoUpdate {
	public void update(){
		File file = new File("D:/SwipeCard/update.jar");
		if(!file.exists()){
			String serverIP=PropertyUtil.getProperty("serverIP");
		    String serverUserName=PropertyUtil.getProperty("serverUserName");
		    String serverPassword=PropertyUtil.getProperty("serverPassword");
			boolean R = downFile(serverIP,serverUserName,serverPassword, 
					"", "update.jar", "D:/SwipeCard/");
		}
	}
	
	public static boolean downFile(  
            String url, //FTP服务器hostname  
//            int port,//FTP服务器端口  
            String username, //FTP登录账号  
            String password, //FTP登录密码  
            String remotePath,//FTP服务器上的相对路径   
            String fileName,//要下载的文件名  
            String localPath//下载后保存到本地的路径 

            ) {    
        boolean success = false;    
        FTPClient ftp = new FTPClient();    
        try {    
        	File file = new File(localPath);
        	if(!file.exists()){
        		file.mkdirs();
        	}
            int reply;    
            ftp.connect(url);    
            //如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器     
            ftp.login(username, password);//登录     
            reply = ftp.getReplyCode();    
            if (!FTPReply.isPositiveCompletion(reply)) {    
                ftp.disconnect();    
                return success;    
            }   
            ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录     
            FTPFile[] fs = ftp.listFiles();  
            
            for(FTPFile ff:fs){ 
             
                if(ff.getName().equals(fileName)){  
                 System.out.println("dd");
                    File localFile = new File(localPath+"/"+ff.getName());    
                    OutputStream is = new FileOutputStream(localFile);     
                    ftp.retrieveFile(ff.getName(), is);  
                  //  System.out.println("ccc" +ff.getName()+fileName);
                    is.close();    
                    success = true;   
                }    
            }    
            ftp.logout();    
             
        } catch (IOException e) {    
            e.printStackTrace();    
            int dialogResult = JOptionPane.showConfirmDialog(null,
					"獲取更新程序失敗，请重新打开程序或者联系管理员", "獲取更新程序失敗",
					JOptionPane.DEFAULT_OPTION);
            System.exit(0);
        } finally {    
            if (ftp.isConnected()) {    
                try {    
                    ftp.disconnect();    
                } catch (IOException ioe) {    
                }    
            }    
        }    
        return success;    
    }
}
