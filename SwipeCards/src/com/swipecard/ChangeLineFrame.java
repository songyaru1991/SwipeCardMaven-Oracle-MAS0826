package com.swipecard;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Reader;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import com.swipecard.model.Employee;
import com.swipecard.model.SwipeCardTimeInfos;
import com.swipecard.util.DESUtils;
import com.swipecard.util.JsonFileUtil;
import com.swipecard.util.SwipeCardJButton;

public class ChangeLineFrame extends JFrame{
	
	private static Logger logger = Logger.getLogger(SwipeCard.class);
	static JsonFileUtil jsonFileUtil = new JsonFileUtil();
	//static SwipeCard swCard = new SwipeCard();
	static String defaultWorkshopNo = jsonFileUtil.getSaveWorkshopNo();
	static  String defaultLineNo = jsonFileUtil.getSaveLineNo();
	
	static JLabel changeLineLabel,showChangeLineMsg;
	static JTextField changeLinetf;
	static JComboBox changeWorkShopBox,changeLineBox;
	static SwipeCardJButton changeLineBut;
	static JPanel p1,p2,psclm;
	
	static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
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
			 * "/Configuration.xml"; FileReader reader = new
			 * FileReader(filePath);
			 */
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,pps);
		} catch (Exception e) {
			logger.error("Error opening session:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		}
	}
	
	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}
	
	public ChangeLineFrame() {
		
		// TODO Auto-generated constructor stub
		changeLineLabel = new JLabel("請換線員工刷卡");
		initFont(changeLineLabel);
		
		showChangeLineMsg = new JLabel("");
		initFont(showChangeLineMsg);
		
		changeLinetf = new JTextField(8);
		initFont(changeLinetf);
		
		changeWorkShopBox = new JComboBox();
		changeWorkShopBox.setPreferredSize(new Dimension(200, 37));
		changeWorkShopBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		SqlSession session = sqlSessionFactory.openSession();	
		List<Object> WorkShops = session.selectList("selectWorkshopNos");
		if(WorkShops!=null && !WorkShops.isEmpty()) {
			for(Object obj : WorkShops) {
				if(obj!=null) {
					changeWorkShopBox.addItem(obj);
				}else {
					changeWorkShopBox.addItem("未知錯誤沒有車間");
				}				
			}
		}		
		
		changeLineBox = new JComboBox();
		changeLineBox.setPreferredSize(new Dimension(200, 37));
		changeLineBox.setFont(new Font("微软雅黑", Font.PLAIN, 18));
		
		//String currWorkShopNo = swCard.workShopNoJlabel.getText();
		changeWorkShopBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					SqlSession session = sqlSessionFactory.openSession();	
					String currWorkShopNo = (String) changeWorkShopBox.getSelectedItem();
					List<Object> LineNos = session.selectList("selectLineNoByWorkShopNo", currWorkShopNo);
					// changeLineComboBox.addItem(LineNos);
					changeLineBox.removeAllItems();
					if (LineNos != null && !LineNos.isEmpty()) {
						for (Object object : LineNos) {
							if (object != null) {
								changeLineBox.addItem(object);
							} else {
								changeLineBox.addItem("該車間沒有線號");
							}
						}
					}
				} catch (Exception e) {
					logger.error("員工換線查詢車間錯誤:" + e);
					dispose();
					throw ExceptionFactory.wrapException("員工換線查詢車間錯誤:" + e,e);
				}
				finally {					
					 ErrorContext.instance().reset();
					if (session != null) {
						session.close();
					}
				}
			}
		});	
		 		
		changeLineBut = new SwipeCardJButton("確認轉線", 2);
		initFont(changeLineBut);
		changeLineBut.addActionListener(new ActionListener() {

			@SuppressWarnings("unused")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				SqlSession session = sqlSessionFactory.openSession();
				String CardID = changeLinetf.getText();
				Employee eif = (Employee) session.selectOne("selectUserByCardID", CardID);
								
				String selectWorkShop = (String) changeWorkShopBox.getSelectedItem();
				String selectLine = (String) changeLineBox.getSelectedItem();

				if (CardID.length() > 10 || CardID.length() < 10) {
					showChangeLineMsg.setBackground(Color.red);
					showChangeLineMsg.setText("卡號輸入有誤，請再次刷卡\n");
				} else {
					String pattern = "^[0-9]\\d{9}$";
					Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
					Matcher m = r.matcher(CardID);
					if (m.matches() == true) {
						try {
							if (eif == null) {
								showChangeLineMsg.setText("員工不存在,請確認工號是否正確！");
								showChangeLineMsg.setForeground(Color.red);
							} else {
								String empName = eif.getName();				
								String empId = eif.getId();
								SwipeCardTimeInfos swipeInfo = new SwipeCardTimeInfos();
								int OutWorkSwipeCount = session.selectOne("isOutWorkSwipeToday", empId);
								if (OutWorkSwipeCount > 0) {
									showChangeLineMsg.setForeground(Color.red);
									showChangeLineMsg.setText("下班卡已刷，無法再進行換線！");
								} else {
									if(selectWorkShop=="" || selectWorkShop==null || selectLine=="" || selectLine==null ){
										showChangeLineMsg.setForeground(Color.red);
										showChangeLineMsg.setText("換線車間或線體不能為空");
									}else{
										int goWorkSwipeCount = session.selectOne("isGoWorkSwipeToday", empId);
										if (goWorkSwipeCount == 1) {// 如果今天有上刷進行更新車間線別的操作
											swipeInfo.setWorkshopNo(selectWorkShop);
											swipeInfo.setProdLineCode(selectLine);
											swipeInfo.setEMP_ID(empId);
											session.update("updateLineNobySwipeCard", swipeInfo);
											session.commit();										
											showChangeLineMsg
													.setText(empId + "員工換到" + selectWorkShop + "的" + selectLine + "線成功！");
											showChangeLineMsg.setForeground(Color.BLUE);
										}else if (goWorkSwipeCount == 2){//昨天是夜班,且昨天只有上刷無下刷，今天已進行上刷的情況
											swipeInfo.setWorkshopNo(selectWorkShop);
											swipeInfo.setProdLineCode(selectLine);
											swipeInfo.setEMP_ID(empId);
											session.update("updateLineNobyToday", swipeInfo);
											session.commit();									
											showChangeLineMsg
													.setText(empId + "員工換到" + selectWorkShop + "的" + selectLine + "線成功！");
											showChangeLineMsg.setForeground(Color.BLUE);
										}else {
											showChangeLineMsg.setText(empId + "員工沒有刷上班卡無法換線！");
											showChangeLineMsg.setForeground(Color.red);
										}
										}

									}
							}
								 
							}catch (Exception e1) {
							logger.error("換線刷卡異常,原因:" + e1);
							dispose();
							throw ExceptionFactory.wrapException("刷卡異常,原因:" + e1, e1);
						} finally {
							
							 ErrorContext.instance().reset();
							if (session != null) {
								session.close();
							}
							changeLinetf.setText("");
						}
						changeLinetf.setText("");
					} else {
						System.out.println("無輸入內容或輸入錯誤!");
					}
				}
			}

		});
		
		
		p1 = new JPanel(new FlowLayout());
		p1.add(changeLineLabel);
		p1.add(changeLinetf);
		p1.add(changeWorkShopBox);
		p1.add(changeLineBox);
		p1.add(changeLineBut);

		psclm = new JPanel(new FlowLayout());
		psclm.add(showChangeLineMsg);

		p2 = new JPanel(new BorderLayout());
		p2.add(p1, BorderLayout.NORTH);
		p2.add(psclm, BorderLayout.CENTER);

		Container c = getContentPane();
		c.add(p2);

		setTitle("轉線界面");
		setLocation(500, 250);
		setSize(1000, 500);
		setVisible(true);

	
	}
	
	public void initFont(Container component) {
		component.setFont(new Font("微软雅黑", Font.BOLD, 25));
	}
	
}
