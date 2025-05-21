package ui;

import java.util.Scanner;

public class ConsolaUI {
	
	private Scanner scanner = new Scanner(System.in);
	
	 public String seleccionarModo() {
	        System.out.println("Seleccione el modo (servidor / cliente):");
	        return scanner.nextLine().trim().toLowerCase();
	    }

	    public int ingresarPuerto() {
	        System.out.println("Ingrese el puerto:");
	        return Integer.parseInt(scanner.nextLine().trim());
	    }

	    public String ingresarIP() {
	        System.out.println("Ingrese la IP del servidor:");
	        return scanner.nextLine().trim();
	    }

}
