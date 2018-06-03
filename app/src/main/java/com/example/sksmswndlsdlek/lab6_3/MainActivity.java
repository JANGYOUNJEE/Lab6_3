package com.example.sksmswndlsdlek.lab6_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;
    EditText name,number;
    Button add,delete;
    ListView listView;
    String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.name);
        number=(EditText)findViewById(R.id.number);
        add=(Button)findViewById(R.id.add);
        delete=(Button)findViewById(R.id.delete);
        listView=findViewById(R.id.listview);

        helper=new MySQLiteOpenHelper(MainActivity.this,"person.db",null,1);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listname,listnumber;
                listname=name.getText().toString();
                listnumber=number.getText().toString();

                if(listname=="")
                    Toast.makeText(getApplicationContext(),"이름을 입력해주세요",Toast.LENGTH_SHORT).show();
                else if((listname=="")&&(listnumber==""))
                    Toast.makeText(getApplicationContext(),"모든 항목을 입력해주세요",Toast.LENGTH_SHORT).show();

                insert(listname,listnumber);
                invalidate();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(name.getText().toString());
                invalidate();
            }
        });
        invalidate();
    }

    public void insert(String name,String number) {
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("name",name);
        values.put("number",number);
        db.insert("student",null,values);
    }

    public void update(String name,String number){
        db=helper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("number",number);
        db.update("student",values,"name=?",new String[]{name});
    }

    public void delete(String name) {
        db=helper.getWritableDatabase();
        db.delete("student","name=?",new String[]{name});
    }
    public void select(){
        db=helper.getReadableDatabase();
        Cursor c=db.query("student",null,null,null,null,null,null);
        names=new String[c.getCount()];
        int count=0;
        while(c.moveToNext()){
            names[count]=c.getString(c.getColumnIndex("name"))+"   " +c.getString(c.getColumnIndex("number"));
            count++;
        }
        c.close();
    }

    private void invalidate(){
        select();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
    }
}
