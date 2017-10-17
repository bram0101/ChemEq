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

public class StringIterator {

	private String s;
	private int index;
	private int size;

	public StringIterator(String s) {
		this.s = s;
		this.index = 0;
		this.size = s.length();
	}

	public int next() {
		return s.codePointAt(++index);
	}

	public boolean hasNext() {
		return index < size - 1;
	}
	
	public int left() {
		return size - index - 1;
	}

	public void skip() {
		index++;
	}
	
	public void skip(int i) {
		index += i;
	}

	public int get() {
		return s.codePointAt(index);
	}

	public int peekNext() {
		return s.codePointAt(index + 1);
	}

	public int peekPrevious() {
		return s.codePointAt(index - 1);
	}

	public int peek(int offset) {
		return s.codePointAt(index + offset);
	}

	public int first() {
		return s.codePointAt(0);
	}

	public void reset() {
		index = 0;
	}

	public void seek(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public void skipSpaces() {
		while (hasNext() && isWhitespace(peekNext()))
			skip();
	}
	
	public boolean isDigit(int codePoint) {
		int subVal = codePoint - 8320;
		return Character.isDigit(codePoint) || (subVal >= 0 && subVal < 10);
	}
	
	public boolean isWhitespace(int codePoint) {
		return Character.isWhitespace(codePoint);
	}
	
	public boolean isLetter(int codePoint) {
		return Character.isLetter(codePoint);
	}
	
	public boolean isLowerCase(int codePoint) {
		return Character.isLowerCase(codePoint);
	}
	
	public boolean isUpperCase(int codePoint) {
		return Character.isUpperCase(codePoint);
	}
	
	public int getIntValue(int codePoint) {
		int subVal = codePoint - 8320;
		return (subVal >= 0 && subVal < 10) ? subVal : Character.getNumericValue(codePoint);
	}

}
