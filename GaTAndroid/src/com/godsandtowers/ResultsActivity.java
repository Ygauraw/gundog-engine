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
package com.godsandtowers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.androidplot.series.XYSeries;
import com.androidplot.ui.AnchorPosition;
import com.androidplot.ui.DynamicTableModel;
import com.androidplot.ui.SizeLayoutType;
import com.androidplot.ui.SizeMetrics;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XLayoutStyle;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.androidplot.xy.YLayoutStyle;
import com.godsandtowers.achievements.Achievement;
import com.godsandtowers.campaigns.CampaignLevel;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.PlayerInfo;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.ResourceUtilities;
import com.godsandtowers.util.TDWAndroidPreferences;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.EmptyLogger;

@SuppressWarnings("deprecation")
public class ResultsActivity extends TabActivity implements TabHost.TabContentFactory {
	private HashMap<String, View> myTabs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Modules.LOG == null)
			Modules.LOG = new EmptyLogger();
		if (Modules.PREFERENCES == null)
			Modules.PREFERENCES = new TDWAndroidPreferences(this);

		Modules.LOG.info("ResultsActivity", "onCreate");

		super.setContentView(R.layout.tabs);
		ADS.setActivity(this);

		myTabs = new HashMap<String, View>();

		Intent intent = super.getIntent();
		GameInfo gameInfo = (GameInfo) intent.getSerializableExtra(MainActivity.GAME_RESULTS);

		PlayerInfo human = gameInfo.getPlayerInfos()[0];
		PlayerInfo ai = gameInfo.getPlayerInfos()[1];

		String tag;
		View layout;

		tag = this.getResources().getText(R.string.results_main).toString();
		layout = generateMainLayout(gameInfo);
		myTabs.put(tag, layout);
		setupTab(tag);

		tag = this.getResources().getText(R.string.results_life).toString();
		layout = generatePlotLayout(tag, human.getLife(), ai.getLife());
		myTabs.put(tag, layout);
		setupTab(tag);

		tag = this.getResources().getText(R.string.results_income).toString();
		layout = generatePlotLayout(tag, human.getIncome(), ai.getIncome());
		myTabs.put(tag, layout);
		setupTab(tag);

		tag = this.getResources().getText(R.string.results_offense).toString();
		layout = generatePlotLayout(tag, human.getOffensivePower(), ai.getOffensivePower());
		myTabs.put(tag, layout);
		setupTab(tag);

		tag = this.getResources().getText(R.string.results_defense).toString();
		layout = generatePlotLayout(tag, human.getDefensivePower(), ai.getDefensivePower());
		myTabs.put(tag, layout);
		setupTab(tag);

		TabHost tabHost = super.getTabHost();
		tabHost.setCurrentTab(0);

	}

	private View generatePayingAD() {
		RelativeLayout layout = new RelativeLayout(this);
		int padding = Modules.PREFERENCES.get(TDWPreferences.PADDING, 1);
		layout.setPadding(padding, padding, padding, padding);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

		ADS.placeADMobAd(layout, params);

		final Activity activity = this;
		Button button = new Button(this);
		button.setText(R.string.results_continue);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});
		params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layout.addView(button, params);
		return layout;
	}

	private View generateMainLayout(GameInfo gameInfo) {
		ScrollView scroll = new ScrollView(this);
		scroll.setScrollbarFadingEnabled(false);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.LEFT);

		layout.addView(generatePayingAD());

		Typeface typeFace = (Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT);
		float size = 20;
		TextView textView;

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size * 2);
		if (gameInfo.disconnected()) {
			textView.setText(R.string.results_disconnected);
		} else {
			textView.setText(gameInfo.won() ? R.string.results_won : R.string.results_lost);
		}
		layout.addView(textView);

		CampaignLevel campaignLevel = gameInfo.getCampaignLevel();
		if (campaignLevel != null) {
			LinearLayout horizontalLayout = new LinearLayout(this);
			horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
			textView = new TextView(this);
			textView.setTypeface(typeFace);
			textView.setTextSize(size);
			textView.setText(super.getText(R.string.results_campaign) + ": ");
			horizontalLayout.addView(textView);

			int stars;
			int starID;

			if (gameInfo.won()) {
				stars = campaignLevel.getStars();
				starID = getStarImageID(campaignLevel.getDifficulty());
			} else {
				stars = 5;
				starID = R.drawable.star_empty;
			}

			for (int i = 0; i < stars; i++) {
				ImageView image = new ImageView(this);
				image.setImageResource(starID);
				horizontalLayout.addView(image);
			}
			layout.addView(horizontalLayout);
		}

		ArrayList<Achievement> achievements = gameInfo.getAchievements();

		if (achievements.size() > 0) {
			LinearLayout verticleLayout = new LinearLayout(this);
			verticleLayout.setOrientation(LinearLayout.VERTICAL);

			textView = new TextView(this);
			textView.setTypeface(typeFace);
			textView.setTextSize(size);
			textView.setText(super.getText(R.string.results_rewards) + ": ");
			verticleLayout.addView(textView);

			for (Achievement achievement : achievements) {
				LinearLayout horizontalLayout = new LinearLayout(this);
				horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

				ImageView image = new ImageView(this);
				int trophyID = getTrophyImageID(achievement.getAchievementLevel());
				image.setImageResource(trophyID);
				horizontalLayout.addView(image);

				textView = new TextView(this);
				textView.setTypeface(typeFace);
				textView.setTextSize(size - 5);
				textView.setText(ResourceUtilities.getAchievementInfo(achievement));
				horizontalLayout.addView(textView);

				verticleLayout.addView(horizontalLayout);
			}

			layout.addView(verticleLayout);
		}
		NumberFormat xpFormatter = NumberFormat.getInstance();
		xpFormatter.setMaximumFractionDigits(0);
		float xp = gameInfo.getBaseXP();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_base_xp) + ": " + xpFormatter.format(xp));
		layout.addView(textView);

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_achievement_bonus) + ": " + gameInfo.getAchievementBonus()
				+ " * " + xpFormatter.format(xp));
		layout.addView(textView);
		xp *= gameInfo.getAchievementBonus();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_win_bonus) + ": " + gameInfo.getWinBonus() + " * "
				+ xpFormatter.format(xp));
		layout.addView(textView);
		xp *= gameInfo.getWinBonus();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_length_bonus) + ": " + gameInfo.getLengthBonus() + " * "
				+ xpFormatter.format(xp));
		layout.addView(textView);
		xp *= gameInfo.getLengthBonus();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_life_bonus) + ": " + gameInfo.getLifeBonus() + " * "
				+ xpFormatter.format(xp));
		layout.addView(textView);
		xp *= gameInfo.getLifeBonus();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMaximumFractionDigits(2);
		textView.setText(super.getText(R.string.results_multiple_race_factor) + ": "
				+ formatter.format(gameInfo.getMultipleRaceFactor()) + " * " + xpFormatter.format(xp));
		layout.addView(textView);
		xp *= gameInfo.getMultipleRaceFactor();

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_total_xp) + ": " + xpFormatter.format(gameInfo.getXP()));
		layout.addView(textView);

		if (super.getPackageName().equals("com.godsandtowers.free")) {
			textView = new TextView(this);
			textView.setTypeface(typeFace);
			textView.setTextSize(size);
			textView.setText(super.getText(R.string.results_free_version));
			layout.addView(textView);
		}

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_length) + ": "
				+ DateUtils.formatElapsedTime(gameInfo.getLength() / 1000));
		layout.addView(textView);

		textView = new TextView(this);
		textView.setTypeface(typeFace);
		textView.setTextSize(size);
		textView.setText(super.getText(R.string.results_waves) + ": " + gameInfo.getCurrentWave());
		layout.addView(textView);

		ADS.placeADMobAd(layout);

		scroll.addView(layout);
		return scroll;
	}

	private int getTrophyImageID(int level) {
		switch (level) {
		case Achievement.PLATINUM:
			return R.drawable.trophy_platinum;
		case Achievement.GOLD:
			return R.drawable.trophy_gold;
		case Achievement.SILVER:
			return R.drawable.trophy_silver;
		case Achievement.BRONZE:
			return R.drawable.trophy_bronze;
		default:
			Modules.LOG.error("ResultsActivity", "Unknown achievement level " + level);
			return 0;
		}
	}

	private int getStarImageID(int level) {

		switch (level) {
		case CampaignLevel.EXPERT:
			return R.drawable.star_platinum;
		case CampaignLevel.HARD:
			return R.drawable.star_gold;
		case CampaignLevel.MEDIUM:
			return R.drawable.star_silver;
		case CampaignLevel.EASY:
			return R.drawable.star_bronze;
		default:
			Modules.LOG.error("ResultsActivity", "Unknown campaign level " + level);
			return R.drawable.star_empty;
		}
	}

	private float dp2px(float dp) {
		return getResources().getDisplayMetrics().density * dp + 0.5f;
	}

	private void evenLists(List<Float> playerNumbers, List<Float> aiNumbers) {
		List<Float> bigger, smaller;
		if (playerNumbers.size() > aiNumbers.size()) {
			bigger = playerNumbers;
			smaller = aiNumbers;
		} else if (aiNumbers.size() > playerNumbers.size()) {
			bigger = aiNumbers;
			smaller = playerNumbers;
		} else {
			return;
		}

		Float n = smaller.get(smaller.size() - 1);
		while (smaller.size() < bigger.size())
			smaller.add(n);
	}

	private LinearLayout generatePlotLayout(String tag, List<Float> playerNumbers, List<Float> aiNumbers) {
		evenLists(playerNumbers, aiNumbers);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		layout.addView(generatePayingAD());
		XYPlot xyPlot = new XYPlot(this, tag);
		xyPlot.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// xyPlot.setPlotMargins(10f, 10f, 10f, 10f);
		float textSize = dp2px(15);
		xyPlot.getTitleWidget().setVisible(false);
		xyPlot.getDomainLabelWidget().getLabelPaint().setTextSize(textSize);
		xyPlot.getGraphWidget().getDomainLabelPaint().setTextSize(dp2px(10));
		xyPlot.getGraphWidget().getRangeLabelPaint().setTextSize(dp2px(10));
		xyPlot.getGraphWidget().getDomainOriginLabelPaint().setTextSize(dp2px(10));
		xyPlot.getGraphWidget().getRangeOriginLabelPaint().setTextSize(dp2px(10));
		float padding = dp2px(5);
		xyPlot.getGraphWidget().setPadding(padding * 3, padding, padding, padding * 3);
		// xyPlot.getDomainLabelWidget().getLabelPaint().setTypeface(null);
		xyPlot.getRangeLabelWidget().getLabelPaint().setTextSize(textSize);
		xyPlot.getLegendWidget().getTextPaint().setTextSize(textSize);
		xyPlot.setDomainLabel(super.getResources().getText(R.string.results_wave).toString());
		xyPlot.setRangeLabel(tag);
		// xyPlot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);

		// DATA
		XYSeries series1 = new SimpleXYSeries(aiNumbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, super.getText(
				R.string.results_ai).toString());
		LineAndPointFormatter formatter = new LineAndPointFormatter(Color.rgb(200, 0, 0), Color.rgb(100, 0, 0), 0);
		formatter.getLinePaint().setShadowLayer(dp2px(1), 0, 0, Color.rgb(200, 0, 0));
		formatter.getVertexPaint().setShadowLayer(dp2px(1), 0, 0, Color.rgb(200, 0, 0));
		xyPlot.addSeries(series1, formatter);

		XYSeries series2 = new SimpleXYSeries(playerNumbers, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, super.getText(
				R.string.results_you).toString());
		formatter = new LineAndPointFormatter(Color.rgb(0, 0, 200), Color.rgb(0, 0, 100), 0);
		formatter.getLinePaint().setShadowLayer(dp2px(1), 0, 0, Color.rgb(0, 0, 200));
		formatter.getVertexPaint().setShadowLayer(dp2px(1), 0, 0, Color.rgb(0, 0, 200));

		xyPlot.addSeries(series2, formatter);

		// Range
		xyPlot.setRangeStep(XYStepMode.SUBDIVIDE, 10);
		xyPlot.setRangeValueFormat(new DecimalFormat("#"));

		// Domain
		xyPlot.setDomainStep(XYStepMode.SUBDIVIDE, 10);
		xyPlot.setDomainValueFormat(new DecimalFormat("#"));

		xyPlot.position(xyPlot.getDomainLabelWidget(), 0, XLayoutStyle.ABSOLUTE_FROM_CENTER, 0,
				YLayoutStyle.ABSOLUTE_FROM_BOTTOM, AnchorPosition.BOTTOM_MIDDLE);
		// LegendWidget
		xyPlot.getLegendWidget().setTableModel(new DynamicTableModel(2, 1));
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.BLACK);
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setAlpha(32);

		xyPlot.getLegendWidget().setBackgroundPaint(bgPaint);

		padding = dp2px(2);
		xyPlot.getLegendWidget().setPadding(padding, padding, padding, padding);

		xyPlot.getLegendWidget().setSize(
				new SizeMetrics(dp2px(20), SizeLayoutType.ABSOLUTE, dp2px(80), SizeLayoutType.ABSOLUTE));

		xyPlot.position(xyPlot.getLegendWidget(), dp2px(30), XLayoutStyle.ABSOLUTE_FROM_RIGHT, 0,
				YLayoutStyle.ABSOLUTE_FROM_BOTTOM, AnchorPosition.RIGHT_BOTTOM);

		xyPlot.disableAllMarkup();
		layout.addView(xyPlot);

		return layout;
	}

	private void setupTab(String tag) {
		TabHost tabHost = super.getTabHost();
		View tabview = LayoutInflater.from(this).inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) tabview.findViewById(R.id.tabsText);
		tv.setText(tag);
		TabSpec setContent = tabHost.newTabSpec(tag).setIndicator(tabview).setContent(this);
		tabHost.addTab(setContent);
	}

	@Override
	public View createTabContent(String tag) {
		return myTabs.get(tag);
	}
}