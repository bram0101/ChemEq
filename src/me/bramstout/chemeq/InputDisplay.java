package me.bramstout.chemeq;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class InputDisplay extends HBox{
	
	public InputDisplay(TextField inputTextField) {
		minWidthProperty().bind(inputTextField.widthProperty());
		minHeightProperty().bind(inputTextField.heightProperty());
		maxWidthProperty().bind(inputTextField.widthProperty());
		maxHeightProperty().bind(inputTextField.heightProperty());
		setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(0), new Insets(0))));
		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				inputTextField.requestFocus();
				inputTextField.deselect();
				inputTextField.end();
			}
			
		});
		
		inputTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				parse(newValue);
			}
			
		});
	}
	
	public void parse(String value) {
		getChildren().clear();
		int index = 0;
		while(index < value.length()) {
			index = parseMolecule(value, index);
			index++;
		}
	}
	
	public int parseMolecule(String value, int index) {
		StringBuilder builder = new StringBuilder();
		char firstChar = value.charAt(index);
		char c = firstChar;
		while(true) {
			builder.append(c);
			index++;
			if(index >= value.length())
				break;
			c = value.charAt(index);
			if((!Character.isLetter(c) && !Character.isDigit(c)))
				break;
		}
		getChildren().add(new Label(builder.toString()));
		return index;
	}

}
