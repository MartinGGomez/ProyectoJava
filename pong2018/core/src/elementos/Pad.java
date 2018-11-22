package elementos;

import utilidades.Configuracion;
import utilidades.Recursos;

public class Pad extends Elemento{

	public Pad(int pos_x, int pos_y) {
		
		super(Recursos.PAD, pos_x, pos_y);		
		
	}
	
	public void abajo(){
		if(this.pos_y > 20){
			super.abajo();
		}
	}
	
	public void arriba(){
		if((this.pos_y+this.getSprite().getHeight()) < (Configuracion.ALTO-20)){
			super.arriba();
		}
	}
	
	public void setPos_y(int pos_y){
		this.pos_y = pos_y;
	}
	
}
