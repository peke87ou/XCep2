package com.irina.xcep.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
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

public class ShareSocialMediaActivity extends Activity{

	   private static final String PERMISSION = "publish_actions";
	    private static final Location SEATTLE_LOCATION = new Location("") {
	        {
	            setLatitude(47.6097);
	            setLongitude(-122.3331);
	        }
	    };

	    public final String PENDING_ACTION_BUNDLE_KEY =
	            "com.example.hellofacebook:PendingAction";
	    ShareDialog shareDialog;
	    public boolean canPresentShareDialog;
	    public boolean canPresentShareDialogWithPhotos;
	    public PendingAction pendingAction = PendingAction.NONE;
	    
	    public enum PendingAction {
	        NONE,
	        POST_PHOTO,
	        POST_STATUS_UPDATE
	    }
	
	    
	    
	    public void shareFacebookPost(){
			
			/*String urlToShare = "extra text";//"https://play.google.com/store/apps/details?id=com.bandainamcogames.dbzdokkanww";
	    	Intent intent = new Intent(Intent.ACTION_SEND);
	    	intent.setType("text/html");
	    	//intent.putExtra(Intent.EXTRA_SUBJECT, "Se comparte "+getIntent().getExtras().getString("NOMEPRODUCTO"));
	    	intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<p>jose</> jose"));
	    	
	    	
	    	boolean facebookAppFound = false;
	    	List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
	    	for (ResolveInfo info : matches) {
	    	    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
	    	        intent.setPackage(info.activityInfo.packageName);
	    	        facebookAppFound = true;
	    	        break;
	    	    }
	    	}

	    	if (!facebookAppFound) {
	    	    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
	    	    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
	    	    
	    	}

	    	startActivity(intent);*/
			
			FacebookSdk.sdkInitialize(this.getApplicationContext());
			CallbackManager callbackManager = CallbackManager.Factory.create();
			
			
			LoginManager.getInstance().registerCallback(callbackManager,
	                new FacebookCallback<LoginResult>() {
	                    @Override
	                    public void onSuccess(LoginResult loginResult) {
	                        //Ejecutar acción de post
	                        onClickPostStatusUpdate();
	                    }

	                    @Override
	                    public void onCancel() {
	                        //Mostrar alerta cancelar
	                    	showAlert();
	                    }

	                    @Override
	                    public void onError(FacebookException exception) {
	                        //Mostrar alerta error
	                    	showAlert();
	                    }

	                    private void showAlert() {
	                        new AlertDialog.Builder(ShareSocialMediaActivity.this)
	                                .setTitle("Erro")
	                                .setMessage("Erro en permisos")
	                                .setPositiveButton("Aceptar", null)
	                                .show();
	                    }
	                });

			shareDialog = new ShareDialog(this);
	        shareDialog.registerCallback(
	                callbackManager,
	                shareCallback);
	        
	        // Can we present the share dialog for regular links?
	        canPresentShareDialog = ShareDialog.canShow(ShareLinkContent.class);

	        // Can we present the share dialog for photos?
	        canPresentShareDialogWithPhotos = ShareDialog.canShow(SharePhotoContent.class);

	        onClickPostStatusUpdate();
		}
		
		private void onClickPostStatusUpdate() {
	        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareDialog);
	    }

		
	    private void postStatusUpdate() {
	        Profile profile = Profile.getCurrentProfile();
	        ShareLinkContent linkContent = new ShareLinkContent.Builder()
	                .setContentTitle("Hello Facebook")
	                .setContentDescription(
	                        "The 'Hello Facebook' sample  showcases simple Facebook integration")
	                .setContentUrl(Uri.parse("http://developers.facebook.com/docs/android"))
	                .build();
	        if (canPresentShareDialog) {
	            shareDialog.show(linkContent);
	        } else if (profile != null && hasPublishPermission()) {
	            ShareApi.share(linkContent, shareCallback);
	        } else {
	            
	        }
	    }
	    
	    private void postPhoto() {
	        Bitmap image = BitmapFactory.decodeResource(this.getResources(), R.drawable.cesta);
	        SharePhoto sharePhoto = new SharePhoto.Builder().setBitmap(image).build();
	        ArrayList<SharePhoto> photos = new ArrayList<>();
	        photos.add(sharePhoto);

	        SharePhotoContent sharePhotoContent =
	                new SharePhotoContent.Builder().setPhotos(photos).build();
	        if (canPresentShareDialogWithPhotos) {
	            shareDialog.show(sharePhotoContent);
	        } else if (hasPublishPermission()) {
	            ShareApi.share(sharePhotoContent, shareCallback);
	        } else {
	            pendingAction = PendingAction.POST_PHOTO;
	            // We need to get new permissions, then complete the action when we get called back.
	            LoginManager.getInstance().logInWithPublishPermissions(
	                    this,
	                    Arrays.asList(PERMISSION));
	        }
	    }
		
		private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
	        @Override
	        public void onCancel() {
	            Log.d("HelloFacebook", "Canceled");
	        }

	        @Override
	        public void onError(FacebookException error) {
	            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
	            String title = "Error";
	            String alertMessage = error.getMessage();
	            showResult(title, alertMessage);
	        }

	        @Override
	        public void onSuccess(Sharer.Result result) {
	            Log.d("HelloFacebook", "Success!");
	            if (result.getPostId() != null) {
	                String title = "Éxito";
	                String id = result.getPostId();
	                String alertMessage = "Se ha publicado correctamente";
	                showResult(title, alertMessage);
	            }
	        }

	        private void showResult(String title, String alertMessage) {
	            new AlertDialog.Builder(ShareSocialMediaActivity.this)
	                    .setTitle(title)
	                    .setMessage(alertMessage)
	                    .setPositiveButton("ok", null)
	                    .show();
	        }
	    };
		
		public void shareTwitter(){
			
			String urlToShare = "https://play.google.com/store/apps/details?id=com.bandainamcogames.dbzdokkanww";
	    	Intent intent = new Intent(Intent.ACTION_SEND);
	    	intent.setType("text/plain");
	    	intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

	    	boolean twitterAppFound = false;
	    	List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
	    	for (ResolveInfo info : matches) {
	    	    if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
	    	        intent.setPackage(info.activityInfo.packageName);
	    	        twitterAppFound = true;
	    	        break;
	    	    }
	    	}

	    	if (!twitterAppFound) {
	    	    urlToShare = "http://twitter.com/share?text="+urlToShare;
	    	    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlToShare));
	    	}
	    	
	    	startActivity(intent);
		}
		
		
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
	        // These actions may re-set pendingAction if they are still pending, but we assume they
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
}
