package model.persistence;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import model.Block;
import model.ItemStack;
import model.Location;
import model.entities.Creature;

/**
 * 
 * @author Luis Vidal Rico 45927898A
 *
 */
public interface IWorld extends IPlayer {

	/**
	 * Devuelve un mapa ordenado de bloques indexado
	 * por objetos de tipo Location que representan un área
	 * de 16x16x16. Loc es el bloque situado en la esquina
	 * noroeste del nivel inferior del mundo.
	 * @param loc Location
	 * @return NavigableMap<Location,Block>
	 */
	public NavigableMap<Location,Block> getMapBlock(Location loc);
	
	/**
	 * Obtiene el valor límite negativo de las posiciones del mundo.
	 * @return int
	 */
	public int getNegativeLimit();
	
	/**
	 * Obtiene el jugador.
	 * @return IPlayer
	 */
	public IPlayer getPlayer();
	
	/**
	 * Obtiene el valor límite positivo de las posiciones del mundo.
	 * @return
	 */
	public int getPositiveLimit();
	
	/**
	 * Obtiene la lista con todas las criaturas que habitan 
	 * en el mundo y cuya posición que dentro del área de 
	 * bloques de 16x16x16 cuya esquina noroeste del nivel más inferior
	 * corresponde con loc.
	 * @param loc
	 * @return
	 */
	public List<Creature> getCreatures(Location loc);
	
	/**
	 * Obtiene un mapa de objetos ItemStack indexado por su posición en el mundo, 
	 * con todos los items cuya posición en el mundo queda dentro del área 
	 * de 16x16x16 bloques cuya esquina noroeste del nivel más inferior 
	 * corresponde a la posición ‘loc’.
	 * @param loc Location
	 * @return Map<Location,ItemStack>
	 */
	public Map<Location,ItemStack> getItems(Location loc);
	
}
