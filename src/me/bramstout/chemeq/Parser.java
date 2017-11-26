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

/**
 * (NL) Deze klas zet een reactievergelijking als tekst om naar de klass
 * 'Reaction' om door een solver gebruikt te kunnen worden. <br>
 * (EN) This class converts an equation in text form, to the class 'Reaction',
 * to be used by the solver.
 * 
 * @author Bram Stout
 *
 */
public class Parser {

	/**
	 * (NL) Constructor. <br>
	 * (EN) Constructor.
	 */
	public Parser() {
	}

	/**
	 * (NL) Zet een String object om naar een Reaction object. <br>
	 * (EN) Converts a String object to a Reaction object.
	 * 
	 * @param s
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Reaction parse(String s) throws IllegalArgumentException {
		try {
			// (NL) Maak een StringIterator object om over elke karakter te gaan. Wij
			// vervangen nu alvast wat karakters met andere om de andere code simpeler te
			// maken.
			// (EN) Make a StringIterator object to iterate over all the characters. We
			// replace some characters already to make the code simpler.
			StringIterator si = new StringIterator(
					" " + s.trim().replace('\t', ' ').replace('[', '(').replace(']', ')'));

			// (NL) Roep een methode om de linker kant van de vergelijking te parsen.
			// (EN) Call a method to parser the left side of the equation.
			List<Molecule> leftTerm = parseTerm(si);

			// (NL) Sla alle rare tekens over, waaronder de '=' en '->'.
			// (EN) Skip all weird characters, including '=' and '->'.
			while (si.hasNext() && !si.isWhitespace(si.peekNext()) && !si.isDigit(si.peekNext())
					&& !si.isLetter(si.peekNext()))
				si.skip();
			si.skipSpaces();

			// (NL) Parse de rechter helft.
			// (EN) Parse the right half.
			List<Molecule> rightTerm = parseTerm(si);

			// (NL) Kijk welke atoomsoorten en hoeveel wij in totaal hebben, en ook voor
			// enkel de linker en rechter kant.
			// (EN) Check which types of atoms and how much we have in total, and for just
			// the left and right side.
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

			// (NL) Geef het nieuwe object terug.
			// (EN) Return the new object.
			return new Reaction(leftTerm, rightTerm, elements, leftTermElements, rightTermElements);
		} catch (Exception ex) {
			// (NL) Als er ergens een fout is, dan wordt dit stukje geroepen.
			// (EN) If there is an error anywhere while parsing, this piece of code is
			// called.
			IllegalArgumentException e = new IllegalArgumentException(
					"Error while parsing caused exception: " + ex.getMessage());
			e.initCause(ex);
			throw e;
		}
	}

	/**
	 * (NL) Parse een hele kant van de vergelijking. <br>
	 * (EN) Parse a whole side of the equation.
	 * 
	 * @param si
	 *            (NL) De StringIterator object. (EN) The StringIterator object.
	 * @return (NL) De lijst met moleculen in die kant. (EN) The list of molecules
	 *         on that side.
	 * @throws IllegalArgumentException
	 */
	private List<Molecule> parseTerm(StringIterator si) throws IllegalArgumentException {
		List<Molecule> term = new ArrayList<Molecule>();

		// (NL) Ga steeds naar de volgende karakter. Stop wanneer wij een '=' of '-'
		// raken, of wanneer wij aan het einde van de invoer zijn.
		// (EN) Keep going to the next character. Stop when we encounter a '=' or '-',
		// or when we hit the end of the input.
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

			// (NL) Wou zouden nu een molecuul moeten hebben geraakt. Dus parse die
			// molecuul.
			// (EN) We should have encountered a molecule, so parse the molecule.
			term.add(parseMolecule(si));
		}

		return term;
	}

	/**
	 * (NL) Parse een molecuul. <br>
	 * (EN) Parse a molecule
	 * 
	 * @param si
	 *            (NL) De StringIterator object. (EN) The StringIterator object.
	 * @return (NL) De molecuul. (EN) The molecule.
	 * @throws IllegalArgumentException
	 */
	private Molecule parseMolecule(StringIterator si) throws IllegalArgumentException {
		// (NL) Maak alvast alle gegevens klaar om te vullen.
		// (EN) Prepare all the data to be filled in later.
		List<Element> elements = new ArrayList<Element>();
		int factor = -1;
		Phase phase = Phase.NULL;
		int charge = 0;

		// (NL) Als wij aan het einde van de invoer zitten, dan geven wij gewoon een
		// lege molecuul weer.
		// (EN) If we are at the end of the input, we just return an empty molecule.
		if (!si.hasNext())
			return new Molecule(elements, factor, Phase.NULL, 0);

		// (NL) Kijk of het begint met een cijfer, zoja, dan is het een coëfficiënt en
		// lezen wij de.
		// (EN) Check if it starts with a number. If so, it is the coefficient and we
		// read that.
		if (si.isDigit(si.peekNext())) {
			StringBuilder sb = new StringBuilder();
			while (si.hasNext() && si.isDigit(si.peekNext())) {
				sb.append(si.getIntValue(si.next()));
			}
			factor = Integer.parseInt(sb.toString());
			si.skipSpaces();
		}

		// (NL) Ga langs elke karakter om te kijken naar elementen.
		// (EN) Iterate over every character to search for elements.
		while (si.hasNext()) {
			// (NL) Als het begint met een '(' dan is het of een fase of een molecuul in een
			// molecuul.
			// (EN) If it starts with a '(' then it is either a phase or a molecule in a
			// molecule.
			if (si.peekNext() == (int) '(') {
				// (NL) Kijk of het een fase is, anders dan laten roepen wij de 'parseMolecule'
				// methode.
				// (EN) Check if it is a phase, else we call the 'parseMolecule' method.
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
				// (NL) Kijk of er enige indexes zijn voor de molecuul als element.
				// (EN) Check for any indices for the molecule as element.
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
				// (NL) Als het begint met een letter, dan is het een element en parsen wij die.
				// (EN) If it starts with a letter, it is an element and we parse it.
				elements.add(parseElement(si));
			} else if (si.peekNext() == (int) '+' || si.peekNext() == (int) '-') {
				// (NL) Wij hebben een '+' of '-' gevonden, nu moeten wij kijken of het een
				// lading aangeeft of een nieuw molecuul.
				// (EN) We encountered a '+' or '-', we now need to check whether it indicates a
				// charge or a new molecule.
				if (si.left() <= 1 || si.isWhitespace(si.peek(2)) || si.peek(2) == (int) '+' || si.peek(2) == (int) '-' || si.peek(2) == (int) '=') {
					// (NL) Erna is een spatie of nog een '+', dus het moet een lading zijn. Dus wij
					// kijken of het een '+' was, dan zetten wij de lading naar 1, en anders was het
					// een '-' en is de lading -1.
					// (EN) After it, there is a whitespace or another '+', so it has to be a
					// charge. So we check if it was a '+', if so we set the charge to 1. Else, it
					// was a '-' and the charge is -1.
					charge = si.peekNext() == (int) '+' ? 1 : -1;
					si.skip();
					// (NL) Zorg ervoor om te stoppen met het molecuul te lezen aangezien dit het
					// einde is.
					// (EN) Make sure to stop with reading the molecule, as this is the end.
					break;
				} else if (si.isDigit(si.peek(2))) {
					// (NL) Na de '+' of '-' zit nog een cijfer is, dus is het de lading, of een
					// coëfficiënt van een nieuw molecuul. Als na de cijfers een spatie of '+' zit,
					// dan is het een lading.
					// (EN) After the '+' or '-' there is another number, so it is either the
					// charge, or the coefficient of a new molecule. If after the numbers there is a
					// whitespace or '+', then if is the charge.
					int i = 2;
					StringBuilder sb = new StringBuilder();
					while (si.left() >= i && si.isDigit(si.peek(i))) {
						sb.append(si.getIntValue(si.peek(i++)));
					}
					if (si.left() < i || si.isWhitespace(si.peek(i)) || si.peek(i) == (int) '+' || si.peek(i) == (int) '-' || si.peek(i) == (int) '=') {
						// (NL) Het is een lading.
						// (EN) It is a charge.
						charge = Integer.parseInt(sb.toString()) * (si.peekNext() == (int) '+' ? 1 : -1);
						si.skip(i - 1);
						// (NL) Zorg ervoor om te stoppen met het molecuul te lezen aangezien dit het
						// einde is.
						// (EN) Make sure to stop with reading the molecule, as this is the end.
						break;
					} else {
						// (NL) Het was geen lading, en het molecuul is dus al gestopt.
						// (EN) It was not a charge, and thus the molecule has already stopped.
						break;
					}
				} else {
					// (NL) Het was geen lading, en het molecuul is dus al gestopt.
					// (EN) It was not a charge, and thus the molecule has already stopped.
					break;
				}
			} else {
				// (NL) Wij hebben iets anders gevonden, dus is de molecuul voorbij.
				// (EN) We encountered something different, so the molecule is over.
				break;
			}
		}

		// (NL) Geef de nieuwe molecuul als object terug.
		// (EN) Return the new molecule as an object.
		return new Molecule(elements, factor, phase, charge);
	}

	/**
	 * (NL) Parse een element. <br>
	 * (EN) Parse an element.
	 * 
	 * @param si
	 *            (NL) De StringIterator object. (EN) The StringIterator object.
	 * @return (NL) De element. (EN) The element.
	 * @throws IllegalArgumentException
	 */
	private Element parseElement(StringIterator si) throws IllegalArgumentException {
		StringBuilder sb = new StringBuilder();
		int factor = 1;

		// (NL) Voeg elke letter toe aan de StringBuilder. Stop wanneer het geen letter
		// is, of als het een hoofdletter is, omdat dan het een nieuw element is.
		// (EN) Add every letter to a StringBuilder. Stop when it is not a letter
		// anymore, or when it is upper case, because it is then a new element.
		while (si.hasNext()) {
			sb.append((char) si.next());
			if (si.hasNext() && (si.isDigit(si.peekNext()) || !si.isLowerCase(si.peekNext())))
				break;
		}

		// (NL) Als er een cijfer achter zit, dan willen wij het getal krijgen en als
		// index zetten.
		// (EN) If there is a number behind it, we want that and set it as the index.
		if (si.hasNext() && si.isDigit(si.peekNext())) {
			StringBuilder sb2 = new StringBuilder();
			while (si.hasNext() && si.isDigit(si.peekNext())) {
				sb2.append(si.getIntValue(si.next()));
			}
			factor = Integer.parseInt(sb2.toString());
		}

		// (NL) Geef de element als object terug.
		// (EN) Return the element as an object.
		return new Element(sb.toString(), factor);
	}

	/**
	 * (NL) Voeg alle elementen in een molecuul toe aan de lijst. <br>
	 * (EN) Add all the elements in a molecule to a list.
	 * 
	 * @param molecule
	 *            (NL) De molecuul. (EN) The molecule.
	 * @param elements
	 *            (NL) De lijst. (EN) The list.
	 */
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

	/**
	 * (NL) Krijg de element met dezelfde atoomsoort uit de lijst. Het is 'null' als
	 * die niet bestaat. <br>
	 * (EN) Get the element with the same type from the list. It is 'null' when it
	 * does not exist.
	 * 
	 * @param data
	 *            (NL) De atoomsoort. (EN) The type
	 * @param elements
	 *            (NL) De lijst. (EN) The list.
	 * @return (NL) De element, of 'null' als die niet bestaat. (EN) The element, or
	 *         'null' if it does not exist.
	 */
	private Element getElement(String data, List<Element> elements) {
		for (Element e : elements)
			if (e.getData().contentEquals(data))
				return e;
		return null;
	}

}
