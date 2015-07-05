package com.weather.ui;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CityPanel extends JPanel {
	private JButton cityButton = new JButton();
	private JButton delButton = new JButton("x");
	
	public CityPanel(String nameStr) {
		cityButton.setText(nameStr);
		add(cityButton);
		add(delButton);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	public JButton getCityButton() {
		return cityButton;
	}

	public JButton getDelButton() {
		return delButton;
	}
}
