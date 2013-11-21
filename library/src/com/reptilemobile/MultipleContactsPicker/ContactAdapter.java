package com.reptilemobile.MultipleContactsPicker;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class ContactAdapter extends ArrayAdapter<ContactData> {

	public ArrayList<ContactData> items;
	public String filter = "";

	ContactPickerActivity actv;

    public ContactAdapter(ContactPickerActivity actv, ArrayList<ContactData> items) {
        super(actv, R.layout.contact_row,items );

        this.actv = actv;
        this.items = items;
        
    }

    @Override
    public View getView(final int position, View convertView,
                                            ViewGroup parent) {
        View row = convertView;
        	
        LayoutInflater inflater= actv.getLayoutInflater();
        row=inflater.inflate(R.layout.contact_row, parent, false);
        

        final ContactData contact = getItem(position);
        
        boolean show = true;
        //Filter ciontacts if needed
        if(filter.length() > 0) {
        	show = false;
        	
        	if(contact.firstname.contains(filter) || contact.lastname.contains(filter) || 
        			contact.email.contains(filter) || contact.phoneNmb.contains(filter)) {
        		show = true;
        	}
        	
        }

        //Init items
        TextView tvName = (TextView) row.findViewById(R.id.tvName);
        TextView tvData = (TextView) row.findViewById(R.id.tvData);
        CheckBox cbSelect = (CheckBox) row.findViewById(R.id.cbSelect);
        
        View wrapper = row.findViewById(R.id.llWrapper);
        View divider = row.findViewById(R.id.divider);

        //Decide if we should show it or not
        if(!show) {
        	wrapper.setVisibility(View.GONE);
        	divider.setVisibility(View.GONE);
        } else {
        	wrapper.setVisibility(View.VISIBLE);
        	divider.setVisibility(View.VISIBLE);
        }

        //Prepare data
        if(contact.firstname == null) {
        	contact.firstname = "---";
        }
        
        //Set items
        tvName.setText(contact.displayName );
        tvData.setText(contact.phoneNmb + " " + contact.email);
        cbSelect.setChecked( contact.checked );

        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				contact.checked = isChecked;
				
				// Update number of selected contacts
				if(contact.checked) {
		        	ContactData.contactsSelected++;
		        } else {
		        	if(ContactData.contactsSelected > 0) {
		        		ContactData.contactsSelected--;
		        	}
		        }
				
				actv.updateNrSelected();
				
			}
		});

        return(row);
    }

    @Override
    public void addAll(Collection<? extends ContactData> collection) {
    	items.addAll(collection);
    	//super.addAll(collection);
    }
    
    @Override
    public void add(ContactData object) {
    	items.add(object);
    	//super.add(object);
    }

    @Override
    public void remove(ContactData object) {
    	items.remove(object);
    	super.remove(object);
    }
    

}