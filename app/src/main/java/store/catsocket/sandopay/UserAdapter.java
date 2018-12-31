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

public class UserAdapter extends ListAdapter<User, UserAdapter.UserHolder> {
    private OnItemClickListener userListener; //TODO Credit Account listener?

    public UserAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<User> DIFF_CALLBACK = new DiffUtil.ItemCallback<User>() {
        @Override
        public boolean areItemsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the adapter has to update the item or not
        @Override
        public boolean areContentsTheSame(@NonNull User oldItem, @NonNull User newItem) {
            return oldItem.getEmail().equals(newItem.getEmail()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPassword().equals(newItem.getPassword());
        }
    };

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new UserAdapter.UserHolder(itemView);
    }

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
