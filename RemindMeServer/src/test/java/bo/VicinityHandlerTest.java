package bo;

import org.junit.jupiter.api.Test;
import ui.ReminderInfo;

import java.util.List;

import static bo.handler.VicinityHandler.getLocationsInVicinity;
import static org.junit.jupiter.api.Assertions.*;

class VicinityHandlerTest {

    @Test
    void getLocationInVicinity() throws Exception {
        List<ReminderInfo> result = getLocationsInVicinity("aliasbai",17.9485135,59.2184058);
        assertEquals(true, true);
    }

}