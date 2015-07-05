package com.weather.string;

public class BasicString {
	public final static String ABOUT = "MiniWeather 3.3.2\n2015.7.5\n";
	
	public final static String HELP = "1.因api访问次数限制原因，每个城市的aqi空气指数限制在每过10分钟才能更新一次\n"
	                                + "2.若要重置软件，则删除软件目录下的data文件夹\n"
			                        + "3.离线更新可能卡住(一阵子后恢复),所以尽量别离线更新\n"
	                                + "4.更新按钮不会同时更新天气与api,请分别更新";

	public final static String AQINULL = "Sorry，您这个小时内的API请求次数用完了，休息一下吧！";
	
	public final static String NOAQI = "该城市还未有PM2.5数据";
	
	public final static String NOWEA = "该城市还未有数据";
	
	public final static String TENAQI = "十分钟后再回来更新吧";
	
	public final static String OFFLINE = "当前已离线";
	
	public final static String AQIURL = "www.pm25.in";
	
	public final static String WEAURL = "api.k780.com";
}
