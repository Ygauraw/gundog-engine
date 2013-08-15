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

import com.godsandtowers.MainActivity;
import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.gundogstudios.modules.Modules;

public class ApplicationHandler implements ApplicationMessageProcessor {

	private MainActivity activity;

	public ApplicationHandler(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	public void process(final int what, final Object[] object) {
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				switch (what) {
				case ATTACH_GAME:
					activity.newGame((GameEngine) object[0]);
					break;
				case DISPLAY_GAME:
					activity.displayGame();
					break;
				case EXIT:
					activity.finish();
					break;
				case RESET:
					activity.reset();
					break;
				case GAME_COMPLETED:
					activity.gameFinished((GameInfo) object[0]);
					break;
				case GAME_ERROR:
					activity.gameError((Exception) object[0]);
					break;
				case CLEAR_ASSETS:
					activity.clearAssets();
					break;
				case LOWER_GRAPHICS:
					activity.lowerGraphics();
					break;
				default:
					Modules.LOG.error("ActivityHandler", "Unknown message " + what);
				}

			}
		});
	}

}
