package com.example.mustufa.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class EditNotesActivity extends AppCompatActivity {

    private EditText mEditNotes;
    private static final String MY_PREF = "myPref";
    int position;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);

        mEditNotes = findViewById(R.id.editNotes);

        Intent intent = getIntent();
         position = intent.getIntExtra("notes",1);

        if(position !=1) {

            mEditNotes.setText(MainActivity.mNotes.get(position));
            mEditNotes.setSelection(mEditNotes.getText().length());

        }else {

            MainActivity.mNotes.add("");
            position = MainActivity.mNotes.size() -1;
            MainActivity.adapter.notifyDataSetChanged();
        }


        //textChange Listener
        mEditNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                MainActivity.mNotes.set(position, String.valueOf(charSequence));
                MainActivity.adapter.notifyDataSetChanged();


                SharedPreferences sharedPreferences = getSharedPreferences(MY_PREF,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                HashSet<String> set = new HashSet<String>(MainActivity.mNotes);
                editor.putStringSet("notes",set);
                editor.apply();

            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });
    }

}
