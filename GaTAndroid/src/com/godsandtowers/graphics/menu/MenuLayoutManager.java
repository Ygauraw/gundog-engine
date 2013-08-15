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
package com.godsandtowers.graphics.menu;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.godsandtowers.MainActivity;
import com.godsandtowers.R;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.graphics.menu.layouts.AchievementsLayout;
import com.godsandtowers.graphics.menu.layouts.AlmanacLeftLayout;
import com.godsandtowers.graphics.menu.layouts.AlmanacRightLayout;
import com.godsandtowers.graphics.menu.layouts.CreditsLayout;
import com.godsandtowers.graphics.menu.layouts.ExitLayout;
import com.godsandtowers.graphics.menu.layouts.GameSelectionLayout;
import com.godsandtowers.graphics.menu.layouts.ILayout;
import com.godsandtowers.graphics.menu.layouts.NewsLayout;
import com.godsandtowers.graphics.menu.layouts.OptionsLayout;
import com.godsandtowers.graphics.menu.layouts.PurchaseLayout;
import com.godsandtowers.graphics.menu.layouts.ResumeLayout;
import com.godsandtowers.graphics.menu.layouts.UpgradeLeftLayout;
import com.godsandtowers.graphics.menu.layouts.UpgradeRightLayout;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;

public class MenuLayoutManager {
	private Button currentButton;
	private LinearLayout rightLayout;
	private LinearLayout leftLayout;
	private ResumeLayout resumeLayout;
	private NewsLayout newsLayout;
	private GameSelectionLayout gameSelectionLayout;
	private UpgradeLeftLayout upgradeLeftLayout;
	private UpgradeRightLayout upgradeRightLayout;
	private AchievementsLayout achievementsLayout;
	private AlmanacRightLayout almanacRightLayout;
	private AlmanacLeftLayout almanacLeftLayout;
	private PurchaseLayout purchaseLayout;
	private CreditsLayout creditsLayout;
	private OptionsLayout optionsLayout;
	private ExitLayout exitLayout;
	private LinearLayout resumeButton;
	private LinearLayout[] buttonLayouts;
	private MainActivity activity;

	public MenuLayoutManager(MainActivity activity, PlayerStats stats) {
		this.activity = activity;
		generateRightLayout(stats);
		int[] buttonIDs = new int[] { R.string.main_resume, R.string.main_newGame, R.string.main_store,
				R.string.main_upgrade, R.string.main_achievements, R.string.main_almanac, R.string.main_options,
				R.string.main_credits, R.string.main_exit };
		ILayout[] leftLayouts = new ILayout[] { null, null, null, upgradeLeftLayout, null, almanacLeftLayout, null,
				null, null };
		ILayout[] rightLayouts = new ILayout[] { resumeLayout, gameSelectionLayout, purchaseLayout, upgradeRightLayout,
				achievementsLayout, almanacRightLayout, optionsLayout, creditsLayout, exitLayout };

		generateLeftLayout(buttonIDs, leftLayouts, rightLayouts);
	}

	public void setOnlinePlay(boolean onlinePlayEnabled) {
		gameSelectionLayout.setOnlinePlay(onlinePlayEnabled);
	}

	public void setNews(String[][] news) {
		newsLayout.setNews(news);
	}

	private void generateRightLayout(PlayerStats stats) {
		int width = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0);
		int height = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0);
		int rightPadding = (int) (.1 * width);
		int leftPadding = (int) (.08 * width);
		int topPadding = (int) (.12 * height);
		int bottomPadding = (int) (.08 * height);

		ScrollView scrollLayout = (ScrollView) activity.findViewById(R.id.scrollableRightLayout);
		scrollLayout.setPadding(leftPadding, topPadding, rightPadding, bottomPadding);
		scrollLayout.setScrollbarFadingEnabled(false);

		leftLayout = (LinearLayout) activity.findViewById(R.id.leftLayout);
		upgradeLeftLayout = new UpgradeLeftLayout(activity, stats);
		almanacLeftLayout = new AlmanacLeftLayout(activity);

		rightLayout = (LinearLayout) activity.findViewById(R.id.rightLayout);

		rightLayout.setGravity(Gravity.LEFT);
		resumeLayout = new ResumeLayout();
		newsLayout = new NewsLayout(activity);
		gameSelectionLayout = new GameSelectionLayout(activity, this, stats);
		purchaseLayout = new PurchaseLayout();
		upgradeRightLayout = new UpgradeRightLayout(activity, upgradeLeftLayout, stats);
		almanacRightLayout = new AlmanacRightLayout(activity, almanacLeftLayout, stats);
		achievementsLayout = new AchievementsLayout(activity, stats);
		optionsLayout = new OptionsLayout();
		creditsLayout = new CreditsLayout();
		exitLayout = new ExitLayout();
		newsLayout.attach(rightLayout);
	}

	private void generateLeftLayout(int[] textIDs, ILayout[] leftLayouts, ILayout[] rightLayouts) {

		int width = Modules.PREFERENCES.get(TDWPreferences.WIDTH, 0);
		int height = Modules.PREFERENCES.get(TDWPreferences.HEIGHT, 0);
		int leftPadding = (int) (.02 * width);
		int rightPadding = (int) (.02 * width);
		int verticlePadding = (int) (.1 * height);
		int buttonWidth = (int) (.275 * width);
		int buttonHeight = (int) (.125 * height);

		final ScrollView scrollLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
		scrollLayout.setPadding(leftPadding, verticlePadding, rightPadding, verticlePadding);
		scrollLayout.setBackgroundResource(R.drawable.menu_strap);
		scrollLayout.setScrollbarFadingEnabled(false);

		buttonLayouts = new LinearLayout[textIDs.length];

		final Button backButton = new Button(activity);
		BitmapDrawable drawable = new BitmapDrawable(activity.getResources(),
				BitmapCache.getBitmap(R.drawable.menu_button_notpressed));
		backButton.setBackgroundDrawable(drawable);
		backButton.setText(R.string.main_back);
		Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT);
		int color = Modules.PREFERENCES.get(TDWPreferences.BUTTON_COLOR, Color.GRAY);
		backButton.setTypeface(typeFace);
		backButton.setTextColor(color);
		backButton.setTextSize(20);
		backButton.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));

		final LinearLayout backButtonLayout = new LinearLayout(activity);
		backButtonLayout.setOrientation(LinearLayout.HORIZONTAL);
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				leftLayout.removeAllViews();
				almanacLeftLayout.reset(leftLayout);
				scrollLayout.setBackgroundResource(R.drawable.menu_strap);
				for (int i = 0; i < buttonLayouts.length; i++) {
					leftLayout.addView(buttonLayouts[i]);
				}
				rightLayout.removeAllViews();
				newsLayout.attach(rightLayout);
				ScrollView scrollLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
				scrollLayout.scrollTo(0, 0);
			}
		});
		backButtonLayout.addView(backButton);
		for (int i = 0; i < textIDs.length; i++) {

			LinearLayout buttonLayout = buttonLayouts[i] = new LinearLayout(activity);
			buttonLayout.setOrientation(LinearLayout.HORIZONTAL);

			final Button button = new Button(activity);
			drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_button_notpressed));
			button.setBackgroundDrawable(drawable);
			button.setText(textIDs[i]);
			button.setTypeface(typeFace);
			button.setTextColor(color);
			button.setTextSize(20);
			button.setLayoutParams(new LayoutParams(buttonWidth, buttonHeight));

			final ILayout llayout = leftLayouts[i];
			final ILayout rlayout = rightLayouts[i];
			if (llayout != null) {
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (currentButton != null) {
							BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), BitmapCache
									.getBitmap(R.drawable.menu_button_notpressed));
							currentButton.setBackgroundDrawable(drawable);
						}
						currentButton = null;

						leftLayout.removeAllViews();

						leftLayout.addView(backButtonLayout);
						llayout.attach(leftLayout);

						rightLayout.removeAllViews();
						rlayout.attach(rightLayout);
						ScrollView scrollLeftLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
						scrollLeftLayout.scrollTo(0, 0);
						ScrollView scrollRightLayout = (ScrollView) activity.findViewById(R.id.scrollableRightLayout);
						scrollRightLayout.scrollTo(0, 0);
					}
				});
			} else {
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (currentButton != null) {
							BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), BitmapCache
									.getBitmap(R.drawable.menu_button_notpressed));
							currentButton.setBackgroundDrawable(drawable);
						}
						ILayout newLayout;
						if (currentButton != button) {
							BitmapDrawable drawable = new BitmapDrawable(activity.getResources(), BitmapCache
									.getBitmap(R.drawable.menu_button_pressed));
							button.setBackgroundDrawable(drawable);
							currentButton = button;
							newLayout = rlayout;
						} else {
							currentButton = null;
							newLayout = newsLayout;
						}
						attachRightLayout(newLayout);
					}
				});
			}
			buttonLayout.addView(button);

			leftLayout.addView(buttonLayout);
			if (textIDs[i] == R.string.main_resume) {
				resumeButton = buttonLayout;
			}
		}
		hideResume();

	}

	public void attachRightLayout(ILayout layout) {
		if (layout == null)
			layout = newsLayout;

		rightLayout.removeAllViews();
		layout.attach(rightLayout);

		ScrollView scrollLeftLayout = (ScrollView) activity.findViewById(R.id.scrollableLeftLayout);
		scrollLeftLayout.scrollTo(0, 0);
		ScrollView scrollRightLayout = (ScrollView) activity.findViewById(R.id.scrollableRightLayout);
		scrollRightLayout.scrollTo(0, 0);
	}

	public void attachNews() {
		if (currentButton != null) {
			BitmapDrawable drawable = new BitmapDrawable(activity.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_button_notpressed));
			currentButton.setBackgroundDrawable(drawable);
		}
		currentButton = null;

		leftLayout.removeAllViews();
		for (int i = 0; i < buttonLayouts.length; i++) {
			leftLayout.addView(buttonLayouts[i]);
		}

		rightLayout.removeAllViews();
		newsLayout.attach(rightLayout);
	}

	public void showResume() {
		resumeButton.setVisibility(View.VISIBLE);
	}

	public void hideResume() {
		resumeButton.setVisibility(View.GONE);
	}

	public void reset() {
		achievementsLayout.refresh();
		upgradeLeftLayout.reset();
		upgradeRightLayout.reset();
		gameSelectionLayout.reset();
	}

}
