// Copyright 2010 Google Inc. All Rights Reserved.
package com.godsandtowers.billing.google;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.godsandtowers.billing.google.Consts.PurchaseState;
import com.godsandtowers.billing.util.Base64;
import com.godsandtowers.billing.util.Base64DecoderException;
import com.gundogstudios.modules.Modules;

public class Security {
	private static final String TAG = "Security";
	public static final int MASK = 234758624;
	private static final String KEY_FACTORY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
	private static final SecureRandom RANDOM = new SecureRandom();

	/**
	 * This keeps track of the nonces that we generated and sent to the server. We need to keep track of these until we
	 * get back the purchase state and send a confirmation message back to Android Market. If we are killed and lose
	 * this list of nonces, it is not fatal. Android Market will send us a new "notify" message and we will re-generate
	 * a new nonce. This has to be "static" so that the {@link BillingReceiver} can check if a nonce exists.
	 */
	private static HashSet<Long> sKnownNonces = new HashSet<Long>();

	/**
	 * A class to hold the verified purchase information.
	 */
	public static class VerifiedPurchase {
		public PurchaseState purchaseState;
		public String notificationId;
		public String productId;
		public String packageName;
		public String orderId;
		public long purchaseTime;
		public String developerPayload;

		public VerifiedPurchase(PurchaseState purchaseState, String notificationId, String productId,
				String packageName, String orderId, long purchaseTime, String developerPayload) {
			this.purchaseState = purchaseState;
			this.notificationId = notificationId;
			this.productId = productId;
			this.packageName = packageName;
			this.orderId = orderId;
			this.purchaseTime = purchaseTime;
			this.developerPayload = developerPayload;
		}
	}

	/** Generates a nonce (a random number used once). */
	public static long generateNonce() {
		long nonce = RANDOM.nextLong();
		sKnownNonces.add(nonce);
		return nonce;
	}

	public static void removeNonce(long nonce) {
		sKnownNonces.remove(nonce);
	}

	public static boolean isNonceKnown(long nonce) {
		return sKnownNonces.contains(nonce);
	}

	/**
	 * Verifies that the data was signed with the given signature, and returns the list of verified purchases. The data
	 * is in JSON format and contains a nonce (number used once) that we generated and that was signed (as part of the
	 * whole data string) with a private key. The data also contains the {@link PurchaseState} and product ID of the
	 * purchase. In the general case, there can be an array of purchase transactions because there may be delays in
	 * processing the purchase on the backend and then several purchases can be batched together.
	 * 
	 * @param signedData
	 *            the signed JSON string (signed, not encrypted)
	 * @param signature
	 *            the signature for the data, signed with the private key
	 */
	public static ArrayList<VerifiedPurchase> verifyPurchase(String signedData, String signature) {
		if (signedData == null) {
			Modules.LOG.error(TAG, "data is null");
			return null;
		}
		if (Consts.DEBUG) {
			Modules.LOG.info(TAG, "signedData: " + signedData);
		}
		boolean verified = false;
		if (!TextUtils.isEmpty(signature)) {
			/**
			 * Compute your public key (that you got from the Android Market publisher site).
			 * 
			 * Instead of just storing the entire literal string here embedded in the program, construct the key at
			 * runtime from pieces or use bit manipulation (for example, XOR with some other string) to hide the actual
			 * key. The key itself is not secret information, but we don't want to make it easy for an adversary to
			 * replace the public key with one of their own and then fake messages from the server.
			 * 
			 * Generally, encryption keys / passwords should only be kept in memory long enough to perform the operation
			 * they need to perform.
			 */
			String encryptedKey = "18,83,-6,-1,-1,-123,-2,-1,-2,117,-78,119,-74,-74,-74,-67,-66,-107,-67,-79,-108,-104,-105,-114,-106,-108,-58,-72,-49,-120,-66,-67,-70,-82,-66,-71,-80,-66,-66,-68,-57,-82,-78,-66,-74,-74,-68,-67,-76,-104,-66,-68,-70,-82,-51,-66,-100,-122,-106,-49,-88,-115,-113,-69,-48,-72,-89,-75,-52,-88,-111,-72,-90,-69,-78,-73,-86,-86,-105,-119,-66,-79,-80,-112,-86,-90,-90,-121,-72,-71,-101,-49,-76,-80,-111,-114,-82,-80,-106,-75,-84,-123,-118,-66,-98,-53,-109,-48,-101,-108,-89,-110,-49,-75,-49,-102,-98,-81,-121,-44,-67,-89,-53,-76,-85,-48,-118,-54,-70,-74,-118,-101,-119,-114,-66,-86,-108,-67,-72,-68,-85,-89,-104,-119,-80,-121,-81,-121,-68,-109,-107,-79,-54,-68,-75,-120,-49,-73,-83,-49,-77,-86,-54,-72,-110,-49,-83,-76,-44,-86,-82,-71,-99,-104,-50,-66,-69,-100,-108,-86,-115,-68,-99,-49,-49,-80,-89,-122,-117,-87,-58,-123,-68,-88,-70,-51,-73,-116,-102,-98,-112,-116,-102,-55,-69,-50,-71,-115,-108,-54,-113,-72,-72,-123,-116,-79,-53,-111,-85,-69,-68,-84,-123,-108,-70,-123,-86,-71,-90,-86,-66,-116,-74,-112,-99,-72,-73,-54,-74,-51,-108,-84,-86,-53,-119,-113,-113,-90,-74,-99,-113,-74,-69,-101,-81,-118,-108,-116,-55,-104,-83,-110,-79,-121,-88,-101,-118,-48,-110,-87,-51,-70,-86,-109,-70,-82,-67,-50,-99,-99,-115,-80,-83,-91,-122,-82,-110,-44,-105,-86,-67,-82,-107,-98,-111,-53,-72,-44,-78,-103,-87,-85,-99,-76,-55,-118,-48,-122,-89,-83,-76,-121,-52,-104,-50,-113,-55,-83,-88,-79,-116,-53,-67,-52,-77,-116,-71,-72,-118,-101,-123,-115,-100,-83,-66,-77,-69,-104,-88,-53,-57,-78,-78,-85,-115,-66,-70,-104,-103,-123,-111,-110,-102,-106,-84,-83,-73,-55,-72,-105,-67,-50,-98,-82,-49,-57,-103,-103,-54,-119,-99,-50,-90,-58,-117,-117,-117,-88,-116,-56,-106,-122,-109,-108,-72,-70,-102,-111,-49,-108,-103,-106,-106,-119,-72,-121,-74,-120,-66,-69,-66,-82,66,";
			String base64EncodedPublicKey;
			try {
				base64EncodedPublicKey = decrypt(encryptedKey);
			} catch (IOException e) {
				Modules.LOG.error(TAG, "unable to decrypt public key");
				return null;
			}
			PublicKey key = Security.generatePublicKey(base64EncodedPublicKey);
			verified = Security.verify(key, signedData, signature);
			if (!verified) {
				Log.w(TAG, "signature does not match data.");
				return null;
			}
		}

		JSONObject jObject;
		JSONArray jTransactionsArray = null;
		int numTransactions = 0;
		long nonce = 0L;
		try {
			jObject = new JSONObject(signedData);

			// The nonce might be null if the user backed out of the buy page.
			nonce = jObject.optLong("nonce");
			jTransactionsArray = jObject.optJSONArray("orders");
			if (jTransactionsArray != null) {
				numTransactions = jTransactionsArray.length();
			}
		} catch (JSONException e) {
			return null;
		}

		if (!Security.isNonceKnown(nonce)) {
			Log.w(TAG, "Nonce not found: " + nonce);
			return null;
		}

		ArrayList<VerifiedPurchase> purchases = new ArrayList<VerifiedPurchase>();
		try {
			for (int i = 0; i < numTransactions; i++) {
				JSONObject jElement = jTransactionsArray.getJSONObject(i);
				int response = jElement.getInt("purchaseState");
				PurchaseState purchaseState = PurchaseState.valueOf(response);
				String productId = jElement.getString("productId");
				String packageName = jElement.getString("packageName");
				long purchaseTime = jElement.getLong("purchaseTime");
				String orderId = jElement.optString("orderId", "");
				String notifyId = null;
				if (jElement.has("notificationId")) {
					notifyId = jElement.getString("notificationId");
				}
				String developerPayload = jElement.optString("developerPayload", null);

				// If the purchase state is PURCHASED, then we require a
				// verified nonce.
				if (purchaseState == PurchaseState.PURCHASED && !verified) {
					continue;
				}
				purchases.add(new VerifiedPurchase(purchaseState, notifyId, productId, packageName, orderId,
						purchaseTime, developerPayload));
			}
		} catch (JSONException e) {
			Modules.LOG.error(TAG, "JSON exception: " + e);
			return null;
		}
		removeNonce(nonce);
		return purchases;
	}

	private static String decrypt(String encryptedKey) throws IOException {
		String[] strings = encryptedKey.split(",");
		byte[] bytes = new byte[strings.length];
		for (int i = 0; i < strings.length; i++) {
			bytes[i] = Byte.parseByte(strings[i]);
		}
		decrypt(bytes, MASK);

		ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
		ObjectInputStream stream = new ObjectInputStream(byteStream);

		String key = stream.readUTF();
		return key;
	}

	private static void decrypt(byte[] bytes, int mask) {

		for (int i = 0; i < bytes.length - 1; i += 2) {
			bytes[i] = (byte) ~bytes[i];
			bytes[i + 1] = (byte) ~bytes[i + 1];
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i + 1] = (byte) (bytes[i] ^ bytes[i + 1]);
			bytes[i] = (byte) (bytes[i] ^ bytes[i + 1]);
		}
	}

	/**
	 * Generates a PublicKey instance from a string containing the Base64-encoded public key.
	 * 
	 * @param encodedPublicKey
	 *            Base64-encoded public key
	 * @throws IllegalArgumentException
	 *             if encodedPublicKey is invalid
	 */
	public static PublicKey generatePublicKey(String encodedPublicKey) {
		try {
			byte[] decodedKey = Base64.decode(encodedPublicKey);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_FACTORY_ALGORITHM);
			return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeySpecException e) {
			Modules.LOG.error(TAG, "Invalid key specification.");
			throw new IllegalArgumentException(e);
		} catch (Base64DecoderException e) {
			Modules.LOG.error(TAG, "Base64 decoding failed.");
			throw new IllegalArgumentException(e);
		}
	}

	/**
	 * Verifies that the signature from the server matches the computed signature on the data. Returns true if the data
	 * is correctly signed.
	 * 
	 * @param publicKey
	 *            public key associated with the developer account
	 * @param signedData
	 *            signed data from server
	 * @param signature
	 *            server signature
	 * @return true if the data and signature match
	 */
	public static boolean verify(PublicKey publicKey, String signedData, String signature) {
		if (Consts.DEBUG) {
			Modules.LOG.info(TAG, "signature: " + signature);
		}
		Signature sig;
		try {
			sig = Signature.getInstance(SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature))) {
				Modules.LOG.error(TAG, "Signature verification failed.");
				return false;
			}
			return true;
		} catch (NoSuchAlgorithmException e) {
			Modules.LOG.error(TAG, "NoSuchAlgorithmException.");
		} catch (InvalidKeyException e) {
			Modules.LOG.error(TAG, "Invalid key specification.");
		} catch (SignatureException e) {
			Modules.LOG.error(TAG, "Signature exception.");
		} catch (Base64DecoderException e) {
			Modules.LOG.error(TAG, "Base64 decoding failed.");
		}
		return false;
	}
}
