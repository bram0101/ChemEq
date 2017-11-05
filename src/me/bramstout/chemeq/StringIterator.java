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
 * (NL) Een hulp klas om langs alle karakters in een tekst te gaan. <br>
 * (EN) A utility class to iterate over all characters in a text.
 * @author Bram Stout
 *
 */
public class StringIterator {

	/**
	 * (NL) Het String object waar wij mee bezig zijn. <br>
	 * (EN) The String object we are using.
	 */
	private String s;
	/**
//	 * (NL) De huidige positie in de tekst van waar wij zijn. <br>
	 * (EN) The current position in the text of where we are.
	 */
	private int index;
	/**
	 * (NL) De aantal karakters in de tekst. <br>
	 * (EN) The amount of characters in the text.
	 */
	private int size;

	/**
	 * (NL) Constructor. <br>
	 * (EN) Constructor.
	 * @param s (NL) Het String object. (EN) The String object.
	 */
	public StringIterator(String s) {
		this.s = s;
		this.index = 0;
		this.size = s.length();
	}

	/**
	 * (NL) Krijg de volgende karacter. <br>
	 * (EN) Get the next character.
	 * @return (NL) De volgende karakter. (EN) The next character.
	 */
	public int next() {
		return s.codePointAt(++index);
	}

	/**
	 * (NL) Kijk of er nog karakters over zijn. <br>
	 * (EN) Check if there are still characters left to read.
	 * @return (NL) 'true' als er nog karakters over zijn. (EN) 'true' is there are still characters left.
	 */
	public boolean hasNext() {
		return index < size - 1;
	}

	/**
	 * (NL) De hoeveelheid karakters over. <br>
	 * (EN) The amount of characters left.
	 * @return (NL) De hoeveelheid. (EN) The amount.
	 */
	public int left() {
		return size - index - 1;
	}

	/**
	 * (NL) Sla de huidige karacter over. <br>
	 * (EN) Skip the current character.
	 */
	public void skip() {
		index++;
	}

	/**
	 * (NL) Sla meerdere karakters over. <br>
	 * (EN) Skip multiple characters.
	 * @param i (NL) De hoeveelheid karakters om over te slaan. (EN) The amount of characters to skip.
	 */
	public void skip(int i) {
		index += i;
	}

	/**
	 * (NL) Krijg de huidige karakter. <br>
	 * (EN) Get the current character.
	 * @return (NL) De huidige karacter als UTF-8. (EN) The current character as UTF-8
	 */
	public int get() {
		return s.codePointAt(index);
	}

	/**
	 * (NL) Krijg de volgende karakter. <br>
	 * (EN) Get the next character.
	 * @return (NL) De volgende karakter. (EN) The next character.
	 */
	public int peekNext() {
		return s.codePointAt(index + 1);
	}
	
	/**
	 * (NL) Krijg de vorige karakter. <br>
	 * (EN) Get the previous character.
	 * @return (NL) De vorige karakter. (EN) The previous character.
	 */
	public int peekPrevious() {
		return s.codePointAt(index - 1);
	}

	/**
	 * (NL) Krijg de karakter. <br>
	 * (EN) Get the character.
	 * @param offset (NL) De afstand van de huidige positie. (EN) The offset from the current position.
	 * @return (NL) De karakter. (EN) De karakter.
	 */
	public int peek(int offset) {
		return s.codePointAt(index + offset);
	}

	/**
	 * (NL) De eerste karakter. <br>
	 * (EN) The first character.
	 * @return (NL) De eerste karakter. (EN) The first character.
	 */
	public int first() {
		return s.codePointAt(0);
	}

	/**
	 * (NL) Zet de positie op 0. <br>
	 * (EN) Set the position to null.
	 */
	public void reset() {
		index = 0;
	}

	/**
	 * (NL) Zet de positie. <br>
	 * (EN) Set the position.
	 * @param index (NL) De positie. (EN) The position.
	 */
	public void seek(int index) {
		this.index = index;
	}

	/**
	 * (NL) De huidige positie. <br>
	 * (EN) The current position.
	 * @return (NL) De huidige positie. (EN) The current position.
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * (NL) Sla alle spaties over. <br>
	 * (EN) SKip all whitespaces.
	 */
	public void skipSpaces() {
		while (hasNext() && isWhitespace(peekNext()))
			skip();
	}

	/**
	 * (NL) Kijk of het een getal is. <br>
	 * (EN) Check if it is a number.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) 'true' als het een getal is. (EN) 'true' if it is a number.
	 */
	public boolean isDigit(int codePoint) {
		int subVal = codePoint - 8320;
		return Character.isDigit(codePoint) || (subVal >= 0 && subVal < 10);
	}

	/**
	 * (NL) Kijk of het een spatie is. <br>
	 * (EN) Check if it is a whitespace.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) 'true' als het een spatie is. (EN) 'true' if it is a whitespace.
	 */
	public boolean isWhitespace(int codePoint) {
		return Character.isWhitespace(codePoint);
	}

	/**
	 * (NL) Kijk of het een letter is. <br>
	 * (EN) Check if it is a letter.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) 'true' als het een letter is. (EN) 'true' if it is a letter.
	 */
	public boolean isLetter(int codePoint) {
		return Character.isLetter(codePoint);
	}

	/**
	 * (NL) Kijk of het een kleine letter is. <br>
	 * (EN) Check if it is lower case.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) 'true' als het een kleine letter is. (EN) 'true' if it is lower ase.
	 */
	public boolean isLowerCase(int codePoint) {
		return Character.isLowerCase(codePoint);
	}

	/**
	 * (NL) Kijk of het een hoofdletter is. <br>
	 * (EN) Check if it is upper case.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) 'true' als het een hoofdletter is. (EN) 'true' if it is upper case.
	 */
	public boolean isUpperCase(int codePoint) {
		return Character.isUpperCase(codePoint);
	}

	/**
	 * (NL) Krijg de waarde van de cijfer, die als een karakter weergegeven is. <br>
	 * (EN) Get the value of a number that is represented as a character.
	 * @param codePoint (NL) De karakter. (EN) The character.
	 * @return (NL) De waarde als integer. (EN) The value as an integer.
	 */
	public int getIntValue(int codePoint) {
		int subVal = codePoint - 8320;
		return (subVal >= 0 && subVal < 10) ? subVal : Character.getNumericValue(codePoint);
	}

}
