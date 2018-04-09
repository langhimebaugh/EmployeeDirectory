package com.himebaugh.employeedirectory;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

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
//  Step 4: Pass data to DetailActivity to display more data and provide other functionality (w/ intent.putExtra)
//          1) Create DetailActivity
//          2) Create activity_detail.xml  (in res/layout)
//          3) Add DetailActivity to AndroidManifest.xml
//          4) Add uses-permissions to AndroidManifest.xml
//          5) Modify strings.xml  (in res/values)
//          6) Create mail.png, phone.png, sms.png  (in res/drawable)
//          7) Create employee_photo.jpg  (in assets/pics)

public class MainActivity extends AppCompatActivity {

    public List<Employee> employees = null;

    //  private ListAdapter listAdapter;
    private ListView listView;
    private AppCompatActivity appCompatActivity;

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

            String[] dataColumns = {
                    EmployeeDatabase.COLUMN_ID,
                    EmployeeDatabase.COLUMN_FIRSTNAME,
                    EmployeeDatabase.COLUMN_TITLE,
                    EmployeeDatabase.COLUMN_DEPARTMENT,
                    EmployeeDatabase.COLUMN_CITY,
                    EmployeeDatabase.COLUMN_OFFICE_PHONE,
                    EmployeeDatabase.COLUMN_MOBILE_PHONE,
                    EmployeeDatabase.COLUMN_EMAIL,
                    EmployeeDatabase.COLUMN_PICTURE
            };
            int[] viewIDs = {
                    R.id.list_item_emp_id,
                    R.id.list_item_name,
                    R.id.list_item_title,
                    R.id.list_item_department,
                    R.id.list_item_city,
                    R.id.list_item_office_phone,
                    R.id.list_item_mobile_phone,
                    R.id.list_item_email,
                    R.id.list_item_picture
            };

            SimpleCursorAdapter records = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item, cursor, dataColumns, viewIDs, 0);

            listView = (ListView) findViewById(R.id.list);
            if (listView != null) {
                listView.setAdapter(records);
            }

            listView.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    // get values from selected ListItem
                    String empID = ((TextView) view.findViewById(R.id.list_item_emp_id)).getText().toString();
                    String name = ((TextView) view.findViewById(R.id.list_item_name)).getText().toString();
                    String title = ((TextView) view.findViewById(R.id.list_item_title)).getText().toString();
                    String department = ((TextView) view.findViewById(R.id.list_item_department)).getText().toString();
                    String city = ((TextView) view.findViewById(R.id.list_item_city)).getText().toString();
                    String officePhone = ((TextView) view.findViewById(R.id.list_item_office_phone)).getText().toString();
                    String mobilePhone = ((TextView) view.findViewById(R.id.list_item_mobile_phone)).getText().toString();
                    String email = ((TextView) view.findViewById(R.id.list_item_email)).getText().toString();
                    String picture = ((TextView) view.findViewById(R.id.list_item_picture)).getText().toString();

                    // Start new intent
                    // getApplicationContext() ?
                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                    intent.putExtra("empID", empID);
                    intent.putExtra("name", name);
                    intent.putExtra("title", title);
                    intent.putExtra("department", department);
                    intent.putExtra("city", city);
                    intent.putExtra("officePhone", officePhone);
                    intent.putExtra("mobilePhone", mobilePhone);
                    intent.putExtra("email", email);
                    intent.putExtra("picture", picture);
                    startActivity(intent);

                }
            });


        }


    }

//    public class MainActivity extends AppCompatActivity  implements OnItemClickListener {

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//        switch (parent.getId()) {
//            case R.id.list:
//                Toast.makeText(this, position + " is clicked", Toast.LENGTH_SHORT).show();
//                break;
//        }
//    }

}