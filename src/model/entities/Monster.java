package model.entities;

import model.Location;

/**
 * Monstruos
 * @author Luis Vidal Rico 45927898A
 *
 */
public class Monster extends Creature {

	/**
	 * Symbolo de Monster
	 */
	private static char symbol = 'M';
	
	@Override
	public char getSymbol() {return symbol;}
	
	/**
	 * Constructor sobrecargado que llama al constructor de LivingEntity
	 * @param loc Location
	 * @param health health
	 */
	public Monster(Location loc, double health) {
		
		super(loc,health);
		
	}
	
	@Override
	public String toString() {
		return "Monster [location=" + location + ", health=" + getHealth() + "]";
	}

}
