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

public enum Phase {

	NULL("", ""), SOLID("s", "s"), LIQUID("l", "l"), GAS("g", "g"), DISSOLVED_IN_WATER("aq", "aq");

	private String token;
	private String[] keys;

	private Phase(String token, String... keys) {
		this.token = token;
		this.keys = keys;
	}

	public String getToken() {
		return token;
	}

	public String[] getKeys() {
		return keys;
	}

	public static Phase getPhase(StringIterator si) {
		int i = 2;
		if(si.left() > 1) {
			if(si.peek(1) == '(') {
				StringBuilder sb = new StringBuilder();
				while(si.left() >= i) {
					if(si.peek(i) == ')') {
						i++;
						break;
					}
					sb.append(si.peek(i++));
				}
				for(Phase p : values()) {
					for(String key : p.keys) {
						if(key.toLowerCase().contentEquals(sb.toString().toLowerCase())) {
							si.skip(i);
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
