package red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import com.mygdx.pong2018.Pong2018;

public class HiloServidor extends Thread{

	private DatagramSocket socket;
	
	int nroConexiones = 0;
	
	public InetAddress ip1; public int puerto1;
	public InetAddress ip2; public int puerto2;
	
	private Pong2018 app;
	
	public HiloServidor(Pong2018 app){
		
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
		
		String m = new String(dp.getData()).trim();
		
		int puerto = dp.getPort();
		InetAddress ip = dp.getAddress();
		
		String[] mensajeCompuesto = m.split("/");
		
		if(mensajeCompuesto.length>1) {
			if(mensajeCompuesto[0].equals("posPad1")) {
				this.enviarDatos(m, ip2, puerto2);
				app.pantallaJuego.pad1.setPos_y(Integer.valueOf(mensajeCompuesto[1]));
			}
			
			if(mensajeCompuesto[0].equals("posPad2")) {
				this.enviarDatos(m, ip1, puerto1);
				app.pantallaJuego.pad2.setPos_y(Integer.valueOf(mensajeCompuesto[1]));
			}
		} else {
			if(m.equals("conexion")) {
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
					this.app.pantallaMenu.empiezaJuego = true;
				}
			}
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
