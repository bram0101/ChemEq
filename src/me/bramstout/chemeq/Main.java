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

import java.lang.Thread.UncaughtExceptionHandler;

public class Main {

	/**
	 * (NL) De versie van deze applicatie. <br>
	 * (EN) The version of this application.
	 */
	public static final String VERSION = "v0.2.1";

	/**
	 * (NL) Dit is de methode die door Java wordt geroepen om het programma te
	 * start. Wij Voegen een 'handler' toe voor wanneer er een fout in het programma
	 * is. <br>
	 * (EN) This is the method that is called by Java to start the program. We add a
	 * handler for when there is an error in the program.
	 * 
	 * @param args
	 *            (NL) De waardes die waren meegegeven tijdens het opstarten van de
	 *            applicatie. (EN) The arguments given at launching this program.
	 */
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread arg0, Throwable arg1) {
				System.err.println("=========EXCEPTION=========");
				System.err.println("Uncaught exception: " + arg1.getMessage());
				arg1.printStackTrace();
				System.exit(1);
			}

		});
		// (NL) Voer de app zelf uit.
		// (EN) Run the app itself.
		App.run(args);
	}

}
