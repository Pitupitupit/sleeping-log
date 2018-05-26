package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
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
            listRec[6] = new Recommendation("Older adults", 65,200,7,8);

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
