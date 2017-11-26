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

/**
 * (NL) Deze klas vertegenwoordigt een atoom in een reactievergelijking. <br>
 * (EN) This class represents an atom in a chemical reaction.
 * 
 * @author Bram Stout
 *
 */
public class Element {

	/**
	 * (NL) De naam van het atoom. Dit geeft weer wat het atoom is. "Al", "C" of "O"
	 * bijvoorbeeld. <br>
	 * (EN) De name of the atom. This shows what the atom is. For example: "Al", "C"
	 * or "O".
	 */
	private String data;
	/**
	 * (NL) Dit is de index van een atoom in een reactievergelijking. <br>
	 * (EN) This is the index of an atom in a chemical reaction.
	 */
	private int factor;

	/**
	 * (NL) Dit is de constructor van de klas. <br>
	 * (EN) This is the constructor of the class.
	 * 
	 * @param data
	 *            (NL) De naam van de atoom. (EN) The name of the atom
	 * @param factor
	 *            (NL) De index van de atoom. (EN) The index of the atom.
	 */
	public Element(String data, int factor) {
		this.data = data;
		this.factor = factor;
	}

	/**
	 * (NL) Getter voor de naam. <br>
	 * (EN) Getter for the name.
	 * 
	 * @return (NL) De naam van de atoom. (EN) The name of the atom.
	 */
	public String getData() {
		return data;
	}

	/**
	 * (NL) Getter voor de index. <br>
	 * (EN) Getter for the index.
	 * @return (NL) De index van de atoom. (EN) The index of the atom.
	 */
	public int getFactor() {
		return factor;
	}

	/**
	 * (NL) Setter voor de index. <br>
	 * (EN) Setter for the index.
	 * @param factor (NL) De index van het atoom. (EN) The index of the atom.
	 */
	public void setFactor(int factor) {
		this.factor = factor;
	}

	/**
	 * (NL) Geef dit element weer voor gebruik in HTMl. <br>
	 * (EN) Represents this element for use in HTMl
	 * @return (NL) Een string met HTML code. (EN) A string with HTML code.
	 */
	public String toHTMLString() {
		return factor == 1 ? data : data + "<sub>" + factor + "</sub>";
	}

	/**
	 * (NL) Geeft dit element weer als text. <br>
	 * (EN) Represents this element as a string.
	 */
	@Override
	public String toString() {
		return factor == 1 ? data : data + factor;
	}

	/**
	 * (NL) Kijkt of het object gelijk is aan dit object.
	 * (EN) Checks if the object is equal to this object.
	 */
	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Element))
			return false;
		if (((Element) a).data == data && ((Element) a).factor == factor)
			return true;
		return false;
	}
	
	/**
	 * (NL) Maak een kopie van dit element. <br>
	 * (EN) Make a copy of this element.
	 * 
	 * @return (NL) Een kopie. (EN) A copy.
	 */
	public Element copy() {
		return new Element(data + "", factor);
	}

}
