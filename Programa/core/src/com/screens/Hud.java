package com.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {

    public Stage stage;
    private Viewport viewport;
    private Texture texture;
    public static final float HUD_HALF_HEIGHT = Gdx.graphics.getHeight() - 346;
    public static final float HUD_HALF_WIDTH = 273.5f;

    public Hud() {
    	
    	viewport = new FitViewport(800, 600, new OrthographicCamera());
        stage = new Stage(viewport, new SpriteBatch());
        
        texture = new Texture("hud.png");
     
        Image image = new Image(texture);
        image.setFillParent(true);
        image.setPosition(0, 0);
        image.setSize(texture.getWidth(), texture.getHeight());
        Label player = new Label("Coxne" , new LabelStyle(new BitmapFont(), Color.WHITE));
        player.setPosition(image.getWidth() - 140, image.getHeight() - 80);    

        stage.addActor(image);
        stage.addActor(player);
    }
    
    
	@Override
	public void dispose() {
		stage.dispose();
		
	}
    
    
	
}
