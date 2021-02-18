/**
 * @author Luis Vidal Rico 45927898A
 */
package model;

import java.util.Random;

/**
 * Clase enum que define los diferentes materiales del mundo.
 * 
 */
public enum Material {
	
	/**
	 * BEDROCK
	 */
	BEDROCK(-1.0,'*'), 		//1
	/**
	 * CHEST
	 */
	CHEST(0.1,'C'),    		//2
	/**
	 * SAND
	 */
	SAND(0.5,'n'),			//3
	/**
	 * DIRT
	 */
	DIRT(0.5,'d'),			//4
	/**
	 * GRASS
	 */
	GRASS(0.6,'g'),			//5
	/**
	 * STONE
	 */
	STONE(1.5,'s'),			//6
	/**
	 * GRANITE
	 */
	GRANITE(1.5,'r'),		//7
	/**
	 * OBSIDIAN
	 */
	OBSIDIAN(5,'o'),		//8
	/**
	 * WATER_BUCKET
	 */
	WATER_BUCKET(1,'W'),	//9
	/**
	 * APPLE
	 */
	APPLE(4,'A'),			//10
	/**
	 * BREAD
	 */
	BREAD(5,'B'),			//11
	/**
	 * BEEF
	 */
	BEEF(8,'F'),			//12
	/**
	 * IRON_SHOVEL
	 */
	IRON_SHOVEL(0.2,'>'),	//13
	/**
	 * IRON_PICKAXE
	 */
	IRON_PICKAXE(0.5,'^'),	//14
	/**
	 * WOOD_SWORD
	 */
	WOOD_SWORD(1,'i'),		//15
	/**
	 * IRON_SWORD
	 */
	IRON_SWORD(2,'I'),		//16
	/**
	 * LAVA
	 */
	LAVA(1.0,'#'),			//17
	/**
	 * WATER
	 */
	WATER(0.0,'@'),
	/**
	 * AIR 					//18
	 */
	AIR(0.0,' ');
	
	/**
	 * Atributo para generar números aleatorios
	 */
	static Random rng = new Random(1L);
	
	/**
	 * Valor del material
	 */
	private double value;

	/**
	 * Simbolo que identifica el material
	 */
	private char symbol;
	
	/**
	 * Constructor que asigna un valor y un simbolo a cada material
	 * 
	 * @param value Valor del material
	 * @param symbol Symbolo del material
	 */
	Material(double value,char symbol){
		
		this.value = value;
		this.symbol = symbol;
		
	}
	
	/**
	 * Comprueba si es un bloque
	 * @return Devuelve true si sí, false si no
	 */
	public boolean isBlock() {
		
		boolean compr = false;
		
		if(symbol == ' ' || symbol == '*' || symbol == 'C' || symbol == 's' || symbol == 'd' || symbol == 'g' || symbol == 's' || symbol == 'r' || symbol == 'o' || symbol == '#' || symbol == '@' || symbol == 'n') {
			
			compr = true;
			
		}
		
		return compr;
		
	}
	
	/**
	 * Comprueba si es comida
	 * @return Devuelve true si sí, false si no
	 */
	public boolean isEdible() {
		
		if(symbol == 'W' || symbol == 'A' || symbol == 'B' || symbol == 'F') {
		
			return true;
		
		}else {
			
			return false;
			
		}
		
	}
	
	/**
	 * Comprueba si es un arma
	 * @return Devuelve true si sí, false si no
	 */
	public boolean isWeapon() {
		
		if(symbol == 'i' || symbol == 'I') {
			
			return true;
			
		}else {
	
			return false;
			
		}
		
	}
	
	/**
	 * Comprueba si es un arma
	 * @return Devuelve true si sí, false si no
	 */
	public boolean isTool() {
		
		if(symbol == '>' || symbol == '^') {
		
			return true;
		
		}else {
			
			return false;
			
		}
		
	}
	
	/**
	 * Comprueba si el bloque es liquido
	 * @return True si es líquido, false si no
	 */
	public boolean isLiquid() {
		
		if(symbol == '#' || symbol == '@' || symbol == ' ') {
			
			return true;
			
		}else {
			
			return false;
			
		}
		
		
		
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}
	
	/**
	 * Clase encargada de devolver un material aleatorio
	 * que esté entre los dos números pasados
	 * 
	 * @param first Primer número del intervalo
	 * @param last Último número del intervalo
	 * @return Un material aleatorio
	 */
	
	public static Material getRandomItem(int first, int last) {
		
		int i = rng.nextInt(last-first+1)+first;
        return values()[i];
	
	}

	
	
	
}
