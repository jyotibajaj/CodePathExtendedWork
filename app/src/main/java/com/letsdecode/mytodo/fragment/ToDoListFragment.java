package com.letsdecode.mytodo.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.letsdecode.mytodo.R;
import com.letsdecode.mytodo.adapters.ListViewAdapter;
import com.letsdecode.mytodo.adapters.SQLiteDataAdapter;
import com.letsdecode.mytodo.models.ListViewItem;
import com.letsdecode.mytodo.models.TaskDetail;
import com.letsdecode.mytodo.utils.ItemsBucketing;

import java.util.ArrayList;

public class ToDoListFragment extends Fragment implements ListViewAdapter.ItemClick {
    private static final String TAG = ToDoListFragment.class.getSimpleName();
    public static final String ITEM = "SELECTED_ITEM_VALUE";
    private OnFragmentInteractionListener mListener;
    private int item_id = 0;

    private ImageButton addItemImageButton;

    //recycler view
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    ArrayList<ListViewItem> listItems = new ArrayList<>();


    public ToDoListFragment() {
        // Required empty public constructor
    }

    public static ToDoListFragment newInstance(String param1, String param2) {
        ToDoListFragment fragment = new ToDoListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(" My ToDo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.todo_list_fragment, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listItems.clear();
        //button reference
        addItemImageButton = (ImageButton) view.findViewById(R.id.imageButton_add);

        addItemImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.fragment_container, AddItemFragment.newInstance(0, false)).addToBackStack(null).commit();

            }
        });
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        SQLiteDataAdapter sqLiteDataAdapterNotPurchased = new SQLiteDataAdapter(getActivity().getApplicationContext());
        // data model
        ArrayList<TaskDetail> itemList = sqLiteDataAdapterNotPurchased.getToDoItemData();
        ItemsBucketing itemsBucketing = new ItemsBucketing();
        listItems = itemsBucketing.createBuckets(itemList);
        mAdapter = new ListViewAdapter(this, listItems, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(TaskDetail itemType) {
        // check if type is time or item
        item_id = itemType.getId();
        FragmentTransaction fragmentTransaction3 = getFragmentManager().beginTransaction();
        fragmentTransaction3.replace(R.id.fragment_container, AddItemFragment.newInstance(item_id, true))
                .addToBackStack(null).commit();
    }

    @Override
    public void onItemDone(TaskDetail taskDetail) {
        item_id = taskDetail.getId();
        SQLiteDataAdapter.setItemDone("" + item_id);
    }

    @Override
    public void onItemNotDone(TaskDetail taskDetail) {
        item_id = taskDetail.getId();
        SQLiteDataAdapter.setItemNotDone("" + item_id);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

