package com.hk.panelbuilders;
import javax.swing.*;
import com.hk.components.*;
import com.hk.ui.HomePage;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteDiaryPanelCreator extends ReadWriteUtils implements Runnable{
	private UserProfile currentuser = CurrentUser.getInstance();
	private JPanel writeDiaryPanel;
	private JLabel greetMessage,dayInfo,lblPickDate;
	private JButton next,setDate; 
	private boolean isDateSet = false;
	public boolean panelupdated = false;

	
	private void initComponents() {
		writeDiaryPanel = new JPanel();
		writeDiaryPanel.setLayout(null);
		writeDiaryPanel.setBounds(166, 0, 628, 575);
		//welcome user
		greetMessage = new JLabel("Welcome "+currentuser.getUserName().toUpperCase());
		greetMessage.setFont(new Font("Script MT Bold", Font.PLAIN, 20));
		greetMessage.setBounds(22, 9, 179, 21);
		//pick date label
		lblPickDate = new JLabel("Pick Date:");
		lblPickDate.setFont(new Font("Viner Hand ITC", Font.PLAIN, 16));
		lblPickDate.setBounds(352, 9, 81, 25);
		//date chooser
		dateChooser.setBounds(443, 9, 91, 20);
		System.out.println("before set jd "+dateChooser.getDateFormatString());
		System.out.println("bfore set pd "+page.getDate());
		//day info
		dayInfo = new JLabel("Click SET to select the date");
		dayInfo.setBounds(22, 61, 286, 14);
		//content field
		contentField.setText("Start writing here");
		//set date button
		setDate = new JButton("SET");
		setDate.setBounds(544, 9, 74, 23);
		//save button
		next = new JButton("NEXT");
		next.setBounds(255, 530, 81, 23);
		//star rater
		rating.setBounds(255, 496, 81, 25);
	}
	
	private void addComponents() {
		toggleComponents(false);
		writeDiaryPanel.add(lblPickDate);
		writeDiaryPanel.add(contentScroll);
		writeDiaryPanel.add(next);
		writeDiaryPanel.add(dateChooser);
		writeDiaryPanel.add(greetMessage);
		writeDiaryPanel.add(dayInfo);	
		writeDiaryPanel.add(setDate);		
		writeDiaryPanel.add(rating);
		
	}
	
	@Override
	public void run() {

		
		initComponents();
		addComponents();
		
		//content field focus
		contentField.addFocusListener(new FocusAdapter() {
		@Override
		public void focusGained(FocusEvent arg0) {
			if(contentField.getText().equals("Start writing here"))
				contentField.setText("");
		}
		@Override
		public void focusLost(FocusEvent arg0) {
			if(contentField.getText().equals(""))
				contentField.setText("Start writing here");
		}
	});	
		
	//set button action	
	setDate.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			    panelupdated = false;
				selectedDate = DateConverter.convertDate(dateChooser);
				toggleComponents(true);
				//to check if set is pressed while in same page
				CustomDate lastDate = (page.getDate().equals(new CustomDate()))?DateConverter.convertDate(CurrentDay.getDate()):page.getDate();
				System.out.println("last date "+lastDate);
				System.out.println("selected date "+selectedDate);
				boolean samepage = selectedDate.equals(lastDate);
				System.out.println("samp is "+samepage);
				if(!samepage) {
				if(isAlreadyWritten())
				{ 
					
					try {
						page = getDiaryPage(DateConverter.convertDate(dateChooser));
						System.out.println("after set pd "+page.getDate());
						int option = readOrEditDialog();
						if(option==0) {
							HomePage.read.updateFields(page);
							HomePage.replacePanel(HomePage.read.getPanel());
							
							dateChooser.setDate(DateConverter.convertfromCustom(lastDate));
							page.setDate(lastDate);
						}
						else if(option==1) {
							panelupdated = true;
						updateEditFields(page);
						}
						else {
							page.setDate(lastDate);
								dateChooser.setDate(DateConverter.convertfromCustom(lastDate));		
							}
					} catch (IOException | ClassNotFoundException e) {
						e.printStackTrace();
					} 
				}
				else{
					resetDiaryPage();
				}
				}
		}
	});
	
	//save button action
		next.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			
			if(contentField.getText().equals("") || contentField.getText().equals("Start writing here"))
			{
				Object[] option = {"I get it","My Bad!"};
				JOptionPane.showOptionDialog(HomePage.getFrame(),"Uh Oh! Can't save an empty page",
						"",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,option,option[0]);
			}
			else {	
			try {
				page.setContent(contentField.getText());
				page.setRating(rating.getSelection());
				InsightQuestionDialog();
				if(!new File(reviseFileName()).exists()) {
				HighlightsManager(ADD_ENTRY, page.getDate());
				}
				EncryptFile();				
				JOptionPane.showConfirmDialog(HomePage.getFrame(),"Diary Updated! If you want to make changes, edit and save again!",
						"Saved",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon("green_tick.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}	
			}
		}
	});
	
		
	}
	
	private void InsightQuestionDialog() {
		QADialogCreator qaDialog;
		qaDialog = new QADialogCreator(page.getQAData(), QADialogCreator.WRITE_MODE);		
		qaDialog.showDialog();
		page.setQAData(qaDialog.getQAData());
	}
	
	private void EncryptFile() throws IOException {
		File f = new File(reviseFileName());
		f.getParentFile().mkdirs();
		FileOutputStream outputStream = new FileOutputStream(reviseFileName());
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
	    out.writeObject(page);
	    outputStream.close();
	}
	
	public boolean dateBoundary() {
		Object[] option = {"I get it","My Bad"};
		if(new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate()).equals(new SimpleDateFormat("dd/MM/yyyy").format(CurrentDay.getDate()))) {
			return true;
		}
		else if(new SimpleDateFormat("dd/MM/yyyy").format(dateChooser.getDate()).equals(new SimpleDateFormat("dd/MM/yyyy").format(DateConverter.convertfromCustom(currentuser.getDob())))) {
			return true;
		}
		else if(dateChooser.getDate().after(CurrentDay.getDate())) {
			JOptionPane.showOptionDialog(HomePage.getFrame(),"We strongly believe you can't know your future",
					"",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,option,option[0]);
			return false;
		}	
		else if(dateChooser.getDate().before(DateConverter.convertfromCustom(currentuser.getDob()))){
			JOptionPane.showOptionDialog(HomePage.getFrame(),"Well we don't see those dates in your life",
					"",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,option,option[0]);
			return false;
		}
		return true;
	}
	
	public boolean isAlreadyWritten() {
		File f = new File(reviseFileName());
		return f.length()!=0;
	}
	
	public int readOrEditDialog() {
		Object[] option = {"Read","Edit"};
		return JOptionPane.showOptionDialog(HomePage.getFrame(),"You already updated diary for this day. Do you want to edit?",
				"",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,option,option[0]);
	}
	
	public void updateEditFields(DiaryPage newpage) {
		page = newpage;
		toggleComponents(true);
		dateChooser.setDate(DateConverter.convertfromCustom(page.getDate()));	
		dayInfo.setText("You are editing entry for: "+ new SimpleDateFormat("dd/MM/yyyy").format(DateConverter.convertfromCustom(page.getDate())));
		contentField.setText(page.getContent());
		rating.setSelection(page.getRating());
	}
	
	public void resetDiaryPage() {
		page = new DiaryPage(DateConverter.convertDate(dateChooser), "", 0);
		contentField.setText("Start writing here");
		rating.setSelection(0);
		dayInfo.setText("You are making entry for: "+ new SimpleDateFormat("dd/MM/yyyy").format(DateConverter.convertfromCustom(page.getDate())));
	}
	
	public void toggleComponents(boolean switcher) {
		contentField.setEnabled(switcher);
		rating.setEnabled(switcher);
		next.setEnabled(switcher);
	}
	
	
	public JPanel getPanel() {
		return writeDiaryPanel;
		}

	
}
