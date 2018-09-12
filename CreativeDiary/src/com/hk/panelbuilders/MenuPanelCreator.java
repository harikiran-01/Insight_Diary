package com.hk.panelbuilders;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.hk.ui.HomePage;

public class MenuPanelCreator {
	private JPanel writeDiaryPanel = HomePage.write.getPanel();
	private JPanel readDiaryPanel = HomePage.read.getPanel();
	private JPanel menuPanel;
	private JButton btnWriteDiary, btnReadDiary, btnExit;
	public MenuPanelCreator() {
		initComponents();
		addComponents();
		//write button action
		btnWriteDiary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomePage.replacePanel(writeDiaryPanel);
			}
		});
		
		//read diary action
		btnReadDiary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				HomePage.replacePanel(readDiaryPanel);
			}
		});
		
		//exit button action
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int choice = JOptionPane.showConfirmDialog(HomePage.getFrame(), "Any unsaved information will be lost. Are you sure?",
						"Exit",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
				if(choice==0)
				HomePage.getFrame().dispose();
			}	
		});
	}
	
	private void addComponents() {
		menuPanel.add(btnWriteDiary);
		menuPanel.add(btnReadDiary);
		menuPanel.add(btnExit);
	}

	private void initComponents() {
		//menu panel
		menuPanel = new JPanel();
		menuPanel.setBounds(0, 0, 167, 475);
		menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 50));
		//write button
		btnWriteDiary = new JButton("Write Diary");
		//read button
		btnReadDiary = new JButton("Read Diary");
		//exit button
		btnExit = new JButton("Exit");
	}
	
	public JPanel getPanel() {
		return menuPanel;
	}
}