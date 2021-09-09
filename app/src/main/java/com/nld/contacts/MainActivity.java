package com.nld.contacts;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mListContactsRcv;
    private TextView mTotalContacts;
    private ContactsAdapter mContactsAdapter;
    private List<Contacts> mListContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btForwardContacts = findViewById(R.id.forward_contacts);
        Button btGetContacts = findViewById(R.id.get_contacts);
        Button btRestoreContacts = findViewById(R.id.restore_contacts);
        mListContactsRcv = findViewById(R.id.list_contacts);
        mTotalContacts = findViewById(R.id.total_contacts);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mListContactsRcv.setLayoutManager(layoutManager);

        btForwardContacts.setOnClickListener(this);
        btGetContacts.setOnClickListener(this);
        btRestoreContacts.setOnClickListener(this);

        mListContacts = getAllContacts();
        mContactsAdapter = new ContactsAdapter(mListContacts);
    }

    private void notification() {

    }

    private List<Contacts> getAllContacts() {
        List<Contacts> listContacts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    Cursor phoneCursor = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id},
                            null);
                    if (phoneCursor != null) {
                        if (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if (phoneNumber.length() == 11) {
                                listContacts.add(new Contacts(id, contactName, phoneNumber));
                            }
                            //At here You can add phoneNUmber and Name to you listView ,ModelClass,Recyclerview
                            phoneCursor.close();
                        }
                    }
                }
            }
        }
        return listContacts;
    }

    private int updatePhoneNumber(String rawContactId, String newPhoneNumber) {
        // Create content values object.
        ContentValues contentValues = new ContentValues();

        // Put new phone number value.
        contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber);

        // Create query condition, query with the raw contact id.
        StringBuffer whereClauseBuf = new StringBuffer();

        // Specify the update contact id.
        whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID);
        whereClauseBuf.append("=");
        whereClauseBuf.append(rawContactId);

        // Specify phone type.
        whereClauseBuf.append(" and ");
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE);
        whereClauseBuf.append(" = ");
        whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);

        // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
        Uri dataUri = ContactsContract.Data.CONTENT_URI;

        // Get update data count.
        int updateCount = getContentResolver().update(dataUri, contentValues, whereClauseBuf.toString(), null);

        return updateCount;
    }

    private String moveContacts(String phoneNumber) {
        String beginNumberPhone = phoneNumber.substring(0, 4);
        String lastNumberPhone = phoneNumber.substring(4, phoneNumber.length());
        String newPhone = "";
        switch (beginNumberPhone) {
            case "0120":
                newPhone = "070";
                break;
            case "0121":
                newPhone = "079";
                break;
            case "0122":
                newPhone = "077";
                break;
            case "0126":
                newPhone = "076";
                break;
            case "0128":
                newPhone = "078";
                break;
            case "0123":
                newPhone = "083";
                break;
            case "0124":
                newPhone = "084";
                break;
            case "0125":
                newPhone = "085";
                break;
            case "0127":
                newPhone = "081";
                break;
            case "0129":
                newPhone = "082";
                break;
            case "0162":
                newPhone = "032";
                break;
            case "0163":
                newPhone = "033";
                break;
            case "0164":
                newPhone = "034";
                break;
            case "0165":
                newPhone = "035";
                break;
            case "0166":
                newPhone = "036";
                break;
            case "0167":
                newPhone = "037";
                break;
            case "0168":
                newPhone = "038";
                break;
            case "0169":
                newPhone = "039";
                break;
            case "0186":
                newPhone = "056";
                break;
            case "0188":
                newPhone = "058";
                break;
            case "0199":
                newPhone = "059";
                break;
        }
        if (newPhone.equals("")) {
            newPhone = beginNumberPhone;
        }
        return newPhone + lastNumberPhone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_contacts:
                mContactsAdapter.setListContacts(mListContacts);
                mListContactsRcv.setAdapter(mContactsAdapter);
                mTotalContacts.setText(new StringBuilder().append("Total phone number need move :").append(mListContacts.size()).toString());
                break;
            case R.id.forward_contacts:
                List<Contacts> mContacts = new ArrayList<>();
                String newPhone;
                for (Contacts contacts : mListContacts) {
                    newPhone = moveContacts(contacts.getContactsNumber());
                    int row = updatePhoneNumber(contacts.getId(), newPhone);
                    if (row == 1) {
                        mContacts.add(new Contacts(contacts.getId(), contacts.getContactsName(), newPhone));
                    }
                }
                mContactsAdapter.setListContacts(mContacts);
                mTotalContacts.setText(new StringBuilder().append("Moved ").append(mContacts.size()).append(" phone number to 10 number").toString());
                break;
            case R.id.restore_contacts:

                for (Contacts contacts : mListContacts) {
                    updatePhoneNumber(contacts.getId(), contacts.getContactsNumber());
                }
                mTotalContacts.setText(R.string.restore_contacts);
                mContactsAdapter.setListContacts(mListContacts);
                break;
        }
    }
}
