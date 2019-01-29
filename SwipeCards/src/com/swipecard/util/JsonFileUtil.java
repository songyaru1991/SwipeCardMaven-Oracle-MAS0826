package com.swipecard.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.swipecard.SwipeCardNoDB;

public class JsonFileUtil {
	  private static Logger logger = Logger.getLogger(JsonFileUtil.class);
	  static JsonFileUtil jsonFileUtil = new JsonFileUtil();
	  static String defaultWorkshopNo = jsonFileUtil.getSaveWorkshopNo();
	public static boolean createWorkshopNoJsonFile(String jsonString, String fileName) {
		boolean flag = true;
		// File.separator如果文件路径考虑跨平台,将分隔符用File.separator 代替
		// String filePath = System.getProperty("user.dir");
		String filePath = "D:/SwipeCard/logs/";
		String WorkshopNoSavePath = filePath + fileName;
		try {
			File file = new File(WorkshopNoSavePath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			jsonString = JsonFormatTool.formatJson(jsonString);

			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
		} catch (Exception e) {
			flag = false;
			logger.error("createJsonFile時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		}
		return flag;
	}

	public Object[] getWorkshopNoByJson() {
		String filePath = "D:/SwipeCard/logs/";
		String fileName = "WorkshopNo.json";
		String workshopNoSavePath = filePath + fileName;
		File file = new File(workshopNoSavePath);

		Object[] a = null;
		BufferedReader brRread = null;
		String workshopNoStr = "";
		JSONArray workshopNoJsonArray;
		try {
			if (!file.exists()) {
				a = new Object[1];
				a[0] = "";
			} else {
				InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				 brRread = new BufferedReader(streamReader);
			//	brRread = new BufferedReader(new FileReader(workshopNoSavePath));
				String tempString = null;
				while ((tempString = brRread.readLine()) != null) {
					workshopNoStr += tempString;
				}
				workshopNoJsonArray = new JSONArray(workshopNoStr);
				if (workshopNoJsonArray.length() > 0) {
					a = new Object[workshopNoJsonArray.length()];
					for (int i = 0; i < workshopNoJsonArray.length(); i++) {
						JSONObject workshopNoJson = workshopNoJsonArray.getJSONObject(i);
					//	System.out.println(workshopNoJson);
						a[i] = workshopNoJson.get("workshopNo");
					}
				}
				brRread.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("getWorkshopNoByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("getWorkshopNoByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("getWorkshopNoByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} finally {
			if (brRread != null) {
				try {
					brRread.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return a;
	}
	
	public JSONObject getSwipeCardRecordByJson(File swipeCardRecordFile) {

		BufferedReader brRread = null;
		String swipeCardRecordStr = "";
		JSONObject swipeCardRecordJson = null;	
		try {
			if (!swipeCardRecordFile.exists()) {
				System.out.println("本地log無刷卡記錄!");
			} else {
				InputStreamReader streamReader = new InputStreamReader(new FileInputStream(swipeCardRecordFile), "gbk");
				 brRread = new BufferedReader(streamReader);
				String tempString = null;
				while ((tempString = brRread.readLine()) != null) {
					swipeCardRecordStr += tempString;
				}
				swipeCardRecordJson = new JSONObject(swipeCardRecordStr);
							
				brRread.close();
				
			//	file.renameTo(new File(filePath+"swipeCardRecord" + DateGet.getDate()+"_"+new Date().getHours() + ".json"));  
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("getSwipeCardRecordByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("getSwipeCardRecordByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("getSwipeCardRecordByJson時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} finally {
			if (brRread != null) {
				try {
					brRread.close();
				} catch (IOException e) {
					logger.error("getSwipeCardRecordByJson時 Error，原因:"+e);
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
					e.printStackTrace();
				}
			}
		}
		return swipeCardRecordJson;
	}
	
	public Boolean changeSwipeRecordFileName(File swipeCardRecordFile) {
		boolean flag = true;
		try {
			String writtedBackDbfilePath = "D:/SwipeCard/logs/WritedBackDbLogs/";

			File writtedBackDbfile = new File(writtedBackDbfilePath);
			if (!writtedBackDbfile.exists()) {
				writtedBackDbfile.mkdirs();
			}

			String swipeCardLogfileName = swipeCardRecordFile.getName();
			String writtedBackDbfileName = swipeCardLogfileName.substring(0, swipeCardLogfileName.indexOf("."))
					+ FormatDateUtil.getHHMM() + ".json";
			flag = swipeCardRecordFile.renameTo(new File(writtedBackDbfilePath + writtedBackDbfileName));
			if (flag) {
				logger.info("修改回写过得刷卡记录" + swipeCardLogfileName + "文件名称 成功");
				System.out.println("修改回写过得刷卡记录" + swipeCardLogfileName + "文件名称 成功");
			} else {
				logger.error("修改回写过得刷卡记录" + swipeCardLogfileName + "文件名称 失败");
				System.out.println("修改回写过得刷卡记录" + swipeCardLogfileName + "文件名称 失败");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("修改回写过得刷卡记录log文件名称异常，原因:" + e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean saveSelectWorkshopNo(String jsonString, String fileName) {
		boolean flag = true;
		// File.separator如果文件路径考虑跨平台,将分隔符用File.separator 代替
		// String filePath = System.getProperty("user.dir");
		String filePath = "D:/SwipeCard/logs/";
		String WorkshopNoSavePath = filePath + fileName;
		try {
			File file = new File(WorkshopNoSavePath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			//jsonString = JsonFormatTool.formatJson(jsonString);

			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
		} catch (Exception e) {
			flag = false;
			logger.error("saveSelectWorkshopNo時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		}
		return flag;
	}

	public String getSaveWorkshopNo() {
		String filePath = "D:/SwipeCard/logs/";
		String fileName = "saveSelectWorkshopNo.json";
		String saveWorkshopNoPath = filePath + fileName;
		File file = new File(saveWorkshopNoPath);

		BufferedReader brRread = null;
		String selectWorkshopNo = "";
		JSONArray workshopNoJsonArray;
		try {
			if (!file.exists()) {
				selectWorkshopNo=null;
			} else {
				InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				 brRread = new BufferedReader(streamReader);
			//	brRread = new BufferedReader(new FileReader(workshopNoSavePath));
				String tempString = null;
				while ((tempString = brRread.readLine()) != null) {
					selectWorkshopNo += tempString;
				}
				JSONObject workshopNoJson  = new JSONObject(selectWorkshopNo);						
	
				selectWorkshopNo = workshopNoJson.get("workshopNo").toString();

				brRread.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("saveSelectWorkshopNo時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("saveSelectWorkshopNo時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("saveSelectWorkshopNo時 Error，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} finally {
			if (brRread != null) {
				try {
					brRread.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.error("saveSelectWorkshopNo時 Error building SqlSession，原因:"+e);
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
				}
			}
		}
		return selectWorkshopNo;
	}
	
	public String getSaveLineNo() {
		String filePath = "D:/swipeCard/logs/";
		String fileName = "saveLineNo.json";
		String saveLineNoPath = filePath + fileName;
		File file = new File(saveLineNoPath);

		BufferedReader brRread = null;
		String selectLineNo = "";
		try {
			//如果文件不存在
			if (!file.exists()) {
				selectLineNo=null;
			} else {
				InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				 brRread = new BufferedReader(streamReader);
			//	brRread = new BufferedReader(new FileReader(workshopNoSavePath));
				String tempString = null;
				while ((tempString = brRread.readLine()) != null) {
					selectLineNo += tempString;
				}
				JSONObject workshopNoJson  = new JSONObject(selectLineNo);						
	
				selectLineNo = workshopNoJson.get("lineNo").toString();

				brRread.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("getSaveLineNo時 Error building SqlSession，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("getSaveLineNo時 Error building SqlSession，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} finally {
			if (brRread != null) {
				try {
					brRread.close();
				} catch (IOException e) {
					logger.error("getSaveLineNo時 Error building SqlSession，原因:"+e);
					// e.printStackTrace();
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
					e.printStackTrace();
				}
			}
		}
		return selectLineNo;
	}
	
	public void deleteSaveLineNo(String lfileName) {
		// TODO Auto-generated method stub
		String filePath = "D:/swipeCard/logs/";
		String LineNoSavePath = filePath + lfileName;
		File file = new File(LineNoSavePath);
		if(file.exists()){
			file.delete();
		}
	}
	
	public JSONObject getLineNoByJson() {
		String filePath = "D:/swipeCard/logs/";
		String fileName = "LineNo.json";
		String LineNoSavePath = filePath + fileName;
		File file = new File(LineNoSavePath);
		JSONObject a = new JSONObject();
		BufferedReader brRread = null;
		String lineNoStr = "";
		try {
			if (!file.exists()) {
				a = null;
			} else {
				InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				 brRread = new BufferedReader(streamReader);
			//	brRread = new BufferedReader(new FileReader(workshopNoSavePath));
				String tempString = null;
				while ((tempString = brRread.readLine()) != null) {
					lineNoStr += tempString;
				}
				a = new JSONObject(lineNoStr);
				brRread.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("getLineNoByJson時 Error building SqlSession，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("getLineNoByJson時 Error building SqlSession，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error("getLineNoByJson時 Error building SqlSession，原因:"+e);
			SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
			e.printStackTrace();
		} finally {
			if (brRread != null) {
				try {
					brRread.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return a;
	}
	
	//取json存的linesize
			public String getLineSize(){
				String filePath = "D:/swipeCard/logs/";
				String fileName = "saveLineSize.json";
				String saveLineNoPath = filePath + fileName;
				File file = new File(saveLineNoPath);

				BufferedReader brRread = null;
				String selectlinesize = "";
				try {
					//如果文件不存在
					if (!file.exists()) {
						selectlinesize=null;
					} else {
						InputStreamReader streamReader = new InputStreamReader(new FileInputStream(file), "UTF-8");
						 brRread = new BufferedReader(streamReader);
					//	brRread = new BufferedReader(new FileReader(workshopNoSavePath));
						String tempString = null;
						while ((tempString = brRread.readLine()) != null) {
							selectlinesize += tempString;
						}
						JSONObject workshopNoJson  = new JSONObject(selectlinesize);						
			
						selectlinesize = workshopNoJson.get("LineSize").toString();

						brRread.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					logger.error("getSavelinesize時 Error building SqlSession，原因:"+e);
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					logger.error("getSaveLinesize時 Error building SqlSession，原因:"+e);
					SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
					e.printStackTrace();
				} finally {
					if (brRread != null) {
						try {
							brRread.close();
						} catch (IOException e) {
							logger.error("getSaveLinesize時 Error building SqlSession，原因:"+e);
							// e.printStackTrace();
							SwipeCardNoDB d = new SwipeCardNoDB(defaultWorkshopNo);
							e.printStackTrace();
						}
					}
				}
				return selectlinesize;
			}
	

}
