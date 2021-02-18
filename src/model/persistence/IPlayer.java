package model.persistence;

import model.Location;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public interface IPlayer extends IInventory {
	
	/**
	 * Devuelve la salud del jugador
	 * @return double
	 */
	public double getHealth();
	
	/**
	 * Devuelve una copia del inventario del jugador.
	 * @return IInventory
	 */
	public IInventory getInventory();
	
	/**
	 * Devuelve la posición del jugador
	 * @return Location
	 */
	public Location getLocation();
	
	/**
	 * Devuelve el nombre del jugador
	 * @return String
	 */
	public String getName();
	
	
	
}
