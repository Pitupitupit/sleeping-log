package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class AddUserActivity extends AppCompatActivity {

    String name;
    int age;
    EditText editTextName;
    EditText editTextAge;
    Button buttonSaveUser;
    User newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_form);

        editTextName = (EditText) findViewById(R.id.userName);
        editTextAge = (EditText) findViewById(R.id.userAge);
        buttonSaveUser = (Button) findViewById(R.id.userSave);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();

        buttonSaveUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSaveUser.setClickable(false);

                name = editTextName.getText().toString();
                age = Integer.parseInt(editTextAge.getText().toString());

                newUser = new User(name,age,true);
                db.userDao().insertAll(newUser);

                Intent mainActivity = new Intent(AddUserActivity.this, MainActivity.class);
                //main.putExtra("userRef", userRef);
                //main.putExtra("login", login);
                AddUserActivity.this.startActivity(mainActivity);
            }
        });






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