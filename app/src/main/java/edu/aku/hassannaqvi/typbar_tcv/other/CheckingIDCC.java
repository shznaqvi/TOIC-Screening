package edu.aku.hassannaqvi.typbar_tcv.other;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import edu.aku.hassannaqvi.typbar_tcv.core.DatabaseHelper;

public final class CheckingIDCC {

    public static final boolean creatingFile(Context mContext, String fName) {

        String DirectoryName = Environment.getExternalStorageDirectory() + File.separator + DatabaseHelper.PROJECT_NAME;

        File folder = new File(DirectoryName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {

            File idFile = new File(folder, fName);

            if (!idFile.exists()) {
                try {
                    writeInFile(idFile, "CASE\n\n\nCONTROL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return true;

        } else {
            Toast.makeText(mContext, "Can't create folder!!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    public static final String accessingFile(String tagID, String fName, String type, boolean increment) {
        try {
            String fileName = Environment.getExternalStorageDirectory()
                    + File.separator
                    + DatabaseHelper.PROJECT_NAME
                    + File.separator
                    + fName;
            String line = "";
            StringBuffer lineBuffer = new StringBuffer();
            tagID = tagID != null ? tagID : "";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            File idFile = new File(fileName);

            while ((line = reader.readLine()) != null) {
                lineBuffer.append(line)
            }

            for (int i = 1; i < 7; i++) {
                if (i == getLineInFile(type)) {
                    if (reader.readLine() == null) {
                        lineBuffer.append(tagID + "-1");
                        writeInFile(idFile, lineBuffer);
                        break;
                    }
                } else
                    lineBuffer.append(reader.readLine());


            }


            if (increment)
                return incrementInFile(idFile, line);

            return line;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private static int getLineInFile(String type) {
        switch (type) {
            case "cas":
                return 2;
            case "cae":
                return 3;
            case "cls":
                return 5;
            case "cle":
                return 6;
            default:
                return 0;
        }
    }

    private static final void writeInFile(File file, StringBuffer id) throws IOException {
        FileWriter writer = new FileWriter(file);

        writer.write(id.toString().getBytes().toString());

        writer.flush();
        writer.close();
    }

    private static final String incrementInFile(File idFile, String fileID) throws IOException {
        String[] idLength = fileID.split("-");
        int lastCont = Integer.valueOf(idLength[idLength.length - 1]) + 1;
        String subStr = fileID.substring(0, fileID.length() - idLength[idLength.length - 1].length());

        writeInFile(idFile, subStr + lastCont);

        return subStr + lastCont;
    }


}
