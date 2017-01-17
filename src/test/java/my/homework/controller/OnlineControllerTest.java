package my.homework.controller;

import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import my.homework.common.LoanTracingContext;
import my.homework.common.MdcWrapper;
import my.homework.common.UuidGenerator;
import my.homework.constant.LoanApplicationRequest;
import my.homework.country.CountryCodeResolver;
import my.homework.exception.BlackListedPersonIdException;
import my.homework.security.SecurityConfiguration;
import my.homework.service.LoanApplication;
import my.homework.service.LoanService;
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
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
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
    OnlineControllerTest.Configuration.class
})
public class OnlineControllerTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private LoanService loanService;
    @Autowired
    private CountryCodeResolver countryCodeResolver;
    @Autowired
    private MdcWrapper mdcWrapper;
    @Autowired
    private MockMvc mockMvc;

    @AfterMethod
    public void resetMocks() {
        reset(loanService, countryCodeResolver, mdcWrapper);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApplySuccess(
        String request,
        String response,
        LoanApplicationRequest loanApplicationRequest,
        String ipAddress,
        String countryCode,
        String requestUuid
    ) throws Exception {
        when(countryCodeResolver.resolve(ipAddress)).thenReturn(countryCode);

        mockMvc.perform(put(URL_APPLY)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(request)
            .header(AUTHORIZATION, BASIC_AUTH_HEADER_VALUE)
            .header(TRACE_ID, TRACE_ID)
            .header(SPAN_ID, SPAN_ID)
            .header(PARENT_SPAN_ID, PARENT_SPAN_ID)
            .with(
                mockHttpServletRequest -> {
                    mockHttpServletRequest.setRemoteAddr(ipAddress);
                    return mockHttpServletRequest;
                }
            )
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(mdcWrapper).enter(
            eq(
                new LoanTracingContext(
                    TRACE_ID,
                    SPAN_ID + "Generated",
                    SPAN_ID
                )
            )
        );

        verify(loanService).apply(
            eq(loanApplicationRequest),
            eq(countryCode),
            eq(requestUuid)
        );

        verify(mdcWrapper).exit();
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
                TEST_UUID
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
                TEST_UUID
            }
        };
    }

    @Test
    public void testPersonIsBlackListed() throws Exception {
        String request = Resources.toString(getResource("request-loan-application-valid.json"), UTF_8);
        String response = Resources.toString(getResource("response-loan-application-blacklisted-person.json"), UTF_8);

        doThrow(new BlackListedPersonIdException()).when(loanService).apply(
            any(LoanApplicationRequest.class),
            anyString(),
            anyString()
        );

        mockMvc.perform(put(URL_APPLY)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(request)
            .header(AUTHORIZATION, BASIC_AUTH_HEADER_VALUE)
            .with(
                mockHttpServletRequest -> {
                    mockHttpServletRequest.setRemoteAddr(TEST_IP_ADDRESS);
                    return mockHttpServletRequest;
                }
            )
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));
    }

    @SuppressWarnings("unchecked")
    @Test(dataProvider = "getLoansApprovedDataProvider")
    public void testGetLoansApproved(List<LoanApplication> loansApproved, String responseExpected) throws Exception {
        when(loanService.getAllLoansApproved(anyLong())).thenReturn(loansApproved);

        mockMvc.perform(createRequestBuilder(URL_GET_APPROVED))
            .andExpect(status().isOk())
            .andExpect(content().json(responseExpected, true));

        verify(loanService).getAllLoansApproved(eq(null));
    }

    @DataProvider
    public Object[][] getLoansApprovedDataProvider() throws IOException {
        return new Object[][]{
            {
                ImmutableList.of(
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, TEST_UUID),
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 456L, TEST_UUID)
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
        when(loanService.getAllLoansApproved(eq(personalId))).thenReturn(loansApproved);
        mockMvc.perform(
            createRequestBuilder(URL_GET_APPROVED + "/{personal_id}", personalId))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(loanService).getAllLoansApproved(personalId);
    }

    @DataProvider
    public static Object[][] getLoansApprovedByUserDataProvider() throws IOException {
        return new Object[][]{
            {
                ImmutableList.of(
                    new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, TEST_UUID)
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
        when(loanService.getLoanApplicationByUid(eq(applicationUid))).thenReturn(loansFound);

        mockMvc.perform(
            createRequestBuilder(URL_GET_BY_UID + "/{application_uid}", applicationUid))
            .andExpect(status().isOk())
            .andExpect(content().json(response, true));

        verify(loanService).getLoanApplicationByUid(eq(applicationUid));
    }

    @DataProvider
    public static Object[][] getLoanByUidDatProvider() throws IOException {
        return new Object[][]{
            {
                new LoanApplication("TEST_TERM", BigDecimal.valueOf(100L), "TEST_NAME", "TEST_SURNAME", 123L, TEST_UUID),
                TEST_UUID,
                Resources.toString(getResource("response-get-loan-by-uid.json"), UTF_8)
            },
            {
                null,
                TEST_UUID,
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
        public LoanService loanService() {
            return mock(LoanService.class);
        }

        @Bean
        public ThrottlingRequestSettings throttlingRequestSettings() {
            ThrottlingRequestSettings mockThrottlingRequestSettings = mock(ThrottlingRequestSettings.class);
            when(mockThrottlingRequestSettings.getMaxCapacity()).thenReturn(1);
            when(mockThrottlingRequestSettings.getPeriod()).thenReturn(1);
            when(mockThrottlingRequestSettings.getTimeUnit()).thenReturn(TimeUnit.MILLISECONDS.name());

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

        @Bean
        public MdcWrapper mdcWrapper() {
            return mock(MdcWrapper.class);
        }

        @Bean
        public UuidGenerator uuidGenerator() {
            return name -> name + "Generated";
        }
    }

    private static final String TEST_IP_ADDRESS = "208.80.152.201";

    private static final String URL_APPLY = "/apply";
    private static final String URL_GET_APPROVED = "/loans_approved";
    private static final String URL_GET_BY_UID = "/loans_applications";

    private static final String TEST_UUID = "requestUuidGenerated";
    private static final String BASIC_AUTH_HEADER_VALUE = "Basic dXNlcjpRd2VydHkxMg==";

    private static final String TRACE_ID = "X-B3-TraceId";
    private static final String SPAN_ID = "X-B3-SpanId";
    private static final String PARENT_SPAN_ID = "X-B3-ParentSpanId";

}