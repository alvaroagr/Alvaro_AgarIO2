package conexion;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.SSLServerSocketFactory;

import hilos.HiloEscribirNuevoUsuario;
import hilos.HiloValidaUsuario;

/**
 * Clase que se encarga de la recepci�n de solicitudes sobre el login y registro
 * de los usuarios
 * 
 * @author cris6
 *
 */
public class SSLServidor extends Thread {

	/**
	 * Colecci�n que contiene la contrase�a de los jugadores
	 */
	private static HashMap<String, String> dataJugadores;

	/**
	 * Colecci�n que contiene la informaci�n de los jugadores
	 */
	private static ArrayList<String> dataJugadores2;

	/**
	 * Cadena con la ubicaci�n del archivo de las claves
	 */
	public static final String KEYSTORE_LOCATION = "./mykeystore/keystore.jks";

	/**
	 * Cadena con la contrase�a del archivo de las claves
	 */
	public static final String KEYSTORE_PASSWORD = "123456";

	public static void main(String[] args) {
		System.setProperty("javax.net.ssl.keyStore", KEYSTORE_LOCATION);
		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);

		cargarDatos();
		SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		ServerSocket serverSocket;
		try {
			serverSocket = ssf.createServerSocket(10000);
			System.out.println("SSLServerSocket iniciado en " + serverSocket.getLocalPort());

			while (true) {
				Socket socket = serverSocket.accept();

				DataInputStream dIS = new DataInputStream(socket.getInputStream());
				DataOutputStream dOS = new DataOutputStream(socket.getOutputStream());

				System.out.println("Clientsocket creado");
				String f = dIS.readUTF();
				if (f.equals("1")) {
					HiloValidaUsuario hvu = new HiloValidaUsuario(socket, dIS, dOS);
					hvu.start();
				} else if (f.equals("2")) {
					String nuevo = dIS.readUTF();
					HiloEscribirNuevoUsuario t = new HiloEscribirNuevoUsuario(nuevo,
							generarReporteActualDeUsuariosValidos());
					t.start();
					if (!t.isAlive()) {
						cargarDatos();
					}
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	/**
	 * Consultar la colecci�n con las contrase�as de los jugadores
	 * @return dataJugadores
	 */
	public static HashMap<String, String> getDataJugadores() {
		return dataJugadores;
	}

	/**
	 * Modificar la colecci�n completa que contiene las contrase�as de los jugadores
	 * @param pDataJugadores
	 */
	public static void setDataJugadores(HashMap<String, String> pDataJugadores) {
		dataJugadores = pDataJugadores;
	}

	/**
	 * Consultar la colecci�n con la informac�n de los jugadores
	 * @return dataJugadores2
	 */
	public static ArrayList<String> getDataJugadores2() {
		return dataJugadores2;
	}

	/**
	 * Modificar la colecci�n completa que contiene la informaci�n de los jugadores
	 * @param dataJugadores2
	 */
	public static void setDataJugadores2(ArrayList<String> dataJugadores2) {
		SSLServidor.dataJugadores2 = dataJugadores2;
	}

	/**
	 * Realiza la lectura del archivo que contiene la informaci�n de los usuarios
	 */
	private static void cargarDatos() {

		dataJugadores = new HashMap<>();
		dataJugadores2 = new ArrayList<String>();
		try (BufferedReader inUsuarios = new BufferedReader(new FileReader("./data/users.txt"))) {

			String lin = "";

			while ((lin = inUsuarios.readLine()) != null && !lin.isEmpty()) {
				String[] dat = lin.split(";");
				String correoElectronico = dat[0];
				String contrase�a = dat[1];

				dataJugadores.put(correoElectronico, contrase�a);
				dataJugadores2.add(lin);
			}
			inUsuarios.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Genera un reporte que contiene la informaci�n de todos los jugadores
	 * @return
	 */
	private static String generarReporteActualDeUsuariosValidos() {
		String reporte = "";
		for (String x : dataJugadores2) {
			reporte += x + "\n";
		}
		return reporte;
	}

}
