package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
        setTitle("Dodaj nową osobę");

        editTextName = (EditText) findViewById(R.id.userName);
        editTextAge = (EditText) findViewById(R.id.userAge);
        buttonSaveUser = (Button) findViewById(R.id.userSave);

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();

        buttonSaveUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buttonSaveUser.setClickable(false);

                name = editTextName.getText().toString();
                if(editTextAge.getText().toString().equals("")){
                    age = -1;
                }
                else
                    age = Integer.parseInt(editTextAge.getText().toString());

                newUser = new User(name,age,true);
                db.userDao().insertAll(newUser);

                Intent mainActivity = new Intent(AddUserActivity.this, MainActivity.class);
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