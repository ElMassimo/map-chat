package com.xmartlabs.xmartchat.ui;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import com.xmartlabs.xmartchat.io.ConnectionHelper;
import com.xmartlabs.xmartchat.utils.HelpUtils;
import com.xmartlabs.xmartchat.utils.LocationUtils;
import com.xmartlabs.xmartchat.utils.LogUtils;
import com.xmartlabs.xmartchat.R;

public class HomeActivity extends BaseActivity implements
        ActionBar.TabListener,
        ViewPager.OnPageChangeListener,
        ChatFragment.ChatFragmentCallbacks {

    private static final String TAG = LogUtils.makeLogTag(HomeActivity.class);

    private Object mSyncObserverHandle;

    private ChatFragment chatFragment;
    private ChatMapFragment mapFragment;

    ViewPager viewPager;
    private Menu mOptionsMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isFinishing()) {
            return;
        }

        setContentView(R.layout.activity_home);

        viewPager = (ViewPager) findViewById(R.id.pager);
        if (viewPager != null) {
            // Phone setup
            viewPager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
            viewPager.setOnPageChangeListener(this);

            final ActionBar actionBar = getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.addTab(actionBar.newTab()
                    .setText(R.string.title_chat)
                    .setTabListener(this));
            actionBar.addTab(actionBar.newTab()
                    .setText(R.string.title_map)
                    .setTabListener(this));
        }

        LocationUtils.setupLocationManager(this);

        getActionBar().setHomeButtonEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ConnectionHelper.disconnect();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Since the pager fragments don't have known tags or IDs, the only way to persist the
        // reference is to use putFragment/getFragment. Remember, we're not persisting the exact
        // Fragment instance. This mechanism simply gives us a way to persist access to the
        // 'current' fragment instance for the given fragment (which changes across orientation
        // changes).
        //
        // The outcome of all this is that the "Refresh" menu button refreshes the stream across
        // orientation changes.
        switch (viewPager.getCurrentItem()) {
            case CHAT_FRAGMENT:
                if (chatFragment != null) {
                    getSupportFragmentManager().putFragment(outState,
                            "chat_fragment", chatFragment);
                }
            case MAP_FRAGMENT:
                if (mapFragment != null) {
                    getSupportFragmentManager().putFragment(outState,
                            "map_fragment", mapFragment);
                }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (chatFragment == null) {
            chatFragment = (ChatFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "chat_fragment");
        }
        if (mapFragment == null) {
            mapFragment = (ChatMapFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "map_fragment");
        }
    }

    public void onChatMessageSelected(String id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //<editor-fold desc="Menu" defaultstate="collapsed">
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        mOptionsMenu = menu;
        getMenuInflater().inflate(R.menu.home, menu);
        setupSearchMenuItem(menu);
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupSearchMenuItem(Menu menu) {
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                //triggerRefresh();
                return true;

            case R.id.menu_search:
                startSearch(null, false, Bundle.EMPTY, false);
                return true;

            case R.id.menu_about:
                HelpUtils.showAbout(this);
                return true;

            case R.id.menu_sign_out:
                ConnectionHelper.disconnect();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //</editor-fold>

    //<editor-fold desc="Tabs and Paging" defaultstate="collapsed">
    private static final int CHAT_FRAGMENT = 0;
    private static final int MAP_FRAGMENT = 1;
    private static final int TABS_COUNT = 2;

    private class HomePagerAdapter extends FragmentPagerAdapter {

        public HomePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case CHAT_FRAGMENT:
                    return (chatFragment = new ChatFragment());
                case MAP_FRAGMENT:
                    return (mapFragment = new ChatMapFragment());
            }
            return null;
        }

        @Override
        public int getCount() {
            return TABS_COUNT;
        }
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onPageSelected(int position) {
        getActionBar().setSelectedNavigationItem(position);

    }
    //</editor-fold>

    //<editor-fold desc="Unused pager/tab methods" defaultstate="collapsed">
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }
    //</editor-fold>
}