package simulator.model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Weather {
	SUNNY, CLOUDY, RAINY, WINDY, STORM;
	
	public Image getImage() {
		String img = null;
		
		switch (this) {
		case SUNNY:
			img = "sun";
			break;
		case CLOUDY:
			img = "cloud";
			break;
		case RAINY:
			img = "rain";
			break;
		case WINDY:
			img = "wind";
			break;
		case STORM:
			img = "storm";
			break;
		}
		
		try {
			return ImageIO.read(new File("resources/icons/" + img + ".png"));
		} catch (IOException e) {
			return null;
		}
	}
}
