package store.catsocket.sandopay;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class BankAccountAdapter extends ListAdapter<DebitAccount, BankAccountAdapter.BankAccountHolder> {
    private OnItemClickListener debitAccountListener; //TODO Credit Account listener?

    public BankAccountAdapter() {
        super(DIFF_CALLBACK);
    }

    public static final DiffUtil.ItemCallback<DebitAccount> DIFF_CALLBACK = new DiffUtil.ItemCallback<DebitAccount>() {
        @Override
        public boolean areItemsTheSame(@NonNull DebitAccount oldItem, @NonNull DebitAccount newItem) {
            return oldItem.getId() == newItem.getId();
        }

        // Check if the adapter has to update the item or not
        @Override
        public boolean areContentsTheSame(@NonNull DebitAccount oldItem, @NonNull DebitAccount newItem) {
            return oldItem.getAccountNumber().equals(newItem.getAccountNumber()) &&
                    oldItem.getBalance() == newItem.getBalance();
        }
    };

    @NonNull
    @Override
    public BankAccountHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.account_item, parent, false);
        return new BankAccountHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BankAccountHolder holder, int position) {
        DebitAccount currentDebitAccount = getItem(position);
        holder.textViewAccountNumber.setText("Account number: " + currentDebitAccount.getAccountNumber());
        holder.textViewBlance.setText("Balance: " + String.valueOf(currentDebitAccount.getBalance()) + "€");
    }


    public DebitAccount getDebitAccountAt(int position) {
        return getItem(position);
    }


    class BankAccountHolder extends RecyclerView.ViewHolder {

        private TextView textViewAccountNumber;
        private TextView textViewBlance;
        private TextView textViewCredit; //TODO - When CreditAccount created

        public BankAccountHolder(View itemView) {
            super(itemView);
            textViewAccountNumber = itemView.findViewById(R.id.text_view_account_number);
            textViewBlance = itemView.findViewById(R.id.text_view_balance);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (debitAccountListener != null && position != RecyclerView.NO_POSITION) {
                        debitAccountListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DebitAccount debitAccount);
    }

    public void setOnItemClickListener(OnItemClickListener debitAccountListener) {
        this.debitAccountListener = debitAccountListener;
    }

}
