package com.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.game.MainGame;

public class HiloServidor extends Thread {

	private DatagramSocket socket;

	int nroConexiones = 0;

	public InetAddress ip1;
	public int puerto1;
	public InetAddress ip2;
	public int puerto2;

	public MainGame app;

	public HiloServidor(MainGame app) {

		this.app = app;

		try {
			this.socket = new DatagramSocket(9000);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
			}
			procesarMensaje(packet);
		}
	}

	private void procesarMensaje(DatagramPacket dp) {

		String mensaje = new String(dp.getData()).trim();

		int puerto = dp.getPort();
		InetAddress ip = dp.getAddress();

		String[] mensajeCompuesto = mensaje.split("/");

		System.out.println("SERVIDOR RECIBE: " + mensaje);

		if (mensajeCompuesto.length == 1) {
			if (mensaje.equals("conexion")) {
				System.out.println("Conexion");
				if (nroConexiones == 0) {
					ip1 = ip;
					puerto1 = puerto;
					nroConexiones++;
					this.enviarDatos("player/1", ip1, puerto1);
				} else if (nroConexiones == 1) {
					ip2 = ip;
					puerto2 = puerto;
					nroConexiones++;
					this.app.menuScreen.esperando = true;
					this.app.menuScreen.empiezaJuego = true;
					this.enviarDatos("player/2", ip2, puerto2);
					this.enviarDatos("empieza", ip1, puerto1);
					this.enviarDatos("empieza", ip2, puerto2);
					//
				}
			}
			
			if(mensaje.equals("salir")) {
				this.enviarDatosATodos(mensaje);
				System.exit(0);
			}
			
			if(mensaje.equals("ping")) {
				this.enviarDatos("recibido", ip, puerto);
			}
		}

		//

		if (mensajeCompuesto.length > 1) {
			this.manejarMovimientos(mensajeCompuesto);
			// atacarNPC/"+enemy.enemyIndex+"/"+attack.name+"/"+nroJugador
			if (mensajeCompuesto[0].equals("atacarNPC")) {
				int cliente = Integer.parseInt(mensajeCompuesto[3]);
				int attackedBy = Integer.parseInt(mensajeCompuesto[3]);
				int enemyIndex = Integer.parseInt(mensajeCompuesto[1]);
				String attackName = mensajeCompuesto[2];
				app.gameScreen.copyAttack = true;
				app.gameScreen.attackPlayer = false;
				app.gameScreen.attackToCopyAttackedBy = attackedBy;
				app.gameScreen.attackToCopyEnemyIndex = enemyIndex;
				app.gameScreen.attackToCopyName = attackName;
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}

			// atacarPlayer/"+enemy.enemyIndex+"/"+attack.name+"/"+this.nroJugador
			if (mensajeCompuesto[0].equals("atacarPlayer")) {
				int cliente = Integer.parseInt(mensajeCompuesto[3]);
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}

			// pocionVida/"+this.nroJugador
			if (mensajeCompuesto[0].equals("pocionVida")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}

			// pocionMana/"+this.nroJugador
			if (mensajeCompuesto[0].equals("pocionMana")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			
			// cofre/enemyIndex/pocionesVida/pocionesMana/nroCliente
			if(mensajeCompuesto[0].equals("cofre")) {
				int cliente = Integer.parseInt(mensajeCompuesto[4]);
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			
			// muerto/nroJugador
			if(mensajeCompuesto[0].equals("muerto")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				if (cliente == 1) {
					this.enviarDatos(mensaje, ip2, puerto2);
				} else {
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			

		}
	

	}
	
	private void manejarMovimientos(String[] mensajeCompuesto) {
		if (mensajeCompuesto[0].equals("arriba")) {
			if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
				app.gameScreen.player.arriba = true;
				app.gameScreen.player.stop = false;
				
			}
			if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
				app.gameScreen.player2.arriba = true;
				app.gameScreen.player2.stop = false;
			}
		}
		if (mensajeCompuesto[0].equals("abajo")) {
			if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
				app.gameScreen.player.abajo = true;
				app.gameScreen.player.stop = false;
			}
			if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
				app.gameScreen.player2.abajo = true;
				app.gameScreen.player2.stop = false;
			}
		}
		if (mensajeCompuesto[0].equals("derecha")) {
			if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
				app.gameScreen.player.derecha = true;
				app.gameScreen.player.stop = false;
			}
			if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
				app.gameScreen.player2.derecha = true;
				app.gameScreen.player2.stop = false;
			}
		}
		if (mensajeCompuesto[0].equals("izquierda")) {
			if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
				app.gameScreen.player.izquierda = true;
				app.gameScreen.player.stop = false;
			}
			if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
				app.gameScreen.player2.izquierda = true;
				app.gameScreen.player2.stop = false;
			}
		}

		if (mensajeCompuesto[0].equals("stop")) {
			if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
				app.gameScreen.player.arriba = false;
				app.gameScreen.player.stop = true;
			}
			if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
				app.gameScreen.player2.arriba = false;
				app.gameScreen.player2.stop = true;
			}
		}
	}

	public void enviarDatosATodos(String mensaje) {
		byte[] data = mensaje.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip1, puerto1);
		DatagramPacket packet2 = new DatagramPacket(data, data.length, ip2, puerto2);
		try {
			socket.send(packet);
			socket.send(packet2);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void enviarDatos(String mensaje, InetAddress ip, int puerto) {

		byte[] data = mensaje.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, puerto);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
