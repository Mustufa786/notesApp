package com.example.mustufa.notes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    public static ArrayList<String> mNotes = new ArrayList<>();
    public static ArrayAdapter<String> adapter;

    private static final String MY_PREF = "myPref";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mListView = findViewById(R.id.listView);

        SharedPreferences preferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) preferences.getStringSet("notes",null);

        if(set == null) {

            mNotes.add("Example Note");

        }else {

            mNotes = new ArrayList<>(set);
        }


        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,mNotes);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent editIntent = new Intent(MainActivity.this,EditNotesActivity.class);
                editIntent.putExtra("notes",i);
                startActivity(editIntent);
            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int pos = i;

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
                dialogBuilder.setTitle("Are You Sure?");
                dialogBuilder.setMessage("This Item will be deleted permenantly");
                dialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                dialogBuilder.setCancelable(false);
                dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mNotes.remove(pos);
                        adapter.notifyDataSetChanged();

                        SharedPreferences sharedPreferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        HashSet<String> set = new HashSet<String>(MainActivity.mNotes);
                        editor.putStringSet("notes",set);
                        editor.apply();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return true;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch (item.getItemId()) {

           case R.id.addNotes:

               Intent intent = new Intent(MainActivity.this,EditNotesActivity.class);
               startActivity(intent);

               break;
       }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        SharedPreferences preferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) preferences.getStringSet("notes",null);

        if(set != null) {

            mNotes = new ArrayList<>(set);
        }

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,mNotes);
        mListView.setAdapter(adapter);
    }


}
