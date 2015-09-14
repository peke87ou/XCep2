package com.irina.xcep.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.irina.xcep.R;

public class ShareSocialMediaActivity extends Activity {

	public static String TAG = ShareSocialMediaActivity.class.getName();

	private static final String PERMISSION = "publish_actions";

	public enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	public final String PENDING_ACTION_BUNDLE_KEY = "com.example.hellofacebook:PendingAction";
	ShareDialog shareDialog;
	public boolean canPresentShareDialog;
	public boolean canPresentShareDialogWithPhotos;
	public PendingAction pendingAction = PendingAction.NONE;
	private String mMessage, mTitle, mUrl, mUrlApp;

	
	/**
	 * Twitter
	 */
	
	public void shareTwitterPost(String message, String title, String url) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, title+"  "+ message+" "+Utils.urlGooglePlay);

		boolean twitterAppFound = false;
		List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
		for (ResolveInfo info : matches) {
			if (info.activityInfo.packageName.toLowerCase(Locale.getDefault()).startsWith("com.twitter")) {
				intent.setPackage(info.activityInfo.packageName);
				twitterAppFound = true;
				break;
			}
		}

		if (!twitterAppFound) {
			String urlToShare = "http://twitter.com/share?text=" + Utils.urlGooglePlay;
			intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToShare));
		}

		startActivity(intent);
	}
	
	
	
	/**
	 * Se comparte un mensaje en Facebook
	 * @param message mensaje del post
	 * @param title título del post
	 * @param url de una imagen para el post
	 */
	public void shareFacebookPost(String message, String title, String url) {

		mMessage = message;
		mTitle = title;
		mUrl = url;
		mUrlApp = null;
		
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		CallbackManager callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
			}

			@Override
			public void onCancel() {
				showAlert();
			}

			@Override
			public void onError(FacebookException exception) {
				showAlert();
			}
		});

		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
		canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);

		performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
	}
	
	
	/**
	 * Se comparte un mensaje en Facebook
	 * @param message mensaje del post
	 * @param title título del post
	 * @param url de una imagen para el post
	 */
	public void shareFacebookApp(String message, String title, String url) {

		mMessage = message;
		mTitle = title;
		mUrl = null;
		mUrlApp= url;
		
		FacebookSdk.sdkInitialize(this.getApplicationContext());
		CallbackManager callbackManager = CallbackManager.Factory.create();

		LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
				performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
			}

			@Override
			public void onCancel() {
				showAlert();
			}

			@Override
			public void onError(FacebookException exception) {
				showAlert();
			}
		});

		shareDialog = new ShareDialog(this);
		shareDialog.registerCallback(callbackManager, shareCallback);

		canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);
		canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);

		performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
	}

	private void postStatusUpdate() {
		Profile profile = Profile.getCurrentProfile();
		
		ShareLinkContent linkContent;
		
		if(mUrl !=null){
			linkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(Utils.urlGooglePlay))
				.setContentTitle(mTitle).setContentDescription(mMessage).setImageUrl(Uri.parse(mUrl)).build();
		}else{
			linkContent = new ShareLinkContent.Builder().setContentUrl(Uri.parse(mUrlApp))
					.setContentTitle(mTitle).setContentDescription(mMessage).build();
		}

		if (canPresentShareDialog) {
			shareDialog.show(linkContent);
		} else if (profile != null && hasPublishPermission()) {
			ShareApi.share(linkContent, shareCallback);
		} else {
			showAlert();
		}
	}

	private void postPhoto() {
		Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.cesta);
		SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
		ArrayList<SharePhoto> photos = new ArrayList<>();
		photos.add(sharePhoto);

		SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder().setPhotos(photos).build();
		if (canPresentShareDialogWithPhotos) {
			shareDialog.show(sharePhotoContent);
		} else if (hasPublishPermission()) {
			ShareApi.share(sharePhotoContent, shareCallback);
		} else {
			pendingAction = PendingAction.POST_PHOTO;
			// We need to get new permissions, then complete the action when we
			// get called back.
			LoginManager.getInstance().logInWithPublishPermissions(this, Arrays.asList(PERMISSION));
		}
	}

	private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
		@Override
		public void onCancel() {
			Log.d(TAG, getString(R.string.cancelouse));
		}

		@Override
		public void onError(FacebookException error) {
			Log.d(TAG, String.format("Erro: %s", error.toString()));
			String title = getString(R.string.erro);
			String alertMessage = error.getMessage();
			showResult(title, alertMessage);
		}

		@Override
		public void onSuccess(Sharer.Result result) {
			Log.d(TAG, "Éxito!");
			if (result.getPostId() != null) {
				String title = getString(R.string.facebook);
				String alertMessage = getString(R.string.publicouse_a_tua_mensaxe);
				showResult(title, alertMessage);
			}
		}

		private void showResult(String title, String alertMessage) {
			new AlertDialog.Builder(ShareSocialMediaActivity.this).setTitle(title).setMessage(alertMessage).setPositiveButton(getString(R.string.aceptar), null).show();
		}
	};

	private boolean hasPublishPermission() {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		return accessToken != null && accessToken.getPermissions().contains("publish_actions");
	}

	private void performPublish(PendingAction action, boolean allowNoToken) {
		AccessToken accessToken = AccessToken.getCurrentAccessToken();
		if (accessToken != null || allowNoToken) {
			pendingAction = action;
			handlePendingAction();
		}
	}

	private void handlePendingAction() {
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case NONE:
			break;
		case POST_PHOTO:
			postPhoto();
			break;
		case POST_STATUS_UPDATE:
			postStatusUpdate();
			break;
		}
	}

	private void showAlert() {
		Log.d(TAG, "Erro. Ir a ShowAlert");
		new AlertDialog.Builder(ShareSocialMediaActivity.this).setTitle(getString(R.string.erro)).setMessage(R.string.erro_en_permisos).setPositiveButton(R.string.aceptar, null).show();
	}

}
