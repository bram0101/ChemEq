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
 * (NL) Een hulp klas om alles mooi op het beeldscherm te kunnen weergeven. <br>
 * (EN) An utility class to display everything nicely on the monitor.
 * 
 * @author Bram Stout
 *
 */
public class DisplayUtil {

	/**
	 * (NL) Weergeef een reactie als tekst. <br>
	 * (EN) Represent a chemical reaction as text.
	 * 
	 * @param r
	 *            (NL) De reactievergelijking. (EN) The chemical reaction.
	 * @return (NL) Een tekst weergave. (EN) A textual representation.
	 */
	public static String reactionToString(Reaction r) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < r.getLeftTerm().size(); i++) {
			sb.append(moleculeToString(r.getLeftTerm().get(i)));
			if (i < r.getLeftTerm().size() - 1)
				sb.append(" + ");
		}

		sb.append(" -> ");

		for (int i = 0; i < r.getRightTerm().size(); i++) {
			sb.append(moleculeToString(r.getRightTerm().get(i)));
			if (i < r.getRightTerm().size() - 1)
				sb.append(" + ");
		}

		return sb.toString();
	}

	/**
	 * (NL) Weergeef een molecuul als tekst. <br>
	 * (EN) Represent a molecule as text.
	 * 
	 * @param m
	 *            (NL) De molecuul. (EN) The molecule.
	 * @return (NL) Een tekst weergave. (EN) A textual representation.
	 */
	public static String moleculeToString(Molecule m) {
		StringBuilder sb = new StringBuilder();

		if (m.getFactor() != 1)
			sb.append(Integer.toString(m.getFactor()));

		for (Element el : m.getElements()) {
			sb.append(elementToString(el));
		}
		
		if(m.getCharge() != 0) {
			if(m.getCharge() == 1)
				sb.append(numberToSuperscript("+"));
			else if(m.getCharge() > 1)
				sb.append(numberToSuperscript("+" + Integer.toString(m.getCharge())));
			else if(m.getCharge() == -1)
				sb.append(numberToSuperscript("-"));
			else if(m.getCharge() < -1)
				sb.append(numberToSuperscript(Integer.toString(m.getCharge())));
		}

		return sb.toString();
	}

	/**
	 * (NL) Weergeef een element als tekst. <br>
	 * (EN) Represent an element as text.
	 * 
	 * @param e
	 *            (NL) Het element. (EN) The element.
	 * @return (NL) Een tekst weergave. (EN) A textual representation.
	 */
	public static String elementToString(Element e) {
		if (e instanceof Molecule) {
			StringBuilder sb = new StringBuilder();

			sb.append("(");

			for (Element el : ((Molecule) e).getElements()) {
				sb.append(elementToString(el));
			}

			sb.append(")");

			if (e.getFactor() != 1)
				sb.append(numberToSubscript(Integer.toString(e.getFactor())));

			return sb.toString();
		}
		return e.getData() + (e.getFactor() == 1 ? "" : numberToSubscript(Integer.toString(e.getFactor())));
	}

	/**
	 * (NL) Geef de nummers in het String object weer als subscript unicode nummers.
	 * <br>
	 * (EN) Return the numbers in the String object as subscript unicode numbers.
	 * 
	 * @param a
	 *            (NL) Het String object met nummers. (EN) The String object with
	 *            numbers.
	 * @return (NL) Een String object met subscript unicode nummers. (EN) A String
	 *         object with subscript unicode numbers.
	 */
	public static String numberToSubscript(String a) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			int codePoint = a.codePointAt(i);

			if (Character.isDigit(codePoint)) {
				int intVal = Character.getNumericValue(codePoint);
				sb.append(new String(new int[] { 8320 + intVal }, 0, 1));
			}
		}
		return sb.toString();
	}
	
	/**
	 * (NL) Geef de nummers in het String object weer als superscript unicode nummers.
	 * <br>
	 * (EN) Return the numbers in the String object as superscript unicode numbers.
	 * 
	 * @param a
	 *            (NL) Het String object met nummers. (EN) The String object with
	 *            numbers.
	 * @return (NL) Een String object met subscript unicode nummers. (EN) A String
	 *         object with subscript unicode numbers.
	 */
	public static String numberToSuperscript(String a) {
		final String[] lookup = {"⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "⁺", "⁻"};
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length(); i++) {
			int codePoint = a.codePointAt(i);

			if (Character.isDigit(codePoint)) {
				int intVal = Character.getNumericValue(codePoint);
				sb.append(lookup[intVal]);
			}else if(codePoint == (int) '+') {
				sb.append(lookup[10]);
			}else if(codePoint == (int) '-') {
				sb.append(lookup[11]);
			}
		}
		return sb.toString();
	}
	
	public static String getValueFromSuperscript(int codepoint) {
		final String[] lookup = {"⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹", "⁺", "⁻"};
		for(int i = 0; i < 10; i++)
			if(lookup[i].codePointAt(0) == codepoint)
				return i + "";
		if(lookup[10].codePointAt(0) == codepoint)
			return "+";
		if(lookup[11].codePointAt(0) == codepoint)
			return "-";
		return null;
	}

}
