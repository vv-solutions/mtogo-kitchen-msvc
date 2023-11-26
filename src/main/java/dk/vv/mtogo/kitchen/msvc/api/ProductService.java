package dk.vv.mtogo.kitchen.msvc.api;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "product-api")
@Path("/api/product")
public interface ProductService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    Response getProducts(List<Integer> ids);

}
