package model.entities;

import model.ItemStack;
import model.Location;
import model.Material;
import model.exceptions.StackSizeException;


/**
 * Animales
 * @author Luis Vidal Rico 45927898A
 *
 */
public class Animal extends Creature {
	
	/**
	 * Symbolo del animal
	 */
	private static char symbol = 'L';
	
	@Override
	public char getSymbol() {
		return symbol;
	}
	
	/**
	 * Constructor sobrecargado que llama a super();
	 * @param loc Location
	 * @param health double
	 */
	public Animal(Location loc, double health) {
		
		super(loc,health);
		
	}
	
	/**
	 * Devuelve una unidad de ItemStack Beef
	 * @return ItemStack
	 */
	public ItemStack getDrops() {
		
		ItemStack b = null;
		
		try {
			b = new ItemStack(Material.BEEF,1);
		} catch (StackSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return b;
		
	}

	@Override
	public String toString() {
		return "Animal [location=" + location + ", health=" + getHealth() + "]";
	}
	
	

	
	

}
