package com.example.portiac.jotted.controller;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portiac.jotted.R;
import com.example.portiac.jotted.model.Note;
import com.example.portiac.jotted.model.NoteType;
import com.example.portiac.jotted.util.JournalDate;

import java.util.List;

import static com.example.portiac.jotted.model.NoteType.DREAM;
import static com.example.portiac.jotted.model.NoteType.ENTRY;
import static com.example.portiac.jotted.model.NoteType.SPROUT;

public class HomeFragment extends Fragment {
    private String strWelcome;
    private TextView mTextGreeting;
    private Button mButtonEntry, mButtonDream, mButtonSprout;
    private View mCard;
    private List<Note> notes;
    private Note recentNote;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strWelcome = JournalDate.welcomeString(JournalDate.currentDate());
        notes = ((MainActivity) getActivity()).getNotes();
        if (notes.size() != 0) {
            recentNote = notes.get(notes.size() - 1);
        } else {
            recentNote = new Note("Create a new note!", JournalDate.currentDate(),"Click one of the above buttons.", ENTRY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mTextGreeting = (TextView) view.findViewById(R.id.textGreeting);
        mTextGreeting.setText(strWelcome);

        mButtonEntry = (Button) view.findViewById(R.id.buttonEntry);
        mButtonEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(ENTRY, "new entry");
            }
        });

        mButtonDream = (Button) view.findViewById(R.id.buttonDream);
        mButtonDream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(DREAM, "new dream");
            }
        });

        mButtonSprout = (Button) view.findViewById(R.id.buttonSprout);
        mButtonSprout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNewNote(SPROUT, "new sprout");
            }
        });

        mCard = view.findViewById(R.id.card_home);
        ((TextView) mCard.findViewById(R.id.noteTitle)).setText(recentNote.getTitle());
        ((TextView) mCard.findViewById(R.id.noteDate)).setText(JournalDate.formatDateToString(recentNote.getDate()));
        ((TextView) mCard.findViewById(R.id.noteContentPreview)).setText(recentNote.getContent());

        ImageView noteTypeImageView = (ImageView) mCard.findViewById(R.id.noteTypeImg);
        if (recentNote.getType() != null) {
            switch (recentNote.getType()) {
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

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void makeNewNote(NoteType type, String tag) {
        DialogNoteEditor dialog = new DialogNoteEditor();
        dialog.setEntryType(type);
        dialog.show(getFragmentManager(), tag);
        ((MainActivity) getActivity()).switchToJournalFragment();
    }
}
