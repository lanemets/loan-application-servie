package my.homework.service;

import java.math.BigDecimal;
import my.homework.constant.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import my.homework.exception.BlackListedPersonIdException;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoanServiceImplTest {

    private LoanServiceImpl loanService;
    @Mock
    private LoanApplicationDao loanApplicationDao;
    @Mock
    private BlackListService blackListService;

    @BeforeTest
    public void setUp() {
        initMocks(this);

        loanService = new LoanServiceImpl(loanApplicationDao, blackListService);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApplySuccess(
        LoanApplicationRequest loanApplicationRequest,
        String countryCode,
        String requestUid
    ) throws Exception {
        loanService.apply(loanApplicationRequest, countryCode, requestUid);

        verify(blackListService).checkBlackListed(eq(loanApplicationRequest.getPersonalId()));

        verify(loanApplicationDao).addLoanApplication(
            eq(loanApplicationRequest.getPersonalId()),
            eq(loanApplicationRequest.getName()),
            eq(loanApplicationRequest.getSurname()),
            eq(loanApplicationRequest.getTerm()),
            eq(loanApplicationRequest.getAmount()),
            eq(countryCode),
            eq(requestUid)
        );
    }

    @DataProvider
    public static Object[][] applyDataProvider() {
        return new Object[][]{
            {
                new LoanApplicationRequest(
                    new BigDecimal(100L),
                    123L,
                    "TERM",
                    "NAME",
                    "SURNAME"
                ),
                "US",
                "TEST_UID"
            }
        };
    }

    @Test(expectedExceptions = BlackListedPersonIdException.class)
    public void testApplicationIsBlackListed() {
        long personalId = 1L;
        LoanApplicationRequest loanApplicationRequest = new LoanApplicationRequest(
            new BigDecimal(100L),
            personalId,
            "term",
            "name",
            "surname"
        );

        doThrow(BlackListedPersonIdException.class).when(blackListService).checkBlackListed(eq(personalId));
        loanService.apply(loanApplicationRequest, TEST_COUNTRY_CODE, TEST_REQUEST_UUID);
    }

    @Test(dataProvider = "getAllLoansDataProvider")
    public void testGetAllLoansApproved(Long personalId) throws Exception {
        loanService.getAllLoansApproved(personalId);

        verify(loanApplicationDao).getAllLoansApproved(eq(personalId));
    }

    @DataProvider
    public static Object[][] getAllLoansDataProvider() {
        return new Object[][]{
            {
                123L
            },
            {
                null
            }
        };
    }

    @Test
    public void testGetLoanApplicationByUid() {
        loanService.getLoanApplicationByUid(TEST_REQUEST_UUID);

        verify(loanApplicationDao).getLoanApplicationByUid(eq(TEST_REQUEST_UUID));
    }

    private static final String TEST_REQUEST_UUID = "3e97f703c2703236";
    private static final String TEST_COUNTRY_CODE = "US";
}