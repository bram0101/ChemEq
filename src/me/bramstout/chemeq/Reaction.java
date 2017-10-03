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

	public Reaction(List<Molecule> leftTerm, List<Molecule> rightTerm, List<Element> elements) {
		super();
		this.leftTerm = leftTerm;
		this.rightTerm = rightTerm;
		this.elements = elements;
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
		return sb.toString();
	}

}
