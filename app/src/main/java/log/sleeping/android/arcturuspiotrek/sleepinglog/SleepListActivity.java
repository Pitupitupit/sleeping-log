package log.sleeping.android.arcturuspiotrek.sleepinglog;

import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import log.sleeping.android.arcturuspiotrek.sleepinglog.adapters.SleepsAdapter;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class SleepListActivity extends AppCompatActivity {

    private int numberOfDaysToGenerateForNewbie = 30;
    int userId;
    String currentDate;
    User user;
    long currentDateMilis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleeps);
        setTitle("Dziennik czasu snu - LISTA DNI");



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getInt("userId");

        } else {
            userId = -1; //bląd
        }

        final AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        user = db.userDao().getUserById(userId);
        final Recommendation r = determineUserType(db,user);

        currentDate = getStringDateFromMilis(System.currentTimeMillis());
        currentDateMilis = System.currentTimeMillis();

        if(user.getNewbie()){
            generateMonthForNewbie(db);
        }

        todaySleep(db);
        final ArrayList<Sleep> sleepList = new ArrayList<Sleep>(db.SleepDao().getSleepfOfUser(user.getId()));

        SleepsAdapter adapter = new SleepsAdapter(sleepList, this, SleepListActivity.this, user);
        ListView lView = (ListView)findViewById(R.id.listViewSleeps);
        lView.setAdapter(adapter);

        fillSleeps(db);

        //klikniecie wiersza
        lView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                float duration = sleepList.get(position).getDurationh() + sleepList.get(position).getDurationm()/60;

                if(duration >= r.getMinHours() && duration <= r.getMaxHours()){
                    Toast.makeText(getApplicationContext(), "Świetnie!", Toast.LENGTH_SHORT).show();
                }
                else if(duration < r.getMinHours()) {
                    Toast.makeText(getApplicationContext(), "Za krótko!", Toast.LENGTH_SHORT).show();
                }
                else if(duration > r.getMaxHours()) {
                    Toast.makeText(getApplicationContext(), "Za długo!", Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(getApplicationContext(), "Sen data: "+sleepList.get(position).getDate(), Toast.LENGTH_SHORT).show();

            }
        });

        Button buttonStatistics = (Button)findViewById(R.id.buttonStatistics);
        buttonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent statisticsActivity = new Intent(SleepListActivity.this, StatisticsActivity.class);
                System.out.println(user.getId());
                statisticsActivity.putExtra("userId", user.getId());
                SleepListActivity.this.startActivity(statisticsActivity);
            }
        });


        TextView howMuchSleepShould = (TextView)findViewById(R.id.howMuchSleepShould);
        howMuchSleepShould.setText("Zalecany czas snu: "+r.getMinHours()+"-"+r.getMaxHours()+" godzin");

    }

    public static Recommendation determineUserType(AppDatabase db, User user){
        for(Recommendation x : db.RecommenadtionDao().getAll()){
            if(user.getAge() >= x.getMinAge() && user.getAge() <= x.getMaxAge()){
                return new Recommendation(x.getName(), x.getMinAge(), x.getMaxAge(), x.getMinHours(), x.getMaxHours());
            }
        }
        return null;
    }

    //jeśli użytkownik nie wchodzil przez przynajmniej ponad 1 dzień to trzeba uzupelnić liste snów - daty muszą iśc po kolei - nie moze być dziur
    public void fillSleeps(AppDatabase db){
        Date startdate = new Date();
        startdate.setTime(db.SleepDao().getLastSleepOfUser(user.getId()).getDateMilis() + 24*60*60*1000); // dodawanie dnia by nie bralo ostatniego wpisu tylko o dzien nastepny

        Date enddate = new Date();
        enddate.setTime(currentDateMilis - 24*60*60*1000); //BY NIE BRALO DZISIAJ (-1 dzień)

        ArrayList<Sleep> fillSleepList = new ArrayList<Sleep>();

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);
        //calendar.add(Calendar.DATE, 1); // BY NIE BRALO początkowej daty

        while (calendar.getTime().before(enddate))
        {
            Date result = calendar.getTime();
            fillSleepList.add(new Sleep(userId,0,0,result.getTime(), ""));
            calendar.add(Calendar.DATE, 1);
        }

        if(!fillSleepList.isEmpty())
        {
            System.out.println("fillsleepLIST NOT EMPTY");
            for(Sleep s : fillSleepList){
                System.out.println(s.getDate()+" "+s.getDateMilis());
            }
            Sleep[] sleepArray = fillSleepList.toArray(new Sleep[fillSleepList.size()]);
            db.SleepDao().insertAll(sleepArray);

            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }

    }

    public void generateMonthForNewbie(AppDatabase db){
        ArrayList<Sleep> a = new ArrayList<Sleep>();
        Date date = new Date();
        date.setTime(currentDateMilis);
        Calendar c = Calendar.getInstance();

        for(int i=0; i<numberOfDaysToGenerateForNewbie; i++){
            c.setTime(date);
            c.add(Calendar.DATE, -1);
            date = c.getTime();
            a.add(new Sleep(user.getId(), 0, 0, date.getTime(),""));
        }
        Sleep[] sleepArray = a.toArray(new Sleep[a.size()]);
        db.SleepDao().insertAll(sleepArray);

        user.setNewbie(false);
        db.userDao().updateUser(user);

    }

    public void todaySleep(AppDatabase db){
        Sleep checkSleep = db.SleepDao().getSleepByIdAndDate(user.getId(), currentDate);
        Button buttonSaveToday = (Button)findViewById(R.id.buttonSaveToday);
        LinearLayout todaylayout = (LinearLayout)findViewById(R.id.todayLayout);
        if(checkSleep != null)
        {
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

                Sleep s = new Sleep(user.getId(), durationH, durationM, currentDateMilis,"");
                db.SleepDao().insertAll(s);

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public static String getStringDateFromMilis(long milis){
        Date date = new Date();
        date.setTime(milis);
        String formattedDate=new SimpleDateFormat("dd.MM.yyyy").format(date);
        return formattedDate;
    }

    public static long getMilisDdateFromString(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        try{
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch(java.text.ParseException e){
            System.out.println(e);
        }
        return -1; //blad

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

                layout.setPadding(10,0,10,0);

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
