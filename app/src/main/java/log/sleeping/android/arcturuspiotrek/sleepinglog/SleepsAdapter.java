package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;

public class SleepsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Sleep> list = new ArrayList<Sleep>();
    private Context context;
    final AppDatabase db;
    public Activity activity;


    public SleepsAdapter(ArrayList<Sleep> list, Context context, Activity activity) {
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
            view = inflater.inflate(R.layout.activity_listview_sleeps, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.textViewSleepString_item);
        String s= list.get(position).getDate()+" | "+list.get(position).getDurationh()+"h "+list.get(position).getDurationm()+"min";
        listItemText.setText(s);

        //Handle buttons and add onClickListeners
        Button editBtn = (Button)view.findViewById(R.id.buttonEditSleep);

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }
}
