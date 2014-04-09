
package com.reptilemobile.MultipleContactsPicker;
import java.util.ArrayList;
import java.util.List;


import java.util.Iterator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.reptilemobile.MultipleContactsPicker.utils.ASyncUtils;
import com.reptilemobile.MultipleContactsPicker.utils.Anim;

/**
 * Author Konrad Gadzinowski
 * kgadzinowski@gmail.com
 */
public class ContactPickerActivity extends SherlockActivity {
	
	ListView lvContacts;
	ContactAdapter contactsAdapter = null;
	boolean allChecked = false;
	final int BUFFER_INTERVAL = 10;
	
	boolean finishTask = false;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.Theme_Sherlock);
		setContentView(R.layout.contact_list);
		super.onCreate(savedInstanceState);
		
		
	}
	
	@Override
	protected void onResume() {
		
		allChecked = false;
		finishTask = false;
		if(getIntent().hasExtra(ContactData.CHECK_ALL)) {
			allChecked = getIntent().getBooleanExtra(ContactData.CHECK_ALL, allChecked);
		} 
		
		setActionBar();
		
		initUi();
		
		showContacts();
	
		setUi();
		
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		
		finishTask = true;
		
		super.onDestroy();
	}
	
	public void setActionBar() {
		
		ActionBar bar = getSupportActionBar();
		bar.setTitle(getResources().getString(R.string.pick_contacts));
		bar.setHomeButtonEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayHomeAsUpEnabled(false);
		
		//Hide search box on start
        LinearLayout linearlayoutSearchBox = (LinearLayout) findViewById(R.id.linearlayoutSearchBox);
        linearlayoutSearchBox.setVisibility(View.GONE);
        
        //define search box
        final EditText etSearchBox = (EditText) findViewById(R.id.editTextSearchBox);
        etSearchBox.addTextChangedListener(searchBoxWocher);
        
        //Define close searchbox button
        ImageButton imageBtnCloseSearchBox = (ImageButton) findViewById(R.id.imageButtonCloseSearchBox);
        imageBtnCloseSearchBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//reset filter and hide search field
				etSearchBox.setText("");
				showSearchBox(false);
			}
		});
		
	}
	
	
	public void initUi() {
		
		lvContacts = (ListView) findViewById(R.id.lvContacts);
	
	}
	
	public void setUi() {
		
		ContactData.contactsSelected = 0;
		
		lvContacts.setClickable(true);
		lvContacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long id) {

				ContactData contact = contactsAdapter.getItem(pos);
				contact.checked = !contact.checked;
				contactsAdapter.notifyDataSetChanged();
				
				// Update number of selected contacts
				if(contact.checked) {
		        	ContactData.contactsSelected++;
		        } else {
		        	if(ContactData.contactsSelected > 0) {
		        		ContactData.contactsSelected--;
		        	}
		        }
				
				updateNrSelected();
			}
		});
		
		updateNrSelected();
		
	}
	
	public void updateNrSelected() {
		setTitle(getString(R.string.pick_contacts) + ": " + String.valueOf(ContactData.contactsSelected) );
		getSupportActionBar().setTitle(getString(R.string.pick_contacts) + ": " + String.valueOf(ContactData.contactsSelected) );
		
	}
	
	public void showContacts() {
		
			AsyncTask<Object, Integer, Object> showContacts = new AsyncTask<Object, Integer, Object>() {

				@Override
				protected Object doInBackground(Object... params) {
					
					// Run query on all contacts id
		            Uri uri = ContactsContract.Contacts.CONTENT_URI;
		            String[] projection = new String[] { ContactsContract.Contacts._ID,
		                                            ContactsContract.Contacts.DISPLAY_NAME};
		            String selection = null;//ContactsContract.Contacts.HAS_PHONE_NUMBER + " = '" + ("1") + "'";
		            String[] selectionArgs = null;
		            String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
		                    + " COLLATE LOCALIZED ASC";

		            ContentResolver contectResolver = getContentResolver();

		            Cursor cursor = contectResolver.query(uri, projection, selection, selectionArgs,
		                    sortOrder);
					
		            //Create buffer
		            final ArrayList<ContactData> bufferContacts = new ArrayList<ContactData>();
		            
		            //Load contacts one by one
		            if(cursor.moveToFirst()) {
		            	while(!cursor.isAfterLast()) {
		            		
		            		if(finishTask) {
		            			return null;
		            		}
		            		
		            		String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
		            		
		            		
		            		String[] emailProj = new String[]{Email.DATA};
		            		Cursor cursorEmail = contectResolver.query(Email.CONTENT_URI, emailProj,Email.CONTACT_ID + " = ? ", new String[] { id }, null);
		            		
		            		String[] phoneProj = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
		            		Cursor cursorPhone = contectResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, phoneProj,
		            				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id }, null);
		            		
		            		String firstName = "";
		            		String lastName = "";
		            		String email = "";
		            		String displayname = "";
		            		String phoneNmb = "";
		            		
		            		if(cursorPhone.moveToFirst()) {
			            		///displayname = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			            		phoneNmb = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		            		}
		            		cursorPhone.close();
		            		
		            		if(cursorEmail.moveToFirst()) {
		            			email = cursorEmail.getString(cursorEmail.getColumnIndex(Email.DATA));
		            		}
		            		cursorEmail.close();
		            		
		            		displayname = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		            		
		            		//Divide display name to first and last
		            		String[] names = new String[]{"---", "---"};
		            		
		            		if(displayname != null) {
		            			names = displayname.split("\\s+");
		            			firstName = displayname;
		            		}
		            		
		            		
		            		if(names.length >= 1) {
		            			firstName = names[0];
		            		}
		            		
		            		if(names.length >= 2) {
		            			lastName = names[1];
		            		}
		            		
		            		final ContactData contactData = new ContactData(id, firstName, lastName, displayname, phoneNmb, email, allChecked);
		            		
		            		bufferContacts.add(contactData);
		            		
		            		//Set list view initialy
			            	runOnUiThread(new Runnable() {
								public void run() {
		
									if(contactsAdapter == null) {
										ArrayList<ContactData> contacts = new ArrayList<ContactData>();
										contactsAdapter = new ContactAdapter(ContactPickerActivity.this, contacts);
										
										lvContacts.setAdapter(contactsAdapter);
									}

									if(bufferContacts.size() >= BUFFER_INTERVAL) {
										addBuffer(bufferContacts);
									}
								}
									
							});
		            		
		            		cursor.moveToNext();
		            	}
		            }
		            
		            cursor.close();
		            
		            runOnUiThread(new Runnable() {
						public void run() {

							addBuffer(bufferContacts);

						}
							
					});
		            
		            
					return null;
					
				}
			
			
			};
			
			ASyncUtils.startMyTask(showContacts, null);
		
	}
	
	public void addBuffer(ArrayList<ContactData> buffer) {
	
		// Add new contacts to count
		if(allChecked) {
			ContactData.contactsSelected += buffer.size();
			updateNrSelected();
		}

		contactsAdapter.addAll(buffer);
		contactsAdapter.notifyDataSetChanged();
		
		
		//reset buffer
		buffer.clear();
	}
	
	//show/hide search box
    public void showSearchBox(boolean show) {
    	 LinearLayout linearlayoutSearchBox = (LinearLayout) findViewById(R.id.linearlayoutSearchBox);
    	 if (show) {
    		 Anim.setLayoutAnim_slidedown(linearlayoutSearchBox, getApplicationContext());
    	 } else {
    		 Anim.setLayoutAnim_slideup(linearlayoutSearchBox, getApplicationContext());
    	 }
    }
	
	@Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
    	
    	getSupportMenuInflater().inflate(R.menu.menu_select, menu);
    	
    	//TODO: Add search option with well performance
    	//Disable search due to poor performance
    	menu.removeItem(R.id.menu_search);
    	
    	super.onCreateOptionsMenu(menu);
    	return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        int id = item.getItemId();
		if( id == android.R.id.home) {
			return true;
			/*
		 case R.id.menu_show_all:
		        mShowAll = true;
		        refreshListView("");
		        return true;
		      */  
		} else if( id == R.id.menu_search) {
			LinearLayout linearlayoutSearchBox = (LinearLayout) findViewById(R.id.linearlayoutSearchBox);
			if(linearlayoutSearchBox.getVisibility() == View.GONE) {
				//show search box
				showSearchBox(true);
			} else {
				//hide search box
				showSearchBox(false);
			}
			
			return true;

		} else if( id == R.id.menu_done) {
			returnData();
			return true;
		
		} else if( id == R.id.menu_cancel) {
			setResult(Activity.RESULT_CANCELED, null);
			finish();
			return true;
        } else if( id == R.id.menu_check) {
        	
        	//Check or uncheck all
			allChecked = !allChecked;
			
			List<ContactData> contacts = contactsAdapter.items;
			Iterator<ContactData> iterContacts = contacts.iterator();
			
			while(iterContacts.hasNext()) {
				ContactData contact = iterContacts.next();
				contact.checked = allChecked;
			}
			
			// Update selected contact numbers
			if(allChecked) {
				ContactData.contactsSelected = contactsAdapter.getCount();
			} else {
				ContactData.contactsSelected = 0;
			}
			
			contactsAdapter.notifyDataSetChanged();
			updateNrSelected();
			return true;
        
        }


		return super.onOptionsItemSelected(item);
    }
	
	public void returnData() {
		
		Intent result = new Intent();         
		
		ArrayList<ContactData> resultList = contactsAdapter.items;
		Iterator<ContactData> iterResultList = resultList.iterator();
		
		ArrayList<ContactData> results = new ArrayList<ContactData>();
		//pass only checked contacts
		while(iterResultList.hasNext()) {
			
			ContactData contactData = iterResultList.next();
			if(contactData.checked) {
				results.add(contactData);
			}
		}
		
		result.putParcelableArrayListExtra(ContactData.CONTACTS_DATA, results);
		
		setResult(Activity.RESULT_OK, result);
		finish();
		
	}
	
	//search box text listener
    TextWatcher searchBoxWocher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			filterList(s.toString());
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private void filterList(String filter) {
    	
    	if(filter != null) {
    		contactsAdapter.filter = filter;
    	} else {
    		contactsAdapter.filter = "";
    	}
    	
    	contactsAdapter.notifyDataSetChanged();
    }
	
}
