package dk.vv.mtogo.kitchen.msvc.facades;

import com.fasterxml.jackson.core.JsonProcessingException;
import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketLineDTO;
import dk.vv.common.data.transfer.objects.kitchen.TicketResponseDTO;

import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import dk.vv.mtogo.kitchen.msvc.repsitories.TicketRepository;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

@QuarkusTest
public class TicketFacadeIT {

    @Inject
    protected Flyway flyway;

    @Inject
    TicketFacade ticketFacade;

    @Inject
    TicketRepository ticketRepository;


    @BeforeEach
    public void before() {
        flyway.migrate();
    }


    @AfterEach
    public void restoreDatabase() {
        flyway.clean();
    }


    @Test
    void when_setStatus_with_status_pending_ticket_status_should_be_1() throws JsonProcessingException {
        //Arrange
        TicketDTO ticketDTO = new TicketDTO();

        //ACT
        TicketDTO updated = ticketFacade.setStatus(ticketDTO, TicketStatus.PENDING);

        //Assert
        Assertions.assertEquals(1, updated.getStatus());
    }



    @Test
    void when_enrich_with_product_names_product_names_should_be_set() throws JsonProcessingException {
        //Arrange
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setComment("test");
        ticketDTO.setSupplierId(1);
        ticketDTO.setOrderId(5);

        TicketLineDTO ticketLine1 = new TicketLineDTO();

        ticketLine1.setQuantity(1);
        ticketLine1.setProductId(1);

        ticketDTO.addTicketLine(ticketLine1);

        //ACT
        var ticket = ticketFacade.enrichWithProductName(ticketDTO);

        //Assert
        Assertions.assertEquals("this is a product name",ticket.getTicketLines().stream().findFirst().get().getProductName());
    }

    @Test
    @Transactional
    void when_save_ticket_size_of_all_products_should_be_4() throws JsonProcessingException {
        //Arrange
        TicketDTO ticketDTO = new TicketDTO();

        //ACT
        TicketDTO created = ticketFacade.saveTicket(ticketDTO);

        var all = ticketRepository.listAll();

        //Assert
        Assertions.assertEquals(7, all.size());
    }

    @Test
    void when_getting_all_tickets_size_should_be_6(){

        //ACT
        var tickets = ticketFacade.getAllTickets();

        //Assert
        Assertions.assertEquals(6,tickets.size());

    }
    @Test
    void when_getting_tickets_by_supplier_id_4_and_ticket_enum_status_in_progress_size_should_be_2(){
        //ACT
        var tickets = ticketFacade.getTicketsBySupplierIdAndStatus(TicketStatus.IN_PROGRESS,4);

        //Assert
        Assertions.assertEquals(2,tickets.size());

    }
    @Test
    void when_getting_all_tickets_by_supplier_id_4_size_should_be_4(){
        //ACT
        var tickets = ticketFacade.getAllTicketsBySupplierId(4);

        //Assert
        Assertions.assertEquals(4,tickets.size());

    }

    @Test
    @Transactional
    void when_update_status_then_status_should_be_persisted(){

        //ACT
        ticketFacade.updateTicketStatus(3,TicketStatus.DONE);

        var ticket = ticketRepository.findById((long)3);

        //Assert
        Assertions.assertEquals(3,ticket.getStatus());

    }

    @Test
    @Transactional
    void when_update_pickup_time_then_pickup_time_should_be_persisted(){
        //Arrange
        LocalDateTime pickupTime = LocalDateTime.of(2023,10,12,12,53);

        //Act
        ticketFacade.updateTicketPickupTime(6,pickupTime);

        var ticket = ticketRepository.findById((long)6);


        //assert
        Assertions.assertEquals(pickupTime,ticket.getPickupTime());

    }


    @Test
    void when_create_response_dto_from_ticket_2_with_accepted_true_response_should_have_order_id_2_and_accepted_true(){

        //ACT
        TicketResponseDTO ticketResponseDTO = ticketFacade.createTicketResponse(true,2);

        //Assert
        Assertions.assertEquals(2,ticketResponseDTO.getOrderId());
        Assertions.assertTrue(ticketResponseDTO.isAccepted());

    }
}
