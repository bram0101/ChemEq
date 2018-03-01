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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * (NL) De hoofdklas van de applicatie. Hier wordt het venster aangemaakt en
 * weergegeven. <br>
 * (EN) De main class of the application. Here, the window is created and
 * displayed.
 * 
 * @author Bram Stout
 *
 */
public class App extends Application {

	/**
	 * (NL) Het venster van de applicate. <br>
	 * (EN) The window of the application.
	 */
	private Scene mainWindow;
	/**
	 * (NL) Het hoogste niveau van de scene. <br>
	 * (EN) The root of the scene.
	 */
	private StackPane root;
	
	/**
	 * (NL) De hoofd layout. <br>
	 * (EN) The head layout.
	 */
	private VBox rootLayout;
	
	/**
	 * (NL) De groep die de data van de parser en solver weergeeft. <br>
	 * (EN) The group that displays the data of the parser and solver.
	 */
	private InputDisplay inputDisplay;
	
	/**
	 * (NL) Het mengvat. <br>
	 * (EN) Calculates masses of the reaction.
	 */
	private Barrel barrel;
	
	/**
	 * (NL) Een object om het grote logo te laten zien. <br>
	 * (EN) An object to show the big logo.
	 */
	private ImageView logoView;
	
	/**
	 * (NL) Een 'Over' knop. <br>
	 * (EN) An 'About' button.
	 */
	private Button aboutButton;
	
	/**
	 * (NL) Het 'Over' venster. <br>
	 * (EN) The 'About' window.
	 */
	private AboutWindow aboutWindow;

	@Override
	/**
	 * (NL) Het beginpunt van de applicatie. <br>
	 * (EN) The starting function of the application.
	 */
	public void start(Stage arg0) throws Exception {
		// (NL) De standaard grootte van het venster.
		// (EN) The default size of the window.
		final double windowWidth = 1280, windowHeight = 720;
		
		arg0.getIcons().add(new Image(App.class.getClassLoader().getResourceAsStream("icon_64.png")));

		// (NL) Maak de UI.
		// (EN) Create the UI.
		root = new StackPane();
		
		BorderPane logoViewBorderPane = new BorderPane();
		logoView = new ImageView();
		logoView.setFitWidth(320);
		logoView.setPreserveRatio(true);
		logoView.setImage(new Image(App.class.getClassLoader().getResourceAsStream("icon_large.png")));
		logoViewBorderPane.setPadding(new Insets(8));
		logoViewBorderPane.setTop(logoView);
		
		root.getChildren().add(logoViewBorderPane);
		
		rootLayout = new VBox();
		
		barrel = new Barrel();
		
		inputDisplay = new InputDisplay(barrel);
		inputDisplay.maxWidthProperty().bind(root.widthProperty());
		inputDisplay.minWidthProperty().bind(root.widthProperty());
		inputDisplay.setPadding(new Insets(windowWidth * 0.16666666, windowWidth * 0.16666666, windowWidth * 0.16666666,
				windowWidth * 0.16666666));
		
		barrel.maxWidthProperty().bind(root.widthProperty());
		barrel.minWidthProperty().bind(root.widthProperty());
		
		rootLayout.getChildren().addAll(inputDisplay, barrel);
		
		
		// (NL) Normaal van je de klas Property gebruiken en het binden aan de
		// 'padding'. Dat kan niet bij 'padding', dus doe ik het zo.
		// (EN) Normally you can use the Property class and bind it to the padding.
		// That is not available for padding, so I do it this way.
		root.widthProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldVal, Number newVal) {
				inputDisplay.setPadding(
						new Insets(Math.max(newVal.doubleValue() * 0.16666666 * 0.5, 156), newVal.doubleValue() * 0.16666666,
								newVal.doubleValue() * 0.16666666 * 0.25, newVal.doubleValue() * 0.16666666));
			}

		});
		
		root.getChildren().add(rootLayout);
		
		aboutWindow = new AboutWindow(arg0);
		
		BorderPane aboutButtonBorderPane = new BorderPane();
		aboutButtonBorderPane.setPickOnBounds(false);
		aboutButton = new Button("Over / About");
		aboutButton.setOnAction((event)->{
			aboutWindow.show();
		});
		aboutButtonBorderPane.setPadding(new Insets(8));
		aboutButtonBorderPane.setBottom(aboutButton);
		
		root.getChildren().add(aboutButtonBorderPane);

		// (NL) Maak het venster aan en geef het weer.
		// (EN) Create and show the window.
		mainWindow = new Scene(root, windowWidth, windowHeight);

		arg0.setTitle("ChemEq " + Main.VERSION);
		arg0.setMinWidth(512);
		arg0.setMinHeight(512);
		arg0.setScene(mainWindow);
		arg0.show();
	}

	/**
	 * (NL) Dit is het beginpunt van het programma. Vanuit hier wordt JavaFX
	 * geroepen om alles over te nemen. De method "start" zal het nieuwe beginpunt
	 * worden. <br>
	 * (EN) This is the starting point of the program. From here, JavaFX is called
	 * to take everything over. The method "start" will be the new starting point.
	 * 
	 * @param args
	 */
	public static void run(String[] args) {
		App.launch(args);
	}

}
