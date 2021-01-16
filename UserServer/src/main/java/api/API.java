package api;

import Middleware.Authorisation;
import bo.APIResponse;
import bo.User;
import bo.UserHandler;
import com.auth0.jwk.JwkException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import static bo.UserHandler.getAll;


@Path("/User")
public class API {

    @GET
    @Path("/test")
    @Produces({MediaType.APPLICATION_JSON})
    public int test(){
        return 1;
    }

    @POST
    @Path("/isLoggedIn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response isLoggedIn(APIResponse APIResponse) throws JwkException {
        APIResponse r = new APIResponse();
        if(!Authorisation.validateToken(APIResponse.getToken())){
            r.setMessage("Invalid Token");
          return Response.status(401).entity(r).build();
      }
      r.setMessage("Auth Success");
      return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(r).build();
    }

    @POST
    @Path("/signUp")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response signUp(User user){
        if(UserHandler.isRegistered(user)){
            return Response.status(Response.Status.NOT_ACCEPTABLE).type(MediaType.APPLICATION_JSON).entity("User Already exists").build();
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("User", user);

        if(UserHandler.register(user)){
            jsonObject.putOnce("Response", true);
            return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(user).build();
        }
        jsonObject.putOnce("Response", false);
        return Response.status(Response.Status.EXPECTATION_FAILED).type(MediaType.APPLICATION_JSON).entity(user).build();
    }

    @POST
    @Path("/signIn")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response signIn(User user){
        User signedIn = UserHandler.signIn(user);
        APIResponse APIResponse = new APIResponse();
        if(signedIn == null){
            //Either user does not exist or wrong password. Not specified to protect from attacks.
            APIResponse.setMessage("Auth failed");
            return Response.status(401).entity(APIResponse).build();
        }
        String jwt = Authorisation.generateJWT(signedIn.getUserID());
        APIResponse.setMessage(signedIn.getName());
        APIResponse.setToken(jwt);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(APIResponse).build();
    }

    @POST
    @Path("/SignInWithGoogle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({MediaType.APPLICATION_JSON})
    public Response signInWithGoogle(String id){
        String jwt = Authorisation.generateJWT(id);
        APIResponse r = new APIResponse();
        r.setToken(jwt);
        return Response.status(Response.Status.OK).type(MediaType.APPLICATION_JSON).entity(r).build();
    }

    @GET
    @Path("/getAll")
    @Produces({MediaType.APPLICATION_JSON})
    public List<User> get(){
        return getAll();
    }

}
