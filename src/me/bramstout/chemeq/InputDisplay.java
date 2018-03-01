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
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * (NL) Weergeeft de invoer in de applicatie. <br>
 * (EN) Displays the input in the application.
 * 
 * @author Bram Stout
 *
 */
public class InputDisplay extends VBox {

	/**
	 * (NL) De parser klas die wordt gebruikt om de invoer naar een bruikbaar
	 * formaat om te zetten. <br>
	 * (EN) The parser class that is used to convert the input to a useable format.
	 */
	private Parser parser;
	/**
	 * (NL) De solver klas die de reactievergelijking oplost. <br>
	 * (EN) The solver class that solves the chemical reactions.
	 */
	private Solver solver;

	private TextField inputTextField;
	private TextField resultTextField;
	private Label reactionInfoLabel;
	private Label errorLabel;
	private Barrel barrel;

	/**
	 * (NL) Constructor om een invoor display te maken. <br>
	 * (EN) Constructor to create an input display.
	 */
	public InputDisplay(Barrel barrel) {
		this.barrel = barrel;
		parser = new Parser();
		solver = new Solver();

		setAlignment(Pos.CENTER);
		
		inputTextField = new TextField();
		inputTextField.setFont(new Font(Font.getDefault().getFamily(), 16));
		getChildren().add(inputTextField);

		Label label = new Label("Opgelost / Solved");
		label.setPadding(new Insets(16, 0, 0, 0));
		label.setFont(new Font(Font.getDefault().getFamily(), 16));
		getChildren().add(label);

		resultTextField = new TextField();
		resultTextField.setFont(new Font(Font.getDefault().getFamily(), 16));
		resultTextField.setEditable(false);
		resultTextField.setFocusTraversable(false);
		getChildren().add(resultTextField);
		
		reactionInfoLabel = new Label("Atomen / Atoms: ");
		getChildren().add(reactionInfoLabel);
		
		errorLabel = new Label("");
		getChildren().add(errorLabel);

		setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				inputTextField.requestFocus();
				inputTextField.deselect();
				inputTextField.end();
			}

		});
		
		// (NL) Als je op het antwoordt klikt, dan selecteert hij alvast alles.
		// (EN) If you click on the answer, it is going to select everything.
		resultTextField.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Platform.runLater(()->{
					resultTextField.selectAll();
					resultTextField.copy();
				});
			}
			
		});

		// (NL) Kijk of the invoer veranderd. Zoja, dan de indexes naar een kleiner
		// weergave zetten, subscript, en parse en los de vergelijking op.
		// (EN) Check if the input has changed. If so, change the indices to a smaller
		// format, subscript, and parse and solve the reaction.
		inputTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String strValue) {
				// (NL) Verander alle subscript getallen naar normale getallen.
				// (EN) Change all subscript numbers to normal numbers.
				String newValue = strValue;
				boolean hasChanged = false;
				for (int i = 0; i < strValue.length(); i++) {
					int codePoint = strValue.codePointAt(i); // (NL) Om UTF-8 te ondersteunen gebruiken wij een integer
																// om een karakter te vertegenwoordigen. (EN) To support
																// UTF-8, we user an integer to represent a karakter.
					int subVal = codePoint - 8320; // (NL) De subscript getallen beginnen bij 8320 als UTF-8 waarde.
													// (EN) Subscript numbers start at 8320.
					String superVal = DisplayUtil.getValueFromSuperscript(codePoint);
					if (subVal >= 0 && subVal < 10) { // (NL) Kijk of het wel een getal is. Zoja, vervang het met een
														// normaal getal. (EN) Check if it is a number. If so, replace
														// it with a normal number.
						newValue = newValue.substring(0, i) + subVal + newValue.substring(i + 1, newValue.length());
					}
					if (superVal != null) {
						hasChanged = true;
						newValue = newValue.substring(0, i) + superVal + newValue.substring(i + 1, newValue.length());
					}
				}
				// (NL) Zoek enige indexes die naar subscript moeten worden verandert, en enige
				// ladingen die naar supscript moeten worden verandert.
				// (EN) Search for any indices that have to be converted to subscript, and any
				// charges that have to be converted to supscript.
				String parsedVal = newValue;
				// (NL) Kijk naar elke karakter in de invoer.
				// (EN) Iterate over all characters in the input.
				for (int i = 0; i < newValue.length(); i++) {
					// (NL) Het moet wel een getal zijn.
					// (EN) It has to be a number.
					if (Character.isDigit(newValue.codePointAt(i)) || newValue.codePointAt(i) == (int) '+'
							|| newValue.codePointAt(i) == (int) '-') {
						boolean isIndex = false;
						boolean isCharge = false;
						int intVal = 0;
						String chargeVal = null;
						int j = i;
						// (NL) Kijk steeds naar een karakter eerder om te kijken of dit een index is.
						// (EN) Keep checking the previous character to see if this is an index.
						while (j >= 0) {
							int codePoint = newValue.codePointAt(j);
							int subVal = codePoint - 8320;
							String superVal = DisplayUtil.getValueFromSuperscript(codePoint);
							if (superVal != null)
								codePoint = superVal.codePointAt(0);
							// (NL) Als het een getal is, dan kijken wij naar de karakter daarvoor. Is het
							// een letter of ')', dan is het een index. Vinden wij iets anders, dan is het
							// geen index. Als wij een '+' of '-' vinden, dan kijken wij of het een lading
							// is.
							// (EN) If it is a number, check the karakter before that. If it is a letter of
							// ')', then it is an index. If we find something else, it is not an index.
							// If we find a '+' or '-', then we check if it is a charge.
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
							} else if (codePoint == (int) '+' || codePoint == (int) '-') {
								if(j > 0) {
									int codeP = newValue.codePointAt(j - 1);
									int subV = codeP - 8320;
									String superV = DisplayUtil.getValueFromSuperscript(codeP);
									if (superV != null)
										codeP = superV.codePointAt(0);
									if(codeP == (int) '+' || codeP == (int) '-' || Character.isWhitespace(codeP))
										break;
									if(Character.isDigit(codeP) || (subV >= 0 && subV < 10)) {
										int k = j - 1;
										boolean b = false;
										while(k >= 0) {
											int cP = newValue.codePointAt(k);
											int sbV = cP - 8320;
											String supV = DisplayUtil.getValueFromSuperscript(cP);
											if (supV != null)
												cP = supV.codePointAt(0);
											if(Character.isDigit(cP) || (sbV >= 0 && sbV < 10)) {
												k--;
												continue;
											}
											if(cP == (int) '+' || cP == (int) '-')
												b = true;
											break;
										}
										if(b)
											break;
									}
								}
								int k = i + 1;
								// (NL) We zeggen alvast dat het een lading is, en dan kunnen wij het in de
								// while loop weer terug zetten. Voor het geval dat de lading het laatste
								// karakter is.
								// (EN) We already set the isCharge to true, so that we can set it to false in
								// the while loop. In case the charge is the last character.
								isCharge = true;
								while (k < newValue.length()) {
									int codeP = newValue.codePointAt(k);
									superVal = DisplayUtil.getValueFromSuperscript(codeP);
									if (superVal != null)
										codeP = superVal.codePointAt(0);
									subVal = codeP - 8320;
									if (Character.isDigit(codeP) || (subVal >= 0 && subVal < 10)) {
										k++;
										continue;
									}
									if (Character.isWhitespace(codeP) || codeP == (int) '+' || codeP == (int) '-'
											|| codeP == (int) '=')
										break;
									isCharge = false;
									break;
								}
								if (isCharge) {
									chargeVal = DisplayUtil.numberToSuperscript(newValue.substring(i, i + 1));
								}
								break;
							} else {
								break;
							}
						}
						// (NL) Als het een index is, vervang het met de zijn subscript variant. Geef
						// ook aan dat wij iets hebben veranderd.
						// (EN) If it is an index, replace the number with it's subscript representative
						// and indicate we changed something.
						if (isIndex) {
							hasChanged = true;
							parsedVal = parsedVal.substring(0, i) + new String(new int[] { 8320 + intVal }, 0, 1)
									+ parsedVal.substring(i + 1, parsedVal.length());
						}
						if (isCharge) {
							hasChanged = true;
							parsedVal = parsedVal.substring(0, i) + chargeVal
									+ parsedVal.substring(i + 1, parsedVal.length());
						}
					}
				}
				// (NL) Als wij iets hebben vervangen, dan updaten wij de invoer.
				// (EN) If we changed anything, we update the input.
				if (hasChanged) {
					final String newText = parsedVal;
					Platform.runLater(() -> {
						int carrotPosition = inputTextField.getCaretPosition();
						inputTextField.setText(newText);
						inputTextField.positionCaret(carrotPosition);
					});
				}
				// (NL) Parse en los de vergelijking op.
				// (EN) Parse and solve the reaction.
				parse(newValue);
			}

		});
		
		inputTextField.setText("CaCl2 + AgNO3 = AgCl + Ca(NO3)2");
	}

	/**
	 * (NL) Parse en los de vergelijking op, en geef de resultaten weer. <br>
	 * (EN) Parse and solve the reaction and display the result.
	 * 
	 * @param value
	 *            (NL) De invoer. (EN) The input.
	 */
	public void parse(String value) {
		// (NL) Parse de invoer. Doe hetzelfde kwa oplossen en geef het resultaat weer.
		// (EN) Parse the input. Do the same for solving it and display it.
		errorLabel.setText("");
		try {
			Reaction reaction = parser.parse(value);
			try {
				Reaction r = solver.solve(reaction);
				resultTextField.setText(DisplayUtil.reactionToString(r));
				
				StringBuilder sb = new StringBuilder();
				DisplayUtil.getAtoms(r.getLeftTerm(), sb);
				
				reactionInfoLabel.setText("Atomen / Atoms: " + sb.substring(0, sb.length() - 2));
				
				barrel.update(r);;
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			errorLabel.setText("ERR: Could not parse!");
			ex.printStackTrace();
		}
	}

}
