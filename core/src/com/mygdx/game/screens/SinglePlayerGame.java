package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.entities.Cobra;
import com.mygdx.game.entities.Pickup.PPHeal;
import com.mygdx.game.entities.Pickup.PPShield;
import com.mygdx.game.entities.Pickup.PowerUp;
import com.mygdx.game.entities.SnakeSpawner;
import com.mygdx.game.HackathonRumble;
import com.mygdx.game.entities.Player;
import com.mygdx.game.entities.projectiles.Projectile;
import com.mygdx.game.handlers.WorldContactListener;
import com.mygdx.game.utils.HighScore;
import com.mygdx.game.utils.Utils;

import java.util.ArrayList;

import static com.mygdx.game.utils.Constants.PPM;

public class SinglePlayerGame implements Screen {
    private static final int SCALE = 3;
    private HackathonRumble parent;

    // Map handling
    private final TiledMap map;
    private final int tileWidth, tileHeight, mapWidth, mapHeight;
    private final OrthogonalTiledMapRenderer tmrenderer;
    private final Box2DDebugRenderer debugRenderer;
    private final OrthographicCamera gameCamera;

    //World and listeners
    private final World world;
    WorldContactListener worldContactListener;

    //rendering
    private final SpriteBatch sBatch;
    private final Hud hud;

    // player
    private Player player;
    private int level = 1;

    //Spawners
    private ArrayList<Cobra> cobras = new ArrayList<>();
    private float cobraSpawnCD = 3f;
    private float cobraSpawnTimer = 0;
    private ArrayList<SnakeSpawner> snakeSpawners;
    private ArrayList<Projectile> snakes;
    private float snakeSpawnTimer = 0;
    private float snakeSpawnCD = 2f;

    private ArrayList<PowerUp> powers;

    private float powerSpawnTimer = 0;
    private float PowerCD = 7f;

    private HighScore[] scores;

    // Spawns get progressively faster based on gametime / level threshold
    private float gameTime = 0;
    private final float LEVEL_THRESHOLD = 30;

    //sound

    private Music theme;

    public SinglePlayerGame(HackathonRumble parent){
        this.parent = parent;
        scores = HighScore.deSerialize(true);

        //TiledMap
        map = new TmxMapLoader().load("map/Arena1.tmx");

        tileWidth = map.getProperties().get("tilewidth", Integer.class);
        tileHeight = map.getProperties().get("tileheight", Integer.class);
        mapWidth = map.getProperties().get("width", Integer.class);
        mapHeight = map.getProperties().get("height", Integer.class);
        tmrenderer = new OrthogonalTiledMapRenderer(map);
        debugRenderer = new Box2DDebugRenderer();


        // World logic
        world = new World(new Vector2(0,0),false);
        Utils.parseTiledObjectLayer(world, map.getLayers().get("collision-layer").getObjects());
        world.setContactListener(new WorldContactListener());

        //sound
        theme = Gdx.audio.newMusic(Gdx.files.internal("AuthenticOctopusGameGrindyourGears.mp3"));
        theme.setLooping(true);
        theme.play();

        // renderer
        sBatch = new SpriteBatch();
        hud = new Hud(sBatch);

        //player
        player = new Player(world, 100, 100);
        Gdx.input.setInputProcessor(player);

        //cameras
        float mapW = Gdx.graphics.getWidth();
        float mapH = Gdx.graphics.getHeight();
        gameCamera = new OrthographicCamera();
        gameCamera.setToOrtho(false, mapW / SCALE, mapH / SCALE);

        // populate spawners
        populateSnakeSpawner();
        snakes = new ArrayList<>();
        powers = new ArrayList<>();
    }

    private void update(float delta){
        world.step(1/60f, 6,2);
        gameTime += delta;
        if(gameTime>= LEVEL_THRESHOLD)
            updateLevel();

        //Player
        player.update(delta);
        if(player.getHealth() <=0)
            endGame();
        player.addPoints(delta);
        hud.updateScore((int)player.getPoints());
        hud.updateLives((int)player.getHealth());



        //Spawners
        cobraSpawnTimer += delta;
//         On cool down spawn Cobra from random spawner
        if(cobraSpawnTimer >= cobraSpawnCD){
            spawnCobra();
            cobraSpawnTimer -= cobraSpawnCD;
        }
        Utils.iterateCobras(world, delta,cobras);

        for (SnakeSpawner spawner : snakeSpawners) {
            spawner.update(delta);
        }


        // On cool down spawn snake from random spawner
        if(snakeSpawnTimer >= snakeSpawnCD){
//            snakes.add(snakeSpawners.get( (int)(Math.random() * (snakeSpawners.size()))).getNewSnake(world));
            snakeSpawnTimer -= snakeSpawnCD;
        }
        Utils.iterateProjectiles(world,delta, snakes);

        Utils.cleanPowerUps(world,powers);

        powerSpawnTimer += delta;
        if(powerSpawnTimer >= PowerCD){
            spawnPowerUp();
            powerSpawnTimer -= powerSpawnTimer;
        }


    }

    @Override
    public void render(float delta) {
//      run update logic
        update(delta);
        //Update camera for rendering game related stuff
        tmrenderer.setView(gameCamera);
        updateCamera();

//      clear screen before rerender
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameCamera.update();
        sBatch.setProjectionMatrix(gameCamera.combined);

        // render tilemap
        tmrenderer.setView(gameCamera);
        tmrenderer.render();

        //Begin render of sprites
        sBatch.begin();
            player.render(sBatch);
            for (Cobra c : cobras) {
                c.render(sBatch);
            }
            for (SnakeSpawner spawner : snakeSpawners) {
                spawner.render(sBatch);
            }
            for(PowerUp pp : powers){
                pp.render(sBatch);
            }
        sBatch.end();

        //set view and Render Hud
        sBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


        // Render debug view
//        debugRenderer.render(world, gameCamera.combined.scl(PPM));
    }
    public void updateCamera(){
        Vector3 position = gameCamera.position;
        position.x = player.getPosition().x * PPM;
        position.y = player.getPosition().y * PPM;
        gameCamera.position.set(position);

        gameCamera.update();
    }


    @Override
    public void dispose() {
        world.dispose();
        sBatch.dispose();
        tmrenderer.dispose();
        map.dispose();
        theme.dispose();

        player.getSpriteSheet().dispose();
        for(Projectile p :player.getProjectiles()){
            p.getTexture().dispose();
        }
        for(PowerUp pp : powers){
            pp.getTexture().dispose();
        }
        for(SnakeSpawner spawner : snakeSpawners){
            spawner.dispose();
        }
    }

    //Places a boulder spawner in each corner
    private void populateSnakeSpawner(){
        snakeSpawners = new ArrayList<>();
        snakeSpawners.add(new SnakeSpawner(world,tileWidth+tileWidth/2,tileHeight*2+tileHeight/2,0));
        snakeSpawners.add(new SnakeSpawner(world,tileWidth * mapWidth -tileWidth*2-tileWidth/2,tileHeight*2+tileHeight/2,90));
        snakeSpawners.add(new SnakeSpawner(world,mapWidth * tileWidth -tileWidth*2-tileWidth/2, mapHeight * tileHeight-tileHeight-tileHeight/2,180));
        snakeSpawners.add(new SnakeSpawner(world,tileWidth+tileWidth/2,mapHeight * tileHeight-tileHeight-tileHeight/2,270));
    }

    // Spawn random PowerUp on a random spot within map, excluding outer 2 tiles around the map
    private void spawnPowerUp(){
        float posX = (float)((Math.random() * ((mapWidth-4)*tileWidth)) +tileWidth*2);
        float posY = (float)((Math.random() * ((mapHeight-4)*tileHeight)) +tileHeight*2);
        int id = (int)(Math.random()*2);
        switch (id) {
            case 0 : {
                powers.add(new PPHeal(world, "HealingCheese", posX, posY));
                break;
            }
            case 1 : {
                powers.add(new PPShield(world, "ShieldingCheese", posX, posY));
                break;
            }
        }
    }
    private void spawnCobra(){
        float posX = (float)((Math.random() * ((mapWidth-4)*tileWidth)) +tileWidth*2);
        float posY = (float)((Math.random() * ((mapHeight-4)*tileHeight)) +tileHeight*2);
        cobras.add(new Cobra(world,posX, posY, player));
    }

    private void updateLevel(){
        level = (int)(gameTime / LEVEL_THRESHOLD);
        cobraSpawnCD *= 0.8f;
        snakeSpawnCD *= 0.8f;
        for (SnakeSpawner spawner : snakeSpawners){
            spawner.setSpawnCDMax(spawner.getSpawnCDMax() * 0.8f);
            spawner.setSpawnCDMin(spawner.getSpawnCDMin() * 0.8f);
        }

        PowerCD *= 1.2f;
        gameTime = 0;
        System.out.println("Stage Level Up");
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    private void endGame(){
        // Serialize if score is good enough
        HighScore.serialize(scores, (int) player.getPoints());
        parent.changeScreen(ScreenType.HIGH_SCORE);
        theme.stop();
    }
}