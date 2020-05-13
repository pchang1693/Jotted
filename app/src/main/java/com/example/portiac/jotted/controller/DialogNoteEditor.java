package com.example.portiac.jotted.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.portiac.jotted.R;
import com.example.portiac.jotted.model.Note;
import com.example.portiac.jotted.model.NoteType;
import com.example.portiac.jotted.util.JournalDate;

import java.util.Date;

public class DialogNoteEditor extends DialogFragment {
    private Toolbar toolbar;
    private RadioGroup radioGroup;
    private Note preexistingNote;
    private NoteType noteType;

    private boolean inEditingMode;
    private int preexistingNotePosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);

        Bundle args = getArguments();
        if (inEditingMode = (args != null)) {
            makeTempPreexistingNote(args);
            preexistingNotePosition = args.getInt("note_index");
        } else {
            preexistingNote = null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.dialog_new_note, container, false);

        final EditText editTitle = view.findViewById(R.id.editTitle);
        //final EditText editDate = view.findViewById(R.id.editDate);
        final EditText editContent = view.findViewById(R.id.editContent);

        if (preexistingNote != null) {
            editTitle.setText(preexistingNote.getTitle());
            editContent.setText(preexistingNote.getContent());
        }

        radioGroup = view.findViewById(R.id.radioGroup);
        setNoteTypeInEditor();

        final Note newNote;
        if (preexistingNote == null) {
            newNote = new Note("", JournalDate.currentDate(), "", noteType);
        } else {
            newNote = preexistingNote;
        }

        toolbar = (Toolbar) view.findViewById(R.id.toolbarNewNoteDialog);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.inflateMenu(R.menu.dialog_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                MainActivity callingActivity = (MainActivity) getActivity();
                JournalFragment callFragment = (JournalFragment) callingActivity.getSupportFragmentManager().findFragmentByTag("curr");
                switch (menuItem.getItemId()) {
                    case R.id.action_save_new_note:
                        String newTitle, newContent;
                        boolean allInputsValid = true;

                        if ((newTitle = editTitle.getText().toString().trim()).isEmpty()) {
                            editTitle.requestFocus();
                            editTitle.setError("Title cannot be empty");
                            allInputsValid = false;
                        }

                        if ((newContent = editContent.getText().toString().trim()).isEmpty()) {
                            newContent = "";
                            /*
                            editContent.setError("Note cannot be empty");
                            allInputsValid = false;
                            */
                        }

                        if (allInputsValid) {
                            newNote.setTitle(newTitle);
                            //newNote.setDate(JournalDate.currentDate());
                            newNote.setContent(newContent);
                            newNote.setType(noteType);
                            callFragment.addNoteToJournal(newNote);
                            if (inEditingMode) {
                                callFragment.deleteNoteAtPosition(preexistingNotePosition);
                            }
                            dismiss();
                        }
                        return true;

                    default:
                        return true;
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setWindowAnimations(R.style.AppTheme_DialogFloat);
        }
    }

    public void setEntryType(NoteType type) {
        noteType = type;
    }

    private void setNoteTypeInEditor() {
        if (noteType == null) {
            noteType = NoteType.ENTRY;
        }

        switch (noteType) {
            case ENTRY:
                radioGroup.check(R.id.radioButtonEntry);
                break;
            case DREAM:
                radioGroup.check(R.id.radioButtonDream);
                break;
            case SPROUT:
                radioGroup.check(R.id.radioButtonSprout);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.radioButtonEntry:
                        noteType = NoteType.ENTRY;
                        break;
                    case R.id.radioButtonDream:
                        noteType = NoteType.DREAM;
                        break;
                    case R.id.radioButtonSprout:
                        noteType = NoteType.SPROUT;
                        break;
                }
            }
        });
    }

    private void makeTempPreexistingNote(Bundle args) {
        String tempTitle = args.getString("note_title");
        Date tempDate =  JournalDate.dataStringToDate(args.getString("note_date"));
        String tempContent = args.getString("note_content");
        NoteType tempType;
        switch(args.getString("note_type")) {
            case NoteType.entryString:
                tempType = NoteType.ENTRY;
                break;
            case NoteType.dreamString:
                tempType = NoteType.DREAM;
                break;
            case NoteType.sproutString:
                tempType = NoteType.SPROUT;
                break;
            default:
                tempType = NoteType.ENTRY;
        }
        preexistingNote = new Note(tempTitle, tempDate, tempContent, tempType);
        setEntryType(tempType);
    }

}
