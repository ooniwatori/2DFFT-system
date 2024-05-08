package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class Scene3Controller implements Initializable{
	@FXML
	private ImageView origin1 = new ImageView();
	@FXML
	private ImageView FFT1 = new ImageView();
	@FXML
	private ImageView change1 = new ImageView();
	@FXML
	private ImageView result1 = new ImageView();
	@FXML
	private ImageView origin2 = new ImageView();
	@FXML
	private ImageView FFT2 = new ImageView();
	@FXML
	private ImageView change2 = new ImageView();
	@FXML
	private ImageView result2 = new ImageView();

	private String imagePath1;
	private String imagePath2;
	
	private ImageFFT imageFFT1;
	private ImageFFT imageFFT2;
	
	
	public void imageSelectButton1Event(ActionEvent event) {
		/* file filter*/
		List<String> fileFilter = new ArrayList<String>();
		fileFilter.add("*.jpg");
		fileFilter.add("*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG/PNG (*.jpg; *.png)", fileFilter));
		File selectedImage = fc.showOpenDialog(null);
		
		if(selectedImage != null) {
			imagePath1 = selectedImage.getAbsolutePath();
		}
		try {
			imageFFT1 = new ImageFFT(ImageIO.read(new File(imagePath1)));
			origin1.setImage(SwingFXUtils.toFXImage(imageFFT1.getOriginalImage(), null));
			FFT1.setImage(SwingFXUtils.toFXImage(imageFFT1.getImageFFT2Magnitude(), null));
			
			
		}
		catch(IOException e) {}
		
	}
	public void imageSelectButton2Event(ActionEvent event) {
		/* file filter*/
		List<String> fileFilter = new ArrayList<String>();
		fileFilter.add("*.jpg");
		fileFilter.add("*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG/PNG (*.jpg; *.png)", fileFilter));
		File selectedImage = fc.showOpenDialog(null);
		
		if(selectedImage != null) {
			imagePath2 = selectedImage.getAbsolutePath();
		}
		try {
			imageFFT2 = new ImageFFT(ImageIO.read(new File(imagePath2)));
			origin2.setImage(SwingFXUtils.toFXImage(imageFFT2.getOriginalImage(), null));
			FFT2.setImage(SwingFXUtils.toFXImage(imageFFT2.getImageFFT2Magnitude(), null));
			
		}
		catch(IOException e) {}
	}

	public void mixBtn(ActionEvent event) {
		if(imagePath1 != null && imagePath2 != null) {
			imageFFT1.applyPhaseMix(imageFFT2.getPhase());
			imageFFT2.applyPhaseMix(imageFFT1.getPhase());
			change1.setImage(SwingFXUtils.toFXImage(imageFFT1.getProcessedFFT2(), null));
			change2.setImage(SwingFXUtils.toFXImage(imageFFT2.getProcessedFFT2(), null));
			result1.setImage(SwingFXUtils.toFXImage(imageFFT1.getProcessedImage(), null));
			result2.setImage(SwingFXUtils.toFXImage(imageFFT2.getProcessedImage(), null));
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
