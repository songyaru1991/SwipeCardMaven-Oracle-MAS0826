package com.swipecard;

import java.io.Reader;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.swipecard.util.DESUtils;


public class CheckIp implements Runnable{
	private static Logger logger = Logger.getLogger(CheckIp.class);
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	private String[] ip;
	private	Object[] Eip;
	private Object[] Cip;
	private Timestamp CurrentDBTimeStamp;
	static Properties pps = new Properties();
	static Reader pr = null;
	static {
		try {
			pr = Resources.getResourceAsReader("db.properties");
			pps.load(pr);
			pps.setProperty("username", DESUtils.getDecryptString(pps.getProperty("username")));
			pps.setProperty("password", DESUtils.getDecryptString(pps.getProperty("password")));
			reader = Resources.getResourceAsReader("Configuration.xml");
			/*
			 * String filePath = System.getProperty("user.dir") +
			 * "/Configuration.xml"; FileReader reader=new FileReader(filePath);
			 */
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,pps);
		} catch (Exception e) {
			logger.error("檢測ip時 Error building SqlSession，原因:"+e);
			e.printStackTrace();
		}
	}

	public CheckIp(String ipv4) {
		// TODO Auto-generated constructor stub
		ip = ipv4.split("/");
		Eip = getIpByControl_except("E");
		Cip = getIpByControl_except("C");
		
	}
	
	public static void main(String[] args) {
		CheckIp c = new CheckIp("123");
	}


	private Object[] getIpByControl_except(String str) {
		List<String> ip;
		Object[] a = null;
		SqlSession session = null;
		try {
			session = sqlSessionFactory.openSession();
			ip = session.selectList("getIpByControl_except",str);
			int con = ip.size();
			if(con > 0){
				a = new Object[con];
				a = new Object[ip.size()];
				for (int i = 0; i < ip.size(); i++) {
					a[i] = ip.get(i);
					// System.out.println("Admin: " + a[i]);
				}
			}else{
				a = new Object[1];
				a[0] = "";
			}
		} catch (Exception e) {
			System.out.println("Error opening session");
			logger.error("获取通用ip异常，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(null);
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
			if (session != null) {
				session.close();
			}
		}
		return a;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean isAdminIp = this.checkIpIsAdminIp();
		if (isAdminIp) {
			System.out.println("该ip允许使用程序");
		} else {
//			if ((currentHour >= 9 && currentHour < 12) || (currentHour >= 14 && currentHour < 17)
//					|| (currentHour >= 21 && currentHour < 24) || (currentHour >= 1 && currentHour < 7)) {
				int dialogResult = JOptionPane.showConfirmDialog(null,
						"此電腦不允許使用此程序，若要使用此程序，請聯繫管理員申請權限\n", "此電腦不允許使用此程序",
						JOptionPane.DEFAULT_OPTION);
				System.exit(0);
//			}

		}
	}

	private boolean checkIpIsAdminIp() {
		// TODO Auto-generated method stub
		for (String str : ip) {
			if(str == null || str.equals("")){
				continue;
			}
			
			for (Object object : Eip) {
				if(!(object == null || object.equals(""))){
					if(str.contains((String)object)){
						return true;
					}
				}
				
			}
			
			for (Object object : Cip) {
				if(str.equals(object)){
					return true;
				}
			}
			
		}
		return false;
	}
	
}
