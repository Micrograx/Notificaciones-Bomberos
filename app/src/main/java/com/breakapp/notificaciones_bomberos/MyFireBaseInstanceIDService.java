package com.breakapp.notificaciones_bomberos;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Usuario on 14/05/2017.
 */
public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {


    private static final String TAG = MyFireBaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }
// [END refresh_token]


    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }}
