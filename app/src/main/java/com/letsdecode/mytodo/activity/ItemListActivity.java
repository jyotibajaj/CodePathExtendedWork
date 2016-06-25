package com.letsdecode.mytodo.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.letsdecode.mytodo.R;
import com.letsdecode.mytodo.fragment.AddItemFragment;
import com.letsdecode.mytodo.fragment.ToDoListFragment;


public class ItemListActivity extends AppCompatActivity implements ToDoListFragment.OnFragmentInteractionListener, AddItemFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list_activity);
        //Set Action bar icon and show that
        getSupportActionBar().setIcon(R.drawable.check);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //setting default fragment in a ItemListActivity
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final ToDoListFragment toDoFragment = new ToDoListFragment();
        if (savedInstanceState == null) {
            fragmentTransaction.add(R.id.fragment_container, toDoFragment);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
