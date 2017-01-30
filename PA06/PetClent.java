//*********************************************************************
//*                                                                   *
//* CIS611               Summer Session 2016                Bing Dou  *
//*                                                                   *
//*                    Program Assignment PA06                        * 
//*                                                                   *
//*                       Class Description                           *
//*                                                                   *
//*                                                                   *
//*           	           7/2/2016 Created                           *
//*                                                                   *
//*                    Saved in: PetClent.java                        *
//*                                                                   *
//*********************************************************************
//

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.Date;

public class PetClent extends JFrame {
	
	// Declare variables
	private String hostname;
	private int port;
	private Socket conn;
	ObjectOutputStream clientOutputStream;
	ObjectInputStream clientInputStream;
	public Message message;
	
	private String[] sexS = {"Male", "Female"};
	private String[] colorS = {"White", "Black", "B&W", "Red"};
	
	// Declare and create all GUI components 
	
	private JPanel p1;
	private JPanel p2;
	private JPanel p3;
	private JPanel p4;
	private JPanel pPI;
	private JPanel pB;
	private JPanel pS;
	
	private JLabel idLabel;
	public JTextField idText;
	
	private JLabel nameLabel;
	public JTextField nameText;
	
	private JLabel ownerFNameLabel;
	public JTextField ownerFNameText;
	
	private JLabel ownerLNameLabel;
	public JTextField ownerLNameText;
	
	private JLabel sexLabel;
	public JComboBox sexBox;
	
	private JLabel speciesLabel;
	public JTextField speciesText;
	
	private JLabel colorLabel;
	public JComboBox colorBox;
	
	private JLabel birthLabel;
	public JTextField birthText;
	
	public JButton viewButton;
	public JButton insertButton;
	public JButton updateButton;
	public JButton deleteButton;
	
	private JLabel statusLabel;
	
	
	private void initComponenet(){
		
		// initialize the GUI components
		idLabel = new JLabel("Pet ID:");
		idText = new JTextField(8);
		
		nameLabel = new JLabel("Name:");
		nameText = new JTextField(22);
		
		ownerFNameLabel = new JLabel("Owner FName:");
		ownerFNameText = new JTextField(10);
		
		ownerLNameLabel = new JLabel("Owner LName:");
		ownerLNameText = new JTextField(10);
		
		sexLabel = new JLabel("Sex:");
		sexBox = new JComboBox(sexS);
		
		speciesLabel = new JLabel("Species:");
		speciesText = new JTextField(8);
		
		colorLabel = new JLabel("Color:");
		colorBox = new JComboBox(colorS);
		
		birthLabel = new JLabel("Birth Date:");
		birthText = new JTextField(10);
		
		viewButton = new JButton("View");
		insertButton = new JButton("Insert");
		updateButton = new JButton("Update");
		deleteButton = new JButton("Delete");
		
		statusLabel = new JLabel("Status: ");
		
		p1 = new JPanel();
		p2 = new JPanel();
		p3 = new JPanel();
		p4 = new JPanel();
		pPI = new JPanel();
		pB = new JPanel();
		pS = new JPanel(new BorderLayout());
		
	}
		
	private void doTheLayout(){
			
		// Organize the components into GUI window
		p1.add(idLabel);
		p1.add(idText);
		p1.add(nameLabel);
		p1.add(nameText);
		
		p2.add(sexLabel);
		p2.add(sexBox);
		p2.add(speciesLabel);
		p2.add(speciesText);
		p2.add(colorLabel);
		p2.add(colorBox);
			
		p3.add(birthLabel);
		p3.add(birthText);
			
		p4.add(ownerFNameLabel);
		p4.add(ownerFNameText);
		p4.add(ownerLNameLabel);
		p4.add(ownerLNameText);
			
		pPI.add(p1);
		pPI.add(p2);
		pPI.add(p3);
		pPI.add(p4);
		pPI.setLayout(new GridLayout(4, 1, 20, 5));
		pPI.setBorder(new TitledBorder("Pet Information"));
		
		pB.add(viewButton);
		pB.add(insertButton);
		pB.add(updateButton);
		pB.add(deleteButton);
		
		pS.add(pB, BorderLayout.CENTER);
		
		Font font = new Font("Serif", Font.BOLD, 18);
		statusLabel.setFont(font);
		pS.add(statusLabel, BorderLayout.SOUTH);
			
		add(pPI);
		add(pS);
		
		setLayout(new GridLayout(2, 1, 10, 5));
			
		}
		
	public PetClent(String hostname, int port) throws IOException{
		
		// View Gui
		initComponenet();
		doTheLayout();
		pack();
		setTitle("PetClent");
		setLocationRelativeTo(null); // Center the frame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		// Get Connection
		conn = new Socket(hostname, port);
		clientOutputStream = new ObjectOutputStream(conn.getOutputStream());
		clientInputStream = new ObjectInputStream(conn.getInputStream());
		statusLabel.setText("Status: connected \n");
		
	    viewButton.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	            try {
					view_actionPerformed(e);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	    });
		
	    insertButton.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	            try {
					insert_actionPerformed(e);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	    });
	    
	    updateButton.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	            try {
					update_actionPerformed(e);
				} catch (ClassNotFoundException | ParseException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	    });
	    
	    deleteButton.addActionListener( new java.awt.event.ActionListener() {
	        public void actionPerformed(ActionEvent e){
	            try {
					delete_actionPerformed(e);
				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	            }
	    });

	}
	
	void view_actionPerformed(ActionEvent e) throws IOException, ClassNotFoundException {
		
		int id;
		String idStr = idText.getText().trim();
		Date birth = null;
		
		if(idStr.length() == 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't be empty.");
			return;
		}
		
		try {
			id = Integer.parseInt(idStr);				
		} 
		catch(NumberFormatException nfe) {
			statusLabel.setText("Invaild Input Data Format \n");
			System.out.println("Validation catched");
			JOptionPane.showMessageDialog(null, "Id is not a number, Please input a number");
			return;
		} 
		
		if(id < 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't smaller than 0.");
			return;
		}
		
		message = new Message (id, "", "", "", 'N', "", "", birth, 1);
		clientOutputStream.writeObject(message);
       	receivingMessage();
	}
	
	void insert_actionPerformed(ActionEvent e) throws IOException, ClassNotFoundException, ParseException {
		
		int id;
		String idStr = idText.getText().trim();
		String name = nameText.getText().trim();
		String ownerFName;
		if(ownerFNameText.getText().trim().matches("^[A-Z][A-Za-z]*[0-9]?[0-9]?$")){
			ownerFName = ownerFNameText.getText().trim();
		}
		else{
			JOptionPane.showMessageDialog(null, "First name is not right. \n1. It should be at least 1 characters or 1 character and 1 number. \n2. The first character should be uppercase. \n3. It can't include any punctuation marks or space. \n3. Name can include a up to 2-digit number but can't be a number.\n4. It can't be null. \nExamples: \nJobs or Henry1" );
			return;
		}
		String ownerLName;
		if(ownerLNameText.getText().trim().matches("^[A-Z][A-Za-z]*$")){
			ownerLName = ownerLNameText.getText().trim();
		}
		else{
			JOptionPane.showMessageDialog(null,"Last name is not right. \n1. It should be at least 1 characters. \n2. The first character should be uppercase. \n3. It can't include any punctuation marks, space, nor digital number. \n4. It can't be null. \nExample: \nSteve" );
			return;
		}
		char sex;
		String sexStr = sexBox.getSelectedItem().toString().trim();
		String species = speciesText.getText().trim();
		String color = colorBox.getSelectedItem().toString().trim();
		Date birth = null;
		String birthStr = birthText.getText().trim();
		
		if (idStr.length() == 0 || name.length() == 0 || ownerFName.length() == 0 || ownerLName.length() == 0 || birthStr.length() == 0){
			statusLabel.setText("Data Is Needed  \n");
			JOptionPane.showMessageDialog(null, "Data Is Needed  \n");
			return;
		}
		
		try {
			id = Integer.parseInt(idStr);				
		} 
		catch(NumberFormatException nfe) {
			statusLabel.setText("Invaild Input Data Format \n");
			System.out.println("Validation catched");
			JOptionPane.showMessageDialog(null, "Id is not a number, Please input a number");
			return;
		} 
		
		if(id < 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't smaller than 0.");
			return;
		}

		// Deal with single character
		if(sexStr.matches("Male")){
			sex = 'M';
		}
		else{
			sex = 'F';
		}
		
		// Deal with date
		// Input date validation
		if(!birthStr.matches("^(19|20)\\d{2}((0[1-9])|(1[0-2]))([0-2]\\d|30|31)$")){
			JOptionPane.showMessageDialog(null, "Date format is wrong. Please input as yyyyMMdd. Example: 19990909 \n*.Years only start with 19 or 20 \n*.months:01-12 \n*.days:01-31");
			return;
		}
		// Convert date format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
	    birth = sdf.parse(birthStr); 
		
		message = new Message (id, name, ownerFName, ownerLName, sex, species, color, birth, 2);
		clientOutputStream.writeObject(message);
       	receivingMessage();
	}
	
	void update_actionPerformed(ActionEvent e) throws ParseException, IOException, ClassNotFoundException {

		int id;
		String idStr = idText.getText().trim();
		String name = nameText.getText().trim();
		String ownerFName;
		if(ownerFNameText.getText().trim().matches("^[A-Z][A-Za-z]*[0-9]?[0-9]?$")){
			ownerFName = ownerFNameText.getText().trim();
		}
		else{
			JOptionPane.showMessageDialog(null, "First name is not right. \n1. It should be at least 1 characters or 1 character and 1 number. \n2. The first character should be uppercase. \n3. It can't include any punctuation marks or space. \n3. Name can include a up to 2-digit number but can't be a number.\n4. It can't be null. \nExamples: \nJobs or Henry1" );
			return;
		}
		String ownerLName;
		if(ownerLNameText.getText().trim().matches("^[A-Z][A-Za-z]*$")){
			ownerLName = ownerLNameText.getText().trim();
		}
		else{
			JOptionPane.showMessageDialog(null,"Last name is not right. \n1. It should be at least 1 characters. \n2. The first character should be uppercase. \n3. It can't include any punctuation marks, space, nor digital number. \n4. It can't be null. \nExample: \nSteve" );
			return;
		}
		char sex;
		String sexStr = sexBox.getSelectedItem().toString().trim();
		String species = speciesText.getText();
		String color = colorBox.getSelectedItem().toString().trim();
		Date birth = null;
		String birthStr = birthText.getText().trim();
		
		if (idStr.length() == 0 || name.length() == 0 || ownerFName.length() == 0 || ownerLName.length() == 0 || birthStr.length() == 0){
			statusLabel.setText("Data Is Needed  \n");
			JOptionPane.showMessageDialog(null, "Data Is Needed  \n");
			return;
		}
		
		try {
			id = Integer.parseInt(idStr);				
		} 
		catch(NumberFormatException nfe) {
			statusLabel.setText("Invaild Input Data Format \n");
			System.out.println("Validation catched");
			JOptionPane.showMessageDialog(null, "Id is not a number, please input a number");
			return;
		}
		
		if(id < 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't smaller than 0.");
			return;
		}

		// Deal with single character
		if(sexStr.matches("Male")){
			sex = 'M';
		}
		else{
			sex = 'F';
		}
		
		// Deal with date
		// Input date validation
		if(!birthStr.matches("^(19|20)\\d{2}((0[1-9])|(1[0-2]))([0-2]\\d|30|31)$")){
			JOptionPane.showMessageDialog(null, "Date format is wrong. Please input as yyyyMMdd. Example: 19990909 \n*.Years only start with 19 or 20\n*.months:01-12\n*.days:01-31");
			return;
		}
		// Convert date format
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
	    birth = sdf.parse(birthStr); 
		
		message = new Message (id, name, ownerFName, ownerLName, sex, species, color, birth, 3);
		clientOutputStream.writeObject(message);
       	receivingMessage();
	}
	
	void delete_actionPerformed(ActionEvent e) throws IOException, ClassNotFoundException {
		
		int id;
		String idStr = idText.getText().trim();
		Date birth = null;
		
		if(idStr.length() == 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't be empty.");
			return;
		}
		
		try {
			id = Integer.parseInt(idStr);				
		} 
		catch(NumberFormatException nfe) {
			statusLabel.setText("Invaild Input Data Format \n");
			System.out.println("Validation catched");
			JOptionPane.showMessageDialog(null, "Id is not a number, Please input a number");
			return;
		} 
		
		if(id < 0){
			idText.setText("");
			JOptionPane.showMessageDialog(null, "Id can't smaller than 0.");
			return;
		}
		
		message = new Message (id, "", "", "", 'N', "", "", birth, 4);
		clientOutputStream.writeObject(message);
       	receivingMessage();
	}
	
	private void receivingMessage() throws IOException, ClassNotFoundException {
		
		message = (Message)clientInputStream.readObject();
		String idStr = String.valueOf(message.id);
		String nameStr = message.name;
		String ownerFNameStr = message.ownerFName;
		String ownerLNameStr = message.ownerLName;
		String sexStr;
		if(String.valueOf(message.sex).matches("M")){
			sexStr = "Male";
		}
		else{
			sexStr = "Female";
		}
		String speciesStr = message.species;
		String birthStr = String.valueOf(message.birth);
		String colorStr = message.color;
		
		switch (message.opType) { 
		
		// View
		case 1: if(message.birth != null){
				// Set label
				statusLabel.setText("PET IS FOUND! \n");
				
				// Show in the fields
				idText.setText(idStr);
				nameText.setText(nameStr);
				ownerFNameText.setText(ownerFNameStr);
				ownerLNameText.setText(ownerLNameStr);
				sexBox.setSelectedItem(sexStr);
				birthText.setText(birthStr);
				speciesText.setText(speciesStr);
				colorBox.setSelectedItem(colorStr);
				}
				else{
					statusLabel.setText("THE PET DOSE NOT EXIST!"  + "\n");
					JOptionPane.showMessageDialog(null, "The pet does not exist!");
					idText.setText("");
					nameText.setText("");
					ownerFNameText.setText("");
					ownerLNameText.setText("");
					birthText.setText("");
					speciesText.setText("");
				}
				break;
		
		// Insert
		case 2: if(message.birth != null){
					statusLabel.setText("THE PET IS INSERTED " + "\n");
					
					idText.setText("");
					nameText.setText("");
					ownerFNameText.setText("");
					ownerLNameText.setText("");
					birthText.setText("");
					speciesText.setText("");
				}
				else{
					statusLabel.setText("THE PET ID EXIST"  + "\n");
					JOptionPane.showMessageDialog(null, "The pet id exist!");
			    }
		        break;
		
		// Update
		case 3: if(message.birth != null){
					statusLabel.setText("THE PET'S INFORMATION IS UPDATED " + "\n");
					
					idText.setText("");
					nameText.setText("");
					ownerFNameText.setText("");
					ownerLNameText.setText("");
					birthText.setText("");
					speciesText.setText("");
				}
				else{
					statusLabel.setText("FAIL TO UPDATE!"  + "\n");
					JOptionPane.showMessageDialog(null, "Fail to update, the pet does not exist!");
				}
                break;
                       
		// Delete
		case 4: if(message.id == -1){
					statusLabel.setText("THE PET DOES NOT EXIST!  "  + "\n");
					JOptionPane.showMessageDialog(null, "The pet does not exist, please check the id.");
				}
				else if(message.birth == null){
					statusLabel.setText("DELETED SUCCESSFULLY!  "  + "\n");
					JOptionPane.showMessageDialog(null, "Deleted successfully!");
					
					idText.setText("");
					nameText.setText("");
					ownerFNameText.setText("");
					ownerLNameText.setText("");
					birthText.setText("");
					speciesText.setText("");
                }
				else{
					statusLabel.setText("FAIL TO DELETE!  "  + "\n");
					JOptionPane.showMessageDialog(null, "Fail to delete!");
				}
                break;
		}
	}
	
	public static void main(String[] args) throws IOException{
		
		new PetClent("localhost", 8000); 
		
	}	
}
