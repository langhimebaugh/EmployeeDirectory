package com.himebaugh.employeedirectory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

//  GOAL: Build a native android Mobile Employee Directory

//  ** The result is similar to the sample with Flex and Flash Builder
//  see http://www.adobe.com/devnet/flex/articles/employee-directory-android-flex.html

//  PURPOSE: Learning how to build an Android App.

//	Step 1: Create a blank App in Android Studio. Modify it to display a ListActivity with some data.
//			1) activity_main.xml  (in res/layout)
//  Step 2: Load data into the ListActivity from an XML file via XmlParser
//          1) employee_list.xml  (in res/xml)
//          2) Employee.java
//          3) EmployeeXmlParser.java

public class MainActivity extends AppCompatActivity {

    public List<Employee> employees = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * This will work, but best practice is to place in a non-ui thread like below.
         *
         * EmployeeXmlParser parser = new EmployeeXmlParser();
         * employees = parser.parse(this);
         *
         * ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this, android.R.layout.simple_list_item_1, employees);
         * ListView listView =  (ListView) findViewById(R.id.list);
         * listView.setAdapter(adapter);
         */

        // Parse xml data in a non-ui thread
        new LoadEmployeesTask().execute();

    }

    private class LoadEmployeesTask extends AsyncTask<String, Void, List<Employee>> {

        @Override
        protected List<Employee> doInBackground(String... args) {

            // CALL XMLPULLPARSER & RETURN A LIST
            EmployeeXmlParser parser = new EmployeeXmlParser();
            employees = parser.parse(getBaseContext());

            return employees;
        }

        @Override
        protected void onPostExecute(List<Employee> employees) {

            ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(getBaseContext(), android.R.layout.simple_list_item_1, employees);

            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);

        }

    }

}
