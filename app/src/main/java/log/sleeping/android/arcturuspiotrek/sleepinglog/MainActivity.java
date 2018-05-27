package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import log.sleeping.android.arcturuspiotrek.sleepinglog.adapters.MyCustomAdapter;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dziennik czasu snu - OSOBY");
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        populateRecommendation(db);

        final ArrayList<User> userList = new ArrayList<User>(db.userDao().getAll());


        MyCustomAdapter adapterv2 = new MyCustomAdapter(userList, this, MainActivity.this);
        GridView lView = (GridView) findViewById(R.id.listViewOfUsers);
        lView.setAdapter(adapterv2);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "Osoba: "+userList.get(position).getName(), Toast.LENGTH_SHORT).show();

                Intent sleepListActivity = new Intent(MainActivity.this, SleepListActivity.class);
                System.out.println(userList.get(position).getId()+" "+userList.get(position).getName());
                sleepListActivity.putExtra("userId", userList.get(position).getId());
                MainActivity.this.startActivity(sleepListActivity);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addUserActivity = new Intent(MainActivity.this, AddUserActivity.class);
                MainActivity.this.startActivity(addUserActivity);
            }
        });





        //Toast.makeText(getApplicationContext(), db.userDao().getUserById(1).getName(), Toast.LENGTH_SHORT).show();

    }

    public void populateRecommendation(AppDatabase db){
        if(db.RecommenadtionDao().getAll().size()==0){
            Recommendation[] listRec = new Recommendation[7];
            listRec[0] = new Recommendation("Toddlers", 1,2,11,14);
            listRec[1] = new Recommendation("Preschoolers ", 3,5,10,13);
            listRec[2] = new Recommendation("School age children ", 6,13,9,11);
            listRec[3] = new Recommendation("Teenagers", 14,17,8,10);
            listRec[4] = new Recommendation("Younger adults ", 18,25,7,9);
            listRec[5] = new Recommendation("Adults", 26,64,7,9);
            listRec[6] = new Recommendation("Older adults", 65,9999,7,8);

            db.RecommenadtionDao().insertAll(listRec);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Informacje nt. odpowiedniego czasu snu");

                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final TextView textViewContent = new TextView(this);
                textViewContent.setText(R.string.info);
                textViewContent.setTextSize(17);
                textViewContent.setTextColor(Color.parseColor("#000000"));
                layout.addView(textViewContent);

                alertDialog.setView(layout);

                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }

}
