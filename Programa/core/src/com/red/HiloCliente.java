package com.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.game.MainGame;

public class HiloCliente extends Thread {

	private InetAddress ip;
	private DatagramSocket socket;
	private MainGame app;

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

		System.out.println(mensaje);

		int puerto = packet.getPort();
		InetAddress ip = packet.getAddress();

		String[] mensajeCompuesto = mensaje.split("/");

		if (mensajeCompuesto.length > 1) {
			if (mensajeCompuesto[0].equals("player")) {
				System.out.println("Sos el cliente nº " + mensajeCompuesto[1]);
				app.nroCliente = Integer.valueOf(mensajeCompuesto[1]);
			}
			if (mensajeCompuesto[0].equals("arriba")) {
				if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverArriba();
				}
				if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverArriba();
				}
			}
			if (mensajeCompuesto[0].equals("abajo")) {
				if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverAbajo();
				}
				if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverAbajo();
				}
			}
			if (mensajeCompuesto[0].equals("derecha")) {
				if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverDerecha();
				}
				if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverDerecha();
				}
			}
			if (mensajeCompuesto[0].equals("izquierda")) {
				if (mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverIzquierda();
				}
				if (mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverIzquierda();
				}
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

	public void enviarDatos(String mensaje) {

		byte[] data = mensaje.getBytes();
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, 9000);
		try {

			socket.send(packet);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
