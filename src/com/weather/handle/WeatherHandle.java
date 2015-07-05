package com.weather.handle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.weather.engine.Weather;
import com.weather.io.CityElement;
import com.weather.string.BasicString;
import com.weather.ui.CityPanel;
import com.weather.ui.MainFrame;

public class WeatherHandle extends MainFrame implements ActionListener {
			
	public WeatherHandle() {
		
		for (int i = 0; i < dto.getCityList().size(); i++) {
			dto.getCityList().get(i).getCityButton().addActionListener(this);
			dto.getCityList().get(i).getDelButton().addActionListener(this);
		}
		
		setWeather();
		setImage();
		
		jTable.getTableHeader().setDefaultRenderer(headRenderer);
		frame.setLocation(dto.getLocation());
		frame.setVisible(true);
		
		for (int i = 0; i < skin.length; i++) {
			skin[i].addActionListener(this);
		}
		help.addActionListener(this);
		about.addActionListener(this);
		weather.addActionListener(this);
		aqi.addActionListener(this);
		area.addActionListener(this);
		update.addActionListener(this);
		exit.addActionListener(this);
		add.addActionListener(this);
		frame.addWindowListener(new WindowAdapter() {
			//关闭窗口按钮手动关闭
			public void windowClosing(WindowEvent e) {
				io.saveConfig();
		    }
			//非活动窗口
			public void windowDeactivated(WindowEvent e) {
				io.saveConfig();
		    }
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		// TODO Auto-generated method stub
		if (o == exit) {
			dto.setLocation(frame.getLocation());
			io.saveConfig();
			System.exit(0);
		}
		
		else if (o == weather) {
			setWeather();
			setImage();
			aqi.setEnabled(true);
			weather.setEnabled(false);
		}
		
		else if (o == aqi) {
			setAQI();	
			aqi.setEnabled(false);
			weather.setEnabled(true);

		}
		
		else if (o == area) {
			cityFrame.setVisible(true);
		}
		
		else if (o == update) {
			JSONArray json = null;
			try {
				if (aqi.isEnabled()) {
					//更新数据
					json = Weather.getWeatherJson(dto.getCityInfo().get(dto.getCityFlag()).getCityName());	
					dto.getCityInfo().get(dto.getCityFlag()).setWeatherJson(json);
					//更新天气更新时间
					dto.getCityInfo().get(dto.getCityFlag()).setWeaUpdateTime(new Date());

					setWeather();
					setImage();
				}
				else {
				    //aqi更新时间大于10分钟就更新一次
				    if (System.currentTimeMillis() - dto.getCityInfo().get(dto.getCityFlag()).getAqiUpdateTime().getTime() > 600000) {
						try {
							json = Weather.getAQIJson(dto.getCityInfo().get(dto.getCityFlag()).getCityName());	
							dto.getCityInfo().get(dto.getCityFlag()).setAqiJson(json);
							//更新aqi更新时间
							dto.getCityInfo().get(dto.getCityFlag()).setAqiUpdateTime(new Date());
						} catch (JSONException e1) {
							JOptionPane.showMessageDialog(frame, "AQI暂时无法更新: " + e1.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
						}
				    }
				    else {
						JOptionPane.showMessageDialog(frame, BasicString.TENAQI, "ERROR", JOptionPane.WARNING_MESSAGE);
				    }
					setAQI();
				}
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(frame, e1.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);

			}
		}
		
		else if (o == help) {
			JOptionPane.showMessageDialog(frame, BasicString.HELP, 
					"帮助", JOptionPane.INFORMATION_MESSAGE);
		}
		
		else if (o == about) {
			JOptionPane.showMessageDialog(frame, BasicString.ABOUT, "关于", JOptionPane.INFORMATION_MESSAGE, aboutImage);
		}
		
		else if (o instanceof JMenuItem) {
			for (int i = 0; i < skin.length; i++) {
				if (o == skin[i]) {
					imagePanel.updateUI(i);
					dto.setSkinFlag(i);
					break;
				}
			}
		}
		
		else if (o == add) {
			String str = JOptionPane.showInputDialog(cityFrame, "请输入城市名(中文or拼音)", "城市", JOptionPane.INFORMATION_MESSAGE);
			if (str != null && str.length() > 0) {
				try {
					Date date = new Date();
					//更新天气
					CityElement t = new CityElement(str);
		        	t.setWeatherJson(Weather.getWeatherJson(str));
					t.setWeaUpdateTime(date);
					dto.getCityInfo().add(t);
					dto.setCityFlag(dto.getCityInfo().size() - 1);
		        	//如果更新天气成功则改变窗体和cityFrame
					cityFrame.remove(add);
			 		CityPanel c = new CityPanel(str);
			 		cityFrame.add(c);
			 		c.getCityButton().addActionListener(this);
			 		c.getDelButton().addActionListener(this);
			 		dto.getCityList().add(c);
					cityFrame.add(add);
					cityFrame.repaint();
					cityFrame.pack();
					weather.setEnabled(false);
					aqi.setEnabled(true);
					setWeather();
					setImage();
					
					//因为api支持城市比较少，所以将更新aqi放在最后来更新aqi
					dto.getCityInfo().get(dto.getCityInfo().size() - 1).setAqiUpdateTime(date);
					dto.getCityInfo().get(dto.getCityInfo().size() - 1).setAqiJson(Weather.getAQIJson(str));			        
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(frame, e1.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
				} catch (UnknownHostException e2) {
					JOptionPane.showMessageDialog(frame, e2.getMessage(), "ERROR", JOptionPane.WARNING_MESSAGE);
				}
			}
		}
		
		else {
			for (int i = 0; i < dto.getCityList().size(); i++) {
				if (o == dto.getCityList().get(i).getDelButton()) {
					if (dto.getCityInfo().size() == 1) {
						JOptionPane.showMessageDialog(cityFrame, "请至少保留一个城市", "ERROR", JOptionPane.ERROR_MESSAGE);
						break;
					}
					cityFrame.remove(dto.getCityList().get(i));
					cityFrame.repaint();
					cityFrame.pack();
					dto.getCityList().remove(i);
					dto.getCityInfo().remove(i);
					if (i < dto.getCityFlag() || (i == dto.getCityFlag() && i == dto.getCityList().size())) {
						dto.setCityFlag(dto.getCityFlag() - 1);
					}
					if (i == dto.getCityFlag() || i == dto.getCityList().size()) {
						setImage();
						setWeather();
						aqi.setEnabled(true);
					}
					break;
				}
				else if (o == dto.getCityList().get(i).getCityButton()) {
					dto.setCityFlag(i);
					if (weather.isEnabled()) {
						setAQI();
						JSONArray json = dto.getCityInfo().get(dto.getCityFlag()).getWeatherJson();
						areaLabel.setText(json.getJSONObject(0).getString("citynm"));
						weaLabel.setText(json.getJSONObject(0).getString("weather"));
						temLabel.setText(json.getJSONObject(0).getString("temperature"));
						aqi.setEnabled(false);
						weather.setEnabled(true);
					}
					else {
						setWeather();
						weather.setEnabled(false);
						aqi.setEnabled(true);
					}
					setImage();
					break;
				}
			}
		}
	}
	
	private void setWeather() {
		
		JSONArray json = dto.getCityInfo().get(dto.getCityFlag()).getWeatherJson();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm"); 

		areaLabel.setText(json.getJSONObject(0).getString("citynm"));
		weaLabel.setText(json.getJSONObject(0).getString("weather"));
		temLabel.setText(json.getJSONObject(0).getString("temperature"));
		Object[][] weaObj = new Object[json.size() + 1][5];
		for (int j = 0; j < json.size(); j++) {
			ArrayList<String> list = new ArrayList<String>();
			list.add(format(json.getJSONObject(j).getString("week")));
			list.add(json.getJSONObject(j).getString("temperature"));
			list.add(json.getJSONObject(j).getString("weather"));
			list.add(json.getJSONObject(j).getString("wind"));
			list.add(json.getJSONObject(j).getString("winp"));
			weaObj[j] = list.toArray();
		}
		weaObj[json.size()][3] = "更新时间:";
		weaObj[json.size()][4] = sdf.format(dto.getCityInfo().get(dto.getCityFlag()).getWeaUpdateTime());
		tableModel.setDataVector(weaObj, WEACOLNAMES);
		jTable.setModel(tableModel);
		jTable.getColumn("星期").setCellRenderer(cellRenderer);
		jTable.getColumn("温度").setCellRenderer(cellRenderer);
		jTable.getColumn("天气").setCellRenderer(cellRenderer);
		jTable.getColumn("风向").setCellRenderer(cellRenderer);
		jTable.getColumn("风力").setCellRenderer(cellRenderer);
		frame.pack();
	}
	
	private void setAQI() {
		JSONArray json = dto.getCityInfo().get(dto.getCityFlag()).getAqiJson();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm MM-dd"); 
		
		Object[][] aqiObj = new Object[json.size() + 1][6];
		for (int i = 0; i < json.size(); i++) {
			ArrayList<String> list = new ArrayList<String>();
			if (i != json.size() - 1) {
				list.add(json.getJSONObject(i).getString("area") + json.getJSONObject(i).getString("position_name"));
			}
			else {
				list.add(json.getJSONObject(i).getString("area"));
			}
			list.add(json.getJSONObject(i).getString("aqi"));
			list.add(json.getJSONObject(i).getString("quality"));
			list.add(json.getJSONObject(i).getString("primary_pollutant"));
			list.add(json.getJSONObject(i).getString("pm2_5"));
			list.add(json.getJSONObject(i).getString("pm2_5_24h"));
			aqiObj[i] = list.toArray();
		}
		aqiObj[json.size()][4] = "更新时间:";
		aqiObj[json.size()][5] = sdf.format(dto.getCityInfo().get(dto.getCityFlag()).getAqiUpdateTime());
		tableModel.setDataVector(aqiObj, AQICOLNAMES);
		jTable.setModel(tableModel);
		jTable.getColumn("监测点").setCellRenderer(cellRenderer);
		jTable.getColumn("AQI").setCellRenderer(cellRenderer);
		jTable.getColumn("空气质量指数类别").setCellRenderer(cellRenderer);
		jTable.getColumn("首要污染物").setCellRenderer(cellRenderer);
		jTable.getColumn("PM2.5/h").setCellRenderer(cellRenderer);
		jTable.getColumn("PM2.5/24h").setCellRenderer(cellRenderer);
		frame.pack();
	}
	
	private void setImage() {
		JSONArray json = dto.getCityInfo().get(dto.getCityFlag()).getWeatherJson();
		
		int i = Integer.parseInt(json.getJSONObject(0).getString("weatid"));
		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY); 
		while (true) {
			if (i == 1) {
				if (hour >= 6 && hour <= 18) {
					picLabel.setIcon(img[18]);
				}
				else {
					picLabel.setIcon(img[12]);
				}
				break ;
			}
			else if (i == 2) {
				if (hour >= 6 && hour <= 18) {
					picLabel.setIcon(img[0]);
				}
				else {
					picLabel.setIcon(img[10]);
				}
				break ;
			}
			else if (i == 4) {
				if (hour >= 6 && hour <= 18) {
					picLabel.setIcon(img[14]);
				}
				else {
					picLabel.setIcon(img[11]);
				}
				break ;
			}
			switch (i) {
				case 3: picLabel.setIcon(img[13]); break;
				case 5: picLabel.setIcon(img[19]); break;
				case 6: picLabel.setIcon(img[19]); break;
				case 7: picLabel.setIcon(img[15]); break;
				case 8: picLabel.setIcon(img[6]); break;
				case 9: picLabel.setIcon(img[6]); break;
				case 10: picLabel.setIcon(img[9]); break;
				case 11: picLabel.setIcon(img[4]); break;
				case 12: picLabel.setIcon(img[4]); break;
				case 13: picLabel.setIcon(img[17]); break;
				case 14: picLabel.setIcon(img[7]); break;
				case 15: picLabel.setIcon(img[7]); break;
				case 16: picLabel.setIcon(img[8]); break;
				case 17: picLabel.setIcon(img[5]); break;
				case 18: picLabel.setIcon(img[16]); break;
				case 19: picLabel.setIcon(img[3]); break;
				case 20: picLabel.setIcon(img[4]); break;
				case 21: picLabel.setIcon(img[2]); break;
				case 22: picLabel.setIcon(img[9]); break;
				case 23: picLabel.setIcon(img[4]); break;
				case 24: picLabel.setIcon(img[4]); break;
				case 25: picLabel.setIcon(img[17]); break;
				case 26: picLabel.setIcon(img[17]); break;
				case 27: picLabel.setIcon(img[7]); break;
				case 28: picLabel.setIcon(img[8]); break;
				case 29: picLabel.setIcon(img[5]); break;
				case 30 : picLabel.setIcon(img[2]); break;
				case 31 : picLabel.setIcon(img[2]); break;
				case 32 : picLabel.setIcon(img[2]); break;
				case 33 : picLabel.setIcon(img[2]); break;
			}
			break;
		 }
	}
	
	private String format(String s) {
		switch(s) {
			case "星期日": return "周日";
			case "星期一": return "周一";
			case "星期二": return "周二";
			case "星期三": return "周三";
			case "星期四": return "周四";
			case "星期五": return "周五";
			case "星期六": return "周六";
		}
		return s;	
	}
}
