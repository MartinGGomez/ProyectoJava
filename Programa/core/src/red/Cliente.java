package red;

import com.game.MainGame;

public class Cliente {

	public HiloCliente hiloCliente;
	
	public Cliente(MainGame app) {
		hiloCliente = new HiloCliente(app);
		hiloCliente.start();
	}
	
}
