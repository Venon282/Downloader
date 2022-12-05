package downloader.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JProgressBar;

import downloader.fc.Downloader;

public class Download extends Thread{
	Downloader download;
	JProgressBar bar;
	
	Download(Downloader download,JProgressBar bar){
		this.download=download;
		this.bar=bar;
		this.start();
	}
	
	public void run(){
		this.download.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				try {
					int value=Integer.parseInt(evt.getNewValue().toString());
					bar.setValue(value);
					bar.setString(value+" %");
					
				}catch (NumberFormatException e) {
					System.out.println(e);
				}

				System.out.flush();
			}
		});
	}
}
