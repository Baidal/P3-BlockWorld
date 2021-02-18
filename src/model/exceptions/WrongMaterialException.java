/**
 * @author Luis Vidal Rico 45927898A
 */
package model.exceptions;
import model.Material;
/**
 * 
 * @author luis
 * Material Erróneo
 */
public class WrongMaterialException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param material Material
	 */
	public WrongMaterialException(Material material){
		
		super("El material " + material.name() + " ha producido WrongMaterialException");
		
	}
	
}
