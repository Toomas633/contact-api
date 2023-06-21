package App;

public class dbconfig {
    private static String url = "jdbc:postgresql://mysql.toomas633.com:5432/smit";
    private static String username = "postgres";
    private static String password = "Testing1234";

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
