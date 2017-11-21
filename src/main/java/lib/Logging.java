package lib;

import com.google.gson.Gson;
import model.Record;
import model.TxData;
import model.LogInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Logging {
    private static Logging instance;

    private String mLogFileName;
    private FileWriter mFileWriter;
    private BufferedWriter mBufferedWriter;
    private PrintWriter mPrintWriter;
    private String mEventId;
    private String mRaceId;
    private Gson mGson;
    private Api mApi;

    public static synchronized Logging getInstance() {
        if (instance == null) {
            instance = new Logging();
        }
        return instance;
    }

    // Create log folder if not available
    public void createLogFolder() {
        String logPathString = PropertyUtils.getLogPath();
        File logPath = new File(logPathString);
        if (logPath.exists()) {
            System.out.println("Log path: " + logPathString);
        } else {
            System.out.println("Creating Log path : " + logPathString);
            try {
                logPath.mkdir();
            } catch(SecurityException se) {
                System.out.println("Log path creation error");
            }
        }
    }
    // Create log file and empty json array for a session
    public String start(String eventId, String raceId, Long startTime) {
        mLogFileName = PropertyUtils.getLogPath() + PropertyUtils.getLogFileName();
        mGson = new Gson();
        mApi = Api.getInstance();
        mEventId = eventId;
        mRaceId = raceId;
        LogInfo logInfo = new LogInfo(eventId, raceId);
        logInfo.setStartTime(startTime);
        String output = mGson.toJson(logInfo);
        System.out.println("mLogFileName: " + mLogFileName);
        System.out.println("output: " + output);
        try {
            mFileWriter = new FileWriter(mLogFileName, true);
            mBufferedWriter = new BufferedWriter(mFileWriter);
            mPrintWriter = new PrintWriter(mBufferedWriter);
            mBufferedWriter.write("[" + output + ",");
            mPrintWriter.print(output);
        } catch (Exception e) {
            System.out.println("startReader Exception: " + e.getMessage());
        }
        return mLogFileName;
    }
    // Create log file and empty json array for a session
    public void stop(String eventId, String raceId, Long endTime) {
        LogInfo logInfo = new LogInfo(eventId, raceId);
        logInfo.setEndTime(endTime);
        String output = mGson.toJson(logInfo);
        try {
            mBufferedWriter.write(output + "]");
            mBufferedWriter.flush();
            mBufferedWriter.close();
            mPrintWriter.print(output);
            mPrintWriter.flush();
            mPrintWriter.close();
        } catch (Exception e) {
            System.out.println("startReader Exception: " + e.getMessage());
        }
    }
    public void addEntry(Record record) {
        TxData txData = new TxData(mEventId, mRaceId);
        txData.addRecords(record);
        String output = mGson.toJson(txData);
        try {
            mBufferedWriter.write("," + output);
        } catch (Exception e) {
            System.out.println("addEntry write file Exception: " + e.getMessage());
        }
        mApi.sendResult(output);
    }
}
