/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xmartlabs.xmartchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.xmartlabs.xmartchat.io.ConnectionHelper;
import com.xmartlabs.xmartchat.R;

/**
 *
 * @author maximo
 */
public class LoginActivity extends Activity {

    public static final String EXTRA_FINISH_INTENT = "com.xmartlabs.xmartchat.extra.FINISH_INTENT";
    private Intent finishIntent;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        if (getIntent().hasExtra(EXTRA_FINISH_INTENT)) {
            finishIntent = getIntent().getParcelableExtra(EXTRA_FINISH_INTENT);
        }
    }

    public void onLoginButtonClick(View button) {
        if(ConnectionHelper.connect()) {
            onConnectionAvailable();
        } else {
            Toast message = Toast.makeText(getApplicationContext(), R.string.error_connection, 2000);
            message.show();
        }
    }

    private void onConnectionAvailable() {

        if (finishIntent != null) {
            finishIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            finishIntent.setAction(Intent.ACTION_MAIN);
            finishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(finishIntent);
        }

        finish();
    }
}
