package com.dimitri.remoiville.go4lunch.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.dimitri.remoiville.go4lunch.BuildConfig;
import com.dimitri.remoiville.go4lunch.R;
import com.dimitri.remoiville.go4lunch.model.PlaceRestaurant;
import com.dimitri.remoiville.go4lunch.model.PlaceDetailsPOJO;
import com.dimitri.remoiville.go4lunch.model.User;
import com.dimitri.remoiville.go4lunch.source.remote.ServicePlacesApiGenerator;
import com.dimitri.remoiville.go4lunch.source.repository.PlacesRepository;
import com.dimitri.remoiville.go4lunch.source.repository.UserFirestoreRepository;
import com.dimitri.remoiville.go4lunch.view.activity.DetailsPlaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 10;
    private final String NOTIFICATION_TAG = "GO4LUNCH";
    private final String API_KEY = BuildConfig.API_KEY;

    private final PlacesRepository mPlacesRepository = new PlacesRepository();
    private final UserFirestoreRepository mUserFirestoreRepository = new UserFirestoreRepository();
    private User mCurrentUser;

    private static final String TAG = "NotificationsService";

    public NotificationsService() {
        super();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // set current user
        mUserFirestoreRepository.getUser(FirebaseAuth.getInstance().getUid()).addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                mCurrentUser = documentSnapshot.toObject(User.class);
                if (remoteMessage.getNotification() != null && mCurrentUser != null) {
                    if (remoteMessage.getNotification().getTitle() != null) {
                        String title = remoteMessage.getNotification().getTitle();
                        if (title.equals("Today lunch") && mCurrentUser.getRestaurantID() != null) {
                            getRestaurantDetails(mCurrentUser.getRestaurantID());
                        }
                    }
                }
            } else {
                mCurrentUser = null;
            }
        });
    }

    private void getRestaurantDetails(String placeId) {
        Call<PlaceDetailsPOJO> restaurantDetailsPOJOOut = ServicePlacesApiGenerator.getRequestGoogleApi().getPlaceDetails(placeId,API_KEY);
        restaurantDetailsPOJOOut.enqueue(new Callback<PlaceDetailsPOJO>() {
            @Override
            public void onResponse(Call<PlaceDetailsPOJO> call, Response<PlaceDetailsPOJO> response) {
                PlaceRestaurant currentPlace = new PlaceRestaurant(response.body().getResult(), API_KEY);
                sendNotification(currentPlace);
            }

            @Override
            public void onFailure(Call<PlaceDetailsPOJO> call, Throwable t) {
                Log.d(TAG, "onFailure: Error call API Places Details " + t.getMessage());
            }
        });
    }

    private void sendNotification(PlaceRestaurant currentPlace) {

        // Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, DetailsPlaceActivity.class);
        intent.putExtra("placeId", mCurrentUser.getRestaurantID());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String message = currentPlace.getName() + " : " + currentPlace.getAddress();

        // Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(message);

        // Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo_go4lunch)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);


        // Create the notification channel Version >= Android 8
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
    }
}
