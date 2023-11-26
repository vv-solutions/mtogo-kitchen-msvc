package dk.vv.mtogo.kitchen.msvc.facades;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketLineDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;
import dk.vv.common.data.transfer.objects.product.ProductDTO;
import dk.vv.mtogo.kitchen.msvc.api.ProductService;
import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import dk.vv.mtogo.kitchen.msvc.pojos.Ticket;
import dk.vv.mtogo.kitchen.msvc.repsitories.TicketRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class TicketFacade {

    private final TicketRepository repository;

    private final ProductService productService;

    private final ObjectMapper mapper = new ObjectMapper();

    public TicketFacade(TicketRepository ticketRepository, @RestClient ProductService productService) {
        this.repository = ticketRepository;
        this.productService = productService;
    }

    @Transactional
    public TicketDTO enrichWithProductName(TicketDTO ticketDTO) throws JsonProcessingException {

        // collect all product Ids
        List<Integer> productIds = ticketDTO.getTicketLines().stream().map(TicketLineDTO::getProductId).toList();

        // enrich with product name
        var response = productService.getProducts(productIds);

        List<ProductDTO> products = mapper.readValue(response.readEntity(String.class), new TypeReference<List<ProductDTO>>(){});

        for (TicketLineDTO ticketLine : ticketDTO.getTicketLines()) {
            for (ProductDTO product : products) {
                if(ticketLine.getProductId() == product.getId()){
                    ticketLine.setProductName(product.getProductName());
                }
            }
        }
        return ticketDTO;
    }


    public TicketDTO setStatus(TicketDTO ticketDTO, TicketStatus ticketStatus )  {
        ticketDTO.setStatus(ticketStatus.value());
        return ticketDTO;
    }

    @Transactional
    public TicketDTO saveTicket(TicketDTO ticketDTO)  {
        Ticket ticket = new Ticket(ticketDTO);

        repository.persist(ticket);

        return ticket.toDto();
    }

    public List<TicketDTO> getAllTickets(){
        return repository.findAll().list().stream().map(Ticket::toDto).collect(Collectors.toList());
    }

    public List<TicketDTO> getTicketsBySupplierIdAndStatus(TicketStatus ticketStatus,int supplierId) {
        return repository.find("select t from Ticket t where t.status = ?1 and t.supplierId = ?2",ticketStatus.value(),supplierId).list()
                .stream()
                .map(Ticket::toDto)
                .collect(Collectors.toList());
    }

    public List<TicketDTO> getAllTicketsBySupplierId(int supplierId) {
        return repository.find("select t from Ticket t where t.supplierId = ?1",supplierId).list()
                .stream()
                .map(Ticket::toDto)
                .collect(Collectors.toList());
    }

    public void updateTicketStatus(int id, TicketStatus ticketStatus) {
        repository.update("status = ?1 where id =?2",ticketStatus.value(),id);

    }

    public void updateTicketPickupTime(int id, LocalDateTime pickupTime) {
        repository.update("pickupTime = ?1 where id = ?2",pickupTime,id);
    }

    public TicketResponseDTO createTicketResponse(boolean accepted, int ticketId) {
        Ticket ticket = repository.findById((long) ticketId);

        return ticket.toResponse(accepted);
    }
}
