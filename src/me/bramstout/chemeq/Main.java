package me.bramstout.chemeq;

import java.lang.Thread.UncaughtExceptionHandler;

public class Main {

	/**
	 * (NL) De versie van deze applicatie. <br>
	 * (EN) The version of this application.
	 */
	public static final String VERSION = "v0.1";

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
