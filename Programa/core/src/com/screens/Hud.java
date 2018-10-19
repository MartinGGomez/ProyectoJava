package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
		Label player = new Label("Coxne", new LabelStyle(new BitmapFont(), Color.WHITE));
		player.setPosition(image.getWidth() - 140, image.getHeight() - 80);

		//
		stage.setDebugAll(true);
		Array items = new Array();
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
		scrollPane.setOverscroll(true, true);
		scrollPane.setBounds(10, 10, 400, 300);
		scrollPane.setScrollBarPositions(false, true);
		scrollPane.setScrollbarsOnTop(true);
		scrollPane.setFadeScrollBars(true);
		scrollPane.setScrollingDisabled(false, false);
		scrollPane.setForceScroll(true, true);
		scrollPane.scrollTo(0, 0, 0, 0);
		scrollPane.setSmoothScrolling(true);
		scrollPane.toFront();
		scrollPane.setScale(0.5f);
		scrollPane.setPosition(25, 400);


		stage.addActor(image);
		stage.addActor(scrollPane);
		stage.addActor(player);

	}

	@Override
	public void dispose() {
		stage.dispose();

	}

}
