package org.example.Assignment;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class FilterInvoiceTest {

    @Test
    void filterInvoiceTest() {
        Database realDb = new Database();
        QueryInvoicesDAO realDao = new QueryInvoicesDAO(realDb);
        FilterInvoice filterInvoice = new FilterInvoice(realDao);

        List<Invoice> result = filterInvoice.lowValueInvoices();

        assertTrue(result.stream().allMatch(invoice -> invoice.getValue() < 100));
    }




    @Test
    void filterInvoiceStubbedTest() {
        QueryInvoicesDAO stubDao = mock(QueryInvoicesDAO.class);

        List<Invoice> fakeInvoices = Arrays.asList(
                new Invoice("Customer A", 50),
                new Invoice("Customer B", 30),
                new Invoice("Customer C", 150),
                new Invoice("Customer D", 90)
        );

        when(stubDao.all()).thenReturn(fakeInvoices);

        FilterInvoice filterInvoice = new FilterInvoice(stubDao);

        List<Invoice> result = filterInvoice.lowValueInvoices();

        assertEquals(3, result.size());
    }
}