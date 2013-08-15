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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.restlet.resource.ClientResource;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.godsandtowers.R;
import com.godsandtowers.core.GameEngine;
import com.godsandtowers.core.GameInfo;
import com.godsandtowers.core.HostGameEngine;
import com.godsandtowers.core.PlayerStats;
import com.godsandtowers.core.RemoteGameEngine;
import com.godsandtowers.core.grid.Board;
import com.godsandtowers.core.grid.Boards;
import com.godsandtowers.core.grid.Grid;
import com.godsandtowers.core.networking.ClientInfo;
import com.godsandtowers.core.networking.DeviceInfo;
import com.godsandtowers.core.networking.GetGameResource;
import com.godsandtowers.core.networking.HostNetworkManager;
import com.godsandtowers.core.networking.NetworkGameInfo;
import com.godsandtowers.core.networking.PrepareGameResource;
import com.godsandtowers.graphics.game.BitmapCache;
import com.godsandtowers.messaging.ApplicationMessageProcessor;
import com.godsandtowers.sprites.Player;
import com.godsandtowers.sprites.Races;
import com.godsandtowers.util.ADS;
import com.godsandtowers.util.Constants;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.gl.ModelUtils;
import com.gundogstudios.modules.Modules;

public class OnlineGameLayout implements ILayout {

	private static final String TAG = "OnlineGameLayout";
	private LinearLayout newGameLayout;
	private int speed;
	private PlayerStats player;
	private String boardName;
	private PopupWindow window;

	public OnlineGameLayout(final PlayerStats player, String boardName) {
		this.player = player;
		this.boardName = boardName;
	}

	private void setGameSpeed(int position) {
		switch (position) {
		case 0:
			speed = GameInfo.SLOW;
			break;
		case 1:
			speed = GameInfo.NORMAL;
			break;
		case 2:
		default:
			speed = GameInfo.FAST;
			break;
		}
	}

	private Spinner generateSpinner(Context context, OnItemSelectedListener listener, int description, int[] choices) {
		String[] mStrings = new String[choices.length];
		Resources resources = context.getResources();
		for (int i = 0; i < mStrings.length; i++) {
			mStrings[i] = resources.getString(description) + ": " + resources.getString(choices[i]).toUpperCase();
		}

		Spinner speedSpinner = new Spinner(context);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mStrings);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		speedSpinner.setAdapter(adapter);
		speedSpinner.setOnItemSelectedListener(listener);
		return speedSpinner;
	}

	public LinearLayout getLayout(final Context context) {
		if (newGameLayout == null) {
			Typeface font = (Typeface) Modules.PREFERENCES.get(TDWPreferences.TEXT_FONT, Typeface.DEFAULT);
			newGameLayout = new LinearLayout(context);
			newGameLayout.setOrientation(LinearLayout.VERTICAL);

			ADS.placeObtrusiveADMobAD(newGameLayout);
			TextView racesTextView = new TextView(context);
			racesTextView.setText(R.string.newgame_yourRace);
			racesTextView.setTypeface(font);
			newGameLayout.addView(racesTextView);

			final RaceSelectionGallery humanRaces = new RaceSelectionGallery(context, Races.ALL_RACES,
					new RaceSelectionGallery.BasicSelector(context, player));
			int races = Modules.PREFERENCES.get(TDWPreferences.PLAYER_RACE, 0);
			if (Races.asArray(races).length <= 1 || player.areMultipleRacesUnlocked())
				humanRaces.setSelections(races);

			newGameLayout.addView(humanRaces);

			Spinner speedSpinner = generateSpinner(context, new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					setGameSpeed(arg2);
					Modules.PREFERENCES.put(TDWPreferences.GAME_SPEED, arg2);
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
				}
			}, R.string.newgame_speed, new int[] { R.string.newgame_slow, R.string.newgame_normal,
					R.string.newgame_fast, });
			int s = Modules.PREFERENCES.get(TDWPreferences.GAME_SPEED, GameInfo.NORMAL);
			setGameSpeed(s);
			speedSpinner.setSelection(s >= 3 ? 0 : s);

			newGameLayout.addView(speedSpinner);

			final Button startButton = new Button(context);
			BitmapDrawable drawable = new BitmapDrawable(context.getResources(),
					BitmapCache.getBitmap(R.drawable.menu_options_button));
			startButton.setBackgroundDrawable(drawable);
			startButton.setText(R.string.newgame_findGame);
			startButton.setTypeface((Typeface) Modules.PREFERENCES.get(TDWPreferences.BUTTON_FONT, Typeface.DEFAULT));
			startButton.setTextColor(Color.WHITE);
			startButton.setTextSize(20);
			startButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					createPopup(context);
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								Modules.NETWORKING.disconnect();

								int races = Races.getRaces(humanRaces.getSelections());
								Modules.PREFERENCES.put(TDWPreferences.PLAYER_RACE, races);
								if (races == 0) {
									Toast toast = Toast.makeText(context, R.string.newgame_minHumanRaces,
											Toast.LENGTH_LONG);
									toast.show();
									return;
								}

								NetworkGameInfo networkGameInfo = null;
								int id = 0;
								id = getID(races);
								networkGameInfo = getNetworkGameInfo(id);

								if (networkGameInfo == null) {
									Modules.LOG.error(TAG, "network info was null");
									return;
								}

								Modules.LOG.info(TAG, "NetworkGameInfo: " + networkGameInfo);

								boolean isHost = id == networkGameInfo.getHostID();

								Modules.LOG.info(TAG, "IsHost: " + isHost);

								Board board = Boards.getBoard(networkGameInfo.getBoardName());

								ClientInfo hostInfo = networkGameInfo.getHost();
								Grid gridOne = new Grid(board);
								Player playerOne = new Player(0, hostInfo.getPlayerStats(), hostInfo.getRaces(),
										gridOne);

								ClientInfo clientInfo = networkGameInfo.getClient();
								Grid gridTwo = new Grid(board);
								Player playerTwo = new Player(1, clientInfo.getPlayerStats(), clientInfo.getRaces(),
										gridTwo);

								Player[] players = new Player[] { playerOne, playerTwo };

								GameInfo gameInfo = new GameInfo(isHost ? 0 : 1, players, networkGameInfo
										.getGameSpeed(), GameInfo.BATTLE, GameInfo.BATTLE_WAVES, board);
								GameEngine engine;

								if (isHost) {
									HostNetworkManager hostManager = new HostNetworkManager(gameInfo);
									engine = new HostGameEngine(gameInfo, hostManager);
								} else {
									engine = new RemoteGameEngine(gameInfo);
								}

								Modules.LOG.info(TAG, "Attaching GameEngine");
								Modules.MESSENGER.submit(ApplicationMessageProcessor.ID,
										ApplicationMessageProcessor.ATTACH_GAME, engine);
							} catch (Exception e) {
								Modules.NETWORKING.disconnect();
								e.printStackTrace();
								Modules.LOG.error(TAG, e.getMessage());
								return;
							} finally {
								if (window != null) {
									try {
										window.dismiss();
									} catch (Exception e) {
										Modules.LOG.error(TAG, e.getMessage());
									}
									window = null;
								}
							}
						}
					}).start();
				}
			});
			newGameLayout.addView(startButton);
			ADS.placeADMobAd(newGameLayout);
		}
		return newGameLayout;
	}

	private void createPopup(Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setGravity(Gravity.CENTER);
		layout.setBackgroundColor(Color.argb(196, 0, 0, 0));
		TextView view = new TextView(context);
		view.setText(R.string.newgame_searching);
		view.setGravity(Gravity.CENTER);
		view.setTextSize(30);
		layout.addView(view);

		window = new PopupWindow(layout, context.getResources().getDisplayMetrics().widthPixels, context.getResources()
				.getDisplayMetrics().heightPixels);

		window.showAtLocation(newGameLayout, Gravity.CENTER, 0, 0);
	}

	@Override
	public void attach(LinearLayout baseLayout) {
		baseLayout.addView(getLayout(baseLayout.getContext()));
	}

	private DeviceInfo getDeviceInfo() {
		return new DeviceInfo(android.os.Build.MODEL, Runtime.getRuntime().maxMemory(), Runtime.getRuntime()
				.availableProcessors(), android.os.Build.VERSION.SDK_INT, Modules.PREFERENCES.get(
				TDWPreferences.GAME_ENGINE_SPEED, GameEngine.FAST), ModelUtils.getMeshQuality(),
				ModelUtils.getTextureQuality());
	}

	private int getID(int races) {
		DeviceInfo deviceInfo = getDeviceInfo();

		ClientInfo clientInfo = new ClientInfo(deviceInfo, player, races, speed, boardName);

		ClientResource prepareGameClient = new ClientResource("http://" + Constants.RESTLET_HOSTNAME + ":"
				+ Constants.RESTLET_PORT + Constants.RESTLET_PREPAREGAME);
		prepareGameClient.setRequestEntityBuffering(true);
		PrepareGameResource prepareGameResource = prepareGameClient.wrap(PrepareGameResource.class);
		Modules.LOG.info(TAG, "sending preparegame requests");
		return prepareGameResource.prepareGame(clientInfo);

	}

	private NetworkGameInfo getNetworkGameInfo(int id) throws IOException, InterruptedException {

		InetAddress address = getAddress();
		DatagramSocket socket = new DatagramSocket(ClientInfo.UDP_PORT, address);

		ClientResource getGameClient = new ClientResource("http://" + Constants.RESTLET_HOSTNAME + ":"
				+ Constants.RESTLET_PORT + Constants.RESTLET_GETGAME);
		getGameClient.setRequestEntityBuffering(true);
		GetGameResource getGameResource = getGameClient.wrap(GetGameResource.class);

		NetworkGameInfo networkGameInfo = null;
		long start = System.currentTimeMillis();

		do {
			Modules.LOG.info(TAG, "sending update packet on ID: " + id);
			String data = "" + id;
			DatagramPacket sentPacket = new DatagramPacket(data.getBytes(), data.length(),
					Inet4Address.getByName(Constants.RESTLET_HOSTNAME), ClientInfo.UDP_PORT);
			socket.send(sentPacket);
			Modules.LOG.info(TAG, "sent packet, trying to get game now");

			networkGameInfo = getGameResource.getGame(id);

			if (networkGameInfo == null)
				Thread.sleep(1000);

			Modules.LOG.info(TAG, networkGameInfo == null ? "no network game info found" : networkGameInfo.toString());
		} while (networkGameInfo == null && (System.currentTimeMillis() - start) < 30000);

		if (networkGameInfo == null)
			return null;

		ClientInfo remoteClient;
		if (networkGameInfo.getHostID() == id) {
			remoteClient = networkGameInfo.getClient();
		} else {
			remoteClient = networkGameInfo.getHost();
		}
		// ModuleManager.LOG.info(TAG, "****** IDS: " + id + " " + networkGameInfo.getHostID());
		Modules.LOG.info(TAG,
				"IsHost: " + (networkGameInfo.getHostID() == id) + " remoteaddress: " + remoteClient.getAddress() + ":"
						+ remoteClient.getPort());
		Modules.NETWORKING.connect(socket, remoteClient.getAddress(), remoteClient.getPort());
		return networkGameInfo;
	}

	private Inet4Address getAddress() throws SocketException {
		Inet4Address address = null;
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
			NetworkInterface intf = en.nextElement();
			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
				InetAddress inetAddress = enumIpAddr.nextElement();

				if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
					address = (Inet4Address) inetAddress;
				}

			}
		}

		if (address != null)
			Modules.LOG.info(TAG, "Local Address Is: " + address.getHostAddress());
		else
			Modules.LOG.error(TAG, "Unable to determine local address");
		return address;
	}

}
