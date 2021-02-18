/**
 * @author Luis Vidal Rico 45927898A
 */
package model;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.BadInventoryPositionException;

/**
 * Define el inventario del jugador
 * @author luis
 *
 */
public class Inventory {

	/**
	 * Objeto actual en la mano
	 */
	private ItemStack inHand;
	
	
	/**
	 * Lista de items que tiene el jugador en el inventario
	 */
	private List<ItemStack> items;
	
	/**
	 * Crea un inventario vacío
	 */
	public Inventory(){
		
		inHand = null;
		items = new ArrayList<ItemStack>();
		
	}
	
	/**
	 * Constructor de copia
	 * @param i Inventory
	 */
	public Inventory(Inventory i) {
		this.inHand = i.getItemInHand();
		items = new ArrayList<ItemStack>();
		
		/**
		 * Recorremos todo el inventario pasado por parámetro para ir asignando
		 * uno a uno todos los items de este al inventario.
		 */
		for(int y = 0; y < i.getSize();y++) {
			
			items.add(i.getItem(y));
			
		}
		
	}
	
	
	/**
	 * Añade una pila de items al inventario en un nuevo espacio
	 * 
	 * @param item Pila de items
	 * @return Cantidad de items añadidos
	 */
	public int addItem(ItemStack item) {
		
		items.add(item);
		
		return item.getAmount();
		
	}
	
	
	/**
	 * Vacía el inventario, incluida la mano
	 */
	public void clear() {
		
		items.clear();
		
		inHand = null;
		
	}
	
	
	/**
	 * Elimina el slot pasado por parámetro.
	 * Si el slot no existe, lanza la exc. BadInventoryPositionException
	 * 
	 * @param slot Slot a eliminar
	 * @throws BadInventoryPositionException Excepcion
	 */
	public void clear(int slot) throws BadInventoryPositionException{
		int size = items.size();
		
		if(slot >= size) { //AQUÍ LANZA LA EXCEPCIÓM
			
			throw new BadInventoryPositionException(slot);
			
		}else {
			
			items.remove(slot);
			
		}
		
		
	}
	
	
	/**
	 * Encuentra la posición donde se encuentra el primer material pasado
	 * y la devuelve, o -1 si no la encuentra
	 * 
	 * @param material material a encontrar
	 * @return Posicion del material o -1 si no lo ha encontrado
	 */
	public int first(Material material) {
		
		int index = -1;
		
		
		
		for(int i = 0; i < items.size();i++) {
			
			ItemStack item = new ItemStack(items.get(i));
			
			if(material.equals(item.getType())){
				
				index = i;
				
				i = items.size(); //Acabamos el for para que la posición sea la del primer bloque encontrado.
			
			}
			
		}
		
		return index;
		
	}
	
	/**
	 * Devuelve los items de una posicion
	 * 
	 * @param a La posición
	 * @return Devuelve el item, o null si la posición se excede
	 */
	public ItemStack getItem(int a) {
		
		if(a >= items.size() || a < 0) {
			
			return null;
			
		}else {
			
			return items.get(a);
			
		}
		
	}
	
	/**
	 * Encargada de devolver el item de la mano
	 * 
	 * @return inHand
	 */
	public ItemStack getItemInHand() {
		
		return inHand;
		
	}
	
	/**
	 * Devuelve el tamaño del inventario, sin contar el objeto en mano
	 * 
	 * @return items.size()
	 */
	public int getSize() {return items.size();}
	
	/**
	 * Añade un item a una posición determinada del
	 * inventario
	 * 
	 * @param pos Posición donde añadirla
	 * @param items Item a añadir
	 * @throws BadInventoryPositionException 
	 */
	public void setItem(int pos, ItemStack items) throws BadInventoryPositionException{
		
		int size = this.items.size();
		
		if(pos >= size) { //Aquí lanza la excpecion
			
			throw new BadInventoryPositionException(pos);
			
		}else {
			
			this.items.set(pos,items);
			
		}
		
	}
	
	/**
	 * Asigna un nuevo item a la mano.
	 * Si antes había un item, lo reemplaza.
	 * Si se pasa null, indicará que la mano está vacía
	 * 
	 * @param items item a asignar
	 */
	public void setItemInHand(ItemStack items) {inHand = items;}


	@Override
	public String toString() {
		String s = "";
		
		if(inHand != null)
			s = "(inHand=(" + inHand.getType() + "," + inHand.getAmount() + "),[";
		else
			s = "(inHand=null,[";
		for(int i = 0; i < getSize();i++) {
			
			items.get(i).toString();
			
			s = s + items.get(i).toString();
			
			//Añade el ", " siempre que no sea el utlimo item
			if(!(i ==  getSize()-1)){
				
				s = s + ", ";
				
			}
			
			
		}
		
		s = s + "])";
		
		return s;
		
		
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inHand == null) ? 0 : inHand.hashCode());
		result = prime * result + ((items == null) ? 0 : items.hashCode());
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
		Inventory other = (Inventory) obj;
		if (inHand == null) {
			if (other.inHand != null)
				return false;
		} else if (!inHand.equals(other.inHand))
			return false;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}
	
	
	
	
	
	
	
	
	
	
}
