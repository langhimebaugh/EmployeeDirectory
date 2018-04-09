package com.himebaugh.employeedirectory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//  GOAL: Build a native android Mobile Employee Directory

//  ** The result is similar to the sample with Flex and Flash Builder
//  see http://www.adobe.com/devnet/flex/articles/employee-directory-android-flex.html

//  PURPOSE: Learning how to build an Android App.

//  Step 1: Create a blank App in eclipse. Modify it to display a ListActivity with some data.
//          1) activity_main.xml  (in res/layout)

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] values = new String[] { "Employee 1", "Employee 2", "Employee 3", "Employee 4", "Employee 5", "Employee 6", "Employee 7", "Employee 8", "Employee 9", "Employee 10" };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
    }
}
