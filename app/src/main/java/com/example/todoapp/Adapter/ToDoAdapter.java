package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddNewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
        private List<ToDoModel> todoList;

        private final MainActivity activity;
        private DatabaseHandler db;

        public ToDoAdapter(DatabaseHandler db,MainActivity activity)
        {
            this.db = db;
            this.activity=activity;
        }

        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
            return new ViewHolder(itemView);
        }

        public void onBindViewHolder(ViewHolder holder,int position)
        {
            db.openDatabase();
            ToDoModel item = todoList.get(position);
            holder.task.setText(item.getTasks());

            holder.task.setChecked(toBoolean(item.getStatus()));
            holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        db.updateStatus(item.getId(),1);
                    }
                    else{
                        db.updateStatus(item.getId(),0);
                    }
                }
            });
        }
        private boolean toBoolean(int n) {
            return n!=0;
        }
        public int getItemCount() {
            return todoList.size();
        }
        public Context getContext() {
             return activity;
        }


        public void setTasks(List<ToDoModel> todoList)
        {
            this.todoList = todoList;
            notifyDataSetChanged();
        }
        public void editItem(int position) {
            ToDoModel item=todoList.get(position);
            Bundle bundle = new Bundle();
            bundle.putInt("id", item.getId());
            bundle.putString("task", item.getTasks());
            AddNewTask fragment = new AddNewTask();
            fragment.setArguments(bundle);
            fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        }
        public void deleteItem(int position){

            ToDoModel item = todoList.get(position);
            db.deleteTask(item.getId());
            todoList.remove(position);
            notifyItemRemoved(position);
        }
        public static class ViewHolder extends RecyclerView.ViewHolder
        {
            CheckBox task;
            ViewHolder(View view)
            {
                super(view);
                task = view.findViewById(R.id.tasksCheckBox);
            }
        }
}
