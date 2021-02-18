/**
 * @author Luis Vidal Rico 45927898A
 */
package model.exceptions;

/**
 * 
 * @author luis
 * Si el jugador a muerto.
 */
public class EntityIsDeadException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public EntityIsDeadException(){
		
		super("La entidad está muerta.");
		
	}
	
	
}
