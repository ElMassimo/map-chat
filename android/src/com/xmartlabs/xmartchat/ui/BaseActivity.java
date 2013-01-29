package com.xmartlabs.xmartchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import com.xmartlabs.xmartchat.io.ConnectionHelper;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!ConnectionHelper.isConnected()) {
            Intent loginFlowIntent = new Intent(this, LoginActivity.class);
            loginFlowIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            loginFlowIntent.putExtra(LoginActivity.EXTRA_FINISH_INTENT, getIntent());
            startActivity(loginFlowIntent);
            finish();
        }
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpTo(this, new Intent(this, HomeActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
