/**
 * @author Luis Vidal Rico 45927898A
 */

package model;

import model.exceptions.WrongMaterialException;;
/**
 * Bloques líquidos.
 * @author Luis Vidal Rico
 *
 */
public class LiquidBlock extends Block{

	/**
	 * Daño que inflinge el bloque en caso de que sea lava.
	 */
	private double damage;
	
	/**
	 * Constructor sobrecargado. Asigna la cantidad de daño que corresponde al valor del material.
	 * @param m Material
	 * @throws WrongMaterialException Si el material no es liquido
	 */
	public LiquidBlock(Material m)  throws WrongMaterialException{
		super(m);
		damage = m.getValue();
		if(!m.isLiquid()) {
			
			throw new WrongMaterialException(m);
			
		}
		
	}
	
	/**
	 * Constructor de copia
	 * @param l LiquidBlock
	 */
	protected LiquidBlock(LiquidBlock l) {
		
		super(l);
		damage = l.getDamage();
	}
	
	/**
	 * Implementaçon de block.clone()
	 * @return Block
	 */
	public Block clone() {
		
		LiquidBlock b = new LiquidBlock(this);
		
		return b;
		
	}
	
	/**
	 * Devuelve el daño que este bloque produce al atravesarlo.
	 * @return Double
	 */
	public double getDamage() {return damage;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(damage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		LiquidBlock other = (LiquidBlock) obj;
		if (Double.doubleToLongBits(damage) != Double.doubleToLongBits(other.damage))
			return false;
		return true;
	}
	
	
	
	
	
	
	
}
