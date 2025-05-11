package com.example.phoneserver;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;


public class MyHTTPServer extends NanoHTTPD {
    private DBHelper dbHelper;
    private Context context;

    public MyHTTPServer(int port, Context context) throws IOException {
        super("0.0.0.0", port);
        this.dbHelper = new DBHelper(context);
        this.context = context;
        start(SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getUri().equals("/users")) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT name FROM users", null);
            JSONArray users = new JSONArray();

            while (cursor.moveToNext()) {
                users.put(cursor.getString(0));
            }
            cursor.close();
            return newFixedLengthResponse(Response.Status.OK, "application/json", users.toString());
        }

        if (Method.POST.equals(session.getMethod()) && "/upload".equals(session.getUri())) {
            try {
                // Parse multipart data
                Map<String, String> files = new HashMap<>();
                session.parseBody(files);

                String tmpFilePath = files.get("audio"); // "audio" is the form field name
                File uploadedFile = new File(tmpFilePath);

                // Optionally move to a known location with a generated name
                File storedFile = new File(context.getFilesDir(), "audio_" + System.currentTimeMillis() + ".wav");
                uploadedFile.renameTo(storedFile);

                return newFixedLengthResponse(Response.Status.OK, MIME_PLAINTEXT, "Upload successful");
            } catch (Exception e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, MIME_PLAINTEXT, "Error: " + e.getMessage());
            }
        }

        return newFixedLengthResponse("No static resource");
    }
}