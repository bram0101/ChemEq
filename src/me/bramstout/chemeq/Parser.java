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

public class Parser {

	public Parser() {
	}

	public Reaction parse(String s) throws IllegalArgumentException {
		try {
			StringIterator si = new StringIterator(
					" " + s.trim().replace('\t', ' ').replace('[', '(').replace(']', ')'));

			List<Molecule> leftTerm = parseTerm(si);

			while (si.hasNext() && !si.isWhitespace(si.peekNext()) && !si.isDigit(si.peekNext())
					&& !si.isLetter(si.peekNext()))
				si.skip();
			si.skipSpaces();

			List<Molecule> rightTerm = parseTerm(si);
			List<Element> elements = new ArrayList<Element>();
			List<Element> leftTermElements = new ArrayList<Element>();
			List<Element> rightTermElements = new ArrayList<Element>();

			for (Molecule m : leftTerm)
				addElements(m, elements);
			for (Molecule m : rightTerm)
				addElements(m, elements);

			for (Molecule m : leftTerm)
				addElements(m, leftTermElements);
			for (Molecule m : rightTerm)
				addElements(m, rightTermElements);

			return new Reaction(leftTerm, rightTerm, elements, leftTermElements, rightTermElements);
		} catch (Exception ex) {
			IllegalArgumentException e = new IllegalArgumentException(
					"Error while parsing caused exception: " + ex.getMessage());
			e.initCause(ex);
			throw e;
		}
	}

	private List<Molecule> parseTerm(StringIterator si) throws IllegalArgumentException {
		List<Molecule> term = new ArrayList<Molecule>();

		while (si.hasNext()) {
			si.skipSpaces();
			int c = si.peekNext();
			if (c == (int) '=' || c == (int) '-')
				break;
			if (c == (int) '+') {
				si.skip();
				si.skipSpaces();
			}
			while (si.hasNext() && !si.isLetter(si.peekNext()) && !si.isDigit(si.peekNext()))
				si.skip();
			term.add(parseMolecule(si));
		}

		return term;
	}

	private Molecule parseMolecule(StringIterator si) throws IllegalArgumentException {
		List<Element> elements = new ArrayList<Element>();
		int factor = -1;
		Phase phase = Phase.NULL;

		if (!si.hasNext())
			return new Molecule(elements, factor, Phase.NULL);

		if (si.isDigit(si.peekNext())) {
			StringBuilder sb = new StringBuilder();
			while (si.hasNext() && si.isDigit(si.peekNext())) {
				sb.append(si.getIntValue(si.next()));
			}
			factor = Integer.parseInt(sb.toString());
			si.skipSpaces();
		}

		while (si.hasNext()) {
			if (si.peekNext() == (int) '(') {
				Phase p = Phase.getPhase(si);
				if (p != Phase.NULL) {
					phase = p;
					continue;
				}

				si.skip();
				Molecule m = parseMolecule(si);
				if (si.hasNext() && si.peekNext() != (int) ')')
					throw new IllegalArgumentException("Err: inline molecule has no closure. Missing ')' at "
							+ si.getIndex() + ", found si '" + si.peekNext() + "'");
				si.skip();
				if (si.hasNext() && si.isDigit(si.peekNext())) {
					StringBuilder sb = new StringBuilder();
					while (si.hasNext() && si.isDigit(si.peekNext())) {
						sb.append(si.getIntValue(si.next()));
					}
					m.setFactor(Integer.parseInt(sb.toString()));
				}
				elements.add(m);
			} else if (si.isLetter(si.peekNext())) {
				elements.add(parseElement(si));
			} else {
				break;
			}
		}

		return new Molecule(elements, factor, phase);
	}

	private Element parseElement(StringIterator si) throws IllegalArgumentException {
		StringBuilder sb = new StringBuilder();
		int factor = 1;
		while (si.hasNext()) {
			sb.append((char) si.next());
			if (si.hasNext() && (si.isDigit(si.peekNext()) || !si.isLowerCase(si.peekNext())))
				break;
		}
		if (si.hasNext() && si.isDigit(si.peekNext())) {
			StringBuilder sb2 = new StringBuilder();
			while (si.hasNext() && si.isDigit(si.peekNext())) {
				sb2.append(si.getIntValue(si.next()));
			}
			factor = Integer.parseInt(sb2.toString());
		}
		return new Element(sb.toString(), factor);
	}

	private void addElements(Molecule molecule, List<Element> elements) {
		for (Element e : molecule.getElements()) {
			if (!(e instanceof Molecule)) {
				Element el = getElement(e.getData(), elements);
				if (el == null) {
					elements.add(new Element(e.getData(), e.getFactor()));
				} else {
					el.setFactor(el.getFactor() + e.getFactor());
				}
			} else {
				addElements((Molecule) e, elements);
			}
		}
	}

	private Element getElement(String data, List<Element> elements) {
		for (Element e : elements)
			if (e.getData().contentEquals(data))
				return e;
		return null;
	}

}
