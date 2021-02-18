/**
 * @author Luis Vidal Rico 45927898A
 */
package model;
import model.exceptions.StackSizeException;


/**
 * Define una pila de items
 * 
 * @author luis
 *
 */
public class ItemStack {
	

	/**
	 * Cantidad de un objeto que se tiene en el inventario
	 */
	private int amount;
	
	/**
	 * Tipo del objeto
	 */
	private Material type;
	
	/**
	 * Tamaño máximo de amount
	 */
	public static final int MAX_STACK_SIZE = 64;
	
	
	/**
	 * Constructor
	 * 
	 * @param type Tipo de objeto
	 * @param amount Cantidad del objeto
	 * @throws StackSizeException Excepción
	 */
	public ItemStack(Material type, int amount) throws StackSizeException{
		if(!(amount >= 1 && amount <= MAX_STACK_SIZE) || (type.isTool() && amount != 1) || (type.isWeapon() && amount != 1)){
		
			throw new StackSizeException();
			
		}else{
			this.amount = amount;
			this.type = type;
		}
	}
	
	/**
	 * Constructor de copia
	 * @param items ItemStack a copiar
	 */
	public ItemStack(ItemStack items) {
		
		amount = items.getAmount();
		type = items.getType();
		
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 * @throws StackSizeException  Excepción
	 */
	public void setAmount(int amount) throws StackSizeException {
	
		if(!(amount >= 1 && amount <= MAX_STACK_SIZE) || (type.isTool() && amount != 1) || (type.isWeapon() && amount != 1)){
			
			throw new StackSizeException();
			
		}else{
	
			this.amount = amount;
	
		}
	
	}

	/**
	 * @return the type
	 */
	public Material getType() {
		return type;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + amount;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		ItemStack other = (ItemStack) obj;
		if (amount != other.amount)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + type + "," + amount + ")";
	}
	
	
}
