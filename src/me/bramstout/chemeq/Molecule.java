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

public class Molecule extends Element {

	private List<Element> elements;

	public Molecule(List<Element> elements, int factor) {
		super(elementsToString(elements), factor);
	}

	public List<Element> getElements() {
		return elements;
	}

	public boolean hasElement(Element e) {
		for (Element el : elements) {
			if (el.equals(e))
				return true;
			if (el instanceof Molecule)
				if (((Molecule) el).hasElement(e))
					return true;
		}
		return false;
	}

	public Element getFirstElement(String e) {
		for (Element el : elements)
			if (el.getData() == e)
				return el;
		return null;
	}

	public List<Element> getAllElements(String e) {
		List<Element> allElements = new ArrayList<Element>();
		for (Element el : elements)
			if (el.getData() == e)
				allElements.add(el);
		return allElements;
	}

	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Molecule))
			return false;
		return super.equals(a);
	}
	
	@Override
	public String toString() {
		return "_" + getFactor() + "(" + getData() + ")";
	}

	private static String elementsToString(List<Element> elements) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements)
			sb.append(e.toString());
		return sb.toString();
	}

}
