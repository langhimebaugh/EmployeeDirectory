package com.himebaugh.employeedirectory;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
// import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

//  GOAL: Build a native android Mobile Employee Directory

//  ** The result is similar to the sample with Flex and Flash Builder
//  see http://www.adobe.com/devnet/flex/articles/employee-directory-android-flex.html

//  PURPOSE: Learning how to build an Android App.

//  Step 6: Create a Search Interface using Android's search dialog.
//          1) Modify MainActivity (add SearchView to onCreateOptionsMenu, add onSearchRequested() to onOptionsItemSelected )
//          2) Modify EmployeeProvider (Add Search logic within the ContentProvider)
//          3) Create SearchableActivity
//          4) Create searchable.xml  (in res/xml)
//          5) Modify main.xml  (in res/menu)
//          6) Modify strings.xml  (in res/values)
//          7) Modify AndroidManifest.xml
//  Step 6B: Pass data to DetailActivity using Intent.ACTION_VIEW in place of intent.putExtra
//          1) Modify MainActivity (add Intent.ACTION_VIEW in onListItemClick )
//          2) Modify DetailActivity
//          3) Modify AndroidManifest.xml (add android:mimeType="vnd.android.cursor.item...)

//	Step 5: Create a ContentProvider to access the database.
//			1) Create EmployeeProvider
//			2) Modify LoadEmployeesTask To implement the new ContentProvider (MainActivity)
//			3) Remove getAllEmployeesCursor() method from EmployeeDatabase

//	Step 4: Pass data to DetailActivity to display more data and provide other functionality (w/ intent.putExtra)
//			1) Create DetailActivity
//			2) Create activity_detail.xml  (in res/layout)
//			3) Add DetailActivity to AndroidManifest.xml
//			4) Add uses-permissions to AndroidManifest.xml
//			5) Modify strings.xml  (in res/values)
//			6) Create mail.png, phone.png, sms.png  (in res/drawable)
//			7) Create employee_photo.jpg  (in assets/pics)

//	Step 3: Save (Persist) the data into a SQLite Database & Load a ListView from a SQLite Database
//			1) Modify LoadEmployeesTask to load the database.
//			2) The database is created when called for the first time. This will also call the EmployeeXmlParser from within.
//			3) A Cursor is returned that exposes results from a query on a SQLiteDatabase.
//			4) The SimpleCursorAdapter displays the data from the Cursor.

//	Step 2: Load data into the ListActivity from an XML file via XmlParser
//			1) employee_list.xml  (in res/xml)
//			2) Employee.java
//			3) EmployeeXmlParser.java

//	Step 1: Create a blank App in eclipse. Modify it to display a ListActivity with some data.
//			1) activity_main.xml  (in res/layout)

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    
    public List<Employee> employees = null;

    //  private ListAdapter listAdapter;
    private ListView listView;
    private AppCompatActivity appCompatActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG, "onItemClick: ");

                Uri details = Uri.withAppendedPath(EmployeeProvider.CONTENT_URI, "" + id);
                Intent detailsIntent = new Intent(Intent.ACTION_VIEW, details);
                startActivity(detailsIntent);
            }
        });

        // Parse xml data in a non-ui thread
        new LoadEmployeesTask().execute();
    }

    private class LoadEmployeesTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... args) {

            // query the database and return a cursor of employees.
            // EmployeeDatabase employeeDatabase = new EmployeeDatabase(getApplicationContext());
            // Cursor cursor = employeeDatabase.getAllEmployeesCursor();

            // To implement the ContentProvider
            // Replace the 2 lines above with the lines below
            // .... and remember to modify AndroidManifest.xml
            Uri uri = EmployeeProvider.CONTENT_URI;
            String[] projection = { EmployeeDatabase.COLUMN_ID, EmployeeDatabase.COLUMN_FIRSTNAME, EmployeeDatabase.COLUMN_LASTNAME, EmployeeDatabase.COLUMN_TITLE, EmployeeDatabase.COLUMN_DEPARTMENT,
                    EmployeeDatabase.COLUMN_CITY, EmployeeDatabase.COLUMN_OFFICE_PHONE, EmployeeDatabase.COLUMN_MOBILE_PHONE, EmployeeDatabase.COLUMN_EMAIL, EmployeeDatabase.COLUMN_PICTURE };
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = EmployeeDatabase.COLUMN_LASTNAME + " COLLATE LOCALIZED ASC";

            Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

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

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getBaseContext(), R.layout.list_item, cursor, dataColumns, viewIDs, 0);

            listView.setAdapter(adapter);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG, "onQueryTextSubmit: ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i(TAG, "onQueryTextChange: ");
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                return true;
            default:
                return false;
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