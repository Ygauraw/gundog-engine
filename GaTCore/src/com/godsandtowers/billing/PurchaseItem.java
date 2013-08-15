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
package com.godsandtowers.billing;

import com.godsandtowers.sprites.Races;

public class PurchaseItem {
	public enum SKU {
		MULTIPLE_RACES("gat_multiple_races"), SPECIAL_100("gat_special_100"), UPGRADE_ALL("gat_upgrade_all"), UPGRADE_ONE_RACE(
				"gat_upgrade_onerace"), UPGRADE_ABILITIES("gat_upgrade_abilities"), DOUBLE_EXPERIENCE(
				"gat_double_experience");

		private String sku;

		private SKU(String sku) {
			this.sku = sku;
		}

		@Override
		public String toString() {
			return sku;
		}
	}

	public static final PurchaseItem MULTIPLE_RACES = new PurchaseItem(SKU.MULTIPLE_RACES, null);
	public static final PurchaseItem SPECIAL_100 = new PurchaseItem(SKU.SPECIAL_100, null);

	public static PurchaseItem[] getPurchaseItems() {
		return new PurchaseItem[] { MULTIPLE_RACES, SPECIAL_100, new PurchaseItem(SKU.UPGRADE_ALL, null),
				new PurchaseItem(SKU.UPGRADE_ABILITIES, null), new PurchaseItem(SKU.DOUBLE_EXPERIENCE, null),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.ICE),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.WIND),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.EARTH),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.FIRE),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.LIFE),
				new PurchaseItem(SKU.UPGRADE_ONE_RACE, "" + Races.DEATH), };
	}

	private SKU sku;
	private String payload;

	public PurchaseItem(SKU sku, String payload) {
		this.sku = sku;
		this.payload = payload;
	}

	public SKU getSku() {
		return sku;
	}

	public String getPurchaseTitle() {
		return "purchase_" + sku.toString().replace("gat_", "");
	}

	public String getPurchaseDescription() {
		return "purchase_" + sku.toString().replace("gat_", "") + "_description";
	}

	public String getItemID() {
		return sku.toString();
	}

	public String getPayload() {
		return payload;
	}

}
