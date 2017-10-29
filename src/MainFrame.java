import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

/** 
 * Okno programu.
 * 
 * @author Anna Sprysak
 */
public class MainFrame extends JFrame{
	JPanel graphicPanel;
	JPanel controlPanel;
	
	private static final long serialVersionUID = 2269971701250845501L;

	MainFrame(){
	
		int frameWidth = 900;
		int frameHeight = 520;
		int screenWidth = this.getToolkit().getScreenSize().width;
		int screenHeight = this.getToolkit().getScreenSize().height;
		this.setLocation((screenWidth - frameWidth) / 2, (screenHeight - frameHeight) / 2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.graphicPanel = new JPanel();
		this.graphicPanel.setLayout(new BorderLayout());
		graphicPanel.setBackground(Color.WHITE);
		graphicPanel.setSize(600, frameHeight);
		this.setSize(frameWidth, frameHeight);
		this.add(graphicPanel, BorderLayout.CENTER);
		this.controlPanel = new JPanel();
		this.controlPanel.setSize((int)(frameWidth - graphicPanel.getSize().getWidth()), frameHeight);	
		this.add(controlPanel, BorderLayout.EAST);
		this.setVisible(true);
	}
}
