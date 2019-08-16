package com.rest.service.content.negotiation.invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class InvoiceResource {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @GetMapping("/invoices")
    public List<Invoice> retrieveAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/invoices/{id}")
    public Invoice retrieveInvoice(@PathVariable long id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);

        if (!invoice.isPresent())
            throw new InvoiceNotFoundException("id-" + id);

        return invoice.get();
    }

    @DeleteMapping("/invoices/{id}")
    public void deleteInvoice(@PathVariable long id) {
        invoiceRepository.deleteById(id);
    }

    @PostMapping("/invoices")
    public ResponseEntity<Object> createInvoice(@RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceRepository.save(invoice);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedInvoice.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<Object> updateInvoice(@RequestBody Invoice invoice, @PathVariable long id) {

        Optional<Invoice> invoiceOptional = invoiceRepository.findById(id);

        if (!invoiceOptional.isPresent())
            return ResponseEntity.notFound().build();

        invoice.setId(id);

        invoiceRepository.save(invoice);

        return ResponseEntity.noContent().build();
    }
}
