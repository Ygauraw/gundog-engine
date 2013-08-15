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
package com.gundogstudios.modules;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class AndroidAudio implements AudioModule {
	private static final String TAG = "AndroidAudio";
	private MediaPlayer mediaPlayer;
	private String playing;
	private AssetManager assetManager;

	public AndroidAudio(AssetManager assetManager) {
		this.assetManager = assetManager;
		this.mediaPlayer = null;
		playing = null;
	}

	@Override
	public void play(final String fileName, boolean resumeIfPlaying) {
		try {
			if (resumeIfPlaying && mediaPlayer != null && playing != null && playing.equals(fileName)) {
				if (!mediaPlayer.isPlaying())
					mediaPlayer.start();
			} else {
				if (mediaPlayer == null) {
					mediaPlayer = new MediaPlayer();
				}

				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						mediaPlayer.setOnCompletionListener(null);
						prepare(fileName + "_main.mp3", true);
					}
				});
				prepare(fileName + "_intro.mp3", false);

			}
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
		}
	}

	private void prepare(String fileName, boolean looping) {
		Modules.LOG.info(TAG, "Preparing: " + fileName);
		try {
			mediaPlayer.reset();
			mediaPlayer.setLooping(looping);
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mediaPlayer.start();
				}
			});
			AssetFileDescriptor afd = assetManager.openFd("music/" + fileName);
			mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			mediaPlayer.prepareAsync();
			playing = fileName;
		} catch (Exception e) {
			Modules.LOG.error(TAG, e.toString());
		}
	}

	@Override
	public void pause() {
		if (mediaPlayer != null && mediaPlayer.isPlaying())
			mediaPlayer.pause();
	}

	@Override
	public void shutdown() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
			mediaPlayer = null;
			playing = null;
		}
	}

}
