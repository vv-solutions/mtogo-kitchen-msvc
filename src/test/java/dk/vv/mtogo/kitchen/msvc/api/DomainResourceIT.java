package dk.vv.mtogo.kitchen.msvc.api;

import dk.vv.common.data.transfer.objects.kitchen.TicketDTO;
import dk.vv.common.data.transfer.objects.product.ProductDTO;

import dk.vv.mtogo.kitchen.msvc.enums.TicketStatus;
import dk.vv.mtogo.kitchen.msvc.repsitories.TicketRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class DomainResourceIT {
    @Inject
    protected Flyway flyway;

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
    void when_getting_pending_tickets_for_supplierid_4_then_size_should_be_1_and_statuscode_should_be_200(){

        //Arrange
        int supplierId = 4;


        //Act
        List<TicketDTO> tickets = given().when()
                .get("/api/kitchen/pending/"+supplierId)

        //Assert
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", TicketDTO.class);

        Assertions.assertEquals(1,tickets.size());

    }

    @Test
    void when_getting_in_progress_tickets_for_supplierid_4_then_size_should_be_2_and_statuscode_should_be_200(){

        //Arrange
        int supplierId = 4;


        //Act
        List<TicketDTO> tickets = given().when()
                .get("/api/kitchen/inProgress/"+supplierId)

                //Assert
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", TicketDTO.class);

        Assertions.assertEquals(2,tickets.size());

    }


    @Test
    void when_getting_all_tickets_for_supplierid_4_then_size_should_be_4_and_statuscode_should_be_200(){

        //Arrange
        int supplierId = 4;


        //Act
        List<TicketDTO> tickets = given().when()
                .get("/api/kitchen/all/"+supplierId)

                //Assert
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList("", TicketDTO.class);

        Assertions.assertEquals(4,tickets.size());

    }


    @Test
    void when_accept_ticket_with_date_time_ticket_should_be_updated_with_that_time_and_status_code_should_be_200_and_status_should_be_2(){

        //Arrange
        TicketDTO ticketDTO = new TicketDTO();
        LocalDateTime pickupTime = LocalDateTime.of(2023,10,19,20,59,32);
        ticketDTO.setPickupTime(pickupTime);
        ticketDTO.setId(1);

        //Act
        given().body(ticketDTO).when()
                .contentType(ContentType.JSON)
                .put("/api/kitchen/accept/")

                //Assert
                .then()
                .assertThat()
                .statusCode(200);

        var ticket = ticketRepository.findById(Long.parseLong("1"));

        System.out.println(ticket);

        //Assert

        Assertions.assertEquals(2,ticket.getStatus());
        Assertions.assertEquals(pickupTime,ticket.getPickupTime());


    }

    @Test
    void when_deny_ticket_should_be_updated_with_status_4_and_status_code_should_be_200(){

        String ticketId = "1";

        //Act
        given().when()
                .contentType(ContentType.JSON)
                .put("/api/kitchen/deny/"+ticketId)

                //Assert
                .then()
                .assertThat()
                .statusCode(200);

        var ticket = ticketRepository.findById(Long.parseLong("1"));

        //Assert
        Assertions.assertEquals(4,ticket.getStatus());
    }

    @Test
    void when_done_ticket_should_be_updated_with_status_3_and_status_code_should_be_200(){

        //Arrange
        String ticketId = "1";

        //Act
        given().when()
                .contentType(ContentType.JSON)
                .put("/api/kitchen/done/"+ticketId)

                //Assert
                .then()
                .assertThat()
                .statusCode(200);

        var ticket = ticketRepository.findById(Long.valueOf(ticketId));

        //Assert
        Assertions.assertEquals(3,ticket.getStatus());
    }



}
