package workshop.mobile.herocycle.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import workshop.mobile.herocycle.R;
import workshop.mobile.herocycle.model.RvCentre;
import workshop.mobile.herocycle.model.RvHistory;

public class RvCenterAdapter extends RecyclerView.Adapter<RvCenterAdapter.RvCenterHolder> {
    private Context context;
    private final ArrayList<RvCentre> item;

    public RvCenterAdapter(Context context, ArrayList<RvCentre> item) {
        this.context = context;
        this.item = item;
    }

    @NonNull
    @Override
    public RvCenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rv_centre,parent,false);
        return new RvCenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RvCenterHolder holder, int position) {
        RvCentre currentItem = item.get(position);

        holder.Ctrname.setText(currentItem.getCtrName());
        holder.Fulladr.setText(currentItem.getFullAdd());
        holder.Region.setText(currentItem.getRegion());
        holder.State.setText(currentItem.getState());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public static class RvCenterHolder extends RecyclerView.ViewHolder{

        TextView Ctrname, Fulladr, Region, State;

        public RvCenterHolder(@NonNull View itemView) {
            super(itemView);

            Ctrname = itemView.findViewById(R.id.Ctrname);
            Fulladr = itemView.findViewById(R.id.Fulladr);
            Region = itemView.findViewById(R.id.Region);
            State = itemView.findViewById(R.id.State);
        }
    }
}
