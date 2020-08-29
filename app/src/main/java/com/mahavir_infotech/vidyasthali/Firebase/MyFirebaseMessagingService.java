package com.mahavir_infotech.vidyasthali.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mahavir_infotech.vidyasthali.R;
import com.mahavir_infotech.vidyasthali.Utility.SavedData;
import com.mahavir_infotech.vidyasthali.activity.Student.ChatActivity;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private NotificationUtils notificationUtils;


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        SavedData.saveTokan(s);
        Log.e("", "Refreshed token:" + s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            Log.e(TAG, "From data: " + remoteMessage.getData());
            // handleNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody().toString(), remoteMessage.getNotification().getTitle().toString(), remoteMessage.getNotification().getIcon().toString());
            String Response = remoteMessage.getData().toString();
            String separated = Response.replace("{body=", "");
            JSONObject jsonObject1 = new JSONObject(separated);
          /*  //JSONObject jsonObject1 = jsonObject.getJSONObject("body");
            //JSONObject jsonObject2 = jsonObject1.getJSONObject("message");*/
            New_Chating(jsonObject1.getString("sender_role"),jsonObject1.getString("receiver_role"),jsonObject1.getString("receiver_id"),jsonObject1.getString("sender_id"),jsonObject1.getString("receiver_name"),jsonObject1.getString("message"),jsonObject1.getString("receiver_profile"));
       } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "From data: " + e.toString());
        }
    }
    private void New_Chating(String sender_role, String receiver_role, String receiver_id, String sender_id, String receiver_name, String message,String reciver_pic) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.Chating);
            pushNotification.putExtra("sender_role", sender_role);
            pushNotification.putExtra("receiver_role", receiver_role);
            pushNotification.putExtra("receiver_id", receiver_id);
            pushNotification.putExtra("sender_id", sender_id);
            pushNotification.putExtra("receiver_name", receiver_name);
            pushNotification.putExtra("message", message);
            pushNotification.putExtra("reciver_pic", reciver_pic);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                final String ANDROID_CHANNEL_ID = "com.mahavir_infotech.vidyasthali.ANDROID";
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("sender_role", sender_role);
                intent.putExtra("receiver_role", receiver_role);
                intent.putExtra("receiver_id", receiver_id);
                intent.putExtra("sender_id", sender_id);
                intent.putExtra("receiver_name", receiver_name);
                intent.putExtra("message", message);
                intent.putExtra("check", "Notification");
                intent.putExtra("reciver_pic", reciver_pic);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());
                Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).
                        setSmallIcon(R.drawable.ic_launcher).setContentTitle(receiver_name).setContentText(message).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                mNotificationUtils.getManager().notify(0, nb.build());
               /* NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();*/
            } else {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("sender_role", sender_role);
                intent.putExtra("receiver_role", receiver_role);
                intent.putExtra("receiver_id", receiver_id);
                intent.putExtra("sender_id", sender_id);
                intent.putExtra("receiver_name", receiver_name);
                intent.putExtra("message", message);
                intent.putExtra("check", "Notification");
                intent.putExtra("reciver_pic", reciver_pic);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).
                        setSmallIcon(R.drawable.ic_launcher).setContentTitle(receiver_name).setContentText(message).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
               /* NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();*/
            }
        }
    }

   /* private void handleNotification(String message, String content, String title, String ImagePath) {
        try {
            int cart_item = Integer.parseInt(SavedData.getAddToCart_Count()) + 1;
            SavedData.saveAddToCart_Count(String.valueOf(cart_item));
            Notification_Model notification_model=new Notification_Model();
            notification_model.setTitle(title);
            notification_model.setBody(content);
            notification_model.setImage_url(ImagePath);
            notification_model.setCollection_id(message.substring(message.lastIndexOf("/")).replaceAll("/", ""));
            NotificationHelper.getInstance().insertNotification_Model(notification_model);
        } catch (Exception e) {}

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = null;
            pushNotification = new Intent(Config.Update);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }

        ErrorMessage.E("Exception handleNotification" + getBitmapFromURL(ImagePath));
        *//*try {
            InputStream in = new URL(Icon).openStream();
            bmp = BitmapFactory.decodeStream(in);
            ErrorMessage.E("Exception>>"+bmp);
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.E("Exception"+e.toString());
        }*//*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                ErrorMessage.E("if is working");
                final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
                Intent intent = new Intent(this, LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());

                Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setLargeIcon(getBitmapFromURL(ImagePath)).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                mNotificationUtils.getManager().notify(0, nb.build());
            }else {
                ErrorMessage.E("Else is working");
                final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
                Intent intent = new Intent(this, LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());

                Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                mNotificationUtils.getManager().notify(0, nb.build());
            }
        } else {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                final int icon = R.mipmap.ic_launcher_round;
                final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
                bigPictureStyle.setBigContentTitle(title);
                bigPictureStyle.setSummaryText(content);
                bigPictureStyle.bigPicture(getBitmapFromURL(ImagePath));
                Notification notification;
                notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher_round).setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setContentText(content).setContentIntent(pendingIntent).setStyle(bigPictureStyle).setSmallIcon(R.mipmap.ic_launcher_round).setLargeIcon(BitmapFactory.decodeResource(this.getResources(), icon)).build();

                NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(Config.NOTIFICATION_ID_BIG_IMAGE, notification);
            }else {
                Intent intent = new Intent(getApplicationContext(), LatestFeedDetailActivity.class);
                intent.putExtra("message", message);
                intent.putExtra("Name", "Firebase");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this).
                        setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle(title).setContentText(content).setAutoCancel(true).setSound(defaultSoundUri).setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, notificationBuilder.build());
            }
        }
      *//*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String ANDROID_CHANNEL_ID = "com.numerical.numerical.ANDROID";
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("Name", "Firebase");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NetworkUtileforOreao mNotificationUtils = new NetworkUtileforOreao(getApplicationContext());
            //Notification.Builder nb = mNotificationUtils.getAndroidChannelNotification("Kamino", "By " + "Burhan");
            remoteViews.setTextViewText(R.id.title, title);
            remoteViews.setTextViewText(R.id.text, content);
            remoteViews.setImageViewBitmap(R.id.Notification_tem_img, getBitmapFromURL(ImagePath));
           // Glide.with(getApplicationContext()).load(ImagePath).into(remoteViews.setImageViewBitmap(););

            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            Notification.Builder nb = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID).setContent(remoteViews).setContentIntent(pIntent).setSmallIcon(R.mipmap.ic_launcher_round).setAutoCancel(true);
            mNotificationUtils.getManager().notify(0, nb.build());
        } else {
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.customnotification);
            remoteViews.setTextViewText(R.id.title, title);
            remoteViews.setTextViewText(R.id.text, content);
            remoteViews.setImageViewBitmap(R.id.Notification_tem_img, getBitmapFromURL(ImagePath));

            Intent intent = new Intent(this, LatestFeedDetailActivity.class);
            intent.putExtra("message", message);
            intent.putExtra("Name", "Firebase");
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    // Set Icon
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    // Set Ticker Message
                    //.setTicker(getString(R.string.customnotificationticker))
                    // Dismiss Notification
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setAutoCancel(true)
                    // Set PendingIntent into Notification
                    .setContentIntent(pIntent).setAutoCancel(true)
                    // Set RemoteViews into Notification
                    .setContent(remoteViews);



            //remoteViews.setTextViewText(R.id.text, source + " To " + destination);

            // Create Notification Manager
            NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Build Notification with Notification Manager
            notificationmanager.notify(0, builder.build());
            *//**//*=====================================*//**//*
        }*//*


    }*/

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}

