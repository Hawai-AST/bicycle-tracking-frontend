package models.utility;

/**
 * Factory Class
 */
public class AST {
    public static PreparedJson preparedJson(String url) {
        return new ASTPreparedJson(url);
    }

    public static PreparedJson preparedJson(String url, String token) {
        return new ASTPreparedJson(url, token);
    }
}
