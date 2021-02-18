/**
 * @author Luis Vidal Rico 45927898A
 */
package model.exceptions;

/**
 * 
 * @author luis
 * Si se intenta asignar un StackSize mayor que MAX_STACK_SIZE
 */
public class StackSizeException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public StackSizeException() {
		
		super("La cantidad de items no es correcta.");
		
	}
	
}
