package factory.nfctest.cht.com.tw.myapplication;


import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by yuchan on 2015/12/31.
 */
public class MovieCRunner implements Request {
    List<Movie> mList =new ArrayList<Movie>();
    private final String TAG="Movie";
    private MovieERunner eRunner =null;

    @Override
    public void execute(String input) {

        Document doc = Jsoup.parse(input);
        Elements voditem = doc.getElementsByClass("voditem");
        ListIterator<Element> list = voditem.listIterator();
        while(list.hasNext()){
            Elements e =  list.next().getElementsByClass("title");
            Log.d(TAG, "execute: " + e.first().text());
            Movie m =new Movie();
            m.setcName(e.first().text());
            mList.add(m);
        }
        eRunner.setMovie(mList);
    }

    @Override
    public String getURL() {
        return "http://mod.cht.com.tw/vodrw/vodcat.php?id=2&tid=20";
    }

    public void setEMovies(MovieERunner eRunner){
        this.eRunner = eRunner;
    }
}
