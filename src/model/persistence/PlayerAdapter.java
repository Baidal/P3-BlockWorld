package model.persistence;

import model.Inventory;
import model.ItemStack;
import model.Location;
import model.entities.Player;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public class PlayerAdapter implements IPlayer {

	
	/**
	 * IInventory
	 */
	private IInventory inventory;
	
	/**
	 * Player
	 */
	private Player player;
	
	/**
	 * Construye un InventoryAdapter a partir del inventario de Player.
	 * @param p Player
	 */
	public PlayerAdapter(Player p) {
		
		player = p;
		inventory = new InventoryAdapter(p.getInventory());
		
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
		
		return inventory.inHandItem();
		
	}

	@Override
	public double getHealth() {
		
		return player.getHealth();
		
	}

	@Override
	public IInventory getInventory() {
		
		Inventory invaux = new Inventory();
		invaux.setItemInHand(inventory.inHandItem());
		for(int i = 0; i < inventory.getSize();i++) {
			
			invaux.addItem(inventory.getItem(i));
			
		}
		
		
		IInventory aux = new InventoryAdapter(invaux);
		
		return aux;
		
	}

	@Override
	public Location getLocation() {
		
		return player.getLocation();
		
	}

	@Override
	public String getName() {
		
		return player.getName();
		
	}

}
