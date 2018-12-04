package com.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.actors.Enemy;
import com.actors.states.PlayerStates;
import com.game.MainGame;
import com.screens.GameScreen;

public class HiloCliente extends Thread {

	private InetAddress ip;
	private DatagramSocket socket;
	public MainGame app;

	private String ipServer = "192.168.";

	private boolean encontrado = false;

	public HiloCliente(MainGame app) {

		this.app = app;

		try {
			this.socket = new DatagramSocket();
			int subRed = 0;
			int ultimoDigito = 0;
			// this.ip = InetAddress.getByName("192.168.0.141"); // direccion del server
			// Busqueda automatica
			this.ip = InetAddress.getByName("192.168.0.141");
			// do {
			// this.ip = InetAddress.getByName(ipServer+subRed+"."+ultimoDigito); //
			// direccion del server
			// System.out.println("PING A " + ipServer+subRed+"."+ultimoDigito );
			// this.enviarDatos("ping");
			// ultimoDigito++;
			// if(ultimoDigito == 255) {
			// ultimoDigito = 0;
			// subRed++;
			// }
			// if(subRed == 255 && ultimoDigito == 254) {
			// encontrado = true;
			// }
			// }while(!encontrado);

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
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
				e.printStackTrace();
			}
			procesarMensaje(packet);

		}
	}

	private void procesarMensaje(DatagramPacket packet) {
		String mensaje = new String(packet.getData()).trim();

		// System.out.println("Cliente recibe: " + mensaje);

		int puerto = packet.getPort();
		InetAddress ip = packet.getAddress();

		String[] mensajeCompuesto = mensaje.split("/");

		if (mensajeCompuesto.length > 1) {
			if (mensajeCompuesto[0].equals("player")) {
				System.out.println("Sos el cliente nº " + mensajeCompuesto[1]);
				app.nroCliente = Integer.valueOf(mensajeCompuesto[1]);
			}
			manejarMovimientos(mensajeCompuesto);
			//
			// atacarNPC/"+enemy.enemyIndex+"/"+attack.name/nroJugador

			if (mensajeCompuesto[0].equals("atacarNPC")) {
				int attackedBy = Integer.parseInt(mensajeCompuesto[3]);
				int enemyIndex = Integer.parseInt(mensajeCompuesto[1]);
				String attackName = mensajeCompuesto[2];
				app.gameScreen.copyAttack = true;
				app.gameScreen.attackPlayer = false;
				app.gameScreen.attackToCopyAttackedBy = attackedBy;
				app.gameScreen.attackToCopyEnemyIndex = enemyIndex;
				app.gameScreen.attackToCopyName = attackName;
			}

			// atacarPlayer/"+enemy.enemyIndex+"/"+attack.name+"/"+this.nroJugador
			if (mensajeCompuesto[0].equals("atacarPlayer")) {
				int attackedBy = Integer.parseInt(mensajeCompuesto[3]);
				int enemyIndex = Integer.parseInt(mensajeCompuesto[1]);
				String attackName = mensajeCompuesto[2];
				app.gameScreen.copyAttack = true;
				app.gameScreen.attackPlayer = true;
				app.gameScreen.attackToCopyAttackedBy = attackedBy;
				app.gameScreen.attackToCopyEnemyIndex = enemyIndex;
				app.gameScreen.attackToCopyName = attackName;
			}

			// pocionVida/"+this.nroJugador
			if (mensajeCompuesto[0].equals("pocionVida")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				app.gameScreen.tomarPocion(cliente, "Vida");
			}

			// pocionMana/"+this.nroJugador
			if (mensajeCompuesto[0].equals("pocionMana")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				app.gameScreen.tomarPocion(cliente, "Mana");
			}

			// cofre/enemyIndex/pocionesVida/pocionesMana/nroCliente
			if (mensajeCompuesto[0].equals("cofre")) {
				int cliente = Integer.parseInt(mensajeCompuesto[4]);
				int enemyIndex = Integer.parseInt(mensajeCompuesto[1]);
				int pocionesVida = Integer.parseInt(mensajeCompuesto[2]);
				int pocionesMana = Integer.parseInt(mensajeCompuesto[3]);
				Enemy enemy = app.gameScreen.getEnemyByIndex(enemyIndex);
				enemy.openChestFromNet(pocionesVida, pocionesMana);
			}

			// muerto/nroJugador
			if (mensajeCompuesto[0].equals("muerto")) {
				int cliente = Integer.parseInt(mensajeCompuesto[1]);
				if (cliente == 2) {
					System.out.println("Murio player 1");
					app.gameScreen.player.alive = false;
				} else {
					System.out.println("Murio player 2");
					app.gameScreen.player2.alive = false;
				}
			}
			
			if (mensajeCompuesto[0].equals("colisionandoEnemigo")) {
				app.gameScreen.colisionandoEnemigo = true;
				app.gameScreen.colisionandoConEnemigoNro = Integer.parseInt(mensajeCompuesto[1]);
				app.gameScreen.colisionandoConPlayerNro = Integer.parseInt(mensajeCompuesto[2]);
			}

		} else {
			if (mensaje.equals("empieza")) {
				app.menuScreen.empiezaJuego = true;
			}
			if (mensaje.equals("salir")) {
				System.exit(0);
			}

			if (mensaje.equals("recibido")) {
				this.encontrado = true;
				this.ip = ip;
				System.out.println("El servidor es: " + this.ip);
			}
			
			if (mensaje.equals("colisionandoJugador")) {
				app.gameScreen.colisionandoJugador = true;
			}
			if (mensaje.equals("colisionTerminada")) {
				app.gameScreen.colisionandoJugador = false;
			}
	
			if (mensaje.equals("terminoColisionEnemigo")) {
				app.gameScreen.colisionandoEnemigo = false;
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

		// pos/x/y/nroJugador/direccion/keyframe
		if (mensajeCompuesto[0].equals("pos")) {
			int cliente = Integer.parseInt(mensajeCompuesto[3]);
			float x = Float.parseFloat(mensajeCompuesto[1]);
			float y = Float.parseFloat(mensajeCompuesto[2]);
			PlayerStates direccion = PlayerStates.valueOf(mensajeCompuesto[4]);
			float frameIndex = Float.parseFloat(mensajeCompuesto[5]);
			if (cliente == 1) {
				app.gameScreen.player.x = x;
				app.gameScreen.player.y = y;
				app.gameScreen.player.currentState = direccion;
				app.gameScreen.player.frameIndex = frameIndex;
			} else {
				app.gameScreen.player2.x = x;
				app.gameScreen.player2.y = y;
				app.gameScreen.player2.currentState = direccion;
				app.gameScreen.player2.frameIndex = frameIndex;
			}
		}

		// posEnemy/x/y/enemyIndex/direccion/keyframe
		if (mensajeCompuesto[0].equals("posEnemy")) {
			int enemyIndex = Integer.parseInt(mensajeCompuesto[3]);
			float x = Float.parseFloat(mensajeCompuesto[1]);
			float y = Float.parseFloat(mensajeCompuesto[2]);
			PlayerStates direccion = PlayerStates.valueOf(mensajeCompuesto[4]);
			float frameIndex = Float.parseFloat(mensajeCompuesto[5]);
			boolean preventMove = Boolean.parseBoolean(mensajeCompuesto[6]);
			Enemy enemy = GameScreen.getEnemyByIndex(enemyIndex);
			enemy.x = x;
			enemy.y = y;
			enemy.currentState = direccion;
			enemy.frameIndex = frameIndex;
			System.out.println("prevent move " + preventMove);
			enemy.preventMove = preventMove;
		}

	}

	public void enviarDatos(String mensaje) {

		byte[] data = mensaje.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 9000);
		try {

			System.out.println("Se envio: " + mensaje);

			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
