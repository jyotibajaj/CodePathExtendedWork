package com.letsdecode.mytodo.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.letsdecode.mytodo.R;
import com.letsdecode.mytodo.models.ListViewItem;
import com.letsdecode.mytodo.models.TaskDetail;
import com.letsdecode.mytodo.models.TypeClass;

import java.util.ArrayList;


public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
    private ItemClick itemClick;
    private ArrayList<ListViewItem> listItems;
    private Context context;


    public interface ItemClick {
        void onItemClicked(TaskDetail taskDetail);

        void onItemDone(TaskDetail taskDetail);

        void onItemNotDone(TaskDetail taskDetail);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView, itemPriority;
        public CardView cardView;
        public TextView time_view;
        public ImageView notDoneBox, doneBox;


        public ViewHolder(View itemView) {
            super(itemView);
            //getting reference of views
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            time_view = (TextView) itemView.findViewById(R.id.time_view);
            itemNameTextView = (TextView) itemView.findViewById(R.id.textView_itemName);
            itemPriority = (TextView) itemView.findViewById(R.id.textView_itemPriority);
            notDoneBox = (ImageView) itemView.findViewById(R.id.checkbox_not_done);
            doneBox = (ImageView) itemView.findViewById(R.id.checkbox_done);
        }
    }

    public ListViewAdapter(ItemClick itemClick, ArrayList<ListViewItem> listItems, Context context) {
        this.context = context;
        this.itemClick = itemClick;
        this.listItems = listItems;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_layout, parent, false);
        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(itemView);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewHolder.getAdapterPosition();
                ListViewItem listViewItem = listItems.get(pos);
                if (listViewItem.getType() == TypeClass.TIME_VIEW) {
                    return;
                }
                TaskDetail taskDetail = (TaskDetail) listViewItem.getObject();
                if (taskDetail != null) {
                    itemClick.onItemClicked(taskDetail);
                }
            }
        });
        viewHolder.doneBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        viewHolder.notDoneBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.doneBox.setVisibility(View.VISIBLE);
                viewHolder.notDoneBox.setVisibility(View.INVISIBLE);
                int pos = viewHolder.getAdapterPosition();
                ListViewItem listViewItem = listItems.get(pos);
                TaskDetail taskDetail = (TaskDetail) listViewItem.getObject();
                if (taskDetail != null) {
                    viewHolder.itemNameTextView.setPaintFlags(viewHolder.itemNameTextView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.itemNameTextView.setAlpha(0.40f);
                    itemClick.onItemDone(taskDetail);
                }

            }
        });

        viewHolder.doneBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.doneBox.setVisibility(View.INVISIBLE);
                viewHolder.notDoneBox.setVisibility(View.VISIBLE);
                int pos = viewHolder.getAdapterPosition();
                ListViewItem listViewItem = listItems.get(pos);
                TaskDetail taskDetail = (TaskDetail) listViewItem.getObject();
                if (taskDetail != null) {
                    viewHolder.itemNameTextView.setAlpha(1.0f);
                    viewHolder.itemNameTextView.setPaintFlags(viewHolder.itemNameTextView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    itemClick.onItemNotDone(taskDetail);
                }

            }
        });

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Resources resources = context.getResources();

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ListViewItem listItem = listItems
                .get(position);
        switch (listItem.getType()) {
            case TypeClass.TIME_VIEW:
                String time = (String) listItem.getObject();
                holder.time_view.setVisibility(View.VISIBLE);
                //setting color corresponding to time text
                if (time.equalsIgnoreCase("overdue")) {
                    holder.time_view.setTextColor(Color.RED);

                } else if (time.equalsIgnoreCase("today")) {
                    holder.time_view.setTextColor(Color.BLUE);

                } else {
                    int colorCode = getColor(time);
                    holder.time_view.setTextColor(colorCode);
                }
                holder.time_view.setText(time);
                holder.cardView.setVisibility(View.GONE);
                break;
            case TypeClass.ITEM_DETAIL_VIEW:
                holder.time_view.setVisibility(View.GONE);
                holder.cardView.setVisibility(View.VISIBLE);
                TaskDetail itemObject = (TaskDetail) listItem.getObject();
                holder.itemNameTextView.setText(itemObject.getItemName());
                holder.itemPriority.setText(itemObject.getPriority());
                String priorityString = itemObject.getPriority();
                if (priorityString.equalsIgnoreCase("Low")) {
                    int lowColor = resources.getColor(R.color.colorLow);
                    holder.itemPriority.setTextColor(lowColor);
                } else if (priorityString.equalsIgnoreCase("Medium")) {
                    int medColor = resources.getColor(R.color.colorMedium);
                    holder.itemPriority.setTextColor(medColor);
                } else if (priorityString.equalsIgnoreCase("High")) {
                    int highColor = resources.getColor(R.color.colorHigh);
                    holder.itemPriority.setTextColor(highColor);
                } else {
                    int urgentColor = resources.getColor(R.color.colorUrgent);
                    holder.itemPriority.setTextColor(urgentColor);
                }
                String status = itemObject.getStatus();
                if ("todo".equalsIgnoreCase(status)) {
                    holder.notDoneBox.setVisibility(View.VISIBLE);
                    holder.doneBox.setVisibility(View.INVISIBLE);
                    holder.itemNameTextView.setAlpha(1.0f);
                } else if ("done".equalsIgnoreCase(status)) {
                    holder.itemNameTextView.setPaintFlags(holder.itemNameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.itemNameTextView.setAlpha(0.40f);
                    holder.notDoneBox.setVisibility(View.INVISIBLE);
                    holder.doneBox.setVisibility(View.VISIBLE);
                }

                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listItems.size();
    }

    private static int a[] = {0xFFEF5682, 0xFFafaded, 0xFF22375a, 0xFF73c82c, 0xFF96a8cd};

    private int getColor(String time) {
        int code = Math.abs(time.hashCode());
        int color = a[code % a.length];
        return color;
    }


}