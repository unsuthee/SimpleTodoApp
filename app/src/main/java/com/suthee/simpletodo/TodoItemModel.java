package com.suthee.simpletodo;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Suthee on 4/18/2015.
 */
@Table(name = "TodoItem")
public class TodoItemModel extends Model {
    @Column(name = "Name")
    public String name;

    @Column(name = "Date_Created")
    public Date dateCreated;

    public TodoItemModel() {
        super();
    }

    public TodoItemModel(String name, Date dateCreated) {
        super();
        this.name = name;
        this.dateCreated = dateCreated;
    }

    public static List<TodoItemModel> getAll() {
        return new Select()
                .from(TodoItemModel.class)
                .orderBy("Date_Created ASC")
                .execute();
    }

    public static ArrayList<TodoItemModel> getTodoItems() {
        ArrayList<TodoItemModel> items = new ArrayList<>();

        SimpleDateFormat dateFmt = new SimpleDateFormat("MM-dd-yyyy");
        try {
            items.add(new TodoItemModel("Buy Milk", dateFmt.parse("4-10-2015")));
            items.add(new TodoItemModel("Buy Car", dateFmt.parse("4-11-2015")));
            items.add(new TodoItemModel("Call Mom", dateFmt.parse("4-12-2015")));
            items.add(new TodoItemModel("Workout", dateFmt.parse("4-13-2015")));
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return items;
    }
}
