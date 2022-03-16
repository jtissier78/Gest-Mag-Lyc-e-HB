package com.jtissdev.api.Resources;

import com.jtissdev.api.Controller.ConsommableController;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.json.simple.JSONObject;

@Path("/consommables")
@Produces("{MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON}")
public class ConsommableRessource {

    @GET
    public JSONObject getConsommables(){
        System.out.println("get All Consumables");
        return  ConsommableController.getConsommables();
    }

}
