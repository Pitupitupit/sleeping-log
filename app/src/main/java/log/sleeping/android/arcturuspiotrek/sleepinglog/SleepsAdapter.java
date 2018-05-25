package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.Activity;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
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
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class SleepsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Sleep> list = new ArrayList<Sleep>();
    private Context context;
    final AppDatabase db;
    public Activity activity;
    public User user;


    public SleepsAdapter(ArrayList<Sleep> list, Context context, Activity activity, User user) {
        this.list = list;
        this.context = context;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        this.activity = activity;
        this.user = user;
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

        Recommendation rec = SleepListActivity.determineUserType(db, user);
        //kolory
        if(list.get(position) != null){
            float duration = list.get(position).getDurationh() + list.get(position).getDurationm()/60;
            System.out.println("rec min:"+rec.getMinHours()+" rec max:"+rec.getMaxHours()+" duration:"+duration);
            if(duration >= rec.getMinHours() && duration <= rec.getMaxHours()){
                listItemText.setBackgroundColor(Color.GREEN);
            }
            else {
                listItemText.setBackgroundColor(Color.RED);
            }

        }

        //Handle buttons and add onClickListeners
        Button editBtn = (Button)view.findViewById(R.id.buttonEditSleep);

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Dane z dnia "+ list.get(position).getDate());

                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView textViewDuration = new TextView(activity);
                textViewDuration.setText("Czas trwania snu:");
                textViewDuration.setTextSize(16);
                textViewDuration.setTypeface(null, Typeface.BOLD);
                textViewDuration.setTextColor(Color.BLACK);
                layout.addView(textViewDuration);

                LinearLayout hoursMins = new LinearLayout(context);
                hoursMins.setOrientation(LinearLayout.HORIZONTAL);

                    final EditText editTextHours = new EditText(activity);
                    editTextHours.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editTextHours.setText(Integer.toString(list.get(position).getDurationh()));
                    hoursMins.addView(editTextHours);

                    final TextView textViewH = new TextView(activity);
                    textViewH.setText("h");
                    hoursMins.addView(textViewH);

                    final EditText editTextMins = new EditText(activity);
                    editTextMins.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editTextMins.setText(Integer.toString(list.get(position).getDurationm()));
                    hoursMins.addView(editTextMins);

                    final TextView textViewM = new TextView(activity);
                    textViewM.setText("min");
                    hoursMins.addView(textViewM);

                 layout.addView(hoursMins);
                 layout.setPadding(50,0,50,0);

                final TextView textViewDescription = new TextView(activity);
                textViewDescription.setText("Opis:");
                textViewDescription.setTextSize(16);
                textViewDescription.setTypeface(null, Typeface.BOLD);
                textViewDescription.setTextColor(Color.BLACK);
                layout.addView(textViewDescription);

                final EditText editTextDescription = new EditText(activity);
                editTextDescription.setText(list.get(position).getDescription());
                layout.addView(editTextDescription);

                alertDialog.setView(layout);

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Zapisz zmiany",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                list.get(position).setDurationh(Integer.parseInt(editTextHours.getText().toString()));
                                list.get(position).setDurationm(Integer.parseInt(editTextMins.getText().toString()));
                                list.get(position).setDescription(editTextDescription.getText().toString());
                                db.SleepDao().updateSleep(list.get(position));
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });


        return view;
    }

}
