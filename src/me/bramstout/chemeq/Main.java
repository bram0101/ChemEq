package me.bramstout.chemeq;

import java.lang.Thread.UncaughtExceptionHandler;

public class Main {
	
	public static final String VERSION = "v0.1";
	
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
		App.run(args);
	}
	
}
