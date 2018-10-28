package com.leafapps.radiogewinnspiel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class AlarmReceiver extends BroadcastReceiver{
    protected String Song0;
    protected String Song1;
    protected String Song2;
    protected String time0;
    protected String time1;
    protected String time2;

    @Override
    public void onReceive(Context context, Intent intent) {
        //start the background job
        ExecuteBackgroundProcess();

}


    //Execute Background Process
    public void ExecuteBackgroundProcess(){
        new SongsDownload().execute();
    }

    //Class for the Background download of the song texts
    private class SongsDownload extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            //For coding on pre execution
            */
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Document fullHtmlDocument = Jsoup.connect("http://www.einslive.de/diggi/on-air/playlist/playlist-diggi100.html").get();
                //Document fullHtmlDocument = Jsoup.connect("http://www.einslive.de/einslive/musik/playlist/index.html").get();

                Document fullHtmlDocument = Jsoup.connect("http://1live.radio.de/").get();
                int i = 0;
                //Elements myElements = fullHtmlDocument.select("a[href]");
                //Elements myElements = fullHtmlDocument.select("div[id=content]"); //td
                //Elements myElements = fullHtmlDocument.select("li[ng-repeat=item in items]");
                Elements myElements = fullHtmlDocument.getElementsByClass("player-inner-wrapper"); //td

                //Elements myElements = fullHtmlDocument.getElementsByClass("table").first().getElementsByTag("tr"); //td


                Log.i("Background", "Job running");
                for (org.jsoup.nodes.Element element : myElements) {
                    switch (i) {
                        case 1:
                            /*
                             // Important: Information for data excess -> check child elements in debugger
                            //Interpret
                            element.child(2).ownText();
                            //Tittel
                            element.child(1).ownText();
                            //Datum
                            element.child(0).textNodes().get(0);
                            //Uhrzeit
                            element.child(0).textNodes().get(1);
                            */

                            //Song0 = element.child(1).child(0).text()+" - "+element.child(1).ownText();
                            Song0 = element.child(2).ownText() + " - " + element.child(1).ownText();
                            time0 = element.child(0).textNodes().get(1).toString();

                            break;
                        case 2:
                            Song1 = element.child(2).ownText() + " - " + element.child(1).ownText();
                            time1 = element.child(0).textNodes().get(1).toString();

                            break;
                        case 3:
                            Song2 = element.child(2).ownText() + " - " + element.child(1).ownText();
                            time2 = element.child(0).textNodes().get(1).toString();
                            break;
                        default:
                            break;
                    }
                    i++;
                    if (i == 4){
                        break;
                    }
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //progressDialog.dismiss();

               MainActivity.settext(Song0, Song1, Song2);
               MainActivity.settime(time0, time1, time2);

        }
    }


}