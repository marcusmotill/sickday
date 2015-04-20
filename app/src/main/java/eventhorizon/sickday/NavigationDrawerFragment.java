package eventhorizon.sickday;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    public static ListView mDrawerListView;
    private View mFragmentContainerView;
    private static TextView tvProfileName;
    static int listViewHeight;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List expandableListTitle;


    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;
    AutoResizeTextView tvlogOff;

    ScrollView menuScroll;
    RelativeLayout profileHeader;
    RelativeLayout parentView;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);


        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainDrawer = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        mDrawerListView = (ListView) mainDrawer.findViewById(R.id.navDrawerListView);
        tvProfileName = (TextView) mainDrawer.findViewById(R.id.profileName);
        tvProfileName.setTypeface(App.caecilia);
        updateProfileName();

        tvlogOff = (AutoResizeTextView) mainDrawer.findViewById(R.id.logOffButton);
        tvlogOff.setOnClickListener(this);
        tvlogOff.setTypeface(App.caecilia);

        mDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        mDrawerListView.setAdapter(new NavigationDrawerAdapter(getActivity().getApplicationContext(),
                new String[]{getString(R.string.home_menu_item),
                        getString(R.string.profile_menu_item)}));
        mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);


        menuScroll = (ScrollView) mainDrawer.findViewById(R.id.menuScrollView);
        profileHeader = (RelativeLayout) mainDrawer.findViewById(R.id.profileHolder);
        parentView = (RelativeLayout) mainDrawer.findViewById(R.id.menuParent);

        mDrawerListView.getLayoutParams().height = dpToPx(123);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int screenHeight = size.y;
        int screenWidth = size.x;


        HashMap expandableListDetail = new HashMap();

        List menuItems = new ArrayList();
        menuItems.add("About us");
        menuItems.add("Help/FAQs");
        menuItems.add("Conditions we treat");
        menuItems.add("Disclaimer");

        expandableListDetail.put("More Information", menuItems);

        expandableListView = (ExpandableListView) mainDrawer.findViewById(R.id.expandable_menu);
        expandableListTitle = new ArrayList(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setGroupIndicator(null);

        expandableListView.getLayoutParams().height = screenHeight - profileHeader.getLayoutParams().height - mDrawerListView.getLayoutParams().height - tvlogOff.getLayoutParams().height;


        Log.i("Scroll Height", "" + menuScroll.getLayoutParams().height);
        Log.i("List Height", "" + mDrawerListView.getLayoutParams().height);
        Log.i("Expand Height", "" + expandableListView.getLayoutParams().height);


        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                if(childPosition == 0){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new AboutUs_Fragment())
                            .commit();
                }else if(childPosition == 1){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FAQ_Fragment())
                            .commit();

                }else if(childPosition == 2){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new Conditions_Fragment())
                            .commit();
                }else if(childPosition == 3){
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new Disclaimer_Fragment())
                            .commit();
                }
                mDrawerLayout.closeDrawers();

                return false;
            }
        });
        return mainDrawer;
    }

    public static void updateProfileName() {

        if (ParseUser.getCurrentUser() != null) {
            tvProfileName.setText(ParseUser.getCurrentUser().get("firstname").toString() + " " + ParseUser.getCurrentUser().get("lastname").toString());
        }
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
        mCurrentSelectedPosition = position;
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null) {
            mCallbacks.onNavigationDrawerItemSelected(position);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.main, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.share) {
            //Toast.makeText(getActivity(), "Share Sickday", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == tvlogOff.getId()) {

            final Dialog dialogLogOff;
            dialogLogOff = new Dialog(getActivity());
            dialogLogOff.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogLogOff.setContentView(R.layout.log_off_popup);
            dialogLogOff.setCancelable(false);

            Button confirm = (Button) dialogLogOff.findViewById(R.id.bConfirmLogOff);
            Button cancel = (Button) dialogLogOff.findViewById(R.id.bCanelLogOff);
            TextView areYouSureLogOff = (TextView) dialogLogOff.findViewById(R.id.tvAreYouSureLogOff);

            confirm.setTypeface(App.caecilia);
            cancel.setTypeface(App.caecilia);
            areYouSureLogOff.setTypeface(App.caecilia);

            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseUser.logOut();
                    Intent loginActivityIntent = new Intent(getActivity(), Login_Activity.class);
                    startActivity(loginActivityIntent);
                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogLogOff.dismiss();
                }
            });


            dialogLogOff.show();


        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(int position);
    }

    public class NavigationDrawerAdapter extends ArrayAdapter<String> {

        Context context;
        private String[] listTitles;
        private int[] listIcons;

        public NavigationDrawerAdapter(Context context, String[] listTitles) {
            super(context, R.layout.navigationdrawer_row, listTitles);
            this.context = context;
            this.listTitles = listTitles;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            listIcons = new int[]{
                    R.drawable.home_icon,
                    R.drawable.insurance_info_icon};
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.navigationdrawer_row, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.rowItemTitle);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.rowItemIcon);

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(lp);

            int width = dpToPx(60);
            int height = 0;
            if (position == 0) {
                height = dpToPx(30);
                listViewHeight += lp.height; //adds the padding
                lp.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            } else if (position == 1) {
                height = dpToPx(43);
                listViewHeight += 20; //adds the padding
                lp.setMargins(dpToPx(10), dpToPx(10), dpToPx(10), dpToPx(10));
            } else if (position == 2) {
                height = dpToPx(40);
                width = dpToPx(40);
                listViewHeight += lp.height; //adds the padding
                lp.setMargins(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(10));
            }
            imageView.getLayoutParams().height = height;
            imageView.getLayoutParams().width = width;

            //listViewHeight += height;
            //mDrawerListView.getLayoutParams().height = listViewHeight;

            textView.setText(listTitles[position]);
            // change the icon for Windows and iPhone
            String s = listTitles[position];
            imageView.setBackgroundResource(listIcons[position]);
            textView.setTypeface(App.caecilia);

            return rowView;
        }

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }
    }
}
