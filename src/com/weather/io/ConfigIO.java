package com.weather.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONArray;
import com.weather.engine.Weather;

@SuppressWarnings("serial")
public class ConfigIO implements Serializable {
	  private WeatherDto dto;
	  private Key key;
	  private ObjectOutputStream out;
	  private ObjectInputStream in;
	  private final static String CONFIG_PATH = "data/config.db";
	  private final static String KEY_PATH = "data/key.db";

	  public ConfigIO(WeatherDto dto) {
		  this.dto = dto;
		  this.readConfig();
	  }

	  private void readConfig() {
		  try {	
			  //如果文件不存在，创建文件
			  if (!new File(CONFIG_PATH).exists() || !new File(KEY_PATH).exists()) {
				  //先连接天气测试网络，若失败则不创建文件
				  JSONArray json = Weather.getWeatherJson(dto.getCityInfo().get(0).getCityName());
				  new File("data").mkdirs();
				  new File(CONFIG_PATH).createNewFile();
				  new File(KEY_PATH).createNewFile();
				  
				  //指定算法,这里为DES
			      KeyGenerator kg = KeyGenerator.getInstance("DES", "SunJCE");
	              //指定密钥长度,长度越高,加密强度越大
	              kg.init(56);
	              //产生密钥
	              key = kg.generateKey();
	              //输出密钥
	              out = new ObjectOutputStream(new 
	            		  BufferedOutputStream(new FileOutputStream(KEY_PATH)));
	              out.writeObject(key);
	              out.close();
	              				  
				//首次运行初始化
				  Date date = new Date();
			      dto.getCityInfo().get(0).setWeaUpdateTime(date);
			      dto.getCityInfo().get(0).setWeatherJson(json);
				  dto.getCityInfo().get(0).setAqiJson(Weather.getAQIJson(dto.getCityInfo().get(0).getCityName()));
				  dto.getCityInfo().get(0).setAqiUpdateTime(date);
			  }
			  else {
				  //读取密钥
                  in = new ObjectInputStream(new
                        BufferedInputStream(new FileInputStream(KEY_PATH)));
                  key = (Key)in.readObject();
                  //读取对象
				  Cipher cipher = Cipher.getInstance("DES");
	              cipher.init(Cipher.DECRYPT_MODE, key);
				  in = new ObjectInputStream(new CipherInputStream(new
		                    BufferedInputStream(new FileInputStream(CONFIG_PATH)),cipher));
				  dto = (WeatherDto)in.readObject();
				  in.close();
			  }
		  } catch (Exception e) {
			// TODO Auto-generated catch block
			  if (e.getMessage().equals("当前已离线")) {
				  JOptionPane.showMessageDialog(null, "当前已离线，请连网", "离线", JOptionPane.INFORMATION_MESSAGE);
				  System.exit(0);
		      }
			  e.printStackTrace();
		  }
	  }

	  public void saveConfig(){
		  try {
			  Cipher cipher = Cipher.getInstance("DES");
              cipher.init(Cipher.ENCRYPT_MODE, key);
			  out = new ObjectOutputStream(new FileOutputStream(CONFIG_PATH));
              out = new ObjectOutputStream(new CipherOutputStream(new
                      BufferedOutputStream(new FileOutputStream(CONFIG_PATH)), cipher));
			  out.writeObject(dto);
			  out.close();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
	  }	
	  /**
	  * @return 返回配置
	  */
	  public WeatherDto getConfig(){
		  return this.dto;
	  }
}
