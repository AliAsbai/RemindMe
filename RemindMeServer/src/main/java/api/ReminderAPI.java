package api;

import ui.LocationInfo;
import ui.ReminderInfo;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static bo.handler.ReminderHandler.*;
import static bo.handler.VicinityHandler.getLocationsInVicinity;
import static java.lang.Double.parseDouble;

@Path("/Reminder")
public class ReminderAPI {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "hello world!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/InVicinity/{user}/{long}/{lat}")
    public List<ReminderInfo> inVicinity(@PathParam("user") String user,
                        @PathParam("long") String lon,
                        @PathParam("lat") String lat) {

        return getLocationsInVicinity(user, parseDouble(lon), parseDouble(lat));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add")
    public Response add(String json) {
        Response r = null;
        JSONObject jsonObject = new JSONObject();
        boolean added = addReminder(json);
        if(added) {
            jsonObject.put("message", "Successfully added");
            r = Response.status(Response.Status.OK).entity(jsonObject).type(MediaType.APPLICATION_JSON).build();
        } else {
            jsonObject.put("message", "Error while adding");
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject).type(MediaType.APPLICATION_JSON).build();
        }
        return r;
    }

    @DELETE
    @Path("/Remove/{id}")
    public Response remove(@PathParam("id") String id) {
        Response r = null;
        JSONObject jsonObject = new JSONObject();
        boolean removed = removeReminder(id);
        if(removed) {
            jsonObject.put("message", "Successfully removed");
            r = Response.status(Response.Status.OK).entity(jsonObject).type(MediaType.APPLICATION_JSON).build();
        } else {
            jsonObject.put("message", "Error while removing");
            r = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsonObject).type(MediaType.APPLICATION_JSON).build();
        }
        return r;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{user}/{long}/{lat}")
    public List<ReminderInfo> get(@PathParam("user") String user,
                                  @PathParam("long") String lon,
                                  @PathParam("lat") String lat) {
        return getReminder(user, parseDouble(lon), parseDouble(lat));
    }

}
