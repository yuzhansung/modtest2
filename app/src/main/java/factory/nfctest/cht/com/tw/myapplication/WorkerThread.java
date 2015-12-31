package factory.nfctest.cht.com.tw.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuchan on 2015/12/31.
 */


public class WorkerThread extends Thread {
    private Request req;
    private boolean isContinued = true;
    boolean isIdle(){
        return req == null;
    }
    void setReq(Request request){
        synchronized (this){
            if(isIdle()){
                this.req = request;
                notify();
            }
        }
    }

    @Override
    public void run() {
        Log.d("Movie", "run: Working thread Started");

        while (isContinued){
            synchronized (this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                HttpURLConnection conn = null;
                if(conn==null){
                    try {
                        if(req.getURL()==null){
                            req =null;
                            break;
                        }

                        URL url = new URL(req.getURL());

                        conn= (HttpURLConnection) url
                                .openConnection();

                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        ((HttpURLConnection) conn).setRequestMethod("GET");
                        conn.setUseCaches(false);
                        conn.setAllowUserInteraction(true);
                        HttpURLConnection.setFollowRedirects(true);
                        conn.setInstanceFollowRedirects(true);
                        conn
                                .setRequestProperty(
                                        "User-agent",
                                        "Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
                                                + "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
                        conn
                                .setRequestProperty("Accept",
                                        "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                        conn.setRequestProperty("Accept-Language",
                                "zh-tw,en-us;q=0.7,en;q=0.3");
                        conn.setRequestProperty("Accept-Charse",
                                "Big5,utf-8;q=0.7,*;q=0.7");
                        conn.connect();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    conn.getOutputStream().flush();
                    Log.d("Movie", "run: " + conn.getURL());
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    StringBuffer buf = new StringBuffer();
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                        buf.append(line);
                    }

                    req.execute(buf.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
                conn=null;
                req=null;
            }
        }
    }

    void terminate(){
        isContinued=false;
        setReq(new Request() {
            @Override
            public void execute(String in) {
                //do nothing.
            }

            @Override
            public String getURL() {
                return null;
            }
        });
    }
}

class WorkerThreadPool{
    private List<WorkerThread> workerThreads;
    WorkerThreadPool(){
        workerThreads = new ArrayList<WorkerThread>();
    }

    synchronized void service(Request request){
        boolean idleNotFound = true;
        for (WorkerThread workerThread : workerThreads) {
            if(workerThread.isIdle()){
                workerThread.setReq(request);
                idleNotFound =false;
                break;
            }
        }
        if(idleNotFound){
            WorkerThread workerThread = createWorkerThread();
            workerThread.setReq(request);
        }

    }

    synchronized void cleanIdel(){
        for (WorkerThread workerThread : workerThreads) {
            if(workerThread.isIdle()){
                workerThreads.remove(workerThread);
                workerThread.terminate();
            }
        }
    }

    private WorkerThread createWorkerThread(){
        WorkerThread wThread = new WorkerThread();
        wThread.start();
        workerThreads.add(wThread);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return wThread;
    }
}

interface Request{
    void execute(String input);
    String getURL();
}