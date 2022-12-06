package com.mycareerplug.iamokah.mycareerplug;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.Window;

public class FragmentDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_detail);
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
     //   JobDetailFragment detailFragment = JobDetailFragment.newInstance("string1", "string2");

        // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, new JobDetailFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }


}
