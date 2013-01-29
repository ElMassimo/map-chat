/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xmartlabs.xmartchat.io;

import android.location.Location;
import com.codebutler.android_websockets.SocketIOClient;
import com.xmartlabs.xmartchat.ui.SendLocationTask;
import java.io.IOException;
import static com.xmartlabs.xmartchat.utils.LogUtils.makeLogTag;
import static com.xmartlabs.xmartchat.utils.LogUtils.LOGD;
import static com.xmartlabs.xmartchat.utils.LogUtils.LOGE;
import java.net.URI;
import org.json.JSONArray;

/**
 *
 * @author maximo
 */
public class ConnectionHelper {
    private static final String TAG = makeLogTag(ConnectionHelper.class);
    private static final String ServerUrl = "http://10.0.2.2:8080";

    private static SocketIOClient socketIO;

    public static boolean isConnected() {
        return socketIO != null;
    }

    public static boolean connect() {
        socketIO = new SocketIOClient(URI.create(ServerUrl), new SocketIOClient.Handler() {
            @Override
            public void onConnect() {
                LOGD(TAG, "Connected!");
            }

            @Override
            public void on(String event, JSONArray arguments) {
                LOGD(TAG, String.format("Got event %s: %s", event, arguments.toString()));
            }

            @Override
            public void onDisconnect(int code, String reason) {
                LOGD(TAG, String.format("Disconnected! Code: %d Reason: %s", code, reason));
            }

            @Override
            public void onError(Exception error) {
                LOGD(TAG, "Error!", error);
            }
        });
        socketIO.connect();
        return true;
    }

    public static boolean disconnect() {
        try {
            socketIO.disconnect();
            return true;
        } catch (IOException ex) {
            LOGE(TAG, "Error during disconnection", ex);
            return false;
        }
    }

    public static void sendLocation(Location location) {
        new SendLocationTask(socketIO).execute(location);
    }
}
