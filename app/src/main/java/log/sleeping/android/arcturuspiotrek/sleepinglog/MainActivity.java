package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class MainActivity extends AppCompatActivity {

    String[] array = {"Piooio","asdfasdf","TAKTAKFFF"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        //User user = new User("Piotrek", 23);

        //db.userDao().insertAll(user);

        ArrayList<User> userList = new ArrayList<User>();
        userList.addAll(db.userDao().getAll());

        String[] names = new String[userList.size()];
        int[] ages = new int[userList.size()];
        String[] combinedNamesAndAges = new String[userList.size()];

        ArrayList<String> combinedList = new ArrayList<String>();

        int i = 0;
        for(User u : userList){
            combinedNamesAndAges[i] = u.getName() + " " + Integer.toString(u.getAge());
            combinedList.add(u.getName() + " " + Integer.toString(u.getAge()));
            names[i] = u.getName();
            ages[i++] = u.getAge();
        }

        MyCustomAdapter adapterv2 = new MyCustomAdapter(userList, this, MainActivity.this);
        ListView lView = (ListView)findViewById(R.id.listViewOfUsers);
        lView.setAdapter(adapterv2);

        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.activity_listview, combinedNamesAndAges);
        //ListView listView = (ListView) findViewById(R.id.listViewOfUsers);
        //listView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, db.userDao().getUserById(1).getName(), Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent addUserActivity = new Intent(MainActivity.this, AddUserActivity.class);
                //main.putExtra("userRef", userRef);
                //main.putExtra("login", login);
                MainActivity.this.startActivity(addUserActivity);
            }
        });

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "XDDDDDDDDD", Toast.LENGTH_SHORT).show();
            }
        });*/



        //Toast.makeText(getApplicationContext(), db.userDao().getUserById(1).getName(), Toast.LENGTH_SHORT).show();

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
