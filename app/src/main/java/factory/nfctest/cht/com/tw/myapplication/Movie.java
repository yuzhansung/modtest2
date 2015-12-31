package factory.nfctest.cht.com.tw.myapplication;

/**
 * Created by yuchan on 2015/12/31.
 */
public class Movie {
    private String cName = "";
    private String eName = "";
    private String imdbStars= "";

    public String getcName() {
        return cName;
    }

    public Movie setcName(String cName) {
        StringBuffer buf = new StringBuffer();
        this.cName = cName;
        return this;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public void setImdbStars(String imdbStars) {
        this.imdbStars = imdbStars;
    }

    public String getImdbStars() {
        return imdbStars;
    }

    public static String parseCName(String input){
        String buf="";
        return buf;
    }

    public static String parseEName(String input){
        String buf="";
        return buf;
    }

    public static String parseIMDBStars(String input){
        String buf="";
        return buf;
    }
}
