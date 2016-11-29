package my.homework.service;

import java.math.BigDecimal;
import my.homework.LoanApplicationRequest;
import my.homework.dao.LoanApplicationDao;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;
    @Mock
    private LoanApplicationDao loanApplicationDao;

    @BeforeTest
    public void setUp() {
        initMocks(this);
    }

    @Test(dataProvider = "applyDataProvider")
    public void testApply(
        LoanApplicationRequest loanApplicationRequest,
        String countryCode,
        String requestUid
    ) throws Exception {
        loanService.apply(loanApplicationRequest, countryCode, requestUid);

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
                new LoanApplicationRequest(new BigDecimal(100L), 123L, "TERM", "NAME", "SURNAME"),
                "US",
                "TEST_UID"
            }
        };
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
        String uid = "3e97f703c2703236";
        loanService.getLoanApplicationByUid(uid);

        verify(loanApplicationDao).getLoanApplicationByUid(eq(uid));
    }
}