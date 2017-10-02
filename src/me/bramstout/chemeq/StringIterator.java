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

	public char next() {
		return s.charAt(++index);
	}

	public boolean hasNext() {
		return index < size - 1;
	}
	
	public void skip() {
		index++;
	}

	public char get() {
		return s.charAt(index);
	}

	public char peekNext() {
		return s.charAt(index + 1);
	}

	public char peekPrevious() {
		return s.charAt(index - 1);
	}

	public char peek(int offset) {
		return s.charAt(index + offset);
	}

	public char first() {
		return s.charAt(0);
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

}
