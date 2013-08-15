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
package com.godsandtowers.messaging;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.godsandtowers.graphics.GameSurface;
import com.godsandtowers.graphics.ModelFetcher;
import com.gundogstudios.gl.CombinedModel;
import com.gundogstudios.gl.Sprite;
import com.gundogstudios.modules.Modules;

public class GLHandler implements GLMessageProcessor {

	private GameSurface surface;
	private ExecutorService executor;

	public GLHandler(GameSurface surface) {
		this.surface = surface;
		executor = Executors.newSingleThreadExecutor();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void process(final int what, final Object[] object) {
		final ModelFetcher modelFetcher = surface.getModelFetcher();
		int player;
		switch (what) {
		case ADD_SPRITE:
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int player = (Integer) object[0];
						Sprite addSprite = (Sprite) object[1];
						CombinedModel model = modelFetcher.getModel(addSprite);

						surface.addSprite(player, addSprite, model);
					} catch (OutOfMemoryError e) {
						Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
								ApplicationMessageProcessor.LOWER_GRAPHICS);
					}
				}
			});
			break;
		case REMOVE_SPRITE:
			player = (Integer) object[0];
			Sprite removeSprite = (Sprite) object[1];
			surface.removeSprite(player, removeSprite);
			break;
		case ADD_TRANSPARENT_SPRITE:
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int player = (Integer) object[0];
						Sprite addTransparentSprite = (Sprite) object[1];
						CombinedModel model = modelFetcher.getModel(addTransparentSprite,
								(object.length > 2) ? object[2] : 1f);
						surface.addTransparentSprite(player, addTransparentSprite, model);
					} catch (OutOfMemoryError e) {
						Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
								ApplicationMessageProcessor.LOWER_GRAPHICS);
					}
				}
			});
			break;
		case REMOVE_TRANSPARENT_SPRITE:
			player = (Integer) object[0];
			Sprite removeTransparentSprite = (Sprite) object[1];
			surface.removeTransparentSprite(player, removeTransparentSprite);
			break;
		case ADD_SPRITE_BATCH:
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int player = (Integer) object[0];
						for (Sprite sprite : (ArrayList<Sprite>) object[1]) {
							CombinedModel model = modelFetcher.getModel(sprite);

							surface.addSprite(player, sprite, model);
						}
					} catch (OutOfMemoryError e) {
						Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
								ApplicationMessageProcessor.LOWER_GRAPHICS);
					}
				}
			});
			break;
		case REMOVE_SPRITE_BATCH:
			player = (Integer) object[0];
			for (Sprite sprite : (ArrayList<Sprite>) object[1]) {
				surface.removeSprite(player, sprite);
			}
			break;
		case ADD_TRANSPARENT_SPRITE_BATCH:
			executor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int player = (Integer) object[0];
						for (Sprite sprite : (ArrayList<Sprite>) object[1]) {
							CombinedModel model = modelFetcher.getModel(sprite);

							surface.addTransparentSprite(player, sprite, model);
						}
					} catch (OutOfMemoryError e) {
						Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
								ApplicationMessageProcessor.LOWER_GRAPHICS);
					}
				}
			});
			break;
		case REMOVE_TRANSPARENT_SPRITE_BATCH:
			player = (Integer) object[0];
			for (Sprite sprite : (ArrayList<Sprite>) object[1]) {
				surface.removeTransparentSprite(player, sprite);
			}
			break;
		case PREVIOUS_BOARD:
			surface.previousBoard();
			break;
		case NEXT_BOARD:
			surface.nextBoard();
			break;
		case ZOOM_IN:
			surface.zoomIn();
			break;
		case ZOOM_OUT:
			surface.zoomOut();
			break;
		case ROTATE_UP:
			surface.rotateUp();
			break;
		case ROTATE_DOWN:
			surface.rotateDown();
			break;
		case RESET_VIEW:
			surface.resetView();
			break;
		case UPDATE_TRANSLATION:
			surface.updateTrans((Float) object[0], (Float) object[1], (Float) object[2]);
			break;
		case SHUTDOWN:
			executor.shutdownNow();
			break;
		default:
			Modules.LOG.error("GLHandler", "Unknown message " + what);
		}
	}

}
