package com.weather.main;

import javax.swing.UIManager;

import com.weather.handle.WeatherHandle;

public class Main {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			new WeatherHandle();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}