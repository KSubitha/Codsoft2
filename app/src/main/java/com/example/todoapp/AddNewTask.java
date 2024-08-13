package com.example.todoapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todoapp.Model.ToDoModel;
import com.example.todoapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.Contract;
public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";

    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private OnTaskAddListener taskAddedListener;

    @NonNull
    public static AddNewTask newInstance() {
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
        db = new DatabaseHandler(getContext());

        // Ensure the parent activity implements the listener
        try {
            taskAddedListener = (OnTaskAddListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement OnTaskAddedListener");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newTaskText = view.findViewById(R.id.newTaskList);
        newTaskSaveButton = view.findViewById(R.id.SaveButton);

        db.openDatabase();

        final Bundle bundle = getArguments();
        boolean isUpdate = bundle != null && bundle.getString("task") != null;

        newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        if (isUpdate) {
            String task = bundle.getString("task", "");
            newTaskText.setText(task);
            if (task.length() > 0) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.Secondary));
            }
        }

        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.WHITE);
                } else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        newTaskSaveButton.setOnClickListener(v -> {
            String text = newTaskText.getText().toString();
            try {
                if (isUpdate) {
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    ToDoModel task = new ToDoModel();
                    task.setTasks(text);
                    task.setStatus(0);
                    db.insertTask(task);
                }
                if (taskAddedListener != null) {
                    taskAddedListener.onTaskAdded();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dismiss();
            }
        });
    }
}
