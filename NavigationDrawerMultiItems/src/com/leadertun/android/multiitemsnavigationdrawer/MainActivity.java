package com.leadertun.android.multiitemsnavigationdrawer;

import java.util.ArrayList;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leadertun.android.multiitemsnavigationdrawer.adapter.customMultiItemsAdapter;
import com.leadertun.android.multiitemsnavigationdrawer.event.MultiItemDrawerEvents;
import com.leadertun.android.multiitemsnavigationdrawer.fragment.MyFragment;
import com.leadertun.android.multiitemsnavigationdrawer.weather.JSONWeatherParser;
import com.leadertun.android.multiitemsnavigationdrawer.weather.WeatherHttpClient;
import com.leadertun.android.multiitemsnavigationdrawer.wrapper.ItemWrapper;
import com.leadertun.android.multiitemsnavigationdrawer.wrapper.WeatherWrapper;

import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private ItemWrapper mItemWrapper;
    private ArrayList<ItemWrapper> mListItemWrapper;
    private Fragment fragment;

    private TextView mCity;
    private TextView mTemperature;

    private ActionBar mActionBar;

    private static int ADD_ACCOUNT = 0;
    private static int ALL_CALENDARS = 1;
    private static int MY_CALENDARS = 2;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mNameTitles;

    private ArrayList<String> itemDrawer;

    private CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        initActionBar();

        fragment = new MyFragment();

        mTitle = mDrawerTitle = getTitle();
        mNameTitles = getResources().getStringArray(R.array.names_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mCheckBox = (CheckBox) findViewById(R.id.check);

        itemDrawer = new ArrayList<String>();

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        customMultiItemsAdapter multiItemsAdapter = CreateAdapterForMultiItems(8);

        mDrawerList.setAdapter(multiItemsAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();

                fragment = new MyFragment();

                EventBus.getDefault()
                        .post(new MultiItemDrawerEvents.MoveToFragmentEvent(
                                fragment));

            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);

                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        

    }

   

    private void initActionBar() {
        mActionBar = getActionBar();
        mActionBar.hide();

    }

    private customMultiItemsAdapter CreateAdapterForMultiItems(
            int numborOfObject) {

        customMultiItemsAdapter multiItemsAdapter = new customMultiItemsAdapter(
                getApplicationContext());

        int j = 1;
        multiItemsAdapter.addSeparatorItem("add account");

        multiItemsAdapter.addItem("All Calendars");
        multiItemsAdapter.addItem("My Calendars");
        multiItemsAdapter.addItem("My Tasks");

        /*
         * for (int i = 1; i < numborOfObject; i++) {
         * multiItemsAdapter.addItem("item " + i); if (i % 4 == 0) { j++;
         * multiItemsAdapter.addSeparatorItem("separator " + j); } }
         */

        return multiItemsAdapter;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
        case R.id.action_websearch:

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available,
                        Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                long id) {
            switch (position) {
            case 0:
                Log.v("adnen", "add account");
                addAccount(0);
                break;
            case 1:
                Log.v("adnen", "all calendars");
                selectItem(1);
                break;
            case 2:
                Log.v("adnen", "my calendars");
                selectItem(2);
                break;
            case 3:
                Log.v("adnen", "my calendars");
                selectItem(3);
                break;
            default:
                break;
            }

            // startFragment();

        }

    }

    public void getChekedItem(int position) {

        Log.v("adnen", "checked box " + position);
        mCheckBox.getText();

    }

    public void onEvent(MultiItemDrawerEvents.ItemClickEvent c) {

        if (c.getItemWrapper() instanceof ItemWrapper) {
            Log.v("adnen", "name get from Adapter = "
                    + c.getItemWrapper().getName());
            mItemWrapper = c.getItemWrapper();
        }

        if (c.getListItemWrapper() instanceof ArrayList<?>) {
            Log.v("adnen", "name get from list in Adapter = "
                    + c.getListItemWrapper().get(0).getName());
            mItemWrapper = c.getItemWrapper();
            mListItemWrapper = c.getListItemWrapper();
        }

        // TODO notify fragment with this changement & update list of calendar

    }

    public void onEvent(MultiItemDrawerEvents.MoveToFragmentEvent e) {

        if (e.getFragment() instanceof MyFragment) {

            Bundle args = new Bundle();
            Log.v("adnen", "name before send to fragment "
                    + mListItemWrapper.get(0).getName());
            args.putString(MyFragment.ARG_NAME_VALUE, mListItemWrapper.get(0)
                    .getName());
            args.putString(MyFragment.ARG_NAME_STATE,
                    Boolean.toString(mListItemWrapper.get(0).isSelected()));

            args.putSerializable(MyFragment.ARG_LIST_CALENDARS,
                    mListItemWrapper);
            fragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, e.getFragment())
                    .addToBackStack(null).commit();

            mDrawerList.setItemChecked(e.getPosition(), true);
            setTitle(mNameTitles[e.getPosition()]);
            
            

        }

    }

    private void addAccount(int position) {

        Bundle args = new Bundle();
        args.putString(MyFragment.ARG_NAME_VALUE, "add account");
        fragment.setArguments(args);

        EventBus.getDefault().post(
                new MultiItemDrawerEvents.MoveToFragmentEvent(fragment,
                        position));

        // FragmentManager fragmentManager = getFragmentManager();
        // fragmentManager.beginTransaction()
        // .replace(R.id.content_frame, fragment).commit();

        mDrawerLayout.closeDrawer(mDrawerList);

    }

    private void selectItem(int position) {

        EventBus.getDefault().post(
                new MultiItemDrawerEvents.MoveToFragmentEvent(fragment,
                        position));

        Bundle args = new Bundle();
        args.putInt(MyFragment.ARG_NAME_STATE, position);
        args.putString("addaccount", "position " + Integer.toString(position));
        fragment.setArguments(args);

        if (mDrawerList != null) {
            mDrawerList.setItemChecked(position, true);
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mDrawerToggle.syncState();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    

}