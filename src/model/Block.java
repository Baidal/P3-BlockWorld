/**
 * @author Luis Vidal Rico 45927898A
 */

package model;

import model.exceptions.WrongMaterialException;


/**
 * Clase encargada de asignar la definición de bloque,
 * permitiendo asignar los drops de estos.
 * @author luis
 *
 */
public abstract class Block {
	
	
	
	
	/**
	 * Tipo de material del bloque
	 */
	private Material type;
	



	/**
	 * @return the type
	 */
	public Material getType() {
		return type;
	}

	/**
	 * Constructor de un bloque
	 * @param type Tipo de bloque
	 * @throws WrongMaterialException Llama la excepción si el material no es un bloque
	 */
	public Block(Material type) throws WrongMaterialException{
		if(!type.isBlock())
			throw new WrongMaterialException(type);
		else
			this.type = type;
	
	}
	
	/**
	 * Constructor de copia
	 * @param b bloque a copiar
	 */
	protected Block(Block b){
		 
		type = b.getType();
	
	}
	
	/**
	 * Devuelve una copia del objeto al ser invocado[implementado en las subclases]
	 * @return Block
	 */
	abstract public Block clone();

	
	

	
	/**
	 * Imprime el tipo 
	 * @return String
	 */
	public String toString() {
		 return "[" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Block other = (Block) obj;
		if (type != other.type)
			return false;
		return true;
	}


	
	
	
	
	
	
}
