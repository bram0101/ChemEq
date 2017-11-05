/*
 * MIT License
 * 
 * Copyright (c) 2017 Bram Stout
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package me.bramstout.chemeq;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

	private Scene mainWindow;
	private BorderPane root;
	private VBox inputPane;
	private TextField inputTextField;
	private InputDisplay inputDisplay;

	@Override
	public void start(Stage arg0) throws Exception {
		final double windowWidth = 1280, windowHeight = 720;
		
		root = new BorderPane();

		inputPane = new VBox();
		inputPane.maxWidthProperty().bind(root.widthProperty());
		inputPane.minWidthProperty().bind(root.widthProperty());
		inputPane.setPadding(new Insets(windowWidth * 0.16666666, windowWidth * 0.16666666, windowWidth * 0.16666666,
				windowWidth * 0.16666666));
		root.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
				inputPane.setPadding(
						new Insets(newVal.doubleValue() * 0.16666666 * 0.5, newVal.doubleValue() * 0.16666666,
								newVal.doubleValue() * 0.16666666 * 0.5, newVal.doubleValue() * 0.16666666));
			}

		});

		inputTextField = new TextField();
		inputTextField.setFont(new Font(Font.getDefault().getFamily(), 16));
		inputPane.getChildren().add(inputTextField);

		inputDisplay = new InputDisplay(inputTextField);
		inputPane.getChildren().add(inputDisplay);
		root.setTop(inputPane);
		Button button = new Button();
		root.setCenter(button);

		mainWindow = new Scene(root, windowWidth, windowHeight);

		arg0.setTitle("ChemEq " + Main.VERSION);
		arg0.setScene(mainWindow);
		arg0.show();
	}

	public static void run(String[] args) {
		App.launch(args);
	}

}
