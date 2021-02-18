package model.persistence;

import model.Inventory;
import model.ItemStack;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public class InventoryAdapter implements IInventory {

	
	/**
	 * Inventory inventory
	 */
	private Inventory inventory;
	
	/**
	 * Constructor sobrecargado a partir de un objeto Inventory
	 * @param i Inventory
	 */
	public InventoryAdapter(Inventory i) {
		
		inventory  = i; 
		
	}
	
	
	
	@Override
	public ItemStack getItem(int pos) {
	
		return inventory.getItem(pos);
		
	}

	@Override
	public int getSize() {
		
		return inventory.getSize();
		
	}

	@Override
	public ItemStack inHandItem() {
		
		return inventory.getItemInHand();
		
	}

}
