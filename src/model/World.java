/**
 * @author Luis Vidal Rico 45927898A
 */
package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.util.noise.CombinedNoiseGenerator;
import org.bukkit.util.noise.OctaveGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;

import model.entities.Animal;
import model.entities.Creature;
import model.entities.LivingEntity;
import model.entities.Monster;
import model.entities.Player;
import model.exceptions.BadLocationException;
import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;

/**
 * class World
 *
 */
public class World {


	/**
	 * name of the world
	 */
    private String name;
    
    /**
     * Size of the world in the (x,z) plane.
     */
    private int worldSize;
  
    /**
     * World seed for procedural world generation
     */
	private long seed;

    /**
     * bloques de este mundo
     */
    private Map<Location, Block> blocks;	
    
	/**
	 * Items depositados en algún lugar de este mundo.
	 */ 
    private Map<Location, ItemStack> items;

    /**
     * 
     */
    private Map<Location,Creature> creatures;
    
    /**
     * El jugador
     */
    private Player player;

    
	/** Esta clase interna representa un mapa de alturas bidimiensional
	 * que nos servirá para guardar la altura del terreno (coordenada 'y')
	 * en un array bidimensional, e indexarlo con valores 'x' y 'z' positivos o negativos.
	 * 
	 * la localización x=0,z=0 queda en el centro del mundo. 
	 * Por ejemplo, un mundo de tamaño 51 tiene su extremo noroeste a nivel del mar en la posición (-25,63,-25) 
	 * y su extremo sureste, también a nivel del mar, en la posición (25,63,25). 
	 * Para un mundo de tamaño 50, estos extremos serán (-24,63,-24) y (25,63,25), respectivamente.
	 * 
	 * Por ejemplo, para obtener la altura del terreno en estas posiciones, invocaríamos al método get() de esta  clase:
	 *   get(-24,24) y get(25,25)
	 * 
	 * de forma análoga, si queremos modificar el valor 'y' almacenado, haremos
	 *   set(-24,24,70)
	 *
	 */
	class HeightMap {
		/**
		 * Mapa que contiene las alturas del mundo
		 */
		double[][] heightMap;
		
		/**
		 * Límite positivo del mundo
		 */
    	int positiveWorldLimit; 
    	/**
    	 * Límite negativo del mundo
    	 */
    	int negativeWorldLimit;

    	/**
    	 * Constructor de HeightMap
    	 * @param worldsize Tamaño del mundo
    	 */
		HeightMap(int worldsize) {
			heightMap = new double[worldsize][worldsize];
			positiveWorldLimit  = worldsize/2;
			negativeWorldLimit = (worldsize % 2 == 0) ? -(positiveWorldLimit-1) : -positiveWorldLimit;
		}
		
		/**
		 * obtiene la atura del  terreno en la posición (x,z)
		 * @param x coordenada 'x' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @param z coordenada 'z' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @return Altura del mundo respecto a 'x' y a 'z'
		 */
		double get(double x, double z) {
			return heightMap[(int)x - negativeWorldLimit][(int)z - negativeWorldLimit];
		}
		/**
		 * Asigna la altura del terreno
		 * @param x coordenada 'x' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @param z coordenada 'z' entre 'positiveWorldLimit' y 'negativeWorldLimit'
		 * @param y coordenada y
		 */
		void set(double x, double z, double y) {
			heightMap[(int)x - negativeWorldLimit][(int)z - negativeWorldLimit] = y;
		}

	}
	
	
	/**
	 * Coordenadas 'y' de la superficie del mundo. Se inicializa en generate() y debe actualizarse
	 * cada vez que el jugador coloca un nuevo bloque en una posición vacía
	 * Puedes usarlo para localizar el bloque de la superficie de tu mundo.
	 */
	private HeightMap heightMap;
  
  
	//-------------------------------------------------------------
	// ImplementaciÃ³n de World.generate() y sus mÃ©todos auxiliares:
	// fillOblateSpheroid(), floodFill() y getFloodNeighborhood()
	//-------------------------------------------------------------

	    /**
	     * Genera un mundo nuevo del tamaÃ±o size*size en el plano (x,z). Si existÃ­an elementos anteriores en el mundo,  
	     * serÃ¡n eliminados. Usando la misma semilla y el mismo tamaÃ±o podemos generar mundos iguales
	     * @param seed semilla para el algoritmo de generaciÃ³n. 
	     * @param size tamaÃ±o del mundo para las dimensiones x y z
	     */
	    private  void generate(long seed, int size) {
	    	
	    	Random rng = new Random(getSeed());

	    	blocks.clear();
	    	creatures.clear();
	    	items.clear();
	    	
	    	// Paso 1: generar nuevo mapa de alturas del terreno
	    	heightMap = new HeightMap(size);
	    	CombinedNoiseGenerator noise1 = new CombinedNoiseGenerator(this);
	    	CombinedNoiseGenerator noise2 = new CombinedNoiseGenerator(this);
	    	OctaveGenerator noise3 = new PerlinOctaveGenerator(this, 6);
	    	
	    	System.out.println("Generando superficie del mundo...");
	    	for (int x=0; x<size; x++) {
	    		for (int z=0; z<size; z++) {
	    	    	double heightLow = noise1.noise(x*1.3, z*1.3) / 6.0 - 4.0;
	    	    	double heightHigh = noise2.noise(x*1.3, z*1.3) / 5.0 + 6.0;
	    	    	double heightResult = 0.0;
	    	    	if (noise3.noise(x, z, 0.5, 2) / 8.0 > 0.0)
	    	    		heightResult = heightLow;
	    	    	else
	    	    		heightResult = Math.max(heightHigh, heightLow);
	    	    	heightResult /= 2.0;
	    	    	if (heightResult < 0.0)
	    	    		heightResult = heightResult * 8.0 / 10.0;
	    	    	heightMap.heightMap[x][z] = Math.floor(heightResult + Location.SEA_LEVEL);
	    		}
	    	}
	    	
	    	// Paso 2: generar estratos
	    	SolidBlock block = null;
	    	Location location = null;
	    	Material material = null;
	    	OctaveGenerator noise = new PerlinOctaveGenerator(this, 8);
	    	System.out.println("Generando terreno...");
	    	for (int x=0; x<size; x++) {
	    		for (int z=0; z<size; z++) {
	    	    	double dirtThickness = noise.noise(x, z, 0.5, 2.0) / 24 - 4;
	    	    	double dirtTransition = heightMap.heightMap[x][z];
	    	    	double stoneTransition = dirtTransition + dirtThickness;
	    	    	for (int y=0; y<= dirtTransition; y++) {
	    	    		if (y==0) material = Material.BEDROCK;
	    	    		else if (y <= stoneTransition) 
	    	    			material = Material.STONE;
	    	    		else // if (y <= dirtTransition)
	    	    			material = Material.DIRT;
						try {
							location = new Location(this,x+heightMap.negativeWorldLimit,y,z+heightMap.negativeWorldLimit);
							block = new SolidBlock(material);
							if (rng.nextDouble() < 0.5) // los bloques contendrÃ¡n item con un 50% de probabilidad
								block.setDrops(block.getType(), 1);
							blocks.put(location, block);
						} catch (WrongMaterialException | StackSizeException e) {
							// Should never happen
							e.printStackTrace();
						}
	    	    	}

	    		}
	    	}
	    	
	    	// Paso 3: Crear cuevas
	    	int numCuevas = size * size * 256 / 8192;
			double theta = 0.0;
			double deltaTheta = 0.0;
			double phi = 0.0;
			double deltaPhi = 0.0;

			System.out.print("Generando cuevas");
	    	for (int cueva=0; cueva<numCuevas; cueva++) {
	    		System.out.print("."); System.out.flush();
	    		Location cavePos = new Location(this,rng.nextInt(size),rng.nextInt((int)Location.UPPER_Y_VALUE), rng.nextInt(size));
	    		double caveLength = rng.nextDouble() * rng.nextDouble() * 200;
	    		//cave direction is given by two angles and corresponding rate of change in those angles,
	    		//spherical coordinates perhaps?
	    		theta = rng.nextDouble() * Math.PI * 2;
	    		deltaTheta = 0.0;
	    		phi = rng.nextDouble() * Math.PI * 2;
	    		deltaPhi = 0.0;
	    		double caveRadius = rng.nextDouble() * rng.nextDouble();

	    		for (int i=1; i <= (int)caveLength ; i++) {
	    			cavePos.setX(cavePos.getX()+ Math.sin(theta)*Math.cos(phi));
	    			cavePos.setY(cavePos.getY()+ Math.cos(theta)*Math.cos(phi));
	    			cavePos.setZ(cavePos.getZ()+ Math.sin(phi));
	    			theta += deltaTheta*0.2;
	    			deltaTheta *= 0.9;
	    			deltaTheta += rng.nextDouble();
	    			deltaTheta -= rng.nextDouble();
	    			phi /= 2.0;
	    			phi += deltaPhi/4.0;
	    			deltaPhi *= 0.75;
	    			deltaPhi += rng.nextDouble();
	    			deltaPhi -= rng.nextDouble();
	    			if (rng.nextDouble() >= 0.25) {
	    				Location centerPos = new Location(cavePos);
	    				centerPos.setX(centerPos.getX() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				centerPos.setY(centerPos.getY() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				centerPos.setZ(centerPos.getZ() + (rng.nextDouble()*4.0-2.0)*0.2);
	    				double radius = (Location.UPPER_Y_VALUE - centerPos.getY()) / Location.UPPER_Y_VALUE;
	    				radius = 1.2 + (radius * 3.5 + 1) * caveRadius;
	    				radius *= Math.sin(i * Math.PI / caveLength);
	    				try {
	    					fillOblateSpheroid( centerPos, radius, null);
	    				} catch (WrongMaterialException e) {
	    					// Should not occur
	    					e.printStackTrace();
	    				}
	    			}

	    		}
	    	}
	    	System.out.println();
	    	
	    	// Paso 4: crear vetas de minerales
	    	// Abundancia de cada mineral
	    	double abundance[] = new double[2];
	    	abundance[0] = 0.5; // GRANITE
	    	abundance[1] =  0.3; // OBSIDIAN
	    	int numVeins[] = new int[2];
	    	numVeins[0] = (int) (size * size * 256 * abundance[0]) / 16384; // GRANITE
	    	numVeins[1] =  (int) (size * size * 256 * abundance[1]) / 16384; // OBSIDIAN

	    	Material vein = Material.GRANITE;
	    	for (int numVein=0 ; numVein<2 ; numVein++, vein = Material.OBSIDIAN) { 
	    		System.out.print("Generando vetas de "+vein);
	    		for (int v=0; v<numVeins[numVein]; v++) {
	    			System.out.print(vein.getSymbol());
	    			Location veinPos = new Location(this,rng.nextInt(size),rng.nextInt((int)Location.UPPER_Y_VALUE), rng.nextInt(size));
	    			double veinLength = rng.nextDouble() * rng.nextDouble() * 75 * abundance[numVein];
	    			//cave direction is given by two angles and corresponding rate of change in those angles,
	    			//spherical coordinates perhaps?
	    			theta = rng.nextDouble() * Math.PI * 2;
	    			deltaTheta = 0.0;
	    			phi = rng.nextDouble() * Math.PI * 2;
	    			deltaPhi = 0.0;
	    			//double caveRadius = rng.nextDouble() * rng.nextDouble();
	    			for (int len=0; len<(int)veinLength; len++) {
	    				veinPos.setX(veinPos.getX()+ Math.sin(theta)*Math.cos(phi));
	    				veinPos.setY(veinPos.getY()+ Math.cos(theta)*Math.cos(phi));
	    				veinPos.setZ(veinPos.getZ()+ Math.sin(phi));
	    				theta += deltaTheta*0.2;
	    				deltaTheta *= 0.9;
	    				deltaTheta += rng.nextDouble();
	    				deltaTheta -= rng.nextDouble();
	    				phi /= 2.0;
	    				phi += deltaPhi/4.0;
	    				deltaPhi *= 0.9; // 0.9 for veins
	    				deltaPhi += rng.nextDouble();
	    				deltaPhi -= rng.nextDouble();
	    				double radius = abundance[numVein] * Math.sin(len * Math.PI / veinLength) + 1;

	    				try {
	    					fillOblateSpheroid(veinPos, radius, vein);
	    				} catch (WrongMaterialException ex) {
	    					// should not ocuur
	    					ex.printStackTrace();
	    				}
	    			}
	    		}
	    		System.out.println();
	    	}
	    	
	    	System.out.println();

	    	// flood-fill water     	
	    	char water= Material.WATER.getSymbol();

	    	int numWaterSources = size*size/800;
	    	
	    	System.out.print("Creando fuentes de agua subterráneas");
	    	int x = 0;
	    	int z = 0;
	    	int y = 0;
	    	for (int w=0; w<numWaterSources; w++) {
	    		System.out.print(water);
	    		x = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		z = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		y = (int)Location.SEA_LEVEL - 1 - rng.nextInt(2);
	    		try {
					floodFill(Material.WATER, new Location(this,x,y,z));
				} catch (WrongMaterialException | BadLocationException e) {
					// no debe suceder
					throw new RuntimeException(e);
				}
	    	}
	    	System.out.println();
	   
	    	System.out.print("Creando erupciones de lava");
	    	char lava = Material.LAVA.getSymbol();
	    	// flood-fill lava
	    	int numLavaSources = size*size/2000;
	    	for (int w=0; w<numLavaSources; w++) {
	    		System.out.print(lava);
	    		x = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		z = rng.nextInt(size)+heightMap.negativeWorldLimit;
	    		y = (int)((Location.SEA_LEVEL - 3) * rng.nextDouble()* rng.nextDouble());
	    		try {
					floodFill(Material.LAVA, new Location(this,x,y,z));
				} catch (WrongMaterialException  | BadLocationException e) {
					// no debe suceder
					throw new RuntimeException(e);			
				}
	    	}
	    	System.out.println();

	    	// Paso 5. crear superficie, criaturas e items
	    	// Las entidades aparecen sÃ³lo en superficie (no en cuevas, por ejemplo)

	    	OctaveGenerator onoise1 = new PerlinOctaveGenerator(this, 8);
	    	OctaveGenerator onoise2 = new PerlinOctaveGenerator(this, 8);
	    	boolean sandChance = false;
	    	double entitySpawnChance = 0.05;
	    	double itemsSpawnChance = 0.10;
	    	double foodChance = 0.8;
	    	double toolChance = 0.1;
	    	double weaponChance = 0.1;
	    	
	    	System.out.println("Generando superficie del terreno, entidades e items...");
	    	for (x=0; x<size; x++) {    		
	    		for (z=0; z<size; z++) {
	    			sandChance = onoise1.noise(x, z, 0.5, 2.0) > 8.0;
	    			y = (int)heightMap.heightMap[(int)x][(int)z];
	    			Location surface = new Location(this,x+heightMap.negativeWorldLimit,y,z+heightMap.negativeWorldLimit); // la posiciÃ³n (x,y+1,z) no estÃ¡ ocupada (es AIR)
	    			try {
		    			if (sandChance) {
		    				SolidBlock sand = new SolidBlock(Material.SAND);
		    				if (rng.nextDouble() < 0.5)
		    					sand.setDrops(Material.SAND, 1);
		    				blocks.put(surface, sand);
		    			}
		    			else {
		    				SolidBlock grass = new SolidBlock(Material.GRASS);
		    				if (rng.nextDouble() < 0.5)
		    					grass.setDrops(Material.GRASS, 1);
		    				blocks.put(surface, grass);
		    			}
	    			} catch (WrongMaterialException | StackSizeException ex) {
	    				// will never happen
	    				ex.printStackTrace();
	    			}
	    			// intenta crear una entidad en superficie
	    			try {
	    				Location aboveSurface = surface.above();
	    				
	    				if (rng.nextDouble() < entitySpawnChance) {
	    					Creature entity =null;
	    					double entityHealth = rng.nextInt((int)LivingEntity.MAX_HEALTH)+1;
	    					if (rng.nextDouble() < 0.75) // generamos Monster (75%) o Animal (25%) de las veces
	    						entity = new Monster(aboveSurface, entityHealth);
	    					else 
	    						entity = new Animal(aboveSurface, entityHealth);
	    					creatures.put(aboveSurface, entity);
	    				} else { 
	    					// si no, intentamos crear unos items de varios tipos (comida, armas, herramientas)
	    					// dentro de cofres
	    					Material itemMaterial = null;
	    					int amount = 1; // p. def. para herramientas y armas
	    					if (rng.nextDouble() < itemsSpawnChance) {
	    						double rand = rng.nextDouble();
	    						if (rand < foodChance) { // crear comida
	    							// hay cuatro tipos de item de comida, en las posiciones 8 a 11 del array 'materiales'
	    							itemMaterial = Material.getRandomItem(8, 11);
	    							amount = rng.nextInt(5)+1;
	    						}
	    						else if (rand < foodChance+toolChance)
	    							// hay dos tipos de item herramienta, en las posiciones 12 a 13 del array 'materiales'
	    							itemMaterial = Material.getRandomItem(12, 13);
	    						else
	    							// hay dos tipos de item arma, en las posiciones 14 a 15 del array 'materiales'
	    							itemMaterial = Material.getRandomItem(14, 15);
	    						
	    						items.put(aboveSurface, new ItemStack(itemMaterial, amount));
	    					}
	    				}
	    			} catch (BadLocationException | StackSizeException e) {
	    				// BadLocationException : no hay posiciones mÃ¡s arriba, ignoramos creaciÃ³n de entidad/item sin hacer nada 
	    				// StackSizeException : no se producirÃ¡
	    				throw new RuntimeException(e);    			}

	    		}
	    	}

	    	// TODO: Crear plantas
	    	    	
	    	// Generar jugador
	    	player = new Player("Steve",this);
	    	// El jugador se crea en la superficie (posiciÃ³n (0,*,0)). AsegurÃ©monos de que no hay nada mÃ¡s ahÃ­
	    	Location playerLocation = player.getLocation();
	    	creatures.remove(playerLocation);
	    	items.remove(playerLocation);
	    	
	    }
	    
	    /**
	     * Where fillOblateSpheroid() is a method which takes a central point, a radius and a material to fill to use on the block array.
	     * @param centerPos central point
	     * @param radius radius around central point
	     * @param material material to fill with
	     * @throws WrongMaterialException if 'material' is not a block material
	     */
	    private void fillOblateSpheroid(Location centerPos, double radius, Material material) throws WrongMaterialException {
	    	
					for (double x=centerPos.getX() - radius; x< centerPos.getX() + radius; x += 1.0) {					
						for (double y=centerPos.getY() - radius; y< centerPos.getY() + radius; y += 1.0) {
							for (double z=centerPos.getZ() - radius; z< centerPos.getZ() + radius; z += 1.0) {
								double dx = x - centerPos.getX();
								double dy = y - centerPos.getY();
								double dz = z - centerPos.getZ();
								
								if ((dx*dx + 2*dy*dy + dz*dz) < radius*radius) {
									// point (x,y,z) falls within level bounds ?
									// we don't need to check it, just remove or replace that location from the blocks map.
									Location loc = new Location(this,Math.floor(x+heightMap.negativeWorldLimit),Math.floor(y),Math.floor(z+heightMap.negativeWorldLimit));
									if (material==null)
										blocks.remove(loc);
									else try { //if ((Math.abs(x) < worldSize/2.0-1.0) && (Math.abs(z) < worldSize/2.0-1.0) && y>0.0 && y<=Location.UPPER_Y_VALUE)
										SolidBlock veinBlock = new SolidBlock(material);
										// los bloques de veta siempre contienen material
										veinBlock.setDrops(material, 1);
										blocks.replace(loc, veinBlock);
									} catch  (StackSizeException ex) {
										// will never happen
										ex.printStackTrace();
									}
								}
							}
						}
					}
		}
	    
	    /**
	     * Funcion hecha por los profesores, que no añadieron comentarios :_(
	     * @param liquid Material
	     * @param from Location
	     * @throws WrongMaterialException WrongMaterialException 
	     * @throws BadLocationException BadLocationException
	     */
	    private void floodFill(Material liquid, Location from) throws WrongMaterialException, BadLocationException {
	    	if (!liquid.isLiquid())
	    		throw new WrongMaterialException(liquid);
	    	if (!blocks.containsKey(from))
	    	{
	    		blocks.put(from, BlockFactory.createBlock(liquid));
	    		items.remove(from);
	    		Set<Location> floodArea = getFloodNeighborhood(from);
	    		for (Location loc : floodArea) 
	    			floodFill(liquid, loc);
	    	}
	    }
	    
		/**
		 * Obtiene las posiciones adyacentes a esta que no estÃ¡n por encima y estÃ¡n libres 
		 * @param location Location
		 * @return si esta posiciÃ³n pertenece a un mundo, devuelve sÃ³lo aquellas posiciones adyacentes vÃ¡lidas para ese mundo,  si no, devuelve todas las posiciones adyacentes
		 * @throws BadLocationException cuando la posiciÃ³n es de otro mundo
		 */
		private Set<Location> getFloodNeighborhood(Location location) throws BadLocationException {
			if (location.getWorld() !=null && location.getWorld() != this)
				throw new BadLocationException("Esta posiciÃ³n no es de este mundo");
			Set<Location> neighborhood = location.getNeighborhood();
			Iterator<Location> iter = neighborhood.iterator();
			while (iter.hasNext()) {
				Location loc = iter.next();
				try {
					if ((loc.getY() > location.getY()) || getBlockAt(loc)!=null)
						iter.remove();
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
					// no sucederÃ¡
				}
			}
			return neighborhood;
		}
    


    
    /**
     * Getter nombre
     * @return name
     */
    public String getName() {return name;}
    
    /**
     * Getter worldSize
     * @return worldSize
     */
    public int getSize() {return worldSize;}
    
    /**
     * Getter seed
     * @return seed
     */
    public long getSeed() {return seed;}
    
    /**
     * Getter player
     * @return player
     */
    public Player getPlayer() {return player;}
    
    
    /**
     * Crea un nuevo mundo a partir de la semilla,
     * y de size. El tamaño del nuevo mundo será
     * size*size en (x,z).Name es el nombre del mundo.
     * 
     * @param seed Semilla del mundo.
     * @param size Tamaño del mundo.
     * @param name Nombre del mundo
     * @param PlayerName String
     * @throws IllegalArgumentException Size es menor o igual a 0
     */
    public World(long seed, int size,String name, String PlayerName) throws IllegalArgumentException{
    	
    	
    	
    	if(size <= 0) {//Lanza la excepción IllegalArgumentException
    		
    		throw new IllegalArgumentException();
    	
    	}else{
    		this.seed = seed;
        	worldSize = size;
        	this.name = name;
        	items = new HashMap<Location,ItemStack>();
        	blocks = new HashMap<Location,Block>();
    		creatures = new HashMap<Location,Creature>();
    		generate(seed,worldSize);
    		player = new Player(PlayerName, this);
    		
    		
    		
    	}
    	
    	
    }
    
    /**
     * @deprecated
     * @param name sdasd
     */
    public World(String name) {
    	
    	this.name = name;
    	
    }
    
    /**
     * Obtiene el bloque que hay en la posición indicada, o null si no hay ningún bloque
     * @param loc Localización donde se encuentra el bloque
     * @return Devuelve el bloque o null
     * @throws BadLocationException Excepción
     */
    public Block getBlockAt(Location loc) throws BadLocationException {
    	
    	Block block = null;
    	
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this)) { //Lanza BadLocationException
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}else {
    		if(blocks.get(loc) != null)
				block = blocks.get(loc).clone();
    	}
    	
    	return block;
    	
    	
    }
    
    /**
     * Devuelve la posición más alta donde hay un bloque a partir de las coordenadas 'x' y 'z'
     * @param ground Localización a encontrar el bloque
     * @return Localización con la altura del primer bloque
     * @throws BadLocationException localización sin mundo o diferente al mundo
     */
    public Location getHighestLocationAt(Location ground) throws BadLocationException{
  
    	Location loc = new Location(ground);
    	
    	if(ground.getWorld() == null || !ground.getWorld().equals(this)) { //Lanza BadLocationException
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}else {
    		
    		
    		loc.setY(heightMap.get(ground.getX(), ground.getZ()));
    	
    	}
    	
    	
    	return loc;	
    		
    		
    }
    
    /**
     * Obtiene el item que se encuentra en la posición indicada, o null si no hay
     * @param loc Localización a buscar el item
     * @return ItemStack item, o null si no hay item
     * @throws BadLocationException localización sin mundo o diferente al mundo
     */
    public ItemStack getItemsAt(Location loc) throws BadLocationException{
    	ItemStack item = null;
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this)) { //Lanza BadLocationException
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}else {
    		
			
			item = items.get(loc);
				
    		
    		
    	}
    	
    	return item;
    	
    }
    
    /**
     * Devuelve la criatura indicada en loc, o null si no 
     * hay ninguna criatura o la posición no existe en 
     * este mundo.
     * @param loc Location
     * @return Creature
     * @throws BadLocationException Si la posición no es de este mundo.
     */
    public Creature getCreatureAt(Location loc) throws BadLocationException{
    	
    	//Comprueba que la posición sea de este mundo
    	if(loc.getWorld() == null ||  !loc.getWorld().equals(this)) {
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}
    	
    	//Devuelve null si la posición no existe en este mundo
    	if(!Location.check(loc))
    		return null;
    	
    	//Devuelve la criatura
    	return creatures.get(loc);
    	
    }
    
    /**
     * Elimina la criatura de la posición dada.
     * @param loc Location
     * @throws BadLocationException Si la posicíon no pertenece a este mundo o no hay una criatura en esa posición.
     */
    public void killCreature(Location loc) throws BadLocationException{
    	
    	if(loc == null || loc.getWorld() == null ||!loc.getWorld().equals(this) || getCreatureAt(loc) == null) {
    		
    		throw new BadLocationException("La posicíon no pertenece a este mundo o no hay una criatura en esa posición.");
    		
    	}else {
    		
    		creatures.remove(loc);
    		
    	}
    	
    }
    
    /**
     * Devuelve un set con las criaturas que hay alrededor de una posición
     * @param loc Location
     * @return Collection<Creature>
     * @throws BadLocationException Si la posición no pertenece a este mundo
     */
    public Collection<Creature> getNearbyCreatures(Location loc) throws BadLocationException{
    	Collection<Creature> criaturas = new ArrayList<Creature>();
    	
    	double contador_pos_z = -1, posy = loc.getY() - 1, contador_pos_x = -1;
		
		//Suponemos que en el mundo hay "3" capas, la posición y-1 respecto a y, la posición y, y la posicion y + 1. Las "y + 1" y "y - 1" estarán rodeadas por 9 posiciones, y la "y" por 8, ya que la original no se 
		//contará como vecino
		
    	if(loc.getWorld() == null || !loc.getWorld().equals(this))
    		throw new BadLocationException("La posición no es de este mundo.");
    	
    	//----------------posiciones "y-1"----------------
		for(int i = 0; i < 3; i++) {
			
			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
			for(int y = 0; y < 3; y++) {
				
				Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
				
				if(Location.check(locaux)) { // Comprobamos que locaux está dentro de los límites del mundo.
					
					if(getCreatureAt(locaux) != null)
						criaturas.add(getCreatureAt(locaux));
					
					
					
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
				
				Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
				
				if(Location.check(locaux)) { // Comprobamos que locaux está dentro de los límites del mundo.
					
					if(getCreatureAt(locaux) != null)
						criaturas.add(getCreatureAt(locaux));
					
					
					
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
				Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
				
				if(Location.check(locaux)) { // Comprobamos que locaux está dentro de los límites del mundo.
					
					if(getCreatureAt(locaux) != null)
						criaturas.add(getCreatureAt(locaux));
					
					
					
				}
				
				
				contador_pos_z++;
				
			}
			
			contador_pos_z = -1;
			contador_pos_x++;
			
			
		}
		
		return criaturas;
    	
    }
    
    
    /**
     * Devuelve la cadena que imprime las posiciones adyacentes a la posición pasada.
     * @param loc Posición a representar
     * @return String con las posiciones
     * @throws BadLocationException localización sin mundo o diferente al mundo
     */
    public String getNeighbourhoodString(Location loc) throws BadLocationException {	 //LAS POSICIONES NO SE RECORREN EN EL ORDEN ADECUADO
    	
    	double contador_pos_z = -1, posy = loc.getY() + 1, contador_pos_x = -1;
    	
    	String s = "";
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this)){ //Lanza BadLocationException 
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    	}else{
    		
    		
    		//Suponemos que en el mundo hay "3" capas, la posición y-1 respecto a y, la posición y, y la posicion y + 1. Las "y + 1" y "y - 1" estarán rodeadas por 9 posiciones, y la "y" por 8, ya que la original no se 
    		//contará como vecino
    		
    		//Primera línea de 9 posiciones
    		
    		for(int i = 0; i < 3; i++) {//BUCLE I
    			
    			/**
    			 * contador_pos_x valdrá -1, 0 o 1 en cada una de las iteraciones del bucle y, mientras que contador_pos_z valdrá siempre
    			 * -1 en todas las posiciones de los dos bucles for. Y valdrá 1, 0 y -1 en cada una de las iteraciones del bucle i
    			 */
    			for(int y = 0; y < 3; y++) {//BUCLE Y
    				/*
    				 * Chequea si la posicion actual está dentro del mundo.
    				 * Si sí, añade el símbolo correspondiente. Si no, añade X.
    				 */
    				if(Location.check(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z)) {
    					
    					Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
					
    					if(getItemsAt(locaux) != null) { //Caso en el que la posicion contiene un item				    						
    						
    						if(getItemsAt(locaux).getType().isBlock()) { //Caso en el que el item es un drop de un bloque, pasamos el char a mayus
    						
    							s = s + Character.toUpperCase(getItemsAt(locaux).getType().getSymbol());
    						
    						}else { //Caso en el que el item no es un drop de un bloque
    							
    							s = s + getItemsAt(locaux).getType().getSymbol();
    							
    						}			   			
    							
    					}else if(locaux.equals(getPlayer().getLocation())){ //Caso en el que la posición contiene un jugador    					    					
    						
    						s = s + "P";
    						   						
    					}else if(getCreatureAt(locaux) != null){
    						
    						s = s + getCreatureAt(locaux).getSymbol();
    						
    					}else if(getBlockAt(locaux) != null) { //Caso en el que la posición contiene un bloque
    					
    						s = s + getBlockAt(locaux).getType().getSymbol();
    					
    					}else{

    						s = s + ".";
    						
    					}
    					
    				}else{
    					
    					s = s + "X";
    					
    				}
    					
    				contador_pos_x++;
    				
    			}
    			if(i != 2)
    				s = s + " ";
    			
    			contador_pos_x = -1;
    			posy--;
    			
    		}
    		
    		s = s + "\n";
    		
    		//Segunda línea de 9  posiciones 
    		posy = loc.getY() + 1; //Restablecemos y a loc.y + 1
    		contador_pos_z = 0;			//La posición z será siempre z en estas 9 posiciones
    		contador_pos_x = -1;		//x será x-1,x,x+1 otra vez 

    		for(int i = 0; i < 3; i++) {
    			
    			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
    			for(int y = 0; y < 3; y++) {
    				
    				
    				
	    			if(Location.check(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z)) {
	    					
	    					
	    				Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
	    					
	    					
	    				if(getItemsAt(locaux) != null) { //Caso en el que la posicion contiene un item				    						
    						
    						if(getItemsAt(locaux).getType().isBlock()) { //Caso en el que el item es un drop de un bloque, pasamos el char a mayus
    						
    							s = s + Character.toUpperCase(getItemsAt(locaux).getType().getSymbol());
    						
    						}else { //Caso en el que el item no es un drop de un bloque
    							
    							s = s + getItemsAt(locaux).getType().getSymbol();
    							
    						}			   			
    							
    					}else if(locaux.equals(getPlayer().getLocation())){ //Caso en el que la posición contiene un jugador    					    					
    						
    						s = s + "P";
    						   						
    					}else if(getCreatureAt(locaux) != null){
    						
    						s = s + getCreatureAt(locaux).getSymbol();
    						
    					}else if(getBlockAt(locaux) != null) { //Caso en el que la posición contiene un bloque
    					
    						s = s + getBlockAt(locaux).getType().getSymbol();
    					
    					}else{

    						s = s + ".";
    						
    					}
	    					
	    					
	    			}else {
	    					
	    				s = s + "X";
	    					
	    			}
    			
	    			contador_pos_x++;
	    			
    			}
    					
    			if(i != 2)	
    				s = s + " ";	
    			
    			contador_pos_x = -1;
    			posy--;
    		}
    			
    		
    		
    		contador_pos_z = 0;
    		contador_pos_x = -1;
    			
    			
    	
    		
    		s = s + '\n';
    		
    		
    		//Tercera línea de 9  posiciones 
    		posy = (int)loc.getY() + 1; //Restablecemos y a loc.y + 1
    		contador_pos_z = 1;			//La posición z será siempre z + 1 en estas 9 posiciones
    		contador_pos_x = -1;		//x será x-1,x,x+1 otra vez 
    		
    		for(int i = 0; i < 3; i++) {
    			
    			//contador_pos_z valdrá -1,0,1 mientras contador_pos_x siempre valdrá -1, 0 o 1 en las 3 iteraciones
    			for(int y = 0; y < 3; y++) {
    				if(Location.check(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z)) {
    					
    					
    					Location locaux = new Location(this,loc.getX() + contador_pos_x, posy, loc.getZ() + contador_pos_z);
    					
    					
	    				if(getItemsAt(locaux) != null) { //Caso en el que la posicion contiene un item				    						
    						
    						if(getItemsAt(locaux).getType().isBlock()) { //Caso en el que el item es un drop de un bloque, pasamos el char a mayus
    						
    							s = s + Character.toUpperCase(getItemsAt(locaux).getType().getSymbol());
    						
    						}else { //Caso en el que el item no es un drop de un bloque
    							
    							s = s + getItemsAt(locaux).getType().getSymbol();
    							
    						}			   			
    							
    					}else if(locaux.equals(getPlayer().getLocation())){ //Caso en el que la posición contiene un jugador    					    					
    						
    						s = s + "P";
    						   						
    					}else if(getCreatureAt(locaux) != null){
    						
    						s = s + getCreatureAt(locaux).getSymbol();
    						
    					}else if(getBlockAt(locaux) != null) { //Caso en el que la posición contiene un bloque
    					
    						s = s + getBlockAt(locaux).getType().getSymbol();
    					
    					}else{

    						s = s + ".";
    						
    					}
    					
    				}else {
    					
    					s = s + "X";
    					
    				}
    				
    				contador_pos_x++;
    				
    			}
    			
    			if(i != 2)
    				s = s + " ";
    			
    			contador_pos_z = 1;
    			contador_pos_x = -1;
    			posy--;
    			
    		}
    		
    	
    	
    	
    	}
    	
    	return s;
	    		
	    		
	    		
	    	
    	
    	
    }
    
    
    /**
     * Comprueba si una posición no está ocupada ni por un bloque sólido ni por un jugador ni por una criatura
     * 
     * @param loc Localización a comprobar
     * @return True si está libre, false si no
     * @throws BadLocationException localización sin mundo o diferente al mundo
     */
    public boolean isFree(Location loc) throws BadLocationException{
    	boolean is = true;
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this)) { //Lanza BadLocationException
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}else {
    	
	    	try {
				if(!loc.equals(getPlayer().getLocation()) && getCreatureAt(loc) == null){	 //No hay ni jugador ni criaturas
					
					if(getBlockAt(loc) == null) //No hay ningún bloque
						is = true;
					else {
						
						if(getBlockAt(loc).getType().isLiquid()) //Hay bloque, pero es liquido
							is = true;
						else //Hay bloque, pero es solido
							is = false;
						
					}
						
					
				}else { //Hay o jugador o criatura
					
					is = false;
					
				}
			} catch (BadLocationException e) {

				e.getMessage();

			}
	    	
    	}
    
    	return is;
    
    }
    
    /**
     * Añade un bloque en la posición dada. Si antes
     * había un bloque, una criatura, o un item, los elimina.
     * @param loc Location 
     * @param b Block
     * @throws BadLocationException Si la posición no es de este mundo, está fuera de sus límites o está ocupada por el jugador
     */
    public void addBlock(Location loc, Block b) throws BadLocationException{	
    	
    	Location locaux = null;
    	if(Location.check(loc))
    		locaux = new Location(getHighestLocationAt(loc)); //Locaux contiene la misma posicion que loc, pero con la coord. 'y' del bloque más alto de loc.x,loc.z
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this) || loc.equals(getPlayer().getLocation()) || !Location.check(loc)){
    	
    		throw new BadLocationException("La posición no es de este mundo, está fuera de sus límites o está ocupada por el jugador.");
    		
    	}else {
    		
    		if(getBlockAt(loc) != null) { //Eliminamos el bloque, si lo hay
    			
    			destroyBlockAt(loc);
    			
    			
    		}else if(getItemsAt(loc) != null) { //Eliminamos el item, si lo hay
    			
    			removeItemsAt(loc);
    			
    		}else if(getCreatureAt(loc) != null) { //Eliminamos la criatura, si la hay
    			
    			killCreature(loc);
    			
    		}
    		
    		//Si la posición donde vamos a poner el bloque es más alta que la posición donde antes estaba el bloque más alto, actualizamos HeightMap
	   
    		if(loc.getY() >= locaux.getY()) { 
    			heightMap.set(loc.getX(), loc.getZ(),loc.getY());
	    	}
    		
    		blocks.put(loc, b);
    		
    	}
    	
    }
    
    /**
     * Añade una criatura a este mundo, si la posición de la criatura está libre.
     * Si habían items, los elimina
     * @param c Creature
     * @throws BadLocationException Si la posición de la criatura no es de este mundo, está fuera de sus límites o isFree() devuelve false
     */
    public void addCreature(Creature c) throws BadLocationException{
    	if(c.getLocation().getWorld() == null || !c.getLocation().getWorld().equals(this) || !isFree(c.getLocation()) || !Location.check(c.getLocation())){
        	
    		throw new BadLocationException("La posición no es de este mundo, está fuera de sus límites o está ocupada.");
    		
    	}else {
    		
    		//Eliminamos el item de la posición donde vamos a crear la criatura, en caso de que la haya.
    		if(getItemsAt(c.getLocation()) != null) 
    			removeItemsAt(c.getLocation());
    		
    		creatures.put(c.getLocation(),c);
    		
    	}
    	
    }
    
    /**
     * Añade una pila de items a este mundo, en la posición dada, que debe estar libre. 
     * Si había otros items en esa posición, son reemplazados
     * @param loc Location
     * @param i ItemStack
     * @throws BadLocationException Si la posición no es de este mundo, está fuera de sus límites o está ocupada
     */
    public void addItems(Location loc, ItemStack i) throws BadLocationException{
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this) || !isFree(loc) || !Location.check(loc)){
        	
    		throw new BadLocationException("La posición no es de este mundo, está fuera de sus límites o está ocupada.");
    		
    	}else {
    		//Eliminamos el item de la posición donde vamos a añadir el item, en caso de que haya.
    		if(getItemsAt(loc) != null) 
    			removeItemsAt(loc);
    		
    		items.put(loc,i);
    		
    		
    	}
    	
    }
    
    /**
     * Destrouye el bloque de la posición dada, eliminándolo del mundo.
     * Además, coloca los items del bloque en la misma loc.
     * @param loc Location
     * @throws BadLocationException si la posicion no pertenece a este mundo, la posicion tiene y == 0, o no hay bloque a destruir
     */
    public void destroyBlockAt(Location loc) throws BadLocationException{
    	SolidBlock b = null;
    
    	
    	if(loc.getWorld() == null || !loc.getWorld().equals(this) || getBlockAt(loc) == null || loc.getY() == 0){
        	
    		throw new BadLocationException("La posición no es de este mundo, no hay bloques en esa posición o es un bloque de altura cero.");
    		
    	}else {
    		
    		//Si la localización del bloque a eliminar coincide con el bloque más alto, modificamos heightMap
	    	
    		//Si eliminamos un bloque, por ejemplo, en y = 61, pero no vuelven a haber bloques hasta, por ejemplo, y = 55, no podemos poner el heightMap a 60, si no a 55
    		Location locaux = new Location(this,loc.getX(), loc.getY()-1,loc.getZ());
    		
    		if(loc.equals(getHighestLocationAt(loc))) {//Si el bloque que eliminamos coincide con el bloque más alto
	    		if(getBlockAt(locaux) == null){	//Caso en el que el bloque que está bajo al que destruimos no existe, hay que bajar hasta encontrar uno
	    			
	    			while(getBlockAt(locaux) == null) {
	    				
	    				locaux.setY(locaux.getY() - 1);	//Bajamos hasta encontrar un bloque
	    				
	    			}
	    		
	    			heightMap.set(locaux.getX(), locaux.getZ(), locaux.getY());
	    			
	    		}else{	//Caso en el que el bloque que está bajo el bloque que destruimos si existe
	    			
	    			
	    				heightMap.set(loc.getX(), loc.getZ(),loc.getY() - 1);
	    		
	    		}
    		}
    		
    		//Si el bloque es de tipo SolidBlock, hacemos downcasting a SolidBlock
    		if(blocks.get(loc) instanceof SolidBlock) {
    			
    			b = (SolidBlock)blocks.get(loc);
    			
    		}
    		
    		blocks.remove(loc);
    		
    		//Si b sigue siendo null, es porque el downcasting no ha funcionado, lo que indicará que el bloque es líquido, y no tiene drops. Si no, añadimos los drops al mundo-
    		if(b != null) {
    			
    			addItems(loc, b.getDrops());
    			
    		}
    		
    	}
    	
    	
    }
    
    
    
    /**
     * Elimina los items que se encuentran en la posición indicada
     * Si no hay ningun item lanza BadLocationException
     * @param loc Posición donde está el item a eliminar
     * @throws BadLocationException localización sin mundo o diferente al mundo
     */
    public void removeItemsAt(Location loc) throws BadLocationException{
    	
    	
    	if(items.get(loc) == null || !loc.getWorld().equals(this)) {//Lanza BadLocationException
    		
    		throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
    		
    	}else {
    		
    		items.remove(loc);
    		
    	}
    	
    
    	
    	
    }
    
    /**
     * Método toString
     * @return String
     */
    public String toString() {return name;}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (int) (seed ^ (seed >>> 32));
		result = prime * result + worldSize;
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
		World other = (World) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (seed != other.seed)
			return false;
		if (worldSize != other.worldSize)
			return false;
		return true;
	}
    	
    




}
