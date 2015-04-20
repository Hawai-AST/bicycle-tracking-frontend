package models.utility;

/**
 * Factory Class
 */
public class AST {
    public static PreparedJson preparedJson(String url) {
        return new ASTPreparedJson(url);
    }
}
