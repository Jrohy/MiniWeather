package com.weather.ui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ImagePanel extends JPanel {
	private String imStr[] = {
			"arc.jpg", "hanksite.jpg", "harmony.jpg", "headcase.png",
			"heresjohnny.jpg", "hexography_blue.png", "hexography_salmon.png", "hextract.png",
			"iconoclast.png", "network.jpg", "quartz.jpg", "slantstyle.jpg", "slantstyleMax.jpg"
			
	};
	private ImageIcon[] icon = new ImageIcon[imStr.length];
	private int flag;
	
	ImagePanel(int flag) {
		this.flag = flag;
		for (int i = 0; i < icon.length; i++) {
			icon[i] = new ImageIcon("resources/images/skins/" + imStr[i]);
		}
	}
	
	public void updateUI(int flag) {
		this.flag = flag;
		repaint();
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(icon[flag].getImage(), 0, 0, this);
	}
}
