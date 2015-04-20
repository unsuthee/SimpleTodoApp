package com.suthee.simpletodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Suthee on 4/19/2015.
 */
public class CustomTodoItemAdapter extends ArrayAdapter<TodoItemModel> {

    private SimpleDateFormat dateFmt = new SimpleDateFormat("MM-dd-yyyy");

    public CustomTodoItemAdapter(Context context, ArrayList<TodoItemModel> items) {
        super(context, R.layout.item_todo, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TodoItemModel item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_todo, parent, false);
        }

        TextView tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
        TextView tvDateCreated = (TextView) convertView.findViewById(R.id.tvDateCreated);

        tvItemName.setText(item.name);
        tvDateCreated.setText(dateFmt.format(item.dateCreated));

        return convertView;
    }
}
