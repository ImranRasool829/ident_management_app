package com.ident.main.mngtapp.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class RecyclerViewGenricAdapter<T, VM extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerViewGenricAdapter.RecyclerViewHolder> implements Filterable {

    private ArrayList<T> items, totalItems;
    private int layoutId;
    private RecyclerCallback<VM, T> bindingInterface;


    public RecyclerViewGenricAdapter(ArrayList<T> items, int layoutId, RecyclerCallback<VM, T> bindingInterface) {
        this.items = items;
        this.totalItems = items;
        this.layoutId = layoutId;
        this.bindingInterface = bindingInterface;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    items = totalItems;
                } else {
                    ArrayList<T> filteredList = new ArrayList<T>();
                    for (T row : totalItems) {
//                    for (row in items) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
//                        if (row.name !!.toLowerCase().contains(charString.toLowerCase())){
                        if (bindingInterface.getFilterChar(row, charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    items = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = items;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                items = (ArrayList<T>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        VM binding;

        public RecyclerViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
        }

        public void bindData(T model, final int pos) {
            bindingInterface.bindData(binding, model, pos, itemView);
        }
    }


    @NotNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new RecyclerViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NotNull RecyclerViewGenricAdapter.RecyclerViewHolder holder, final int position) {
        T item = items.get(position);
        holder.itemView.setTag(position);
        holder.bindData(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void updateAdpater(ArrayList<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

}
