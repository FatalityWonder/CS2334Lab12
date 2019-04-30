import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class to create a GUI for an example program to accept form submissions. The program keeps a list of several
 * forms that are being filled out. Each form holds some information about the user, such as their name, email,
 * and signature. The JComboBox at the top of the program is used to swap between forms that are being filled out.
 * Each form can be filled out separately, and information about each form is stored in a FormData object. These
 * objects are stored in a list and selected for editing with the ComboBox.
 *
 * The following is required of your program:
 * (1) Your frame layout should roughly match the layout demonstrated in lab and uploaded to canvas.
 * (2) You should be able to use the text fields to modify a FormData object. Pressing the "Save" button will
 *     attempt to set the values of the currently selected formdata (as corresponding to the currently selected
 *     index from the ComboBox). Use the values in the text fields to set the values of the formdata. Pressing "Reset"
 *     will clear all fields of the formdata. Pressing "New Form" will generate a new formdata for editing.
 * (3) You should be able to export all of your stored forms (i.e. export the datalist object).
 * (4) You should be abel to import a set of stored forms (i.e. import a list of FormData into the datalist object).
 * (5) You should not serialize the Social Security Numbers (see the FormData class).
 *
 * Note: the different forms are represented in the ComboBox by the display names.
 *
 * Follow the TODOs to complete your code.
 *
 * @author Stephen
 * @version 2019-04-24
 */
public class DataEntryFrame extends JFrame
{
	/**
	 * Users may fill out multiple forms at once. Only one form can be displayed at once, however.
	 * As such, users may cycle through this list to edit different forms.
	 */
	private ArrayList<FormData> datalist = new ArrayList<FormData>();
	private JComboBox<String> formSelect = new JComboBox<String>();

	/**
	 * Function used for refreshing the combo box contents. Populates the box with the display names.
	 */
	private DefaultComboBoxModel<String> getComboBoxModel(List<FormData> data)
	{
		ArrayList<String> displayNames = new ArrayList<String>();
		for (FormData form : data)
		{
			displayNames.add(form.getDisplayName());
		}
		String[] comboBoxModel = displayNames.toArray(new String[displayNames.size()]);
	    return new DefaultComboBoxModel<>(comboBoxModel);
	}

	/**
	 * Identifying Information:
	 */
	private JLabel firstNameInfo = new JLabel("First Name:");
	private JTextField firstName = new JTextField(15);
	private JLabel midddleInitialInfo = new JLabel("Middle Initial:");
	private JTextField middleInitial = new JTextField(1);
	private JLabel lastNameInfo = new JLabel("Last Name:");
	private JTextField lastName = new JTextField(15);
	private JLabel displayNameInfo = new JLabel("Display Name:");
	private JTextField displayName = new JTextField(15);
	private JLabel SSNInfo = new JLabel("Social Security Number:");
	private JTextField SSN = new JTextField(15);

	/**
	 * Contact information:
	 */
	private JLabel phoneInfo = new JLabel("Phone Number:");
	private JTextField phone = new JTextField(15);
	private JLabel emailInfo = new JLabel("Email Address:");
	private JTextField email = new JTextField(15);
	private JLabel addressInfo = new JLabel("Street Address:");
	private JTextField address = new JTextField(15);

	/**
	 * User verification:
	 */
	private JLabel signatureInfo = new JLabel("Signature:");
	private SignaturePanel spanel = new SignaturePanel();

	/**
	 * Translate stored form information into visual update...
	 */
	private void setVisuals(FormData data)
	{
		firstName.setText(data.getFirstName());
		middleInitial.setText(Character.toString(data.getMiddleInitial()));
		lastName.setText(data.getLastName());
		displayName.setText(data.getDisplayName());
		SSN.setText(data.getSSN());
		phone.setText(data.getPhone());
		email.setText(data.getEmail());
		address.setText(data.getAddress());	
		spanel.setSignature(data.getSignature());
	}

	/**
	 * Error/confirmation message:
	 */
	private JTextField errorField = new JTextField("No Errors");

	public DataEntryFrame()
	{
		this.setLayout(new GridLayout(7, 1));

		// Add initial form:
		datalist.add(new FormData());
		datalist.get(0).setValues("fn", 'm', "ln", "dn", "111111111", "1234567890",
				"test@ou.edu", "111 first st", new ArrayList<Point>());
		this.setVisuals(datalist.get(0));

		// Add in the form selector:
		DefaultComboBoxModel<String> comboBoxModel = getComboBoxModel(datalist);
		formSelect.setModel(comboBoxModel);
		formSelect.setSelectedIndex(0);
		formSelect.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();
			this.setVisuals(datalist.get(select));
		});
		this.add(formSelect);

		JPanel formFill = new JPanel(new GridBagLayout());
		GridBagConstraints grid = new GridBagConstraints();

		this.add(formFill);
		

	    grid.anchor = GridBagConstraints.WEST;
	    grid.fill = GridBagConstraints.BOTH;
		grid.gridx = 0;
		grid.gridy = 0;
		grid.weightx = 1.0;
		grid.weighty = 1.0;
		
		formFill.add(firstNameInfo, grid);
		
		grid.gridy = 1;
		formFill.add(midddleInitialInfo, grid);
		
		grid.gridy = 2;
		formFill.add(lastNameInfo, grid);
		
		grid.gridy = 3;
		formFill.add(displayNameInfo, grid);
		
		grid.gridy = 4;
		formFill.add(SSNInfo, grid);
		
		grid.gridy = 5;
		formFill.add(phoneInfo, grid);
		
		grid.gridy = 6;
		formFill.add(emailInfo, grid);
		
		grid.gridy = 7;
		formFill.add(addressInfo, grid);
		
		grid.gridx = 1;
		grid.gridy = 0;
		formFill.add(firstName, grid);
		
		grid.gridy = 1;
		formFill.add(middleInitial, grid);
		
		grid.gridy = 2;
		formFill.add(lastName, grid);
		
		grid.gridy = 3;
		formFill.add(displayName, grid);
		
		grid.gridy = 4;
		formFill.add(SSN, grid);
		
		grid.gridy = 5;
		formFill.add(phone, grid);
		
		grid.gridy = 6;
		formFill.add(email, grid);
		
		grid.gridy = 7;
		formFill.add(address, grid);
		
		// Add in the signature panel:
		spanel.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent e) {}

			@Override
			public void mouseDragged(MouseEvent e)
			{
				spanel.addPoint(e.getPoint());
				
				repaint();
			}
		});
		this.add(signatureInfo);
		this.add(spanel);

		// Add in the form create, save, and reset panel:
		JPanel formHandling = new JPanel(new GridLayout(1, 3));
		JButton createForm = new JButton("New Form");
		createForm.addActionListener((e) -> {
			FormData newData = new FormData();
			newData.setValues("fn", 'm', "ln", "dn", "111111111", "1234567890",
					"test@ou.edu", "111 first st", new ArrayList<Point>());
			datalist.add(newData);
			int select = datalist.size() - 1;
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);
			this.setVisuals(datalist.get(select));
		});

		JButton saveForm = new JButton("Save");
		saveForm.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();

			boolean valueSet = datalist.get(select).setValues(
						firstName.getText(),
						middleInitial.getText().charAt(0), 
						lastName.getText(),
						displayName.getText(),
						SSN.getText(),
						phone.getText(),
						email.getText(),
						address.getText(),
						spanel.getSignature());

			this.setVisuals(datalist.get(select));
			
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);

			if (valueSet)
			{
				errorField.setText("Failed to set form Information!");
			}
			else
			{
				errorField.setText("Form information sucessfully updated!");
			}
		});

		JButton resetForm = new JButton("Reset");
		resetForm.addActionListener((e) -> {
			int select = formSelect.getSelectedIndex();
			
			datalist.get(select).reset();
			
			this.setVisuals(datalist.get(select));
		});

		formHandling.add(createForm);
		formHandling.add(saveForm);
		formHandling.add(resetForm);
		
		this.add(formHandling);

		// Add in the error message field:
		this.errorField.setEditable(false);
		this.add(errorField);
		
		// Add in the import/export panel:
		JPanel importExport = new JPanel(new GridLayout(1, 2));
		JButton importButton = new JButton("Import");
		
		importButton.addActionListener((e) -> {
			JFileChooser chooser = new JFileChooser();
			    
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getName());
			    }
			
			    ArrayList<FormData> importList = new ArrayList<FormData>();
			    
			boolean fail = false;
			
			try {
				FileInputStream fileIn = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
            	ObjectInputStream in = new ObjectInputStream(fileIn);
            	
            	Object current = new Object();
            	while ((current = in.readObject()) != null)
            	{
            		importList.add((FormData) current);
            	}
            	
            	datalist = importList;
            	
            	in.close();
            	fileIn.close();
			}
			catch (IOException | ClassNotFoundException d)
			{
				fail = true;
			}
			
			if (fail)
			{
				errorField.setText("Could not read from file!");
			}
			else
			{
				errorField.setText("Data Sucessfully imported from file!");
			}
					
        	// Use this code snippet to reset visuals after importing:
            int select = 0;
			DefaultComboBoxModel<String> newComboBoxModel = getComboBoxModel(datalist);
			formSelect.setModel(newComboBoxModel);
			formSelect.setSelectedIndex(select);
			this.setVisuals(datalist.get(select));
			
		});
		JButton exportButton = new JButton("Export");
		exportButton.addActionListener((e) -> {

			JFileChooser chooser = new JFileChooser();			    
			    int returnVal = chooser.showOpenDialog(getParent());
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getName());
			    }
			
			boolean fail = false;
			
			try {
				FileOutputStream fileOut = new FileOutputStream(chooser.getSelectedFile().getAbsolutePath());
            	ObjectOutputStream out = new ObjectOutputStream(fileOut);
            	
            	for(int i = 0; i < datalist.size(); ++i)
            	{
            		out.writeObject(datalist.get(i));
            	}
            	
            	out.close();
            	fileOut.close();
			}
			catch (IOException d)
			{
				fail = true;
			}
			
			if (fail)
			{
				errorField.setText("Could not write to file!");
			}
			else
			{
				errorField.setText("Data Sucessfully saved to file!");
			}
		});

		importExport.add(importButton);
		importExport.add(exportButton);
		this.add(importExport);

		// JFrame basics:
		this.setTitle("Example Form Fillout");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(500, 700);
		this.setVisible(true);
	}

	public static void main(String[] args)
	{
		new DataEntryFrame();
	}
}
