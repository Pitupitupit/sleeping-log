package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class SleepListActivity extends AppCompatActivity {

    int userId;
    String currentDate;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeps);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");

        } else {
            userId = 0; //bląd
        }

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        user = db.userDao().getUserById(userId);
        currentDate = getStringDateFromMilis(System.currentTimeMillis());
        db.SleepDao().insertAll(new Sleep(userId, 1,2,"01-05-2018"));
        db.SleepDao().insertAll(new Sleep(userId, 2,2,"30-04-2018"));
        db.SleepDao().insertAll(new Sleep(userId, 3,2,"01-04-2018"));
        db.SleepDao().insertAll(new Sleep(userId, 4,2,"01-04-2018"));
        db.SleepDao().insertAll(new Sleep(userId, 5,2,"01-04-2018"));
        db.SleepDao().insertAll(new Sleep(userId, 6,2,"01-04-2018"));

        todaySleep(db);
        final ArrayList<Sleep> sleepList = new ArrayList<Sleep>(db.SleepDao().getSleepfOfUser(user.getId()));

        SleepsAdapter adapter = new SleepsAdapter(sleepList, this, SleepListActivity.this);
        ListView lView = (ListView)findViewById(R.id.listViewSleeps);
        lView.setAdapter(adapter);

        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(), "Sen data: "+sleepList.get(position).getDate(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    public void todaySleep(AppDatabase db){
        Sleep checkSleep = db.SleepDao().getSleepByIdAndDate(user.getId(), currentDate);
        Button buttonSaveToday = (Button)findViewById(R.id.buttonSaveToday);
        LinearLayout todaylayout = (LinearLayout)findViewById(R.id.todayLayout);
        if(checkSleep != null)
        {
            Toast.makeText(getApplicationContext(), "checkSleep nie null H="+checkSleep.getDurationh()+" min="+checkSleep.getDurationm(), Toast.LENGTH_SHORT).show();

            buttonSaveToday.setVisibility(View.GONE);
            todaylayout.setVisibility(LinearLayout.GONE);

        }
        else
        {
            Toast.makeText(getApplicationContext(), "checkSleep NULL", Toast.LENGTH_SHORT).show();
            getTodayHoursAndMinutes(db);

        }
    }

    public void getTodayHoursAndMinutes(final AppDatabase db){
        final EditText editTextH = (EditText)findViewById(R.id.editTextHours);
        final EditText editTextM = (EditText)findViewById(R.id.editTextMinutes);
        Button buttonSaveToday = (Button)findViewById(R.id.buttonSaveToday);
        buttonSaveToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int durationH = Integer.parseInt(editTextH.getText().toString());
                int durationM = Integer.parseInt(editTextM.getText().toString());

                Sleep s = new Sleep(user.getId(), durationH, durationM, currentDate);
                db.SleepDao().insertAll(s);
                finish();
                startActivity(getIntent());
            }
        });
    }

    public String getStringDateFromMilis(long milis){
        Date date = new Date();
        date.setTime(milis);
        String formattedDate=new SimpleDateFormat("dd-MM-yyyy").format(date);
        return formattedDate;
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
