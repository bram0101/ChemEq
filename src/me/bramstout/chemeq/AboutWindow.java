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

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AboutWindow {
	
	private Stage stage;
	
	private Scene scene;
	
	private TabPane root;
	
	private Tab nlTab;
	
	private Tab enTab;
	
	public AboutWindow(Stage primaryStage) {
		root = new TabPane();
		
		nlTab = new Tab();
		nlTab.setText("Nederlands");
		setupTab(nlTab, "nl.html");
		
		enTab = new Tab();
		enTab.setText("English");
		setupTab(enTab, "en.html");
		
		root.getTabs().addAll(nlTab, enTab);
		root.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		root.setTabMinWidth(128);
		
		scene = new Scene(root, 540, 720);
		
		stage = new Stage();
		stage.initOwner(primaryStage);
        stage.setTitle("Over / About");
        stage.setScene(scene);
        stage.getIcons().add(new Image(App.class.getClassLoader().getResourceAsStream("icon_64.png")));
	}
	
	private void setupTab(Tab tab, String file) {
		WebView browser = new WebView();
		WebEngine webEngine = browser.getEngine();
		webEngine.load(App.class.getClassLoader().getResource("about/" + file).toExternalForm());
		tab.setContent(browser);
	}
	
	public void show() {
		stage.show();
	}
	
}
