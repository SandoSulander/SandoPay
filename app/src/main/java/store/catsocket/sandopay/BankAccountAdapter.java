package store.catsocket.sandopay;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BankAccountAdapter extends RecyclerView.Adapter<BankAccountAdapter.BankAccountHolder> {
    private List<DebitAccount> debitAccounts = new ArrayList<>();

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
        DebitAccount currentDebitAccount = debitAccounts.get(position);
        holder.textViewAccountNumber.setText("Account number: "+ currentDebitAccount.getAccountNumber());
        holder.textViewBlance.setText("Balance: "+ String.valueOf(currentDebitAccount.getBalance())+"€");
    }

    @Override
    public int getItemCount() {
        return debitAccounts.size();
    }

    public void setDebitAccounts(List<DebitAccount> debitAccounts){
        this.debitAccounts = debitAccounts;
        notifyDataSetChanged();
    }

    class BankAccountHolder extends RecyclerView.ViewHolder{

        private TextView textViewAccountNumber;
        private TextView textViewBlance;
        private TextView textViewCredit; //TODO - When CreditAccount created

        public BankAccountHolder(View itemView){
            super(itemView);
            textViewAccountNumber = itemView.findViewById(R.id.text_view_account_number);
            textViewBlance = itemView.findViewById(R.id.text_view_balance);
        }
    }
}
