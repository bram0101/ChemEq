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

import java.util.List;

/**
 * (NL) Deze klas vertegenwoordigt een vergelijking. Deze klas wordt gebruikt
 * door de solver, om reactie vergelijking op te lossen. <br>
 * (EN) This class represents an equation. This class is used by the solver, to
 * solve chemical reactions.
 * 
 * @author Bram Stout
 *
 */
public class Equation {

	/**
	 * (NL) Geeft aan welke molecuul coëfficiënt deze vergelijking het antwoord op
	 * geeft. <br>
	 * (EN) Shows which molecule coefficient this equation answers.
	 */
	private Term resultTerm;

	/**
	 * (NL) De termen van vergelijking, exclusief de term "resultTerm". <br>
	 * (EN) The terms of this equation, excluding the term "resultTerm".
	 */
	private List<Term> terms;

	/**
	 * (NL) Constructor om een instantie te maken. <br>
	 * (EN) Constructor to make an instance.
	 * 
	 * @param resultTerm
	 *            (NL) Het antwoord van de vergelijking. (EN) The answer of the
	 *            equation.
	 * @param terms
	 *            (NL) De termen van de vergelijking, exclusief het antwoord. (EN)
	 *            The terms of the equation, excluding the answer term.
	 */
	public Equation(Term resultTerm, List<Term> terms) {
		this.resultTerm = resultTerm;
		this.terms = terms;
	}

	/**
	 * (NL) Getter voor de term "resultTerm".  <br>
	 * (EN) Getter for the term "resultTerm".
	 * 
	 * @return (NL) De term "resultTerm". (EN) The term "resultTerm".
	 */
	public Term getResultTerm() {
		return resultTerm;
	}

	/**
	 * (NL) Getter voor de termen van de vergelijking. <br>
	 * (EN) Getter for the terms of the equation.
	 * @return (NL) De termen. (EN) The terms.
	 */
	public List<Term> getTerms() {
		return terms;
	}

	
	/**
	 * (NL) Kijk of deze vergelijking een bepaalde molecuul / term nodig heeft. <br>
	 * (EN) Check if this equation requires a certain molecule / term.
	 * @param id (NL) De id van het molecuul. (EN) The id of the molecule.
	 * @return (NL) Is 'true' wanneer deze vergelijking de molecuul nodig heeft. (EN) Is 'true' when this equation requires the molecule.
	 */
	public boolean needsTerm(int id) {
		for (Term t : terms) {
			if (t.getId() == id)
				return true;
		}
		return false;
	}
	
	/**
	 * (NL) Kijkt of het object gelijk is aan dit object.
	 * (EN) Checks if the object is equal to this object.
	 */
	@Override
	public boolean equals(Object eq) {
		if (!(eq instanceof Equation))
			return false;

		if (!((Equation) eq).resultTerm.equals(resultTerm))
			return false;

		if (((Equation) eq).terms.size() != terms.size())
			return false;

		for (int i = 0; i < terms.size(); i++) {
			if (!((Equation) eq).terms.get(i).equals(terms.get(i)))
				return false;
		}

		return true;
	}

}
