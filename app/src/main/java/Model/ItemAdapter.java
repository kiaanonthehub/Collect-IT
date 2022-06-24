package Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kiaan.collect_it.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {

    ArrayList<ItemViewModel> mList;
    Context context;

    public ItemAdapter(Context context, ArrayList<ItemViewModel> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ItemViewModel model = mList.get(position);
        holder.name.setText(model.getName());
        holder.desc.setText(model.getDescription());
        holder.date.setText(model.getDate_of_acquisition());
        //Picasso.with(context).load(model.getUri()).into(holder.uri);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, desc, date;
        ImageView uri;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.item_name_text);
            desc = itemView.findViewById(R.id.item_desc_text);
            date = itemView.findViewById(R.id.item_date_text);
            uri = itemView.findViewById(R.id.imageViewItem);

        }
    }
}
