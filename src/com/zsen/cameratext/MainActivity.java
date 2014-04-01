package com.zsen.cameratext;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.telephony.SmsManager;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private MyView myview;
	EditText editReceiver;
	String userNumber;
	EditText editContent;
	/** Called when the activity is first created. */
	@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(1);
    LinearLayout.LayoutParams layoutParams =   new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    layout.setLayoutParams(layoutParams);
    
    LinearLayout layoutSender = new LinearLayout(this);
    layoutSender.setOrientation(0);
    
    editReceiver=new EditText(this);
    editReceiver.setHint("");
    editReceiver.setEms(12);
    editReceiver.setInputType(InputType.TYPE_CLASS_PHONE);
    ViewGroup.LayoutParams editReciverParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    editReceiver.setLayoutParams(editReciverParams);
    layoutSender.addView(editReceiver);
    
    
    Button btnSelect = new Button(this);
    btnSelect.setText("RECEIVER");
    ViewGroup.LayoutParams btnSelectParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    btnSelect.setLayoutParams(btnSelectParams);
    layoutSender.addView(btnSelect);
    
    layout.addView(layoutSender);
    
    editContent=new EditText(this);
    editContent.setHint("PLEASE TYPE IN CONTENT");
    editContent.setLines(2);
    editContent.setInputType(InputType.TYPE_CLASS_TEXT);
    ViewGroup.LayoutParams editContentParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    editContent.setLayoutParams(editContentParams);
    layout.addView(editContent);
    
    Button btnSend = new Button(this);
    btnSend.setText("CLICK HERE TO SEND THE TEXT");
    ViewGroup.LayoutParams btnSendParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    btnSend.setLayoutParams(btnSendParams);
    layout.addView(btnSend);

    myview = new MyView(this);
    myview.setLayoutParams(btnSelectParams);
    layout.addView(myview);
    
    setContentView(layout,layoutParams);     
    
    btnSelect.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivityForResult(new Intent(  
		    Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI), 0);  
		}
	});
    
    btnSend.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(userNumber==null)
			{
				Toast.makeText(MainActivity.this, "No number is selected, please choose one",  Toast.LENGTH_LONG).show();
			}
			else if(editContent.getText()==null)
			{
				Toast.makeText(MainActivity.this, "No content is typed",  Toast.LENGTH_LONG).show();
			}
			else
			{
				 SmsManager smsManager = SmsManager.getDefault();
                 if(editContent.getText().length() > 70) {
                     List<String> contents = smsManager.divideMessage(editContent.getText().toString().trim());
                     for(String sms : contents) {
                         smsManager.sendTextMessage(userNumber, null, sms, null, null);
                     }
                 } else {
                  smsManager.sendTextMessage(userNumber, null, editContent.getText().toString().trim(), null, null);
                 }
                 Toast.makeText(MainActivity.this, "Message has been sent", Toast.LENGTH_SHORT).show();
			}
		}
	});
    
    /*btnSend.setOnClickListener(new OnClickListener() {                        
                    @Override
                    public void onClick(View v) {
                            
     
                    }
            });   */  
}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) { 
		    //ContentProviderչʾ��������һ���������ݿ��
		    //ContentResolverʵ�����ķ�����ʵ���ҵ�ָ����ContentProvider����ȡ��ContentProvider������
		                ContentResolver reContentResolverol = getContentResolver();  
		                //URI,ÿ��ContentProvider����һ��Ψһ�Ĺ�����URI,����ָ�����������ݼ�
		                Uri contactData = data.getData();
		                //��ѯ��������URI�Ȳ���,����URI�Ǳ����,�����ǿ�ѡ��,���ϵͳ���ҵ�URI��Ӧ��ContentProvider������һ��Cursor����.
		                Cursor cursor = managedQuery(contactData, null, null, null, null);  
		                cursor.moveToFirst(); 
		                //���DATA���е�����
		                String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));  
		                //����Ϊ��ϵ��ID
		                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));  
		                // ���DATA���еĵ绰���룬����Ϊ��ϵ��ID,��Ϊ�ֻ�������ܻ��ж��
		                Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,   
		                         null,   
		                         ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,   
		                         null,   
		                         null);  
		                 while (phone.moveToNext()) {  
		                     userNumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
		                 }  
	                     editReceiver.setText(username);  
		      
		            }  
	}
}