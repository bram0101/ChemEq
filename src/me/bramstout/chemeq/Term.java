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
 * (NL) Deze klas vertegenwoordigt een term in een vergelijking. <br>
 * (EN) This class represents a term in an equation.
 * 
 * @author Bram Stout
 *
 */
public class Term {

	/**
	 * (NL) Het id van de molecuul die deze term vertegenwoordigd. <br>
	 * (EN) The id of the molecule this term represents.
	 */
	private int id;

	/**
	 * (NL) De factor van deze term. <br>
	 * (EN) The factor of this term.
	 */
	private double factor;

	/**
	 * (NL) Constructor. <br>
	 * (EN) Constructor.
	 * 
	 * @param id
	 *            (NL) De id van de molecuul. (EN) The id of the molecule.
	 * @param factor
	 *            (NL) De factor van deze term. (EN) The factor of this term.
	 */
	public Term(int id, double factor) {
		this.id = id;
		this.factor = factor;
	}

	/**
	 * (NL) Getter voor de id. (EN) Getter for the id.
	 * 
	 * @return (NL) Id. (EN) Id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * (NL) Getter voor de factor. <br>
	 * (EN) Getter for the factor.
	 * 
	 * @return (NL) De factor. <br>
	 *         (EN) The factor.
	 */
	public double getFactor() {
		return factor;
	}

	/**
	 * (NL) Setter voor de factor. <br>
	 * (EN) Setter for the factor.
	 * 
	 * @param factor
	 */
	public void setFactor(double factor) {
		this.factor = factor;
	}

	/**
	 * (NL) Kijkt of dit object gelijk is aan het andere object. <br>
	 * (EN) Checks if the object is equal to this object.
	 */
	@Override
	public boolean equals(Object object) {
		if (object == null)
			return false;
		if (object == this)
			return true;
		if (!(object instanceof Term))
			return false;
		if (((Term) object).id == id && ((Term) object).factor == factor)
			return true;
		return false;
	}

}
