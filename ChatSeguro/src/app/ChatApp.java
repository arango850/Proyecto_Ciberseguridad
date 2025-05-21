package app;

import net.ConexionManager;
import ui.ConsolaUI;

public class ChatApp {

	public static void main(String[] args) {
		ConsolaUI ui = new ConsolaUI();
		String modo = ui.seleccionarModo();
		
		if(modo.equalsIgnoreCase("servidor")) {
			int puerto = ui.ingresarPuerto();
			ConexionManager servidor = new ConexionManager(true,null,puerto);
			servidor.iniciar();		
		}else if (modo.equalsIgnoreCase("cliente")) {
            String ip = ui.ingresarIP();
            int puerto = ui.ingresarPuerto();
            ConexionManager cliente = new ConexionManager(false, ip, puerto);
            cliente.iniciar();
        } else {
            System.out.println("Modo inválido. Terminando...");
        }
	}
}
