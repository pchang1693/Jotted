package com.example.portiac.jotted.persistence;

import com.example.portiac.jotted.model.Note;
import com.example.portiac.jotted.model.NoteType;
import com.example.portiac.jotted.util.JournalDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;

public class JSONConverter {

    private static final String JSON_TITLE = "title";
    private static final String JSON_DATE = "date";
    private static final String JSON_CONTENT = "content";
    private static final String JSON_TYPE = "type";

    static JSONObject noteToJSON(Note note) throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JSON_TITLE, note.getTitle());
        jo.put(JSON_DATE, JournalDate.formatDateToJSONString(note.getDate()));
        jo.put(JSON_CONTENT, note.getContent());

        switch (note.getType()) {
            case ENTRY:
                jo.put(JSON_TYPE, NoteType.entryString);
                break;
            case DREAM:
                jo.put(JSON_TYPE, NoteType.dreamString);
                break;
            case SPROUT:
                jo.put(JSON_TYPE, NoteType.sproutString);
                break;
            default:
                jo.put(JSON_TYPE, NoteType.entryString);
                break;
        }

        return jo;
    }

    static Note JSONtoNote(JSONObject jo) throws JSONException {
        String title = jo.getString(JSON_TITLE);
        String content = jo.getString(JSON_CONTENT);

        String str_date = jo.getString(JSON_DATE);
        Date date;
        try {
            date = JournalDate.JSONStringToDate(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new JSONException("Parsing date JSON string failed.");
        }

        NoteType type;
        switch (jo.getString(JSON_TYPE)) {
            case NoteType.entryString:
                type = NoteType.ENTRY;
                break;
            case NoteType.dreamString:
                type = NoteType.DREAM;
                break;
            case NoteType.sproutString:
                type = NoteType.SPROUT;
                break;
            default:
                type = NoteType.ENTRY;
        }

        return new Note(title, date, content, type);
    }

}