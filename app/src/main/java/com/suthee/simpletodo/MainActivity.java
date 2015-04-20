package com.suthee.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.activeandroid.query.Delete;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    ListView lvItems;
    ArrayList<TodoItemModel> items;
    CustomTodoItemAdapter todoItemAdapter;

    private final int REQUEST_EDIT_ITEM_TEXT_CODE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvItems = (ListView) findViewById(R.id.lvItems);

        // Load all items from the database
        items = (ArrayList<TodoItemModel>) TodoItemModel.getAll();

        todoItemAdapter = new CustomTodoItemAdapter(this, items);
        lvItems.setAdapter(todoItemAdapter);
        setupListViewListener();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        TodoItemModel removedItem = items.remove(position);
                        todoItemAdapter.notifyDataSetChanged();

                        // remove this item from the database
                        new Delete().from(TodoItemModel.class)
                                .where("Id = ?", removedItem.getId())
                                .execute();

                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                        TodoItemModel todoItem = todoItemAdapter.getItem(position);
                        intent.putExtra(Intent.EXTRA_TEXT, todoItem.name);
                        intent.putExtra(getString(R.string.Intent_List_Position), position);
                        startActivityForResult(intent, REQUEST_EDIT_ITEM_TEXT_CODE);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        if (! itemText.isEmpty()) {
            TodoItemModel addItem = new TodoItemModel(itemText, new Date());
            todoItemAdapter.add(addItem);
            etNewItem.setText("");

            addItem.save();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_EDIT_ITEM_TEXT_CODE && resultCode == RESULT_OK) {
            String itemText = data.getStringExtra(Intent.EXTRA_TEXT);
            int position = data.getIntExtra(getString(R.string.Intent_List_Position), -1);
            if (position >= 0 && position < todoItemAdapter.getCount()) {
                // We don't update if the itemText remains unchanged
                if (! items.get(position).name.equals(itemText)) {
                    TodoItemModel todoItem = todoItemAdapter.getItem(position);
                    todoItem.name = itemText;
                    todoItemAdapter.notifyDataSetChanged();

                    todoItem.save();
                }
            }
        }
    }
}
