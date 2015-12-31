package factory.nfctest.cht.com.tw.myapplication;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by yuchan on 2015/12/31.
 */
public class MovieERunner{
    private List<Movie>  mMovies = null;
    private WorkerThreadPool threadPool = null;

    public MovieERunner(WorkerThreadPool t){
        this.threadPool = t;
    }


    public void setMovie(List<Movie> movies){
        this.mMovies= movies;
        startChkEName();
    }

    private void startChkEName() {
        threadPool.cleanIdel();
        ListIterator<Movie> movieListIterator = mMovies.listIterator();
        while(movieListIterator.hasNext()){
            EMovieRequest req = new EMovieRequest();
            req.setmMovie(movieListIterator.next());
            threadPool.service(req);
        }

    }


}
class EMovieRequest implements Request{
    Movie mMovie = null;

    @Override
    public void execute(String input) {
        Document doc = Jsoup.parse(input);
        Elements voditem = doc.getElementsByClass("functionList");
        ListIterator<Element> list = voditem.listIterator();
        while (list.hasNext()) {
            Element e =  list.next();
            Log.d("Movie", "execute2: " + e.text());
            for (int i = 0; i <6 ; i++) {
                list.next();
            }

        }
    }

    @Override
    public String getURL() {
        String tmp ="";
        try {
            tmp = URLEncoder.encode(mMovie.getcName(),"BIG5");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "http://movie.starblvd.net/cgi-bin/Movie/MV_Film?mode=result&js=1&pg=1?keyword="+tmp;
    }

    public void setmMovie(Movie mMovie) {
        this.mMovie = mMovie;
    }
}