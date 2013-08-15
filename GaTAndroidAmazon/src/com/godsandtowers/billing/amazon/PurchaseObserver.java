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
package com.godsandtowers.billing.amazon;

import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.amazon.inapp.purchasing.BasePurchasingObserver;
import com.amazon.inapp.purchasing.GetUserIdResponse;
import com.amazon.inapp.purchasing.GetUserIdResponse.GetUserIdRequestStatus;
import com.amazon.inapp.purchasing.Item;
import com.amazon.inapp.purchasing.ItemDataResponse;
import com.amazon.inapp.purchasing.PurchaseResponse;
import com.amazon.inapp.purchasing.PurchaseUpdatesResponse;
import com.amazon.inapp.purchasing.PurchasingManager;
import com.amazon.inapp.purchasing.Receipt;
import com.gundogstudios.modules.Modules;

/**
 * Purchasing Observer will be called on by the Purchasing Manager asynchronously. Since the methods on the UI thread of
 * the application, all fulfillment logic is done via an AsyncTask. This way, any intensive processes will not hang the
 * UI thread and cause the application to become unresponsive.
 */
public class PurchaseObserver extends BasePurchasingObserver {

	private static final String TAG = "Amazon-IAP";
	private AmazonPurchaser purchaser;

	/**
	 * Creates new instance of the ButtonClickerObserver class.
	 * 
	 * @param buttonClickerActivity
	 *            Activity context
	 */
	public PurchaseObserver(Context context, AmazonPurchaser purchaser) {
		super(context);
		this.purchaser = purchaser;
	}

	/**
	 * Invoked once the observer is registered with the Puchasing Manager If the boolean is false, the application is
	 * receiving responses from the SDK Tester. If the boolean is true, the application is live in production.
	 * 
	 * @param isSandboxMode
	 *            Boolean value that shows if the app is live or not.
	 */
	@Override
	public void onSdkAvailable(final boolean isSandboxMode) {
		Modules.LOG.verbose(TAG, "onSdkAvailable recieved: Response -" + isSandboxMode);
		PurchasingManager.initiateGetUserIdRequest();
	}

	/**
	 * Invoked once the call from initiateGetUserIdRequest is completed. On a successful response, a response object is
	 * passed which contains the request id, request status, and the userid generated for your application.
	 * 
	 * @param getUserIdResponse
	 *            Response object containing the UserID
	 */
	@Override
	public void onGetUserIdResponse(final GetUserIdResponse getUserIdResponse) {
		Modules.LOG.verbose(TAG, "onGetUserIdResponse recieved: Response -" + getUserIdResponse);
		Modules.LOG.verbose(TAG, "RequestId:" + getUserIdResponse.getRequestId());
		Modules.LOG.verbose(TAG, "IdRequestStatus:" + getUserIdResponse.getUserIdRequestStatus());
		new GetUserIdAsyncTask().execute(getUserIdResponse);
	}

	/**
	 * Invoked once the call from initiateItemDataRequest is completed. On a successful response, a response object is
	 * passed which contains the request id, request status, and a set of item data for the requested skus. Items that
	 * have been suppressed or are unavailable will be returned in a set of unavailable skus.
	 * 
	 * @param itemDataResponse
	 *            Response object containing a set of purchasable/non-purchasable items
	 */
	@Override
	public void onItemDataResponse(final ItemDataResponse itemDataResponse) {
		Modules.LOG.verbose(TAG, "onItemDataResponse recieved");
		Modules.LOG.verbose(TAG, "ItemDataRequestStatus" + itemDataResponse.getItemDataRequestStatus());
		Modules.LOG.verbose(TAG, "ItemDataRequestId" + itemDataResponse.getRequestId());
		new ItemDataAsyncTask().execute(itemDataResponse);
	}

	/**
	 * Is invoked once the call from initiatePurchaseRequest is completed. On a successful response, a response object
	 * is passed which contains the request id, request status, and the receipt of the purchase.
	 * 
	 * @param purchaseResponse
	 *            Response object containing a receipt of a purchase
	 */
	@Override
	public void onPurchaseResponse(final PurchaseResponse purchaseResponse) {
		Modules.LOG.verbose(TAG, "onPurchaseResponse recieved");
		Modules.LOG.verbose(TAG, "PurchaseRequestStatus:" + purchaseResponse.getPurchaseRequestStatus());
		new PurchaseAsyncTask().execute(purchaseResponse);
	}

	/**
	 * Is invoked once the call from initiatePurchaseUpdatesRequest is completed. On a successful response, a response
	 * object is passed which contains the request id, request status, a set of previously purchased receipts, a set of
	 * revoked skus, and the next offset if applicable. If a user downloads your application to another device, this
	 * call is used to sync up this device with all the user's purchases.
	 * 
	 * @param purchaseUpdatesResponse
	 *            Response object containing the user's recent purchases.
	 */
	@Override
	public void onPurchaseUpdatesResponse(final PurchaseUpdatesResponse purchaseUpdatesResponse) {
		Modules.LOG.verbose(TAG, "onPurchaseUpdatesRecived recieved: Response -" + purchaseUpdatesResponse);
		Modules.LOG.verbose(TAG,
				"PurchaseUpdatesRequestStatus:" + purchaseUpdatesResponse.getPurchaseUpdatesRequestStatus());
		Modules.LOG.verbose(TAG, "RequestID:" + purchaseUpdatesResponse.getRequestId());
		new PurchaseUpdatesAsyncTask().execute(purchaseUpdatesResponse);
	}

	/*
	 * Started when the Observer receives a GetUserIdResponse. The Shared Preferences file for the returned user id is
	 * accessed.
	 */
	private class GetUserIdAsyncTask extends AsyncTask<GetUserIdResponse, Void, Boolean> {

		@Override
		protected Boolean doInBackground(GetUserIdResponse... params) {
			GetUserIdResponse getUserIdResponse = params[0];

			if (getUserIdResponse.getUserIdRequestStatus() == GetUserIdRequestStatus.SUCCESSFUL) {
				String userId = getUserIdResponse.getUserId();
				Modules.LOG.verbose(TAG, "onGetUserIdResponse: Got user ID: " + userId);
				return true;
			} else {
				Modules.LOG.verbose(TAG, "onGetUserIdResponse: Unable to get user ID.");
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean result) {
			super.onPostExecute(result);
			if (result) {
				// Call initiatePurchaseUpdatesRequest for the returned user to sync purchases that are not yet
				// fulfilled.
			}
		}
	}

	/*
	 * Started when the observer receives an Item Data Response. Takes the items and display them in the logs. You can
	 * use this information to display an in game storefront for your IAP items.
	 */
	private class ItemDataAsyncTask extends AsyncTask<ItemDataResponse, Void, Void> {
		@Override
		protected Void doInBackground(final ItemDataResponse... params) {
			final ItemDataResponse itemDataResponse = params[0];

			switch (itemDataResponse.getItemDataRequestStatus()) {
			case SUCCESSFUL_WITH_UNAVAILABLE_SKUS:
				// Skus that you can not purchase will be here.
				for (String s : itemDataResponse.getUnavailableSkus()) {
					Modules.LOG.verbose(TAG, "Unavailable SKU:" + s);
				}
			case SUCCESSFUL:
				// Information you'll want to display about your IAP items is here
				// In this example we'll simply log them.
				Map<String, Item> items = itemDataResponse.getItemData();
				for (String key : items.keySet()) {
					Item i = items.get(key);
					Modules.LOG.verbose(TAG, String.format(
							"Item: %s\n Type: %s\n SKU: %s\n Price: %s\n Description: %s\n", i.getTitle(),
							i.getItemType(), i.getSku(), i.getPrice(), i.getDescription()));
				}
				break;
			case FAILED:
				// On failed responses will fail gracefully.
				break;

			}

			return null;
		}
	}

	/*
	 * Started when the observer receives a Purchase Response Once the AsyncTask returns successfully, the UI is
	 * updated.
	 */
	private class PurchaseAsyncTask extends AsyncTask<PurchaseResponse, Void, Boolean> {

		@Override
		protected Boolean doInBackground(final PurchaseResponse... params) {
			try {
				PurchaseResponse purchaseResponse = params[0];

				Receipt receipt = purchaseResponse.getReceipt();
				if (receipt == null) {
					return false;
				}
				switch (purchaseResponse.getPurchaseRequestStatus()) {
				case SUCCESSFUL:
					purchaser.purchased(receipt.getSku(), "");
					return true;
				case ALREADY_ENTITLED:
					return true;
				case FAILED:
					/*
					 * If the purchase failed for some reason, (The customer canceled the order, or some other
					 * extraneous circumstance happens) the application ignores the request and logs the failure.
					 */
					Modules.LOG.verbose(TAG, "Failed purchase for request " + receipt.getSku() + " failed for user "
							+ purchaseResponse.getUserId());
					return false;
				case INVALID_SKU:
					/*
					 * If the sku that was purchased was invalid, the application ignores the request and logs the
					 * failure. This can happen when there is a sku mismatch between what is sent from the application
					 * and what currently exists on the dev portal.
					 */
					Modules.LOG.verbose(TAG, "Invalid Sku for request " + receipt.getSku());
					return false;
				}
			} catch (Exception e) {
				Modules.REPORTER.report(e);
				Modules.LOG.error(TAG, "Error in PurchaseAsyncTask: " + e.toString());
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			super.onPostExecute(success);
		}
	}

	/*
	 * Started when the observer receives a Purchase Updates Response Once the AsyncTask returns successfully, we'll
	 * update the UI.
	 */
	private class PurchaseUpdatesAsyncTask extends AsyncTask<PurchaseUpdatesResponse, Void, Boolean> {

		@Override
		protected Boolean doInBackground(final PurchaseUpdatesResponse... params) {
			final PurchaseUpdatesResponse purchaseUpdatesResponse = params[0];

			switch (purchaseUpdatesResponse.getPurchaseUpdatesRequestStatus()) {
			case SUCCESSFUL:
				for (final Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
					final String sku = receipt.getSku();

					switch (receipt.getItemType()) {
					case ENTITLED:
						purchaser.purchased(sku, "");
						break;
					case CONSUMABLE:
					case SUBSCRIPTION:
					default:
						break;
					}
					Modules.LOG.verbose(TAG, "Invalid Sku for request " + receipt.getSku());
				}

				return true;
			case FAILED:

				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			super.onPostExecute(success);
		}
	}
}
