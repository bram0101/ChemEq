package me.bramstout.chemeq;

public class Element {

	private String data;
	private int factor;

	public Element(String data, int factor) {
		this.data = data;
		this.factor = factor;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getFactor() {
		return factor;
	}

	public void setFactor(int factor) {
		this.factor = factor;
	}

	@Override
	public String toString() {
		return factor == 1 ? data : data + factor;
	}

	@Override
	public boolean equals(Object a) {
		if (!(a instanceof Element))
			return false;
		if (((Element) a).data == data && ((Element) a).factor == factor)
			return true;
		return false;
	}

}
