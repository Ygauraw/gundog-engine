/**
 * Copyright (C) 2013 Gundog Studios LLC.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.godsandtowers.graphics;

import static com.godsandtowers.sprites.BaseCreature.ANGEL;
import static com.godsandtowers.sprites.BaseCreature.DRAGON;
import static com.godsandtowers.sprites.BaseCreature.DRAKE;
import static com.godsandtowers.sprites.BaseCreature.DWARF;
import static com.godsandtowers.sprites.BaseCreature.EAGLE;
import static com.godsandtowers.sprites.BaseCreature.EARTH_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.ELF_WIZARD;
import static com.godsandtowers.sprites.BaseCreature.ELVISH_ARCHER;
import static com.godsandtowers.sprites.BaseCreature.ELVISH_ARCHER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.FALCON;
import static com.godsandtowers.sprites.BaseCreature.FALLEN_ANGEL;
import static com.godsandtowers.sprites.BaseCreature.FEMALE_ELF_MAGE;
import static com.godsandtowers.sprites.BaseCreature.FEMALE_NECROMANCER;
import static com.godsandtowers.sprites.BaseCreature.FIERY_BEAR;
import static com.godsandtowers.sprites.BaseCreature.FIRE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.FROZEN_SOLDIER;
import static com.godsandtowers.sprites.BaseCreature.FROZEN_SOLDIER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.GHOUL;
import static com.godsandtowers.sprites.BaseCreature.GNOME;
import static com.godsandtowers.sprites.BaseCreature.GRIZZLY_BEAR;
import static com.godsandtowers.sprites.BaseCreature.HALFLING;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_ARCHER;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_ARCHER_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_MAGE;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_NECROMANCER;
import static com.godsandtowers.sprites.BaseCreature.HUMAN_WITCH;
import static com.godsandtowers.sprites.BaseCreature.ICE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.KOBOLD;
import static com.godsandtowers.sprites.BaseCreature.LIFE_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.LIGHTNING_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.MUMMY;
import static com.godsandtowers.sprites.BaseCreature.MUMMY_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.PANDA_BEAR;
import static com.godsandtowers.sprites.BaseCreature.POLAR_BEAR;
import static com.godsandtowers.sprites.BaseCreature.SERAPHIM;
import static com.godsandtowers.sprites.BaseCreature.SUCCUBUS;
import static com.godsandtowers.sprites.BaseCreature.TROLL;
import static com.godsandtowers.sprites.BaseCreature.UNDEAD_GOLEM;
import static com.godsandtowers.sprites.BaseCreature.ZEALOT;
import static com.godsandtowers.sprites.BaseCreature.ZEALOT_HORSEMAN;
import static com.godsandtowers.sprites.BaseCreature.ZOMBIE;
import static com.godsandtowers.sprites.BaseCreature.ZOMBIE_HORSEMAN;
import static com.godsandtowers.sprites.BaseTower.ANGEL_STATUE;
import static com.godsandtowers.sprites.BaseTower.DIRT_EMITTER;
import static com.godsandtowers.sprites.BaseTower.EARTH_BALLISTA;
import static com.godsandtowers.sprites.BaseTower.EARTH_CRYSTALLIZER;
import static com.godsandtowers.sprites.BaseTower.EARTH_PILLAR;
import static com.godsandtowers.sprites.BaseTower.ENERGY_CANNON;
import static com.godsandtowers.sprites.BaseTower.FIRE_EMITTER;
import static com.godsandtowers.sprites.BaseTower.FIRE_PILLAR;
import static com.godsandtowers.sprites.BaseTower.FLAME_BALLISTA;
import static com.godsandtowers.sprites.BaseTower.FLAME_BLADE;
import static com.godsandtowers.sprites.BaseTower.FLAME_CANNON;
import static com.godsandtowers.sprites.BaseTower.FLAME_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.FLAME_CRYSTALLIZER;
import static com.godsandtowers.sprites.BaseTower.FLAME_WHIP;
import static com.godsandtowers.sprites.BaseTower.GARGOYLE_STATUE;
import static com.godsandtowers.sprites.BaseTower.GRIFFIN_STATUE;
import static com.godsandtowers.sprites.BaseTower.HOLY_WATER_EMITTER;
import static com.godsandtowers.sprites.BaseTower.ICE_BALLISTA;
import static com.godsandtowers.sprites.BaseTower.ICE_BLADE;
import static com.godsandtowers.sprites.BaseTower.ICE_CRYSTALLIZER;
import static com.godsandtowers.sprites.BaseTower.LIFE_PILLAR;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_BLADE;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_CANNON;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_EMITTER;
import static com.godsandtowers.sprites.BaseTower.LIGHTNING_PILLAR;
import static com.godsandtowers.sprites.BaseTower.POISON_CANNON;
import static com.godsandtowers.sprites.BaseTower.ROCK_CANNON;
import static com.godsandtowers.sprites.BaseTower.STONE_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.TENTICAL_WHIP;
import static com.godsandtowers.sprites.BaseTower.TOXIC_GAS_EMITTER;
import static com.godsandtowers.sprites.BaseTower.UNDEAD_PILLAR;
import static com.godsandtowers.sprites.BaseTower.WATER_CANNON;
import static com.godsandtowers.sprites.BaseTower.WATER_CATAPULT;
import static com.godsandtowers.sprites.BaseTower.WATER_EMITTER;
import static com.godsandtowers.sprites.BaseTower.WATER_PILLAR;
import static com.godsandtowers.sprites.BaseTower.WATER_WHIP;
import static com.godsandtowers.sprites.MovingProjectile.EARTH_ARROW_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.EARTH_BALL_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.FIRE_ARROW_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.FIRE_BALL_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.ICE_ARROW_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.ICE_BALL_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.LIFE_BALL_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.UNDEAD_BALL_PROJECTILE;
import static com.godsandtowers.sprites.MovingProjectile.WIND_BALL_PROJECTILE;
import static com.godsandtowers.sprites.Races.DEATH;
import static com.godsandtowers.sprites.Races.EARTH;
import static com.godsandtowers.sprites.Races.FIRE;
import static com.godsandtowers.sprites.Races.ICE;
import static com.godsandtowers.sprites.Races.LIFE;
import static com.godsandtowers.sprites.Races.WIND;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import com.godsandtowers.core.grid.Square;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BuildingSphere;
import com.gundogstudios.gl.Actions;
import com.gundogstudios.gl.BaseModel;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.gl.GSModel;
import com.gundogstudios.gl.GSModelVBOs;
import com.gundogstudios.gl.ModelManager;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.gl.TextureManager;
import com.gundogstudios.gl.custom.Background;
import com.gundogstudios.gl.custom.Border;
import com.gundogstudios.gl.custom.Explosion;
import com.gundogstudios.gl.custom.MorphingBox;
import com.gundogstudios.gl.custom.Nova;
import com.gundogstudios.gl.custom.Plane;
import com.gundogstudios.gl.custom.SkyBox;
import com.gundogstudios.gl.custom.SpecialSquare;
import com.gundogstudios.gl.custom.Sphere;
import com.gundogstudios.gl.custom.Spray;
import com.gundogstudios.gl.custom.Wall;
import com.gundogstudios.modules.LogModule;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class ModelFetcher {

	private static final String TAG = "TDWModelManager";

	// MESHES
	private static final String ARCHER_HORSEMEN = "ArcherHorsemen";
	private static final String ARCHERS = "Archers";
	private static final String ARROW_PROJECTILES = "ArrowProjectiles";
	private static final String BALL_PROJECTILES = "BallProjectiles";
	private static final String BALLISTAS = "Ballistas";
	private static final String BASE = "Base";
	private static final String BEARS = "Bears";
	private static final String BIRDS = "Birds";
	private static final String BLADE = "Blade";
	private static final String CACTUS = "Cactus";
	private static final String CANNONS = "Cannons";
	private static final String CATAPULTS = "Catapults";
	private static final String CRYSTALLIZERS = "Crystallizers";
	private static final String DEAD_HORSEMEN = "DeadHorsemen";
	private static final String DEAD_SOLDIERS = "DeadSoldiers";
	private static final String DRAGONS = "Dragons";
	private static final String EMITTERS = "Emitters";
	private static final String FEMALE_ANGELS = "FemaleAngels";
	private static final String FEMALE_MAGES = "FemaleMages";
	private static final String GOLEMS = "Golems";
	private static final String HORSEMEN = "Horsemen";
	private static final String HORSES = "Horses";
	private static final String HUMANOIDS = "Humanoids";
	private static final String HUMANOID_BEASTS = "HumanoidBeasts";
	private static final String MAGES = "Mages";
	private static final String MALE_ANGELS = "MaleAngels";
	private static final String PILLARS = "Pillars";
	private static final String ROCK = "Rock";
	private static final String RUBBLE = "Rubble";
	private static final String SOLDIERS = "Soldiers";
	private static final String STATUES = "Statues";
	private static final String TREE = "Tree";
	private static final String WHIP = "Whip";

	// Textures
	private static final String HORSE = "horse";
	private static final String DEAD_HORSE = "dead_horse";
	private static final String FROZEN_HORSE = "frozen_horse";
	private static final String UNDEAD_RUBBLE = "undead_rubble";
	private static final String LIFE_RUBBLE = "life_rubble";
	private static final String EARTH_RUBBLE = "earth_rubble";
	private static final String WIND_RUBBLE = "wind_rubble";
	private static final String WATER_RUBBLE = "water_rubble";
	private static final String FIRE_RUBBLE = "fire_rubble";

	// Tilesets
	private static final String FOREST_PROPS = "forest_props";
	public static final String FOREST_BACKGROUND = "forest_ground";
	public static final String FOREST_ROCK = "forest_rock";
	public static final String FOREST_TREE = "forest_tree";
	public static final String FOREST_WALL = "forest_wall";

	private static final String WINTER_FOREST_PROPS = "winter_forest_props";
	public static final String WINTER_FOREST_BACKGROUND = "winter_forest_ground";
	public static final String WINTER_FOREST_ROCK = "winter_forest_rock";
	public static final String WINTER_FOREST_TREE = "winter_forest_tree";
	public static final String WINTER_FOREST_WALL = "winter_forest_wall";

	private static final String DESERT_PROPS = "desert_props";
	public static final String DESERT_BACKGROUND = "desert_ground";
	public static final String DESERT_ROCK = "desert_rock";
	public static final String DESERT_TREE = "desert_tree";
	public static final String DESERT_WALL = "desert_wall";

	private HashMap<String, Fetcher> modelFetchers;
	private ModelManager modelManager;
	private TextureManager textureManager;

	public ModelFetcher() {
		modelFetchers = new HashMap<String, Fetcher>();
		textureManager = new TextureManager();
		modelManager = new ModelManager();

		loadModelFetchers();
	}

	public void release() {
		modelManager.releaseVBOs();
		textureManager.releaseTextures();
	}

	public void loadCustomModels(int rows, int columns, String background, String wall) {

		modelFetchers.get(background).fetchModel(rows, columns);

		modelFetchers.get(Explosion.NAME).fetchModel();
		// modelFetchers.get(MorphingBox.NAME).fetchModel();
		// modelFetchers.get(Nova.NAME).fetchModel();
		modelFetchers.get(Plane.NAME).fetchModel();
		modelFetchers.get(SkyBox.NAME).fetchModel(rows, columns);
		modelFetchers.get(SpecialSquare.NAME).fetchModel(1f);
		modelFetchers.get(BuildingSphere.NAME).fetchModel();
		modelFetchers.get(Spray.NAME).fetchModel();
		modelFetchers.get(Wall.NAME).fetchModel(rows, columns);
		modelFetchers.get(Border.NAME).fetchModel(rows, columns, wall);
		for (BaseSpecial special : BaseSpecial.getSpecials()) {
			modelFetchers.get(special.getName()).fetchModel(rows, columns);
		}
	}

	public void loadVBOandTextures() {
		modelManager.generateVBOs();
		textureManager.generateTextureIDs();
	}

	public ArrayList<String> getAllModelNames() {
		return new ArrayList<String>(new TreeSet<String>(modelFetchers.keySet()));
	}

	public ArrayList<CombinedModel> getAllModels() {
		ArrayList<CombinedModel> models = new ArrayList<CombinedModel>();
		TreeSet<String> names = new TreeSet<String>(modelFetchers.keySet());
		LogModule logger = Modules.LOG;
		long bigstart = System.currentTimeMillis();
		for (String name : names) {
			long free = Runtime.getRuntime().freeMemory() / 1024;
			long total = Runtime.getRuntime().totalMemory() / 1024;
			long max = Runtime.getRuntime().maxMemory() / 1048576;
			logger.info(TAG, "Loading " + name + " Ram: f" + free + "KB t" + total + "KB m" + max + "MB");
			Fetcher fetcher = modelFetchers.get(name);
			try {
				models.add(fetcher.fetchModel());
			} catch (Exception e) {
				logger.info(TAG, "Failed to fetch: " + name + "\n" + e);
				e.printStackTrace();
			}
			long start = System.currentTimeMillis();
			logger.info(TAG, "Loading " + name + " took: " + (System.currentTimeMillis() - start) + " Ram: f" + free
					+ "KB t" + total + "KB m" + max + "MB");

		}
		logger.info(TAG, "Took: " + (System.currentTimeMillis() - bigstart) + " ms to load");

		return models;
	}

	public CombinedModel getModel(String name, Object... args) {
		if (name == null)
			return null;

		Fetcher fetcher = modelFetchers.get(name);

		if (fetcher == null)
			return null;

		return fetcher.fetchModel(args);
	}

	public CombinedModel getModel(Sprite sprite, Object... args) {

		String name = sprite.getModel();
		CombinedModel model = getModel(name, args);
		if (model != null && sprite.getLevel() > 0) {
			float scale = FastMath.min(1f, .7f + sprite.getLevel() * .02f);
			model.modifyXYZScale(scale);
			// model.setRGB(scale, scale, scale);
		}
		return model;

	}

	private void loadModelFetchers() {
		loadArchers();
		loadArcherHorsemen();
		loadBears();
		loadBirds();
		loadDeadHorsemen();
		loadDeadSoldiers();
		loadDragons();
		loadFemaleAngels();
		loadGolems();
		loadHorsemen();
		loadHumanoids();
		loadHumanoidBeasts();
		loadFemaleMages();
		loadMages();
		loadMaleAngels();
		loadSoldiers();

		loadBallistas();
		loadCrystallizers();
		loadWhips();
		loadCannon();
		loadCatapults();
		loadEmitters();
		loadPillars();
		loadStatues();
		loadBlades();

		loadProjectiles();
		loadBoards();
		loadSpecials();
		loadBoardFetchers();
		loadCustomModels();
	}

	private void addCreatureFetcher(String modelName, float scaleSize, String mesh) {
		modelFetchers.put(modelName, new CombinedFetcher(scaleSize, new GSFetcher(modelName, mesh, true)));
	}

	private void addCreatureFetcher(String modelName, float scaleSize, String textureOne, String meshOne,
			String textureTwo, String meshTwo) {
		modelFetchers.put(modelName, new CombinedFetcher(scaleSize, new GSFetcher(textureOne, meshOne, true),
				new GSFetcher(textureTwo, meshTwo, true)));
	}

	private void addPillarFetcher(String modelName, float scaleSize, String rubbleTexture, String mesh) {
		ActionFetcher rubbleFetcher = new ActionFetcher(rubbleTexture, RUBBLE, false, Actions.DEATH);
		ActionFetcher towerFetcher = new ActionFetcher(modelName, mesh, false, Actions.combineActions(Actions.IDLE,
				Actions.ATTACK));
		CombinedFetcher combinedFetcher = new CombinedFetcher(scaleSize, rubbleFetcher, towerFetcher);
		modelFetchers.put(modelName, combinedFetcher);
	}

	private void addEmitterFetcher(String modelName, float scaleSize, String rubbleTexture, String meshOne,
			boolean rotatesOne, String baseTexture, int race) {
		ActionFetcher rubbleFetcher = new ActionFetcher(rubbleTexture, RUBBLE, false, Actions.DEATH);
		ActionFetcher towerFetcher = new ActionFetcher(modelName, meshOne, rotatesOne, Actions.combineActions(
				Actions.IDLE, Actions.ATTACK));
		ActionFetcher baseFetcher = new ActionFetcher(baseTexture, BASE, false, Actions.combineActions(Actions.IDLE,
				Actions.ATTACK));
		CombinedFetcher combinedFetcher = new CombinedFetcher(scaleSize, rubbleFetcher, towerFetcher, baseFetcher);
		modelFetchers.put(modelName, new EmitterFetcher(combinedFetcher, race));
	}

	private void addTowerWithBaseAndAttackFetcher(String modelName, float scaleSize, String rubbleTexture, String mesh,
			String baseTexture) {
		ActionFetcher rubbleFetcher = new ActionFetcher(rubbleTexture, RUBBLE, false, Actions.DEATH);
		ActionFetcher baseFetcher = new ActionFetcher(baseTexture, BASE, false, Actions.combineActions(Actions.IDLE,
				Actions.ATTACK));
		GSFetcher towerFetcher = new GSFetcher(modelName, mesh, true);
		CombinedFetcher combinedFetcher = new CombinedFetcher(scaleSize, rubbleFetcher, towerFetcher, baseFetcher);
		modelFetchers.put(modelName, combinedFetcher);
	}

	private void loadArchers() {
		addCreatureFetcher(HUMAN_ARCHER, .0001f, ARCHERS);
		addCreatureFetcher(ELVISH_ARCHER, .0001f, ARCHERS);
	}

	private void loadArcherHorsemen() {
		addCreatureFetcher(HUMAN_ARCHER_HORSEMAN, .0001f, HUMAN_ARCHER, ARCHER_HORSEMEN, HORSE, HORSES);
		addCreatureFetcher(ELVISH_ARCHER_HORSEMAN, .0001f, ELVISH_ARCHER, ARCHER_HORSEMEN, HORSE, HORSES);
	}

	private void loadBears() {
		addCreatureFetcher(FIERY_BEAR, .0001f, BEARS);
		addCreatureFetcher(PANDA_BEAR, .0001f, BEARS);
		addCreatureFetcher(GRIZZLY_BEAR, .0001f, BEARS);
		addCreatureFetcher(POLAR_BEAR, .0001f, BEARS);
	}

	private void loadBirds() {
		addCreatureFetcher(EAGLE, .0001f, BIRDS);
		addCreatureFetcher(FALCON, .0001f, BIRDS);

	}

	private void loadDeadHorsemen() {
		addCreatureFetcher(MUMMY_HORSEMAN, .0001f, MUMMY, DEAD_HORSEMEN, DEAD_HORSE, HORSES);
		addCreatureFetcher(ZOMBIE_HORSEMAN, .0001f, ZOMBIE, DEAD_HORSEMEN, DEAD_HORSE, HORSES);

	}

	private void loadDeadSoldiers() {
		addCreatureFetcher(MUMMY, .0001f, DEAD_SOLDIERS);
		addCreatureFetcher(ZOMBIE, .0001f, DEAD_SOLDIERS);
	}

	private void loadDragons() {
		addCreatureFetcher(DRAGON, .0001f, DRAGONS);
		addCreatureFetcher(DRAKE, .0001f, DRAGONS);
	}

	private void loadFemaleAngels() {
		addCreatureFetcher(SUCCUBUS, .0001f, FEMALE_ANGELS);
		addCreatureFetcher(SERAPHIM, .0001f, FEMALE_ANGELS);
	}

	private void loadGolems() {
		addCreatureFetcher(FIRE_GOLEM, .0001f, GOLEMS);
		addCreatureFetcher(LIGHTNING_GOLEM, .0001f, GOLEMS);
		addCreatureFetcher(EARTH_GOLEM, .0001f, GOLEMS);
		addCreatureFetcher(ICE_GOLEM, .0001f, GOLEMS);
		addCreatureFetcher(LIFE_GOLEM, .0001f, GOLEMS);
		addCreatureFetcher(UNDEAD_GOLEM, .0001f, GOLEMS);
	}

	private void loadHorsemen() {
		addCreatureFetcher(ZEALOT_HORSEMAN, .0001f, ZEALOT, HORSEMEN, HORSE, HORSES);
		addCreatureFetcher(FROZEN_SOLDIER_HORSEMAN, .0001f, FROZEN_SOLDIER, HORSEMEN, FROZEN_HORSE, HORSES);
	}

	private void loadFemaleMages() {
		addCreatureFetcher(FEMALE_ELF_MAGE, .0001f, FEMALE_MAGES);
		addCreatureFetcher(FEMALE_NECROMANCER, .0001f, FEMALE_MAGES);
		addCreatureFetcher(HUMAN_WITCH, .0001f, FEMALE_MAGES);
	}

	private void loadHumanoids() {
		addCreatureFetcher(DWARF, .0001f, HUMANOIDS);
		addCreatureFetcher(HALFLING, .0001f, HUMANOIDS);
		addCreatureFetcher(TROLL, .0002f, HUMANOIDS);
		addCreatureFetcher(GNOME, .0001f, HUMANOIDS);
	}

	private void loadHumanoidBeasts() {
		addCreatureFetcher(KOBOLD, .0001f, HUMANOID_BEASTS);
		addCreatureFetcher(GHOUL, .0001f, HUMANOID_BEASTS);
	}

	private void loadMages() {
		addCreatureFetcher(HUMAN_MAGE, .0001f, MAGES);
		addCreatureFetcher(HUMAN_NECROMANCER, .0001f, MAGES);
		addCreatureFetcher(ELF_WIZARD, .0001f, MAGES);
	}

	private void loadMaleAngels() {
		addCreatureFetcher(ANGEL, .0001f, MALE_ANGELS);
		addCreatureFetcher(FALLEN_ANGEL, .0001f, MALE_ANGELS);
	}

	private void loadSoldiers() {
		addCreatureFetcher(FROZEN_SOLDIER, .0001f, SOLDIERS);
		addCreatureFetcher(ZEALOT, .0001f, SOLDIERS);
	}

	private void loadCannon() {
		addTowerWithBaseAndAttackFetcher(FLAME_CANNON, .0001f, FIRE_RUBBLE, CANNONS, FLAME_CANNON);
		addTowerWithBaseAndAttackFetcher(WATER_CANNON, .0001f, WATER_RUBBLE, CANNONS, WATER_CANNON);
		addTowerWithBaseAndAttackFetcher(LIGHTNING_CANNON, .0001f, WIND_RUBBLE, CANNONS, LIGHTNING_CANNON);
		addTowerWithBaseAndAttackFetcher(ROCK_CANNON, .0001f, EARTH_RUBBLE, CANNONS, ROCK_CANNON);
		addTowerWithBaseAndAttackFetcher(ENERGY_CANNON, .0001f, LIFE_RUBBLE, CANNONS, ENERGY_CANNON);
		addTowerWithBaseAndAttackFetcher(POISON_CANNON, .0001f, UNDEAD_RUBBLE, CANNONS, POISON_CANNON);
	}

	private void loadWhips() {
		addTowerWithBaseAndAttackFetcher(FLAME_WHIP, .0001f, FIRE_RUBBLE, WHIP, LIGHTNING_CANNON);
		addTowerWithBaseAndAttackFetcher(WATER_WHIP, .0001f, WATER_RUBBLE, WHIP, LIGHTNING_CANNON);
		addTowerWithBaseAndAttackFetcher(TENTICAL_WHIP, .0001f, EARTH_RUBBLE, WHIP, LIGHTNING_CANNON);
	}

	private void loadCrystallizers() {
		addTowerWithBaseAndAttackFetcher(ICE_CRYSTALLIZER, .0001f, WATER_RUBBLE, CRYSTALLIZERS, ENERGY_CANNON);
		addTowerWithBaseAndAttackFetcher(EARTH_CRYSTALLIZER, .0001f, EARTH_RUBBLE, CRYSTALLIZERS, ENERGY_CANNON);
		addTowerWithBaseAndAttackFetcher(FLAME_CRYSTALLIZER, .0001f, FIRE_RUBBLE, CRYSTALLIZERS, ENERGY_CANNON);
	}

	private void loadBallistas() {
		addTowerWithBaseAndAttackFetcher(ICE_BALLISTA, .0001f, WATER_RUBBLE, BALLISTAS, POISON_CANNON);
		addTowerWithBaseAndAttackFetcher(EARTH_BALLISTA, .0001f, EARTH_RUBBLE, BALLISTAS, POISON_CANNON);
		addTowerWithBaseAndAttackFetcher(FLAME_BALLISTA, .0001f, FIRE_RUBBLE, BALLISTAS, POISON_CANNON);

	}

	private void loadBlades() {
		addTowerWithBaseAndAttackFetcher(FLAME_BLADE, .0001f, FIRE_RUBBLE, BLADE, ROCK_CANNON);
		addTowerWithBaseAndAttackFetcher(LIGHTNING_BLADE, .0001f, WATER_RUBBLE, BLADE, LIGHTNING_CANNON);
		addTowerWithBaseAndAttackFetcher(ICE_BLADE, .0001f, EARTH_RUBBLE, BLADE, ROCK_CANNON);
	}

	private void loadCatapults() {
		addTowerWithBaseAndAttackFetcher(FLAME_CATAPULT, .0001f, FIRE_RUBBLE, CATAPULTS, FLAME_CANNON);
		addTowerWithBaseAndAttackFetcher(WATER_CATAPULT, .0001f, WATER_RUBBLE, CATAPULTS, WATER_CANNON);
		addTowerWithBaseAndAttackFetcher(STONE_CATAPULT, .0001f, EARTH_RUBBLE, CATAPULTS, ROCK_CANNON);
	}

	private void loadEmitters() {
		addEmitterFetcher(FIRE_EMITTER, .0001f, FIRE_RUBBLE, EMITTERS, true, FLAME_CANNON, FIRE);
		addEmitterFetcher(WATER_EMITTER, .0001f, WATER_RUBBLE, EMITTERS, true, WATER_CANNON, ICE);
		addEmitterFetcher(LIGHTNING_EMITTER, .0001f, WIND_RUBBLE, EMITTERS, true, LIGHTNING_CANNON, WIND);
		addEmitterFetcher(DIRT_EMITTER, .0001f, EARTH_RUBBLE, EMITTERS, true, ROCK_CANNON, EARTH);
		addEmitterFetcher(HOLY_WATER_EMITTER, .0001f, LIFE_RUBBLE, EMITTERS, true, ENERGY_CANNON, LIFE);
		addEmitterFetcher(TOXIC_GAS_EMITTER, .0001f, UNDEAD_RUBBLE, EMITTERS, true, POISON_CANNON, DEATH);
	}

	private void loadPillars() {
		addPillarFetcher(FIRE_PILLAR, .0001f, FIRE_RUBBLE, PILLARS);
		addPillarFetcher(WATER_PILLAR, .0001f, WATER_RUBBLE, PILLARS);
		addPillarFetcher(LIGHTNING_PILLAR, .0001f, WIND_RUBBLE, PILLARS);
		addPillarFetcher(EARTH_PILLAR, .0001f, EARTH_RUBBLE, PILLARS);
		addPillarFetcher(LIFE_PILLAR, .0001f, LIFE_RUBBLE, PILLARS);
		addPillarFetcher(UNDEAD_PILLAR, .0001f, UNDEAD_RUBBLE, PILLARS);
	}

	private void loadStatues() {
		addTowerWithBaseAndAttackFetcher(ANGEL_STATUE, .0001f, LIFE_RUBBLE, STATUES, ENERGY_CANNON);
		addTowerWithBaseAndAttackFetcher(GARGOYLE_STATUE, .0001f, UNDEAD_RUBBLE, STATUES, POISON_CANNON);
		addTowerWithBaseAndAttackFetcher(GRIFFIN_STATUE, .0001f, WIND_RUBBLE, STATUES, LIGHTNING_CANNON);
	}

	private void loadProjectiles() {
		modelFetchers.put(EARTH_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(EARTH_BALL_PROJECTILE,
				BALL_PROJECTILES, false), EARTH));
		modelFetchers.put(FIRE_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(FIRE_BALL_PROJECTILE,
				BALL_PROJECTILES, false), FIRE));
		modelFetchers.put(LIFE_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(LIFE_BALL_PROJECTILE,
				BALL_PROJECTILES, false), LIFE));
		modelFetchers.put(WIND_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(WIND_BALL_PROJECTILE,
				BALL_PROJECTILES, false), WIND));
		modelFetchers.put(ICE_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(ICE_BALL_PROJECTILE,
				BALL_PROJECTILES, false), ICE));
		modelFetchers.put(UNDEAD_BALL_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(UNDEAD_BALL_PROJECTILE,
				BALL_PROJECTILES, false), DEATH));
		modelFetchers.put(FIRE_ARROW_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(FLAME_BALLISTA,
				ARROW_PROJECTILES, true), FIRE));
		modelFetchers.put(ICE_ARROW_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(ICE_BALLISTA,
				ARROW_PROJECTILES, true), ICE));
		modelFetchers.put(EARTH_ARROW_PROJECTILE, new ProjectileFetcher(.0001f, new GSFetcher(EARTH_BALLISTA,
				ARROW_PROJECTILES, true), EARTH));
	}

	private void loadBoards() {
		modelFetchers.put(FOREST_BACKGROUND, new BackgroundFetcher(FOREST_BACKGROUND));
		modelFetchers.put(FOREST_TREE, new RotatingFetcher(.0001f, new GSFetcher(FOREST_PROPS, TREE, true)));
		modelFetchers.put(FOREST_ROCK, new RotatingFetcher(.0001f, new GSFetcher(FOREST_PROPS, ROCK, true)));
		modelFetchers.put(WINTER_FOREST_BACKGROUND, new BackgroundFetcher(WINTER_FOREST_BACKGROUND));
		modelFetchers.put(WINTER_FOREST_TREE, new RotatingFetcher(.0001f,
				new GSFetcher(WINTER_FOREST_PROPS, TREE, true)));
		modelFetchers.put(WINTER_FOREST_ROCK, new RotatingFetcher(.0001f,
				new GSFetcher(WINTER_FOREST_PROPS, ROCK, true)));
		modelFetchers.put(DESERT_BACKGROUND, new BackgroundFetcher(DESERT_BACKGROUND));
		modelFetchers.put(DESERT_TREE, new RotatingFetcher(.0001f, new GSFetcher(DESERT_PROPS, CACTUS, true)));
		modelFetchers.put(DESERT_ROCK, new RotatingFetcher(.0001f, new GSFetcher(DESERT_PROPS, ROCK, true)));
	}

	private void loadSpecials() {
		modelFetchers.put(BaseSpecial.DAMAGE_CREATURES, new SpecialFetcher(1f, 0, 0));
		modelFetchers.put(BaseSpecial.HEAL_CREATURES, new SpecialFetcher(1f, 1f, 1f));
		modelFetchers.put(BaseSpecial.KILL_CREATURES, new SpecialFetcher(.25f, .75f, .25f));
		modelFetchers.put(BaseSpecial.SLOW_CREATURES, new SpecialFetcher(0, 0, 1f));
		modelFetchers.put(BaseSpecial.SPEEDUP_CREATURES, new SpecialFetcher(0, 1f, 1f));
		modelFetchers.put(BaseSpecial.STUN_CREATURES, new SpecialFetcher(0, 1f, 0));

	}

	private void loadCustomModels() {
		modelFetchers.put(MorphingBox.NAME, new Fetcher() {
			private MorphingBox box;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (box == null) {
					box = new MorphingBox(textureManager);
				}
				return new CombinedModel(new MorphingBox(box));
			}
		});
		modelFetchers.put(Plane.NAME, new Fetcher() {
			private Plane plane;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (plane == null) {
					plane = new Plane();
				}
				if (args.length > 0) {
					plane.setRGBA((Float) args[0], (Float) args[1], (Float) args[2], .25f);
				} else {
					plane.setRandomColor(true);
				}
				return new CombinedModel(new Plane(plane));
			}
		});
		modelFetchers.put(SpecialSquare.NAME, new Fetcher() {
			private SpecialSquare square;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (square == null) {
					square = new SpecialSquare(false);
				}
				SpecialSquare newSquare = new SpecialSquare(square);
				if (args.length > 0)
					newSquare.modifyXYZScale(1f, 1f, (Float) args[0]);
				else
					newSquare.modifyXYZScale(1f, 1f, 4f);

				if (args.length > 1) {
					newSquare.setRGBA((Float) args[1], (Float) args[2], (Float) args[3], 1f);
				} else {
					newSquare.setRandomColor(true);
				}
				return new CombinedModel(newSquare);
			}
		});
		modelFetchers.put(BuildingSphere.NAME, new Fetcher() {
			private Sphere model;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (model == null) {
					model = new Sphere();
				}
				Sphere sphere = new Sphere(model);
				if (args.length > 0)
					sphere.modifyXYZScale((Float) args[0]);
				return new CombinedModel(sphere);
			}

		});
		modelFetchers.put(Nova.NAME, new Fetcher() {
			private Nova model;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (model == null) {
					model = new Nova();
				}
				return new CombinedModel(new Nova(model));
			}

		});
		modelFetchers.put(Explosion.NAME, new Fetcher() {
			private Explosion model;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (model == null) {
					model = new Explosion();
				}
				return new CombinedModel(new Explosion(model));
			}

		});
		modelFetchers.put(Spray.NAME, new Fetcher() {
			private Spray model;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (model == null) {
					model = new Spray();
					model.setAlpha(.5f);
				}
				return new CombinedModel(new Spray(model));
			}

		});
		modelFetchers.put(Wall.NAME, new Fetcher() {
			private Wall border;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (border == null) {
					border = new Wall(textureManager, (Integer) args[0], (Integer) args[1]);
				}

				return new Wall(border);
			}

		});
		modelFetchers.put(Border.NAME, new Fetcher() {
			private Border border;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (border == null) {
					border = new Border(textureManager, modelManager, (Integer) args[0], (Integer) args[1],
							(String) args[2]);
				}
				return new Border(border);
			}
		});
		modelFetchers.put(SkyBox.NAME, new Fetcher() {
			private SkyBox model;

			@Override
			public CombinedModel fetchModel(Object... args) {
				if (model == null) {
					model = new SkyBox(textureManager, (Integer) args[0], (Integer) args[1]);
				}
				return new CombinedModel(new SkyBox(model));
			}

		});
	}

	// TODO combine all special square models for start and finish like
	private void loadBoardFetchers() {
		modelFetchers.put(Square.START, new Fetcher() {

			@Override
			public CombinedModel fetchModel(Object... args) {

				return modelFetchers.get(SpecialSquare.NAME).fetchModel(4f, 0f, 0f, .5f);
			}
		});
		modelFetchers.put(Square.FINISH, new Fetcher() {

			@Override
			public CombinedModel fetchModel(Object... args) {

				return modelFetchers.get(SpecialSquare.NAME).fetchModel(4f, .5f, 0f, 0f);
			}
		});
		modelFetchers.put(Square.ATTACK_RATE_BONUS, new Fetcher() {

			@Override
			public CombinedModel fetchModel(Object... args) {

				return modelFetchers.get(Plane.NAME).fetchModel(.5f, 0f, 0f);
			}
		});
		modelFetchers.put(Square.DAMAGE_BONUS, new Fetcher() {

			@Override
			public CombinedModel fetchModel(Object... args) {

				return modelFetchers.get(Plane.NAME).fetchModel(0f, .5f, 0f);
			}
		});
		modelFetchers.put(Square.RANGE_BONUS, new Fetcher() {

			@Override
			public CombinedModel fetchModel(Object... args) {

				return modelFetchers.get(Plane.NAME).fetchModel(0f, 0f, .5f);
			}
		});
	}

	private interface Fetcher {
		public CombinedModel fetchModel(Object... args);
	}

	private static class SpecialFetcher implements Fetcher {
		private float r, g, b;
		private static SpecialSquare square;

		public SpecialFetcher(float r, float g, float b) {
			this.r = r;
			this.b = b;
			this.g = g;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			if (square == null) {
				square = new SpecialSquare((Integer) args[0], (Integer) args[1]);
			}
			SpecialSquare newSquare = new SpecialSquare(square);

			newSquare.setRGBA(r, g, b, 1f);

			return new CombinedModel(newSquare);
		}

	}

	private class BackgroundFetcher implements Fetcher {
		private Background model;

		private String textureName;

		public BackgroundFetcher(String textureName) {
			this.textureName = textureName;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			if (model == null) {
				model = new Background(textureManager, textureName, (Integer) args[0], (Integer) args[1]);
			}
			return new CombinedModel(new Background(model));
		}
	}

	private class EmitterFetcher implements Fetcher {
		private CombinedFetcher towerFetcher;
		private int race;

		public EmitterFetcher(CombinedFetcher towerFetcher, int race) {
			this.towerFetcher = towerFetcher;
			this.race = race;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			CombinedModel combinedModel = modelFetchers.get(Spray.NAME).fetchModel();

			setColor(combinedModel, race);

			combinedModel.addModels(towerFetcher.fetchModel());

			return combinedModel;
		}

	}

	private void setColor(CombinedModel model, int race) {
		switch (race) {
		case FIRE:
			model.setRGB(.5f, 0f, 0f);
			break;
		case ICE:
		case WIND:
			model.setRGB(0f, 0f, .5f);
			break;
		case EARTH:
			model.setRGB(0f, .5f, 0f);
			break;
		case DEATH:
			model.setRGB(0f, 0f, 0f);
			break;
		case LIFE:
		default:
			model.setRGB(.5f, .5f, .5f);
		}
	}

	private class ProjectileFetcher implements Fetcher {

		private GSFetcher fetcher;
		private float scaleSize;
		private int race;

		public ProjectileFetcher(float scaleSize, GSFetcher fetcher, int race) {
			this.fetcher = fetcher;
			this.scaleSize = scaleSize;
			this.race = race;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			CombinedModel combinedModel = modelFetchers.get(Explosion.NAME).fetchModel();

			setColor(combinedModel, race);
			ArrayList<GSModel> projectile = fetcher.getModels();
			for (BaseModel model : projectile) {
				model.setXYZScale(scaleSize);
				combinedModel.addModel(model);
			}
			return combinedModel;
		}
	}

	private class RotatingFetcher implements Fetcher {
		private GSFetcher[] modelParts;
		private float scaleSize;

		public RotatingFetcher(float scaleSize, GSFetcher... modelParts) {
			this.modelParts = modelParts;
			this.scaleSize = scaleSize;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			ArrayList<BaseModel> parts = new ArrayList<BaseModel>();
			for (GSFetcher fetcher : modelParts) {
				parts.addAll(fetcher.getModels());
			}
			float rz = FastMath.random(360);
			for (BaseModel baseModel : parts) {
				baseModel.setXYZScale(scaleSize);
				baseModel.setZRotation(rz);
			}

			return new CombinedModel(parts);
		}
	}

	private class CombinedFetcher implements Fetcher {
		private GSFetcher[] modelParts;
		private float scaleSize;

		public CombinedFetcher(float scaleSize, GSFetcher... modelParts) {
			this.modelParts = modelParts;
			this.scaleSize = scaleSize;
		}

		@Override
		public CombinedModel fetchModel(Object... args) {
			ArrayList<BaseModel> parts = new ArrayList<BaseModel>();
			for (GSFetcher fetcher : modelParts) {
				parts.addAll(fetcher.getModels());
			}
			for (BaseModel baseModel : parts) {
				baseModel.setXYZScale(scaleSize);
			}

			return new CombinedModel(parts);
		}
	}

	private class ActionFetcher extends GSFetcher {
		private int action;

		public ActionFetcher(String textureName, String meshName, boolean rotates, int action) {
			super(textureName, meshName, rotates);
			this.action = action;
		}

		@Override
		public ArrayList<GSModel> getModels(Object... args) {
			ArrayList<GSModel> models = super.getModels(args);
			for (GSModel model : models) {
				model.setAction(action);
			}
			return models;
		}
	}

	private class GSFetcher {
		private String textureName;
		private String meshName;
		private boolean rotates;

		public GSFetcher(String textureName, String meshName, boolean rotates) {
			this.meshName = meshName;
			this.textureName = textureName;
			this.rotates = rotates;
		}

		public ArrayList<GSModel> getModels(Object... args) {

			int textureId = textureManager.getTextureID(textureName, false);
			GSModelVBOs data = modelManager.getGSModelVBOs(meshName, false);
			ArrayList<GSModel> models = ModelUtils.createModel(textureId, data);
			for (BaseModel baseModels : models) {
				baseModels.setRotates(rotates);
			}

			return models;

		}
	}

}
