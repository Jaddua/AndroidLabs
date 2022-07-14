package com.cst2335.jone0648;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "CHATROOM_ACTIVITY";

    ArrayList<Messages> messages = new ArrayList<>();

    MyListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        ListView listView = findViewById(R.id.lab4listview);
        listView.setAdapter(adapter = new MyListAdapter());

        EditText input = findViewById(R.id.lab4_message_input);
        Button send = findViewById(R.id.lab4_send_button);
        loadDataFromDatabase();


        send.setOnClickListener( v -> {
            String inputText = input.getText().toString();
            Log.i(TAG, "Adding a sending row");
            MyOpener dataBaseOpener = new MyOpener(this);
            SQLiteDatabase db = dataBaseOpener.getWritableDatabase();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL2, inputText);
            newRowValues.put(MyOpener.COL3, 1);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            //Messages message = new Messages(inputText, false, newId);
            messages.add(new Messages(inputText, true, newId));
            input.setText("");
            adapter.notifyDataSetChanged();

        });

        Button receive = findViewById(R.id.lab4_receive_button);
        receive.setOnClickListener(click -> {
            String inputText = input.getText().toString();
            Log.i(TAG, "Adding a receiving row");
            MyOpener dataBaseOpener = new MyOpener(this);
            SQLiteDatabase db = dataBaseOpener.getWritableDatabase();
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL2, inputText);
            newRowValues.put(MyOpener.COL3, 0);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            //Messages messages = new Messages(inputText, true, newId);
            messages.add(new Messages(inputText, false, newId));
            input.setText("");
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemLongClickListener((p, b, index, id) -> {
            AlertDialog.Builder Builder = new AlertDialog.Builder(this);
            Builder.setTitle(String.format(getString(R.string.lab4_delete), index, id))
                    .setIcon(0)
                    .setPositiveButton("Yes", (v, arg) -> {
                        messages.remove(index);
                        adapter.notifyDataSetChanged();
                        deleteMessage(messages.get(index));
                    })
                    .setNegativeButton("No", (v, arg) -> {
                    });
            Builder.create().show();
            return false;
        });
    }
    protected void deleteMessage(Messages m) {
        MyOpener myOpener = new MyOpener(this);
        SQLiteDatabase db = myOpener.getWritableDatabase();
        db.delete(myOpener.TABLE_NAME, myOpener.COL1 + "= ?", new String[]{Long.toString(m.getId())});
    }
    public class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return messages.size();
        }

        public Messages getItem(int position) {
            return messages.get(position);
        }

        public long getItemId(int position) {
            return getItem(position).getId();
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();

            //New row:
            if (!messages.get(position).sr) {
                View newView1 = inflater.inflate(R.layout.activity_chat_send, parent, false);
                //set what the text should be for this row:
                TextView tv1 = newView1.findViewById(R.id.textView);
                tv1.setText(getItem(position).getMessage());

                //return it to be put in the table
                return newView1;
            } else {
                View newView2 = inflater.inflate(R.layout.activity_chat_receive, parent, false);
                //set what the text should be for this row:
                TextView tv2 = newView2.findViewById(R.id.textViewr);

                tv2.setText(getItem(position).getMessage());

                //return it to be put in the table
                return newView2;
            }
        }


    }
        Cursor results;
        private void loadDataFromDatabase() {
           MyOpener dataBaseOpener = new MyOpener(this);
            SQLiteDatabase db = dataBaseOpener.getWritableDatabase();

            String[] columns = {MyOpener.COL1, MyOpener.COL2, MyOpener.COL3};
            results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);
            printCursor(results, db.getVersion());
            int messageColIndex = results.getColumnIndex(MyOpener.COL2);
            int idColIndex = results.getColumnIndex(MyOpener.COL1);
            int sOrR = results.getColumnIndex(MyOpener.COL3);


            while (results.moveToNext()) {
                String message = results.getString(messageColIndex);
                long id = results.getLong(idColIndex);
                boolean value = (results.getString(sOrR).equals("1"));

                messages.add(new Messages(message, value, id));

            }

        }

    public void printCursor(Cursor c, int version) {
        Log.i("Database Version", Integer.toString(version));
        Log.i("Number of Columns", Integer.toString(c.getColumnCount()));
        Log.i("Column Names", Arrays.toString(c.getColumnNames()));
        Log.i("Number of Rows", Integer.toString(c.getCount()));
    }


    }

