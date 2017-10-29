import javax.swing.JTextField;

/** 
 * Klasa rozszerzająca JTextField.
 * 
 * @author Anna Sprysak
 */
public class ExtendedTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	/** 
	 * Konstruktor przyjmujący obiekt typu String.
	 * 
	 * @param text wyświetlany tekst
	 */
	public ExtendedTextField(String text) {
		super(text);
		setColumns(5);
		setHorizontalAlignment(JTextField.RIGHT);
	}

}
