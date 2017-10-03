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

	public Reaction parse(String s) {
		StringIterator si = new StringIterator(" " + s.trim().replace('\t', ' '));

		List<Molecule> leftTerm = parseTerm(si);

		while (si.hasNext() && !Character.isWhitespace(si.peekNext()))
			si.skip();
		si.skipSpaces();

		List<Molecule> rightTerm = parseTerm(si);
		List<Element> elements = new ArrayList<Element>();

		// TODO: Go through the terms, and collect the elements

		return new Reaction(leftTerm, rightTerm, elements);
	}

	private List<Molecule> parseTerm(StringIterator si) {
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
			term.add(parseMolecule(si));
		}

		return term;
	}

	private Molecule parseMolecule(StringIterator si) {
		List<Element> elements = new ArrayList<Element>();
		int factor = 0;

		if (!si.hasNext())
			return new Molecule(elements, factor);

		if (Character.isDigit(si.peekNext())) {
			StringBuilder sb = new StringBuilder();
			while (si.hasNext() && Character.isDigit(si.peekNext())) {
				sb.append(si.next());
			}
			factor = Integer.parseInt(sb.toString());
		}

		while (si.hasNext()) {
			if (si.peekNext() == '(') {
				si.skip();
				elements.add(parseMolecule(si));
				if (si.hasNext() && si.peekNext() != ')')
					System.err.println("Err: inline molecule has no closure. Missing ')' at " + si.getIndex()
							+ ", found character '" + si.peekNext() + "'");
				si.skip();
			} else if (Character.isLetter(si.peekNext())) {
				elements.add(parseElement(si));
			} else {
				break;
			}
		}

		return new Molecule(elements, factor);
	}

	private Element parseElement(StringIterator si) {
		StringBuilder sb = new StringBuilder();
		int factor = 0;
		while (si.hasNext()) {
			sb.append(si.next());
			if (si.hasNext() && (Character.isDigit(si.peekNext()) || Character.isUpperCase(si.peekNext())))
				break;
		}
		if(si.hasNext() && Character.isDigit(si.peekNext())) {
			StringBuilder sb2 = new StringBuilder();
			while (si.hasNext() && Character.isDigit(si.peekNext())) {
				sb2.append(si.next());
			}
			factor = Integer.parseInt(sb2.toString());
		}
		return new Element(sb.toString(), factor);
	}

}
