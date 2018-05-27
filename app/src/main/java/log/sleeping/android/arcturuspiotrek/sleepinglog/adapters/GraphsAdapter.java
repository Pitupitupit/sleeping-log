package log.sleeping.android.arcturuspiotrek.sleepinglog.adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import log.sleeping.android.arcturuspiotrek.sleepinglog.R;
import log.sleeping.android.arcturuspiotrek.sleepinglog.SleepListActivity;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;
import log.sleeping.android.arcturuspiotrek.sleepinglog.models.GraphData;

public class GraphsAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<GraphData> listGraphsData;
    private Context context;
    final AppDatabase db;
    public User user;

    public GraphsAdapter(Context context, User user, ArrayList<GraphData> listGraphsData) {
        this.context = context;
        this.db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-name").allowMainThreadQueries().build();
        this.user = user;

        System.out.println("konstruktorlistgraphsdata size"+listGraphsData.size());
        this.listGraphsData = listGraphsData;
    }

    @Override
    public int getCount() {
        return listGraphsData.size();
    }

    @Override
    public Object getItem(int i) {
        return listGraphsData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_listview_graphs, null);
        }

        GraphView graph = (GraphView) view.findViewById(R.id.graph_listView);

        int maxH =0;
        ArrayList<DataPoint> listDataPoints = new ArrayList<DataPoint>();
        for(Sleep s : listGraphsData.get(position).getListSleeps())
        {
            //if do znalezienia maxh
            if(s.getDurationh() > maxH)
            {
                maxH = s.getDurationh();
            }
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(s.getDateMilis());

            listDataPoints.add(new DataPoint(c.get(Calendar.DAY_OF_MONTH),s.getDurationh()+s.getDurationm()/60));

        }


        DataPoint[] arrayDataPoints = listDataPoints.toArray(new DataPoint[listDataPoints.size()]);
        System.out.println("LENGHT:"+arrayDataPoints.length);

         LineGraphSeries<DataPoint> series = new LineGraphSeries<>( arrayDataPoints );
        series.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series series, DataPointInterface dataPoint) {
                Toast.makeText(context, "Series1: On Data Point clicked: "+dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        // VVVVVVVVVVVV !!!!!!
        graph.removeAllSeries(); // <<<<<<<<<  !!!!!!
        // ^^^^^^^^^^^^ !!!!!!
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(arrayDataPoints[0].getX());
        graph.getViewport().setMaxX(arrayDataPoints[arrayDataPoints.length-1].getX());
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(maxH+2);
        graph.addSeries(series);
        graph.getViewport().setScalable(true);






        TextView monthYear = (TextView)view.findViewById(R.id.textViewMonthYear);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(listGraphsData.get(position).getListSleeps().get(0).getDateMilis());
        monthYear.setText(getStringMonthNameFromInt(c.get(Calendar.MONTH))+" "+ c.get(Calendar.YEAR));

        double sumHours=0, sumMinutes=0, sumAllMinutes =0;
        int averageHours, averageMin;
        int maxHours =0, maxMinutes =0, minHours = 999, minMinutes = 999;
        TextView average = (TextView)view.findViewById(R.id.textView_average);
        for(Sleep s : listGraphsData.get(position).getListSleeps())
        {
            if(s.getDurationh() > maxHours)
            {
                maxHours = s.getDurationh();
                maxMinutes = s.getDurationm();
            }
            else if(s.getDurationh() == maxHours)
            {
                if(s.getDurationm() > maxMinutes)
                {
                    maxMinutes = s.getDurationm();
                }
            }

            if(s.getDurationh() < minHours)
            {
                minHours = s.getDurationh();
                minMinutes = s.getDurationm();
            }
            else if(s.getDurationh() == maxHours)
            {
                if(s.getDurationm() < minMinutes)
                {
                    minMinutes = s.getDurationm();
                }
            }
            sumHours = sumHours+s.getDurationh();
            sumMinutes = sumMinutes+s.getDurationm();
        }
        sumAllMinutes = sumHours*60+sumMinutes;

        averageHours = (int)Math.floor(sumAllMinutes/60/listGraphsData.get(position).getListSleeps().size());
        averageMin =  (int) Math.round((sumAllMinutes/listGraphsData.get(position).getListSleeps().size()%60));
        System.out.println("sumHours:"+sumHours+" sumMinutes:"+sumMinutes+" sumAllminutes:"+sumAllMinutes);



        //kolor pod kreską wykresu
        Recommendation rec = SleepListActivity.determineUserType(db,user);
        series.setDrawBackground(true);
        System.out.println("rec min:"+rec.getMinHours()+" rec max:"+rec.getMaxHours());
        System.out.println("averageHours+(averageMin/60)"+(averageHours+(averageMin/60.00)));
        String stringToAverageRate = "";
        if(averageHours+(averageMin/60) >= rec.getMinHours() && averageHours+(averageMin/60) <= rec.getMaxHours())
        {
            stringToAverageRate = "Świetnie!";
            series.setBackgroundColor(Color.parseColor("#90ce4e"));
        }
        else if(averageHours+(averageMin/60) < rec.getMinHours())
        {
            stringToAverageRate = "Za krótko śpisz!";
            series.setBackgroundColor(Color.parseColor("#ff6666"));
        }
        else if(averageHours+(averageMin/60) > rec.getMaxHours())
        {
            stringToAverageRate = "Za dużo śpisz!";
            series.setBackgroundColor(Color.parseColor("#ff6666"));
        }

        average.setText("\u2022 Spałeś/aś średnio "+averageHours+"h "+averageMin+"min ("+stringToAverageRate+")\n\u2022 Najdłużej "+maxHours+"h "+maxMinutes+"min\n\u2022 Najkrócej "+minHours+"h "+minMinutes+"min\n");

        return view;
    }


    public static String getStringMonthNameFromInt(int month) {
        if (month == 0)
            return "Styczeń";
        else if (month == 1)
            return "Luty";
        else if (month == 2)
            return "Marzec";
        else if (month == 3)
            return "Kwiecień";
        else if (month == 4)
            return "Maj";
        else if (month == 5)
            return "Czerwiec";
        else if (month == 6)
            return "Lipiec";
        else if (month == 7)
            return "Sierpień";
        else if (month == 8)
            return "Wrzesień";
        else if (month == 9)
            return "Październik";
        else if (month == 10)
            return "Listopad";
        else if (month == 11)
            return "Grudzień";
        else return "Nie ma takiego miesiąca";
    }

}
