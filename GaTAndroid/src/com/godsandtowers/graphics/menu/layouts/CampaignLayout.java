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
package com.godsandtowers.graphics.menu.layouts;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.godsandtowers.R;
import com.godsandtowers.campaigns.Campaign;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.MenuLayoutManager;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.gundogstudios.modules.Modules;

public class CampaignLayout implements ILayout {

	private LinearLayout campaignLayout;
	private PlayerStats player;
	private Activity activity;
	private LinearLayout levelLayout;
	private RaceSelectionGallery humanRaces;
	private MenuLayoutManager manager;

	public CampaignLayout(final Activity context, final MenuLayoutManager manager, final PlayerStats player) {
		this.player = player;
		this.manager = manager;
		this.activity = context;

	}

	private void attachLevels(int race) {

		levelLayout.removeAllViews();

		HashMap<Integer, Campaign> campaigns = player.getCampaigns();

		Campaign campaign = campaigns.get(race);
		if (campaign == null)
			return;

		for (CampaignLevel level : campaign.getLevels()) {
			addLevel(level);
		}

	}

	private void addLevel(final CampaignLevel level) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		LinearLayout insideLayout = (LinearLayout) inflater.inflate(R.layout.campaignlayout, null);

		addRatingBar(insideLayout, level);

		ImageView boardImage = new ImageView(activity);
		boardImage.setImageBitmap(BitmapCache.getBitmap("boards/", level.getBoard() + "_icon"));
		insideLayout.addView(boardImage);

		ImageView raceImage = new ImageView(activity);
		String raceName = Races.getName(level.getComputerRaces());
		int icon = ResourceUtilities.getIconID(raceName);
		raceImage.setImageBitmap(BitmapCache.getBitmap(icon));
		insideLayout.addView(raceImage);

		ImageView seperator = new ImageView(activity);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		insideLayout.addView(seperator);

		insideLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CampaignLevelLayout layout = new CampaignLevelLayout(activity, player, level);
				manager.attachRightLayout(layout);
			}
		});

		levelLayout.addView(insideLayout);
	}

	private RatingBar addRatingBar(LinearLayout insideLayout, CampaignLevel level) {
		RatingBar bar;
		switch (level.getDifficulty()) {
		case CampaignLevel.EXPERT:
			bar = (RatingBar) insideLayout.findViewById(R.id.platinumRatingBar);
			break;
		case CampaignLevel.HARD:
			bar = (RatingBar) insideLayout.findViewById(R.id.goldRatingBar);
			break;
		case CampaignLevel.MEDIUM:
			bar = (RatingBar) insideLayout.findViewById(R.id.silverRatingBar);
			break;
		case CampaignLevel.EASY:
			bar = (RatingBar) insideLayout.findViewById(R.id.bronzeRatingBar);
			break;
		default:
			Modules.LOG.error("CampaignLayout", "Unknown campagin level " + level);
			bar = (RatingBar) insideLayout.findViewById(R.id.bronzeRatingBar);
			break;
		}
		int stars = level.getStars();
		bar.setRating(stars);
		bar.setVisibility(RatingBar.VISIBLE);
		return bar;
	}

	public void reset() {
		if (campaignLayout != null) {
			levelLayout.removeAllViews();
			humanRaces.reset();
		}
	}

	private LinearLayout getLayout(final Context context) {
		campaignLayout = new LinearLayout(context);
		campaignLayout.setOrientation(LinearLayout.VERTICAL);
		campaignLayout.setGravity(Gravity.CENTER);

		ADS.placeObtrusiveADMobAD(campaignLayout);
		humanRaces = new RaceSelectionGallery(context, Races.ALL_RACES, new RaceSelectionGallery.Selector() {
			RaceSelectionGallery.BasicSelector baseSelector = new RaceSelectionGallery.BasicSelector(context, player);

			@Override
			public void onSelection(boolean[] selections) {
				baseSelector.onSelection(selections);
				int races = Races.getRaces(selections);

				attachLevels(races);

			}

		});
		campaignLayout.addView(humanRaces);

		ImageView seperator = new ImageView(context);
		seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
		campaignLayout.addView(seperator);

		levelLayout = new LinearLayout(context);
		levelLayout.setOrientation(LinearLayout.VERTICAL);
		levelLayout.setGravity(Gravity.CENTER);
		campaignLayout.addView(levelLayout);

		ADS.placeADMobAd(campaignLayout);
		return campaignLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

}
