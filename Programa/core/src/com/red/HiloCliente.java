package com.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.actors.Enemy;
import com.attacks.Attack;
import com.game.MainGame;
import com.screens.GameScreen;

public class HiloCliente extends Thread {

	private InetAddress ip;
	private DatagramSocket socket;
	public MainGame app;
	
	private boolean playerStop = false;

	public HiloCliente(MainGame app) {

		this.app = app;

		try {
			// Buscarla automaticamente
			this.socket = new DatagramSocket();
			String ipServer = "192.168.0.141";
			this.ip = InetAddress.getByName(ipServer); // direccion del server

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

		System.out.println("Cliente recibe: " + mensaje);

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

			if(mensajeCompuesto[0].equals("atacarNPC")) {
				int attackedBy = Integer.parseInt(mensajeCompuesto[3]);
				int enemyIndex = Integer.parseInt(mensajeCompuesto[1]);
				String attackName = mensajeCompuesto[2];
				app.gameScreen.copyAttack = true;
				app.gameScreen.attackToCopyAttackedBy = attackedBy;
				app.gameScreen.attackToCopyEnemyIndex = enemyIndex;
				app.gameScreen.attackToCopyName = attackName;
			}
			
			
		} else {
			if (mensaje.equals("empieza")) {
				app.menuScreen.empiezaJuego = true;
			}
			
		}

		//
		// if(mensajeCompuesto[0].equals("posPad1")) {
		// app.pantallaJuego.pad1.setPos_y(Integer.valueOf(mensajeCompuesto[1]));
		// }
		//
		// if(mensajeCompuesto[0].equals("posPad2")) {
		// app.pantallaJuego.pad2.setPos_y(Integer.valueOf(mensajeCompuesto[1]));
		// }
		//
		// if(mensajeCompuesto[0].equals("actualizarPelota")) {
		// app.pantallaJuego.pelota.setPos(Integer.valueOf(mensajeCompuesto[1]),Integer.valueOf(mensajeCompuesto[2]));
		// }
		//
		// if(mensajeCompuesto[0].equals("punto")) {
		// app.pantallaJuego.pelota.fx.punto();
		// app.pantallaJuego.pelota.excelent.play();
		// System.out.println("Procesar punto");
		// app.pantallaJuego.procesarPunto();
		// if(mensajeCompuesto[1].equals("1")) {
		// app.pantallaJuego.pelota.p1.actualizarPuntaje();
		// }
		// if(mensajeCompuesto[1].equals("2")) {
		// app.pantallaJuego.pelota.p2.actualizarPuntaje();
		// }
		// }
		//
		//

		//
		// if(m.equals("ready")) {
		// app.pantallaJuego.resto = true;
		// app.pantallaJuego.preparados = true;
		// app.pantallaJuego.ya = false;
		// app.pantallaJuego.readyrep = false;
		// app.pantallaJuego.gorep = false;
		// }
		// }
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
			playerStop = true;
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
