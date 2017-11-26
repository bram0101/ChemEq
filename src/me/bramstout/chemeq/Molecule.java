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

/**
 * (NL) Deze klas vertegenwoordigt een molecuul in een reactievergelijking. Het
 * is onderdeel van de klas 'Element' zodat je een molecuul in een molecuul kan
 * zetten. Sommige moleculen worden zo opgeschreven<br>
 * (EN) This class represents a molecule in a chemical reaction. It extends the
 * class 'Element' to support having a molecule inside a molecule. Some
 * molecules are noted this way.
 * 
 * @author Bram Stout
 *
 */
public class Molecule extends Element {

	/**
	 * (NL) De lijst met elementen en moleculen in deze molecuul. <br>
	 * (EN) The list of elements and molecules in this molecule.
	 */
	private List<Element> elements;
	/**
	 * (NL) De fase van dit molecuul. Het wordt nergens voor gebruikt, maar het is
	 * handig om te hebben. <br>
	 * (EN) The phase of this molecule. It is not used anywhere, but it is handy to
	 * have.
	 */
	private Phase phase;

	/**
	 * (NL) De lading van dit molecuul.<br>
	 * (EN) The charge of this molecule.
	 */
	private int charge;

	/**
	 * (NL) De constructor om een instantie te maken. <br>
	 * (EN) The constructor to make an instance.
	 * 
	 * @param elements
	 *            (NL) De elementen of moleculen in deze molecuul. (EN) The elements
	 *            or molecules in this molecule.
	 * @param factor
	 *            (NL) De coëfficiënt van deze molecuul. (EN) The coefficient of
	 *            this molecule.
	 * @param phase
	 *            (NL) De fase van deze molecuul. (EN) The phase of this molecule.
	 */
	public Molecule(List<Element> elements, int factor, Phase phase, int charge) {
		super(elementsToString(elements), factor);
		this.elements = elements;
		this.phase = phase;
		this.charge = charge;
	}

	/**
	 * (NL) Getter voor de elementen en moleculen in deze molecuul. <br>
	 * (EN) Getter for the elements and molecules in this molecule.
	 * 
	 * @return (NL) De elementen en moleculen. (EN) The elements and molecules.
	 */
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * (NL) Kijkt of deze molecuul een bepaalde atoomsoort heeft. <br>
	 * (EN) Checks if this molecule has a certain type of atom.
	 * 
	 * @param e
	 *            (NL) De element om de soort van te checken. (EN) The element to
	 *            check the type of.
	 * @return (NL) 'true' als deze molecuul het atoomsoort heeft. (EN) 'true' if
	 *         this molecule has the type of atom.
	 */
	public boolean hasElement(Element e) {
		for (Element el : elements) {
			if (el instanceof Molecule)
				if (((Molecule) el).hasElement(e))
					return true;
			if (el.getData().contentEquals(e.getData()))
				return true;
		}
		return false;
	}

	/**
	 * (NL) Krijg de hoeveelheid van een bepaalde atoomsoort in deze molecuul. <br>
	 * (EN) Get the amount of a certain atom type in this molecule.
	 * 
	 * @param e
	 *            (NL) De element om de soort van te krijgen. (EN) The element to
	 *            get the type of.
	 * @return (NL) De hoeveelheid atomen. (EN) The amount of atoms.
	 */
	public int getElementFactor(Element e) {
		int elementFactor = 0;
		for (Element el : elements) {
			if (el instanceof Molecule)
				elementFactor += ((Molecule) el).getElementFactor(e) * el.getFactor();
			else if (el.getData().contentEquals(e.getData()))
				elementFactor += el.getFactor();
		}
		return elementFactor;
	}

	/**
	 * (NL) Getter voor de fase. <br>
	 * (EN) Getter for the phase.
	 * 
	 * @return (NL) De fase. (EN) The phase.
	 */
	public Phase getPhase() {
		return phase;
	}

	/**
	 * (NL) Getter voor de lading. <br>
	 * (EN) Getter for the charge
	 * 
	 * @return (NL) De lading. (EN) The charge.
	 */
	public int getCharge() {
		return charge;
	}

	/**
	 * (NL) Kijkt of dit object gelijk is aan het andere object. <br>
	 * (EN) Checks if the object is equal to this object.
	 */
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Molecule))
			return false;
		return super.equals(a);
	}

	/**
	 * (NL) Geef deze molecuul weer voor gebruik in HTMl. <br>
	 * (EN) Represents this molecule for use in HTMl
	 * 
	 * @return (NL) Een string met HTML code. (EN) A string with HTML code.
	 */
	public String toHTMLString() {
		StringBuilder sb = new StringBuilder();
		if (getFactor() >= 0)
			sb.append(getFactor());
		sb.append("(");
		for (Element e : elements)
			sb.append(e.toHTMLString());
		if (phase != Phase.NULL) {
			sb.append("(");
			sb.append(phase.getToken());
			sb.append(")");
		}
		sb.append(")");
		return sb.toString();
	}

	/**
	 * (NL) Geeft deze molecuul weer als text. <br>
	 * (EN) Represents this molecule as a string.
	 */
	@Override
	public String toString() {
		return "_" + getFactor() + "(" + getData() + (phase == Phase.NULL ? "" : ("(" + phase.getToken() + ")")) + ")" + 
					(charge != 0 ? (charge > 0 ? ("+" + charge) : ("-" + (charge * -1))) : "");
	}

	/**
	 * (NL) Zet de lijst met elementen om naar een string om door te geven naar de
	 * constructor van de 'Element' klas. <br>
	 * (EN) Convert the list of elements to a string to pass forward to the
	 * constructor of the 'Element' class.
	 * 
	 * @param elements
	 *            (NL) Lijst met elementen. (EN) List of elements.
	 * @return (NL) De tekst weergave. (EN) The textual representation.
	 */
	private static String elementsToString(List<Element> elements) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements)
			sb.append(e.toString());
		return sb.toString();
	}
	
	/**
	 * (NL) Maak een kopie van dit molecuul. <br>
	 * (EN) Make a copy of this molecule.
	 * 
	 * @return (NL) Een kopie. (EN) A copy.
	 */
	public Molecule copy() {
		List<Element> elements = new ArrayList<Element>();
		for(int i = 0; i < this.elements.size(); i++)
			elements.add(this.elements.get(i).copy());
		return new Molecule(elements, getFactor(), phase, charge);
	}

}
