package com.example.remindme.Adapters;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.remindme.HttpsTrustManager;
import com.example.remindme.R;
import com.example.remindme.model.Reminder;

import java.util.List;

public class MyRemindersAdapter extends RecyclerView.Adapter<MyRemindersAdapter.MyRemindersHolder> {

    protected static class MyRemindersHolder extends RecyclerView.ViewHolder {

        public final TextView titleView;
        public final TextView descriptionView;
        public final RecyclerView locationView;
        public final Button button;
        public final ImageButton toggle;
        public final LinearLayout topBar;
        public boolean toggled;

        public MyRemindersHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
            locationView = itemView.findViewById(R.id.recycler_location);
            button = itemView.findViewById(R.id.markDone);
            toggle = itemView.findViewById(R.id.reminder_toggle);
            topBar = itemView.findViewById(R.id.topBar);
            toggled = true;
        }
    }

    private  List<Reminder> myReminders;
    private final LayoutInflater inflater;
    private final Context context;

    public MyRemindersAdapter(Context context, List<Reminder> myReminders) {
        this.myReminders = myReminders;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public MyRemindersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.my_reminders_item, parent, false);
        return new MyRemindersHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MyRemindersHolder holder, int position) {
        Reminder current = myReminders.get(holder.getAdapterPosition());
        holder.titleView.setText(current.getTask());
        holder.descriptionView.setText(current.getDescription());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.locationView.setLayoutManager(layoutManager);
        LocationAdapter adapter = new LocationAdapter(context, current.getLocations());
        holder.locationView.setAdapter(adapter);

        holder.locationView.setVisibility(View.GONE);
        holder.topBar.setZ(100);
        holder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.toggled) {
                    holder.toggle.setImageResource(R.drawable.down_arrow);
                    holder.locationView.setVisibility(View.VISIBLE);
                    holder.locationView.animate().translationY(holder.topBar.getY()).setDuration(1000);
                } else {
                    holder.toggle.setImageResource(R.drawable.left_arrow);
                    holder.locationView.animate().translationY(holder.topBar.getY() - holder.locationView.getHeight()).setDuration(1000)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    holder.locationView.setVisibility(View.GONE);
                                }
                            });
                }
                holder.toggled = !holder.toggled;
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpsTrustManager.allowAllSSL();
                String url = "https://192.168.10.245:8443/Server_war_exploded/RemindMe/Reminder/Remove/" + current.getID();
                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest jsonObjectRequest = new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("LOCATION", "remove success");
                        myReminders.remove(current);
                        notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("LOCATION", "remove failed");
                        error.printStackTrace();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                Log.i("LOCATION", "WORKS!!!");
            }
        });
    }

    @Override
    public int getItemCount() {
        return myReminders.size();
    }

    public List<Reminder> getMyReminders() {
        return myReminders;
    }

    public void setMyReminders(List<Reminder> myReminders) {
        this.myReminders = myReminders;
    }
}
