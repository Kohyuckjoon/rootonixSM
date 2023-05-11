package com.planuri.rootonixsmartmirror.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AppTrace {

    public final static boolean 		SHOW		= true;


    public final static int LOG_LEVEL_DEBUG			= 0;
    public final static int LOG_LEVEL_INFO			= 1;
    public final static int LOG_LEVEL_WARNING		= 2;
    public final static int LOG_LEVEL_ERROR			= 3;
    public final static int LOG_LEVEL_TIME			= 4;


    private static String m_strTag			= "LPLANURI";


    private static boolean			m_bShowTag			= true;


    private static File m_objFile			= null;
    private static FileOutputStream m_objFileOutStream	= null;

    private static String strDir;

    private static Object m_logLock			= new Object();


    static public void setLogTag(String strTag) {
        m_strTag = strTag;
    }

    static public void showTagName(boolean show) {
        m_bShowTag = show;
    }

    static public String getLogPath(){
        return strDir;
    }

    static public void startFileLog(String serviceName) {
        if ( m_objFile != null) {
            return;
        }

        synchronized (m_logLock) {
            String strMountState = Environment.getExternalStorageState();
            if (strMountState.equals(Environment.MEDIA_MOUNTED) == false) {
                return;
            }

            Calendar objCal  	= Calendar.getInstance();
            strDir 	= Environment.getExternalStorageDirectory() + "/" + serviceName;
            Log.e("LOG_FILE", strDir);
            File objDir 	= new File(strDir);
            if (!objDir.isDirectory()) {
                objDir.mkdir();
            }

            String strFilePath =
                    strDir
                            + "/"
                            + serviceName
                            + "_"
                            + String.format("%02d", objCal.get(Calendar.YEAR))
                            + String.format("%02d", objCal.get(Calendar.MONTH) + 1)
                            + String.format("%02d", objCal.get(Calendar.DATE) )
                            + String.format("%02d", objCal.get(Calendar.HOUR_OF_DAY))
                            + String.format("%02d", objCal.get(Calendar.MINUTE))
                            + String.format("%02d", objCal.get(Calendar.SECOND))
                            + ".log";
            m_objFile 			= new File(strFilePath);
            boolean bExist 		= m_objFile.exists();

            // remove
            if (bExist) {
                m_objFile.delete();
                bExist = false;
            }

            // create
            if (!bExist) {
                try {
                    m_objFile.createNewFile();
                } catch (IOException e) {
                    close();
                    return;
                }
            }

            // log
            Date objToday = new Date();
            SimpleDateFormat objDate	 = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat objTime	 = new SimpleDateFormat("hh:mm:ss");
            String strDate  =
                    "=============================================================================="
                            + "\n"
                            + "Start File Logger"
                            + "\n"
                            + "Sevice Name : " + serviceName
                            + "\n"
                            + "File Path   : " + strFilePath
                            + "\n"
                            + "Time        : " + objDate.format(objToday) + " " + objTime.format(objToday)
                            + "\n"
                            + "=============================================================================="
                            + "\n";

            // write
            try {
                if (m_objFile.canWrite() == false) {
                    return;
                }

                m_objFileOutStream = new FileOutputStream(m_objFile);
                write(strDate);
            } catch (IOException e) {
                close();
            }
        }
    }


    static public void endFileLog() {

        if (m_objFile == null || m_objFileOutStream == null) {
            close();
            return;
        }

        synchronized (m_logLock) {
            Date objToday = new Date();
            SimpleDateFormat objDate	 = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat objTime	 = new SimpleDateFormat("hh:mm:ss");
            String strDate	 =
                    "=============================================================================="
                            + "\n"
                            + "Finish File Logger"
                            + "\n"
                            + "Time        : " + objDate.format(objToday) + " " + objTime.format(objToday)
                            + "\n"
                            + "=============================================================================="
                            + "\n";
            write(strDate);
            close();
        }
    }

    static public void d(Object... args) {
        if (!SHOW) {
            return;
        }

        synchronized (m_logLock) {
            print(LOG_LEVEL_DEBUG, args);
        }
    }

    static public void i(Object...args) {
        if (!SHOW) {
            return;
        }

        synchronized (m_logLock) {
            print(LOG_LEVEL_INFO, args);
        }
    }

    static public void W(Object... args) {
        if (!SHOW) {
            return;
        }

        synchronized (m_logLock) {
            print(LOG_LEVEL_WARNING, args);
        }
    }

    static public void e(String args) {

        synchronized (m_logLock) {
            print(LOG_LEVEL_ERROR, new String[] {args});
        }
    }


    static public void e(Throwable e) {

        synchronized (m_logLock) {
            printException(LOG_LEVEL_ERROR, e);
        }
    }

    static private long _time = 0;
    static public void TIME(String args) {
        if (!SHOW) {
            return;
        }

        long _diff = 0;
        if(_time != 0) {
            _diff = System.currentTimeMillis() - _time;
        }
        _time = System.currentTimeMillis();

        String args2 = "TIME::" + args + " _time=" + Long.toString(_time) + " _diff=" + Long.toString(_diff);
        synchronized (m_logLock) {
            print(LOG_LEVEL_TIME, new Object[]{ args2 });
        }
    }

    static public void TIME(String args, boolean reset) {
        if (!SHOW) {
            return;
        }

        long _diff = 0;
        if(_time != 0 ) {
            _diff = System.currentTimeMillis() - _time;
        }
        _time = System.currentTimeMillis();

        String args2 = "TIME::" + args + " _time=" + Long.toString(_time) + " _diff=" + Long.toString(_diff);
        synchronized (m_logLock) {
            print(LOG_LEVEL_TIME, new Object[]{ args2 });
        }
        if(reset) {
            _time = 0;
        }
    }

    public static void dump(byte[] buf) {
        if(SHOW) {
            String s = "0123456789ABCDEF";
            byte[] digit = s.getBytes();

            StringBuilder strBuild = new StringBuilder();
            for(int i = 0; i < buf.length ; i++) {

                int n = (buf[i] >> 4) & 0x0f;
                strBuild.append((char)digit[n]);
                n = buf[i] & 0x0f;
                strBuild.append((char)digit[n]);

                strBuild.append(' ');

                if( i == buf.length -1) {
                    strBuild.append("\n");
                    Log.d(m_strTag, strBuild.toString());
                }
                else if( i != 0 && (i+1) % 16 == 0) {
                    strBuild.append("\n");
                    Log.d(m_strTag, strBuild.toString());
                    strBuild = new StringBuilder();
                }
            }
        }
    }

    static private void printException(int nLevel, Throwable e) {

        StackTraceElement[] ele = e.getStackTrace();

        Thread t = Thread.currentThread();
        // get thread name
        String strThreadName = "";
        strThreadName = t.getName();

        // tag
        String strTag = m_strTag;


        // get file name and line number
        String strFileName = t.getStackTrace()[4].getFileName();
        int 	nLineNumber = t.getStackTrace()[4].getLineNumber();

        if(strFileName == null) {
            strFileName = "Unknown";
        }
        else {
            if (strFileName.length() > 20) {
                strFileName = strFileName.substring(0, 20);
            }
        }

        int count = ele.length;
        String strLog = "";

        // print head line
        if( nLevel == LOG_LEVEL_ERROR ) {
            strLog = String.format( "%s:[%-20s:%5d] %s: %s",
                    strThreadName, strFileName, nLineNumber, e.getClass().getName(), e.getMessage() );
        }
        else {
            strLog = String.format( "%s:[%-20s:%5d] %s",
                    strThreadName, strFileName, nLineNumber,"== PRINT CALL STACK ==" );
        }

        // Level
        switch(nLevel) {
            case LOG_LEVEL_ERROR :
                Log.e(strTag, strLog);
                break;
            default :
                Log.d(strTag, strLog);
                break;
        }

        if (m_objFileOutStream != null) {
            write(strLog);
        }

        // print stack trace
        for (int i=0; i < count; i++) {
            if( i == 0 &&  nLevel != LOG_LEVEL_ERROR ) {
                // do nothing
                continue;
            }
            else {
                strLog = String.format( "%s:[%-20s:%5d]    at %s %s (%s:%d)",
                        m_strTag,
                        strFileName,
                        nLineNumber,
                        ele[i].getClassName(),
                        ele[i].getMethodName(),
                        ele[i].getFileName(),
                        ele[i].getLineNumber());
            }

            // Level
            switch(nLevel) {
                case LOG_LEVEL_ERROR :
                    Log.e(strTag, strLog);
                    break;
                default :
                    Log.d(strTag, strLog);
                    break;
            }

            if (m_objFileOutStream != null) {
                write(strLog);
            }
        }
    }



    private synchronized static void write(String log) {

//		Log.d("log_write", "------STREAM"+m_objFileOutStream);

        if (m_objFile == null || m_objFileOutStream == null) {
            close();
            return;
        }

        try {
            if (m_objFile.canWrite() == false) {
//				Log.d("log_write", "FILE"+m_objFile);
                return;
            }
//			Log.d("log_write", "STREAM"+m_objFileOutStream);

            synchronized (m_objFileOutStream) {
                m_objFileOutStream.write(log.getBytes());
            }
        } catch (IOException e) {
            close();
        }
    }

    private synchronized static void close() {
        try {
            if (m_objFileOutStream != null) {
                m_objFileOutStream.close();
            }
        } catch (IOException e1) {
            ; // do nothing
        }
        m_objFileOutStream = null;
        m_objFile = null;
    }


    private static void print(int nLevel, Object[] args) {
        Thread t = Thread.currentThread();

        // get thread name
        String strThreadName = "";
        strThreadName = t.getName();

        // get file name and line number
        String strFileName = t.getStackTrace()[4].getFileName();
        int 	nLineNumber = t.getStackTrace()[4].getLineNumber();

        // limit filename length
        if(strFileName == null) {
            strFileName = "Unknown";
        }
        else {
            if (strFileName.length() > 20) {
                strFileName = strFileName.substring(0, 20);
            }
        }

        // format
        String strFormat 	= "" + args[0];
        strFormat = strFormat.replaceAll( "%d", "%s" );
        strFormat = strFormat.replaceAll( "%f", "%s" );
        strFormat = strFormat.replaceAll( "%c", "%s" );
        strFormat = strFormat.replaceAll( "%b", "%s" );
        strFormat = strFormat.replaceAll( "%x", "%s" );
        strFormat = strFormat.replaceAll( "%l", "%s" );

        // argument
        String strArgument = "";
        switch( args.length - 1) {
            case 0 :
                strArgument = strFormat;
                break;
            case 1 :
                strArgument = String.format(strFormat
                        , "" + args[1]);
                break;
            case 2 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]);
                break;
            case 3 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3] );
                break;
            case 4 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]);
                break;
            case 5 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]);
                break;
            case 6 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]
                        , "" + args[6]);
                break;
            case 7 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]
                        , "" + args[6]
                        , "" + args[7]);
                break;
            case 8 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]
                        , "" + args[6]
                        , "" + args[7]
                        , "" + args[8]);
                break;
            case 9 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]
                        , "" + args[6]
                        , "" + args[7]
                        , "" + args[8]
                        , "" + args[9]);
                break;
            case 10 :
                strArgument = String.format(strFormat
                        , "" + args[1]
                        , "" + args[2]
                        , "" + args[3]
                        , "" + args[4]
                        , "" + args[5]
                        , "" + args[6]
                        , "" + args[7]
                        , "" + args[8]
                        , "" + args[9]
                        , "" + args[10]);
                break;
            default :
                ;
        }

        // log
        String strLog = "";
        if (m_bShowTag) {
            strLog = String.format( "%s:[%-20s:%5d] %s\n", strThreadName, strFileName, nLineNumber, strArgument);
        } else {
            strLog = String.format( "[%-20s:%5d] %s\n", strFileName, nLineNumber, strArgument);
        }

        // tag
        String strTag = m_strTag;


        // Level
        switch(nLevel) {
            case LOG_LEVEL_ERROR :
                Log.e(strTag, strLog);
                break;
            case LOG_LEVEL_WARNING :
                Log.w(strTag, strLog);
                break;
            case LOG_LEVEL_INFO :
            case LOG_LEVEL_TIME :
                Log.i(strTag, strLog);
                break;
            case LOG_LEVEL_DEBUG :
            default :
                Log.d(strTag, strLog);
                break;
        }

        if (m_objFileOutStream != null) {
            write(strLog);
        }
    }


    static public void memory(String msg)
    {
		/*
		 Log.e("ETMEM","==========> "+ msg + " <==========");
		 Log.e("ETMEM","TOTAL MEMORY : "+(Runtime.getRuntime().totalMemory() / (1024)) + "KB");
		 Log.e("ETMEM","MAX MEMORY : "+(Runtime.getRuntime().maxMemory() / (1024)) + "KB");
		 Log.e("ETMEM","FREE MEMORY : "+(Runtime.getRuntime().freeMemory() / (1024 )) + "KB");
		 Log.e("ETMEM","ALLOCATION MEMORY : "+((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024)) + "KB");

		 Log.e("ETMEM","GLOBAL MEMORY : "+(Debug.getGlobalAllocSize() / (1024)) + "KB");
		 //Log.e("ETMEM","NATIVE HEAP : "+(Debug.getNativeHeapSize() / (1024)) + "KB");
		 //Log.e("ETMEM","NATIVE HEAP ALLOC : "+(Debug.getNativeHeapAllocatedSize() / (1024)) + "KB");
		 //Log.e("ETMEM","NATIVE HEAP FREE : "+(Debug.getNativeHeapFreeSize() / (1024)) + "KB");

		 Log.e("ETMEM","--------------------------------------------------------------------------");
		 */
    }
}
