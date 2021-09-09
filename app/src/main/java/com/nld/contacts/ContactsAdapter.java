package com.nld.contacts;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsHolder> {
    private List<Contacts> listContacts;

    public ContactsAdapter(List<Contacts> listContacts) {
        this.listContacts = listContacts;
    }

    @Override
    public ContactsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts, parent, false);
        Log.e("TAG","a");
        return new ContactsHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsHolder holder, int position) {
        Contacts contacts = listContacts.get(position);
        holder.contactName.setText(contacts.getContactsName());
        holder.contactNumber.setText(contacts.getContactsNumber());
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    public void setListContacts(List<Contacts> mContacts) {
        listContacts = mContacts;
        notifyDataSetChanged();
    }

    public class ContactsHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactNumber;

        public ContactsHolder(View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contacts_name);
            contactNumber = itemView.findViewById(R.id.contacts_number);
        }
    }
}
