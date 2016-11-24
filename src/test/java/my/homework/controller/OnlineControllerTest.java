package my.homework.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import my.homework.dao.BlackListDao;
import my.homework.dao.LoanApplicationDao;
import my.homework.service.BlackListServiceConfiguration;
import my.homework.service.Loan;
import my.homework.service.LoanApplicationStatus;
import my.homework.service.LoanServiceConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OnlineController.class)
@Import({
    LoanServiceConfiguration.class,
    BlackListServiceConfiguration.class,
    OnlineControllerTest.Configuration.class
})
public class OnlineControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private LoanApplicationDao loanApplicationDao;
    @Autowired
    private BlackListDao blackListDao;

    @AfterMethod
    public void resetMocks() throws InterruptedException {
        reset(loanApplicationDao, blackListDao);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApply(
        String request,
        String response,
        boolean expectedBlackListed,
        LoanApplicationStatus expectedStatus
    ) throws Exception {

        when(blackListDao.isPersonalIdBlackListed(anyInt())).thenReturn(expectedBlackListed);

        mockMvc.perform(put(URL_APPLY)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(request)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        //NOTE: simple workaround for async method testing
        Thread.sleep(200);
        verify(blackListDao).isPersonalIdBlackListed(eq(TEST_PERSONAL_ID));

        verify(loanApplicationDao).addLoanApplication(
            eq(TEST_PERSONAL_ID),
            eq(TEST_NAME),
            eq(TEST_SURNAME),
            eq(TEST_TERM),
            eq(TEST_AMOUNT),
            eq(expectedStatus),
            eq(TEST_UUID)
        );
    }

    @DataProvider
    public Object[][] applyDataProvider() throws IOException {
        return new Object[][]{
            {
                Resources.toString(getResource("request-loan-application-valid.json"), UTF_8),
                Resources.toString(getResource("response-loan-application-valid.json"), UTF_8),
                false,
                LoanApplicationStatus.OK
            },
            {
                Resources.toString(getResource("request-loan-application-valid.json"), UTF_8),
                Resources.toString(getResource("response-loan-application-valid.json"), UTF_8),
                true,
                LoanApplicationStatus.PERSON_BLACKLISTED
            }
        };
    }

    @Test(dataProvider = "getLoansApprovedDataProvider")
    public void testGetLoansApproved(List<Loan> loansApproved, String response) throws Exception {
        when(loanApplicationDao.getAllLoansApproved()).thenReturn(loansApproved);

        mockMvc.perform(get(URL_GET_APPROVED)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));
    }

    @DataProvider
    public Object[][] getLoansApprovedDataProvider() throws IOException {
        return new Object[][]{
            {
                ImmutableList.of(
                    new Loan(TEST_TERM, TEST_AMOUNT, TEST_NAME, TEST_SURNAME, TEST_UUID),
                    new Loan(TEST_TERM, TEST_AMOUNT, TEST_NAME, TEST_SURNAME, ANOTHER_TEST_UUID)
                ),
                Resources.toString(getResource("response-get-loans-approved.json"), UTF_8)
            },
            {
                ImmutableList.of(),
                "{\"result\" : [], \"errorResult\": null}"
            }
        };
    }

    @TestConfiguration
    static class Configuration {

        @Bean
        public LoanApplicationDao loanApplicationDao() {
            return mock(LoanApplicationDao.class);
        }

        @Bean
        public BlackListDao blackListDao() {
            return mock(BlackListDao.class);
        }

        @Bean
        public UuidGenerator uuidGenerator() {
            UuidGenerator mockUuidGenerator = mock(UuidGenerator.class);
            when(mockUuidGenerator.generate()).thenReturn(TEST_UUID);

            return mockUuidGenerator;
        }
    }

    private static final String URL_APPLY = "/apply";
    private static final String URL_GET_APPROVED = "/loans_approved";

    private static final long TEST_PERSONAL_ID = 1L;
    private static final String TEST_NAME = "TEST_NAME";
    private static final String TEST_SURNAME = "TEST_SURNAME";
    private static final String TEST_TERM = "TEST_TERM";
    private static final BigDecimal TEST_AMOUNT = BigDecimal.valueOf(100L);
    private static final String TEST_UUID = "TEST_UUID";
    private static final String ANOTHER_TEST_UUID = "ANOTHER_TEST_UUID";
}