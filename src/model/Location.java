/**
 * @author Luis Vidal Rico 45927898A
 */
package model;



import java.util.HashSet;
import java.util.Set;

import model.exceptions.BadLocationException;


/**
 * 
 * Clase encargada de definir las coordenadas donde se
 * encuentran los world, y además gestionar las diferentes
 * operaciones que se llevan a cabo entre estas.
 *
 */
public class Location implements Comparable<Location>{

	
	/**
	 * Posicion x
	 */
	private double x;
	/**
	 * Posicion y
	 */
	private double y;
	/**
	 * Posicion z
	 */
	private double z;
		
	/**
	 * Mundo de la posicion
	 */
	private World world;
	
	/**
	 * Tamaño maximo de la posicion y
	 */
	public static final double UPPER_Y_VALUE = 255.0;
	/**
	 * Tamaño del mar
	 */
	public static final double SEA_LEVEL = 63.0;
	
	//Setters
	/**
	 * 
	 * @param w encargada de asignarle un mundo a world
	 */
	public void setWorld(World w) {this.world = w;}
	/**
	 * 
	 * @param x Encargados de establecer el valor de x
	 */
	public void setX(double x) { this.x = x;}
	/**
	 * 
	 * @param y Encargados de establecer el valor de y
	 */
	public void setY(double y) { this.y = y;}
	/**
	 * 
	 * @param z Encargados de establecer el valor de z
	 */
	public void setZ(double z) { this.z = z;}
	
	
	//getters
	/**
	 * 
	 * @return x Encargado de devolver el valor de x
	 */
	public final double getX() {return x;}
	/**
	 * 
	 * @return y Encargado de devolver el valor de y
	 */
	public final double getY() {return y;}
	/**
	 * 
	 * @return z Encargado de devolver el valor de z 
	 */
	public final double getZ() {return z;}
		
	
	
	
	/**
	 * Constructor de la clase. Encargado de establecer el mundo 
	 * y sus 3 coordenadas
	 * @param w mundo world
	 * @param x posicion x de las coord
	 * @param y posicion y de las coord
	 * @param z posicion z de las coord
	 */
	public Location(World w, double x, double y, double z){
		world = w;
		
		setX(x);
		setY(y);
		setZ(z);
		
		
	}
	
	/**
	 * Constructor de copia. Encargado de copiar 
	 * el objeto que se le pasa
	 * @param loc pasa el objeto de location
	 */
	public Location(Location loc){
		world = loc.world;
		x = loc.x;
		y = loc.y;
		z = loc.z;
	}
	
	
	/**
	 * 
	 * @param loc pasa el objeto de location
	 * @return Devuelve -1.0 si no se puede calcular la distancia,
	 *		   y un núm. postivio si se ha podido calcular
	 */
	public final double distance(Location loc){
		
		if(loc.getWorld() == null || getWorld() == null) {
			
			System.err.println("Cannot measure distance to a null world");
			return -1.0;
			
		}else if(loc.getWorld() != getWorld()) {
			
			System.err.println("Cannot measure distance between " + world.getName() + " and " + loc.world.getName());
			return -1.0;
			
		}
		
		double dx = x - loc.x;
		double dy = y - loc.y;
		double dz = z - loc.z;
		
		return Math.sqrt(dx*dx + dy*dy + dz*dz);
		
		
	}
	
	
	
	/**
	 * Encargado de calcular la longitud de una posicion
	 * @return Devuelve la raíz cuadrada de la suma de los cuadrados de cada coord.
	 */
	public final double length() {return Math.sqrt(x*x + y*y + z*z);}
	
	
	/**
	 * Encargado de devolver los 3 valores a 0
	 * @return this devuelve la posicion del objeto location
	 */
	public Location zero() {
		x = y = z = 0.0;
		
		return this;
		
		
	}
	
	
	@Override
	/**
	 * @return el resultado de la operacion
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	/**
	 * Método encargado de ver si el objeto que invoca y el objeto que se pasa son iguales
	 * @return Devuelve true en caso de que obj sea igual que el objeto que lo invoca y false si no.
	 * @param obj se le pasa un objeto de la superclase objeto
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (world == null) {
			if (other.world != null)
				return false;
		} else if (!world.equals(other.world))
			return false;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	/**
	 * Encargado de restarle a las coordenadas las coordeanadas del objeto pasado
	 * @param loc le pasa un objeto location
	 * @return this
	 */
	public Location substract(Location loc) {
		
		if(loc.world != world){
			System.err.println("Cannot substract Locations of differing worlds.");
		}else{
			x -= loc.x;
			setY(y - loc.y);
			z -= loc.z;
		}
		
		return this;
	
	}
	
	/**
	 * Encargado de multiplicar las coordenadas del objeto por las del objeto pasado
	 * @param factor le pasa el valor a multiplicar
	 * @return this
	 */
	public Location multiply(double factor) {
		x *= factor;
		setY(y * factor);
		z *= factor;
		return this;
		
	}
	
	/**
	 * Devuelve el mundo de la clase
	 * @return world
	 */
	public final World getWorld() {return world;}
	
	/**
	 * Encargado de sumar a las coordenadas del objeto
	 * las coordenadas del objeto pasado 
	 * @param loc le pasa un objeto location
	 * @return this
	 */
	public Location add(Location loc) {
		
		if(loc.world != world) {
			
			System.err.println("Cannot add Locations of differing worlds.");
		
		}else {
			
			x += loc.x;
			setY(y + loc.y);
			z += loc.z;
			
		}
		return this;
		
	}
	
	/** Método encargado de imprimir por consola el nombre del mundo y las coordenadas de la posición.
	 *  @return s devuelve la string
	 */
	public String toString() {
	
		String s;
		s = "Location{world=";
		
		if(world == null) {
			
			s = s + "NULL";
			
		}else {
			
			s =  s + world;
			
		}
		
		s = s + ",x=" + x + ",y=" + y + ",z=" + z + "}";
		
		return s;
	
		
	}
	
	//Practica 2 hacia bajo
	
	/**
	 * 
	 * Se encarga de comprobar que los valores dados estan dentro de los límites del mundo dado.
	 * 
	 * @param w Mundo donde checkear
	 * @param x Posición x a comprobar
	 * @param y Posición y a comprobar
	 * @param z Posición z a comprobar
	 * @return Devolverá true si está dentro, false si no
	 */
	public static boolean check(World w, double x, double y, double z) {
		boolean comprobacion = true;
		
		double t_max_x = 0.0,t_max_y = 0.0,t_max_z = 0.0,t_min_x = 0.0,t_min_y = 0.0,t_min_z = 0.0;
		
		if(w == null)
			comprobacion =  true;
		
		//Calculamos todos los limites del mundo
		t_min_y = 0;
		t_max_y = UPPER_Y_VALUE;
		
		if(w != null) {
			t_max_x  = w.getSize()/2;
			t_max_z  = w.getSize()/2;	
			t_min_z = (w.getSize() % 2 == 0) ? -(t_max_z-1) : -t_max_z;
			t_min_x = (w.getSize() % 2 == 0) ? -(t_max_x-1) : -t_max_x;
		}
		
		if(w != null)
			//Comprobamos si las coordenadas pasadas están dentro del mundo
			if(x > t_max_x || x < t_min_x || y > t_max_y || y < t_min_y || z > t_max_z || z < t_min_z) {
				
				comprobacion = false;
				
			}
		
		return comprobacion;
	}
	
	/**
	 * Se encarga de comprobar que los valores dados estan dentro de los límites del mundo dado.
	 * 
	 * @param loc Se pasa una localizacion para comprobar sus coordenadas
	 * @return Devolverá true si está en los limites, false si no
	 */
	public static boolean check(Location loc) {
		boolean comprobacion = true;
		
		double t_max_x = 0.0,t_max_y = 0.0,t_max_z = 0.0,t_min_x = 0.0,t_min_y = 0.0,t_min_z = 0.0;
		
		
		//Calculamos todos los limites del mundo
		t_min_y = 0;
		t_max_y = UPPER_Y_VALUE;
		if(loc.getWorld() != null) {
			t_max_x  = loc.getWorld().getSize()/2;
			t_max_z  = loc.getWorld().getSize()/2;	
			t_min_z = (loc.getWorld().getSize() % 2 == 0) ? -(t_max_z-1) : -t_max_z;
			t_min_x = (loc.getWorld().getSize() % 2 == 0) ? -(t_max_x-1) : -t_max_x;
		}
		//System.out.println(t_max_x + "," + t_min_x);
		
		//Comprobamos si las coordenadas pasadas están dentro del mundo
		if(loc.getWorld() != null)
			if(loc.getX() > t_max_x || loc.getX() < t_min_x || loc.getY() > t_max_y || loc.getY() < t_min_y || loc.getZ() > t_max_z || loc.getZ() < t_min_z) {
				
				comprobacion = false;
				
			}
		
		if(loc.getWorld() == null)
			comprobacion = true;
		
		
		
		return comprobacion;
		
	}
	
	/**
	 * Se encarga de comprobar si una posición del mundo está libre o no
	 * 
	 * @return Devolverá true si la posición está libre, false si no
	 */
	public boolean isFree() {
		boolean compr = true;
		
		try {
			
			
			if(world == null || !getWorld().isFree(this)) {
				compr = false;
				
			}
			
		
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
		
		
		
		
		return compr;
	}
	
	/**
	 * Devuelve la posición que está justo debajo (y-1)
	 * @return location1, un nuevo objeto Location exactamente igual al anterior, pero con la coord. y cambiada
	 * @throws BadLocationException excepcion
	 */
	public Location below() throws BadLocationException{
		
		Location location1 = null;
		
		if(world != null && y == 0) {
			
			throw new BadLocationException("Debajo de la posición " + x + "," + y + "," + z + " no pueden haber bloques.");
		}else { //Lanza BadLocationException
			
			location1 = new Location(world,x,y-1,z);
			
		}
		
		return location1;
		
	}
	
	/**
	 * Devuelve la posición que está justo arriba(y+1)
	 * @return location1, un nuevo objeto Location exactamente igual al anterior, pero con la coord. y cambiada
	 * @throws BadLocationException error en la localización
	 */
	public Location above() throws BadLocationException{
		Location location1 = null;
		
		if(world != null && y == UPPER_Y_VALUE) {
		
			throw new BadLocationException("Arriba de la posición " + y + " no pueden haber bloques.");
		
		}else {
			
			
			location1 = new Location(world,x,y+1,z);
		}
		
		return location1;
		
	}
	
	/**
	 * Se encarga de calcular las posiciones adyacentes
	 * 
	 * @return Devuelve un conjunto con todas las posiciones adyacentes
	 */
	public Set<Location> getNeighborhood(){
		Set<Location> vecinos = new HashSet<Location>();
		
		double contador_pos_z = -1, posy = y - 1, contador_pos_x = -1;
		
		//Suponemos que en el mundo hay "3" capas, la posición y-1 respecto a y, la posición y, y la posicion y + 1. Las "y + 1" y "y - 1" estarán rodeadas por 9 posiciones, y la "y" por 8, ya que la original no se 
		//contará como vecino
		
		
		//----------------posiciones "y-1"----------------
		for(int i = 0; i < 3; i++) {
			
			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
			for(int y = 0; y < 3; y++) {
				
				if(check(world, x + contador_pos_x, posy, z + contador_pos_z)) {
					
					Location loc = new Location(world,x + contador_pos_x, posy, z + contador_pos_z);
					
					vecinos.add(loc);
					
				}
				
				contador_pos_z++;
				
			}
			
			contador_pos_z = -1;
			contador_pos_x++;
			
			
		}
		
		//----------------posiciones "y"----------------
		posy++;
		contador_pos_z = -1;
		contador_pos_x = -1;

		for(int i = 0; i < 3; i++) {
			
			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
			for(int y = 0; y < 3; y++) {
				
				
				
				//este if se evita calcular la coordenada actual
				if(!(contador_pos_z == 0 && contador_pos_x == 0)) {
					
					if(check(world, x + contador_pos_x, posy, z + contador_pos_z)) {
						
						Location loc = new Location(world,x + contador_pos_x, posy, z + contador_pos_z);
						
						vecinos.add(loc);
						
					}
				
				}
				
				contador_pos_z++;
				
			}
			
			contador_pos_z = -1;
			contador_pos_x++;
			
			
		}
		
		
		//----------------posiciones "y + 1"----------------
		posy++;
		contador_pos_z = -1;
		contador_pos_x = -1;
		
		for(int i = 0; i < 3; i++) {
			
			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
			for(int y = 0; y < 3; y++) {
				
				if(check(world, x + contador_pos_x, posy, z + contador_pos_z)) {
					
					Location loc = new Location(world,x + contador_pos_x, posy, z + contador_pos_z);
					
					vecinos.add(loc);
					
				}
				
				contador_pos_z++;
				
			}
			
			contador_pos_z = -1;
			contador_pos_x++;
			
			
		}
		
		return vecinos;
	}
	
	
	@Override
	public int compareTo(Location other) {
		int devolver = 0;
		
		if((getX() < other.getX()) || ((getX() == other.getX()) && getY() < other.getY()) || ( (getX() == other.getX()) && (getY() == other.getY()) ) && getZ() < other.getZ()) {
			 
			devolver = -1;
			
		}else if(getX() == other.getX() && getY() == other.getY() && getZ() == other.getZ()) {
			
			devolver = 0;
			
		}else {
			
			devolver = 1;
			
		}
		
		
		
		return devolver;
	}
	
	
}
