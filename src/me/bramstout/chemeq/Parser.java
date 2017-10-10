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

	public Reaction parse(String s) throws IllegalArgumentException {
		try {
			StringIterator si = new StringIterator(" " + s.trim().replace('\t', ' '));

			List<Molecule> leftTerm = parseTerm(si);

			while (si.hasNext() && !Character.isWhitespace(si.peekNext()) && !Character.isDigit(si.peekNext())
					&& !Character.isLetter(si.peekNext()))
				si.skip();
			si.skipSpaces();

			List<Molecule> rightTerm = parseTerm(si);
			List<Element> elements = new ArrayList<Element>();

			for (Molecule m : leftTerm)
				addElements(m, elements);
			for (Molecule m : rightTerm)
				addElements(m, elements);

			return new Reaction(leftTerm, rightTerm, elements);
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
			char c = si.peekNext();
			if (c == '=' || c == '-')
				break;
			if (c == '+') {
				si.skip();
				si.skipSpaces();
			}
			while (si.hasNext() && !Character.isLetter(si.peekNext()) && !Character.isDigit(si.peekNext()))
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

		if (Character.isDigit(si.peekNext())) {
			StringBuilder sb = new StringBuilder();
			while (si.hasNext() && Character.isDigit(si.peekNext())) {
				sb.append(si.next());
			}
			factor = Integer.parseInt(sb.toString());
			si.skipSpaces();
		}

		while (si.hasNext()) {
			if (si.peekNext() == '(') {
				Phase p = Phase.getPhase(si);
				if (p != Phase.NULL) {
					phase = p;
					continue;
				}

				si.skip();
				Molecule m = parseMolecule(si);
				if (si.hasNext() && si.peekNext() != ')')
					throw new IllegalArgumentException("Err: inline molecule has no closure. Missing ')' at "
							+ si.getIndex() + ", found character '" + si.peekNext() + "'");
				si.skip();
				if (si.hasNext() && Character.isDigit(si.peekNext())) {
					StringBuilder sb = new StringBuilder();
					while (si.hasNext() && Character.isDigit(si.peekNext())) {
						sb.append(si.next());
					}
					m.setFactor(Integer.parseInt(sb.toString()));
				}
				elements.add(m);
			} else if (Character.isLetter(si.peekNext())) {
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
			sb.append(si.next());
			if (si.hasNext() && (Character.isDigit(si.peekNext()) || !Character.isLowerCase(si.peekNext())))
				break;
		}
		if (si.hasNext() && Character.isDigit(si.peekNext())) {
			StringBuilder sb2 = new StringBuilder();
			while (si.hasNext() && Character.isDigit(si.peekNext())) {
				sb2.append(si.next());
			}
			factor = Integer.parseInt(sb2.toString());
		}
		return new Element(sb.toString(), factor);
	}

	private void addElements(Molecule molecule, List<Element> elements) { //TODO: this does not work well yet, mainly the updating of existing elements, it just adds a new one.
		for (Element e : molecule.getElements()) {
			if (e instanceof Element) {
				Element el = getElement(e.getData(), elements);
				if(el == null) {
					elements.add(new Element(e.getData(), e.getFactor()));
				}else {
					el.setFactor(el.getFactor() + e.getFactor());
				}
			} else {
				addElements((Molecule) e, elements);
			}
		}
	}

	private Element getElement(String data, List<Element> elements) {
		for (Element e : elements)
			if (e.getData() == data)
				return e;
		return null;
	}

}
