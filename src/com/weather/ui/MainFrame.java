package com.weather.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.weather.io.ConfigIO;
import com.weather.io.WeatherDto;

public class MainFrame {
	private String[] icon = {
	    "cloudy", "default","dust","haze",
		"heavyrain","heavysnow","lightrain","lightsnow",
		"moderatesnow", "moderaterain", "night_cloudy", "night_shower",
		"night_sunny", "overcast", "shower", "sleet",
		"snowstorm", "storm" ,"sunny", "thundershower",
		"typhoon"
	};
	protected final static String[] WEACOLNAMES = {
		"星期", "温度", "天气", "风向", "风力"
	};
	protected final static String[] AQICOLNAMES = {
		"监测点", "AQI", "空气质量指数类别", "首要污染物", "PM2.5/h", "PM2.5/24h"
	};
	protected final static String[] SKINNAMES = {
		"arc", "hanksite", "harmony", "headcase", "heresjohnny",
		"hexography_blue", "hexography_salmon", "hextract", "iconoclast",
		"network", "quartz", "slantstyle", "slantstlyeMax"
	};
	private Box box = new Box(BoxLayout.Y_AXIS);
	private Box butBox = new Box(BoxLayout.X_AXIS);
	private Box labBox = new Box(BoxLayout.X_AXIS);
	private Box weaBox = new Box(BoxLayout.Y_AXIS);
	private int xOld, yOld;
	protected  Color white = new Color(255, 255, 255);
	protected Color cellColor = new Color(0, 100, 100);
	
	protected JPopupMenu popMenu = new JPopupMenu();
	protected JMenu skinMenu = new JMenu("皮肤");
	protected JMenuItem[] skin = new JMenuItem[SKINNAMES.length];
	protected JMenuItem help = new JMenuItem("帮助");
	protected JMenuItem about = new JMenuItem("关于");
	protected JTable jTable = new JTable(4, 5);
	protected DefaultTableModel tableModel = new DefaultTableModel();
	protected JScrollPane jsp = new JScrollPane(jTable);
	protected ImageIcon[] img = new ImageIcon[icon.length];
	protected ImageIcon aboutImage = new ImageIcon("resources/images/about.gif");
	protected JLabel picLabel = new JLabel();
	protected JLabel areaLabel = new JLabel();
	protected JLabel weaLabel = new JLabel();
	protected JLabel temLabel = new JLabel("");
	protected Font labFont = new Font("Times", Font.BOLD, 20);
	protected Font texFont = new Font("Times", Font.BOLD, 10);
	protected Font butFont = new Font("Times",Font.BOLD,12);
	protected JButton weather = new JButton("天气");
	protected JButton aqi = new JButton("aqi");
	protected JButton area = new JButton("城市");
	protected JButton update = new JButton("更新");
	protected JButton exit = new JButton("退出");
	protected JButton add = new JButton("添加");
    protected DefaultTableCellRenderer headRenderer = new DefaultTableCellRenderer();   
    protected DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer(); 
	protected JFrame frame = new JFrame();
	protected JFrame cityFrame = new JFrame();
    protected ImagePanel imagePanel;
	protected WeatherDto dto;
	protected ConfigIO io;

	protected MainFrame() {
		
		dto = new WeatherDto();
		//通过ConfigIO对象来获得上一次的系统配置
		io = new ConfigIO(dto);
		//重获配置
		dto = io.getConfig();

		
		cityFrame.setLayout(new GridLayout(5, 3));
		cityFrame.setTitle("城市管理器");
		for (int i = 0; i < dto.getCityList().size(); i++) {
			cityFrame.add(dto.getCityList().get(i));
		}
		cityFrame.add(add);
		cityFrame.pack();
		cityFrame.setLocationRelativeTo(null);
		
		imagePanel = new ImagePanel(dto.getSkinFlag());
		
		for (int i = 0; i < skin.length; i++) {
			skin[i] = new JMenuItem(SKINNAMES[i]);
			skinMenu.add(skin[i]);
		}
		
		for (int i = 0; i < img.length; i++) {
			img[i] = new ImageIcon("resources/images/weather/" + icon[i] + ".png");
		}
		picLabel.setIcon(img[10]);

		jsp.getViewport().setBackground(white);
		jTable.setShowHorizontalLines(false);
		jTable.setShowVerticalLines(false);
	    jTable.setPreferredScrollableViewportSize(new Dimension(400, 130));
	    jTable.setEnabled(false);
	    jTable.setBackground(white);
	    headRenderer.setBackground(white);
	    headRenderer.setForeground(cellColor);
	    cellRenderer.setForeground(cellColor);

		weather.setEnabled(false);
	    weather.setContentAreaFilled(false);
	    weather.setFocusPainted(false);
	    weather.setFont(butFont);
	    aqi.setContentAreaFilled(false);
	    aqi.setFocusPainted(false);	  
	    aqi.setFont(butFont);
	    area.setContentAreaFilled(false);
	    area.setFocusPainted(false);   
	    area.setFont(butFont);
	    update.setContentAreaFilled(false);
	    update.setFocusPainted(false);	
	    update.setFont(butFont);
	    exit.setContentAreaFilled(false);
	    exit.setFocusPainted(false);
	    exit.setFont(butFont);
	    
		areaLabel.setForeground(white);
		temLabel.setForeground(white);
		weaLabel.setForeground(white);
		weather.setForeground(white);
		aqi.setForeground(white);
		area.setForeground(white);
		update.setForeground(white);
		exit.setForeground(white);
		areaLabel.setFont(labFont);
		weaLabel.setFont(labFont);
		temLabel.setFont(labFont);
		
		popMenu.add(skinMenu);
		popMenu.add(help);
		popMenu.add(about);
		weaBox.add(areaLabel);
		weaBox.add(weaLabel);
		weaBox.add(temLabel);
		labBox.add(popMenu);
		labBox.add(picLabel);
		labBox.add(weaBox);
		butBox.add(weather);
		butBox.add(aqi);
		butBox.add(area);
		butBox.add(update);
		butBox.add(exit);
		box.add(labBox);
		box.add(butBox);
		box.add(jsp);
		imagePanel.setOpaque(false);
		imagePanel.add(box);
	    frame.add(imagePanel);
	    frame.setUndecorated(true); 
	    frame.getContentPane().setBackground(white);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseMotionListener(new MouseMotionAdapter() {  
		    @Override  
		    public void mouseDragged(MouseEvent e) {  
		        int xOnScreen = e.getXOnScreen();  
		        int yOnScreen = e.getYOnScreen(); 
		        int xx = xOnScreen - xOld;  
		        int yy = yOnScreen - yOld;  
		        frame.setLocation(xx, yy);  
		    }  
		});
		frame.addMouseListener(new MouseAdapter() {  
		    @Override  
		    public void mousePressed(MouseEvent e) {  
		        xOld = e.getX();  
		        yOld = e.getY(); 
		    }
		    public void mouseReleased(MouseEvent e)
		    {
		        //如果释放的是鼠标右键
		        if(e.isPopupTrigger())
		        	popMenu.show(frame,e.getX(),e.getY());
		    }
		});		
	}
}
