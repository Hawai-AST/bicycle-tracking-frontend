package models.utility;

/**
 * Factory Class
 */
public class AST {
    public static ASTPreparedJson PreparedJson(String url) {
        return new ASTPreparedJson(url);
    }
}
