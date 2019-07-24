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

    private static final String DirectoryName = Environment.getExternalStorageDirectory() + File.separator + DatabaseHelper.PROJECT_NAME;

    public static final String accessingFile(Context mContext, String tagID, String fName, String type, String autoCode, boolean increment) {
        try {

            File idFile = creatingFile(mContext, fName);

            String fileName = DirectoryName
                    + File.separator
                    + fName;
            String line = "";
            StringBuffer lineBuffer = new StringBuffer();
            BufferedReader reader = new BufferedReader(new FileReader(fileName));

            while ((line = reader.readLine()) == null) {
                lineBuffer.append("CASE");
                lineBuffer.append(tagID + "-000000");
                lineBuffer.append(tagID + "-000000");
                lineBuffer.append("CONTROL");
                lineBuffer.append(tagID + "-000000");
                lineBuffer.append(tagID + "-000000");

                writeInFile(idFile, lineBuffer.toString().getBytes().toString());
            }

            if (increment)
                return incrementInFile(idFile, line, tagID, getLineInFile(type), autoCode);

            return line;

        } catch (IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private static final File creatingFile(Context mContext, String fName) {

        File folder = new File(DirectoryName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {

            return new File(folder, fName);

        } else {
            Toast.makeText(mContext, "Can't create folder!!", Toast.LENGTH_SHORT).show();
        }

        return new File(DirectoryName + File.separator + fName);
    }

    private static int getLineInFile(String type) {
        switch (type) {
            case "cas":
                return 1;
            case "cae":
                return 2;
            case "cls":
                return 4;
            case "cle":
                return 5;
            default:
                return 0;
        }
    }

    private static final void writeInFile(File file, String line) throws IOException {
        FileWriter writer = new FileWriter(file);

        writer.write(line);

        writer.flush();
        writer.close();
    }

    private static final String incrementInFile(File idFile, String fileLine, String tagID, int type, String autoCode) throws IOException {
        String[] idLength = fileLine.split("-");
        int lastCont = Integer.valueOf(idLength[idLength.length - 1]) + 1;
        String subStr = fileLine.substring(0, fileLine.length() - idLength[idLength.length - 1].length());
        writeInFile(idFile, subStr + lastCont);


        return subStr + lastCont;
    }


}
