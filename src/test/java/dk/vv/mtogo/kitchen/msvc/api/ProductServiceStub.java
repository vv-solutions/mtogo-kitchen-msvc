package dk.vv.mtogo.kitchen.msvc.api;

import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Mock
@RestClient
@ApplicationScoped
public class ProductServiceStub implements ProductService{


    @Override
    public Response getProducts(List<Integer> ids) {
        return Response.ok("[{\"id\":1,\"productName\":\"this is a product name\"},{\"id\":2,\"productName\":\"this is also a productName\"}]").build();
    }
}
