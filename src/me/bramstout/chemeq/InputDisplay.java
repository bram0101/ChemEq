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

import javafx.application.Platform;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;

public class InputDisplay extends VBox {

	private Parser parser;
	private Solver solver;

	public InputDisplay(TextField inputTextField) {
		parser = new Parser();
		solver = new Solver();

		minWidthProperty().bind(inputTextField.widthProperty());
		minHeightProperty().bind(inputTextField.heightProperty().multiply(2));
		maxWidthProperty().bind(inputTextField.widthProperty());
		maxHeightProperty().bind(inputTextField.heightProperty().multiply(2));
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
			public void changed(ObservableValue<? extends String> observable, String oldValue, String strValue) {
				String newValue = strValue;
				for (int i = 0; i < strValue.length(); i++) {
					int codePoint = strValue.codePointAt(i);
					int subVal = codePoint - 8320;
					if (subVal >= 0 && subVal < 10) {
						newValue = newValue.substring(0, i) + subVal + newValue.substring(i + 1, newValue.length());
					}
				}
				boolean hasChanged = false;
				String parsedVal = newValue;
				for (int i = 0; i < newValue.length(); i++) {
					if (Character.isDigit(newValue.codePointAt(i))) {
						boolean isIndex = false;
						int intVal = 0;
						int j = i - 1;
						while (j >= 0) {
							int codePoint = newValue.codePointAt(j);
							int subVal = codePoint - 8320;
							if (Character.isDigit(codePoint) || Character.isLetter(codePoint)
									|| (subVal >= 0 && subVal < 10) || codePoint == (int) ')') {
								if (Character.isDigit(codePoint) || (subVal >= 0 && subVal < 10)) {
									j--;
									continue;
								} else {
									isIndex = true;
									intVal = Character.getNumericValue(newValue.codePointAt(i));
									break;
								}
							} else {
								break;
							}
						}
						if (isIndex) {
							hasChanged = true;
							parsedVal = parsedVal.substring(0, i) + new String(new int[] { 8320 + intVal }, 0, 1)
									+ parsedVal.substring(i + 1, parsedVal.length());
						}
					}
				}
				if (hasChanged) {
					final String newText = parsedVal;
					Platform.runLater(() -> {
						int carrotPosition = inputTextField.getCaretPosition();
						inputTextField.setText(newText);
						inputTextField.positionCaret(carrotPosition);
					});
				}
				parse(newValue);
			}

		});
	}

	public void parse(String value) {
		getChildren().clear();
		try {
			Reaction reaction = parser.parse(value);
			WebView webView = new WebView();
			webView.getEngine().loadContent(reaction.toHTMLString());
			getChildren().add(webView);
			getChildren().add(new Label(reaction.toString()));
			try {
				Reaction r = solver.solve(reaction);
				WebView webView2 = new WebView();
				webView2.getEngine().loadContent(r.toHTMLString());
				getChildren().add(webView2);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			getChildren().add(new Label("ERR: Could not parse!"));
			ex.printStackTrace();
		}
	}

}
