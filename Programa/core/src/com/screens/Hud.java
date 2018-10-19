package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class Hud implements Disposable {

	private MainGame game;
	public Stage stage;
	private Viewport viewport;
	private Texture texture;
	public static final float HUD_HALF_HEIGHT = Gdx.graphics.getHeight() - 346;
	public static final float HUD_HALF_WIDTH = 273.5f;
	private Skin skin;
	private List list;
	private ScrollPane scrollPane;
	Array items = new Array();

	public Hud(MainGame game) {
		this.game = game;

		viewport = new FitViewport(800, 600, new OrthographicCamera());
		stage = new Stage(viewport, new SpriteBatch());

		texture = new Texture("hud.png");

		this.skin = new Skin();
		this.skin.addRegions(this.game.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
		this.skin.add("default-font", this.game.font);
		this.skin.load(Gdx.files.internal("ui/uiskin.json"));

		Image image = new Image(texture);
		image.setFillParent(true);
		image.setPosition(0, 0);
		image.setSize(texture.getWidth(), texture.getHeight());
		image.toBack();
		//
		Label player = new Label("Coxne", new LabelStyle(new BitmapFont(), Color.WHITE));
		player.setPosition(image.getWidth() - 140, image.getHeight() - 80);
		TextButton testButton = new TextButton("CLICK", skin);
		testButton.setPosition(200, 200);
		testButton.addListener(new ClickListener() {
			 @Override
			    public void clicked(InputEvent event, float x, float y) {
			        newItem();
			 }
		});
		//
		stage.setDebugAll(true);
		
		list = new List<String>(skin);
		list.setFillParent(false);
		

		items.add("Item 1");
		items.add("Item 2");
		items.add("Item 3");
		items.add("Item 4");
		items.add("Item 5");
		items.add("Item 6");
		items.add("Item 7");
		items.add("Item 8");
		items.add("Item 9");
		items.add("Item 10");

		list.setItems(items);
		list.setColor(Color.BLACK);
		
		
		scrollPane = new ScrollPane(list);
//		scrollPane.setBounds(0, 0, 200, 500);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setHeight(190);
        scrollPane.setWidth(1100);
        scrollPane.setForceScroll(true, true);
        scrollPane.setPosition(10, Gdx.graphics.getHeight()-105);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
       
        
        Gdx.input.setInputProcessor(stage);
		stage.addActor(image);
		stage.addActor(scrollPane);
		stage.addActor(player);
		stage.addActor(testButton);

	}

	public void newItem() {
		items.add("Nuevo");
		list.setItems(items);
	}
	
	@Override
	public void dispose() {
		stage.dispose();

	}

}
