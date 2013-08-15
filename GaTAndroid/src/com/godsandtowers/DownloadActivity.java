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

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager;

import com.godsandtowers.util.Constants;
import com.godsandtowers.util.DownloadManager;
import com.godsandtowers.util.TDWAndroidPreferences;
import com.godsandtowers.util.TDWPreferences;
import com.gundogstudios.modules.AndroidAssets;
import com.gundogstudios.modules.AndroidLogger;
import com.gundogstudios.modules.AndroidReporter;
import com.gundogstudios.modules.Modules;
import com.gundogstudios.modules.basic.BasicNetworkModule;
import com.gundogstudios.modules.basic.BasicProfilerModule;
import com.gundogstudios.modules.basic.EmptyLogger;
import com.gundogstudios.modules.basic.EmptyProfilerModule;

public class DownloadActivity extends Activity {

	private static final String TAG = "DownloadActivity";
	private static final int DIALOG_ID = 10;

	private ProgressDialog progressDialog;
	private DownloadManager downloadManager;
	private DownloadFileAsync asyncTask;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.setProperty("java.net.preferIPv6Addresses", "false");

		super.onCreate(savedInstanceState);
		super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Modules.LOG = Constants.TESTING ? new AndroidLogger() : new EmptyLogger();
		Modules.ASSETS = new AndroidAssets(this.getAssets());
		Modules.PROFILER = Constants.TESTING ? new BasicProfilerModule() : new EmptyProfilerModule();
		Modules.PREFERENCES = new TDWAndroidPreferences(this);
		Modules.REPORTER = new AndroidReporter();
		Modules.NETWORKING = new BasicNetworkModule();

		downloadManager = new DownloadManager(Environment.getExternalStorageDirectory().toString());

		if (!downloadManager.verifyAllAssets()) {
			asyncTask = new DownloadFileAsync(this);
			asyncTask.execute();
		} else {
			Modules.LOG.info(TAG, "All assets verified, not launching downloadManager");
			super.setResult(RESULT_OK);
			super.finish();
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage(getResources().getText(R.string.main_downloading));
		progressDialog.setIndeterminate(false);
		progressDialog.setMax((int) DownloadManager.TOTAL_SIZE);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);

		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getText(R.string.main_exit),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							if (asyncTask != null) {
								asyncTask.shutdown();
								asyncTask.cancel(true);
							}
						} catch (Exception e) {
							Modules.LOG.error(TAG, "Download cancel attempt failed due to: " + e.toString());
						}
						setResult(RESULT_CANCELED);
						finish();
					}
				});
		progressDialog.show();
		return progressDialog;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Modules.NETWORKING.disconnect();
		if (downloadManager != null)
			downloadManager.shutdown();
		try {
			if (asyncTask != null) {
				asyncTask.shutdown();
				asyncTask.cancel(true);
			}
		} catch (Exception e) {
			Modules.LOG.error(TAG, "Download cancel attempt failed due to: " + e.toString());
		}
	}

	private class DownloadFileAsync extends AsyncTask<Boolean, Integer, Boolean> {
		private Activity activity;

		public DownloadFileAsync(Activity activity) {
			this.activity = activity;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_ID);
		}

		private boolean shutdown = false;

		public void shutdown() {
			shutdown = true;
		}

		@Override
		protected Boolean doInBackground(Boolean... unused) {
			try {
				long startTime = System.currentTimeMillis();

				downloadManager.verifyAllAssets();

				super.publishProgress((int) downloadManager.getCurrentSize());

				downloadMissing();

				Modules.LOG.info(TAG, "Took " + (System.currentTimeMillis() - startTime) + " msec to fetch assets");

				if (downloadManager.downloadFailed() || !downloadManager.verifyAllAssets()) {
					Modules.LOG.info(TAG, "Download attempt failed to get all the correct assets");
					return false;
				} else {
					Modules.LOG.info(TAG, "All assets are downloaded and verified");
					Modules.ASSETS = new AndroidAssets(activity.getAssets());
					if (Modules.PREFERENCES.get(TDWPreferences.SOUND, true)) {
						Modules.AUDIO.play("theme", true);
					}
					return true;
				}

			} catch (Exception e) {
				Modules.LOG.info(TAG, "Download attempt failed to get all the correct assets due to: " + e.toString());
				return false;
			}
		}

		private void downloadMissing() {
			downloadManager.downloadMissingAssets();
			while (!shutdown && !downloadManager.downloadComplete()) {
				int progress = (int) downloadManager.getCurrentSize();
				Modules.LOG.info(
						TAG,
						"Still waiting on download, "
								+ (downloadManager.getTotalSize() - downloadManager.getCurrentSize())
								+ " bytes remaining.  Progress: " + progress);
				super.publishProgress(progress);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result == null || !result.booleanValue()) {
				progressDialog.setMessage(getResources().getText(R.string.main_download_failed));
			} else {
				removeDialog(DIALOG_ID);
				activity.setResult(RESULT_OK);
				activity.finish();
			}
		}
	}
}