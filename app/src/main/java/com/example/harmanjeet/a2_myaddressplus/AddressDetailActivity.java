package com.example.harmanjeet.a2_myaddressplus;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddressDetailActivity extends AppCompatActivity {
    private Spinner mIntials;
    private EditText mFName;
    private EditText mLName;
    private EditText mAddr;
    private Spinner mProvince;
    private EditText mCountry;
    private EditText mPostalCode;
    public String missingEdit;

    private Uri myAddrUri;
    @Override
    protected void onCreate( Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.address_edit);

        mIntials = (Spinner) findViewById(R.id.intialId);
        mFName = (EditText) findViewById(R.id.fnameId);
        mLName = (EditText) findViewById(R.id.lnameId);
        mAddr = (EditText) findViewById(R.id.editaddrId);
        mProvince = (Spinner) findViewById(R.id.provId);
        mCountry = (EditText) findViewById(R.id.editCountryId);
        mPostalCode = (EditText) findViewById(R.id.edit_pcode);

        Bundle extras = getIntent().getExtras();

        myAddrUri = (bundle == null) ? null : (Uri) bundle.getParcelable
                (MyContentProvider.CONTENT_ITEM_TYPE);

        if (extras != null){
            myAddrUri = extras.getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);
            fillData(myAddrUri);
        }
    }

    private void fillData(Uri uri) {
        String[] projection = {AddressTableHandler.COLUMN_INTIALS,AddressTableHandler.COLUMN_FNAME,
        AddressTableHandler.COLUMN_LNAME, AddressTableHandler.COLUMN_ADDRESS,
        AddressTableHandler.COLUMN_PROVINCE,AddressTableHandler.COLUMN_COUNTRY,
        AddressTableHandler.COLUMN_PCODE};
        Cursor cursor =getContentResolver().query(uri, projection, null, null,null);
        if (cursor != null){
            cursor.moveToFirst();
        String initials = cursor.getString(cursor.getColumnIndexOrThrow
                    (AddressTableHandler.COLUMN_INTIALS));

            for (int i = 0; i < mIntials.getCount(); i++) {

                String s = (String) mIntials.getItemAtPosition(i);
                if (s.equalsIgnoreCase(initials)) {
                    mIntials.setSelection(i);
                }
            }

        String province = cursor.getString(cursor.getColumnIndexOrThrow
                    (AddressTableHandler.COLUMN_PROVINCE));

            for (int i = 0; i < mProvince.getCount(); i++) {

                String s = (String) mProvince.getItemAtPosition(i);
                if (s.equalsIgnoreCase(province)) {
                    mProvince.setSelection(i);
                }
            }


        mFName.setText(cursor.getString(cursor.getColumnIndexOrThrow
                (AddressTableHandler.COLUMN_FNAME)));
        mLName.setText(cursor.getString(cursor.getColumnIndexOrThrow
                    (AddressTableHandler.COLUMN_LNAME)));
        mAddr.setText(cursor.getString(cursor.getColumnIndexOrThrow
                    (AddressTableHandler.COLUMN_ADDRESS)));
        mCountry.setText(cursor.getString(cursor.getColumnIndexOrThrow
                    (AddressTableHandler.COLUMN_COUNTRY)));
        mPostalCode.setText(cursor.getString(cursor.getColumnIndexOrThrow
                (AddressTableHandler.COLUMN_PCODE)));

        cursor.close();
        }
    }

    public void cnfirmBtn(View view) {
        if (checkEmpty()) {
            makeToast(missingEdit);
        }else {
            setResult(RESULT_OK);
            finish();
        }

    }

    private Boolean checkEmpty(){
        if (TextUtils.isEmpty(mFName.getText().toString())){
            missingEdit= "First Name";
            return true;
        } else if (TextUtils.isEmpty(mLName.getText().toString())){
            missingEdit= "Last Name";
            return true;
        } else if (TextUtils.isEmpty(mAddr.getText().toString())){
            missingEdit= "Address";
            return true;
        } else if (TextUtils.isEmpty(mProvince.getSelectedItem().toString())){
            missingEdit= "Province";
            return true;
        } else if (TextUtils.isEmpty(mCountry.getText().toString())){
            missingEdit= "Country";
            return true;
        } else if (TextUtils.isEmpty(mPostalCode.getText().toString())){
            missingEdit= "Postal Code";
            return true;
        } else if (TextUtils.isEmpty(mIntials.getSelectedItem().toString())){
            missingEdit= "Intials";
            return true;
        } else {
            return false;
        }
    }

    private void makeToast(String emptyField) {
        Toast.makeText(this, "Please insert "+ emptyField + " !!",Toast.LENGTH_LONG).show();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, myAddrUri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String intials = (String) mIntials.getSelectedItem();
        String firstName = mFName.getText().toString();
        String lastName = mLName.getText().toString();
        String address = mAddr.getText().toString();
        String prov = (String) mProvince.getSelectedItem();
        String country = mCountry.getText().toString();
        String pcode = mPostalCode.getText().toString();
        // Only save if either summary or description
        // is available

        if (intials.length() == 0 && firstName.length() == 0 && lastName.length() ==0
                && address.length() ==0 && prov.length() ==0 && country.length() ==0
                && pcode.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(AddressTableHandler.COLUMN_INTIALS, intials);
        values.put(AddressTableHandler.COLUMN_FNAME, firstName);
        values.put(AddressTableHandler.COLUMN_LNAME, lastName);
        values.put(AddressTableHandler.COLUMN_ADDRESS, address);
        values.put(AddressTableHandler.COLUMN_PROVINCE, prov);
        values.put(AddressTableHandler.COLUMN_COUNTRY, country);
        values.put(AddressTableHandler.COLUMN_PCODE, pcode);


        if (myAddrUri == null) {
            myAddrUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
        } else {
            getContentResolver().update(myAddrUri, values, null, null);
        }
    }

}
