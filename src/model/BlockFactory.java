/**
 * @author Luis Vidal Rico 45927898A
 */
package model;

import model.exceptions.WrongMaterialException;

/**
 * Clase factory que crea bloques en funcion del material pasado.
 * @author Luis Vidal Rico
 *
 */
public class BlockFactory{

	/**
	 * Crea bloques según el tipo de material pasado
	 * @param m Material
	 * @return Block
	 * @throws WrongMaterialException Si el material no es adecuado
	 */
	public static Block createBlock(Material m) throws WrongMaterialException {
		
		Block b = null;
		
		if(m.isLiquid()) {
			
			try {
				
				b = new LiquidBlock(m);
			
			}catch(WrongMaterialException e) {
			
				throw new WrongMaterialException(m);
				
			}
				
		}else {
			
			try {
				
				b = new SolidBlock(m);
			
			}catch(WrongMaterialException e) {
			
				throw new WrongMaterialException(m);
				
			}
			
			
		}
		
		return b;
		
	}
	
	
}
