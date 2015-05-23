package org.target;

public class IncorrectWeightException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -179933645171773156L;

	public IncorrectWeightException(Double weight) {
		super("Weight [" + weight + "] is incorrect");
	}
}
