package com.example.portiac.jotted.controller;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.portiac.jotted.R;
import com.example.portiac.jotted.model.Note;
import com.example.portiac.jotted.persistence.JSONSerializer;
import com.example.portiac.jotted.util.JournalDate;

import java.util.List;

public class JournalFragment extends Fragment {

    private JournalAdapter mJournalAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewJournal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        mJournalAdapter = new JournalAdapter();
        recyclerView.setAdapter(mJournalAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewNote dialog = new DialogNewNote();
                dialog.show(getFragmentManager(), "new note");
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mJournalAdapter.saveNotes();
    }

    public void addNoteToJournal(Note note) {
        mJournalAdapter.addNote(note);
    }

    public class JournalAdapter extends RecyclerView.Adapter {
        private JSONSerializer mSerializer;
        private List<Note> noteList;
        JournalAdapter(List<Note> noteList) {this.noteList = noteList;}

        JournalAdapter() {
            mSerializer = new JSONSerializer("Notes.json", getActivity().getApplicationContext());
//            try {
//                noteList = mSerializer.loadNotes();
//            } catch (Exception e) {
//                noteList = new ArrayList<Note>();
//                Log.e("error", "Error loading notes: ", e);
//            }
            noteList = ((MainActivity) getActivity()).getNotes();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(i, viewGroup, false);
            return new JournalViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
            final Note tempNote = noteList.get(i);
            ((JournalViewHolder) viewHolder).bindData(tempNote);
            ((JournalViewHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), JournalDate.formatDateToJSONString(tempNote.getDate()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "Card clicked", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                    //deleteNote(tempNote);
                }
            });
            ((JournalViewHolder) viewHolder).itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(getContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                    deleteNote(tempNote);
                    return false;
                }
            });
        }

        @Override
        public int getItemViewType(int i) {
            return R.layout.card_note;
        }

        @Override
        public int getItemCount() {
            return noteList.size();
        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        void addNote(Note note) {
            noteList.add(note);
            notifyDataSetChanged();
            //Toast.makeText(getActivity(), (note.getType().getNoteTypeString() + " created!"), Toast.LENGTH_SHORT).show();
        }

        void deleteNote(Note note) {
            noteList.remove(note);
            notifyDataSetChanged();
        }

        void saveNotes() {
            try {
                mSerializer.saveNotes(noteList);
            } catch (Exception e) {
                Log.e("error", "Error saving notes: ", e);
            }
        }

    }

    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView noteTitleTextView, noteDateTextView, noteContentTextView;
        private ImageView noteTypeImageView;

        JournalViewHolder(final View itemView) {
            super(itemView);
            noteTitleTextView = (TextView) itemView.findViewById(R.id.noteTitle);
            noteDateTextView = (TextView) itemView.findViewById(R.id.noteDate);
            noteContentTextView = (TextView) itemView.findViewById(R.id.noteContentPreview);
            noteTypeImageView = (ImageView) itemView.findViewById(R.id.noteTypeImg);
            itemView.setOnClickListener(this);
        }

        void bindData(Note note) {
            noteTitleTextView.setText(note.getTitle());
            noteDateTextView.setText(JournalDate.formatDateToString(note.getDate()));
            noteContentTextView.setText(note.getContent());

            if (note.getType() != null) {
                switch (note.getType()) {
                    case ENTRY:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.entry_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorEntry)));
                        break;
                    case DREAM:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.dream_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorDream)));
                        break;
                    case SPROUT:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.sprout_circle));
                        noteTypeImageView.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorSprout)));
                        break;
                    default:
                        noteTypeImageView.setImageDrawable(getResources().getDrawable(R.drawable.entry_circle));
                        break;
                }
            }
        }

        @Override
        public void onClick(View v) {}
    }

}
