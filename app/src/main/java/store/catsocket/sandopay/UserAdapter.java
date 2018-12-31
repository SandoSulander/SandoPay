package store.catsocket.sandopay;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/* This Class works as An Adapter, which is an object that acts as a bridge between an AdapterView and the underlying data for that view.
   It provides access to the data items. The Adapter is also responsible for making a View for each item in the data set.
   This adapter extends RecyclerView.Adapter, which is a base class for presenting List data in a RecyclerView, including computing diffs between Lists on a background thread.
   In other words, it acts as a convenience wrapper around AsyncListDiffer that implements Adapter's common default behavior for item access and counting.
   Furthermore, while using a LiveData<List> is an easy way to provide data to the adapter, it isn't required - we can use submitList(List) instead,
   when ever new lists are available. Finally, to get the list comparison to work properly, this class also utilizes DiffUtil.
   It is a utility class that can calculate the difference between two lists and output a list of update operations that converts the first list into the second one.
   It can be used to calculate updates for a RecyclerView Adapter. */

public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {
    private OnItemClickListener userListener; //TODO Credit Account listener?

    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    // Check if the item hardcoded id is the same
    private static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the adapter has to update the item or not -- Execute only if everything is the same.
        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getEmail().equals(newItem.getEmail()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPassword().equals(newItem.getPassword());
        }
    };

    /* Inflate the correct Item LayOut */
    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new UserAdapter.UserHolder(itemView);
    }

    /* Set the correct position of the item and the correct content into the Item */
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        User currentUser = getItem(position);
        holder.textViewName.setText("Name: " + currentUser.getName());
        holder.textViewEmail.setText("E-mail: " + currentUser.getEmail());
    }


    public User getUserAt(int position) {
        return getItem(position);
    }

    /* This class describes an item view and metadata about its place within the RecyclerView. */
    class UserHolder extends RecyclerView.ViewHolder {

        private TextView textViewName;
        private TextView textViewEmail;

        public UserHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewEmail = itemView.findViewById(R.id.text_view_email);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (userListener != null && position != RecyclerView.NO_POSITION) {
                        userListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(OnItemClickListener userListener) {
        this.userListener = userListener;
    }
}
