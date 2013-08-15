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

import java.text.NumberFormat;
import java.util.HashMap;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.godsandtowers.R;
import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.util.FastMath;

public class AchievementsLayout implements ILayout {
	private static final float NOT_COMPLETED_ALPHA = .5f;
	private LinearLayout achievementLayout;
	private LinearLayout achievementsLayout;
	private PlayerStats stats;
	private Activity activity;
	private ProgressBar progressBar;
	private TextView currentLevel;
	private TextView nextLevel;
	private TextView currentXP;
	private TextView wins;
	private TextView losses;
	private TextView longestGame;
	private TextView shortestGame;
	private TextView highestLevelBeaten;
	private TextView highestScore;
	private TextView bronze;
	private TextView silver;
	private TextView gold;
	private TextView platinum;
	private int widthPadding;
	private HashMap<String, String> raceNames;

	public AchievementsLayout(Activity activity, PlayerStats stats) {
		this.activity = activity;
		this.stats = stats;
		raceNames = new HashMap<String, String>();
		for (String race : Races.ALL_RACES_STRINGS) {
			String name = ResourceUtilities.getString("race_" + race.toLowerCase());
			raceNames.put(race, name);
		}
	}

	private TextView generateView(Activity activity, int id) {
		LinearLayout insideLayout = new LinearLayout(activity);
		insideLayout.setOrientation(LinearLayout.HORIZONTAL);
		insideLayout.setGravity(Gravity.CENTER_VERTICAL);

		ImageView image = new ImageView(activity);
		Bitmap bitmap = BitmapCache.getBitmap(id);
		image.setImageBitmap(bitmap);
		image.setPadding(0, 0, widthPadding, 0);
		insideLayout.addView(image);

		TextView textView = new TextView(activity);
		textView.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
		textView.setTextColor(Color.WHITE);
		insideLayout.addView(textView);

		achievementLayout.addView(insideLayout);
		return textView;
	}

	public LinearLayout getLayout() {
		if (achievementLayout == null) {

			this.widthPadding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 0);

			LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			achievementLayout = (LinearLayout) inflater.inflate(R.layout.achievementlayout, null);

			Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT);

			currentLevel = (TextView) achievementLayout.findViewById(R.id.currentLevel);
			currentLevel.setTypeface(typeFace);
			nextLevel = (TextView) achievementLayout.findViewById(R.id.nextLevel);
			nextLevel.setTypeface(typeFace);
			currentXP = (TextView) achievementLayout.findViewById(R.id.currentXP);
			currentXP.setTypeface(typeFace);

			progressBar = (ProgressBar) achievementLayout.findViewById(R.id.progressBar);

			BitmapDrawable full = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.achievement_progressbar_filled));
			ClipDrawable progress = new ClipDrawable(full, Gravity.LEFT, ClipDrawable.HORIZONTAL);
			progressBar.setProgressDrawable(progress);
			progressBar.setMax(100);
			progressBar.setClickable(false);

			wins = generateView(activity, R.drawable.achievement_wins);
			losses = generateView(activity, R.drawable.achievement_losses);
			longestGame = generateView(activity, R.drawable.achievement_longest_game);
			shortestGame = generateView(activity, R.drawable.achievement_quickest_game);
			highestLevelBeaten = generateView(activity, R.drawable.achievement_highest_level_beaten);
			highestScore = generateView(activity, R.drawable.achievement_high_score);
			bronze = generateView(activity, R.drawable.trophy_bronze);
			silver = generateView(activity, R.drawable.trophy_silver);
			gold = generateView(activity, R.drawable.trophy_gold);
			platinum = generateView(activity, R.drawable.trophy_platinum);

			ImageView seperator = new ImageView(activity);
			seperator.setImageBitmap(BitmapCache.getBitmap(R.drawable.menu_seperator));
			achievementLayout.addView(seperator);

			achievementsLayout = new LinearLayout(activity);
			achievementsLayout.setOrientation(LinearLayout.VERTICAL);
			achievementLayout.addView(achievementsLayout);
			refresh();

			ADS.placeADMobAd(achievementLayout);
		}
		return achievementLayout;
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout());
	}

	public void refresh() {
		if (achievementLayout == null)
			return;
		Resources resources = activity.getResources();

		currentLevel.setText("" + stats.getCurrentLevel());
		nextLevel.setText("" + (stats.getCurrentLevel() + 1));

		long xpToNextLevel = stats.getTotalXP() - PlayerStats.getLevelXP(stats.getCurrentLevel());
		long xpNeeded = PlayerStats.getLevelXP(stats.getCurrentLevel() + 1)
				- PlayerStats.getLevelXP(stats.getCurrentLevel());
		float progress = (float) xpToNextLevel / (float) xpNeeded * 100f;
		progressBar.setProgress(FastMath.round(progress));

		currentXP.setText(stats.getTotalXP() + " " + resources.getString(R.string.achievements_XP));

		wins.setText(resources.getString(R.string.achievements_wins) + ": " + stats.getWins());

		losses.setText(resources.getString(R.string.achievements_losses) + ": " + stats.getLosses());

		longestGame.setText(resources.getString(R.string.achievements_longestGame) + ": "
				+ DateUtils.formatElapsedTime(stats.getLongestGame() / 1000));

		shortestGame.setText(resources.getString(R.string.achievements_shortestGame)
				+ ": "
				+ ((stats.getShortestGame() == Long.MAX_VALUE) ? 0 : DateUtils.formatElapsedTime(stats
						.getShortestGame() / 1000)));

		highestLevelBeaten.setText(resources.getString(R.string.achievements_highestLevelBeaten) + ": "
				+ stats.getHighestLevelBeaten());

		highestScore.setText(resources.getString(R.string.achievements_highestScore) + ": " + stats.getHighestScore());

		updateTrophyText(bronze, Achievement.BRONZE);
		updateTrophyText(silver, Achievement.SILVER);
		updateTrophyText(gold, Achievement.GOLD);
		updateTrophyText(platinum, Achievement.PLATINUM);

		updateAchievementLayout();
	}

	private void updateTrophyText(TextView view, int level) {

		int completed = 0;
		int total = 0;
		for (Achievement achievement : stats.getAchievements()) {
			if (level == achievement.getAchievementLevel()) {
				total++;
				if (achievement.isCompleted())
					completed++;
			}
		}
		view.setText(completed + "/" + total);
	}

	private void updateAchievementLayout() {
		achievementsLayout.removeAllViews();
		for (Achievement achievement : stats.getAchievements()) {

			LinearLayout layout = new LinearLayout(activity);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setGravity(Gravity.CENTER_VERTICAL);

			ImageView image = new ImageView(activity);
			Bitmap bitmap = BitmapCache.getBitmap(getTrophyIcon(achievement.getAchievementLevel()));
			image.setImageBitmap(bitmap);
			image.setPadding(0, 0, widthPadding / 2, 0);
			layout.addView(image);
			if (!achievement.isCompleted()) {
				image.setAlpha(NOT_COMPLETED_ALPHA);

				NumberFormat formatter = NumberFormat.getInstance();
				formatter.setMaximumFractionDigits(0);
				TextView textView = new TextView(activity);
				textView.setText(formatter.format(achievement.getPercentComplete(stats) * 100f) + "%");
				textView.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
				textView.setTextColor(Color.WHITE);
				textView.setPadding(0, 0, widthPadding / 2, 0);
				layout.addView(textView);
			}

			TextView textView = new TextView(activity);
			textView.setText(ResourceUtilities.getAchievementInfo(achievement));
			textView.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT));
			textView.setTextColor(Color.WHITE);
			layout.addView(textView);

			achievementsLayout.addView(layout);
		}
	}

	private int getTrophyIcon(int level) {
		switch (level) {
		case Achievement.BRONZE:
			return R.drawable.trophy_bronze;
		case Achievement.SILVER:
			return R.drawable.trophy_silver;
		case Achievement.GOLD:
			return R.drawable.trophy_gold;
		case Achievement.PLATINUM:
			return R.drawable.trophy_platinum;
		default:
			throw new RuntimeException("Unknown trophy type in Achievements Layout: " + level);
		}
	}

}
