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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * (NL) Het stukje van het venster waar je kan laten berekenen hoeveel je van wat nodig hebt. <br>
 * (EN) The interface where you can calculate how much of everything you need.
 * 
 * @author bram
 *
 */
public class Barrel extends HBox {

	/**
	 * (NL) De invoer delen van de linkerkant van de reactie.
	 * (EN) The input parts of the left side of the reaction.
	 */
	private List<BarrelMolecule> leftBarrelMolecules;
	/**
	 * (NL) De invoer delen van de rechterkant van de reactie.
	 * (EN) The input parts of the right side of the reaction.
	 */
	private List<BarrelMolecule> rightBarrelMolecules;

	/**
	 * (NL) Constructor. <br>
	 * (EN) Constructor.
	 */
	public Barrel() {
		setAlignment(Pos.CENTER);

		leftBarrelMolecules = new ArrayList<BarrelMolecule>();
		rightBarrelMolecules = new ArrayList<BarrelMolecule>();
	}

	/**
	 * (NL) Update het venster zodat de nieuwe reactie wordt weergegeven. <br>
	 * (EN) Update the interface so that the new reaction is being displayed.
	 * @param reaction
	 */
	public void update(Reaction reaction) {
		getChildren().clear();
		
		int i = 0;
		for (i = 0; i < reaction.getLeftTerm().size(); i++) {
			if (leftBarrelMolecules.size() > i) {
				BarrelMolecule bm = leftBarrelMolecules.get(i);
				bm.set(reaction.getLeftTerm().get(i));
				getChildren().add(bm);
			} else {
				BarrelMolecule bm = new BarrelMolecule(reaction.getLeftTerm().get(i));
				getChildren().add(bm);
				leftBarrelMolecules.add(bm);
			}
			if (i < reaction.getLeftTerm().size() - 1)
				getChildren().add(new Label("+"));
		}
		for (int j = i; j < leftBarrelMolecules.size();)
			leftBarrelMolecules.remove(i);

		getChildren().add(new Label("->"));

		for (i = 0; i < reaction.getRightTerm().size(); i++) {
			if (rightBarrelMolecules.size() > i) {
				BarrelMolecule bm = rightBarrelMolecules.get(i);
				bm.set(reaction.getRightTerm().get(i));
				getChildren().add(bm);
			} else {
				BarrelMolecule bm = new BarrelMolecule(reaction.getRightTerm().get(i));
				getChildren().add(bm);
				rightBarrelMolecules.add(bm);
			}
			if (i < reaction.getRightTerm().size() - 1)
				getChildren().add(new Label("+"));
		}
		for (int j = i; j < rightBarrelMolecules.size();)
			rightBarrelMolecules.remove(i);

		calculateResult();
	}

	/**
	 * (NL) Bereken de massa's. <br>
	 * (EN) Calculate the masses.
	 */
	public void calculateResult() {
		// (NL) Krijg de laagste hoeveelheid die je kan maken. Je deelt door het coëfficiënt.
		// (EN) Get the lowest amount you can make. You divide by the coefficient.
		double mass = Double.MAX_VALUE;
		for(BarrelMolecule bm : leftBarrelMolecules) {
			double m = bm.getMass();
			if(m > 0)
				mass = Math.min(mass, m / bm.getMolecule().getFactor());
		}
		
		for(BarrelMolecule bm : rightBarrelMolecules) {
			double m = bm.getMass();
			if(m > 0)
				mass = Math.min(mass, m / bm.getMolecule().getFactor());
		}
		
		// (NL) Als wij niks hebben ingevoerd, dan hoeven wij niks meer te doen.
		// (EN) If we entered nothing, we do not have to do anything.
		if(mass == Double.MAX_VALUE)
			return;
		
		//(NL) Voer het resultaat in. We vermenigvuldigen weer met het coëfficiënt.
		//(EN) Input the result. We multiply by the coefficient.
		
		for(BarrelMolecule bm : leftBarrelMolecules) {
			bm.setResult(mass *  bm.getMolecule().getFactor());
		}
		
		for(BarrelMolecule bm : rightBarrelMolecules) {
			bm.setResult(mass *  bm.getMolecule().getFactor());
		}
		
	}

	/**
	 * (NL) Het stukje venster voor elk molecuul. <br>
	 * (EN) The interface for each molecule.
	 * 
	 * @author bram
	 *
	 */
	public static class BarrelMolecule extends VBox {

		public Molecule mol;
		private Label name;
		private TextField mass;
		private ChoiceBox<String> type;
		private HBox massLayout;
		private Label result;

		/**
		 * Constructor
		 * 
		 * @param mol
		 */
		public BarrelMolecule(Molecule mol) {
			this.mol = mol;
			this.name = new Label(DisplayUtil.moleculeToString(mol, false));

			this.mass = new TextField("");
			this.mass.setMinWidth(48);
			this.mass.setMaxWidth(48);

			this.type = new ChoiceBox<String>(FXCollections.observableArrayList("mol", "g", "kg"));
			this.type.setMinWidth(48);
			this.type.setMaxWidth(48);
			this.type.setValue("mol");

			massLayout = new HBox();
			massLayout.getChildren().addAll(mass, type);
			massLayout.setPadding(new Insets(8));

			this.result = new Label("0.00 mol");

			setAlignment(Pos.CENTER);
			getChildren().addAll(name, massLayout, result);
			setPadding(new Insets(8));

			this.mass.setOnKeyTyped((e) -> {
				Platform.runLater(() -> {
					((Barrel) BarrelMolecule.this.getParent()).calculateResult();
				});
			});
			
			this.type.setOnAction((e) -> {
				Platform.runLater(() -> {
					((Barrel) BarrelMolecule.this.getParent()).calculateResult();
				});
			});
		}

		public void set(Molecule mol) {
			if (!mol.getData().contentEquals(name.getText())) {
				name.setText(DisplayUtil.moleculeToString(mol, false));
				this.mass.setText("");
				this.result.setText("0.00 " + this.type.getValue());
			}
			this.mol = mol;
		}

		/**
		 * (NL) Zet de waarde (in mol) om naar wat moet worden weergegeven, en weergeef dat. <br>
		 * (EN) Convert the value (in mol) to wat needs to be displayed and display that.
		 * 
		 * @param value (NL) De waarde in mol. (EN) The value in mol.
		 */
		public void setResult(double value) {
			if(this.type.getValue().contains("g"))
				value *= mol.getMass();
			if(this.type.getValue().contains("k"))
				value *= 0.001;
			this.result.setText(new DecimalFormat("#.0###").format(value) + this.type.getValue());
		}
		
		/**
		 * (NL) Lees de invoer en zet het om in de massa in mol. Anders is het -1. <br>
		 * (EN) Read the input and convert it to the mass in mol. Else it is -1.
		 * 
		 * @return (NL) De massa in mol, anders is het -1. (EN) The mass in mol, else it is -1.
		 */
		public double getMass() {
			if(this.mass.getText().isEmpty())
				return -1;
			try {
				double mass = Double.parseDouble(this.mass.getText().replace(',', '.'));
				if(this.type.getValue().contains("g"))
					mass /= mol.getMass();
				if(this.type.getValue().contains("k"))
					mass *= 1000;
				return mass;
			}catch(Exception ex) {}
			
			return -1;
		}
		
		/**
		 * (NL) Getter voor het molecuul waar deze invoer voor is. <br>
		 * (EN) Getter for the molecule that this input is for.
		 * 
		 * @return
		 */
		public Molecule getMolecule() {
			return mol;
		}

	}

}
