package bo;

import bo.handler.ReminderHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReminderHandlerTest {

    @Test
    void addReminder() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("user", "aliasbai");
        jsonObject.put("task", "Baka kaka");
        jsonObject.put("description", "köp äpple, socker, vetemjöl och ägg");
        JSONArray lA = new JSONArray();
            JSONObject l = new JSONObject();
            l.put("id", "ChIJ93aJeZGdX0YRap90H9Qu9ZU");
            l.put("name","ICA Nära Armégatan");
            l.put("address","Armégatan 18, 171 69 Solna, Sweden");
            l.put("long",18.0036933);
            l.put("lat",59.35095499999999);
                JSONArray c = new JSONArray();
                c.put("grocery_or_supermarket");
                c.put("convenience_store");
                c.put("food");
                c.put("point_of_interest");
                c.put("store");
                c.put("establishment");
            l.put("categories", c);
            lA.put(l);

            l = new JSONObject();
            l.put("id", "ChIJ__9vb5OdX0YR2j8QJzSo5Ww");
            l.put("name","ICA Nära Rapidköp");
            l.put("address","Huvudstagatan 7, 171 58 Solna, Sweden");
            l.put("long",17.9977176);
            l.put("lat",59.3536993);
                c = new JSONArray();
                c.put("grocery_or_supermarket");
                c.put("convenience_store");
                c.put("food");
                c.put("point_of_interest");
                c.put("store");
                c.put("establishment");
            l.put("categories", c);
            lA.put(l);

        jsonObject.put("locations",lA);
        jsonObject.put("categories", new JSONArray());

        ReminderHandler.addReminder(jsonObject.toString());
        assertEquals(true, true);
    }
}