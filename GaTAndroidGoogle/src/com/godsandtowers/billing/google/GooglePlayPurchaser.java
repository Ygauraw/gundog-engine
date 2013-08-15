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
package com.godsandtowers.billing.google;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Handler;

import com.godsandtowers.R;
import com.godsandtowers.billing.PurchaseItem;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.sprites.BaseCreature;
import com.godsandtowers.sprites.BasePlayer;
import com.godsandtowers.sprites.BaseRace;
import com.godsandtowers.sprites.BaseSpecial;
import com.godsandtowers.sprites.BaseTower;
import com.godsandtowers.sprites.Races;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.PurchaseModule;

public class GooglePlayPurchaser implements PurchaseModule {

	private static final String TAG = "GooglePlayPurchaser";
	private PurchaseObserver purchaseObserver;
	private Handler handler;
	private Activity activity;
	private BillingService billingService;
	private HashMap<String, PurchaseItem> purchaseItems;
	private PlayerStats stats;

	public GooglePlayPurchaser() {

	}

	@Override
	public void restoreTransactions() {
		try {
			billingService.restoreTransactions();
		} catch (Exception e) {

			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void purchase(String itemID, String payload) {
		try {
			if (!billingService.requestPurchase(itemID, payload)) {
				createDialog(R.string.purchase_billing_not_supported_title,
						R.string.purchase_billing_not_supported_message);
			}
		} catch (Exception e) {

			Modules.REPORTER.report(e);
			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void purchased(String itemId, String payload) {

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

	private Dialog createDialog(int titleId, int messageId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(titleId).setIcon(android.R.drawable.stat_sys_warning).setMessage(messageId)
				.setCancelable(false).setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}

	@Override
	public void destroy() {
		try {
			ResponseHandler.unregister(purchaseObserver);
			billingService.unbind();
		} catch (Exception e) {

			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void init(Object... args) {
		destroy();
		this.activity = (Activity) args[0];
		this.stats = (PlayerStats) args[1];
		purchaseItems = new HashMap<String, PurchaseItem>();
		for (PurchaseItem item : PurchaseItem.getPurchaseItems()) {
			purchaseItems.put(item.getSku().toString(), item);
		}
		try {
			handler = new Handler();
			purchaseObserver = new PurchaseObserver(activity, handler);
			billingService = new BillingService();
			billingService.setContext(activity);

			ResponseHandler.register(purchaseObserver);
			if (!billingService.checkBillingSupported()) {
				createDialog(R.string.purchase_cannot_connect_title, R.string.purchase_cannot_connect_message).show();
			} else {
				// stats.setAllRaces(false);
				// stats.setMultipleRaces(false);
			}
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
			createDialog(R.string.purchase_cannot_connect_title, R.string.purchase_cannot_connect_message).show();
		}
	}
}
