package model.score;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import model.Block;
import model.BlockFactory;
import model.ItemStack;
import model.Location;
import model.Material;
import model.SolidBlock;
import model.World;
import model.entities.Player;
import model.exceptions.StackSizeException;
import model.exceptions.score.ScoreException;

public class XPScore_PreP4Test {

	XPScore xpJulia, xpCharles;
	Player pJulia, pCharles;
	World world;
	CollectedItemsScore cis;
	MiningScore ms;
	PlayerMovementScore pms;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		world = new World(3,10,"A Little World", "Joan");
		pJulia = new Player("Julia",world);
		pCharles = new Player("Charles",world);
		xpJulia = new XPScore(pJulia);
		xpCharles = new XPScore(pCharles);
	}

	@Test
	public void testXPScoreAndGetName() {
		Score<Player> score = xpJulia; //Implementas la herencia ???
		assertEquals("Julia",score.getName());	
		
		assertEquals("Julia",xpJulia.getName());
		assertEquals("Charles", xpCharles.getName());
	}

	/* Comparar las puntuaciones de xpJulia y de xpCharles modificando solo
	 * el health y el foodLevel
	 */	 
	//TODO
	@Test
	public void testCompareTo1() {
			//Inicialmente ambas puntuaciones son iguales 
			assertTrue(xpJulia.compareTo(xpCharles)==0);
			
			xpCharles.score(pCharles);
			assertTrue(xpJulia.compareTo(xpCharles)==0);
			
			pJulia.setHealth(15);
			xpJulia.score(pJulia);
			assertTrue(xpJulia.compareTo(xpCharles)==1);
			/* Modifica el health de Julia y comprueba que
			 * si xpJulia invoca a compareTo, devuelve un valor >0
			 * al ser el score de xpJulia menor
			 */	
			
			
			/* Modifica el foodLevel de Charles al mismo valor que
			 * el health de Julia y comprueba que compareTo devuelve 0
			 */
			pCharles.setFoodLevel(15);
			xpCharles.score(pCharles);
			assertTrue(xpJulia.compareTo(xpCharles)==0);
			
	}
	
	/* Comparar los XPScore de xpJulia y de xpCharles sin modificar
	 * health y foodLevel pero sí añadiendo a la lista de XPScore de xpJulia 
	 * y xpCharles: un CollectedItemsScore, un MiningScore y un PlayerMovementScore 
	 * sucesivamente.
	 */
	//TODO
	@Test
	public void testCompareTo2() {
		try {
			//Añadimos un ItemScore al marcador de Julia
			cis = new CollectedItemsScore("Julia");
			cis.score(new ItemStack(Material.BREAD,5));
			xpJulia.addScore(cis);							//EL getScoring() de xpJulia es de 65
			assertTrue(xpJulia.compareTo(xpCharles)<0);	
			
			
			
			
			//Añadimos el mismo ItemScore al marcador de Charles
			xpCharles.addScore(cis);						//El getScoring() de xpCharles es de 65
			assertTrue(xpCharles.compareTo(xpJulia)==0);	
			
			//Añadimos un MiningScore al marcador de Julia
			ms = new MiningScore("Julia");					
			ms.score( new SolidBlock(Material.DIRT));
			xpJulia.addScore(ms);							//El getScoring() de xpJulia es de 	52.75		
			assertTrue(xpJulia.compareTo(xpCharles)>0);	
			
			
			//Añadimos el mismo MiningScore al marcador de Charles
			xpCharles.addScore(ms);
			assertTrue(xpCharles.compareTo(xpJulia)==0);
			
			//Añadimos un PlayerMovementScore al marcador de Julia
			pms = new PlayerMovementScore("Julia");
			Location aux=new Location(pJulia.getLocation());
			pms.score(aux);			//LA PRIMERA VEZ QUE LLAMAMOS A SCORE, ESTE VALE 0 (PORQUE PREVIOUSLOCATION VALE NULL)
			pms.score(aux.add(aux));//LA NUEVA LOCALIZACIÓN VA A VALER (0,132,0), QUE AL HACER LOS CALCULOS PARA SABER LA DISTANCIA ENTRE (0,66,0) Y (0,132,0) NOS DA 66.
			xpJulia.addScore(pms);  //AHORA SCORE VA A VALER (25 + 0,5 + 66)/ + 40 = 70,5
			assertTrue(xpJulia.compareTo(xpCharles)<0);	
			//Añadimos el mismo MiningScore al marcador de Charles
			xpCharles.addScore(pms);
			assertTrue(xpCharles.compareTo(xpJulia)==0);
			
		} catch (Exception e) {
			fail ("Error, no debió lanzar la excepcion "+e.getClass().getName());
		}
	}

	/* Comprobar que inicialmente, sin calcular, xpJulia tiene el marcador a 0. 	 
	 * Calcular el Score inicial de xpJulia, y comprobar que es 40. Modificar
	 * el health y calcular y comprobar el nuevo resultado. Hacer lo mismo con
	 * foodLevel.
	 */
	//TODO
	@Test
	public void testScorePlayer() {
		assertEquals(0, xpJulia.score,0.01);
		
		xpJulia.score(pJulia);
		assertEquals(40, xpJulia.score,0.01);
				
		pJulia.setHealth(10);
		xpJulia.score(pJulia);
		assertEquals(30,xpJulia.score,0.01);
		
		pJulia.setFoodLevel(10);
		xpJulia.score(pJulia);
		assertEquals(20,xpJulia.score,0.01);
		
		
		
	}	
	
	//Se comprueba la excepción ScoreException en el método score
	@Test(expected=ScoreException.class)
	public void testScorePlayerException() {
		Player p = new Player("Marta",world);
		xpJulia.score(p);
	}
	
	
	/* Añadir un CollectedIntemsScore a xpJulia, y comprobar
	 * que lo que obteneis es lo esperado.
	 * Hacer lo mismo con un MininScore y un PlayerMovementScore
	 */
	//TODO
	@Test
	public void testAddScorePlayer() {
		
		CollectedItemsScore c = new CollectedItemsScore(pJulia.getName());
		MiningScore m = new MiningScore(pJulia.getName());
		PlayerMovementScore p = new PlayerMovementScore(pJulia.getName());
		
		
		try {
			c.score(new ItemStack(Material.APPLE,3)); //El score de c es 12
			assertEquals(c.getScoring(),12,0.01);
			
			m.score(BlockFactory.createBlock(Material.OBSIDIAN)); // El score de m es 5
			assertEquals(m.getScoring(),5,0.01);
			
			p.score(pJulia.getLocation());
			p.score(new Location(pJulia.getLocation().getWorld(),pJulia.getLocation().getX(),pJulia.getLocation().getY()+1,pJulia.getLocation().getZ())); //El score de p es 1
			assertEquals(p.getScoring(),1,0.01);
			
			System.out.println(xpJulia.getScoring());
			
			xpJulia.addScore(c);
			xpJulia.addScore(m);
			xpJulia.addScore(p);
			
			
			//LA MEDIA DE LOS SCORES ES (12 + 5 + 1)/3 = 6, más los 40 de sumar el nivel de comida y el nivel de salud, 46
			assertEquals(46,xpJulia.getScoring(),0.01);
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
		
	}

	/* Comprobar lo mismo que en el testScorePlayer1 pero con 
	 * getScore()
	 */
	@Test
	public void testGetScoring() {
		assertEquals(0, xpJulia.score,0.01);
		
		xpJulia.score(pJulia);
		assertEquals(40, xpJulia.getScoring(),0.01);
				
		pJulia.setHealth(10);
		xpJulia.score(pJulia);
		assertEquals(30,xpJulia.getScoring(),0.01);
		
		pJulia.setFoodLevel(10);
		xpJulia.score(pJulia);
		assertEquals(20,xpJulia.getScoring(),0.01);
		
		
		
	}
	
    /**********************************************/
	//FUNCIONES DE APOYO
	/* Para las salidas Score.toString() compara los valores impresos
	 * de los Scores hasta una precisión de 0.01
	 * 
	 */
	void compareScores(String expected, String result ) {
		String ex[]= expected.split(":");
		String re[]= result.split(":");
		if (ex.length!=re.length) fail("Lineas distintas");
		if (ex.length==2) {
			if (ex[0].trim().equals(re[0].trim())) {
				double ed = Double.parseDouble(ex[1]);
				double rd = Double.parseDouble(re[1]);
		
				assertEquals(ex[0],ed,rd,0.01);
			}
			else fail("Nombres jugadores distintos: esperado=<"+ex[0].trim()+"> obtenido=<"+re[0].trim()+">");
		}
		else
			assertEquals(expected.trim(),result.trim());		
	}

	/* Repite lo mismo que en testScorePlayer() y testAddScorePlayer pero usando
	 * toString() en los asseretEquals
	 */
	//TODO
	@Test
	public void testToString() {
		
		CollectedItemsScore c = new CollectedItemsScore(pJulia.getName());
		MiningScore m = new MiningScore(pJulia.getName());
		PlayerMovementScore p = new PlayerMovementScore(pJulia.getName());
		
		xpJulia.score(pJulia);
		assertEquals("Julia:40.0", xpJulia.toString());
		
		pJulia.setFoodLevel(10);
		xpJulia.score(pJulia);
		assertEquals("Julia:30.0", xpJulia.toString());
		
		pJulia.setHealth(10);
		xpJulia.score(pJulia);
		assertEquals("Julia:20.0", xpJulia.toString());
		
		try {
			c.score(new ItemStack(Material.APPLE,3)); //El score de c es 12
			assertEquals(c.getScoring(),12,0.01);
			
			xpJulia.addScore(c);
			assertEquals("Julia:32.0", xpJulia.toString());
			
			m.score(BlockFactory.createBlock(Material.OBSIDIAN)); // El score de m es 5
			assertEquals(m.getScoring(),5,0.01);
			
			xpJulia.addScore(m);
			assertEquals("Julia:28.5", xpJulia.toString());
			
			p.score(pJulia.getLocation());
			p.score(new Location(pJulia.getLocation().getWorld(),pJulia.getLocation().getX(),pJulia.getLocation().getY()+1,pJulia.getLocation().getZ())); //El score de p es 1
			xpJulia.addScore(p);
			assertEquals("Julia:26.0", xpJulia.toString());
			
		
			
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}	
}
