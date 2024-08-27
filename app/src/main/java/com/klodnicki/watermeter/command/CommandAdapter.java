package com.klodnicki.watermeter.command;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.klodnicki.watermeter.model.Group;
import com.klodnicki.watermeter.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommandAdapter extends RecyclerView.Adapter<CommandAdapter.CommandViewHolder> {

    private List<Command> commands = new ArrayList<>();

    @NonNull
    @Override
    public CommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_command, parent, false);
        return new CommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommandViewHolder holder, int position) {
        Command command = commands.get(position);
        if (command != null) {
            holder.textView.setText(command.getLabel());
        }
    }

    @Override
    public int getItemCount() {
        return commands.size();
    }

    public void setGroups(Map<String, Group> groups) {
        commands.clear();
        for (Group group : groups.values()) {
            if (group.getItems() != null) {
                commands.addAll(group.getItems().values());
            }
        }
        notifyDataSetChanged();
    }

    public static class CommandViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CommandViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
