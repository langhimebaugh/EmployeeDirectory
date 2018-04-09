package com.himebaugh.employeedirectory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;

//  GOAL: Build a native android Mobile Employee Directory

//  ** The result is similar to the sample with Flex and Flash Builder
//  see http://www.adobe.com/devnet/flex/articles/employee-directory-android-flex.html

//  PURPOSE: Learning how to build an Android App.

//	Step 1: Create a blank App in eclipse. Modify it to display a ListActivity with some data.
//			1) activity_main.xml  (in res/layout)
//  Step 2: Load data into the ListActivity from an XML file via XmlParser
//          1) employee_list.xml  (in res/xml)
//          2) Employee.java
//          3) EmployeeXmlParser.java
//  Step 3: Save (Persist) the data into a SQLite Database & Load a ListView from a SQLite Database
//          1) Modify LoadEmployeesTask to load the database.
//          2) The database is created when called for the first time. This will also call the EmployeeXmlParser from within.
//          3) A Cursor is returned that exposes results from a query on a SQLiteDatabase.
//          4) The SimpleCursorAdapter displays the data from the Cursor.

public class MainActivity extends AppCompatActivity {

    public List<Employee> employees = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Parse xml data in a non-ui thread
        new LoadEmployeesTask().execute();
    }

    private class LoadEmployeesTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... args) {

            // query the database and return a cursor of employees.
            EmployeeDatabase employeeDatabase = new EmployeeDatabase(getApplicationContext());

            Cursor cursor = employeeDatabase.getAllEmployeesCursor();

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {

            String[] dataColumns = { EmployeeDatabase.COLUMN_FIRSTNAME, EmployeeDatabase.COLUMN_TITLE, EmployeeDatabase.COLUMN_DEPARTMENT };
            int[] viewIDs = { R.id.list_item_name, R.id.list_item_title, R.id.list_item_department };

            SimpleCursorAdapter records = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item, cursor, dataColumns, viewIDs, 0);

            ListView listView = (ListView) findViewById(R.id.list);
            if (listView != null) {
                listView.setAdapter(records);
            }

        }

    }

}