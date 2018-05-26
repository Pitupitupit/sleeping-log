package log.sleeping.android.arcturuspiotrek.sleepinglog.adapters;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

import log.sleeping.android.arcturuspiotrek.sleepinglog.R;
import log.sleeping.android.arcturuspiotrek.sleepinglog.db.AppDatabase;
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

        //graph.getViewport().setScalable(true);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);


        return view;
    }
}
