/**
 * @author Luis Vidal Rico 45927898A
 */

package model;

import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * Bloques sólidos.
 * @author Luis Vidal Rico
 *
 */
public class SolidBlock extends Block{

	/**
	 * Dropeo del bloque
	 */
	private ItemStack drops;
	
	/**
	 * @return the drops
	 */
	public ItemStack getDrops() {
		return drops;
	}

	/**
	 * Reemplaza los items que contiene un bloque.
	 * Elimina cualquier contenido previo.
	 * 
	 * @param type Tipo de drop
	 * @param amount Cantidad de drop
	 * @throws StackSizeException excepcion
	 */
	public void setDrops(Material type, int amount) throws StackSizeException {
		
		if(this.getType().getSymbol() != 'C' && amount == 1) { //Caso en el que no es un cofre, pero amount es correcto
		
			
			
			try {
				drops = new ItemStack(type,amount);
			}catch(StackSizeException ex) {
				System.out.println(ex.getMessage());
			}
		
		}else if((this.getType().getSymbol() != 'C' && amount > 1) || (this.getType().getSymbol() == 'C' && amount > ItemStack.MAX_STACK_SIZE)) { //Caso en el que no es un cofre y amount es diff de 1, o es un cofre y amount es mayor que MAX_STACK_SIZE
		
			throw new StackSizeException();
			
		}else if(this.getType().getSymbol() == 'C' && amount < ItemStack.MAX_STACK_SIZE) {//Caso en el que es un cofre y la cantidad es la correcta			
			
			try {
				drops = new ItemStack(type,amount);
			}catch(StackSizeException ex) {
				System.out.println(ex.getMessage());
			}
			
		}
	
	}
	
	/**
	 * Constructor sobrecargado
	 * @param m Material
	 * @throws WrongMaterialException Material no es un bloque
	 */
	public SolidBlock(Material m) throws WrongMaterialException{
		
		super(m);
		
		if(m.isLiquid() || !m.isBlock()) {
			
			throw new WrongMaterialException(m);
			
		}
	
	}
	
	/**
	 * Constructor de copia
	 * @param b SolidBlock
	 */
	protected SolidBlock(SolidBlock b) {
		
		super(b);
		drops = b.getDrops();
		
	}
	
	/**
	 * Devuelve una copia del  this
	 * @return Block
	 */
	public Block clone() {
		
		return new SolidBlock(this);
		
	}
	
	/**
	 * Devuelve cierto si la cantidad de daño (damage) es igual o mayor que la dureza del material del bloque.
	 * @param dmg Daño a comprobar
	 * @return Boolean
	 */
	public boolean breaks(double dmg) {
		
		if(dmg >= getType().getValue()) {
		
			return true;
		
		}else {
			
			return false;
			
		}
		
		
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((drops == null) ? 0 : drops.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SolidBlock other = (SolidBlock) obj;
		if (drops == null) {
			if (other.drops != null)
				return false;
		} else if (!drops.equals(other.drops))
			return false;
		return true;
	}
	
	
	
}