package com.cst2335.jone0648;


import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
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

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChatroomActivity";

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
        send.setOnClickListener( v -> {
            String inputText = input.getText().toString();
            Log.i(TAG, "Adding a sending row");
            messages.add(new Messages(inputText, true));
            input.setText("");
            adapter.notifyDataSetChanged();
        });

        Button receive = findViewById(R.id.lab4_receive_button);
        receive.setOnClickListener(click -> {
            String inputText = input.getText().toString();
            Log.i(TAG, "Adding a receiving row");
            messages.add(new Messages(inputText, false));
            input.setText("");
            adapter.notifyDataSetChanged();
        });

        listView.setOnItemClickListener((p, b, index, id) -> {
            AlertDialog.Builder Builder = new AlertDialog.Builder(this);
            Builder.setTitle(String.format(getString(R.string.lab4_delete), index, id))
                    .setIcon(0)
                    .setPositiveButton("Yes", (v, arg) -> {
                        messages.remove(index);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (v, arg) -> {
                    });
            Builder.create().show();
            return;
        });
    }

    public class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return messages.size();
        }

        public Object getItem(int position) {
            return messages.get(position).msg;
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int index, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();
            if (!messages.get(index).sr){
                View v1 = inflater.inflate(R.layout.activity_chat_send, parent, false);

                TextView tv = v1.findViewById(R.id.textView);
                tv.setText( getItem(index).toString() );

                return v1;
            }
            else  {
                View v2 = inflater.inflate(R.layout.activity_chat_receive, parent, false);
                TextView tv = v2.findViewById(R.id.textView);
                tv.setText(getItem(index).toString());
                return v2;
            }
        }



    }
}
