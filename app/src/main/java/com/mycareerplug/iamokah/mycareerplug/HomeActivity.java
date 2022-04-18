package com.mycareerplug.iamokah.mycareerplug;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    ActionBar ab;

    private Fragment fragment;
    FragmentTransaction ft;

    String get_bottom_id;

    String[] filterArray;
    boolean[] checkedItems;
    List<String> mUserItems;
    ArrayList<Integer> userItemsInt;
    boolean isFilterApplied = false;

    TinyDB tinyDB;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar tb = findViewById(R.id.my_toolbar);
        setSupportActionBar(tb);

        ab = getSupportActionBar();
       // TinyDB tinyDB = new TinyDB();

        tinyDB = new TinyDB(HomeActivity.this);
        //interested_modelarray_pos = tinyDB.getListInt("interested_arraypos_key");


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigationView);

        //BOTTOMNAV SETUP
        bottomNavigationView.setSelectedItemId(R.id.discover);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_placeholder, new DiscoverFragment()).commit();

        get_bottom_id = bottomNavigationView.getMenu().
                findItem(bottomNavigationView.getSelectedItemId()).toString();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(HomeActivity.this, "Got Error", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                System.out.println("Printing menu Item " + menuItem.toString());
                boolean applyFragment = false;
                // TODO: 3/24/2019 When you hit the same menuitem you are currently on it shouldn't do anyhting.
                if (!menuItem.toString().equals(get_bottom_id)) {

                    switch (menuItem.getItemId()) {
                        case R.id.discover:
                            System.out.println("touched discover");
                            get_bottom_id = menuItem.toString();
                            fragment = new DiscoverFragment();
                            //resetList(modelList);
                            //  eventlist_array(get_nav_id.toLowerCase());
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, fragment).commit();
                            //   ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            break;
                        case R.id.interests:
                            System.out.println("touched interests");
                            get_bottom_id = menuItem.toString();
                            ab.setTitle("Interests");
                            fragment = new InterestsFragment();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, fragment).commit();
                            //  eventlist_array(get_nav_id.toLowerCase());
                            break;
                        case R.id.profile:
                            System.out.println("touched profile");
                            get_bottom_id = menuItem.toString();
                            //sendToCreateProfile();
                            fragment = new ProfileFragment();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, fragment).commit();
                            //  eventlist_array(get_nav_id.toLowerCase());
                            break;
                        case R.id.filter_event:
                           showFilterDialog();
                           break;
                    }


                }
                return true;
            }
        });
    }


    public void sendToCreateProfile() {
        Intent intent = new Intent(HomeActivity.this, CreateUserProfile.class);
        intent.putExtra("HomeProfile", "HomeProfile");
        startActivity(intent);
    }

    public void setBottomNavSelected(int itemId) {
        bottomNavigationView.setSelectedItemId(itemId);
    }

    private void showFilterDialog() {
        filterArray = getResources().getStringArray(R.array.filter_list);
        final ArrayList<String> checkedItemsStringVal = new ArrayList<>();
        ArrayList<Boolean> test = tinyDB.getListBoolean("checkedItemList");
        checkedItems = new boolean[filterArray.length];
        userItemsInt = new ArrayList<>();
        if (test == null || test.isEmpty()) {

            System.out.println("TEST IS EMPTY");
            for (int i = 0; i < checkedItems.length; i++) {
                checkedItems[i] = true;
                userItemsInt.add(i);
            }
        } else {
            //convert list to array.
            for (int i = 0; i < test.size(); i++) {
                checkedItems[i] = test.get(i);
                if (checkedItems[i]) {
                    userItemsInt.add(i);
                }
            }
        }
        // TODO: 3/30/2019 Get boolean checklist.
      /*  checkedItems = new boolean[filterArray.length];
        userItemsInt = new ArrayList<>();
        for (int i = 0; i < checkedItems.length; i++) {
            checkedItems[i] = true;
            userItemsInt.add(i);
        } */
        mUserItems = Arrays.asList(filterArray);

        final int previousBottomNavSelected = bottomNavigationView.getSelectedItemId();

       // System.out.println("Printing checkedItems " + checkedItems.toString());

        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("Choose Event Filters");

        builder.setMultiChoiceItems(filterArray, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                if (isChecked) {
                    if (!userItemsInt.contains(position)) {
                        userItemsInt.add(position);

                        // Notify the current action
                     //   Toast.makeText(getApplicationContext(),
                     //           mUserItems.get(position).toString() + " " + isChecked, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (userItemsInt.contains(position)) {
                        userItemsInt.remove(Integer.valueOf(position));

                    }
                }
               // checkedItems[position] = isChecked;
            }

             /*   checkedItems[position] = isChecked;

                String currentItem = mUserItems.get(position);



                // Notify the current action
                Toast.makeText(getApplicationContext(),
                        currentItem + " " + isChecked, Toast.LENGTH_SHORT).show(); */


        });

        builder.setCancelable(false);
        builder.setPositiveButton("Apply", null);
      /*  builder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: 3/30/2019 MAKE SURE ATLEAST 1 value is selected.
                System.out.println("WHICH VALUEF " + which);
                String items = "";
                for (int i = 0; i < userItemsInt.size(); i++) {
                    boolean checked = checkedItems[i];
                    items = items + filterArray[userItemsInt.get(i)];
                    if (i != userItemsInt.size() - 1) {
                        items = items  + ", ";
                    }
                //    items += items + ", " + filterArray[mUserItems.get(i)];
                }

                System.out.println("Printing ok Items " + items);
                ArrayList<Boolean> aListCheckedItems = new ArrayList<>();
                int numFalse = 0;
                for (int i = 0; i < checkedItems.length; i++) {
                    boolean checked = checkedItems[i];
                    if (!checked) {
                        numFalse++;
                    }
                    aListCheckedItems.add(checkedItems[i]);
                }

                if (numFalse == checkedItems.length) {

                }
                tinyDB.putListBoolean("checkedItemList", aListCheckedItems);

                System.out.println("SIZE " + userItemsInt.size());

                setBottomNavSelected(R.id.discover);
                Fragment mfragment = new DiscoverFragment();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder, mfragment).commit();

              //  isFilterApplied = true;
            }
        }); */

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                setBottomNavSelected(previousBottomNavSelected);
                dialog.dismiss();
            }
        });


        //  builder.setNeutralButton("Clear all", null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        // TODO: 3/30/2019 MAKE SURE ATLEAST 1 value is selected.
                     //   System.out.println("WHICH VALUEF " + which);
                        String items = "";

                        System.out.println("printing useritem SIZE " + userItemsInt.size());
                        for (int i = 0; i < userItemsInt.size(); i++) {
                            boolean checked = checkedItems[i];
                            items = items + filterArray[userItemsInt.get(i)];
                            if (i != userItemsInt.size() - 1) {
                                items = items  + ", ";
                            }
                            checkedItemsStringVal.add(filterArray[userItemsInt.get(i)]);
                            //    items += items + ", " + filterArray[mUserItems.get(i)];
                        }

                        System.out.println("Printing ok Items " + items);
                        ArrayList<Boolean> aListCheckedItems = new ArrayList<>();
                        int numFalse = 0;
                        for (int i = 0; i < checkedItems.length; i++) {
                            boolean checked = checkedItems[i];
                            if (!checked) {
                                numFalse++;
                            }
                            aListCheckedItems.add(checkedItems[i]);
                        }
                        //NO BOXES CHECKED.
                        if (numFalse == checkedItems.length) {
                            // Notify the current action
                            Toast.makeText(HomeActivity.this,
                                    "Please select at least one filter.", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 0; i < checkedItemsStringVal.size(); i++) {
                                System.out.println("Saving val " + checkedItemsStringVal.get(i));
                            }
                            tinyDB.putListString("filter_checklist", checkedItemsStringVal);
                            tinyDB.putListBoolean("checkedItemList", aListCheckedItems);
                            setBottomNavSelected(R.id.discover);
                            Fragment mfragment = new DiscoverFragment();
                            ft = getSupportFragmentManager().beginTransaction();
                            ft.replace(R.id.fragment_placeholder, mfragment).commit();
                            dialog.dismiss();
                        }






                        //  isFilterApplied = true;
                        //Dismiss once everything is OK.
                      //  dialog.dismiss();
                    }
                });


            }
        });

        dialog.show();

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                //  statusTextView.setText("You have Signed Out");
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sign_out_btn) {
            Intent intent = new Intent(HomeActivity.this, LoginUIActivity.class);
            startActivity(intent);
            finish();

            if (FirebaseAuth.getInstance() != null) {
                FirebaseAuth.getInstance().signOut();
                signOut();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
