package com.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.game.MainGame;

public class HiloServidor extends Thread{

	private DatagramSocket socket;
	
	int nroConexiones = 0;
	
	public InetAddress ip1; public int puerto1;
	public InetAddress ip2; public int puerto2;
	
	private MainGame app;
	
	public HiloServidor(MainGame app){
		
		this.app = app;
		
		try {
			this.socket = new DatagramSocket(9000);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
	}
	
	public void run(){
		while(true){
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
		
		if(mensajeCompuesto.length == 1) {
			if(mensaje.equals("conexion")) {
				System.out.println("Conexion");
				if(nroConexiones==0) {
					ip1 = ip;
					puerto1 = puerto;
					nroConexiones++;
					this.enviarDatos("player/1", ip1, puerto1);
				} else if(nroConexiones==1) {
					ip2 = ip;
					puerto2 = puerto;
					nroConexiones++;
					this.enviarDatos("player/2", ip2, puerto2);
					this.enviarDatos("empieza", ip1, puerto1);
					this.enviarDatos("empieza", ip2, puerto2);
					this.app.menuScreen.empiezaJuego = true;
				}
			}
		}
		
		// arriba/1

		if(mensajeCompuesto.length>1) {
			if(mensajeCompuesto[0].equals("arriba")) {
				if(mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverArriba();
					this.enviarDatos(mensaje, ip2, puerto2);
				}
				if(mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverArriba();
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			if(mensajeCompuesto[0].equals("abajo")) {
				if(mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverAbajo();
					this.enviarDatos(mensaje, ip2, puerto2);
				}
				if(mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverAbajo();
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			if(mensajeCompuesto[0].equals("derecha")) {
				if(mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverDerecha();
					this.enviarDatos(mensaje, ip2, puerto2);
				}
				if(mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverDerecha();
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			if(mensajeCompuesto[0].equals("izquierda")) {
				if(mensajeCompuesto[1].equals("1")) { // Mover jugador 1
					app.gameScreen.player.moverIzquierda();
					this.enviarDatos(mensaje, ip2, puerto2);
				}
				if(mensajeCompuesto[1].equals("2")) { // Mover jugador 2
					app.gameScreen.player2.moverIzquierda();
					this.enviarDatos(mensaje, ip1, puerto1);
				}
			}
			
		}
//			
//			if(mensajeCompuesto[0].equals("posPad2")) {
//				this.enviarDatos(m, ip1, puerto1);
//				app.pantallaJuego.pad2.setPos_y(Integer.valueOf(mensajeCompuesto[1]));
//			}
//		} else {
//			if(m.equals("conexion")) {
//				if(nroConexiones==0) {
//					ip1 = ip;
//					puerto1 = puerto;
//					nroConexiones++;
//					this.enviarDatos("player/1", ip1, puerto1);
//				} else if(nroConexiones==1) {
//					ip2 = ip;
//					puerto2 = puerto;
//					nroConexiones++;
//					this.enviarDatos("player/2", ip2, puerto2);
//					this.enviarDatos("empieza", ip1, puerto1);
//					this.enviarDatos("empieza", ip2, puerto2);
//					this.app.pantallaMenu.empiezaJuego = true;
//				}
//			}
//		}
		
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
