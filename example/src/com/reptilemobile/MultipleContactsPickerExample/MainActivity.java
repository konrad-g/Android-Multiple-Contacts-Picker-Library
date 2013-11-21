package com.reptilemobile.MultipleContactsPickerExample;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.reptilemobile.MultipleContactsPicker.ContactData;
import com.reptilemobile.MultipleContactsPicker.ContactPickerActivity;

/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class MainActivity extends Activity{
	
	final int REQUEST_CODE = 100;
	
	Button btnPickContact = null;
	TextView tvData = null;
	CheckBox cbCheckAll = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_activity);
		
		initUi();
		setUi();
		
	}
	
	public void initUi() {
		tvData = (TextView) findViewById(R.id.tvData);
		btnPickContact = (Button) findViewById(R.id.btnPick);
		cbCheckAll = (CheckBox) findViewById(R.id.cbCheckAll);
	}
	
	public void setUi() {
		
		btnPickContact.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent contactPicker = new Intent(MainActivity.this, ContactPickerActivity.class);
				contactPicker.putExtra(ContactData.CHECK_ALL, cbCheckAll.isChecked());
				contactPicker.setPackage("com.reptilemobile.MultipleContactsPicker");
				startActivityForResult(contactPicker, REQUEST_CODE);
				
			}
		});

	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == REQUEST_CODE) {
			if(resultCode == Activity.RESULT_OK) {
				if(data.hasExtra(ContactData.CONTACTS_DATA)) {
					ArrayList<ContactData> contacts = data.getParcelableArrayListExtra(ContactData.CONTACTS_DATA);

					if(contacts != null) {

						String dataTxt = "";
						
						Iterator<ContactData> iterContacts = contacts.iterator();
						while(iterContacts.hasNext()) {
							
							ContactData contact = iterContacts.next();
							
							dataTxt += contact.firstname + " " + contact.lastname + " " + contact.phoneNmb + " " + contact.email + "\n";
							
						}
						tvData.setText(dataTxt);
						
					}
				}
			}
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
