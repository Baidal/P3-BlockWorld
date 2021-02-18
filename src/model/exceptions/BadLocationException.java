/**
 * @author Luis Vidal Rico 45927898A
 */
package model.exceptions;

/**
 * 
 * @author luis
 * Encargada de capturar una Localización errónea
 */
public class BadLocationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param s String
	 */
	public BadLocationException(String s) {
		super(s);
		
	}
	
	
	
}
