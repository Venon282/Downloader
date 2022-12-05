package downloader.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import downloader.fc.Downloader;

public class Main extends JFrame{
	//http://iihm.imag.fr/index.html
	static final int WIDTH = 600;
	static final int HEIGHT = 400;
	
	private JPanel downloads_box2;

	Main(String title,String[] args){
		super(title);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		downloads_box2 = new JPanel(new StackLayout());
		downloads_box2.setPreferredSize(new Dimension(WIDTH,HEIGHT));		
		JPanel controls_box = new JPanel(new BorderLayout());
		
		JTextField url_field=new JTextField();
		JButton add_button=new JButton("add");
		controls_box.add(url_field,BorderLayout.CENTER);
		controls_box.add(add_button,BorderLayout.EAST); 
		add_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String url=url_field.getText();
				JPanel telechargement_box = new_download(url);
				if(telechargement_box!=null) {
					downloads_box2.add(telechargement_box,BorderLayout.NORTH);
					downloads_box2.revalidate();
					url_field.setText("");
				}else {
					url_field.setText("ERROR download : " + url_field.getText());
				}
			}
		});
		
		//add download arguments
		for(int i=0;i<args.length;i++) {
			JPanel telechargement_box = new_download(args[i]);
			if(telechargement_box!=null) {
				downloads_box2.add(telechargement_box,BorderLayout.NORTH);
				downloads_box2.revalidate();
			}
		}

		add(controls_box,BorderLayout.SOUTH);
		add(downloads_box2);
		pack();
	}
	
	JPanel new_download(String url) {
		Downloader download = Down(url);
		if(download!=null) {
			JPanel download_box=new JPanel(new BorderLayout());
			
			JPanel download_buttons=new JPanel(new BorderLayout());
			JPanel download_down=new JPanel(new BorderLayout());
			download_box.add(download_buttons,BorderLayout.EAST);
			download_box.add(download_down,BorderLayout.CENTER);
			
			JToggleButton play=new JToggleButton("▶");
			JButton delete=new JButton("❌");
			download_buttons.add(play,BorderLayout.WEST);
			download_buttons.add(delete,BorderLayout.CENTER);
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					download.remove();
					download_box.setVisible(false);
				}
			});
			play.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(play.isSelected()) {
						play.setText("| |");
						download.pause();
					}else {
						play.setText("▶");
						download.play();
					}
					
				}
				
			});
	
			JLabel url_label=new JLabel(url);
			JProgressBar bar=new JProgressBar(0);
			bar.setForeground(Color.blue);
			bar.setStringPainted(true);
			download_down.add(url_label,BorderLayout.NORTH);
			download_down.add(bar,BorderLayout.CENTER);
			
			download.execute();
			new Download(download,bar);
			
			return download_box;
		}else return null;
	}
	
	Downloader Down(String url){
		Downloader downloader = null;
		try {
			downloader = new Downloader(url);
		}
		catch(RuntimeException e) {
			System.err.format("skipping %s %s\n", url, e);
		}
		System.out.format("Downloading %s:", downloader);
		return downloader;
	}
	
	public static void main(String[] argv) {
		final String[] expressions = argv;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				new Main("downloader", expressions).setVisible(true); 
			}
		});
	}

}
