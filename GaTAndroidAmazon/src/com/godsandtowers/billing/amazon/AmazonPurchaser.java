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
package com.godsandtowers.billing.amazon;

import java.util.HashMap;
import java.util.HashSet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import com.amazon.inapp.purchasing.PurchasingManager;
import com.godsandtowers.R;
import com.godsandtowers.billing.PurchaseItem;
import com.godsandtowers.billing.PurchaseItem.SKU;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.BaseRace;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.PurchaseModule;

public class AmazonPurchaser implements PurchaseModule {

	private static final String TAG = "AmazonPurchaser";
	private HashMap<String, PurchaseItem> purchaseItems;
	private PlayerStats stats;
	private Activity activity;
	private String payload;

	public AmazonPurchaser() {

	}

	@Override
	public void restoreTransactions() {
		HashSet<String> skus = new HashSet<String>();
		skus.add(SKU.MULTIPLE_RACES.toString());
		try {
			PurchasingManager.initiateItemDataRequest(skus);
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void purchase(String itemId, String payload) {
		try {
			if (itemId.equals(SKU.UPGRADE_ONE_RACE))
				this.payload = payload;
			PurchasingManager.initiatePurchaseRequest(itemId);
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void purchased(String itemId, String unusedPayload) {
		PurchaseItem item = purchaseItems.get(itemId);
		if (item == null) {
			Modules.LOG.error(TAG, "Unable to locate purchase item");
			return;
		}
		Modules.LOG.info(TAG, "Purchased: " + itemId);

		switch (item.getSku()) {
		case MULTIPLE_RACES:
			purchasedMultipleRaces();
			break;
		case SPECIAL_100:
			purchasedSpecial100();
			break;
		case UPGRADE_ALL:
			purchasedUpgradeAll();
			break;
		case UPGRADE_ONE_RACE:
			purchasedUpgradeOneRace(payload);
			break;
		case UPGRADE_ABILITIES:
			purchasedUpgradeAbilities();
			break;
		case DOUBLE_EXPERIENCE:
			purchasedDoubleExperience();
			break;
		default:
			Modules.LOG.error(TAG, "Unknown name ID: " + item.getSku());
		}

	}

	@Override
	public void destroy() {

	}

	private Dialog createDialog(int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(titleId).setIcon(android.R.drawable.stat_sys_warning).setMessage(messageId)
				.setCancelable(false).setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}

	private void purchasedDoubleExperience() {
		stats.doubleExperience();
	}

	private void purchasedMultipleRaces() {
		stats.unlockMultipleRaces();
	}

	private void purchasedSpecial100() {
		for (BaseSpecial special : stats.getSpecials())
			for (int i = 0; i < 100; i++)
				special.upgrade(0);
	}

	private void purchasedUpgradeAll() {
		for (BaseCreature creature : stats.getBaseCreatures()) {
			for (int id : creature.getUpgradeIDs()) {
				creature.upgrade(id);
			}
		}

		for (BaseTower tower : stats.getBaseTowers()) {
			for (int id : tower.getUpgradeIDs()) {
				tower.upgrade(id);
			}
		}
	}

	private void purchasedUpgradeOneRace(String payload) {
		int race = Integer.parseInt(payload);
		for (BaseCreature creature : stats.getBaseCreatures()) {
			if (Races.isRaces(creature.getRaces(), race)) {
				for (int id : creature.getUpgradeIDs()) {
					creature.upgrade(id);
				}
			}
		}

		for (BaseTower tower : stats.getBaseTowers()) {
			if (Races.isRaces(tower.getRaces(), race)) {
				for (int id : tower.getUpgradeIDs()) {
					tower.upgrade(id);
				}
			}
		}
	}

	private void purchasedUpgradeAbilities() {
		BasePlayer player = stats.getBasePlayer();
		for (int id : player.getUpgradeIDs()) {
			player.upgrade(id);
		}
		BaseRace race = stats.getBaseRace();
		for (int id : race.getUpgradeIDs()) {
			race.upgrade(id);
		}
	}

	@Override
	public void init(Object... args) {
		this.activity = (Activity) args[0];
		this.stats = (PlayerStats) args[1];
		try {
			PurchaseObserver buttonClickerObserver = new PurchaseObserver(activity, this);
			PurchasingManager.registerObserver(buttonClickerObserver);
			PurchasingManager.initiateGetUserIdRequest();

			purchaseItems = new HashMap<String, PurchaseItem>();
			for (PurchaseItem item : PurchaseItem.getPurchaseItems()) {
				purchaseItems.put(item.getSku().toString(), item);
			}
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
			createDialog(R.string.purchase_cannot_connect_title, R.string.purchase_cannot_connect_message).show();
		}

	}
}
