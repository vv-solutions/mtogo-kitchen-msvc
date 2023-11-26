package dk.vv.mtogo.kitchen.msvc.api;

import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.common.data.transfer.objects.product.ProductDTO;
import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import dk.vv.mtogo.kitchen.msvc.facades.TicketFacade;
import dk.vv.mtogo.kitchen.msvc.message.MessageService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import java.util.List;

import static dk.vv.mtogo.kitchen.msvc.api.ExamplePayloads.ACCEPT_TICKET;

@Path("/api/kitchen")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped

public class DomainResource {
    private final TicketFacade ticketFacade;
    private final MessageService messageService;

    @Inject
    public DomainResource(TicketFacade ticketFacade, MessageService messageService) {
        this.ticketFacade = ticketFacade;
        this.messageService = messageService;
    }


    @GET
    @Path("/pending/{supplierId}")
//    @RequestBody(
//            required = true,
//            content = @Content(
//                    schema = @Schema(implementation = ProductDTO.class, required = true, requiredProperties = {"productName", "description", "grossPrice", "supplierId"}),
//                    examples = @ExampleObject(
//                            name = "Product",
//                            value = NEW_PRODUCTS,
//                            summary = "Product",
//                            description = "Product"
//                    )
//            ))
    @Operation(summary = "Get pending tickets", description = "Get pending tickets by supplier id")
    public List<TicketDTO> getPendingBySupplierId(int supplierId){
        return ticketFacade.getTicketsBySupplierIdAndStatus(TicketStatus.PENDING,supplierId);
    }

    @GET
    @Path("/inProgress/{supplierId}")
    @Operation(summary = "Get in progress tickets", description = "Get in progress tickets by supplier id")
    public List<TicketDTO> getInProgressBySupplierId(int supplierId){
        return ticketFacade.getTicketsBySupplierIdAndStatus(TicketStatus.IN_PROGRESS,supplierId);
    }

    @GET
    @Path("/all/{supplierId}")
    @Operation(summary = "Get all tickets", description = "Get all tickets by supplier id")
    public List<TicketDTO> getAllTicketsBySupplierId(int supplierId){
        return ticketFacade.getAllTicketsBySupplierId(supplierId);
    }

    @PUT
    @Path("/accept")
    @Transactional
        @RequestBody(
            required = true,
            content = @Content(
                    schema = @Schema(implementation = TicketDTO.class, required = true, requiredProperties = {"id", "pickupTime"}),
                    examples = @ExampleObject(
                            name = "Ticket",
                            value = ACCEPT_TICKET,
                            summary = "Ticket",
                            description = "Ticket"
                    )
            ))
    @Operation(summary = "Accept ticket", description = "Updates the ticket to acepted by ticket id")
    public Response acceptTicket(TicketDTO ticketDTO){

        // update status and pickup time
        ticketFacade.updateTicketStatus(ticketDTO.getId(),TicketStatus.IN_PROGRESS);
        ticketFacade.updateTicketPickupTime(ticketDTO.getId(),ticketDTO.getPickupTime());

        // create ticket response
        TicketResponseDTO ticketResponseDTO = ticketFacade.createTicketResponse(true,ticketDTO.getId());

        // send ticket response on queue
        messageService.sendTicketResponse(ticketResponseDTO);

       return Response.ok().build();
    }

    @PUT
    @Path("/deny/{id}")
    @Operation(summary = "Deny ticket", description = "Updates the ticket to denied by ticket id")
    @Transactional
    public Response denyTicketById(int id){

        //Updates the status to denied
        ticketFacade.updateTicketStatus(id,TicketStatus.DENIED);


        //Creates the ticket response
        TicketResponseDTO ticketResponseDTO = ticketFacade.createTicketResponse(false,id);

        // send ticket response on queue
        messageService.sendTicketResponse(ticketResponseDTO);

        return Response.ok().build();
    }

    @PUT
    @Path("/done/{id}")
    @Operation(summary = "Done ticket", description = "Updates the ticket to done by ticket id")
    @Transactional
    public Response doneTicketById(int id){

        ticketFacade.updateTicketStatus(id,TicketStatus.DONE);

        //TODO should this send an event?

        return Response.ok().build();
    }



}
