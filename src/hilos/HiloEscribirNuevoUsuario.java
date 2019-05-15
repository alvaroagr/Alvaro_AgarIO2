package hilos;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Clase encargada de realizar el registro de los nuevos usuarios en el archivo
 * de texto plano
 * 
 * @author cris6
 *
 */
public class HiloEscribirNuevoUsuario extends Thread {

	/**
	 * Cadena con el nickname del nuevo usuario
	 */
	private String nickname;

	/**
	 * Cadena con la contrase�a del nuevo usuario
	 */
	private String contrase�a;

	/**
	 * Constructor del HiloEscribirNuevoUsuario
	 * 
	 * @param nickname   - nickname del nuevo usuario
	 * @param contrase�a - contrase�a del nuevo usuario
	 */
	public HiloEscribirNuevoUsuario(String nickname, String contrase�a) {
		// TODO Auto-generated constructor stub
		this.nickname = nickname;
		this.contrase�a = contrase�a;
	}

	@Override
	public void run() {
		try {
			String completa = "";
			BufferedWriter escritor = new BufferedWriter(new FileWriter("./data/users.txt"));
			completa += contrase�a + nickname;
			escritor.write(completa);
			escritor.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Hubo un error en el registro del nuevo usuario", "REGISTO FALLIDO",
					JOptionPane.ERROR_MESSAGE);
		}
	}

}
