package org.example.Assignment;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SAP_BasedInvoiceSenderTest {
    @Test
    public void testWhenLowInvoicesSent() throws Exception {
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSap = mock(SAP.class);

        List<Invoice> fakeInvoices = Arrays.asList(
                new Invoice("Customer A", 50),
                new Invoice("Customer B", 30)
        );

        when(mockFilter.lowValueInvoices()).thenReturn(fakeInvoices);

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSap);
        sender.sendLowValuedInvoices();

        verify(mockSap, times(1)).send(new Invoice("Customer A", 50));
        verify(mockSap, times(1)).send(new Invoice("Customer B", 30));
        verifyNoMoreInteractions(mockSap);
    }

    @Test
    public void testWhenNoInvoices()throws Exception {
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSap = mock(SAP.class);

        when(mockFilter.lowValueInvoices()).thenReturn(Arrays.asList());

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSap);
        sender.sendLowValuedInvoices();

        verify(mockSap, never()).send(any(Invoice.class));
        verifyNoMoreInteractions(mockSap);
    }
    @Test
    public void testThrowExceptionWhenBadInvoice() throws Exception {
        FilterInvoice mockFilter = mock(FilterInvoice.class);
        SAP mockSap = mock(SAP.class);

        Invoice goodInvoice = new Invoice("Customer A", 50);
        Invoice badInvoice = new Invoice("Customer B", 30);

        List<Invoice> fakeInvoices = Arrays.asList(goodInvoice, badInvoice);
        when(mockFilter.lowValueInvoices()).thenReturn(fakeInvoices);

        doThrow(new FailToSendSAPInvoiceException("SAP failed")).when(mockSap).send(badInvoice);

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(mockFilter, mockSap);
        List<Invoice> failedInvoices = sender.sendLowValuedInvoices();

        assertEquals(1, failedInvoices.size());
        assertEquals(badInvoice, failedInvoices.get(0));

        verify(mockSap, times(1)).send(goodInvoice);
        verify(mockSap, times(1)).send(badInvoice);
    }
}
