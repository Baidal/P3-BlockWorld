/**
 * @author Luis Vidal Rico 45927898A
 */
package model;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.entities.Animal;
import model.entities.Monster;
import model.entities.Player;
import model.exceptions.BadInventoryPositionException;
import model.exceptions.BadLocationException;
import model.exceptions.EntityIsDeadException;
import model.exceptions.StackSizeException;
import model.exceptions.WrongMaterialException;
import model.score.CollectedItemsScore;
import model.score.MiningScore;
import model.score.PlayerMovementScore;

/**
 * Clase Singleton, que permite crear el mundo,mover
 * el jugador, elegir sus items y usarlos. Clase principal que envuelve todo el juego.
 * @author luis
 *
 */
public class BlockWorld {
	
	/**
	 * CollectedItemsScore
	 */
	private CollectedItemsScore itemsScore;
	
	/**
	 * MiningScore 
	 */
	private MiningScore miningScore;
	
	/**
	 * PlayerMovementScore
	 */
	private PlayerMovementScore movementScore;
	
	
	/**
	 * Instancia
	 */
	private static BlockWorld instance = null;
	
	/**
	 * Mundo 
	 */
	private World world;
	
	/**
	 * Constructor que inicializa el mundo a null.
	 */
	private BlockWorld() {world = null;
						  itemsScore = null;
						  miningScore = null;
						  movementScore = null;}

	
	/**
	 * Obtiene la instancia de un nuevo BlockWorld;
	 * @return BlockWorld
	 */
	static public BlockWorld getInstance() {
		if(instance == null)
			instance = new BlockWorld();
		
		return instance;
	}
	
	/**
	 * Crea un mundo nuevo.
	 * Inicializa las puntuaciones.
	 * @param seed Semilla del mundo
	 * @param size Tamaño del mundo
	 * @param name Nombre del mundo
	 * @return mundo creado
	 */
	public World createWorld(long seed,int size,String name, String PlayerName) {
		
		world = new World(seed,size,name,PlayerName);
		itemsScore = new CollectedItemsScore(PlayerName);
		miningScore = new MiningScore(PlayerName);
		movementScore = new PlayerMovementScore(PlayerName);
		
		
		return world;
		
		
	}
	
	/**
	 * Muestra toda la información de player
	 * @param player Player 
	 * @return String con la cadena que informa sobre el jugador
	 */
	public String showPlayerInfo(Player player) {
		
		String s = player.toString();
		
		try {
			s = s + "\n" + world.getNeighbourhoodString(player.getLocation());
			
			s = s + "\n" + "Scores: [items: " + itemsScore.getScoring() + ", blocks: " + miningScore.getScoring() + ", movements: " + movementScore.getScoring() + "]";
			
		} catch (BadLocationException e) {
			e.getMessage();
		}

		return s;
	}

	/**
	 * Mueve el jugador tantos bloques como indique dx,dy,dz.
	 * Si encuentra un item, lo coge. 
	 * Si se mueve a la lava, le baja la vida.
	 * 
	 * @param p Player a mover
	 * @param dx Coord. x
	 * @param dy Coord. y
	 * @param dz Coord. z
	 * @throws BadLocationException Posición errónea
	 * @throws EntityIsDeadException Entidad Muerta
	 */
	//TODO modificar el método para añadir los cambios en el sistema de puntuación
	public void movePlayer(Player p, int dx, int dy, int dz) throws BadLocationException, EntityIsDeadException{
		
		try {
			p.move(dx, dy, dz);
			movementScore.score(p.getLocation()); //Puntuamos al jugador por la posición a la que se ha movido
			
			
			//Comprobamos si hay un item en la posición a la que nos hemos movido, y lo cogemos
			if(world != null && world.getItemsAt(p.getLocation()) != null) {	 																				
				
				p.addItemsToInventory(new ItemStack(world.getItemsAt(p.getLocation()))); //Añadimos el item del mundo al inventario del jugador
				itemsScore.score(world.getItemsAt(p.getLocation())); //Puntuamos al jugador por el item recogido
				world.removeItemsAt(p.getLocation()); //Eliminamos el item del mundo
				
			}
			
			//Si hay un bloque en la posición donde nos hemos movido
			if(world != null && world.getBlockAt(p.getLocation()) != null) {
				
				//Si el bloque donde nos hemos movido, este nos hará daño si es lava
				if(world.getBlockAt(p.getLocation()) instanceof LiquidBlock) {
					
					LiquidBlock b = null;
					try {
						b = new LiquidBlock(world.getBlockAt(p.getLocation()).getType());
					} catch (WrongMaterialException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
					//Si es lava, inflinge daño al jugador
					if(b.getType().getSymbol() == '#') {
						p.damage(b.getDamage());
					}
				}
				
				
			}
		} catch (EntityIsDeadException e) {
			throw new EntityIsDeadException();
		} catch (BadLocationException e) {
			throw new BadLocationException("La posición no pertenece a este mundo o no tiene mundo asociado");
		}
		
		
		
	}
	
	/**
	 * Elije el item de la posición pos
	 * 
	 * @param p Player
	 * @param pos int
	 * @throws BadInventoryPositionException Posición errónea
	 */
	public void selectItem(Player p, int pos) throws BadInventoryPositionException {
		
		try {
			p.selectItem(pos);
		} catch (BadInventoryPositionException e) {
			
			throw new BadInventoryPositionException(pos);
			
		}
		
	}
	/**
	 * Usar item de la mano
	 * @param p Player 
	 * @param times int
	 * @throws EntityIsDeadException Posición errónea
	 * @throws IllegalArgumentException  Argumento erróneo
	 */
	//TODO modificar para que puntúe al jugadr
	public void useItem(Player p, int times) throws EntityIsDeadException, IllegalArgumentException{
		
		try {
		
			ItemStack s = p.useItemInHand(times); //useItemInHand puede devolver null.
			
			double daño = 0.1;	//Daño será el valor del daño que produce el jugador si rompe un bloque o ataca a una criatura (sin contar las time veces)
			
			if(s != null) {
				if(s.getType().isWeapon() || s.getType().isTool()) {
					
					daño = s.getType().getValue();
					
				}
			}
			
			
			
			//CASO EN EL QUE 'S' ES NULL, COMIDA O LA ORIENTACION DE 'P' ES HACIA FUERA DEL MUNDO 
			//EN EL CHECK, SE COMPRUEBA QUE LA Location QUE IDENTIFICA
			//LA ORIENTACIÓN DEL JUGADOR ESTÉ DENTRO DEL MUNDO.
			if(s != null)
			if(!(s == null || s.getType().isEdible() || !Location.check(p.getOrientation())) || s.getType().isLiquid()) {
			
				
				try {
					
					if(world.getBlockAt(p.getOrientation()) != null && !world.getBlockAt(p.getOrientation()).getType().isLiquid()) {//CASO EN EL QUE LA POSICIÓN EN LA QUE MIRA HAY UN BLOQUE SÓLIDO
						
						
						
							SolidBlock b = new SolidBlock((SolidBlock)world.getBlockAt(p.getOrientation()));	//Añadir el bloque en caso de romperlo, ya que si primero lo eliminamos, no podemos saber los drops de este.
							
						
						//SI EL DAÑO DE LO QUE LLEVA EL JUGADOR EN LA MANO ES MAYOR O IGUAL QUE EL VALOR DEL BLOQUE
						//LO ROMPE Y DEJA SUS ITEMS EN SU POSICION
						if(daño*times >= world.getBlockAt(p.getOrientation()).getType().getValue()) {
							
							miningScore.score(b); //Puntuamos al jugador por romper el bloque que está en la posición a la que está mirando.
							
							//Eliminamos el bloque
							world.destroyBlockAt(p.getOrientation());
						
							if(world.getBlockAt(p.getOrientation()) instanceof SolidBlock)
								//Añadimos el bloque en forma de items.
								world.addItems(p.getOrientation(), b.getDrops());
							
						}
						
					}else if(world.getCreatureAt(p.getOrientation()) != null) {	//CASO EN EL QUE LA POSICIÓN A LA QUE MIRA ESTÁ OCUPADA POR UNA CRIATURA
						
						
						if(world.getCreatureAt(p.getOrientation()) instanceof Monster) { //CASO EN EL QUE ES UN MONSTRUO
							
							if(daño*times >= world.getCreatureAt(p.getOrientation()).getHealth()) { //Monstruo muere
									
								//Matamos a la criatura
								world.killCreature(p.getOrientation());
									
							}else { //No muere
									
								//Bajamos la vida a la criatura
								world.getCreatureAt(p.getOrientation()).damage(daño*times);
									
								//Bajamos la vida a player
								world.getPlayer().damage(0.5*times);
							}
								
							
						}else if(world.getCreatureAt(p.getOrientation()) instanceof Animal) {//Caso en el que es un animal
								
							if(daño*times >= world.getCreatureAt(p.getOrientation()).getHealth()) { //Animal muere
								
								//Matamos a la criatura :(
								world.killCreature(p.getOrientation());
								
								//Añadimos 1 BEEF en la posición del animal 
								world.addItems(p.getOrientation(), new ItemStack(Material.BEEF,1));
							
							}else {
								
								//Bajamos la vida a la criatura
								world.getCreatureAt(p.getOrientation()).damage(daño*times);
							
							}
							
							
						}
						
					}else if(world.getBlockAt(p.getOrientation()) == null && world.getCreatureAt(p.getOrientation()) == null && world.getItemsAt(p.getOrientation()) == null) { //CASO EN EL QUE POSICIÓN A LA QUE MIRA ESTÁ VACÍA
						
						
						if(s.getType().isBlock()) { //Es bloque
							
							
							world.addBlock(p.getOrientation(),BlockFactory.createBlock(s.getType())); //Añadimos el bloque al mundo
							
							
						}
						
					}
				
				} catch (BadLocationException  | StackSizeException | WrongMaterialException e) {
					e.printStackTrace();
				}
				
			}
				
				//No hacemos nada
				
			
			
		
		}catch (IllegalArgumentException e) {
			
			throw new IllegalArgumentException();
			
		} catch (EntityIsDeadException e) {
			
			throw new EntityIsDeadException();
			
		}
		
		
	}
	
	/**
	 * Orienta al jugador hacia la posición (dx,dy,dz)
	 * @param p Player
	 * @param dx Int
	 * @param dy Int
	 * @param dz Int
	 * @throws BadLocationException Si la posición no es adyacente a la posición del jugador
	 * @throws EntityIsDeadException Si la entidad ha muerto
	 */
	public void orientatePlayer(Player p, int dx, int dy, int dz) throws BadLocationException, EntityIsDeadException{
		
		try {
			p.orientate(dx, dy, dz);
		}catch(BadLocationException e) {
			
			throw new BadLocationException(e.getMessage());
			
		}catch(EntityIsDeadException e) {
			
			throw new EntityIsDeadException();
			
		}
			
	}
	
	/**
	 * Abre el fichero de entrada indicado y ejecuta cada una de sus órdenes
	 * @param path String
	 * @throws FileNotFoundException si no encuentra el archivo
	 */
	public void playFile(String path) throws FileNotFoundException{
		
		File file = new File(path);
		
		if(!file.exists())
			throw new FileNotFoundException();
		
		Scanner sc = new Scanner(file);
		
		play(sc);
		
		
	}
	
	/**
	 * Abre la entrada estándar de la consola para leer las ordenes del juego
	 */
	public void playFromConsole() {
		
		Scanner sc = new Scanner(System.in);
		
		int contador = 0, seed = 0,size = 0, dx = 0, dy = 0,dz = 0, n = 0;
		
		String name = "";
		
		String ord = "";
		
		String namePlayer = "";
		
		Boolean compr = true; //Se encarga de comprobar que los argumentos del scanner son correctos;
		
		if(sc.hasNextInt())
			seed = sc.nextInt();
		else
			compr = false;
		
		
		if(sc.hasNextInt())
			size = sc.nextInt();
		else
			compr = false;
		
		
		if(sc.hasNext())
			while(sc.hasNext()) {
				name = name + sc.next();
				if(sc.hasNext())
					name = name + " ";
			
			}
		else
			compr = false;
		
		if(sc != null && sc.hasNext())
			namePlayer = sc.next();
		else
			compr = false;
		
		
		if(compr)
			createWorld(seed,size,name,namePlayer);
		else {
			System.out.println("No se ha inicializado bien el mundo.");
			System.exit(0);
		}
		
		sc.nextLine();
		contador++;
		
		while(!world.getPlayer().isDead()) {
			
			compr = true;
				
			ord = sc.next();
				
			switch(ord) {
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "MOVE"----------------	
				case "move":
							
					if(sc.hasNextInt())
						dx = sc.nextInt();
					else
						compr = false;
							
					if(sc.hasNextInt())
						dy = sc.nextInt();
					else
						compr = false;
							
					if(sc.hasNextInt())
						dz = sc.nextInt();
					else
						compr = false;
							
					if(compr)
						try {
							movePlayer(world.getPlayer(),dx,dy,dz);
						} catch (BadLocationException e) {
							e.printStackTrace();
						} catch (EntityIsDeadException e) {
							e.printStackTrace();
						}
							
				break;
				
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "ORIENTATE"----------------
				case "orientate":
					if(sc.hasNextInt())
						dx = sc.nextInt();
					else
						compr = false;
							
					if(sc.hasNextInt())
						dy = sc.nextInt();
					else
						compr = false;
							
					if(sc.hasNextInt())
						dz = sc.nextInt();
					else
						compr = false;
							
					if(compr)
						if(!(dx == 0 && dy == 0 && dz == 0))
							try {
								orientatePlayer(world.getPlayer(),dx,dy,dz);
							} catch (BadLocationException | EntityIsDeadException e) {
								e.printStackTrace();
							}
				break;
						
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "USEITEM"----------------
				case "useItem":
					if(sc.hasNextInt())
						n = sc.nextInt();
					else
						compr = false;
							
					if(compr)
						try {
							useItem(world.getPlayer(),n);
						} catch (IllegalArgumentException | EntityIsDeadException e) {
							e.printStackTrace();
						}
				break;
	
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "MOVE"----------------
				case "show":
					System.out.println(showPlayerInfo(world.getPlayer()) + "\n");
				break;
						
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "SELECTITEM"----------------
				case "selectItem":
					if(sc.hasNextInt())
						n = sc.nextInt();
					else
						compr = false;
						
					if(compr)
						try {
							useItem(world.getPlayer(),n);
						} catch (IllegalArgumentException | EntityIsDeadException e) {
							e.printStackTrace();
						}
							
				break;
						
				default:
							
					compr = false;
				
			}
		
			
			if(!compr)
				System.out.println("El comando que aparece en la línea " + (contador+1) + " no se ha reconocido.");
			
			contador++;
			if(sc.hasNextLine())
				sc.nextLine();
		}
			
			
		
		
		
		sc.close();
	}
	
	/**
	 * Se encarga de crear una partida que va leyendo del scanner pasado por parámetro
	 * @param sc Scanner
	 */
	private void play(Scanner sc){
		
		int seed = 0,size = 0, dx = 0, dy = 0,dz = 0, n = 0;
		
		String name = "";
		
		String ord = "";
		
		String namePlayer = "";
		
		Scanner sc1 = null;
		
		if(sc.hasNextLine())
			sc1 = new Scanner(sc.nextLine());
		
		
		Boolean compr = true, continuar = true; //Se encarga de comprobar que los argumentos del scanner son correctos
		
		//----------------CASO DEL PRIMER PARÁMETRO, EL QUE CREA EL MUNDO----------------
		if(sc1 != null && sc1.hasNextInt())
			seed = sc1.nextInt();
		else
			compr = false;
		
		
		if(sc1 != null && sc1.hasNextInt())
			size = sc1.nextInt();
		else
			compr = false;
		
		if(sc1 != null && sc1.hasNext())
			namePlayer = sc1.next();
		else
			compr = false;
		
		
		if(sc1 != null && sc1.hasNext())
			while(sc1.hasNext()) {
				name = name + sc1.next();
				if(sc1.hasNext())
					name = name + " ";
			}
		else
			compr = false;
		
		
		
		
		if(compr)
			createWorld(seed,size,name,namePlayer);
		else {
			System.out.println("No se ha inicializado bien el mundo.");
			System.exit(0);
		}
			
		if(sc.hasNextLine())
			sc1 = new Scanner(sc.nextLine());
		
		
		while(continuar && !world.getPlayer().isDead()) {
			
			compr = true;
				
			ord = sc1.next();
			
			switch(ord) {
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "MOVE"----------------	
				case "move":
							
					if(sc1.hasNextInt())
						dx = sc1.nextInt();
					else
						compr = false;
							
					if(sc1.hasNextInt())
						dy = sc1.nextInt();
					else
						compr = false;
							
					if(sc1.hasNextInt())
						dz = sc1.nextInt();
					else
						compr = false;
							
					if(compr)
						try {
							movePlayer(world.getPlayer(),dx,dy,dz);
						} catch (BadLocationException e) {
							
							e.printStackTrace();
						} catch (EntityIsDeadException e) {
							
							e.printStackTrace();
						}
							
				break;
				
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "ORIENTATE"----------------
				case "orientate":
					if(sc1.hasNextInt())
						dx = sc1.nextInt();
					else
						compr = false;
							
					if(sc1.hasNextInt())
						dy = sc1.nextInt();
					else
						compr = false;
							
					if(sc1.hasNextInt())
						dz = sc1.nextInt();
					else
						compr = false;
							
					if(compr)
						if(!(dx == 0 && dy == 0 && dz == 0))
							try {
								orientatePlayer(world.getPlayer(),dx,dy,dz);
							} catch (BadLocationException | EntityIsDeadException e) {
								e.printStackTrace();
							}
				break;
						
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "USEITEM"----------------
				case "useItem":
					if(sc1.hasNextInt())
						n = sc1.nextInt();
					else
						compr = false;
							
					if(compr)
						try {
							useItem(world.getPlayer(),n);
						} catch (IllegalArgumentException | EntityIsDeadException e) {
	
							e.printStackTrace();
						}
				break;
	
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "MOVE"----------------
				case "show":
					System.out.println(showPlayerInfo(world.getPlayer()) + "\n");
				break;
						
				//----------------CASO EN EL QUE EL PRIMER PARÁMETRO ES "SELECTITEM"----------------
				case "selectItem":
					if(sc1.hasNextInt())
						n = sc1.nextInt();
					else
						compr = false;
						
					if(compr)
						try {
							selectItem(world.getPlayer(),n);
						} catch (BadInventoryPositionException e) {
							
							e.printStackTrace();
						}
						
							
				break;
						
				default:
							
					compr = false;
				
			}
		
			/*
			if(!compr)
				System.out.println("El comando que aparece en la línea " + (contador+1) + " no se ha reconocido.");*/
			
			
			
			if(sc.hasNextLine())
				sc1 = new Scanner(sc.nextLine());
			else
				continuar  = false;
			
				
			
		}
		
		sc.close();
		sc1.close();
	}

	/**
	 * Getter
	 * @return CollectedItemsScore
	 */
	public CollectedItemsScore getItemsScore() {
		return itemsScore;
	}

	/**
	 * Getter
	 * @return MiningScore
	 */
	public MiningScore getMiningScore() {
		return miningScore;
	}

	/**
	 * Getter
	 * @return PlayerMovementScore
	 */
	public PlayerMovementScore getMovementScore() {
		return movementScore;
	}
	
	
	
}


