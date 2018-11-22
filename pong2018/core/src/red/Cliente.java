package red;

import com.mygdx.pong2018.Pong2018;

public class Cliente {

	public HiloCliente hc;
	
	public Cliente(Pong2018 app) {
		hc = new HiloCliente(app);
		hc.start();
	}
	
}
