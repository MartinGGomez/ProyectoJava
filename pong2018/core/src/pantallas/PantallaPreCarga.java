package pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.pong2018.Pong2018;

import utilidades.Configuracion;
import utilidades.Recursos;

public class PantallaPreCarga implements Screen {

	private Pong2018    app;
	private Texture texturaLogo;
	private Sprite  logo;
	private float   alpha = 0;
		
	public PantallaPreCarga(Pong2018 app) {
		this.app = app;
	}

	@Override
	public void show() {
		texturaLogo = new Texture(Recursos.LOGO);
		logo = new Sprite(texturaLogo);		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
	}

	private void update(float delta) {
		
		app.batch.enableBlending();
		
		if(alpha<1){
			Color c = app.batch.getColor();
			app.batch.setColor(c.r,c.g,c.b,alpha);
			alpha+=0.007f;
		} else {
			alpha+=0.007f;
			if(alpha>3){
				app.setScreen(app.pantallaMenu);
			}
		}
		
		app.batch.begin();
			app.batch.draw(logo,(Configuracion.ANCHO/2)-(logo.getWidth()/2), (Configuracion.ALTO/2) - (logo.getHeight()/2));
		app.batch.end();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		//texturaLogo.dispose();
	}

	
	
}
