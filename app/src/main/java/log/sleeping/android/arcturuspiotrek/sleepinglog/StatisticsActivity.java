package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

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
        setTitle("STATYSTYKI");

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
            //System.out.println(date.get(Calendar.MONTH));

        }
        listGraphsData.add(gh);

        Iterator<GraphData> iter = listGraphsData.iterator();
        while (iter.hasNext()) {
            GraphData x = iter.next();
            if (x.getListSleeps().size()==0)
            {
                iter.remove();
                System.out.println("usuwam");
            }
        }

        for(GraphData x : listGraphsData)
        {
            Collections.reverse(x.getListSleeps());
        }

        for(GraphData x : listGraphsData)
        {
            for(Sleep s : x.getListSleeps())
            {
                System.out.println(s.getDate()+" "+s.getDurationh()+"h");
            }
            System.out.println("kuniec mies");
        }

        System.out.println("statystyki size:" +listGraphsData.size());

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
