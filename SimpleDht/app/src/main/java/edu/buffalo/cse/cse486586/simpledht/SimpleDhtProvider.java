package edu.buffalo.cse.cse486586.simpledht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class SimpleDhtProvider extends ContentProvider {
    Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
    String[] remoteports= new String[]{"11108","11112","11116","11120","11124"};
    public String ring_start="11108";
    public int s = 0;
    public int rr = 0;
    String[] proj={null,null};
    String pred="";
    String succ="";
    boolean sendports=false;
    String sid="11120";
    String lid="11124";
    ArrayList<String> remports =new ArrayList<String>();
    Hashtable<String, String> hash_table =
            new Hashtable<String, String>();
    String qp="qp";
    boolean nowquery=false;
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }



    static final int SERVER_PORT = 10000;
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr))*2);
        sqlitedb mydb1 = new sqlitedb(getContext());
        String s = mydb1.getTableAsString(mydb1,"SimpleDht");
        Log.v("database", s);
        Log.v("insideelse", selection);
        //int rowa = mydb1.delete("SimpleDht",selection,null);
        mydb1.getReadableDatabase().execSQL("delete from SimpleDht where key" + "= ?", new String[]{selection});
        //int rowa = mydb1.delete("SimpleDht", "key = ?", new String[]{"*"});
     //   Log.v("insideelserowa", String.valueOf(rowa));
        return 0;

  /*      if(selection.equals("*")) {
        //    for (int i = 0; i <= remports.size(); i++) {
        //        String remotePort = remports.get(i);
        //        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, remotePort, "0","0","star","deleteall","0","0");
                sqlitedb mydb1 = new sqlitedb(getContext());
                String s = mydb1.getTableAsString(mydb1, "SimpleDht");
                Log.v("query", selection);
                Log.v("curdel", "deleted");
        //    }
        }
        else if(selection.equals("@")){
            sqlitedb mydb1 = new sqlitedb(getContext());
            String s = mydb1.getTableAsString(mydb1,"SimpleDht");
            Log.v("query", selection);
            Log.v("cursor", "entered@");
            int rows = mydb1.delete("SimpleDht", "key = ?", new String[]{selection});
        }
        else{


        }*/
        // TODO Auto-generated method stub
      //  return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public boolean onCreate() {
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr))*2);
        final String myNode = String.valueOf((Integer.parseInt(portStr)));
        Log.v("onc", myPort);
        try {
            String[] remoteports= new String[]{"11108","11112","11116","11120","11124"};
            Log.v("rm0", genHash("5554"));
            Log.v("rm1", genHash("5556"));
            Log.v("rm2", genHash("5558"));
            Log.v("rm3", genHash("5560"));
            Log.v("rm4", genHash("5562"));
            Log.v("comp", String.valueOf(genHash(remoteports[4]).compareTo(genHash(remoteports[1]))));
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, "nogenhash");
            e.printStackTrace();
        }
        try{
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);

        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Can't create a ServerSocket");
        }
        try {
            String node_hash = genHash(myPort);
            Log.v("hellohash", node_hash);
            if (myPort.compareTo("11108")==0){
                Log.v("insideif", "11108");
                pred=myPort;
                succ=myPort;
                remports.add("11108");
            }
            else{
                Log.v("insideelse", myPort);
                Thread.sleep(100);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,myPort,ring_start,"0","0","0","0","0","qp");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // TODO Auto-generated method stub
        return false;
    }

 /*   public Cursor sqlquery(Uri uri, String[] projection, String selection, String[] selectionArgs,
                           String sortOrder){

    }*/
 @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                           String sortOrder){
     Log.v("sidfinal", sid);
     Log.v("lidfinal", lid);
     Log.v("remportsfinal", String.valueOf(remports));
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr))*2);
     if((succ==""&&pred==""&&myPort.compareTo("11108")!=0&&selection.equals("*"))||(succ.compareTo("11108")==0&&pred.compareTo("11108")==0&&myPort.compareTo("11108")==0&&selection.equals("*"))){
         sqlitedb mydb1 = new sqlitedb(getContext());
         String s = mydb1.getTableAsString(mydb1,"SimpleDht");
         Log.v("query", selection);
         Log.v("cursor", "entered@");
         Cursor  cursor2 =  mydb1.getReadableDatabase().rawQuery("select * from SimpleDht",null);
         return cursor2;
     }

        else if (selection.equals("*")){
            sqlitedb mydb1 = new sqlitedb(getContext());
            String s = mydb1.getTableAsString(mydb1,"SimpleDht");
            Log.v("query", selection);
            Log.v("cursor", "enteredquerystar");
            String[] cn = {
                    "key", "value"
            };
            MatrixCursor cursor = new MatrixCursor(cn);
            Log.v("remportssize", String.valueOf(remports.size()));
            for (int i = 0; i < remports.size(); i++) {
                String remotePort = remports.get(i);
                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, remotePort, "0","0","star","queryall","0","0");
                while (nowquery == false) {
                    Log.v("waitinghash", String.valueOf(proj));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //  break;

                }
                nowquery = false;
                Set<String> keys = hash_table.keySet();
                for (String key : keys) {
                    Log.v("aenteredquerynow", String.valueOf(hash_table.get(key)));
                    String hval = String.valueOf(hash_table.get(key));

                    String[] row = {
                            key, hval
                    };
                    cursor.addRow(row);
                }
                hash_table.clear();
            }
            return cursor;
        }
        if(selection.equals("@")){
            sqlitedb mydb1 = new sqlitedb(getContext());
            String s = mydb1.getTableAsString(mydb1,"SimpleDht");
            Log.v("query", selection);
            Log.v("cursor", "entered@");
            Cursor  cursor2 =  mydb1.getReadableDatabase().rawQuery("select * from SimpleDht",null);
            return cursor2;
            }
            else{
                if (sortOrder==null){
                    sortOrder=myPort;
                }
         return sqlquery(uri,projection,selection,null,sortOrder,null);
     }

    }
    public Cursor rawsqlquery(Uri uri, String[] projection, String selection, String[] selectionArgs,
                       String sortOrder){
        sqlitedb mydb1 = new sqlitedb(getContext());
        String s = mydb1.getTableAsString(mydb1, "SimpleDht");
        Log.v("query", selection);
        String[] x = {selection};
        Cursor cursor4 = mydb1.getReadableDatabase().rawQuery("select * from SimpleDht where key" + "= ?", x);
        Log.v("cursor", String.valueOf(cursor4));
        return cursor4;
    }

    public Cursor sqlquery(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder,Cursor cur) {
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr))*2);
        if(succ==""&&pred==""&&myPort.compareTo("11108")!=0) {
            return rawsqlquery(uri,null,selection,null,null);

        }
        else if(succ.compareTo("11108")==0&&pred.compareTo("11108")==0&&myPort.compareTo("11108")==0){
            return rawsqlquery(uri,null,selection,null,null);
        }
        else if(selection.compareTo("@")==0||selection.compareTo("@")==0){
            Log.v("allandwhole", selection);
            return rawsqlquery(uri,null,selection,null,null);
        }
        else if(selection.compareTo("*")==0||selection.compareTo("*")==0){
            Log.v("allandwhole", selection);
            return rawsqlquery(uri,null,selection,null,null);
        }
        /*if(sortOrder!=null){
            if(sortOrder.compareTo("Querynow")==0) {
                Log.v("aenteredquerynow", String.valueOf(projection));
                String[] cn = {
                        "key", "value"
                };
                MatrixCursor cursor = new MatrixCursor(cn);
                String[] row = {
                        projection[0], projection[1]
                };
                cursor.addRow(row);
                return cursor;
            }
        }*/
        else{
            String key11 = selection;
            Log.v("insertvalueeenteredelse", key11);
            try {
                String keyhash = genHash(key11);
                String predhash= genHash(String.valueOf(Integer.parseInt(pred) / 2));
                String succhash=genHash(String.valueOf(Integer.parseInt(succ) / 2));
                String myPorthash=genHash(String.valueOf(Integer.parseInt(myPort) / 2));
                int keycomppred=keyhash.compareTo(predhash);
                int keycompmyport=keyhash.compareTo(myPorthash);
                int myportcomppred=myPorthash.compareTo(predhash);
                if(keycomppred>0 && keycompmyport<=0 && myportcomppred>0){
                    Cursor curret = rawsqlquery(uri,null,key11,null,null);
                    Log.v("rawsqlfoundcursor", String.valueOf(curret));
                    Log.v("rawsqlfound",String.valueOf(curret));
                    if( curret != null && curret.moveToFirst() ){
                        String va = curret.getString(curret.getColumnIndex("value"));

                    boolean contains = Arrays.asList(remoteports).contains(sortOrder);
                    if(contains){
                        Log.v("curretcolumn",va);
                        String ur= String.valueOf(uri);
                        qp=sortOrder;
                        new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, qp, va, "0", key11, "query", ur, qp);////might cause error 2 argument changed.
                    }
                        curret.close();
                        if(sortOrder.compareTo(myPort)!=0){
                            return curret;
                        }
                    }
                    else {
                        return curret;
                    }
                }
                else if (myPorthash.equals(genHash(String.valueOf(Integer.parseInt(sid) / 2))) && (keyhash.compareTo(genHash(String.valueOf(Integer.parseInt(lid) / 2))) > 0)) {
                    Cursor curret = rawsqlquery(uri,null,key11,null,null);
                    Log.v("rawsqlfoundcursor", String.valueOf(curret));
                    Log.v("rawsqlfound",String.valueOf(curret));
                    if( curret != null && curret.moveToFirst() ){
                        String va = curret.getString(curret.getColumnIndex("value"));

                        boolean contains = Arrays.asList(remoteports).contains(sortOrder);
                        if(contains){
                            Log.v("curretcolumn",va);
                            String ur= String.valueOf(uri);
                            qp=sortOrder;
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, qp, va, "0", key11, "query", ur, qp);////might cause error 2 argument changed.
                        }
                        curret.close();
                        if(sortOrder.compareTo(myPort)!=0){
                            return curret;
                        }
                    }
                    else {
                        return curret;
                    }
                }
                else if (keycompmyport > 0) {
                    String ur= String.valueOf(uri);
                    Log.v("passing",succ);
                    qp=sortOrder;
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, succ, "0","0",key11,"query",ur,qp);
                    if(sortOrder.compareTo(myPort)!=0){
                        return null;
                    }
                }
                else if (myPorthash.equals(genHash(String.valueOf(Integer.parseInt(sid) / 2))) && (keycompmyport <= 0)) {
                    Cursor curret = rawsqlquery(uri,null,key11,null,null);
                    Log.v("rawsqlfoundcursor", String.valueOf(curret));
                    Log.v("rawsqlfound",String.valueOf(curret));
                    if( curret != null && curret.moveToFirst() ){
                        String va = curret.getString(curret.getColumnIndex("value"));

                        boolean contains = Arrays.asList(remoteports).contains(sortOrder);
                        if(contains){
                            Log.v("curretcolumn",va);
                            String ur= String.valueOf(uri);
                            qp=sortOrder;
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, qp, va, "0", key11, "query", ur, qp);////might cause error 2 argument changed.
                        }
                        curret.close();
                        if(sortOrder.compareTo(myPort)!=0){
                            return curret;
                        }
                    }
                    else {
                        return curret;
                    }

                } else if ((keycompmyport < 0) && (keycomppred <= 0)) {
                    String ur= String.valueOf(uri);
                    Log.v("passing",succ);
                    qp=sortOrder;
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, pred, "0","0",key11,"query",ur,qp);
                    if(sortOrder.compareTo(myPort)!=0){
                        return null;
                    }
                }


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


        }
        while(nowquery==false && proj!=null){
            Log.v("waiting", String.valueOf(proj));
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
          //  break;

        }
        nowquery=false;
        Log.v("aenteredquerynow", String.valueOf(proj));
        String[] cn = {
                "key", "value"
        };
        MatrixCursor cursor = new MatrixCursor(cn);
        String[] row = {
                proj[0], proj[1]
        };
        cursor.addRow(row);
        Arrays.fill(proj, null);
        return cursor;
        // TODO Auto-generated method stub
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    
    public Uri sqlinsert(Uri uri, ContentValues values){
        sqlitedb mydb = new sqlitedb(getContext());
        mydb.addrow(values);
        return uri;
    }
    
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v("sidfinal", sid);
        Log.v("lidfinal", lid);
        Log.v("justtestingsucc",succ);
        Log.v("justtestingpred",pred);
        Log.v("remportsfinal", String.valueOf(remports));
        TelephonyManager tel = (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        final String myPort = String.valueOf((Integer.parseInt(portStr))*2);
        if(sendports==false&&myPort.compareTo("11108")==0){
            for (int i = 0; i < remports.size(); i++) {
                if(remports.get(i).compareTo("11108")!=0) {
                    Log.v("remportsfinalist", String.valueOf(remports.get(i)));
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, remports.get(i), "0", "0", remports.toString(), "receiveports", sid, lid);
                }
            }
            sendports=true;
        }
        if(succ==""&&pred==""&&myPort.compareTo("11108")!=0) {
            Log.v("checksucc", "succnnn");
            sqlitedb mydb = new sqlitedb(getContext());
            mydb.addrow(values);
            Log.v("iniquer", "qury added");
            Log.v("iniinser", values.getAsString("key"));
            Log.v("insert", "hello");
        }
        else if(succ.compareTo("11108")==0&&pred.compareTo("11108")==0&&myPort.compareTo("11108")==0){
            sqlitedb mydb = new sqlitedb(getContext());
            mydb.addrow(values);
            Log.v("iniquerypassed11108", "qury added");
            Log.v("iniinsertkeyyy11108", values.getAsString("key"));
            Log.v("insert", "hello");

        }
        else{
            Log.v("insertkeyyy", values.getAsString("key"));
            Log.v("insertvaluee", values.getAsString("value"));
            String key11 = values.getAsString("key");
            String value11 = values.getAsString("value");
            try {
                String keyhash = genHash(key11);
                String predhash= genHash(String.valueOf(Integer.parseInt(pred) / 2));
                String succhash=genHash(String.valueOf(Integer.parseInt(succ) / 2));
                String myPorthash=genHash(String.valueOf(Integer.parseInt(myPort) / 2));
                int keycomppred=keyhash.compareTo(predhash);
                int keycompmyport=keyhash.compareTo(myPorthash);
                int myportcomppred=myPorthash.compareTo(predhash);
                if(keycomppred>0 && keycompmyport<=0 && myportcomppred>0){
             //       ContentResolver cr3 = getContext().getContentResolver();
             /*       new ContentValues();
                    ContentValues cv3;
                    cv3 = new ContentValues();
                    cv3.put("key", key11);
                    cv3.put("value", value11);*/
                    sqlinsert(uri,values);
                }
                else if (myPorthash.equals(genHash(String.valueOf(Integer.parseInt(sid) / 2))) && (keyhash.compareTo(genHash(String.valueOf(Integer.parseInt(lid) / 2))) > 0)) {
               /*     ContentResolver cr1 = getContext().getContentResolver();
                    new ContentValues();
                    ContentValues cv1;
                    cv1 = new ContentValues();
                    cv1.put("key", key11);
                    cv1.put("value", value11);*/
                    sqlinsert(uri, values);
                }
                else if (keycompmyport > 0) {
                    String ur= String.valueOf(uri);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, succ, "0","0",key11,value11,ur,"qp");
                }
                else if (myPorthash.equals(genHash(String.valueOf(Integer.parseInt(sid) / 2))) && (keycompmyport <= 0)) {
              /*      ContentResolver cr6 = getContext().getContentResolver();
                    new ContentValues();
                    ContentValues cv6;
                    cv6 = new ContentValues();
                    cv6.put("key", key11);
                    cv6.put("value", value11);*/
                    sqlinsert(uri, values);

                } else if ((keycompmyport < 0) && (keycomppred <= 0)) {
                    String ur= String.valueOf(uri);
                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort, pred, "0","0",key11,value11,ur,"qp");

                }

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        }
        return uri;
        // TODO Auto-generated method stub
    }
    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {
        @SuppressLint("WrongThread")
        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];
            Log.e(TAG, "connection established");
            Object object = new Object();
            try {
                while (true) {
                    Log.e(TAG, "before accept");
                    Socket socket = serverSocket.accept();
                    Log.e(TAG, "after accept");
                    ArrayList<String> clal = null;
                    clal = new ArrayList<String>();
                    ObjectInputStream objectInput = new ObjectInputStream(socket.getInputStream()); //Error Line!
                    object = objectInput.readObject();
                    clal = (ArrayList<String>) object;
                    Log.i("recvd 1st msg frm clint", String.valueOf(clal));
                    //            object = objectInput.readObject();
                    //            InputStream is = socket.getInputStream();
                    //            InputStreamReader isr = new InputStreamReader(is);
                    //            BufferedReader br = new BufferedReader(isr);
                    Log.e(TAG, "readline");
                    //            String st;
                    //            st = br.readLine();
                    String recvd_port=String.valueOf((Integer.parseInt(clal.get(0))));
                    String curr_port = String.valueOf((Integer.parseInt(clal.get(1))));
                    String _key = (String.valueOf((clal.get(4))));
                    String hash_ke=genHash((String.valueOf((clal.get(4)))));
                    String valkey = String.valueOf((clal.get(5)));
                    String curr_porthash=genHash(String.valueOf(Integer.parseInt(curr_port)/2));
                    String recport_hash = genHash(String.valueOf(Integer.parseInt(recvd_port)/2));
                    String succhash1=genHash(String.valueOf(Integer.parseInt("0") / 2));
                    String predhash1 = genHash(String.valueOf(Integer.parseInt("0") / 2));
                    if(succ.compareTo("")!=0) {
                        succhash1 = genHash(String.valueOf(Integer.parseInt(succ) / 2));
                        Log.i("hash_succ",String.valueOf(Integer.parseInt(succ) / 2));
                        Log.i("hash_succ",succhash1);
                    }
                    if(pred.compareTo("")!=0) {
                        predhash1 = genHash(String.valueOf(Integer.parseInt(pred) / 2));
                        Log.i("hash_pred",String.valueOf(Integer.parseInt(pred) / 2));
                        Log.i("hash_pred",predhash1);

                    }
                    if (_key.compareTo("0")!=0) {
                        Log.i("hash_kejustkey",_key);
                        Log.i("valueofkey",valkey);

                        if(valkey.compareTo("query")==0){
                            Uri u = Uri.parse((clal.get(6)));
                            String[] columnNames = {
                                    "key", "value"
                            };
                            MatrixCursor cursor = new MatrixCursor(columnNames);
                            if(clal.get(2).compareTo("0")!=0) {
                                Log.i("hascursor",clal.get(2));
                                String[] row = {
                                        _key, clal.get(2)
                                };
                                cursor.addRow(row);
                               // getContext().getContentResolver().query(u,row,_key,null,"Querynow");
                                proj=row;
                                Log.i("aftersentcurser", String.valueOf(row));
                                nowquery=true;
                            }
                            else{
                                Log.i("nocursor",clal.get(2));
                                getContext().getContentResolver().query(u,null,_key,null,clal.get(7));
                                Log.i("nocursor",clal.get(7));

                            }
                        }
                        else if(valkey.compareTo("queryall")==0){
                            Cursor cursor4 = getContext().getContentResolver().query(mUri,null,"@",null,"Querynow");
                            Hashtable<String, String> hash_tableserv =
                                    new Hashtable<String, String>();
                            Log.i("beforecurserstar", String.valueOf(hash_tableserv));
                            if (cursor4.moveToFirst()) {
                                do {
                                    Log.i("do", String.valueOf(hash_tableserv));
                                    String ke = cursor4.getString(cursor4.getColumnIndex("key"));
                                    String valu = cursor4.getString(cursor4.getColumnIndex("value"));
                                    Log.i("dolat", String.valueOf(hash_tableserv));
                                    String[] rowst = {
                                            ke, valu
                                    };

                                    hash_tableserv.put(ke,valu);
                                    Log.i("whiledo", String.valueOf(hash_tableserv));
                                    // do what you need with the cursor here
                                } while (cursor4.moveToNext());
                                String ht = hash_tableserv.toString();
                                Log.i("aftersentcurserstar", String.valueOf(hash_tableserv));
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,curr_port,recvd_port,"0",ht,"querysend","queryallsend","0","qp");
                            }
                            else{
                                Log.i("nocurdata", "null");
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,curr_port,recvd_port,"0","null","querysend","queryallsend","0","qp");
                            }
                            cursor4.close();
                        }
                        else if(valkey.compareTo("queryallsend")==0) {
                            String value1 = clal.get(3);
                            if (value1.compareTo("null") != 0) {
                                String value = clal.get(3);
                                value = value.substring(1, value.length() - 1);           //remove curly brackets
                                String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs

                                for (String pair : keyValuePairs)                        //iterate over the pairs
                                {
                                    String[] entry = pair.split("=");                   //split the pairs to get key and value
                                    hash_table.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
                                }
                                Log.i("queryallsend", String.valueOf(hash_table));
                                nowquery = true;
                            }
                            else{
                                nowquery=true;
                            }
                        }
                        else if(valkey.compareTo("receiveports")==0){
                            String sidch = clal.get(6);
                            String lidch = clal.get(7);
                            String remportsstr=clal.get(4);
                            Log.i("receivedremportlist", remportsstr);
                            ArrayList<String> myList=new ArrayList<String>();
                            remportsstr = remportsstr.substring(1, remportsstr.length()-1);
                            String[] portpai = remportsstr.split(",");
                            for (String pai : portpai)                        //iterate over the pairs
                            {
                                                                    //split the pairs to get key and value
                                myList.add(pai.trim());          //add them to the hashmap and trim whitespaces
                            }
                            remports=myList;
                            Log.i("sidcheckbeforenot1", String.valueOf(myList));
                            Log.i("sidcheckafternot1", String.valueOf(Arrays.asList(remportsstr.split(","))));
                            Log.i("lidcheckbeforenot1", remports.get(0));
                            Log.i("lidcheckafternot1", recvd_port);
                            sid=sidch;
                            lid=lidch;
                        }
                        else if(valkey.compareTo("query")!=0) {
                            ContentResolver cr4 = getContext().getContentResolver();
                            ContentValues cv4;
                            cv4 = new ContentValues();
                            cv4.put("key", _key);
                            cv4.put("value", valkey);
                            Uri u = Uri.parse((clal.get(6)));
                            cr4.insert(u, cv4);
                        }
                    }
                    else{
                        Log.i("nohashkey",_key);
                        if(curr_port.compareTo("11108")==0) {
                            boolean contains = remports.contains(recvd_port);
                            if(!remports.contains(recvd_port)) {
                                Log.i("remportsbefore", String.valueOf(remports));
                                remports.add(recvd_port);
                                Log.i("remportsafter", String.valueOf(remports));
                            }
                            if (genHash(String.valueOf(Integer.parseInt(sid) / 2)).compareTo(recport_hash) > 0) {
                                Log.i("sidcheck1", recvd_port);
                                sid = recvd_port;
                            }
                            if(genHash(String.valueOf(Integer.parseInt(sid) / 2)).compareTo(curr_porthash) > 0){
                                sid="11108";
                            }
                            if (genHash(String.valueOf(Integer.parseInt(lid) / 2)).compareTo(recport_hash) < 0) {
                                Log.i("lidcheck1", recvd_port);
                                lid = recvd_port;
                            }
                            if(genHash(String.valueOf(Integer.parseInt(lid) / 2)).compareTo(curr_porthash) < 0){
                                lid="11108";
                            }
                        }
                        /*else{
                            String sidch = clal.get(6);
                            String lidch = clal.get(7);
                            String remportsstr=clal.get(5);
                            Log.i("receivedremportlist", remportsstr);
                            ArrayList<String> myList=new ArrayList<String>();
                            remportsstr = remportsstr.substring(1, remportsstr.length()-1);
                            myList.addAll(Arrays.asList(remportsstr.split(",")));
                            remports = myList;
                            Log.i("sidcheckbeforenot1", String.valueOf(myList));
                            Log.i("sidcheckafternot1", String.valueOf(Arrays.asList(remportsstr.split(","))));
                            Log.i("lidcheckbeforenot1", remports.get(0));
                            Log.i("lidcheckafternot1", recvd_port);
                            sid=sidch;
                            lid=lidch;
                        }*/
                        if (recport_hash.compareTo(curr_porthash)==0){
                            if(recvd_port.compareTo("11116")==0||recvd_port.compareTo("11120")==0){
                                if(String.valueOf((Integer.parseInt(clal.get(2)))).compareTo("0")!=0){
                                    succ=String.valueOf((Integer.parseInt(clal.get(2))));
                                    pred="11108";
                                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,succ,succ,"0",curr_port,"0",remports.toString(),sid,lid);
                                    Log.i("succof11124there",succ);
                                }
                                else if(String.valueOf((Integer.parseInt(clal.get(3)))).compareTo("0")!=0){
                                    if(succ.compareTo("")==0){
                                        succ="11108";
                                    }
                                    pred=String.valueOf((Integer.parseInt(clal.get(3))));
                                    Log.i("succof11124no",succ);
                                    Log.i("predof11124no",pred);

                                }

                            }

                        else if(String.valueOf((Integer.parseInt(clal.get(2)))).compareTo("0")!=0 &&String.valueOf((Integer.parseInt(clal.get(2)))).compareTo("11108")!=0 ){
                            succ=String.valueOf((Integer.parseInt(clal.get(2))));
                            pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            Log.i("succaftrnxthas",succ);
                            Log.i("predaftrnxthas",pred);
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,succ,succ,"0",curr_port,"0",remports.toString(),sid,lid);
                            Log.i("nullcompared","ddfdfdfdf");

                        }
                        else if(String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11120")!=0&&String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11116")!=0 && succ.compareTo("")==0) {
                            succ = ring_start;
                            pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            Log.i("succaftrlast",succ);
                            Log.i("predaftrlast",pred);
                        }
                        else if(String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11120")!=0&&String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11116")!=0 && succ.compareTo("")!=0){
                                Log.i("onlypredbefore",succ);
                                pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            }
                            else if(String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11120")==0||String.valueOf(Integer.parseInt(clal.get(3))).compareTo("11116")==0 ){
                                if(succ.compareTo("")==0){
                                    succ=ring_start;
                                }
                            }

                            if(String.valueOf((Integer.parseInt(clal.get(3)))).compareTo("11120")==0||String.valueOf((Integer.parseInt(clal.get(3)))).compareTo("11116")==0){
                                Log.i("onlypred",succ);
                                pred=String.valueOf((Integer.parseInt(clal.get(3))));
                            }

                    }
                    else if (recport_hash.compareTo(curr_porthash)>0){
                        Log.v("recgtcurrp",succ);
                        if (recport_hash.compareTo(succhash1)<0){
                            Log.v("currrbefore", curr_port);
                            Log.v("recportbefore", recvd_port);
                            Log.v("succbefore", succ);
                            String nextn=succ;
                            if(String.valueOf((Integer.parseInt(clal.get(2)))).compareTo("0")!=0 &&String.valueOf((Integer.parseInt(clal.get(2)))).compareTo("11108")!=0 ){
                                succ=String.valueOf((Integer.parseInt(clal.get(2))));
                                Log.v("aftersuccifyesnext", succ);

                            }
                            else {
                                succ = recvd_port;
                                Log.v("aftersuccnonext", succ);
                            }
                            if(String.valueOf((Integer.parseInt(clal.get(3)))).compareTo("0")!=0) {
                                pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            }
                            else if(recvd_port.compareTo("11120")==0&&predhash1.compareTo(recport_hash)>0){
                                pred = recvd_port;
                            }
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, recvd_port, succ, nextn, curr_port, "0", remports.toString(),sid,lid);

                        }
                        else if(recport_hash.compareTo(succhash1)>0){
                            Log.e(TAG, "recvdgtsucc");
                            Log.v("currr", curr_port);
                            Log.v("recportelse", recvd_port);
                            Log.v("succelse", succ);
                            if(recvd_port.compareTo("11116")==0||recvd_port.compareTo("11120")==0 ){
                                if(succ.compareTo("11108")!=0){
                                    String nextn="0";
                                    if(succ.compareTo("11116")!=0&&succ.compareTo("11120")!=0){
                                        nextn=succ;
                                        succ=recvd_port;
                                    }
                                    if(predhash1.compareTo(recport_hash)<0&&(pred.compareTo("11116")==0||pred.compareTo("11120")==0)&&curr_port.compareTo("11108")==0){
                                        pred=recvd_port;
                                    }
                                    if((pred.compareTo("11108")==0)&&curr_port.compareTo("11108")==0){
                                        pred=recvd_port;
                                    }
                     //               else if(predhash1.compareTo(recport_hash)<0 && curr_port.compareTo("11108")==0){
                     //                   pred=recvd_port;
                     //               }
                          //          else if(predhash1.compareTo(recport_hash)<0){
                          //
                          //          }
                                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,nextn,curr_port,"0",remports.toString(),sid,lid);
                                }
                                else{
                                    succ=recvd_port;
                                    if(pred.compareTo("11108")==0 && curr_port.compareTo("11108")==0) {
                                        pred = recvd_port;
                                    }
                                    new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,"0",curr_port,"0",remports.toString(),sid,lid);

                                }
                            }
                            else if(succ.compareTo("11108")==0 && pred.compareTo("11108")==0 && curr_port.compareTo("11108")==0) {
                                Log.v("no0", recvd_port);
                                Log.v("no0", curr_port);
                                Log.v("no0", succ);
                                succ = recvd_port;
                                pred = recvd_port;
                            }
                            else if(succ.compareTo("11108")==0 && pred.compareTo("11108")==0 && curr_port.compareTo("11108")!=0) {
                                Log.v("no1", recvd_port);
                                Log.v("no1", curr_port);
                                Log.v("no1", succ);
                                succ = recvd_port;
                                pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            }
                            else if(succ.compareTo("11108")!=0){
                                Log.v("no2", recvd_port);
                                Log.v("no2", curr_port);
                                Log.v("no2", succ);
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,"0",curr_port,"0",remports.toString(),sid,lid);

                            }
                        }
                    }
                    else if((recport_hash.compareTo(curr_porthash)<0)){
                        Log.v("currpgtrecvd",succ);
                        if (curr_port.compareTo("11108")==0 && (succ.compareTo("11116")==0||succ.compareTo("11120")==0)){
                            String prevn=pred;
                            Log.v("changepred0", pred);
                            Log.v("changerecvd",recvd_port);
                            Log.v("changepred0", String.valueOf(recport_hash.compareTo(predhash1)));
                            if(predhash1.compareTo(recport_hash)<0){
                                pred=recvd_port;
                                Log.v("changepred", pred);
                            }
                            else if(prevn.compareTo("11116")==0||prevn.compareTo("11120")==0){

                                pred=recvd_port;
                                Log.v("changepred2", pred);
                            }
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,"0",curr_port,"0",remports.toString(),sid,lid);

                        }

                        else if (recport_hash.compareTo(succhash1)<0){
                            Log.v("recvdlesstnsucc", recvd_port);
                            Log.v("currrbeforecgr", curr_port);
                            Log.v("recportbefrcgr", recvd_port);
                            Log.v("succbfrecgr", succ);
                            Log.v("predbefrcgr", pred);
                            String nextn=succ;
                            String prevn=pred;
                            if(predhash1.compareTo(recport_hash)<0){
                                pred=recvd_port;
                            }
                            else if((prevn.compareTo("11108")==0)&&curr_port.compareTo("11108")==0){
                                pred=recvd_port;
                            }
                            if(curr_port.compareTo("11116")!=0&&succ.compareTo("11120")!=0&&(recvd_port.compareTo("11124")==0||recvd_port.compareTo("11112")==0)){
                                succ=recvd_port;
                            }
                            else if(curr_port.compareTo("11116")==0&&succ.compareTo("11108")==0){  //added new
                                succ=recvd_port;
                            }
                            if (succ.compareTo(nextn)==0){
                                nextn="0";
                            }
                            Log.v("succaftercgr", succ);
                            Log.v("predaftercgr", pred);
                            Log.v("nextnaftr", nextn);
                            new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,nextn,curr_port,"0",remports.toString(),sid,lid);
                        }
                        else if(recport_hash.compareTo(succhash1)>0){
                            Log.e(TAG, "recvdgtsucc");
                            Log.v("currr", curr_port);
                            Log.v("recportbfr", recvd_port);
                            Log.v("succbfr", succ);
                            if(succ.compareTo("11108")==0 && pred.compareTo("11108")==0 && curr_port.compareTo("11108")==0) {
                                Log.v("no0", recvd_port);
                                Log.v("no0", curr_port);
                                Log.v("no0", succ);
                                succ = recvd_port;
                                pred = recvd_port;
                            }
                            else if(succ.compareTo("11108")==0 && pred.compareTo("11108")==0 && curr_port.compareTo("11108")!=0) {
                                Log.v("no1", recvd_port);
                                Log.v("no1", curr_port);
                                Log.v("no1", succ);
                                succ = recvd_port;
                                pred = String.valueOf((Integer.parseInt(clal.get(3))));
                            }
                            else if(succ.compareTo("11108")!=0){
                                Log.v("no2", recvd_port);
                                Log.v("no2", curr_port);
                                Log.v("no2", succ);
                                Log.v("no2pred", pred);
                                String prevn=pred;
                                if(predhash1.compareTo(recport_hash)<0){
                                    pred=recvd_port;
                                }
                                else if((prevn.compareTo("11108")==0)&&curr_port.compareTo("11108")==0){
                                    pred=recvd_port;
                                }
                                new ClientTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,recvd_port,succ,"0",curr_port,"0",remports.toString(),sid,lid);

                            }

                        }

                    }
                    //                 publishProgress();
                    //                 socket.close();
                    Log.e(TAG, "Fileend");
                        Log.i("successor",succ);
                        Log.i("predecessor",pred);
                        Log.i("curr_port",curr_port);
                }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "File axax");
            }

            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */
            Log.i("myTag", "con0");
            return null;

        }

        protected void onProgressUpdate(String... strings) {
            /*
             * The following code displays what is received in doInBackground().
             */
            String strReceived = strings[0].trim();
            //           ContentResolver cr = getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put("key", Integer.toString(s));
            cv.put("value", strReceived);
//            cr.insert(mUri, cv);
//            Cursor resultCursor = cr.query(mUri, null, Integer.toString(s), null, null);
            s=s+1;
//            TextView remoteTextView = (TextView) findViewById(R.id.textView1);
//            remoteTextView.append(strReceived + "\t\n");

        }
    }
    private class ClientTask extends AsyncTask<String, Void, Void> {
        //   ObjectOutputStream objOutput = new ObjectOutputStream;
        //    ObjectInputStream[] objInput = new ObjectInputStream[2];
        ArrayList<String> mal = new ArrayList<String>();
        @Override
        protected Void doInBackground(String... msgs) {
            try {

                String remotePort = msgs[1];
                mal.add(msgs[0]);
                      Thread.sleep(500);
                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(remotePort));
                Log.i("myTag", remotePort);
                //       String msgToSend = msgs[0];
                mal.add(msgs[1]);
                mal.add(msgs[2]);
                mal.add(msgs[3]);
                mal.add(msgs[4]);
                mal.add(msgs[5]);
                mal.add(msgs[6]);
                mal.add(msgs[7]);
                try {
                    ObjectOutputStream objOutput = new ObjectOutputStream(socket.getOutputStream());
                    objOutput.writeObject(mal);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //           OutputStreamWriter os = new OutputStreamWriter(socket.getOutputStream());
                //           PrintWriter out = new PrintWriter(os);
                //           out.println(msgToSend);
                //           out.flush();
                //socket.close();

            } catch (UnknownHostException e) {
                Log.e(TAG, "ClientTask UnknownHostException");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
