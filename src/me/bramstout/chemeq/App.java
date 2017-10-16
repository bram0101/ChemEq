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

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	private Parser parser;

	@Override
	public void start(Stage arg0) throws Exception {
		final double windowWidth = 1280, windowHeight = 720;

		parser = new Parser();

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

		
		  inputTextField = new TextField(); inputTextField.setFont(new
		  Font(Font.getDefault().getFamily(), 16));
		  inputPane.getChildren().add(inputTextField);
		  
		  inputDisplay = new InputDisplay(inputTextField);
		  inputPane.getChildren().add(inputDisplay);
		 
		//TODO: Try to get a nicer looking real time parsing. Maybe just use unicodes. Convert a number to unicode when it is after a letter
		/*WebView webView = new WebView();
		webView.setPrefHeight(38);

		HTMLEditor inputField = new HTMLEditor();
		((GridPane) inputField.getChildrenUnmodifiable().get(0)).getChildren().get(0).setVisible(false);
		((GridPane) inputField.getChildrenUnmodifiable().get(0)).getChildren().get(1).setVisible(false);
		((ToolBar) ((GridPane) inputField.getChildrenUnmodifiable().get(0)).getChildren().get(0)).setManaged(false);
		((ToolBar) ((GridPane) inputField.getChildrenUnmodifiable().get(0)).getChildren().get(1)).setManaged(false);
		inputField.setPrefHeight(38);
		inputField.addEventHandler(InputEvent.ANY, new EventHandler<InputEvent>() {

			@Override
			public void handle(InputEvent arg0) {
				if (arg0.getEventType() == KeyEvent.KEY_PRESSED || arg0.getEventType() == KeyEvent.KEY_TYPED) {
					System.out.println(arg0.getEventType());
					KeyEvent event = (KeyEvent) arg0;
					if (event.getCode() == KeyCode.ENTER) {
						System.out.println("enter");
						event.consume();
					}
					Platform.runLater(() -> {
						try {
							String reactionString = "";
							System.out.println(inputField.getHtmlText().replaceAll("<br>", ""));
							Document doc = parser.parseHTML(inputField.getHtmlText().replaceAll("<br>", ""));
							cleanUpDocument(doc);
							org.w3c.dom.Element contentElement = getContentElement(doc);
							reactionString = contentElement.getTextContent();
							System.out.println(reactionString);
							Reaction reaction = parser.parse(reactionString);
							webView.getEngine().loadContent(reaction.toHTMLString());
							System.out.println("===================");
							System.out.println(doc.getTextContent());
							inputField.setHtmlText("<html dir=\"ltr\"><head></head><body contenteditable=\"true\">"
									+ reaction.toHTMLString() + 
									"</body></html>");
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					});
				}
			}

		});

		inputPane.getChildren().add(inputField);
		inputPane.getChildren().add(webView);*/

		root.setTop(inputPane);
		Button button = new Button();
		root.setCenter(button);

		mainWindow = new Scene(root, windowWidth, windowHeight);

		arg0.setTitle("ChemEq " + Main.VERSION);
		arg0.setScene(mainWindow);
		arg0.show();
	}

	private org.w3c.dom.Element getContentElement(Document doc) {
		NodeList bodyList = doc.getDocumentElement().getElementsByTagName("body");
		org.w3c.dom.Element body = (org.w3c.dom.Element) bodyList.item(0);
		System.out.println(body);
		if (!body.hasChildNodes())
			return body;
		NodeList paragraphList = body.getElementsByTagName("p");
		System.out.println(paragraphList);
		org.w3c.dom.Element paragraph = null;
		for (int i = paragraphList.getLength() - 1; i >= 0; i--) {
			org.w3c.dom.Element n = (org.w3c.dom.Element)paragraphList.item(i);
			if(n.getTextContent() != "") {
				if(n.hasChildNodes()) {
					if(((org.w3c.dom.Element) n.getFirstChild()).getTextContent() != ""){
						paragraph = n;
					}
				}
			}
			System.out.println("_" + n);
		}
		System.out.println(paragraph);
		if(paragraph == null)
			return body;
		if (paragraph.hasChildNodes())
			return (org.w3c.dom.Element) paragraph.getFirstChild();
		else
			return paragraph;
	}

	private void cleanUpDocument(Document doc) {
		NodeList bodyList = doc.getDocumentElement().getElementsByTagName("body");
		org.w3c.dom.Element body = (org.w3c.dom.Element) bodyList.item(0);
		if (!body.hasChildNodes())
			return;
		NodeList paragraphList = body.getChildNodes();
		org.w3c.dom.Element paragraph = null;
		List<Node> removeNodes = new ArrayList<Node>();
		for (int i = 0; i < paragraphList.getLength(); i++) {
			Node n = paragraphList.item(i);
			if (n instanceof org.w3c.dom.Element) {
				if (((org.w3c.dom.Element) n).getTagName() == "p") {
					paragraph = (org.w3c.dom.Element) n;
					break;
				}
			} else
			removeNodes.add(n);
		}
		for(Node n : removeNodes) {
			body.removeChild(n);
		}
		if (paragraph != null) {
			body.appendChild(paragraph);
		}
	}

	public static void run(String[] args) {
		App.launch(args);
	}

}
