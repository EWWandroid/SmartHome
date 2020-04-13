package com.app.smarthome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smarthome.R;
import com.app.smarthome.retrofit.model.main.ModelSimpleListItem;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;

public class SearchUserAdapter extends SuggestionsAdapter<ModelSimpleListItem, SearchUserAdapter.EmailViewHolder> {

    private Context context;
    private MaterialSearchBar searchBar;

    public SearchUserAdapter(LayoutInflater inflater, Context context, MaterialSearchBar searchBar) {
        super(inflater);
        this.context = context;
        this.searchBar = searchBar;
    }

    @Override
    public void onBindSuggestionHolder(ModelSimpleListItem suggestion, EmailViewHolder holder, int position) {
        holder.tv_email.setText(suggestion.getEmail());
        holder.tv_email.setOnClickListener(v -> {
            searchBar.setText(suggestion.getEmail());
        });
    }

    @Override
    public int getSingleViewHeight() {
        return 55;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.simple_list_item, parent, false);
        return new EmailViewHolder(view);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (ModelSimpleListItem item: suggestions_clone)
                        if(item.getEmail().toLowerCase().contains(term.toLowerCase()))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                suggestions = (ArrayList<ModelSimpleListItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    static class EmailViewHolder extends RecyclerView.ViewHolder {
        TextView tv_email;
        EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_email = itemView.findViewById(R.id.tv_simple_list_view_item);
        }
    }

}
