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

public class Reaction {

	private List<Molecule> leftTerm;
	private List<Molecule> rightTerm;
	private List<Element> elements;
	private List<Element> leftTermElements;
	private List<Element> rightTermElements;

	public Reaction(List<Molecule> leftTerm, List<Molecule> rightTerm, List<Element> elements, List<Element> leftTermElements, List<Element> rightTermElements) {
		super();
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.elements = elements;
		this.leftTermElements = leftTermElements;
		this.rightTermElements = rightTermElements;
	}

	public List<Molecule> getLeftTerm() {
		return leftTerm;
	}

	public void setLeftTerm(List<Molecule> leftTerm) {
		this.leftTerm = leftTerm;
	}

	public List<Molecule> getRightTerm() {
		return rightTerm;
	}

	public void setRightTerm(List<Molecule> rightTerm) {
		this.rightTerm = rightTerm;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
	
	public List<Element> getLeftTermElements(){
		return leftTermElements;
	}
	
	public List<Element> getRightTermElements(){
		return rightTermElements;
	}
	
	public String toHTMLString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < leftTerm.size(); i++) {
			sb.append(leftTerm.get(i).toHTMLString());
			if(i < leftTerm.size() - 1)
				sb.append(" + ");
		}
		sb.append(" = ");
		for(int i = 0; i < rightTerm.size(); i++) {
			sb.append(rightTerm.get(i).toHTMLString());
			if(i < rightTerm.size() - 1)
				sb.append(" + ");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < leftTerm.size(); i++) {
			sb.append(leftTerm.get(i).toString());
			if(i < leftTerm.size() - 1)
				sb.append(" + ");
		}
		sb.append(" = ");
		for(int i = 0; i < rightTerm.size(); i++) {
			sb.append(rightTerm.get(i).toString());
			if(i < rightTerm.size() - 1)
				sb.append(" + ");
		}
		
		sb.append(" {elements:[");
		for(Element e : elements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("],");
		sb.append(" left_term:[");
		for(Element e : leftTermElements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("],");
		sb.append(" right_term:[");
		for(Element e : rightTermElements)
			sb.append(e.getData() + ": " + e.getFactor() + ", ");
		sb.append("]}");
		return sb.toString();
	}

	public Reaction copy() {
		return new Reaction(leftTerm, rightTerm, elements, leftTermElements, rightTermElements);
	}

}
