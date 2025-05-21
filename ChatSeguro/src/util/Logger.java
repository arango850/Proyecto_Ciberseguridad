package util;

public class Logger {
	
	public static void error(String mensaje) {
        System.err.println("[ERROR] " + mensaje);
    }

    public static void info(String mensaje) {
        System.out.println("[INFO] " + mensaje);
    }
	

}
