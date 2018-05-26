package log.sleeping.android.arcturuspiotrek.sleepinglog.adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import log.sleeping.android.arcturuspiotrek.sleepinglog.R;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
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
        System.out.println("w getcount"+listGraphsData.size());
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

        ArrayList<DataPoint> listDataPoints = new ArrayList<DataPoint>();
        for(Sleep s : listGraphsData.get(position).getListSleeps())
        {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(s.getDateMilis());
            Date d = c.getTime();
            //listDataPoints.add(new DataPoint(d,s.getDurationh()+s.getDurationm()/60));
            listDataPoints.add(new DataPoint(c.get(Calendar.DAY_OF_MONTH),s.getDurationh()+s.getDurationm()/60));
        }

        DataPoint[] arrayDataPoints = listDataPoints.toArray(new DataPoint[listDataPoints.size()]);


        //graph.getViewport().setScalable(true);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>( arrayDataPoints );
        graph.addSeries(series);

        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));
        //graph.getGridLabelRenderer().setNumHorizontalLabels(3);

/*
        // set manual x bounds to have nice steps

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(listGraphsData.get(position).getListSleeps().get(0).getDateMilis());
        Date d = c.getTime();

        graph.getViewport().setMinX(d.getTime());
        c.setTimeInMillis(listGraphsData.get(position).getListSleeps().get(listGraphsData.get(position).getListSleeps().size()-1).getDateMilis());
        d = c.getTime();
        graph.getViewport().setMaxX(d.getTime());
        graph.getViewport().setXAxisBoundsManual(true);

        // as we use dates as labels, the human rounding to nice readable numbers
        // is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
*/
        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);

        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(arrayDataPoints[0].getX());
        graph.getViewport().setMaxY(arrayDataPoints[arrayDataPoints.length-1].getX());


        TextView monthYear = (TextView)view.findViewById(R.id.textViewMonthYear);
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(listGraphsData.get(position).getListSleeps().get(0).getDateMilis());
        monthYear.setText(getStringMonthNameFromInt(c.get(Calendar.MONTH))+" "+ c.get(Calendar.YEAR));

        double sumHours=0, sumMinutes=0, sumAllMinutes =0;
        int averageHours, averageMin;
        TextView average = (TextView)view.findViewById(R.id.textView_average);
        for(Sleep s : listGraphsData.get(position).getListSleeps())
        {
            sumHours = sumHours+s.getDurationh();
            sumMinutes = sumMinutes+s.getDurationm();
        }
        sumAllMinutes = sumHours*60+sumMinutes;

        averageHours = (int)Math.floor(sumAllMinutes/60/listGraphsData.get(position).getListSleeps().size());
        averageMin =  (int) Math.round((sumAllMinutes/listGraphsData.get(position).getListSleeps().size()%60));
        System.out.println("sumHours:"+sumHours+" sumMinutes:"+sumMinutes+" sumAllminutes:"+sumAllMinutes);

        average.setText("Spałeś/aś średnio "+averageHours+"h "+averageMin+"min");

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
