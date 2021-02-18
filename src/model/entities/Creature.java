package model.entities;
import model.Location;

/**
 * Superclase que hereda de LivingEntity, y que se encarga de definir las criaturas, que luego podrán ser Animal o Monster
 * @author Luis Vidal Rico 45927898A
 *
 */
public abstract class Creature extends LivingEntity {

	/**
	 * Abstract char.
	 * @return char
	 */
	public abstract char getSymbol();
	
	/**
	 * Constructor sobrecargado que solamente llama al constructor de LivingEntity
	 * @param loc Location
	 * @param health Health
	 */
	public Creature(Location loc, double health) {
		super(loc,health);
	}

}
