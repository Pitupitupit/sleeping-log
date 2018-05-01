package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class MyCustomAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<User> list = new ArrayList<User>();
    private Context context;
    final AppDatabase db;
    public Activity activity;


    public MyCustomAdapter(ArrayList<User> list, Context context, Activity activity) {
        this.list = list;
        this.context = context;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        //return list.get(pos).getId();
        //just return 0 if your list items do not have an Id variable.
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_listview, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getName()+" | "+list.get(position).getAge());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
        Button editBtn = (Button)view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                //alertDialog.setTitle("Potwierdź usunięcie: " + list.get(position).getName());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Potwierdź usunięcie: " + list.get(position).getName(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.userDao().delete(list.get(position));
                                list.remove(position); //or some other task
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Zmień dane osoby");

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText editTextName = new EditText(activity);
                editTextName.setText(list.get(position).getName());
                layout.addView(editTextName);

                final EditText editTextAge = new EditText(activity);
                editTextAge.setInputType(InputType.TYPE_CLASS_NUMBER);
                editTextAge.setText(Integer.toString(list.get(position).getAge()));
                layout.addView(editTextAge);

                alertDialog.setView(layout);

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zapisz zmiany",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                list.get(position).setName(editTextName.getText().toString());
                                list.get(position).setAge(Integer.parseInt(editTextAge.getText().toString()));
                                db.userDao().updateUser(list.get(position));
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                });
                alertDialog.show();
            }
        });

        return view;
    }
}