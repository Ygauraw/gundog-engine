// Copyright 2010 Google Inc. All Rights Reserved.

package com.godsandtowers.billing.google;

import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;

import com.gundogstudios.modules.Modules;
import com.godsandtowers.R;
import com.godsandtowers.billing.google.BillingService.RequestPurchase;
import com.godsandtowers.billing.google.BillingService.RestoreTransactions;
import com.godsandtowers.billing.google.Consts.PurchaseState;
import com.godsandtowers.billing.google.Consts.ResponseCode;

/**
 * An interface for observing changes related to purchases. The main application extends this class and registers an
 * instance of that derived class with {@link ResponseHandler}. The main application implements the callbacks
 * {@link #onBillingSupported(boolean)} and {@link #onPurchaseStateChange(PurchaseState, String, int, long)}. These
 * methods are used to update the UI.
 */
public class PurchaseObserver {
	private static final String TAG = "PurchaseObserver";
	protected final Activity mActivity;
	private final Handler mHandler;
	private Method mStartIntentSender;
	private Object[] mStartIntentSenderArgs = new Object[5];

	public PurchaseObserver(Activity activity, Handler handler) {
		mActivity = activity;
		mHandler = handler;
		initCompatibilityLayer();
	}

	/**
	 * This is the callback that is invoked when Android Market responds to the
	 * {@link BillingService#checkBillingSupported()} request.
	 * 
	 * @param supported
	 *            true if in-app billing is supported.
	 */

	public void onBillingSupported(boolean supported) {
		if (Consts.DEBUG) {
			Modules.LOG.info(TAG, "supported: " + supported);
		}
		if (!supported) {
			createDialog(mActivity, R.string.purchase_billing_not_supported_title,
					R.string.purchase_billing_not_supported_message);
		}
	}

	/**
	 * This is the callback that is invoked when an item is purchased, refunded, or canceled. It is the callback invoked
	 * in response to calling {@link BillingService#requestPurchase(String)}. It may also be invoked asynchronously when
	 * a purchase is made on another device (if the purchase was for a Market-managed item), or if the purchase was
	 * refunded, or the charge was canceled. This handles the UI update. The database update is handled in
	 * {@link ResponseHandler#purchaseResponse(Context, PurchaseState, String, String, long)}.
	 * 
	 * @param purchaseState
	 *            the purchase state of the item
	 * @param itemId
	 *            a string identifying the item (the "SKU")
	 * @param quantity
	 *            the current quantity of this item after the purchase
	 * @param purchaseTime
	 *            the time the product was purchased, in milliseconds since the epoch (Jan 1, 1970)
	 */

	public void onPurchaseStateChange(PurchaseState purchaseState, String itemId, int quantity, long purchaseTime,
			String developerPayload) {
		if (Consts.DEBUG) {
			Modules.LOG.info(TAG, "onPurchaseStateChange() itemId: " + itemId + " " + purchaseState);
		}

		if (developerPayload == null) {
			logProductActivity(itemId, purchaseState.toString());
		} else {
			logProductActivity(itemId, purchaseState + "\n\t" + developerPayload);
		}

		if (purchaseState == PurchaseState.PURCHASED) {
			Modules.PURCHASER.purchased(itemId, developerPayload);
		}
	}

	/**
	 * This is called when we receive a response code from Market for a RequestPurchase request that we made. This is
	 * NOT used for any purchase state changes. All purchase state changes are received in
	 * {@link #onPurchaseStateChange(PurchaseState, String, int, long)}. This is used for reporting various errors, or
	 * if the user backed out and didn't purchase the item. The possible response codes are: RESULT_OK means that the
	 * order was sent successfully to the server. The onPurchaseStateChange() will be invoked later (with a purchase
	 * state of PURCHASED or CANCELED) when the order is charged or canceled. This response code can also happen if an
	 * order for a Market-managed item was already sent to the server. RESULT_USER_CANCELED means that the user didn't
	 * buy the item. RESULT_SERVICE_UNAVAILABLE means that we couldn't connect to the Android Market server (for example
	 * if the data connection is down). RESULT_BILLING_UNAVAILABLE means that in-app billing is not supported yet.
	 * RESULT_ITEM_UNAVAILABLE means that the item this app offered for sale does not exist (or is not published) in the
	 * server-side catalog. RESULT_ERROR is used for any other errors (such as a server error).
	 */

	public void onRequestPurchaseResponse(RequestPurchase request, ResponseCode responseCode) {
		if (Consts.DEBUG) {
			Log.d(TAG, request.mProductId + ": " + responseCode);
		}
		if (responseCode == ResponseCode.RESULT_OK) {
			if (Consts.DEBUG) {
				Modules.LOG.info(TAG, "purchase was successfully sent to server");
			}
			logProductActivity(request.mProductId, "sending purchase request");
		} else if (responseCode == ResponseCode.RESULT_USER_CANCELED) {
			if (Consts.DEBUG) {
				Modules.LOG.info(TAG, "user canceled purchase");
			}
			logProductActivity(request.mProductId, "dismissed purchase dialog");
		} else {
			if (Consts.DEBUG) {
				Modules.LOG.info(TAG, "purchase failed");
			}
			logProductActivity(request.mProductId, "request purchase returned " + responseCode);
		}
	}

	/**
	 * This is called when we receive a response code from Android Market for a RestoreTransactions request that we
	 * made. A response code of RESULT_OK means that the request was successfully sent to the server.
	 */

	public void onRestoreTransactionsResponse(RestoreTransactions request, ResponseCode responseCode) {
		if (responseCode == ResponseCode.RESULT_OK) {
			if (Consts.DEBUG) {
				Log.d(TAG, "completed RestoreTransactions request");
			}

		} else {
			if (Consts.DEBUG) {
				Log.d(TAG, "RestoreTransactions error: " + responseCode);
			}
		}
	}

	private void initCompatibilityLayer() {
		try {
			mStartIntentSender = mActivity.getClass().getMethod("startIntentSender",
					new Class[] { IntentSender.class, Intent.class, int.class, int.class, int.class });
		} catch (SecurityException e) {
			mStartIntentSender = null;
		} catch (NoSuchMethodException e) {
			mStartIntentSender = null;
		}
	}

	void startBuyPageActivity(PendingIntent pendingIntent, Intent intent) {
		if (mStartIntentSender != null) {
			// This is on Android 2.0 and beyond. The in-app buy page activity
			// must be on the activity stack of the application.
			try {
				// This implements the method call:
				// mActivity.startIntentSender(pendingIntent.getIntentSender(),
				// intent, 0, 0, 0);
				mStartIntentSenderArgs[0] = pendingIntent.getIntentSender();
				mStartIntentSenderArgs[1] = intent;
				mStartIntentSenderArgs[2] = Integer.valueOf(0);
				mStartIntentSenderArgs[3] = Integer.valueOf(0);
				mStartIntentSenderArgs[4] = Integer.valueOf(0);
				mStartIntentSender.invoke(mActivity, mStartIntentSenderArgs);
			} catch (Exception e) {
				Modules.LOG.error(TAG, "error buy page starting activity");
			}
		} else {
			// This is on Android version 1.6. The in-app buy page activity must be on its
			// own separate activity stack instead of on the activity stack of
			// the application.
			try {
				pendingIntent.send(mActivity, 0 /* code */, intent);
			} catch (CanceledException e) {
				Modules.LOG.error(TAG, "error buy page starting activity");
			}
		}
	}

	/**
	 * Updates the UI after the database has been updated. This method runs in a background thread so it has to post a
	 * Runnable to run on the UI thread.
	 * 
	 * @param purchaseState
	 *            the purchase state of the item
	 * @param itemId
	 *            a string identifying the item
	 * @param quantity
	 *            the quantity of items in this purchase
	 */
	void postPurchaseStateChange(final PurchaseState purchaseState, final String itemId, final int quantity,
			final long purchaseTime, final String developerPayload) {
		mHandler.post(new Runnable() {
			public void run() {
				onPurchaseStateChange(purchaseState, itemId, quantity, purchaseTime, developerPayload);
			}
		});
	}

	private Dialog createDialog(Activity activity, int titleId, int messageId) {

		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle(titleId).setIcon(android.R.drawable.stat_sys_warning).setMessage(messageId)
				.setCancelable(false).setPositiveButton(android.R.string.ok, null);
		return builder.create();
	}

	private void logProductActivity(String product, String activity) {
		SpannableStringBuilder contents = new SpannableStringBuilder();
		contents.append(Html.fromHtml("<b>" + product + "</b>: "));
		contents.append(activity);
		contents.append('\n');
	}
}
