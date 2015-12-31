package factory.nfctest.cht.com.tw.myapplication;

/**
 * Created by yuchan on 2015/12/31.
 */
public class MovieIRunner extends Movie implements Request {
    @Override
    public void execute(String input) {
        setImdbStars(input);
    }

    @Override
    public String getURL() {
        return null;
    }
}
