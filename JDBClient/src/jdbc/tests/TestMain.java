package jdbc.tests;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.SQLWarning;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import jdbc.model.Table;
import jdbc.model.controller.CommandLineErrorHandler;
import jdbc.model.controller.ConnectionController;
import jdbc.model.controller.DB2ConnectionController;
import jdbc.model.controller.ErrorHandler;
import jdbc.model.controller.WarningDetectedEvent;
import jdbc.model.controller.WarningDetectedListener;

public class TestMain implements ActionListener, WarningDetectedListener {
	
	private JTextField urlField;
	private JTextField userField;

	private JPasswordField passwordField;

	private JButton button;
	private JTextArea textArea;

	
	public static void main(String [] args) throws SQLWarning {
		TestMain main = new TestMain();
		main.init();	
	}
	
	private void init() {
		//chiedo url user password
				String url = "diva.deis.unibo.it:50000/SIT_STUD";
				String user = "00767320";
				JFrame frame = new JFrame();
				JPanel panel = new JPanel();
				urlField = new JTextField();
				userField = new JTextField();
				passwordField = new JPasswordField();
				button = new JButton("Send");
				textArea = new JTextArea();
				//textArea.
				textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				textArea.setWrapStyleWord(true);
				textArea.setLineWrap(true);
				urlField.setText(url);
				userField.setText(user);
				{
					JPanel north = new JPanel();
					panel.setLayout(new BorderLayout());
					north.setLayout(new GridLayout(4,1));
					north.add(urlField);
					north.add(userField);
					north.add(passwordField);
					north.add(button);
					panel.add(north,BorderLayout.NORTH);
				}
				
				panel.add(textArea, BorderLayout.CENTER);		
				
				button.addActionListener(this);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				frame.setSize(new Dimension(200,200));
				frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ConnectionController controller = new DB2ConnectionController();
		try {
			controller.connectTo(urlField.getText(), userField.getText(), new String(passwordField.getPassword()));
			
			String singleLineQuery = "SELECT * FROM AUTO\n";
			Table table = controller.executeQuery(singleLineQuery);
			textArea.append(table.toString());
			controller.disconnect();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

				
	}

	@Override
	public void WarningDetected(WarningDetectedEvent warning) {
		ErrorHandler handler = new CommandLineErrorHandler();
		handler.handleSQLWarning(warning.getWarning());
	}
	
}
