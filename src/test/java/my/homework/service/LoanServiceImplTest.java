package my.homework.service;

import my.homework.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;
    @Mock
    private LoanApplicationDao loanApplicationDao;
    @Mock
    private BlackListService blackListService;

    @BeforeTest
    public void setUp() {
        initMocks(this);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApply(
        LoanApplicationRequest loanApplicationRequest,
        String countryCode,
        String requestUid,
        boolean blackListed,
        LoanApplicationStatus status
    ) throws Exception {
        doReturn(blackListed).when(blackListService)
            .isPersonalIdBlackListed(eq(loanApplicationRequest.getPersonalId()));

        loanService.apply(loanApplicationRequest, countryCode, requestUid);

        verify(blackListService, times(1)).isPersonalIdBlackListed(eq(loanApplicationRequest.getPersonalId()));
        verify(loanApplicationDao, times(1)).addLoanApplication(
            eq(loanApplicationRequest.getPersonalId()),
            eq(loanApplicationRequest.getName()),
            eq(loanApplicationRequest.getSurname()),
            eq(loanApplicationRequest.getTerm()),
            eq(loanApplicationRequest.getAmount()),
            eq(status),
            eq(countryCode),
            eq(requestUid)
        );
    }

    @DataProvider
    public static Object[][] applyDataProvider() {
        return new Object[][]{
            {
                new LoanApplicationRequest(new BigDecimal(100L), 123L, "TERM", "NAME", "SURNAME"),
                "US",
                "TEST_UID",
                false,
                LoanApplicationStatus.OK
            },
            {
                new LoanApplicationRequest(new BigDecimal(101L), 124L, "TERM", "NAME", "SURNAME"),
                "US",
                "TEST_UID",
                true,
                LoanApplicationStatus.PERSON_BLACKLISTED
            }
        };
    }

    @Test(dataProvider = "getAllLoansDataProvider")
    public void testGetAllLoansApproved(Long personalId) throws Exception {
        loanService.getAllLoansApproved(personalId);

        verify(loanApplicationDao, times(1)).getAllLoansApproved(eq(personalId));
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
        String uid = "3e97f703c2703236";
        loanService.getLoanApplicationByUid(uid);

        verify(loanApplicationDao, times(1)).getLoanApplicationByUid(eq(uid));
    }
}