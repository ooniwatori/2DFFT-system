package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Scene2Controller implements Initializable {
	@FXML
	private ImageView originalImageR = new ImageView();
	@FXML
	private ImageView FFT1RM = new ImageView();
	@FXML
	private ImageView FFT1RP = new ImageView();
	@FXML
	private ImageView FFT2RM = new ImageView();
	@FXML
	private ImageView FFT2RP = new ImageView();
	@FXML
	private ImageView FFT2RMShift = new ImageView();
	@FXML
	private ImageView Filter = new ImageView();
	@FXML
	private ImageView processedImageR = new ImageView();
	@FXML
	private ImageView inverseR = new ImageView();
	@FXML
	private ComboBox<String> filterComboBox;
	private String[] filter = {"Low pass filter", "High pass filter", "Band pass filter", "Band reject filter"};
	
	@FXML
	private Slider omega1Slider = new Slider();
	@FXML
	private Slider omega2Slider = new Slider();
	
	@FXML
	private Label omega1Label = new Label();
	@FXML
	private Label omega2Label = new Label();
	
	@FXML
	private Pane omega1Pane = new Pane();
	@FXML
	private Pane omega2Pane = new Pane();
	
	private int omega0;
	private int omega1;
	private int type = 0;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		filterComboBox.setItems(FXCollections.observableArrayList(filter));
		omega1Pane.setVisible(false);
		omega2Pane.setVisible(false);
		
		
		omega1Slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				omega0 = (int)omega1Slider.getValue();
				omega1Label.setText(Integer.toString(omega0));
				transformRealTime();
			}
		});
		
		omega2Slider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
				omega1 = (int)omega2Slider.getValue();
				omega2Label.setText(Integer.toString(omega1));
				transformRealTime();
			}
		});
		
		originalImageR.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getOriginalImage(), null));
		FFT1RM.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getImageFFT1Magnitude(), null));
		FFT1RP.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getImageFFT1Phase(), null));
		FFT2RM.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getImageFFT2Magnitude(), null));
		FFT2RP.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getImageFFT2Phase(), null));
		FFT2RMShift.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getImageFFT2Magnitude(), null));
		processedImageR.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getProcessedFFT2(), null));
		inverseR.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getProcessedImage(), null));
		Filter.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getFilter(), null));
	}
	
	@FXML
	public void filterBoxAction(ActionEvent event){
		omega0 = (int)omega1Slider.getValue();
		omega1 = (int)omega2Slider.getValue();
		switch(getFilter()) {
		case "Low pass filter":
			System.out.printf("A");
			omega1Pane.setVisible(true);
			omega2Pane.setVisible(false);
			omega1Label.setText(Integer.toString(omega0));
			type = 1;
			break;
		case "High pass filter":
			omega1Pane.setVisible(true);
			omega2Pane.setVisible(false);
			omega1Label.setText(Integer.toString(omega0));
			type = 2;
			break;
		case "Band pass filter":
			omega1Pane.setVisible(true);
			omega2Pane.setVisible(true);
			omega1Label.setText(Integer.toString(omega0));
			omega2Label.setText(Integer.toString(omega1));
			type = 3;
			break;
		case "Band reject filter":
			omega1Pane.setVisible(true);
			omega2Pane.setVisible(true);
			omega1Label.setText(Integer.toString(omega0));
			omega2Label.setText(Integer.toString(omega1));
			type = 4;
			break;
		}
		Scene1Controller.transform().filterApply(omega0, omega1, type);
	}
	
	@FXML
	public String getFilter() {
		return filterComboBox.getSelectionModel().getSelectedItem();
	}
	
	/* do transform in real time*/
	public void transformRealTime() {
		Scene1Controller.transform().filterApply(omega0, omega1, type);
		processedImageR.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getProcessedFFT2(), null));
		inverseR.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getProcessedImage(), null));
		Filter.setImage(SwingFXUtils.toFXImage(Scene1Controller.transform().getFilter(), null));
	}
}
