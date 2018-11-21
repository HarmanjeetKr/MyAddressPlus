package com.example.harmanjeet.a2_myaddressplus;

import android.app.ListActivity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ListActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor>{
        //implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "A2_MyAddress";

    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    // private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG,"Harmanjeet Kaur 147941181");

        setContentView(R.layout.address_list);
        this.getListView().setDividerHeight(2);
        fillData();
        registerForContextMenu(getListView());

        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setLogo(R.drawable.logo);
        getActionBar().setDisplayUseLogoEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.insertId:
                createAddr();
                   return true;
            case R.id.aboutId:
                Intent i = new Intent(this, About.class);
                startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                fillData();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void createAddr() {
        Intent i = new Intent(this, AddressDetailActivity.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    // Opens the second activity if an entry is clicked
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, AddressDetailActivity.class);
        Uri todoUri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        i.putExtra(MyContentProvider.CONTENT_ITEM_TYPE, todoUri);

        // Activity returns an result if called with startActivityForResult
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    // Called with the result of the other activity
    // requestCode was the origin request code send to the activity
    // resultCode is the return code, 0 is everything is ok
    // intend can be used to get data
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void fillData() {
        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[] { AddressTableHandler.COLUMN_FNAME };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.label };

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.address_row, null, from, to, 0);

        setListAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }

    @Override
    public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = { AddressTableHandler.COLUMN_ID,
                AddressTableHandler.COLUMN_FNAME };
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

//



}
