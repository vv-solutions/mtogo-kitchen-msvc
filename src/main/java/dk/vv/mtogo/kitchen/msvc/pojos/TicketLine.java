package dk.vv.mtogo.kitchen.msvc.pojos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import dk.vv.common.data.transfer.objects.kitchen.TicketLineDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticket_line")
public class TicketLine {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "product_id")
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "product_name")
    private String productName;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;


    // ====== Constructors ======

    public TicketLine() {
    }

    public TicketLine(TicketLineDTO ticketLineDTO) {
        this.id = ticketLineDTO.getId();
        this.productId = ticketLineDTO.getProductId();
        this.quantity = ticketLineDTO.getQuantity();
        this.productName = ticketLineDTO.getProductName();
    }



    // ====== Methods ======

    public TicketLineDTO toDto() {
        TicketLineDTO ticketLineDTO = new TicketLineDTO();
        ticketLineDTO.setId(this.id);
        ticketLineDTO.setQuantity(this.quantity);
        ticketLineDTO.setProductName(this.productName);
        ticketLineDTO.setProductId(this.productId);

        return ticketLineDTO;
    }

    // ====== Getters and setters ======

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

}
