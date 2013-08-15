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
package com.godsandtowers.amazon;

import com.godsandtowers.MainActivity;
import com.godsandtowers.billing.amazon.AmazonPurchaser;
import com.gundogstudios.modules.Modules;

public class GaTAmazonActivity extends MainActivity {

	@Override
	protected void initPurchaser() {
		Modules.PURCHASER = new AmazonPurchaser();
	}
}