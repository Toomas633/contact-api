package App;

import org.json.JSONObject;

public class Kasutaja {
    private int id;
    private String nimi;
    private String salajane;
    private String tel;

    public Kasutaja(int id, String nimi, String salajane, String tel) {
        this.id = id;
        this.nimi = nimi;
        this.salajane = salajane;
        this.tel = tel;
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("nimi", nimi);
        json.put("salajane", salajane);
        json.put("tel", tel);
        return json;
    }
}
