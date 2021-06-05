package com.example.upiapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
public class MainActivity extends AppCompatActivity {

    Button insert, view, delete;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        insert = findViewById(R.id.btnInsert);
        view = findViewById(R.id.btnView);
        delete = findViewById(R.id.btnDelete);
        DB = new DBHelper(this);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, PackageManager.PERMISSION_GRANTED);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.delete();
            }
        });
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uriSms = Uri.parse("content://sms/inbox");
                Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

                cursor.moveToFirst();
                while  (cursor.moveToNext())
                {
                    String body = cursor.getString(3);
                    ///String body="Rs.10000.00 is credited in your kotak bank a/c xxxxx5232 by UPI ID 8106930218@ybl on 01-05-21";
                    String address = cursor.getString(1);


                    if(body.contains("credited") && body.contains("UPI ID") ) {
                        Log.i("msg",String.valueOf(body));
                        // messages.add(dateVal + ":" + msgData + "axqw" + add);
                        //contentMessage.add(msgData);
                        //sms.add("Address=&gt; "+address+"n SMS =&gt; "+body);
                        //String transactiondate, String transStatus, int amount
                        int amount =Integer.parseInt(body.substring(body.indexOf("Rs.")+3 , body.indexOf(".00")));
                        //int amount=500;
                        String transactiondate=body.substring(body.indexOf("on ")+3, body.indexOf("on ")+11);
                        String transStatus="credited";
                       // String upiId=body.substring(body.indexOf("UPI ID ")+7 , body.indexOf(" on"));

                        Boolean checkinsertdata = DB.insertTransactionData(transactiondate, transStatus, amount);
                        if(checkinsertdata==true)
                            Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();

                    }
                    else if(body.contains("debited") && body.contains("UPI ID")) {
                        Log.i("msg",String.valueOf(body));
                        int amount =Integer.parseInt(body.substring(body.indexOf("Rs.")+3 , body.indexOf(".00")));
                        //int amount=500;
                    String transactiondate=body.substring(body.indexOf("on ")+3, body.indexOf("on ")+11);
                    String transStatus="debited";
                    //String upiId=body.substring(body.indexOf("UPI ID ")+7 , body.indexOf(" on"));

                    Boolean checkinsertdata = DB.insertTransactionData(transactiondate, transStatus, amount);
                    if(checkinsertdata==true)
                        Toast.makeText(MainActivity.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(MainActivity.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();

                }

                }


            }        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = DB.getdata();
                if(res.getCount()==0){
                    Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Transactiondate :"+res.getString(1)+"\n");
                    buffer.append("TransStatus :"+res.getString(2)+"\n");
                  //  buffer.append("UPI ID :"+res.getString(3)+"\n\n");
                    buffer.append("Amount :"+res.getString(3)+"\n\n");
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(true);
                builder.setTitle("Transaction Details");
                builder.setMessage(buffer.toString());
                builder.show();
            }        });
    }

       /// myTextView=findViewById(R.id.textView);
        //ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

       /* ListView lViewSMS = (ListView) findViewById(R.id.listViewSMS);

        if(fetchInbox()!=null)
        {
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, fetchInbox());
            lViewSMS.setAdapter(adapter);
        }
    }*/
    /*public ArrayList fetchInbox()
    {
        ArrayList sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"},null,null,null);

        cursor.moveToFirst();
        while  (cursor.moveToNext())
        {
            String address = cursor.getString(1);
            String body = cursor.getString(3);

           // System.out.println("======&gt; Mobile number =&gt; "+address);
            //System.out.println("=====&gt; SMS Text =&gt; "+body);
            if(body.contains("credited") && body.contains("UPI") || body.contains("debited") && body.contains("UPI")) {
               // messages.add(dateVal + ":" + msgData + "axqw" + add);
                //contentMessage.add(msgData);
                sms.add("Address=&gt; "+address+"n SMS =&gt; "+body);
            }

        }
        return sms;

    }*/
    /*public void Read_SMS(View view){
        Object content;
        //Cursor cursor = getContentResolver().query(Uri.parse(“content://sms”), null, null,null,null);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        cursor.moveToFirst();


        myTextView.setText(cursor.getString(12));
    }*/
}

