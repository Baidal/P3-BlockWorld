package model.persistence;

import model.ItemStack;

/**
 * 
 * @author Luis Vidal Rico
 * Representa un inventario en minetest
 */
public interface IInventory {
	
	/**
	 * Devuelve el item que se encuentra en la posicion 'pos'.
	 * Si no hay item en esa posición o no existe, devuelve null.
	 * @param pos int
	 * @return ItemStack
	 */
	public ItemStack getItem(int pos);
	
	/**
	 * Devuelve el tamaño del inventario
	 * @return int
	 */
	public int getSize();
	
	/**
	 *Obtiene el item del inventario que el jugador lleva en la mano.
	 * @return
	 */
	public ItemStack inHandItem();
	
	
}
