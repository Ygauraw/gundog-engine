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

import android.app.Activity;

import com.godsandtowers.graphics.game.layouts.BottomLayout;
import com.godsandtowers.graphics.game.layouts.CenterLayout;
import com.godsandtowers.graphics.game.layouts.TopLayout;
import com.godsandtowers.sprites.Tower;
import com.gundogstudios.modules.Modules;

public class ViewHandler implements ViewMessageProcessor {
	private Activity activity;
	private TopLayout top;
	private CenterLayout center;
	private BottomLayout bottom;

	public ViewHandler(Activity activity, TopLayout top, CenterLayout center, BottomLayout bottom) {
		this.activity = activity;
		this.top = top;
		this.center = center;
		this.bottom = bottom;
	}

	@Override
	public void process(final int what, final Object[] object) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				switch (what) {
				case REFRESH:
					top.refresh();
					break;
				case UPGRADE_TOWER:
					Tower tower = (Tower) object[0];
					center.attachUpgradeTowerLayout(tower);
					bottom.attachUpgradeTowerLayout();
					bottom.setUpgradeTower(tower.getBaseTower());
					break;
				case DISPLAY_VIEW_MENU:
					center.attachOptionsLayout();
					bottom.attachCancelLayout();
					break;
				case TURN_OFF_ATTACKING:
					bottom.setAttacking(false);
					break;
				default:
					Modules.LOG.error("ViewHandler", "Unknown message " + what);
				}
			}
		});

	}
}
