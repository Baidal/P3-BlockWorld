package model.entities;

import model.Location;

/**
 * Superclase que representa todas las entidades del mundo
 * @author Luis Vidal Rico 45927898A
 *
 */
public abstract class  LivingEntity {

	/**
	 * Salud del jugador. Double
	 */
	private double health;
	
	/**
	 * location de location. Location
	 */
	protected Location location;
	
	/**
	 * Tamaño máximo de la barra de vida. Double
	 */
	public static final double MAX_HEALTH = 20;
	
	
	/**
	 * Getter
	 * @return double
	 */
	public double getHealth() {
		return health;
	}
	
	/**
	 * Crea una entidad en loc, con salud health
	 * @param loc Location
	 * @param health Double
	 */
	public LivingEntity(Location loc, double health) {
		
		location = new Location(loc);
		setHealth(health);
		
	}
	
	
	
	/**
	 * Establece la vida, que satura en MAX_HEALTH
	 * 
	 * @param health the health to set
	 */
	public void setHealth(double health) {
		
		if(health > MAX_HEALTH)
			this.health = MAX_HEALTH;
		else
			this.health = health;
		
		
	}
	
	/**
	 * Getter
	 * 
	 * @return location
	 */
	public Location getLocation() {
		
		//copia defensiva 
		Location loc = new Location(location);
		
		return loc;
	}
	
	/**
	 * Comprueba si el jugador está muerto
	 * 
	 * @return True si está muerto, false si no
	 */
	public boolean isDead() {
		
		if(health <= 0)
			return true;
		else
			return false;
		
	}
	
	

	/**
	 * Resta amount a health
	 * @param amount double
	 */
	public void damage(double amount) {
		
		health = health - amount;
		
	}
	
	/**
	 * Abstracto. Devuelve le carácter que representa la entidad.
	 * @return char
	 */
	public abstract char getSymbol();
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(health);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LivingEntity other = (LivingEntity) obj;
		if (Double.doubleToLongBits(health) != Double.doubleToLongBits(other.health))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LivingEntity [health=" + health + ", location=" + location + "]";
	}
	

	
	
	
}
