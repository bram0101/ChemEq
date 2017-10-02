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
		root.setCenter(new Button());

		mainWindow = new Scene(root, windowWidth, windowHeight);

		arg0.setTitle("ChemEq " + Main.VERSION);
		arg0.setScene(mainWindow);
		arg0.show();
	}

	public static void run(String[] args) {
		App.launch(args);
	}

}
