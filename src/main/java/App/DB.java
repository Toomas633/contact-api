package App;

public class DB {
    private static String url = "jdbc:postgresql://sql.toomas633.com:5432/smit";
    private static String username = "postgres";
    private static String password = "Testing1234";

    public static String getUrl() {
        return url + "?charSet=UTF8&useUnicode=true";
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
