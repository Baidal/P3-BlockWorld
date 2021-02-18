package model.exceptions;


/**
 * No se para que sirve la verdad.
 * @author luis
 *
 */
public class UnknownGameCommandException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Se produce si no se introduce el comando correcto en el fichero de texto.
	 * @param s String
	 */
	public UnknownGameCommandException(String s) {
		
		super(s);
		
	}

}
