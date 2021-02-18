package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import model.exceptions.BadInventoryPositionException;

public class Inventory_P2Test {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(2);

	Inventory inventory;
	final static  Material materias[] = { Material.BEDROCK, Material.CHEST, Material.SAND, Material.DIRT, Material.GRASS, 
			Material.STONE, Material.GRANITE, Material.OBSIDIAN, Material.WATER_BUCKET, 
			Material.APPLE, Material.BREAD, Material.BEEF, Material.IRON_SHOVEL,Material.IRON_PICKAXE, 
			Material.WOOD_SWORD, Material.IRON_SWORD, };
	final String invOut = "(inHand=(GRASS,5),[(BEDROCK,1), (CHEST,2), (SAND,3), (DIRT,4), (GRASS,5), (STONE,6), (GRANITE,7), (OBSIDIAN,8), (WATER_BUCKET,9), (APPLE,10), (BREAD,11), (BEEF,12), (IRON_SHOVEL,1), (IRON_PICKAXE,1), (WOOD_SWORD,1), (IRON_SWORD,1)])";
	ItemStack isApple,isGrass;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		inventory = new Inventory();
		isApple= new ItemStack(Material.APPLE, ItemStack.MAX_STACK_SIZE);
		isGrass = new ItemStack(Material.GRASS, 5);
	}

	@Test
	public void testInventory() {
		
		assertNull(inventory.getItemInHand());
	}

	/* Añade una pila de MAX_STACK_SIZE items del tipo APPLE a un inventario
	 * vacío. Comprueba la agregación entre Inventory e ItemStack.
	 * Vuelve a repetir lo mismo con una pila de 5 items del tipo GRASS
	 */
	
	@Test
	public void testAddItem() {
		assertEquals (ItemStack.MAX_STACK_SIZE, inventory.addItem(isApple ));
		
		assertSame (isApple , inventory.getItem(0)); //Agregación
		
		assertEquals (5, inventory.addItem(isGrass));
		assertSame (isGrass, inventory.getItem(1));
	}
	
	/* Añade a un inventario vacío 16 items cuyos types son cada uno de los 
	 * 16 materiales, repetidos 3 veces (makeInventory(3)). Comprueba que
	 * se han añadido correctamente. 
	 */
	@Test
	public void testAddItemWithRepeatedItems() {
	 	
		inventory = makeInventory(3);
		for (int i=0; i<=15; i++) {
			assertEquals("item["+i+"]==item["+i+16+"]",
					inventory.getItem(i),inventory.getItem(i+16));
			assertEquals("item["+i+"]==item["+i+32+"]",inventory.getItem(i),inventory.getItem(i+32));
		}
	}

	/* 
	 * Prueba los accesos de getItem(n) a posiciones correctas en un
	 * inventario con elementos.
	 */
	@Test
	public void testGetItemOk() {
		ItemStack isOut;
		
		inventory = makeInventory(1);	
		 
		for (int i=11; i>=0; i--) { //food and blocks	
			try {
				isOut = new ItemStack(materias[i], i+1);
				assertEquals ("getItem("+i+") == "+isOut.toString(),
						isOut, inventory.getItem(i));
			} catch (Exception e) {
				fail("Error: excepción "+e.getClass().toString()+" inesperada");
			}	
		 }
		
	}
	
	/* Prueba getItem para inventory vacío  */
	@Test
	public void testGetItemNull() {
		assertNull(inventory.getItem(0)); //inventory vacío
			
	}

	/* Prueba getItem para índices fuera del tamaño del 
	  inventario */
	@Test
	public void testGetItemOutOfRange() {
		
	    inventory = makeInventory(1);
		assertNull(inventory.getItem(16));
		assertNull(inventory.getItem(-1));
		
	}

	/* Borra un inventario con 32 elementos  */
	@Test
	public void testClear() {
	  
	   inventory = makeInventory(2);
	   assertEquals("size=32",32,inventory.getSize());
	   inventory.clear();	
	   assertEquals("size=0",0,inventory.getSize());
	 
	}

	/* Creamos un inventario con las 16 materias. Vamos borrando el primer elemento
	 * y comprobamos que hay siempre uno menos
	 */
	@Test
	public void testClearInt() {
		
		inventory = makeInventory(1);
		
		try {
			for (int i=15; i>=0; i-- ) {
				isApple = inventory.getItem(i);
				assertNotNull(isApple );
				inventory.clear(0);
				isApple = inventory.getItem(i); 
				assertNull(isApple );	//La última posición ya no existe
			}
		} catch (Exception e) {
			fail("Error: excepción "+e.getClass().toString()+" inesperada");
		}
	}
		
	// Se intenta borrar una posición que no existe.
	@Test(expected=BadInventoryPositionException.class)
	public void testClearIntException() throws BadInventoryPositionException {
	 
		inventory = makeInventory(1);
	    inventory.clear(16);
	}

	//First en inventario vacío.
	@Test
	public void testFirstVacio() {
		//Inventory vacío
		assertEquals(-1, inventory.first(Material.BEDROCK));
		
	}

	//First sin repetición de elementos.
	@Test
	public void testFirst1() {
		inventory= makeInventory(1);

		for (int i=0;i<=15; i++) {
			assertEquals(i,inventory.first(materias[i]));
		}
		
	}

	// First en elemento eliminado.
	@Test
	public void testFirst1AndClearInt() {
		inventory= makeInventory(1);

		try {
			inventory.clear(9);  //Borramos APPLE
			assertEquals(-1, inventory.first(Material.APPLE));
			inventory.clear(14); //Borramos IRON_SWORD (estará 1 posición menos)
			assertEquals(-1, inventory.first(Material.IRON_SWORD));
		} catch (Exception e) {
			fail("Error: excepción "+e.getClass().toString()+" inesperada");
		} 
		
	}


	/*First en inventario con 3 repeticiones de items.
	*/
	@Test
	public void testFirst2AndClearInt() {

		inventory = makeInventory(3);  //Habrá 3 items de cada material
			
		assertEquals(5,inventory.first(Material.STONE));
		assertEquals(15,inventory.first(Material.IRON_SWORD));
		try {
			inventory.clear(15);  //Borramos el primer IRON_SWORD;
			assertEquals(30,inventory.first(Material.IRON_SWORD));
			inventory.clear(30);  //Borramos el segundo IRON_SWORD;
			assertEquals(45,inventory.first(Material.IRON_SWORD));
			inventory.clear(45);  //Borramos el último IRON_SWORD;
			assertEquals(-1,inventory.first(Material.IRON_SWORD));
			
		} catch (Exception e) {
			fail("Error: excepción "+e.getClass().toString()+" inesperada");
		} 
	}
	
	//Comprobamos setItemInHand y GetItemInHand
	@Test
	public void testSetAndGetItemInHand() {
		
		inventory.setItemInHand(isApple );
		assertSame(isApple ,inventory.getItemInHand());
		
	}

	//Comprobamos setItemInHand y GetItemInHand
		@Test
		public void testSetAndGetItemInHandNull() {
					
			inventory.setItemInHand(null);
			assertNull(inventory.getItemInHand());
		}

	/* Comprueba getSize() para un inventario vacío y para un inventario
	 * con 160 elementos.
	 */
	
	@Test
	public void testGetSize() {
		assertEquals(0,inventory.getSize());
		
		//Incluimos 10 veces todos los materiales en el inventario
		inventory=makeInventory(10);	
		assertEquals(160,inventory.getSize());
		
		
	}

	//Pone un ItemStack en todo un inventario. Comprueba que lo va poniendo.
	@Test
	public void testSetItem() {
	
		inventory = makeInventory(1);
		for (int i=0; i<=15; i++)
			try {
				inventory.setItem(i,isApple );
				assertEquals(isApple , inventory.getItem(i));
			} catch (Exception e) {
				fail("Error: excepción "+e.getClass().toString()+" inesperada");
			}
	}

	//Excepción al intentar poner un item en una posición en un inventario vac´io
	@Test(expected=BadInventoryPositionException.class)
	public void testSetItemException1() throws BadInventoryPositionException {
		inventory.setItem(0, isApple );
	}
	
	//Excepción por intentar poner un item en una posición no existente en un inventario
	@Test(expected=BadInventoryPositionException.class)
	public void testSetItemException2() throws BadInventoryPositionException {
		inventory = makeInventory(1);
		inventory.setItem(16, isApple );
	}
	
	//Prueba toString() con un inventario vacío e inHand a null.
	@Test
	public void testToStringInventoryEmpty() {
		assertEquals("(inHand=null,[])",inventory.toString().replace('\n', ' ').trim());
	}
	
	//Prueba toString() con un inventario e inHand no vacíos.
	@Test
	public void testToStringInventory() {
	
		inventory = makeInventory(1);
		inventory.setItemInHand(isGrass);
		assertEquals(invOut, inventory.toString().replace('\n', ' ').trim());
	}

	// Test para equals probando con cada uno de los atributos que deben intervenir
	@Test
	public void testEqualsObject() {

			try {
				ItemStack isGrass = new ItemStack(Material.BREAD,5);
				ItemStack isChest = new ItemStack(Material.CHEST,5);
				//Los dos inventarios vacíos
				Inventory inventaux = new Inventory();
				assertTrue(inventaux.equals(inventory));
				
				assertFalse(inventaux.equals(null));
				
				//El mismo inventario
				inventory = makeInventory(1);
				assertTrue(inventory.equals(inventory));
				
				//Dos inventarios con elementos iguales.
				inventaux = makeInventory(1);
				
				inventaux.setItemInHand(isGrass);
				inventory.setItemInHand(isGrass);
				assertTrue(inventaux.equals(inventory));
				
				//Distinto inHand
				inventaux.setItemInHand(isChest);
				assertFalse(inventaux.equals(inventory));
				
				//Mismo material con distinto amount en inHand 
				isChest=new ItemStack(Material.CHEST,2);
				inventory.setItemInHand(isChest);
				assertFalse(inventaux.equals(inventory));
				
				//Mismo inHand, distinto número de elementos en inventario
				inventaux = makeInventory(2);
				inventaux.setItemInHand(isChest);
				assertFalse(inventaux.equals(inventory));
				
				//Parámetro de otra clase
				assertFalse(inventaux.equals(isChest));
				
			} catch (Exception e) {
				fail ("Error: No debió lanzarse la excepción "+e.getClass().getName());
			}
	}
	
	//Test para hasCode() probando los atributos que deben intervenir
	@Test
	public void testHashCode() {
		
		Inventory auxInv = new Inventory();
	
		assertEquals("codes iguales, items vacíos",inventory.hashCode(), auxInv.hashCode());
		
		inventory.setItemInHand(isApple );
		
		assertNotEquals ("codes distintos por inHand distintos", inventory.hashCode(), auxInv.hashCode());
		
		auxInv.setItemInHand(isApple );
		inventory = makeInventory(1);	
		auxInv = makeInventory(1);
		assertEquals("Codes iguales por items iguales, inHand iguales",inventory.hashCode(), auxInv.hashCode());
		auxInv = makeInventory(2);
	
		assertNotEquals ("codes distintos por items distintos", inventory.hashCode(), auxInv.hashCode());
		
	}
	
/*****************************************************************************/
	//FUNCION DE APOYO
	
	//Crea un inventario con todas las materias incluyéndolas n veces 
	 final Inventory makeInventory(int n)  {
		Inventory inv = new Inventory();
		ItemStack isaux;
		try {
		 for (int j=1; j<=n; j++) {
		   for (int i=0; i<=11; i++) { //Para blocks y food
		    isaux = new ItemStack(materias[i],(i+1));	
			inv.addItem(isaux);
		   }
			
		   for (int i=12; i<=15; i++) { //Para weapons and tools
		      isaux = new ItemStack(materias[i],1);
			  inv.addItem(isaux);
		   }
		 }
		} catch (Exception ex) {
			throw new RuntimeException();
		}
		
		return inv;
	}
}
