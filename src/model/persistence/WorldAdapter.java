package model.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import model.Block;
import model.BlockFactory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.World;
import model.entities.Creature;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public class WorldAdapter implements IWorld {

	/**
	 * Player
	 */
	private IPlayer player;
	
	/**
	 * World
	 */
	private World world;
	
	/**
	 * Constructor sobrecargado a partir de un objeto World 
	 * que queremos adaptar a la interfaz IWorld. Construye 
	 * un PlayerAdapter a partir del jugador del mundo ‘w’.
	 * @param w World
	 */
	public WorldAdapter(World w){
		
		world = w;
		player = new PlayerAdapter(w.getPlayer());
		
	}
	
	
	@Override
	public double getHealth() {
	
		return player.getHealth();
		
	}

	@Override
	public IInventory getInventory() {
		
		return player.getInventory();
		
	}

	@Override
	public Location getLocation() {
		
		return player.getLocation();
	
	}

	@Override
	public String getName() {
		
		return player.getName();
		
	}

	@Override
	public ItemStack getItem(int pos) {
		
		return player.getItem(pos);
		
	}

	@Override
	public int getSize() {
		
		return player.getSize();
		
	}

	@Override
	public ItemStack inHandItem() {
		
		return player.inHandItem();
		
	}

	@Override
	public NavigableMap<Location, Block> getMapBlock(Location loc) {
		NavigableMap<Location,Block> n = new TreeMap<>();
		
		
		Location l1;
		try {
			
			/**
			 * Este for recorre todas las posiciones posibles de la x.
			 */
			for(int x = 0; x < 16;x++) {
				
				/**
				 * Este for recorre todas las posiciones posibles de la y.
				 */
				for(int y = 0; y < 16; y++) {
					
					/**
					 * Este for recorre todas las posiciones posibles de la z.
					 */
					for(int z = 0; z < 16; z++) {
						
						/**
						 * Location absoluta
						 */
						l1 = new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z);
						
						if(!Location.check(l1)) //Caso en el que la posición está fuera del mapa.
							n.put(new Location(world,x,y,z),null); 
						else if(Location.check(l1) && world.getBlockAt(l1) != null) {//Caso en el que la posición está dentro del mapa, y hay un bloque.
							
							n.put(new Location(world,x,y,z), world.getBlockAt(l1));
						
						}else if(Location.check(l1) && world.getBlockAt(l1) == null && world.getCreatureAt(l1) == null && world.getItemsAt(l1) == null) //Caso en el que la posicion está dentro del mapa, y no hay ni bloque ni criaturas ni items.
							n.put(new Location(world,x,y,z), BlockFactory.createBlock(Material.AIR));
						else //Cualquier otro caso (o hay item, o creature)
							n.put(new Location(world,x,y,z),null); 
					}
					
				}
				
			}
		
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
		
		return n;
	}

	@Override
	public int getNegativeLimit() {
		
		return (world.getSize() % 2 == 0) ? -((world.getSize()/2)-1) : -(world.getSize()/2);
		
	}

	@Override
	public IPlayer getPlayer() {
		
		return player;
		
	}

	@Override
	public int getPositiveLimit() {
		
		return world.getSize()/2;
		
	}

	@Override
	public List<Creature> getCreatures(Location loc) {
		List<Creature> c = new ArrayList<>();
		try {
			/**
			 * Este for recorre todas las posiciones posibles de la x.
			 */
			for(int x = 0; x < 16;x++) {
				
				/**
				 * Este for recorre todas las posiciones posibles de la y.
				 */
				for(int y = 0; y < 16; y++) {
					
					/**
					 * Este for recorre todas las posiciones posibles de la z.
					 */
					for(int z = 0; z < 16; z++) {
						
						if(world.getCreatureAt(new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z)) != null) //Si hay criatura
							c.add(world.getCreatureAt(new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z)));
						
					}
					
				}
				
			}
			
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return c;
	}

	@Override
	public Map<Location, ItemStack> getItems(Location loc) {
		Map<Location,ItemStack> i = new HashMap<>();
		try {
			/**
			 * Este for recorre todas las posiciones posibles de la x.
			 */
			for(int x = 0; x < 16;x++) {
				
				/**
				 * Este for recorre todas las posiciones posibles de la y.
				 */
				for(int y = 0; y < 16; y++) {
					
					/**
					 * Este for recorre todas las posiciones posibles de la z.
					 */
					for(int z = 0; z < 16; z++) {
						
						if(world.getItemsAt(new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z)) != null) //Si hay un item en la posición absoluta
							i.put(new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z), world.getItemsAt(new Location(world,loc.getX() + x,loc.getY() + y,loc.getZ() + z)));
						
						
					}
					
				}
				
			}
		}catch(Exception e) {
			
			e.printStackTrace();
			
		}
		
		return i;
	}

}
