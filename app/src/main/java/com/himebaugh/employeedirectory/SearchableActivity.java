package com.himebaugh.employeedirectory;

import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SearchableActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private static final String TAG = SearchableActivity.class.getSimpleName();

    private static final int LOADER_ID = 2;

    private ListView mListView;

    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_searchable);

        mListView = findViewById(R.id.search_list);

        mListView.setOnItemClickListener(this);

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

        // Create an empty adapter for the ListView.
        // The search results will be put in when they are fully loaded.
        mAdapter = new android.support.v4.widget.SimpleCursorAdapter(getBaseContext(), R.layout.list_item, null, dataColumns, viewIDs, 0);

        mListView.setAdapter(mAdapter);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        // update the activity launch intent
        setIntent(newIntent);
        // handle it
        handleIntent(newIntent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // The user has initiated a search.

            Log.i(TAG, "handleIntent: Intent.ACTION_SEARCH");

            String query = intent.getStringExtra(SearchManager.QUERY);
            doSearch(query);

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {

            // The user has selected a suggestion.
            // Handles clicking on the search results drop down...

            Log.i(TAG, "handleIntent: Intent.ACTION_VIEW");

            Uri details = intent.getData();
            Intent detailsIntent = new Intent(Intent.ACTION_VIEW, details);
            startActivity(detailsIntent);

            finish();

        }

    }

    private void doSearch(String query) {

        Bundle data = new Bundle();
        data.putString("query", query);

        // Initialize the search results loader.
        getSupportLoaderManager().initLoader(LOADER_ID, data, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle data) {

        String wildcardQuery = "%" + data.getString("query") + "%";

        Uri uri = EmployeeProvider.CONTENT_URI;
        String[] projection = {
                EmployeeDatabase.COLUMN_ID,
                EmployeeDatabase.COLUMN_FIRSTNAME,
                EmployeeDatabase.COLUMN_LASTNAME,
                EmployeeDatabase.COLUMN_TITLE,
                EmployeeDatabase.COLUMN_DEPARTMENT,
                EmployeeDatabase.COLUMN_CITY,
                EmployeeDatabase.COLUMN_OFFICE_PHONE,
                EmployeeDatabase.COLUMN_MOBILE_PHONE,
                EmployeeDatabase.COLUMN_EMAIL,
                EmployeeDatabase.COLUMN_PICTURE
        };
        String selection = EmployeeDatabase.COLUMN_FIRSTNAME + " LIKE ? OR " + EmployeeDatabase.COLUMN_LASTNAME + " LIKE ?";
        String[] selectionArgs = {wildcardQuery, wildcardQuery};
        String sortOrder = EmployeeDatabase.COLUMN_LASTNAME + " COLLATE LOCALIZED ASC";

        CursorLoader cursorLoader = new CursorLoader(this, uri, projection, selection, selectionArgs, sortOrder);

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        // Put the results in the adapter.
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                onSearchRequested();
                // break;
                return true;
            case android.R.id.home:
                // This is called when the Home (Up) button is pressed
                // in the Action Bar.
                Intent parentActivityIntent = new Intent(this, MainActivity.class);
                parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(parentActivityIntent);
                finish();
                // break;
                return true;
            default:
                return false;
        }

        // return super.onOptionsItemSelected(item);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Log.i(TAG, "onItemClick");

        Uri details = Uri.withAppendedPath(EmployeeProvider.CONTENT_URI, "" + id);
        Intent detailsIntent = new Intent(Intent.ACTION_VIEW, details);
        startActivity(detailsIntent);

        finish();

    }

//    /**
//     * This method is called after this activity has been paused or restarted.
//     */
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
//    }
}
