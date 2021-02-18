/**
 * @author Luis Vidal Rico 45927898A
 */
package model.exceptions;

/**
 * 
 * @author luis
 *
 * Excepción encargada de capturar una posición errónea.
 */
public class BadInventoryPositionException extends Exception{

	/**
	 * No sé
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 * @param pos posición errónea
	 */
	public BadInventoryPositionException(int pos) {
		super("La posición indicada: " + pos + " no existe.");
		
	}
	
	
	
}
