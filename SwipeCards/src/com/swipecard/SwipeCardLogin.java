package com.swipecard;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swipecard.util.DESUtils;
import com.swipecard.util.FrameShowUtil;
import com.swipecard.util.JsonFileUtil;
import com.swipecard.util.PropertyUtil;
import com.swipecard.util.SwipeCardJButton;
import com.swipecard.model.Employee;
import com.swipecard.model.LineNO;

public class SwipeCardLogin extends JFrame {
	private final static String CurrentVersion = PropertyUtil.getProperty("currentVersion");;
	private static Logger logger = Logger.getLogger(SwipeCardLogin.class);
	static JsonFileUtil jsonFileUtil = new JsonFileUtil();
	final  String defaultWorkshopNo = jsonFileUtil.getSaveWorkshopNo();
	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	static Properties pps = new Properties();
	static Reader pr = null;
	static {
		try {
			pr = Resources.getResourceAsReader("db.properties");
			pps.load(pr);
			pps.setProperty("username", DESUtils.getDecryptString(pps.getProperty("username")));
			pps.setProperty("password", DESUtils.getDecryptString(pps.getProperty("password")));
			// 读取内部配置文件
			reader = Resources.getResourceAsReader("Configuration.xml");

			/*
			 * // 读取系统外配置文件 (即Jar包外文件) --- 外部工程引用该Jar包时需要在工程下创建config目录存放配置文件
			 * String filePath = System.getProperty("user.dir") +
			 * "/Configuration.xml"; FileReader reader=new FileReader(filePath);
			 */
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader,pps);
			// System.out.println("sqlSessionFactory:"+sqlSessionFactory);
		} catch (Exception e) {
			logger.error("Login時 Error building SqlSession，原因:" + e);
			SwipeCardNoDB d = new SwipeCardNoDB(null);
			throw ExceptionFactory.wrapException("Error building SqlSession.", e);
		}
	}

	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}

	final Object[] WorkshopNo = getWorkshopNo();
	final Object[] LineLeader = getLineLeader();
	final JSONObject LineNoObject = getLineNoObject();
	Object[] lineno = null;

	private JPanel panel1;
	private JLabel label1, label2, label3, label4;
	static JPasswordField text1;
	static JTextField jtf1, jtf3;
	private SwipeCardJButton but1;
	static JComboBox comboBox1, comboBox2;

	/**
	 * 为界面中的每一个组件指定一个GridBagConstraints对象,通过设置该对象的属性值指出组件在管理器中的布局方案
	 * 
	 * @author Yaru_Song
	 *
	 */
	public class setGBC extends GridBagConstraints {
		// 初始化左上角位置 ,在y行，x列，行对应的是gridy,列对应的是gridx
		public setGBC(int gridx, int gridy) {
			this.gridx = gridx; // 第几列
			this.gridy = gridy; // 第几行
		}

		// 初始化左上角位置和所占行数和列数 ,例:1行2列，则gridwidth=2,gridheight=1
		public setGBC(int gridx, int gridy, int gridwidth, int gridheight) {
			this.gridx = gridx;
			this.gridy = gridy;
			this.gridwidth = gridwidth; // 占几列
			this.gridheight = gridheight; // 占几行
		}

		// 对齐方式
		public setGBC setAnchor(int anchor) {
			this.anchor = anchor;
			return this;
		}

		// 是否拉伸及拉伸方向
		public setGBC setFill(int fill) {
			this.fill = fill;
			return this;
		}

		// x和y方向上的增量
		public setGBC setWeight(double weightx, double weighty) {
			this.weightx = weightx;
			this.weighty = weighty;
			return this;
		}

		// 外部填充
		public setGBC setInsets(int distance) {
			this.insets = new Insets(distance, distance, distance, distance);
			return this;
		}

		// 外填充
		public setGBC setInsets(int top, int left, int bottom, int right) {
			this.insets = new Insets(top, left, bottom, right);
			return this;
		}

		// 内填充 ,在组件首选大小的基础上x方向上加上ipadx，y方向上加上ipady,保证组件不会收缩到ipadx,ipady所确定的大小以下
		public setGBC setIpad(int ipadx, int ipady) {
			this.ipadx = ipadx;
			this.ipady = ipady;
			return this;
		}
	}

	public SwipeCardLogin() {
		super("管理人員登陸-" + CurrentVersion);
		// setBounds(212, 159, 600, 450);
		setResizable(false);

		Container c = getContentPane();
		panel1 = new JPanel();
		panel1.setLayout(null);
		label1 = new JLabel("實時工時系統", JLabel.CENTER);
		label2 = new JLabel("管理人員：");
		label3 = new JLabel("车间：");
		label4 = new JLabel("線別：");

		text1 = new JPasswordField(10);
		if(!(defaultWorkshopNo == null || defaultWorkshopNo.equals(""))){
			text1.setEchoChar((char) 0);
			text1.setText("不需要刷卡");
			text1.setEditable(false);
		}

		but1 = new SwipeCardJButton("確認 ", 2);

		comboBox1 = new JComboBox(WorkshopNo);
		comboBox1.setEditable(false);// 設置comboBox1為不可編輯，此時jtf1不生效
		comboBox1.setFont(new Font("微软雅黑", Font.PLAIN, 18));

		comboBox2 = new JComboBox();
		comboBox2.setFont(new Font("微软雅黑", Font.PLAIN, 18));

		jtf1 = (JTextField) comboBox1.getEditor().getEditorComponent();
		if (defaultWorkshopNo != null) {
			comboBox1.setSelectedItem(defaultWorkshopNo);
		}

		lineno = getLineno(comboBox1.getSelectedItem().toString());
		if (lineno != null) {
			for (Object object : lineno) {
				comboBox2.addItem(object);
			}
		} else {
			comboBox2.addItem("不需要選擇線號");
		}

		label1.setFont(new Font("微软雅黑", Font.BOLD, 40));
		but1.setFont(new Font("微软雅黑", Font.BOLD, 18));

		Container container = getContentPane();
		// 设置布局管理器为6行8列的GridLayout,组件间水平与垂直间距为5
		container.setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();

		/*
		 * label1.setBounds(150, 50, 300, 40); label1.setFont(new Font("微软雅黑",
		 * Font.BOLD, 40)); but1.setFont(new Font("微软雅黑", Font.BOLD, 18));
		 * label2.setBounds(120, 200, 100, 30); text1.setBounds(220, 200, 160,
		 * 40); but1.setBounds(240, 300, 120, 40);
		 * 
		 * label3.setBounds(120, 120, 100, 30); comboBox1.setBounds(220, 120,
		 * 160, 40);
		 */
		// (第几列,第几行,占几列,占几行)
		container.add(label1,
				new setGBC(0, 0, 2, 1).setFill(setGBC.CENTER).setIpad(300, 40).setWeight(0, 0).setInsets(10));
		container.add(label3, new setGBC(0, 2, 1, 1).setFill(setGBC.EAST).setAnchor(setGBC.EAST).setIpad(10, 0)
				.setWeight(0, 0).setInsets(10, 70, 10, 0));
		container.add(label2, new setGBC(0, 3, 1, 1).setFill(setGBC.EAST).setAnchor(setGBC.EAST).setIpad(10, 0)
				.setWeight(0, 0).setInsets(10, 70, 10, 0));
		container.add(label4, new setGBC(0, 4, 1, 1).setFill(setGBC.EAST).setAnchor(setGBC.EAST).setIpad(10, 0)
				.setWeight(0, 0).setInsets(10, 70, 10, 0));
		container.add(comboBox1, new setGBC(1, 2, 1, 1).setFill(setGBC.CENTER).setAnchor(setGBC.WEST).setIpad(60, 20)
				.setWeight(0, 0).setInsets(10));
		container.add(comboBox2, new setGBC(1, 4, 1, 1).setFill(setGBC.CENTER).setAnchor(setGBC.WEST).setIpad(60, 20)
				.setWeight(0, 0).setInsets(10));
		container.add(text1, new setGBC(1, 3, 1, 1).setFill(setGBC.CENTER).setAnchor(setGBC.WEST).setIpad(30, 20)
				.setWeight(0, 0).setInsets(10));
		container.add(but1, new setGBC(1, 5, 2, 1).setFill(setGBC.CENTER).setAnchor(setGBC.CENTER).setIpad(35, 20)
				.setWeight(0, 0).setInsets(10, 0, 10, 10));
		// c.add(panel1);
		but1.addActionListener(new TextFrame_jButton1_actionAdapter(this));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		// setMaximumSize(new Dimension(400, 200));//设置最大值
		// setMinimumSize(new Dimension(200, 100));//设置最小值

		FrameShowUtil frameShow = new FrameShowUtil();
		frameShow.sizeWindowOnScreen(this, 0.4, 0.5);

		comboBox1.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub

				if (e.getStateChange() == ItemEvent.SELECTED) {
					String key = jtf1.getText();
					lineno = getLineno(comboBox1.getSelectedItem().toString());
					comboBox2.removeAllItems();
					if (lineno != null) {
						for (Object object : lineno) {
							comboBox2.addItem(object);
						}
					} else {
						comboBox2.addItem("不需要選擇線號");
					}
				}
			}
		});
	}

	private JSONObject getLineNoObject() {
		// TODO Auto-generated method stub
		List<LineNO> LineNo;
		JSONObject jsonObject = new JSONObject();
		Map<String, String> map = new HashMap<String, String>();
		try {
			SqlSession session = sqlSessionFactory.openSession();
			LineNo = session.selectList("selectLineNoList");
			int con = LineNo.size();
			if (con > 0) {
				for (int i = 0; i < con; i++) {
					if (map.containsKey(LineNo.get(i).getWorkShopNo())) {
						String str = map.get(LineNo.get(i).getWorkShopNo()) + "," + LineNo.get(i).getLineNo();
						map.put(LineNo.get(i).getWorkShopNo(), str);
					} else {
						map.put(LineNo.get(i).getWorkShopNo(), LineNo.get(i).getLineNo());
					}
				}
				Iterator<Entry<String, String>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Entry<String, String> entry = entries.next();
					String str = "";
					if(entry.getValue() == null){
						jsonObject.put(entry.getKey(), str);
					}else{
						jsonObject.put(entry.getKey(), entry.getValue());
					}
				}
				String fileName = "LineNo.json";
				jsonFileUtil.createWorkshopNoJsonFile(jsonObject.toString(), fileName);
			}
		} catch (Exception e) {
			System.out.println("Error opening session");
			logger.error("取得线体異常,原因" + e);
			dispose();
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
		System.out.println(jsonObject);
		return jsonObject;
	}

	public Object[] getLineno(String selectWorkshopNo) {// TODO
		String linenoList;
		Object[] a = null;
		Object[] s = null;
		ArrayList<Object> list = new ArrayList<Object>();
		System.out.println(selectWorkshopNo);
		if (!(selectWorkshopNo == null || selectWorkshopNo.equals("") || selectWorkshopNo.equals("--請選擇車間--"))) {
			if (!(LineNoObject == null || LineNoObject.equals(""))) {
				linenoList = LineNoObject.getString(selectWorkshopNo);
				System.out.println(linenoList);
				if (!(linenoList == null || linenoList.equals(""))) {
					s = linenoList.split(",");
					int con = s.length;
					for (int i = 0; i < con; i++) {
						String str;
						str = s[i].toString().trim();
						if (!(str == null || str.equals("") || str.equals("null"))) {
							list.add(s[i]);
						}
					}
					int lcon = list.size();
					System.out.println(lcon);
					if (lcon > 0) {
						a = new Object[lcon + 2];
						a[0] = "請選擇線號";
						a[1] = "不需要選擇線號";
						for (int i = 0; i < lcon; i++) {
							a[i + 2] = list.get(i);
							System.out.println(list.get(i).toString());
						}
					}
				}
			}
		}
		return a;
	}

	public Object[] getWorkshopNo() {// TODO
		List<LineNO> workshopNoInfo;
		Object[] a = null;
		try {
			SqlSession session = sqlSessionFactory.openSession();
			JSONArray workshopNoArray = new JSONArray();
			workshopNoInfo = session.selectList("selectWorkshopNo");
			int con = workshopNoInfo.size();
			if (con > 0) {
				a = new Object[con + 1];
				a[0] = "--請選擇車間--";
				for (int i = 1; i < con + 1; i++) {
					a[i] = workshopNoInfo.get(i - 1).getWorkShopNo();
					JSONObject workshopNoJson = new JSONObject();
					workshopNoJson.put("workshopNo", a[i].toString());
					workshopNoArray.put(workshopNoJson);
				}
				String fileName = "WorkshopNo.json";
				jsonFileUtil.createWorkshopNoJsonFile(workshopNoArray.toString(), fileName);
			} else {
				a = new Object[1];
				a[0] = "--請選擇車間--";
			}
		} catch (Exception e) {
			logger.error("取得車間異常,原因" + e);
			dispose();
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
		return a;
	}

	@SuppressWarnings("rawtypes")
	public Object[] getLineLeader() {
		// TODO
		List<Employee> user;
		Object[] a = null;
		try {
			SqlSession session = sqlSessionFactory.openSession();
			user = session.selectList("selectUserByPermission");
			if (user.size() > 0) {
				a = new Object[user.size()];
				for (int i = 0; i < user.size(); i++) {
					a[i] = user.get(i).getCardID();
					// System.out.println("Admin: " + a[i]);
				}
			} else {
				a = new Object[1];
				a[0] = "";
			}
		} catch (Exception e) {
			logger.error("取得管理員卡號異常，原因:" + e);
			dispose();
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			throw ExceptionFactory.wrapException("Error opening session.  Cause: " + e, e);
		} finally {
			ErrorContext.instance().reset();
		}
		return a;
	}

	public static void main(String args[]) {
		InitGlobalFont(new Font("微软雅黑", Font.BOLD, 18));
		SwipeCardLogin d = new SwipeCardLogin();

		/*CheckCurrentVersion chkVersion = new CheckCurrentVersion(CurrentVersion);
		Thread executeCheckVersion = new Thread(chkVersion);
		executeCheckVersion.start();*/

	}

	private static void InitGlobalFont(Font font) {
		FontUIResource fontRes = new FontUIResource(font);
		for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				UIManager.put(key, fontRes);
			}
		}
	}
	
	public static int getTotal() {
		// 標準人力
		String selectWorkShopNo = SwipeCardLogin.comboBox1.getSelectedItem().toString();
		String selectLineNo = SwipeCardLogin.comboBox2.getSelectedItem().toString();

		LineNO standardUser = new LineNO();
		SqlSession session = sqlSessionFactory.openSession();
		standardUser.setWorkShopNo(selectWorkShopNo);
		standardUser.setLineNo(selectLineNo);
		//standardUser.setPROD_LINE_CODE(selectLineNo);
		int totalStandardcount=0;
		if(selectLineNo == "不需要選擇線號"){
//			standardUser.setLineNo(null);
//			totalStandardcount= session.selectOne("selectLineSize", standardUser);
			totalStandardcount=0;
		}else{
		totalStandardcount= session.selectOne("selectLineSize", standardUser);
		}
		System.out.println(totalStandardcount);
		return totalStandardcount;
	}
}

class TextFrame_jButton1_actionAdapter implements ActionListener {
	private SwipeCardLogin adaptee;

	private static SqlSessionFactory sqlSessionFactory;
	private static Reader reader;
	static {
		try {
			reader = Resources.getResourceAsReader("Configuration.xml");
			/*
			 * String filePath = System.getProperty("user.dir") +
			 * "/Configuration.xml"; FileReader reader=new FileReader(filePath);
			 */
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
		} catch (Exception e) {
			JsonFileUtil jsonFileUtil = new JsonFileUtil();
			final String defaultWorkshopNo = jsonFileUtil.getSaveWorkshopNo();
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		}
	}

	public static SqlSessionFactory getSession() {
		return sqlSessionFactory;
	}

	public TextFrame_jButton1_actionAdapter(SwipeCardLogin adaptee) {
		this.adaptee = adaptee;
	}

	public static boolean isHave(Object[] a, String s) {
		/*
		 * 此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串
		 */
		for (int i = 0; i < a.length; i++) {
			// if (obj[i].indexOf(s) != -1) {// 循环查找字符串数组中的每个字符串中是否包含所有查找的内容
			if (a[i].equals(s)) {
				return true;// 查找到了就返回真，不在继续查询
			}
		}
		return false;// 没找到返回false
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if (adaptee.defaultWorkshopNo == null || adaptee.defaultWorkshopNo.equals("")) {
				char[] pass = adaptee.text1.getPassword();
				String CardID = new String(pass);
				System.out.println("CardID: " + CardID);
				String pattern = "^[0-9]\\d{9}$";
				Pattern r = Pattern.compile(pattern, Pattern.DOTALL);
				Matcher m = r.matcher(CardID);
				// String WorkshopNo = SwipeCardLogin.jtf1.getText();
				String selectWorkShopNo = adaptee.comboBox1.getSelectedItem().toString();
				String selectLineNo = adaptee.comboBox2.getSelectedItem().toString();
				if (selectWorkShopNo.equals("--請選擇車間--")) {
					JOptionPane.showMessageDialog(adaptee, "請選擇車間!");
				} else {
					if(selectLineNo.equals("請選擇線號")){
						JOptionPane.showMessageDialog(adaptee, "請選擇線號!");
					}else{
						if(selectLineNo == "不需要選擇線號"){
							selectLineNo = null;
						}
					if (m.matches() == true) {
						Object[] a = adaptee.LineLeader;
						/*
						 * if(ShiftName.equals("日班")){ Shift="D"; }else
						 * if(ShiftName.equals("夜班")){ Shift="N"; }
						 */
						if (isHave(a, CardID)) {// 调用自己定义的函数isHave，如果包含则返回true,否则返回false
							System.out.println("成功登陸！");
							JsonFileUtil jsonFileUtil = new JsonFileUtil();
							String fileName = "saveSelectWorkshopNo.json";
							JSONObject selectWorkshopNoJson = new JSONObject();
							selectWorkshopNoJson.put("workshopNo", selectWorkShopNo);
							jsonFileUtil.saveSelectWorkshopNo(selectWorkshopNoJson.toString(), fileName);
							
							String lfileName = "saveLineNo.json";
							if(selectLineNo != null){
								JSONObject selectLineNoJson = new JSONObject();
								selectLineNoJson.put("lineNo", selectLineNo);
								jsonFileUtil.saveSelectWorkshopNo(selectLineNoJson.toString(), lfileName);
							}else{
								jsonFileUtil.deleteSaveLineNo(lfileName);
							}
							adaptee.dispose();
							SwipeCard swipe = new SwipeCard(selectWorkShopNo,selectLineNo);
							// System.out.println("WorkShopNo: " +
							// selectWorkShopNo);
						} else {
							JOptionPane.showMessageDialog(adaptee, "此卡無管理員權限");
							System.out.println("此管理员不存在");// 打印结果
						}
					} else {
						JOptionPane.showMessageDialog(adaptee, "不合法卡號");
						System.out.println("不合法卡號，含有非數字字符或卡號長度不正確");
					}
					}
				}
			}else{
				String selectWorkShopNo = adaptee.comboBox1.getSelectedItem().toString();
				String selectLineNo = adaptee.comboBox2.getSelectedItem().toString();
				if (selectWorkShopNo.equals("--請選擇車間--")) {
					JOptionPane.showMessageDialog(adaptee, "請選擇車間!");
				} else {
					if(selectLineNo.equals("請選擇線號")){
						JOptionPane.showMessageDialog(adaptee, "請選擇線號!");
					}else{
						if(selectLineNo == "不需要選擇線號"){
							selectLineNo = null;
						}
					
						/*
						 * if(ShiftName.equals("日班")){ Shift="D"; }else
						 * if(ShiftName.equals("夜班")){ Shift="N"; }
						 */
							System.out.println("成功登陸！");
							JsonFileUtil jsonFileUtil = new JsonFileUtil();
							String fileName = "saveSelectWorkshopNo.json";
							JSONObject selectWorkshopNoJson = new JSONObject();
							selectWorkshopNoJson.put("workshopNo", selectWorkShopNo);
							jsonFileUtil.saveSelectWorkshopNo(selectWorkshopNoJson.toString(), fileName);
							
							String lfileName = "saveLineNo.json";
							if(selectLineNo != null){
								JSONObject selectLineNoJson = new JSONObject();
								selectLineNoJson.put("lineNo", selectLineNo);
								jsonFileUtil.saveSelectWorkshopNo(selectLineNoJson.toString(), lfileName);
							}else{
								jsonFileUtil.deleteSaveLineNo(lfileName);
							}
							
							int savelinesize= SwipeCardLogin.getTotal();
							String  linesize =String.valueOf(savelinesize);
							JsonFileUtil jsonFileUtil1 = new JsonFileUtil();
							String totalFileName="saveLineSize.json";
							JSONObject linesizeObject=new JSONObject();
							linesizeObject.put("LineSize", savelinesize);
							jsonFileUtil.saveSelectWorkshopNo(linesizeObject.toString(), totalFileName);
							adaptee.dispose();
							SwipeCard swipe = new SwipeCard(selectWorkShopNo,selectLineNo);
							// System.out.println("WorkShopNo: " +
							// selectWorkShopNo);
					}
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
