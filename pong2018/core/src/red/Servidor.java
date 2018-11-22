package red;

import com.mygdx.pong2018.Pong2018;

public class Servidor {

	public HiloServidor hs;
	
	public Servidor(Pong2018 app) {
		hs = new HiloServidor(app);
		hs.start();
	}
	
}
