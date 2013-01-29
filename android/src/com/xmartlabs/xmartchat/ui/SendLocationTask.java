package com.xmartlabs.xmartchat.ui;

import android.location.Location;
import android.os.AsyncTask;
import com.codebutler.android_websockets.SocketIOClient;
import static com.xmartlabs.xmartchat.utils.LogUtils.LOGD;
import static com.xmartlabs.xmartchat.utils.LogUtils.LOGE;
import static com.xmartlabs.xmartchat.utils.LogUtils.makeLogTag;

import org.json.JSONArray;
import org.json.JSONObject;

public class SendLocationTask extends AsyncTask<Location, Void, Boolean> {

    private static final String TAG = makeLogTag(SendLocationTask.class);
    private SocketIOClient webSocket;

    public SendLocationTask(SocketIOClient client) {
        webSocket = client;
    }

    @Override
    protected Boolean doInBackground(Location... params) {
        try {
            JSONArray arguments = new JSONArray();

            for (int i = 0; i < params.length; i++) {
                JSONObject data = new JSONObject();
                double lat = params[i].getLatitude();
                double lng = params[i].getLongitude();
                data.put("lat", lat);
                data.put("lng", lng);
                arguments.put(data);
                LOGD(TAG, String.format("send location lat:%d lng:%d", lat, lng));
            }

            webSocket.emit("send location", arguments);
            return true;
        } catch (Exception e) {
            LOGE(TAG, "SendLocationThread: " + e.getMessage());
            return false;
        }
    }
}
