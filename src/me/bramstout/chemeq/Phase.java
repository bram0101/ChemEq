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
 * (NL) Een klas om de fase mee te vertegenwoordigen. <br>
 * (EN) A class to represent the phase.
 * 
 * @author Bram Stout
 *
 */
public enum Phase {

	/**
	 * (NL) The soorten fasen. <br>
	 * (EN) The types of phases.
	 */
	NULL("", ""), SOLID("s", "s"), LIQUID("l", "l"), GAS("g", "g"), DISSOLVED_IN_WATER("aq", "aq");

	/**
	 * (NL) Het symbool van de fase. <br>
	 * (EN) The symbol of the phase.
	 */
	private String token;
	/**
	 * (NL) De manieren om de fase aan te duiden. <br>
	 * (EN) The ways to indicate a fase.
	 */
	private String[] keys;

	/**
	 * (NL) De constructor. <br>
	 * (EN) The constructor.
	 * 
	 * @param token
	 *            (NL) Het symbool. (EN) The symbol.
	 * @param keys
	 *            (NL) De manieren om het aan te geven. (EN) De ways to indicate it.
	 */
	private Phase(String token, String... keys) {
		this.token = token;
		this.keys = keys;
	}

	/**
	 * (NL) Getter voor het symbool. <br>
	 * (EN) Getter for the symbol.
	 * 
	 * @return (NL) Symbool. (EN) Symbol.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * (NL) Getter voor de manieren om het weer te geven. <br>
	 * (EN) Getter for the ways to represent it.
	 * 
	 * @return (NL) Manieren om het aan te geven. (EN) Ways to represent it.
	 */
	public String[] getKeys() {
		return keys;
	}

	/**
	 * (NL) Dit geeft de fase aan. Het geeft de fase 'NULL' wanneer er geen fase is
	 * aangegeven. <br>
	 * (EN) This returns the phase. It returns the phase 'NULL' when there is no
	 * phase represented.
	 * 
	 * @param si
	 *            (NL) De StringIterator object. (EN) The stringIterator object.
	 * @return (NL) De fase. (EN) The phase.
	 */
	public static Phase getPhase(StringIterator si) {
		int i = 2;
		//(NL) Krijg naar alle tekst totdat het teken ')' te zien is. Kijk dan of er een fase is die zo wordt aangeduid. Zoja, dan geven wij die terug.
		//(EN) Get all the text until the character ')' is found. Then check if there is a phase represented like that. If so, then return that phase.
		if (si.left() > 1) {
			if (si.peek(1) == '(') {
				StringBuilder sb = new StringBuilder();
				while (si.left() >= i) {
					if (si.peek(i) == ')') {
						i++;
						break;
					}
					sb.append((char) si.peek(i++));
				}
				for (Phase p : values()) {
					for (String key : p.keys) {
						if (key.contentEquals(sb.toString())) {
							si.skip(i - 1);
							return p;
						}
					}
				}
				i = 0;
			}
		}
		return NULL;
	}

}
