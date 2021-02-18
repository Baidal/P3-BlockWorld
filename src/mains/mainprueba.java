package mains;




import model.BlockWorld;

public class mainprueba {

	public static void main(String[] args) {
		
		
		BlockWorld b = BlockWorld.getInstance();
		
		/*
		try {
			b.playFile("C:\\Users\\PC\\Desktop\\U\\Segundo\\1 Cuatrimestre\\Programacion 3\\Practica 3\\prog3-blockworld-p3\\src\\mains\\hola.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		b.playFromConsole();
		
		
		
			
		
		
	}
}


