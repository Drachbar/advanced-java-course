package application;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;

import controller.Controller;

public class App {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		
		//String NimbusLookAndFeel = ("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		
		//UIManager.setLookAndFeel(NimbusLookAndFeel);
		UIManager.setLookAndFeel(new FlatDarkLaf());
		
		SwingUtilities.invokeLater(Controller::new);
		
	}

}
