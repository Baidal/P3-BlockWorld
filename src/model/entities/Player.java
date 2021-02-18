/**
 * @author Luis Vidal Rico 45927898A
 */
package model.entities;
//import java.util.Set;

import model.Inventory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.World;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;

/**
 * Clase que define el jugador y todos sus atributos.
 * 
 * @author luis
 *
 */
public class Player extends LivingEntity{
	
	

	/**
	 * Nombre del jugador
	 */
	private String name;

	/**
	 * Location
	 */
	private Location orientation;
	
	
	/**
	 * Nivel de hambre del jugador
	 */
	private double foodLevel;
	
	/**
	 * Inventorio del jugador
	 */
	private Inventory inventory;
	
	/**
	 * Tamaño máximo de la barra de hambre
	 */
	public static final double MAX_FOODLEVEL = 20;
	
	/**
	 * Char
	 */
	private static char symbol = 'P';
	
	
	/**
	 * 
	 * @param name Nombre a asignar al jugador
	 * @param world Mundo al que pertenece el jugador
	 */
	public Player(String name, World world){
		super(new Location(world,0.0,0.0,0.0), LivingEntity.MAX_HEALTH);
		
		//ASIGNACION DE LA LOCATION
		try {
			
			location = world.getHighestLocationAt(location);	
			
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			location = location.above();
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//ASIGNACIÓN DE LA ORIENTACIÓN(AL SUR DE location)
		orientation = new Location(getLocation().getWorld(),0,0,1);
		
		
		//ASIGNACION DEL INVENTARIO
		inventory = new Inventory();
		
		try {
			inventory.setItemInHand(new ItemStack(Material.WOOD_SWORD,1));
		} catch (StackSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.name = name;
		
		foodLevel = MAX_FOODLEVEL;
		
	}
	
	/**
	 * Getter. Char
	 * @return char
	 */
	public char getSymbol() {
		
		return symbol;
		
	}
	

	

	/**
	 * Getter
	 * @return the foodLevel
	 */
	public double getFoodLevel() {
		return foodLevel;
	}

	/**
	 * Se encarga de asignar el nivel de comida.
	 * Si este se pasa de MAX_FOODLEVEL, el nuevo
	 * foodLevel será igual a MAX_FOODLEVEL
	 * 
	 * @param foodLevel the foodLevel to set
	 */
	public void setFoodLevel(double foodLevel) {
		
		
		if(foodLevel > MAX_FOODLEVEL) {;
			this.foodLevel = MAX_FOODLEVEL;
		}else
			this.foodLevel = foodLevel;
	
		
	}

	/**
	 * Getter
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Devuelve una copia del inventario del jugador
	 * @return Inventory
	 */
	public Inventory getInventory() {
		
		return new Inventory(inventory);
		
	}
	
	
	
	
	/**
	 * Encargada de mover el jugador a la posición (x + dx,y + dy, z + dz).
	 * Si el jugador a muerto, la posición está ocupada, o el destino no es adyacente,
	 * no se moverá.
	 * 
	 * @param dx Posición x
	 * @param dy Posición y
	 * @param dz Posición z
	 * @return La nueva localización 
	 * @throws EntityIsDeadException La entidad está muerta
	 * @throws BadLocationException Localización errónea
	 */
	public Location move(int dx, int dy, int dz) throws EntityIsDeadException, BadLocationException{
		
		Location loc = new Location(location.getWorld(),dx + location.getX(),dy + location.getY(),dz + location.getZ());
		
		if(isDead()) {	 //LANZA EntityIsDeadException
			
			throw new EntityIsDeadException();
			
		}
		
		if(!loc.isFree() || !location.getNeighborhood().contains(loc)) { //LANZA BadLocationException
			
			throw new BadLocationException("La posición indicada no es correcta.");
			
		}
		
		if(!isDead() && loc.isFree() &&  location.getNeighborhood().contains(loc)) {//Caso en que todo esté OK
			
			location.setX(location.getX() + dx);
			location.setY(location.getY() + dy);
			location.setZ(location.getZ() + dz);
			
			decreaseFoodLevel(0.05);
			
			
			
		}
		
		return location;
		
		
	}
	
	/**
	 * Encargada de utilizar el objeto de la mano del jugador.
	 * Si es comida, restaurará comida/salud, si no, gastará comida
	 * 
	 * @param times Número de veces a utilizar el objeto.
	 * @throws IllegalArgumentException Excepción
	 * @throws EntityIsDeadException Excepción
	 * @return ItemStack. Puede devolver null
	 */
	public ItemStack useItemInHand(int times) throws IllegalArgumentException, EntityIsDeadException{
		
		boolean isempty = false;
		
		if(times <= 0) {//Lanza excepción IllegalArgumentException
			
			throw new IllegalArgumentException();
			
		}else if(isDead()) {
		
			throw new EntityIsDeadException();
		
		}else if(inventory.getItemInHand() == null) {
		
				
				
				
			
			
		}else if(inventory.getItemInHand().getType().isEdible() == true) {//inHand es comida
			
			
			
			if(inventory.getItemInHand().getAmount() <= times) {//Comprueba que no se intenten comer mas items de los que se tienen
															   //En este caso, la mano se quedará vacía
				
				times = inventory.getItemInHand().getAmount();
				isempty = true;
				
			}
			
			increaseFoodLevel(times*inventory.getItemInHand().getType().getValue());//Asigna el nivel de comida
			
			try {
				inventory.getItemInHand().setAmount(inventory.getItemInHand().getAmount() - times);
			} catch (StackSizeException e) {
				// TODO Auto-generated catch block
				e.getMessage();
			}
				
			if(isempty)
				inventory.setItemInHand(null);
			
		}else if(inventory.getItemInHand().getType().isEdible() == false) {//inHand no es comida
			
			decreaseFoodLevel(times*0.1);
						
		}
		
		return inventory.getItemInHand();
		
	}
	
	/**
	 * Devuelve la orientación del jugador como una Location de forma absoluta.
	 * @return Location
	 */
	public Location getOrientation() {return new Location(orientation.getWorld(),orientation.getX()+location.getX(),orientation.getY()+location.getY(),orientation.getZ()+location.getZ());}
	
	
	
	/**
	 * Orienta al jugador a orientation(x,y,z) + (dx,dy,dz)
	 * @param dx int
	 * @param dy int
	 * @param dz int
	 * @return Location
	 * @throws EntityIsDeadException Si el jugador a muerto
	 * @throws BadLocationException Si la posicion pasada es 0,0,0 o no es adyacente.
	 */
	public Location orientate(int dx, int dy, int dz) throws EntityIsDeadException,BadLocationException{
		/*
		Location auxloc = new Location(null,location.getX(),location.getY(),location.getZ()); //CREAMOS AUXLOC PARA CALCULAR EL SET DE VECINOS PARA PODER ASIGNARLE UN WORLD NULL, Y QUE EL SET CONTENGA TODAS LA POS ADYACENTES A AUXLOC
		Location auxorientation = new Location(null,location.getX() + dx, location.getY() + dy, location.getZ() + dz); //AUXORIENTATION TIENE LA NUEVA ORIENTACION A CALCULAR
		
		Set<Location> vecinos = auxloc.getNeighborhood();//CALCULAMOS EL CONJUNTO DE POSICIONES QUE RODEAN A AUXLOC. ESTAS SERÁN TODAS LAS POS. ADYACENTES, ESTÉN O NO DENTRO DEL MUNDO, YA QUE CHECK() DE LOCATION DEVOLVERÁ SIEMPRE TRUE SI EL MUNDO ES TRUE
		
		*/
		
		
		if(isDead()) {
			
			throw new EntityIsDeadException();
			
		}else if((dx == 0 && dy == 0 && dz == 0) || /*!vecinos.contains(auxorientation)*/ !(dx >= -1 && dx <= 1) || !(dy >= -1 && dy <= 1) || !(dz >= -1 && dz <= 1) ) {
			
			throw new BadLocationException("La orientación introducida es errónea.");
			
		}
		
		
		orientation.setX(dx);
		orientation.setY(dy);
		orientation.setZ(dz);
		orientation.setWorld(this.getLocation().getWorld());
		
		return new Location(getOrientation());
	}
	
	/**
	 * Reduce el nivel de comida en d
	 * 
	 * @param d Cantidad a reducir el nivel de comida
	 */
	private void decreaseFoodLevel(double d){
		
		
		if(foodLevel - d <= 0.0) {
			
			setHealth((foodLevel - d) + getHealth());
			setFoodLevel(0.0);
			
			
			
		}else{
			
			foodLevel -= d;
			
		}
		
	}
	/**
	 * Aumenta el nivel de comida en d
	 * 
	 * @param d Cantidad a aumentar el nivel de comida
	 */
	private void increaseFoodLevel(double d) {
		
		if(d + foodLevel > MAX_FOODLEVEL) {
			
			setHealth(((foodLevel + d) - MAX_FOODLEVEL) + getHealth());
			setFoodLevel(MAX_FOODLEVEL);
			
		}else {
			
			setFoodLevel(foodLevel + d);
			
		}
		
		
	}
	
	
	/**
	 * Intercambia el objeto de la mano por
	 * el objeto que se encuentra en la 
	 * posición pos
	 * 
	 * @param pos Posición del objeto
	 * @throws BadInventoryPositionException Posición errónea
	 */
	public void selectItem(int pos) throws BadInventoryPositionException{
		
		if(inventory.getItem(pos) == null) { //LANZA BadInventoryPositionException
			
			throw new BadInventoryPositionException(pos);
			
		}else {
			
			//Caso en el que la mano está vacía
			if(inventory.getItemInHand() == null) {
				
				inventory.setItemInHand(inventory.getItem(pos));
				try {
					inventory.clear(pos);
				} catch (BadInventoryPositionException e) {
					// TODO Auto-generated catch block
					e.getMessage();
				}
			
			//Caso en el que la mano no está vacía
			}else {
				
				//Nos sirve como auxiliar para poder intercambiar los dos valores
				ItemStack itemaux = new ItemStack(inventory.getItemInHand());
				
				inventory.setItemInHand(inventory.getItem(pos));
				
				inventory.setItem(pos, itemaux);
				
			}
		
		}
		
	}
	
	/**
	 * Añade un item al inventario del jugador
	 * 
	 * @param items Item a añadir
	 */
	public void addItemsToInventory(ItemStack items) {
		
		inventory.addItem(items);
		
		
	}
	
	/**
	 * Tamaño del inventario del jugador
	 * 
	 * @return Inventory.getSize()
	 */
	public int getInventorySize() {return inventory.getSize();}
	
	/**
	 * Método toString
	 * @return String
	 */
	public String toString() {
		String c;
		
		
		
		c = "Name=" + name + "\n" + location.toString() + "\n" + "Orientation=" + orientation.toString() +"\n"+"Health=" + getHealth() + "\n" + "Food level=" + foodLevel + "\n";
		c = c + "Inventory=" + inventory.toString();
		
		return c;
	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(foodLevel);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((inventory == null) ? 0 : inventory.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((orientation == null) ? 0 : orientation.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (Double.doubleToLongBits(foodLevel) != Double.doubleToLongBits(other.foodLevel))
			return false;
		if (inventory == null) {
			if (other.inventory != null)
				return false;
		} else if (!inventory.equals(other.inventory))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (orientation == null) {
			if (other.orientation != null)
				return false;
		} else if (!orientation.equals(other.orientation))
			return false;
		return true;
	}

	
	
	
	
	
	
	
	
	
	
	
}
