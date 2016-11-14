package com.rba.remoteconfigdemo.view.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rba.remoteconfigdemo.R;
import com.rba.remoteconfigdemo.model.entity.OptionEntity;

import java.util.List;

/**
 * Created by Ricardo Bravo on 14/11/16.
 */


public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private List<OptionEntity> optionEntityList;
    private static LayoutInflater inflater = null;


    public OptionAdapter(Context context, List<OptionEntity> optionEntityList) {
        if(context!=null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.optionEntityList = optionEntityList;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        OptionEntity optionEntity = optionEntityList.get(position);
        holder.txtDescription.setText(optionEntity.getDescription());

    }

    @Override
    public int getItemCount() {
        return optionEntityList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final AppCompatTextView txtDescription;

        public ViewHolder(View view) {
            super(view);
            txtDescription = (AppCompatTextView) view.findViewById(R.id.txtDescription);
        }

    }
}