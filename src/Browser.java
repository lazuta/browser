import java.io.IOException;
import java.util.ArrayList;
import com.sun.javafx.application.PlatformImpl;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javafx.scene.web.WebView;
import javafx.scene.Scene;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

public class Browser extends JFrame{

	private JTextField addressField;
	private JFXPanel display;
	private ArrayList<String> history = new ArrayList<String>();
	private int historyIndex = -1;
	WebView web;
	
	public Browser() throws IOException {
		super("Julia-WEB");

		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));

		Image left_arrow = ImageIO.read(getClass().getResource("img/left_arrow.png"));
		Image right_arrow = ImageIO.read(getClass().getResource("img/right_arrow.png"));
		Image refreshImg = ImageIO.read(getClass().getResource("img/refresh.png"));

		JButton back = new JButton();
		back.setIcon(new ImageIcon(left_arrow));
		back.addActionListener(
				event -> Platform.runLater(this::back)
		);

		JButton forward = new JButton();
		forward.setIcon(new ImageIcon(right_arrow));
		forward.addActionListener(
				event -> Platform.runLater(this::forward)
		);

		JButton refresh = new JButton();
		refresh.setIcon(new ImageIcon(refreshImg));
		refresh.addActionListener(
				event -> Platform.runLater(() -> web.getEngine().load(addressField.getText()))
		);

		addressField = new JTextField("http://",80);
		addressField.addActionListener(
				event -> Platform.runLater(() -> loadSite(event.getActionCommand()))
		);

		Font font = new Font("Arial", Font.PLAIN,14);
		addressField.setFont(font);

		toolbar.add(back);
		toolbar.add(forward);
		toolbar.add(refresh);
		toolbar.add(addressField);
		add(toolbar, BorderLayout.NORTH);

		display = new JFXPanel();
		PlatformImpl.startup(() -> {
			web = new WebView();
			display.setScene(new Scene(web));
		});
		add(display, BorderLayout.CENTER);

		setSize(1440,900);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void loadSite(@org.jetbrains.annotations.NotNull String url) {
		if(url.length() < 7 || !url.substring(0, 7).equals("http://")) {
			url = "http://" + url;
		}
		web.getEngine().load(url);
		addressField.setText(url);
			
		if(history.size() > historyIndex+1) {
			history.subList(historyIndex+1, history.size()).clear();
		}
			
		history.add(url);
		historyIndex += 1;
	}
	
	private void back() {
		if(historyIndex > 0) {
			historyIndex--;
			String url = history.get(historyIndex);
			web.getEngine().load(url);
			addressField.setText(url);
		}
	}
	
	private void forward() {
		if(historyIndex+1 < history.size()) {
			historyIndex++;
			String url = history.get(historyIndex);
			web.getEngine().load(url);
			addressField.setText(url);
		}
	}
	
}
