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
package com.gundogstudios.gl;

import java.util.HashMap;

import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class Scene {
	private static final String TAG = "Scene";

	private HashMap<Sprite, CombinedModel> spriteModels;
	private HashMap<Sprite, CombinedModel> transparentSpriteModels;

	public Scene() {
		spriteModels = new HashMap<Sprite, CombinedModel>();
		transparentSpriteModels = new HashMap<Sprite, CombinedModel>();
	}

	public synchronized void draw(int timePassed) {
		drawSprites(timePassed, spriteModels);
		drawSprites(timePassed, transparentSpriteModels);
	}

	private void drawSprites(int timePassed, HashMap<Sprite, CombinedModel> sprites) {
		for (Sprite sprite : sprites.keySet()) {
			CombinedModel graphic = sprites.get(sprite);

			graphic.setXYZTranslation(sprite.getX(), sprite.getY(), sprite.getZ());

			graphic.setAction(sprite.getAction());

			Sprite target = sprite.getTarget();
			if (target != null) {
				float rotation = rotate(sprite.getX(), sprite.getY(), target.getX(), target.getY());
				graphic.setZRotation(rotation);
			}
			graphic.draw(timePassed);
		}
	}

	private static float rotate(float x1, float y1, float x2, float y2) {
		return FastMath.toDegrees(FastMath.atan2(y2 - y1, x2 - x1));
	}

	public synchronized void removeSprite(Sprite sprite) {
		if (spriteModels.remove(sprite) == null) {
			Modules.LOG.error(TAG, "Trying to removeSprite that does not exist in Scene: " + sprite);
		}
	}

	public synchronized void removeTransparentSprite(Sprite sprite) {
		if (transparentSpriteModels.remove(sprite) == null) {
			Modules.LOG.error(TAG, "Trying to removeTransparentSprite that does not exist in Scene: " + sprite);
		}
	}

	public synchronized void addSprite(Sprite sprite, CombinedModel graphic) {
		if (spriteModels.put(sprite, graphic) != null) {
			Modules.LOG.error(TAG, "addSprite already exists in Scene " + sprite);
		}
	}

	public synchronized void addTransparentSprite(Sprite sprite, CombinedModel graphic) {
		if (transparentSpriteModels.put(sprite, graphic) != null) {
			Modules.LOG.error(TAG, "addTransparentSprite already exists in Scene " + sprite);
		}
	}

}
