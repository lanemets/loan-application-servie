package my.homework.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import my.homework.LoanApplicationRequest;
import my.homework.common.UuidGenerator;
import my.homework.country.CountryCodeResolver;
import my.homework.dao.BlackListDao;
import my.homework.dao.LoanApplicationDao;
import my.homework.security.SecurityConfiguration;
import my.homework.service.BlackListServiceConfiguration;
import my.homework.service.LoanApplication;
import my.homework.service.LoanApplicationStatus;
import my.homework.service.LoanServiceConfiguration;
import my.homework.settings.ThreadPoolSettings;
import my.homework.settings.ThrottlingRequestSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OnlineController.class)
@Import({
    SecurityConfiguration.class,
    LoanServiceConfiguration.class,
    BlackListServiceConfiguration.class,
    OnlineControllerTest.Configuration.class
})
public class OnlineControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private LoanApplicationDao loanApplicationDao;
    @Autowired
    private BlackListDao blackListDao;
    @Autowired
    private CountryCodeResolver countryCodeResolver;
    @Autowired
    private MockMvc mockMvc;

    @AfterMethod
    public void resetMocks() {
        reset(loanApplicationDao, blackListDao, countryCodeResolver);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApply(
        String request,
        String response,
        LoanApplicationRequest loanApplicationRequest,
        String ipAddress,
        String countryCode,
        String uuid,
        boolean blackListed,
        LoanApplicationStatus expectedStatus
    ) throws Exception {

        when(blackListDao.isPersonalIdBlackListed(anyInt())).thenReturn(blackListed);
        when(countryCodeResolver.resolve(ipAddress)).thenReturn(countryCode);

        mockMvc.perform(put(URL_APPLY)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(request)
            .header(AUTHORIZATION, BASIC_AUTH_HEADER_VALUE)
            .with(
                mockHttpServletRequest -> {
                    mockHttpServletRequest.setRemoteAddr(ipAddress);
                    return mockHttpServletRequest;
                }
            )
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        //NOTE: simple workaround for async method testing
        Thread.sleep(200);
        verify(blackListDao).isPersonalIdBlackListed(eq(loanApplicationRequest.getPersonalId()));

        verify(loanApplicationDao).addLoanApplication(
            eq(loanApplicationRequest.getPersonalId()),
            eq(loanApplicationRequest.getName()),
            eq(loanApplicationRequest.getSurname()),
            eq(loanApplicationRequest.getTerm()),
            eq(loanApplicationRequest.getAmount()),
            eq(expectedStatus),
            eq(countryCode),
            eq(uuid)
        );
    }

    @DataProvider
    public Object[][] applyDataProvider() throws IOException {
        return new Object[][]{
            {
                Resources.toString(getResource("request-loan-application-valid.json"), UTF_8),
                Resources.toString(getResource("response-loan-application-valid.json"), UTF_8),
                new LoanApplicationRequest(
                    BigDecimal.valueOf(100L),
                    1L,
                    "TEST_TERM",
                    "TEST_NAME",
                    "TEST_SURNAME"
                ),
                "208.80.152.201",
                "US",
                "TEST_UUID",
                false,
                LoanApplicationStatus.OK
            },
            {
                Resources.toString(getResource("request-loan-application-valid.json"), UTF_8),
                Resources.toString(getResource("response-loan-application-valid.json"), UTF_8),
                new LoanApplicationRequest(
                    BigDecimal.valueOf(100L),
                    1L,
                    "TEST_TERM",
                    "TEST_NAME",
                    "TEST_SURNAME"
                ),
                "208.80.152.201",
                "US",
                TEST_UUID,
                true,
                LoanApplicationStatus.PERSON_BLACKLISTED
            },
            {
                Resources.toString(getResource("request-loan-application-valid.json"), UTF_8),
                Resources.toString(getResource("response-loan-application-valid.json"), UTF_8),
                new LoanApplicationRequest(
                    BigDecimal.valueOf(100L),
                    1L,
                    "TEST_TERM",
                    "TEST_NAME",
                    "TEST_SURNAME"
                ),
                "",
                "lv",
                TEST_UUID,
                false,
                LoanApplicationStatus.OK
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "getLoansApprovedDataProvider")
    public void testGetLoansApproved(List<LoanApplication> loansApproved, String response) throws Exception {
        when(loanApplicationDao.getAllLoansApproved(anyLong())).thenReturn(loansApproved);

        mockMvc.perform(createRequestBuilder(URL_GET_APPROVED))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(loanApplicationDao).getAllLoansApproved(eq(null));
    }

    @DataProvider
    public Object[][] getLoansApprovedDataProvider() throws IOException {
        return new Object[][]{
            {
                ImmutableList.of(
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, "TEST_UUID"),
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 456L, "ANOTHER_TEST_UUID")
                ),
                Resources.toString(getResource("response-get-loans-approved.json"), UTF_8)
            },
            {
                ImmutableList.of(),
                "{\"result\" : [], \"errorResult\": null}"
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "getLoansApprovedByUserDataProvider")
    public void testGetLoansApprovedByPerson(List<LoanApplication> loansApproved, Long personalId, String response) throws Exception {
        when(loanApplicationDao.getAllLoansApproved(eq(personalId))).thenReturn(loansApproved);
        mockMvc.perform(
            createRequestBuilder(URL_GET_APPROVED + "/{personal_id}", personalId))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(loanApplicationDao).getAllLoansApproved(personalId);
    }

    @DataProvider
    public static Object[][] getLoansApprovedByUserDataProvider() throws IOException {
        return new Object[][]{
            {
                ImmutableList.of(
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, "TEST_UUID")
                ),
                123L,
                Resources.toString(getResource("response-get-loans-approved-by-user.json"), UTF_8)
            },
            {
                ImmutableList.of(),
                123L,
                "{\"result\" : [], \"errorResult\": null}"
            }
        };
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "getLoanByUidDatProvider")
    public void testGetLoanByUid(LoanApplication loansFound, String applicationUid, String response) throws Exception {
        when(loanApplicationDao.getLoanApplicationByUid(eq(applicationUid))).thenReturn(loansFound);

        mockMvc.perform(
            createRequestBuilder(URL_GET_BY_UID + "/{application_uid}", applicationUid))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(loanApplicationDao).getLoanApplicationByUid(eq(applicationUid));
    }

    @DataProvider
    public static Object[][] getLoanByUidDatProvider() throws IOException {
        return new Object[][]{
            {
                new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, "TEST_UUID"),
                "TEST_UUID",
                Resources.toString(getResource("response-get-loan-by-uid.json"), UTF_8)
            },
            {
                null,
                "TEST_UUID",
                "{\"result\":null,\"errorResult\":null}"
            }
        };
    }

    private static RequestBuilder createRequestBuilder(String url, Object... params) {
        return get(url, params)
            .header(AUTHORIZATION, BASIC_AUTH_HEADER_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE);
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

        @Bean
        public ThrottlingRequestSettings throttlingRequestSettings() {
            ThrottlingRequestSettings mockThrottlingRequestSettings = mock(ThrottlingRequestSettings.class);
            when(mockThrottlingRequestSettings.getMaxCapacity()).thenReturn(1);
            when(mockThrottlingRequestSettings.getPeriod()).thenReturn(1);
            when(mockThrottlingRequestSettings.getTimeUnit()).thenReturn("MILLISECONDS");

            return mockThrottlingRequestSettings;
        }

        @Bean
        public CountryCodeResolver countryCodeResolver() {
            return mock(CountryCodeResolver.class);
        }

        @Bean
        public ThreadPoolSettings threadPoolSettings() {
            ThreadPoolSettings mockThreadPoolSettings = mock(ThreadPoolSettings.class);
            when(mockThreadPoolSettings.getCorePoolSize()).thenReturn(10);
            when(mockThreadPoolSettings.getMaxPoolSize()).thenReturn(10);
            when(mockThreadPoolSettings.getQueueCapacity()).thenReturn(100);
            when(mockThreadPoolSettings.getThreadNamePrefix()).thenReturn("TestThread-");

            return mockThreadPoolSettings;
        }
    }

    private static final String URL_APPLY = "/apply";
    private static final String URL_GET_APPROVED = "/loans_approved";
    private static final String URL_GET_BY_UID = "/loans_applications";

    private static final String TEST_UUID = "TEST_UUID";
    private static final String BASIC_AUTH_HEADER_VALUE = "Basic dXNlcjpRd2VydHkxMg==";

}