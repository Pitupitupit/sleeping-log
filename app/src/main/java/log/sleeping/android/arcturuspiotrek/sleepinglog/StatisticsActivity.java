package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import log.sleeping.android.arcturuspiotrek.sleepinglog.adapters.GraphsAdapter;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;
import log.sleeping.android.arcturuspiotrek.sleepinglog.models.GraphData;

public class StatisticsActivity extends AppCompatActivity {
    int userId;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");

        } else {
            userId = -1; //bląd
        }

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        user = db.userDao().getUserById(userId);
        Recommendation rec = SleepListActivity.determineUserType(db, user);

        ArrayList<GraphData> listGraphsData = new ArrayList<GraphData>();

        ArrayList<Sleep> listSleeps = new ArrayList<Sleep>(db.SleepDao().getSleepfOfUser(userId));

        int currMonth = 0, prevMonth = 0;
        GraphData gh = new GraphData(user, rec, new ArrayList<Sleep>());
        for(Sleep s : listSleeps)
        {
            Calendar date = GregorianCalendar.getInstance();
            date.setTimeInMillis(s.getDateMilis());
            currMonth = date.get(Calendar.MONTH);
            if(currMonth != prevMonth)
            {
                listGraphsData.add(gh);
                gh = new GraphData(user, rec, new ArrayList<Sleep>());
                prevMonth = currMonth;
            }
            gh.getListSleeps().add(s);
            System.out.println(date.get(Calendar.MONTH));

        }

        GraphsAdapter graphsAdapter = new GraphsAdapter(this, user, listGraphsData);
        ListView lView = (ListView)findViewById(R.id.listViewGraphs);
        lView.setAdapter(graphsAdapter);

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
