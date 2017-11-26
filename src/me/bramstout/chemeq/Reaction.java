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
 * (NL) Vertegenwoordigt een chemische reactie. <br>
 * (EN) Represents a chemical reaction.
 * 
 * @author Bram Stout
 *
 */
public class Reaction {

	/**
	 * (NL) Alle moleculen aan de linker kant. <br>
	 * (EN) All the molecules on the left side.
	 */
	private List<Molecule> leftTerm;
	/**
	 * (NL) Alle moleculen aan de rechter kant. <br>
	 * (EN) All the molecules on the right side.
	 */
	private List<Molecule> rightTerm;
	/**
	 * (NL) Alle atoomsoorten in de vergelijking. <br>
	 * (EN) All types of atoms in the reaction.
	 */
	private List<Element> elements;
	/**
	 * (NL) Alle atoomsoorten aan de linker kant. <br>
	 * (EN) All types of atoms on the left side.
	 */
	private List<Element> leftTermElements;
	/**
	 * (NL) Alle atoomsoorten aan de rechter kant. <br>
	 * (EN) All types of atoms on the right side.
	 */
	private List<Element> rightTermElements;

	/**
	 * (NL) Constructor. <br>
	 * (EN) Constructor.
	 * 
	 * @param leftTerm
	 * @param rightTerm
	 * @param elements
	 * @param leftTermElements
	 * @param rightTermElements
	 */
	public Reaction(List<Molecule> leftTerm, List<Molecule> rightTerm, List<Element> elements,
			List<Element> leftTermElements, List<Element> rightTermElements) {
		super();
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.elements = elements;
		this.leftTermElements = leftTermElements;
		this.rightTermElements = rightTermElements;
	}

	/**
	 * (NL) Getter voor de moleculen aan de linker kant. <br>
	 * (EN) Getter for the molecules on the left side.
	 * 
	 * @return
	 */
	public List<Molecule> getLeftTerm() {
		return leftTerm;
	}

	/**
	 * (NL) Setter voor de moleculen aan de linker kant. <br>
	 * (EN) Setter for the molecules on the left side.
	 * 
	 * @return
	 */
	public void setLeftTerm(List<Molecule> leftTerm) {
		this.leftTerm = leftTerm;
	}

	/**
	 * (NL) Getter voor de moleculen aan de rechter kant. <br>
	 * (EN) Getter for the molecules on the right side.
	 * 
	 * @return
	 */
	public List<Molecule> getRightTerm() {
		return rightTerm;
	}

	/**
	 * (NL) Setter voor de moleculen aan de rechter kant. <br>
	 * (EN) Setter for the molecules on the right side.
	 * 
	 * @return
	 */
	public void setRightTerm(List<Molecule> rightTerm) {
		this.rightTerm = rightTerm;
	}

	/**
	 * (NL) Getter voor alle moleculen in de vergelijking. <br>
	 * (EN) Getter for all types of atoms in the reaction.
	 * 
	 * @return
	 */
	public List<Element> getElements() {
		return elements;
	}

	/**
	 * (NL) Setter voor alle moleculen in de vergelijking. <br>
	 * (EN) Setter for all types of atoms in the reaction.
	 * 
	 * @return
	 */
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	/**
	 * (NL) Setter voor alle atoomsoorten aan de linker kant. <br>
	 * (EN) Setter for all types of atoms on the left side.
	 * 
	 * @return
	 */
	public List<Element> getLeftTermElements() {
		return leftTermElements;
	}

	/**
	 * (NL) Setter voor alle atoomsoorten aan de rechter kant. <br>
	 * (EN) Setter for all types of atoms on the right side.
	 * 
	 * @return
	 */
	public List<Element> getRightTermElements() {
		return rightTermElements;
	}

	/**
	 * (NL) Geef deze reactievergelijking weer voor gebruik in HTMl. <br>
	 * (EN) Represents this chemical equation for use in HTMl
	 * 
	 * @return (NL) Een string met HTML code. (EN) A string with HTML code.
	 */
	public String toHTMLString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leftTerm.size(); i++) {
			sb.append(leftTerm.get(i).toHTMLString());
			if (i < leftTerm.size() - 1)
				sb.append(" + ");
		}
		sb.append(" = ");
		for (int i = 0; i < rightTerm.size(); i++) {
			sb.append(rightTerm.get(i).toHTMLString());
			if (i < rightTerm.size() - 1)
				sb.append(" + ");
		}
		return sb.toString();
	}

	/**
	 * (NL) Geeft deze reactievergelijking weer als text. <br>
	 * (EN) Represents this chemical reaction as a string.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < leftTerm.size(); i++) {
			sb.append(leftTerm.get(i).toString());
			if (i < leftTerm.size() - 1)
				sb.append(" + ");
		}
		sb.append(" = ");
		for (int i = 0; i < rightTerm.size(); i++) {
			sb.append(rightTerm.get(i).toString());
			if (i < rightTerm.size() - 1)
				sb.append(" + ");
		}

		sb.append(" {elements:[");
		for (Element e : elements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("],");
		sb.append(" left_term:[");
		for (Element e : leftTermElements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("],");
		sb.append(" right_term:[");
		for (Element e : rightTermElements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("]}");
		return sb.toString();
	}

	/**
	 * (NL) Maak een kopie van deze vergelijking. <br>
	 * (EN) Make a copy of this equation.
	 * 
	 * @return (NL) Een kopie. (EN) A copy.
	 */
	public Reaction copy() {
		List<Molecule> leftTerm = new ArrayList<Molecule>();
		for(int i = 0; i < this.leftTerm.size(); i++)
			leftTerm.add(this.leftTerm.get(i).copy());
		List<Molecule> rightTerm = new ArrayList<Molecule>();
		for(int i = 0; i < this.rightTerm.size(); i++)
			rightTerm.add(this.rightTerm.get(i).copy());
		List<Element> elements = new ArrayList<Element>();
		for(int i = 0; i < this.elements.size(); i++)
			elements.add(this.elements.get(i).copy());
		List<Element> leftTermElements = new ArrayList<Element>();
		for(int i = 0; i < this.leftTermElements.size(); i++)
			leftTermElements.add(this.leftTermElements.get(i).copy());
		List<Element> rightTermElements = new ArrayList<Element>();
		for(int i = 0; i < this.rightTermElements.size(); i++)
			rightTermElements.add(this.rightTermElements.get(i).copy());
		return new Reaction(leftTerm, rightTerm, elements, leftTermElements, rightTermElements);
	}

}
