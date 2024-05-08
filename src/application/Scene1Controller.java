package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Scene1Controller {
	@FXML
	private Button imageSelectButton = new Button();
	private String imagePath = null;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private static ImageFFT imageFFT;
	@FXML
	private Label pathLabel = new Label();
	public void imageSelectButtonEvent(ActionEvent event) {
		/* file filter*/
		List<String> fileFilter = new ArrayList<String>();
		fileFilter.add("*.jpg");
		fileFilter.add("*.png");
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPEG/PNG (*.jpg; *.png)", fileFilter));
		File selectedImage = fc.showOpenDialog(null);
		
		if(selectedImage != null) {
			imagePath = selectedImage.getAbsolutePath();
			pathLabel.setWrapText(true);
			pathLabel.setText(imagePath);
			System.out.printf(imagePath);
		}
	}
	public void transformButtonEvent(ActionEvent event){
		try{
			imageFFT = new ImageFFT(ImageIO.read(new File(imagePath)));
			root = FXMLLoader.load(getClass().getResource("Scene2.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e) {
			event.consume();
			Warning(stage, "No image has been selected.");
		}
	}
	/* warning user error is happened.*/
	public void Warning(Stage stage, String s) {
		/* Alert*/
		Alert alert = new Alert(AlertType.WARNING, null);
		alert.setTitle("Warning");
		alert.setHeaderText(null);
		alert.setContentText(s);
		
		/* Dialog style*/
		DialogPane closeDialog = alert.getDialogPane();
		closeDialog.getStyleClass().add("closeDialog");
		alert.showAndWait();
	}
	public static ImageFFT transform() {
		return imageFFT;
	}
	public void twoImageBtnEvent(ActionEvent event) {
		try {
			root = FXMLLoader.load(getClass().getResource("Scene3.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
		catch(IOException e) {}
		
	}
}
