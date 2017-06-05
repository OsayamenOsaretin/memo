package com.dera.memoapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dera.memoapp.activity.SendMemoActivity;
import com.dera.memoapp.util.MemoAndReport;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;



public class RecentActivityListAdapter extends RecyclerView.Adapter<RecentActivityListAdapter.MyViewHolder> {


    private List<MemoAndReport> memoList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, sender, message;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            sender = (TextView) view.findViewById(R.id.sender);
            message = (TextView) view.findViewById(R.id.message);
        }
    }


    public RecentActivityListAdapter(List<MemoAndReport> memos_List) {
        this.memoList = memos_List;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.memo_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MemoAndReport memo = memoList.get(position);
        holder.title.setText(memo.getsubject());
        holder.sender.setText(memo.getsender());
        holder.message.setText(memo.getbody());
    }

    @Override
    public int getItemCount() {
        return memoList.size();
    }

}
