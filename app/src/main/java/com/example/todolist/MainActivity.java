package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding  binding;
    private  NoteViewModel noteViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        noteViewModel= new ViewModelProvider(this,ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                .get(NoteViewModel.class);




        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(MainActivity.this,DataInsertActivity.class);
                intent.putExtra("type","addMode");
                startActivityForResult(intent,1);
            }
        });

// set adpater in main activity
        binding.rv.setLayoutManager(new LinearLayoutManager(this));
        binding.rv.setHasFixedSize(true);

        RvAdpater adpater = new RvAdpater();

        binding.rv.setAdapter(adpater);
//        binding.rv.setAdapter(new RvAdpater());


        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adpater.submitList(notes);
            }
        });

    new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if (direction==ItemTouchHelper.RIGHT){
                noteViewModel.delete(adpater.getNote(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "note deleted ", Toast.LENGTH_SHORT).show();
            }else {

//                Toast.makeText(MainActivity.this, "Note Updated ", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(MainActivity.this,DataInsertActivity.class);

                intent.putExtra("type","update");
                intent.putExtra("title" , adpater.getNote(viewHolder.getAdapterPosition()).getTitle());
                intent.putExtra("disp" , adpater.getNote(viewHolder.getAdapterPosition()).getDisp());
                intent.putExtra("id" , adpater.getNote(viewHolder.getAdapterPosition()).getId());
                startActivityForResult(intent,2);
            }

        }
    }).attachToRecyclerView(binding.rv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1){
            String tittle = data.getStringExtra("tittle");
            String disp=data.getStringExtra("disp");


            Note note = new Note(tittle,disp);
            noteViewModel.insert(note);

            Toast.makeText(this, "note added", Toast.LENGTH_SHORT).show();

        } else if (requestCode==2) {
            String tittle = data.getStringExtra("tittle");
            String disp=data.getStringExtra("disp");
            Note note = new Note(tittle,disp);
             note.setId(data.getIntExtra("id",0));
            noteViewModel.update(note);
            Toast.makeText(this, "note updated", Toast.LENGTH_SHORT).show();


        }
    }


    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}