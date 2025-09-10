/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package za.gov.sars.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.hibernate.envers.AuditReader;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ActivityType;
import za.gov.sars.common.ApplicationType;
import za.gov.sars.common.ChargeCategory;
import za.gov.sars.common.NotificationType;
import za.gov.sars.common.QuestionType;
import za.gov.sars.common.ResourceType;
import za.gov.sars.common.RulingType;
import za.gov.sars.common.Status;
import za.gov.sars.common.TaxAct;
import za.gov.sars.config.TestDataSourceConfiguration;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.SystemQuery;
import za.gov.sars.domain.User;
import za.gov.sars.domain.UserApplicationActivity;
import za.gov.sars.domain.UserAtrApplication;
import za.gov.sars.domain.UserRole;
import za.gov.sars.domain.service.ActivityServiceLocal;

//@Ignore
@EnableJpaAuditing
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestDataSourceConfiguration.class)
public class AtrApplicationTestCase {

    @Autowired
    private UserRoleServiceLocal userRoleServiceLocal;

    @Autowired
    private UserServiceLocal userServiceLocal;

    @Autowired
    private AtrApplicationServiceLocal atrApplicationServiceLocal;

    @Autowired
    private UserAtrApplicationServiceLocal userAtrApplicationServiceLocal;

    @Autowired
    private TimesheetServiceLocal timesheetServiceLocal;

    @Autowired
    private EmailNotificationServiceLocal emailService;

    @Autowired
    private ActivityServiceLocal activityServiceLocal;

    @Autowired
    private UserApplicationActivityServiceLocal userApplicationActivityService;

    @Autowired
    private QuestionServiceLocal questionService;

    @Autowired
    private ChargeOutRateCategoryServiceLocal categoryService;

    @Autowired
    private SystemQueryServiceLocal queryService;

    @Autowired
    private ActSectionServiceLocal actSectionService;

    @Autowired
    private AuditReader auditReader;
//    @Autowired
//    private EstimatePlanServiceLocal estimatePlanServiceLocal;

    public AtrApplicationTestCase() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Test
    public void testA() {
        UserRole adminRole = BootStrapHelper.getUserRole("Administrator");
        userRoleServiceLocal.save(adminRole);

        UserRole resourceRole = BootStrapHelper.getUserRole("Resource");
        userRoleServiceLocal.save(resourceRole);

        UserRole managerRole = BootStrapHelper.getUserRole("Manager");
        userRoleServiceLocal.save(managerRole);

        UserRole systemAdminRole = BootStrapHelper.getUserRole("System Administrator");
        userRoleServiceLocal.save(systemAdminRole);

        UserRole snrManagerRole = BootStrapHelper.getUserRole("Senior Manager");
        userRoleServiceLocal.save(snrManagerRole);

        UserRole applicantRole = BootStrapHelper.getUserRole("Applicant");
        userRoleServiceLocal.save(applicantRole);

    }

    @Test
    public void testB() {

        // Administrator Role Tests
        UserRole adminRole = userRoleServiceLocal.findByDescription("Administrator");

        User user0 = BootStrapHelper.getUser("Ramatladi Kopotja", "Ramatladi", "Kopotja", "s1020246", "", "rkopotja@sars.gov.za", "0863832734", "0124834957", "", "");
        user0.setUserRole(adminRole);
        userServiceLocal.save(user0);

        // Senior Manager Role Tests
        UserRole snrManagerRole = userRoleServiceLocal.findByDescription("Senior Manager");

        User user1 = BootStrapHelper.getUser("Vongani Maluleke", "Vongani", "Maluleke", "S2024726", "", "vmaluleke@sars.gov.za", "0826543210", "0124834957", "", "");
        user1.setUserRole(snrManagerRole);
        userServiceLocal.save(user1);

//        User user2 = BootStrapHelper.getUser("Nomsa Dlamini", "Nomsa", "Dlamini", "s1020412", "", "ndlamini@sars.gov.za", "0837654321", "0124834957", "", "");
//        user2.setUserRole(adminRole);
//        userServiceLocal.save(user2);
//
//        User user3 = BootStrapHelper.getUser("Sipho Nkomo", "Sipho", "Nkomo", "s1020523", "", "snkomo@sars.gov.za", "0848765432", "0124834957", "", "");
//        user3.setUserRole(adminRole);
//        userServiceLocal.save(user3);
//
//        User user4 = BootStrapHelper.getUser("Zanele Mabaso", "Zanele", "Mabaso", "s1020634", "", "zmabaso@sars.gov.za", "0859876543", "0124834957", "", "");
//        user4.setUserRole(adminRole);
//        userServiceLocal.save(user4);
//
//        User user5 = BootStrapHelper.getUser("Mandla Khumalo", "Mandla", "Khumalo", "s1020745", "", "mkhumalo@sars.gov.za", "0860987654", "0124834957", "", "");
//        user5.setUserRole(adminRole);
//        userServiceLocal.save(user5);
        // Resource Role Tests
        UserRole resourceRole = userRoleServiceLocal.findByDescription("Resource");

        User user00 = BootStrapHelper.getUser("Andile Qumbisa", "Andile", "Qumbisa", "s2026080", "Andile", "AQumbisa@sars.gov.za", "0826329772", "0124834957", "", "8798080666082");
        user00.setUserRole(resourceRole);
        userServiceLocal.save(user00);

        User user6 = BootStrapHelper.getUser("Mpelwane Morudi", "Mpelwane", "Morudi", "S2026015", "Mpelwane", "mmorudi@sars.gov.za", "0826543210", "0124834957", "", "8801155432108");
        user6.setUserRole(resourceRole);
        userServiceLocal.save(user6);

        User user7 = BootStrapHelper.getUser("Terry Ramurebiwa", "Terry", "Ramurebiwa", "S2028398", "Terry", "tramurebiwa@sars.gov.za", "0837654321", "0124834957", "", "9203087654321");
        user7.setUserRole(resourceRole);
        userServiceLocal.save(user7);

        User user8 = BootStrapHelper.getUser("Nicholas Rabalao", "Nicholas", "Rabalao", "s2030702", "Nicholas", "nrabalao@sars.gov.za", "0848765432", "0124834957", "", "8505128765432");
        user8.setUserRole(resourceRole);
        userServiceLocal.save(user8);

        User user9 = BootStrapHelper.getUser("Bongani Radebe", "Bongani", "Radebe", "s2026434", "Bongani", "BRadebe@sars.gov.za", "0859876543", "0124834957", "", "7910159876543");
        user9.setUserRole(resourceRole);
        userServiceLocal.save(user9);

        User user10 = BootStrapHelper.getUser("Ntombi Shezi", "Ntombi", "Shezi", "s2026545", "Ntombi", "NShezi@sars.gov.za", "0860987654", "0124834957", "", "9407260987654");
        user10.setUserRole(resourceRole);
        userServiceLocal.save(user10);

        // Manager Role Tests
        UserRole managerRole = userRoleServiceLocal.findByDescription("Manager");
        User user11 = BootStrapHelper.getUser("Tshepo Mahlangu", "Tshepo", "Mahlangu", "S2024727", "Tshepo", "TMahlangu@sars.gov.za", "0826543210", "0124834957", "", "8612101234567");
        user11.setUserRole(managerRole);
        userServiceLocal.save(user11);

        User user12 = BootStrapHelper.getUser("Amogelang Phangisa", "Amogelang", "Phangisa", "S2028873", "Amogelang", "aphangisa@sars.gov.za", "0837654321", "0124834957", "", "9008152345678");
        user12.setUserRole(managerRole);
        userServiceLocal.save(user12);

        User user13 = BootStrapHelper.getUser("Mpho Masango", "Mpho", "Masango", "s2028323", "Mpho", "MMasango@sars.gov.za", "0848765432", "0124834957", "", "8309203456789");
        user13.setUserRole(managerRole);
        userServiceLocal.save(user13);

        User user14 = BootStrapHelper.getUser("Lebohang Mohlala", "Lebohang", "Mohlala", "s2028434", "Lebohang", "LMohlala@sars.gov.za", "0859876543", "0124834957", "", "7705244567890");
        user14.setUserRole(managerRole);
        userServiceLocal.save(user14);

        User user15 = BootStrapHelper.getUser("Dineo Motseki", "Dineo", "Motseki", "s2028545", "Dineo", "DMotseki@sars.gov.za", "0860987654", "0124834957", "", "9111105678901");
        user15.setUserRole(managerRole);
        userServiceLocal.save(user15);

        // Applicant Role Tests
        UserRole applicantRole = userRoleServiceLocal.findByDescription("Applicant");

        User user000 = BootStrapHelper.getUser("Tebogo Makau", "Tebogo", "Makau", "", "Andile", "makautebogo@gmail.com", "0826329772", "0124834957", "438305783", "9017286052085");
        user000.setUserRole(applicantRole);
        userServiceLocal.save(user000);

        User user16 = BootStrapHelper.getUser("Regina Machava", "Regina", "Machava", "", "Regina", "rmachava@gmail.com", "0826543210", "0124834957", "438305784", "8903156789012");
        user16.setUserRole(applicantRole);
        userServiceLocal.save(user16);

        User user17 = BootStrapHelper.getUser("Themba Masuku", "Themba", "Masuku", "", "Themba", "themba.masuku@outlook.com", "0837654321", "0124834957", "438305785", "9204187890123");
        user17.setUserRole(applicantRole);
        userServiceLocal.save(user17);

        User user18 = BootStrapHelper.getUser("Precious Mbeki", "Precious", "Mbeki", "", "Precious", "precious.mbeki@yahoo.com", "0848765432", "0124834957", "438305786", "8506218901234");
        user18.setUserRole(applicantRole);
        userServiceLocal.save(user18);

        User user19 = BootStrapHelper.getUser("Kgothatso Mthembu", "Kgothatso", "Mthembu", "", "Kgothatso", "kgothatso.mthembu@webmail.co.za", "0859876543", "0124834957", "438305787", "7808259012345");
        user19.setUserRole(applicantRole);
        userServiceLocal.save(user19);

        User user20 = BootStrapHelper.getUser("Bontle Mogale", "Bontle", "Mogale", "", "Bontle", "bontle.mogale@telkomsa.net", "0860987654", "0124834957", "438305788", "9410030123456");
        user20.setUserRole(applicantRole);
        userServiceLocal.save(user20);

    }
//    @Ignore

    @Test
    public void testC() {
        // Non Billable Activities
        Activity a1 = BootStrapHelper.getActivity("Screening and Allocation");
        a1.setActivityType(ActivityType.NON_BILLABLE);
        a1.setActivityCategory(ActivityCategory.SCREENING_AND_ALLOCATION);
        activityServiceLocal.save(a1);
        Activity a2 = BootStrapHelper.getActivity("Initial Research");
        a2.setActivityCategory(ActivityCategory.INITIAL_RESEARCH);
        a2.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a2);

        Activity a3 = BootStrapHelper.getActivity("Fee Estimate Preparation");
        a3.setActivityCategory(ActivityCategory.FEE_ESTIMATE_PREPARATION);
        a3.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a3);

        Activity a5 = BootStrapHelper.getActivity("Client Meeting");
        a5.setActivityCategory(ActivityCategory.CLIENT_COMMUNICATION_PRIO_TO_ACCEPTANCE_OF_ESTIMATE);
        a5.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a5);

        Activity a20 = BootStrapHelper.getActivity("Client Communication");
        a20.setActivityCategory(ActivityCategory.CLIENT_COMMUNICATION_PRIO_TO_ACCEPTANCE_OF_ESTIMATE);
        a20.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a20);

        Activity a6 = BootStrapHelper.getActivity("Resource Scheduling and Planning");
        a6.setActivityCategory(ActivityCategory.RESOURCE_SCHEDULING_AND_PLANNING);
        a6.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a6);

        Activity a7 = BootStrapHelper.getActivity("Admin and Project Management");
        a7.setActivityCategory(ActivityCategory.ADMIN_AND_PROJECT_MANAGEMENT);
        a7.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a7);

        // Billable or General Activities
        Activity a4 = BootStrapHelper.getActivity("Client Communication");
        a4.setActivityCategory(ActivityCategory.RESEARCH_AND_MEETINGS);
        a4.setActivityType(ActivityType.NON_BILLABLE);
        activityServiceLocal.save(a4);

        Activity a8 = BootStrapHelper.getActivity("Research");
        a8.setActivityCategory(ActivityCategory.RESEARCH_AND_MEETINGS);
        a8.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a8);

        Activity a9 = BootStrapHelper.getActivity("Client Meeting / Discussion / Conference");
        a9.setActivityCategory(ActivityCategory.RESEARCH_AND_MEETINGS);
        a9.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a9);

        Activity a10 = BootStrapHelper.getActivity("Drafting Memos");
        a10.setActivityCategory(ActivityCategory.DRAFT_INTERNAL_DOCUMENTS_AND_REVIEW);
        a10.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a10);

        Activity a11 = BootStrapHelper.getActivity("Drafting Others");
        a11.setActivityCategory(ActivityCategory.DRAFT_INTERNAL_DOCUMENTS_AND_REVIEW);
        a11.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a11);

        Activity a12 = BootStrapHelper.getActivity("Review Document");
        a12.setActivityCategory(ActivityCategory.DRAFT_INTERNAL_DOCUMENTS_AND_REVIEW);
        a12.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a12);

        Activity a13 = BootStrapHelper.getActivity("Consulting – LBC");
        a13.setActivityCategory(ActivityCategory.CONSULTING);
        a13.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a13);

        Activity a19 = BootStrapHelper.getActivity("Consulting – Enforcement");
        a19.setActivityCategory(ActivityCategory.CONSULTING);
        a19.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a19);

        Activity a17 = BootStrapHelper.getActivity("Consulting – Third Party");
        a17.setActivityCategory(ActivityCategory.CONSULTING);
        a17.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a17);

        Activity a18 = BootStrapHelper.getActivity("Consulting – Legal and Policy");
        a18.setActivityCategory(ActivityCategory.CONSULTING);
        a18.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a18);

        Activity a14 = BootStrapHelper.getActivity("Ruling Drafting");
        a14.setActivityCategory(ActivityCategory.RULING_DRAFTING);
        a14.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a14);

        Activity a15 = BootStrapHelper.getActivity("Committee Review");
        a15.setActivityCategory(ActivityCategory.COMMITTEE_REVIEW);
        a15.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a15);

        Activity a16 = BootStrapHelper.getActivity("Ruling Sanitisation");
        a16.setActivityCategory(ActivityCategory.RULING_SANITISATION);
        a16.setActivityType(ActivityType.BILLABLE);
        activityServiceLocal.save(a16);
    }
//      @Ignore

    @Test
    // @Ignore
    public void testD() {
        ChargeOutRateCategory category = BootStrapHelper.getChargeOutRateCategory();
        category.setChargeCategory(ChargeCategory.STANDARD);
        category.setFeeRangeMax(35000.00);
        category.setFeeRangeMin(10000.00);
        category.setPredefinedDays(20);
        category.setDepositPercentage(0.2);
        category.setHourlyRate(650.00);
        categoryService.save(category);

        ChargeOutRateCategory category1 = BootStrapHelper.getChargeOutRateCategory();
        category1.setChargeCategory(ChargeCategory.INVOLVED);
        category1.setFeeRangeMax(70000.00);
        category1.setFeeRangeMin(35000.00);
        category1.setPredefinedDays(45);
        category1.setDepositPercentage(0.2);
        category1.setHourlyRate(650.00);
        categoryService.save(category1);

        ChargeOutRateCategory category2 = BootStrapHelper.getChargeOutRateCategory();
        category2.setChargeCategory(ChargeCategory.COMPLEX);
        category2.setFeeRangeMax(105000.00);
        category2.setFeeRangeMin(70000.00);
        category2.setPredefinedDays(60);
        category2.setDepositPercentage(0.2);
        category2.setHourlyRate(650.00);
        categoryService.save(category2);

        ChargeOutRateCategory category3 = BootStrapHelper.getChargeOutRateCategory();
        category3.setChargeCategory(ChargeCategory.EXTRAORDINARY);
        category3.setFeeRangeMax(999999.00);
        category3.setFeeRangeMin(0.00);
        category3.setDepositPercentage(0.2);
        category3.setPredefinedDays(40);
        category3.setHourlyRate(650.00);
        categoryService.save(category3);

        ChargeOutRateCategory category4 = BootStrapHelper.getChargeOutRateCategory();
        category4.setChargeCategory(ChargeCategory.URGENT_APPLICATION);
        category4.setFeeRangeMax(999999.00);
        category4.setFeeRangeMin(0.00);
        category4.setPredefinedDays(0);
        category4.setDepositPercentage(0.2);
        category4.setHourlyRate(1000.00);
        categoryService.save(category4);

    }

    @Test
    @Ignore
    public void testE() {
        //  Create and save the category

        // SUBMITTED_PAYMENT_UNCONFIRMED Status (3 applications)
        ATRApplication atr1 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_PAYMENT_UNCONFIRMED, RulingType.PRIVATE_RULING, "Discovery Health", ApplicationType.THIRD_PARTY
        );
        atr1.setCaseNum(200000001L);
        atr1.setStatus(Status.SUBMITTED_PAYMENT_UNCONFIRMED);
        atrApplicationServiceLocal.save(atr1);

        ATRApplication atr2 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_PAYMENT_UNCONFIRMED, RulingType.EXTENSION, "MTN Group", ApplicationType.THIRD_PARTY);
        atr2.setCaseNum(200000002L);
        atr2.setStatus(Status.SUBMITTED_PAYMENT_UNCONFIRMED);
        atrApplicationServiceLocal.save(atr2);

        ATRApplication atr3 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_PAYMENT_UNCONFIRMED, RulingType.EXTENSION, "Shoprite Holdings", ApplicationType.THIRD_PARTY);
        atr3.setCaseNum(200000003L);
        atr3.setStatus(Status.SUBMITTED_PAYMENT_UNCONFIRMED);
        atrApplicationServiceLocal.save(atr3);

        // SUBMITTED_AND_PAID Status (3 applications)
        ATRApplication atr4 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_AND_PAID, RulingType.PRIVATE_RULING, "Standard Bank Group", ApplicationType.THIRD_PARTY);
        atr4.setCaseNum(200000004L);
        atr4.setStatus(Status.SUBMITTED_AND_PAID);
        atrApplicationServiceLocal.save(atr4);

        ATRApplication atr5 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_AND_PAID, RulingType.EXTENSION, "Sasol Limited", ApplicationType.THIRD_PARTY);
        atr5.setCaseNum(200000005L);
        atr5.setStatus(Status.SUBMITTED_AND_PAID);
        atrApplicationServiceLocal.save(atr5);

        ATRApplication atr6 = BootStrapHelper.getAtrApplication(Status.SUBMITTED_AND_PAID, RulingType.EXTENSION, "Woolworths Holdings", ApplicationType.THIRD_PARTY);
        atr6.setCaseNum(200000006L);
        atr6.setStatus(Status.SUBMITTED_AND_PAID);
        atrApplicationServiceLocal.save(atr6);

        //persist manager to allocate
        User user1 = userServiceLocal.findBySid("s2026080");
        ATRApplication atrApplication1 = BootStrapHelper.getAtrApplication(Status.ESTIMATE_REQUIRES_AUTHORISATION, RulingType.EXTENSION, "Estimate Plan", ApplicationType.THIRD_PARTY);
        atrApplication1.setCaseNum(100000004L);
        //  atrApplication1.setCreatedDate(new Date());
        atrApplication1.setCreatedBy("s2026015");
        atrApplicationServiceLocal.save(atrApplication1);
        UserAtrApplication userAtrApplication = new UserAtrApplication();
        userAtrApplication.setUser(user1);
        userAtrApplication.setAtrApplication(atrApplication1);
        ATRApplication atrApplication3 = atrApplicationServiceLocal.findByCaseNum(100000003L);

        // READY_FOR_ALLOCATION Status (3 applications)
        ATRApplication atr7 = BootStrapHelper.getAtrApplication(Status.READY_FOR_ALLOCATION, RulingType.PRIVATE_RULING, "Anglo American Platinum", ApplicationType.THIRD_PARTY);
        atr7.setCaseNum(200000007L);
        atr7.setStatus(Status.READY_FOR_ALLOCATION);
        atrApplicationServiceLocal.save(atr7);

        ATRApplication atr8 = BootStrapHelper.getAtrApplication(Status.READY_FOR_ALLOCATION, RulingType.EXTENSION, "FirstRand Bank", ApplicationType.THIRD_PARTY);
        atr8.setCaseNum(200000008L);
        atr8.setStatus(Status.READY_FOR_ALLOCATION);
        atrApplicationServiceLocal.save(atr8);

        ATRApplication atr9 = BootStrapHelper.getAtrApplication(Status.READY_FOR_ALLOCATION, RulingType.EXTENSION, "Vodacom Group", ApplicationType.THIRD_PARTY);
        atr9.setCaseNum(200000009L);
        atr9.setStatus(Status.READY_FOR_ALLOCATION);
        atrApplicationServiceLocal.save(atr9);

        // ALLOCATED_TO_MANAGER Status (3 applications)
        ATRApplication atr10 = BootStrapHelper.getAtrApplication(Status.ALLOCATED_TO_MANAGER, RulingType.PRIVATE_RULING, "Naspers Limited", ApplicationType.THIRD_PARTY);
        atr10.setCaseNum(200000010L);
        atr10.setStatus(Status.ALLOCATED_TO_MANAGER);
        atrApplicationServiceLocal.save(atr10);

        ATRApplication atr11 = BootStrapHelper.getAtrApplication(Status.ALLOCATED_TO_MANAGER, RulingType.EXTENSION, "Gold Fields Limited", ApplicationType.THIRD_PARTY);
        atr11.setCaseNum(200000011L);
        atr11.setStatus(Status.ALLOCATED_TO_MANAGER);
        atrApplicationServiceLocal.save(atr11);

        ATRApplication atr12 = BootStrapHelper.getAtrApplication(Status.ALLOCATED_TO_MANAGER, RulingType.EXTENSION, "Capitec Bank Holdings", ApplicationType.THIRD_PARTY);
        atr12.setCaseNum(200000012L);
        atr12.setStatus(Status.ALLOCATED_TO_MANAGER);
        atrApplicationServiceLocal.save(atr12);

        // CASE_INPROGRESS Status (3 applications)
        ATRApplication atr13 = BootStrapHelper.getAtrApplication(Status.CASE_INPROGRESS, RulingType.PRIVATE_RULING, "Tiger Brands Limited", ApplicationType.THIRD_PARTY);
        atr13.setCaseNum(200000013L);
        atrApplicationServiceLocal.save(atr13);

        ATRApplication atr14 = BootStrapHelper.getAtrApplication(Status.CASE_INPROGRESS, RulingType.EXTENSION, "Clicks Group Limited", ApplicationType.THIRD_PARTY);
        atr14.setCaseNum(200000014L);
        atrApplicationServiceLocal.save(atr14);

        ATRApplication atr15 = BootStrapHelper.getAtrApplication(Status.CASE_INPROGRESS, RulingType.EXTENSION, "Pick n Pay Stores", ApplicationType.THIRD_PARTY);
        atr15.setCaseNum(200000015L);
        atrApplicationServiceLocal.save(atr15);

    }

    @Test
    @Ignore
    public void testG() {
        User user = userServiceLocal.findBySid("s2026080");
        ATRApplication atrApplication = atrApplicationServiceLocal.findByCaseNum(200000016L);
        UserAtrApplication userAtrApplication = BootStrapHelper.getUserAtrApplication(user, atrApplication);
        userAtrApplication.setUser(user);
        userAtrApplication.setResourceType(ResourceType.APPLICANT);
        userAtrApplication.setAtrApplication(atrApplication);
        userAtrApplicationServiceLocal.save(userAtrApplication);
    }

    @Test
    @Ignore
    public void testK() {
        User user = userServiceLocal.findBySid("s2026080");
        Activity activity = activityServiceLocal.findByDescription("Cape Town");
        ATRApplication atrApplication = atrApplicationServiceLocal.findByCaseNum(100000001L);
        UserApplicationActivity userApplicationActivity = BootStrapHelper.getUserApplicationActivity(user, activity, atrApplication);
        userApplicationActivity.setActivity(activity);
        userApplicationActivity.setAtrApplication(atrApplication);
        userApplicationActivity.setUser(user);
        userApplicationActivityService.save(userApplicationActivity);
    }

    @Test
    public void testJ() {
        questionService.save(BootStrapHelper.getQuestion("1. Does your application relate to a completed transaction?", "Transactions that have already occurred or where binding\n"
                + "obligations have already been established, are excluded from the ATR process.\n"
                + "Advance rulings are issued in respect of a 'proposed"
                + "transaction', and receiving a ruling in advance enables an"
                + "applicant to accurately determine the tax consequences. The applicant will have tax certainty on certain aspects before the transaction becomes binding.", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("2. Does your application request a ruling in respect of any of "
                + "the following –", "", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("a) The market value of and asset", "The market value of an asset is a matter of fact, not law, and therefore an application that requires a determination of the market value of an asset or a view on its accuracy may generally be rejected. An advance ruling involves the interpretation and application of the law. [Section 80(1)(a)(I)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("b) The interpretation/application of foreign law", "SARS may reject an application regarding the application or "
                + "interpretation of the laws of a foreign country. The statutory mandate of SARS is to interpret and apply South African law. [Section 80(1)(a)(ii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("c) The pricing of goods or services", "An application may be rejected if it requires the rendering of an opinion or conclusion or the determination of the pricing of goods or services supplied by or rendered to a connected person or associated enterprise in relation to the applicant or a class member. [Section 80(1)(a)(iii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("d) The constitutionality of any tax law", "SARS may not accept an application requesting an opinion, "
                + "conclusion or determination regarding the constitutionality of any tax Act. [Section 80(1)(a)(iv)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("e) A transaction that is hypothetical or not seriously contemplated or matter that is submitted for academic purposes (e.g. as research for a publication).", "Hypothetical scenarios, transactions not seriously contemplated and academic questions are specifically provided for in [section 80(1)(a)(v)[ and (viii) and may be rejected.", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("f) A matter which can be resolved by SARS issuing a directive under the Fourth or Seventh Schedule of the Income Tax Act", "SARS may reject an application on any matters that can be "
                + "resolved by issuing a directive. Directives are also binding on the Commissioner and there are no fees involved in obtaining directives. [Section 80(1)(a)(vi)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("g) Establishing an employer's duty to determine whether a person is an independent contractor, labour broker, personal service broker or a personal service trust", "SARS may reject an application for a ruling request which "
                + "requires a determination relating to an employer’s duty to determine whether a person is an independent contractor, "
                + "labour broker, personal service broker. This issue is both fact and resource intensive. [Section 80(1)(a)(vii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("h) An issue that is frivolous or vexatious in nature.", "ARS may reject an application that presents, contains, or "
                + "raises a frivolous or vexatious issue. Frivolous issues would"
                + "include questions with answers that are completely obvious "
                + "under the plain language of the statute. Vexatious issues would include a request for a determination that a particular tax law is unfair and should be repealed (or that, despite its applicability to the proposed transaction, it should not be enforced because of the hardship which doing so would entail to the applicant). [Section 80(1)(b)(i)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("3. Does your application request a ruling in connection with an alternative course of action by the applicant or a class member that is not seriously contemplated?", "An application where the applicant, not having settled on one set of facts, provides different facts and transaction alternatives that are not seriously contemplated, may be rejected.\n"
                + "Alternative interpretations of law on the same set of facts are permitted, but not an alternate set of facts. For example, an applicant may argue that an amount is not a donation, but if SARS finds that it is, the applicant may argue that a specific exemption applies. Such an approach is acceptable. [Section 80(1)(b)(ii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("4. Does your application present, raise or contain any of the following issues:", "", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("a) A matter currently before SARS in connection with an audit, investigation or other proceeding "
                + "involving you or a connected person in relation to you or a class member", "SARS may not accept an application if it contains an issue that is currently under review (being audited) or otherwise "
                + "investigated by SARS, in respect of that taxpayer."
                + "[Section 80(1)(b)(iii)(aa)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("b) The subject of a policy document or draft legislation that has been published", "Advance rulings are issued in respect of enacted legislation and SARS may, therefore, reject an application relating to draft legislation, not yet enacted. Applications relating to legislation that has been enacted, but which is not in force yet, will be considered.\n"
                + "SARS may accept an application that is based on the current wording of a tax Act, despite the fact that draft legislation is proposed. However, the ruling (if issued) may have limited application in that it may become void if and when the proposed legislation is enacted, which could significantly affect the tax consequences of the transaction. [Section 80(1)(b)(iii)(bb)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("c) Subject to audit or dispute resolution", "An application may be rejected if it contains an issue that is the same or substantially similar to an issue that is currently before SARS in an audit, investigation or other proceeding in respect of the applicant or class member (or person connected to that applicant of class member). "
                + "An application may be rejected if it contains an issue that is subject to dispute resolution under Chapter 9 of the TA Act. The issue in dispute must directly or indirectly affect the application. [Section 80(1)(b)(iii)(cc)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("5. Does the application request the application or interpretation of any general or specific anti-avoidance"
                + "provision or doctrine?", "SARS may reject an application that involves the application or interpretation of an anti-avoidance provision or doctrine. [Section 80(1)(c)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("6. Does the application present an issue that is of an inherently or distinctly factual nature?", "An application that involves issues of a factual nature may be rejected. For example, when SARS is unable to make important factual determinations, because the circumstances are such that the facts are not readily ascertainable.\n"
                + "Other examples, of matters that are generally too factual, are instances where SARS is required to determine whether a company has a ‘business establishment’ or ‘permanent establishment’ in a specificalocation or where its ‘place of effective management’ is located. [Section 80(1)(d)(i)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("7. Does the application entail a proposed transaction, the resolution of which would depend upon assumptions of future events or other matters that cannot be reasonably determined at the time of the application?", "SARS may reject an application regarding or in respect of an issue of which the material facts in connection with the issue raised cannot be established at the time of the application.\n"
                + "Under this exclusion, an application would generally be "
                + "rejected if the resolution of the issue depends upon a future action of another person unless that person is a party to the proposed transaction. In addition, an application would generally be rejected if the resolution of the issue depends upon future events that are beyond the control of the applicant or other parties to the proposed transaction. [Section 80(1)(d)(ii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("8. Does the application present an issue which would be more appropriately dealt with by the competent "
                + "authorities of the parties to an agreement for the avoidance of double taxation?", "[Section 80(1)(d)(iii)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("9. Does your application present an issue in which the tax treatment of the applicant is dependent upon the tax treatment of another party to the proposed transaction (and that other party has not applied for a ruling)?", "[Section 80(1)(d)(vi)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("10. Does your application present an issue which is the same or substantially similar to an issue that the applicant has already received an unfavourable ruling?", "[Section 80(1)(d)(vi)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("11. Does your application involve an issue in respect of a transaction that is part of another transaction which has a "
                + "bearing on the issue, the details of which have not been disclosed;", "[Section 80(1)(d)(v)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("12. Does your application present an issue that would be unduly time-consuming or resource intensive?", "SARS may reject an application regarding a matter that would be unduly time-consuming or resource intensive. [Section 80(1)(e)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("13. Does your application require SARS to rule on the substance of a transaction and disregard its form?", "[Section 80(1)(f)]", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("14. Does your application require a ruling on a matter that is contained in the current No-Rulings List published in the Government Gazette.", "Section 80(1)(a) to (f) of the TA Act provides for several "
                + "instances in respect of which SARS may reject an application.\n"
                + "The Commissioner may also publish a list of additional "
                + "considerations in respect of which it may reject an application.\n"
                + "The latter is loosely referred to as the 'No- Rulings List'. The 'No-Rulings List' is updated by SARS as and when the need arises.", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("15. Have you obtained any view / opinion, preliminary or otherwise, from any third party or SARS in relation to this ruling application?", "n/a", QuestionType.PRE_SCREENING));
        questionService.save(BootStrapHelper.getQuestion("16. Is the Applicant a SMME (Small, Medium and Micro Enterprise)?", "An SMME is any small, medium or micro enterprise. In particular, it means any person that is either – \n• a natural person (including the deceased or insolvent estate of a natural person); or \n• unlisted company; \n• close corporation; \n• trust (provided that all of the members of that trust are natural persons); if the gross income for the most recent year of assessment did not exceed the amount prescribed in the definition of a ‘small business corporation’ under section 12E(4)(a)(i) of the income Tax act; and \n• in respect of applications for VAT rulings only, any partnership, where the gross income for the most recent year of assessment did not exceed the amount prescribed in the definition of a ‘small business corporation’.", QuestionType.PRE_SCREENING));

        questionService.save(BootStrapHelper.getQuestion("Is the PBO registered in South Africa for tax purposes?", "", QuestionType.BPO));
        questionService.save(BootStrapHelper.getQuestion("Is the CC registered in South Africa for tax purposes?", "", QuestionType.NGO));
        questionService.save(BootStrapHelper.getQuestion("Is the company a Public company?", "", QuestionType.COMPANY_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the company listed?", "", QuestionType.COMPANY_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the company a member of a group?", "", QuestionType.COMPANY_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the company a subsidiary of a foreign holding company?", "", QuestionType.COMPANY_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the company registered in South Africa for tax purposes?", "", QuestionType.COMPANY_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the trust registered in South Africa for tax purposes?", "", QuestionType.TRUST_QUESTIONS));
        questionService.save(BootStrapHelper.getQuestion("Is the NGO registered in South Africa for tax purposes?", "", QuestionType.NGO));
        questionService.save(BootStrapHelper.getQuestion("Have you or a connected person to you or a member on behalf of your class, applied for a previous ruling on the same or substantially similar issue(s)?", "", QuestionType.NGO));
        questionService.save(BootStrapHelper.getQuestion("Are there any parties connected to you that have entered into a similar transaction to which the ruling applies?", "", QuestionType.NGO));
        questionService.save(BootStrapHelper.getQuestion("If yes, to the best of your knowledge are any of these parties being audited by SARS? Please provide details.", "", QuestionType.NGO));
        questionService.save(BootStrapHelper.getQuestion("Have you obtained any view or opinion, preliminary or otherwise, whether from any third party or SARS in relation to this ruling application? If so, please attach it to the application documents.", "", QuestionType.TRUST_QUESTIONS));

        questionService.save(BootStrapHelper.getQuestion("Is the partnership registered in South Africa for VAT and/or PAYE purposes?", "", QuestionType.NGO));

        questionService.save(BootStrapHelper.getQuestion("Is this person a resident as defined?", "", QuestionType.INDIVIDUAL));
        questionService.save(BootStrapHelper.getQuestion("Is this person registered in south africa for tax purposes?", "", QuestionType.INDIVIDUAL));

    }

    @Test
    @Ignore
    public void testL() {
        List<ATRApplication> atrApplicationRevisions = new ArrayList<>();
        ATRApplication atrApplication = atrApplicationServiceLocal.findByCaseNum(200000001L);
        List<Number> revisions = auditReader.getRevisions(ATRApplication.class, atrApplication.getId());
        assertEquals(1, revisions.size());

        // Verify creation revision
//        ATRApplication createdVersion = auditReader.find(ATRApplication.class, atrApplication.getId(), revisions.get(0));
//        assertEquals("Johnathan Doe", createdVersion.getApplicantName());
        // Make an update
        atrApplication.setApplicantName("Johnathan Doe");
        atrApplicationServiceLocal.save(atrApplication);
        // entityManager.flush();
        atrApplication.setCountry("South Africa");
        atrApplicationServiceLocal.save(atrApplication);
        // Should now have 2 revisions
        revisions = auditReader.getRevisions(ATRApplication.class, atrApplication.getId());
        assertEquals(3, revisions.size());

        // Verify update revision
//        ATRApplication updatedVersion = auditReader.find(ATRApplication.class, atrApplication.getId(), revisions.get(0));
//        assertEquals("Johnathan Doe", updatedVersion.getApplicantName());
//      
        revisions.stream().map(n -> auditReader.find(ATRApplication.class, atrApplication.getId(), n)).forEachOrdered(auditedAtrApplication -> {
            System.out.print(auditedAtrApplication.getApplicantName());
            atrApplicationRevisions.add(auditedAtrApplication);
        });
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        for (ATRApplication thisApplication : atrApplicationRevisions) {
            System.out.println(thisApplication.getApplicantName());
        }

    }

    @Test
    @Ignore
    public void testN() {
        ATRApplication atrApplication1 = atrApplicationServiceLocal.findByCaseNum(200000001L);
        SystemQuery query = BootStrapHelper.getQuery("I am generating my first system query!I am generating my first system query!I am generating my first system query!I am generating my first system query!I am generating my first system query!");
        query.setCreatedBy("Applicant");
        query.setAtrApplication(atrApplication1);
        queryService.save(query);

        SystemQuery query1 = BootStrapHelper.getQuery("I am responding to your system query!I am responding to your system query!I am responding to your system query!I am responding to your system query!I am responding to your system query!");
        query1.setCreatedBy("Admin");
        query1.setAtrApplication(atrApplication1);
        queryService.save(query1);

        SystemQuery query2 = BootStrapHelper.getQuery("Thank you for responding!");
        query2.setCreatedBy("Applicant");
        query2.setAtrApplication(atrApplication1);
        queryService.save(query2);
    }

    @Test
    @Ignore
    public void testO() {
//        ChargeOutRateCategory category = categoryService.findByDescription("Standard");
        // ESTIMATE_INPROGRESS Status (3 applications)
        ATRApplication atr16 = atrApplicationServiceLocal.findByCaseNum(200000001L);
        // Create and set up EstimatePlan
        EstimatePlan plan = new EstimatePlan();
        plan.setCreatedDate(new Date());
        plan.setCreatedBy("s2026080");
        //  plan.setDaysToComplete(category.getPredefinedDays());
        plan.setDepositAmount(100.00);
        plan.setDepositPercentage(12.0);
        plan.setExternalResource(100);
        plan.setGrandTotal(100.00);
        plan.setResourceType(ResourceType.PRIMARY_RESOURCE);
        // plan.setStatus(Status.ESTIMATE_INPROGRESS);
        plan.setSubtotalFee(150.00);
        plan.synchronizeWithActivities(activityServiceLocal);
//        plan.setChargeOutRateCategory(category); // use the category you saved earlier

        // Set EstimatePlan on atr16
        atr16.addEstimatePlan(plan);

        atrApplicationServiceLocal.save(atr16);
    }

    @Test
    @Ignore
    public void testP() {
        ATRApplication atr16 = atrApplicationServiceLocal.findByCaseNum(200000001L);
        // EstimatePlan plan = atr16.getCurrentEstimatePlan();

        atrApplicationServiceLocal.save(atr16);

        // plan.setDepositPercentage(25.0);
        atrApplicationServiceLocal.save(atr16);
    }

    @Test
//    @Ignore
    public void testQ() {
        UserRole admin = userRoleServiceLocal.findByDescription("Administrator");
        UserRole resource = userRoleServiceLocal.findByDescription("Resource");
        UserRole manager = userRoleServiceLocal.findByDescription("Manager");
        UserRole seniorManager = userRoleServiceLocal.findByDescription("Senior Manager");
        UserRole applicant = userRoleServiceLocal.findByDescription("Applicant");
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_ALLOCATED_MANAGER, manager, Arrays.asList(resource, admin), "ATR allocated to manager", "@ApplicantName: @ARRefNum was allocated to you on @CreatedDate as the Manager."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_ALLOCATED_RESOURCE, resource, Arrays.asList(manager, admin), "ATR allocated to primary resource", "@ApplicantName: @ARRefNum was allocated to you on @CreatedDate as the primary resource."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_ASSIGNED_ADDITIONAL_RESOURCES, resource, Arrays.asList(manager, admin), "ATR allocated", "@ApplicantName: @ARRefNum was assigned to you on @CreatedDate as the additional resource."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_INPROGRESS, applicant, Arrays.asList(admin), "ATR estimation in progress", "Dear Applicant\n"
                + "\n"
                + "The ATR application for @ApplicantName: @ARRefNum has been accepted and an estimate of the cost of the application will be published within 5 business days unlessadditional information is required before the estimate is published. The ATR team members assigned to your application will engage with you if additional information is required.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.NEW_APPLICATION_REJECTED, applicant, Arrays.asList(manager, admin), "ATR application rejected", "Dear Applicant\n"
                + "\n"
                + "Please note that @ApplicantName: @ARRefNum that was submitted on @CreatedDate has been rejected by the ATR unit. Please contact the ATR unit if you have not been provided with the reasons for the rejection prior to this notification.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_WITHDRAWN_BY_ATR_UNIT, applicant, Arrays.asList(manager, admin), "ATR application withdrawn", "Dear Applicant\n"
                + "\n"
                + "Kindly note that the ATR application for @ApplicantName: @ARRefNum was withdrawn on @CreatedDate on your behalf. A notification will be emailed as soon as the invoice has been generated and published on the ATR system.\n"
                + "\n"
                + "Kindly note that all outstanding amounts are payable within 30 days from the date of the invoice.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_WITHDRAWN, admin, Arrays.asList(manager, resource, seniorManager), "ATR application withdrawn", "@ApplicantName: @ARRefNum was withdrawn on @CreatedDate. Please begin with the Quality Review process for this application."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_DOCUMENTS_UPLOADED, manager, Arrays.asList(resource, admin), "ATR documents uploaded", "Documents were uploaded for @ApplicantName: @ARRefNum on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_ONHOLD, manager, Arrays.asList(seniorManager, admin, resource), "On hold", "@ApplicantName: @ARRefNum has been placed on hold on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_REINSTATED, manager, Arrays.asList(seniorManager, admin, resource), "Reinstated", "@ApplicantName: @ARRefNum has been taken off hold on @CreatedDate and is back in progress."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.INVOICE_ISSUED, applicant, Arrays.asList(resource, manager, admin), "ATR invoice issued", "Dear Applicant\n"
                + "\n"
                + "An invoice has been issued for @ApplicantName: @ARRefNum on @CreatedDate and is available on the ATR system.\n"
                + "\n"
                + "Kindly note that all outstanding amounts are payable within 30 days from the date of the invoice. Interest will be levied at the prescribed rates on any outstanding amount not paid within 30 days and will accrue monthly in arrears.\n"
                + "\n"
                + "Please use the ATR application reference number as the beneficiary reference for the EFT and email the proof of payment to the ATR unit.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_PAYMENT_REMINDER, applicant, Arrays.asList(admin), "ATR payment reminder", "Dear Applicant\n"
                + "\n"
                + "The ATR account for @ApplicantName: @ARRefNum is overdue. \n"
                + "\n"
                + "Please note that interest on the account accrues monthly in arrears. Arrear accounts will be referred for debt collection and theapplicant will be held liable for the recovery costs and legal fees incurred as indicated in the Letter of Engagement.\n"
                + "\n"
                + "No new ATR applications will be accepted from applicants or their representatives while previous ATR accounts remain outstanding.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_ESTIMATE_IN_PROGRESS, manager, Arrays.asList(resource, admin), "Case in progress", "The estimation for @ApplicantName: @ARRefNum was accepted, the signed Letter of Engagement and the deposit has been received. The application is ‘case in progress’ from @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_ACCEPTED, manager, Arrays.asList(resource, admin), "Draft ruling accepted", "The applicant has accepted the draft ruling for @ApplicantName: @ARRefNum on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_FEEDBACK_RECEIVED, manager, Arrays.asList(resource, admin), "Draft ruling feedback received", "The applicant has selected the feedback option on the draft ruling for @ApplicantName: @ARRefNum on @CreatedDate. Please ensure feedback has been received before proceeding."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_REQUIRE_APPROVAL, manager, Arrays.asList(admin, seniorManager), "Draft ruling requires approval", "Please approve the draft ruling for @ApplicantName: @ARRefNum that was uploaded on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_PUBLISHED, applicant, Arrays.asList(manager, admin, resource), "Draft ruling published", "Dear Applicant\n"
                + "\n"
                + "A draft ruling has been published for @ApplicantName: @ARRefNum on @CreatedDate for your review and comment. The next step is to consider the draft ruling for acceptance. Please click the 'Accept' button to proceed to the sanitised ruling process. If you are of the view that any errors or omissions may have been made, please click the 'Feedback' button and provide the ATR team members with the amendments.\n"
                + "\n"
                + "Should you wish to withdraw the ATR application, please email ATRinfo@sars.gov.za as well as the ATR team members assigned to your ruling application.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_REMINDER, applicant, Arrays.asList(resource, admin, manager), "Draft ruling reminder", "Dear Applicant\n"
                + "\n"
                + "No action has been received on the draft ruling that was published on @CreatedDate for @ApplicantName: @ARRefNum. For the application to progress to the sanitised ruling stage, your action on eFiling is required within 10 working days. If no response has been received within the indicated time frame, the ATR unit will presume the draft ruling is accepted and will be actioned on the ATR system accordingly.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_DRAFT_RULING_WITHDRAWN, manager, Arrays.asList(seniorManager, admin, resource), "ATR application withdrawn at draft stage", "The applicant has decided on @CreatedDate to withdraw @ApplicantName: @ARRefNum at draft ruling stage."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_PAYMENT_REMINDER, applicant, Arrays.asList(resource, admin, manager), "ATR estimate payment reminder", "Dear Applicant\n"
                + "\n"
                + "The estimation payment (deposit) for @ApplicantName: @ARRefNum that was accepted on @CreateDate must be paid before the application can move into “Case in Progress” stage.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_ACCEPTED_PENDING_DOCUMENTATION, admin, Arrays.asList(), "Estimate accepted and paid - action required", "The estimate for @ApplicantName: @ARRefNum was accepted and R@PaymentAmt was paid on @CreatedDate. Verify that the signed Letter of Engagement has been received and confirm receipt thereof on the ATR system. Please email the applicant acknowledging receipt of thepayment and confirming that the amount has been allocated on the ATR system."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_EXPIRED, applicant, Arrays.asList(resource, admin, manager), "ATR estimate expired", "Dear Applicant\n"
                + "\n"
                + "The estimate published for @ApplicantName: @ARRefNum expired on @CreatedDate. Please contact the ATR team members for an extension should you still wish to accept the estimation published.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_EXPIRED, admin, Arrays.asList(), "ATR estimate expired - action required", "The estimate for @ApplicantName: @ARRefNum expired on @CreatedDate. Please confirm with the Manager if the estimate should be extended."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_REJECTED, applicant, Arrays.asList(resource, admin, manager), "ATR estimate rejected", "Dear Applicant\n"
                + "\n"
                + "You have selected to reject the estimate for @ApplicantName: @ARRefNum. Please contact the ATR team members regarding this rejection.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. \n"
                + "Should Status Subject Send to Old content information Amended content information you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_REQUIRE_APPROVAL, manager, Arrays.asList(seniorManager, admin), "Estimate requires approval", "The estimate for @ApplicantName: @ARRefNum requires your approval."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_PUBLISHED, applicant, Arrays.asList(resource, admin, manager), "ATR estimate published", "Dear Applicant\n"
                + "\n"
                + "An estimate indicating the fee range has been published for @ApplicantName: @ARRefNum on @CreatedDate. Please access the ATR system on eFiling to either accept or reject the estimated cost of the ruling application.\n"
                + "\n"
                + "Once the estimate is accepted online, the signed Letter of Engagement and the estimation payment (deposit) is received the application will progress to “Case in Progress” stage. Kindly note that advancing to “Case in Progress” stage would complete the administration process for the application submitted. The number of days indicated as the estimated time for completion will only start running once the application progresses to “Case in Progress”.\n"
                + "\n"
                + "The cost estimate is valid for 15 business days, after which it will expire. Should you require additional time, please contact the ATR Team members.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ESTIMATION_REMINDER, applicant, Arrays.asList(resource, admin, manager), "ATR estimate reminder", "Dear Applicant\n"
                + "\n"
                + "The estimate for @ApplicantName: @ARRefNum will expire in 5 business days. Please access the ATR system on eFiling to either accept or reject the published estimate.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_EXPIRED, applicant, Arrays.asList(admin), "ATR application expired", "Dear Applicant\n"
                + "\n"
                + "The ATR application for @ApplicantName: @ARRefNum has expired due to the application fee not being paid within 10 business days of the application date. A new ATR application will need to be submitted should you wish to continue with the ruling request. Please do not re-submit the expiredapplication that was submitted previously on eFiling.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.REVISED_ESTIMATION_EXPIRED, applicant, Arrays.asList(resource, admin, manager), "ATR revised estimate expired", "Dear Applicant\n"
                + "\n"
                + "The revised estimate published for @ApplicantName: @ARRefNum expired on @Created Date.\n"
                + "\n"
                + "Please contact the ATR team members should you still wish to accept the revised estimate.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.REVISED_ESTIMATION_REJECTED, applicant, Arrays.asList(resource, admin, manager), "ATR revised estimate rejected", "Dear Applicant\n"
                + "\n"
                + "The revised estimate for @ApplicantName: @ARRefNum has been recorded as rejected.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.REVISED_ESTIMATION_PUBLISHED, applicant, Arrays.asList(resource, admin, manager), "ATR revised estimate published", "Dear Applicant\n"
                + "\n"
                + "A revised estimate has been published for @ApplicantName: @ARRefNum on @CreatedDate. Please access the ATR system on eFiling to either accept or reject the revised estimate.\n"
                + "\n"
                + "The ATR process will only progress to “Case in Progress” stage once the revised estimate has been accepted online and the signed revised Letter of Engagement has been received.\n"
                + "\n"
                + "The number of days indicated as the estimated time for completion will only start running once the application progresses to “Case in Progress”.Please note that the revised estimate is valid for 15 business days, after which it will expire. Should you require additional time, please contact the ATR team members.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.REVISED_ESTIMATION_REMINDER, applicant, Arrays.asList(resource, admin, manager), "ATR revised estimate reminder", "Dear Applicant\n"
                + "\n"
                + "The revised estimate for @ApplicantName: @ARRefNum will expire in 5 business days. Please access the ATR system on eFiling to either accept or reject the published revised estimate.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_PAYMENT_REMINDER, admin, Arrays.asList(), "ATR payment received - action required", "A payment of @PaymentAmt has been received for @ApplicantName: @ARRefNum on @CreatedDate. Please email the applicant acknowledging receipt of the payment and confirming that the amount has been receipted on the ATR system."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.RULING_UPLOADED_PENDING_OUTSTANDING_AMOUNT, applicant, Arrays.asList(resource, admin, manager), "ATR ruling letter uploaded", "Dear Applicant\n"
                + "\n"
                + "The ruling letter has been uploaded for @ApplicantName: @ARRefNum. The ruling letter will only be accessible on the ATR system once the account has been settled in full and has a zero balance.\n"
                + "\n"
                + "An invoice has been issued and is available on the ATR system. Kindly note that all outstanding amounts are payable within 30 days of the invoice date. Interest will be levied at the prescribed rates on any outstanding amount not paid within 30 days and will accrue monthly in arrears.\n"
                + "\n"
                + "Please use the ATR application reference number as the beneficiary reference for the EFT and email the proof of payment to the ATR unit.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.RULING_PUBLISHED_PUBLICLY, applicant, Arrays.asList(resource, admin, manager), "ATR ruling published publicly", "Dear Applicant\n"
                + "\n"
                + "The sanitised version of the ruling for @ApplicantName: @ARRefNum has been published on the SARS website and can be viewed on the Published Binding Rulings page unless you have been informed that a sanitised version will not be published.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. \n"
                + "Should Status Subject Send to Old content information Amended content information you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SANITISED_RULING_ACCEPTED, manager, Arrays.asList(resource, admin), "Sanitised ruling accepted", "The sanitised ruling for @ApplicantName: @ARRefNum was accepted on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPROVE_CHARGE_SHEET, manager, Arrays.asList(seniorManager, admin), "Authorise charge sheet", "Please authorise the charge sheet for @ApplicantName: @ARRefNum created on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.AUTHORISE_RULING, seniorManager, Arrays.asList(admin), "Authorise ruling", "Please authorise the ruling for @ApplicantName: @ARRefNum uploaded on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SANITISED_RULING_FEEDBACK, manager, Arrays.asList(resource, admin), "Sanitised ruling feedback received", "The applicant has selected the feedback option on the sanitised ruling for @ApplicantName: @ARRefNum on @CreatedDate. Please ensure feedback has been received before proceeding."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SANITISED_RULING_REQUIRES_APPROVAL, manager, Arrays.asList(seniorManager, admin), "Sanitised ruling requires approval", "Please approve the sanitised version of the ruling for @ARRefNum @ApplicantName that was uploaded on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SANITISED_RULING_PUBLISHED, applicant, Arrays.asList(resource, admin, manager), "Sanitised ruling published", "Dear Applicant\n"
                + "\n"
                + "The next step is to consider the sanitised version of the ruling. The sanitised (edited) version of the ruling has been published for @ApplicantName: @ARRefNum on @CreatedDate for your review and comment. If there are no comments and you agree with the published sanitised ruling, please click the  “Accept” button to proceed to the finalisation process.\n"
                + "\n"
                + "However, if you would like to give feedback, please click the “Feedback” button and provide the ATR team members with any comments and proposed edits for consideration.\n"
                + "\n"
                + "If it is not possible to publish the ruling in a form that does not reveal the confidential information of the applicant, a nonpublication letter will be issued in place of a sanitised ruling on the ATR system to ensure continuity of the ruling process. Please acknowledge receipt of the non-publication letter by clicking on the “Accept” button on the “Sanitised Ruling” page. Status Subject Send to Old content information Amended content information\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_SANITISED_RULING_WITHDRAWN, admin, Arrays.asList(resource, manager, seniorManager), "ATR application withdrawn at sanitised ruling stage", "The applicant has decided on @CreatedDate to withdraw @ApplicantName: @ARRefNum at sanitised ruling stage. Please begin the finalisation process."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_FEE_REMINDER, applicant, Arrays.asList(admin), "ATR application fee reminder", "Dear Applicant\n"
                + "\n"
                + "Kindly note the payment for the application fee for @ApplicantName: @ARRefNum has not been received. If the payment has not been received within 10 business days from the submission date the application will expire on eFiling and a new application will need to be submitted.\n"
                + "\n"
                + "Kindly note, this is a system-generated email.\n"
                + "Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.NEW_APPLICATION_RECEIVED, applicant, Arrays.asList(admin), "New ATR application", "Dear Applicant\n"
                + "\n"
                + "An ATR application has been received for @ApplicantName: @ARRefNum on @CreatedDate. After acceptance of the application, the ATR unit will contact you regarding the process for the application fee payment.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.NEW_APPLICATION_FEE_PAID, admin, Arrays.asList(), "Application fee paid - action required", "The application fee of R@PaymentAmt was received for @ApplicantName: @ARRefNum, has been receipted on @CreatedDate. Please email the applicant acknowledging the payment and confirming that the amount has been receipted on the ATR system."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_REJECTION_REQUIRES_APPROVAL, manager, Arrays.asList(admin), "Approve application rejection", "The rejection for @ApplicantName: @ARRefNum requires your approval."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_WITHDRAWAL_REQUIRES_APPROVAL, manager, Arrays.asList(admin), "Approve application withdrawal", "The withdrawal of @ApplicantName: @ARRefNum requires your approval."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.DRAFT_RULING_ACCEPTED_BY_ATR_UNIT_REQUIRES_APPROVAL, manager, Arrays.asList(admin), "Approve acceptance of draft ruling on behalf of aplicant", "The acceptance of the draft ruling on behalf of the applicant for @ApplicantName: @ARRefNum requires your approval."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SANITISED_RULING_ACCEPTED_BY_ATR_UNIT_REQUIRES_APPROVAL, manager, Arrays.asList(admin), "Approve acceptance of draft ruling on behalf of applicant", "The acceptance of the sanitised ruling on behalf of the applicant for @ApplicantName: @ARRefNum requires your approval."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.RULING_AMEND_REQUIRES_APPROVAL, seniorManager, Arrays.asList(admin), "Authorise amended ruling", "An amended ruling for @ApplicantName: @ARRefNum has been uploaded on @CreatedDate for your authorisation."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.APPLICATION_SUBMITTED_IN_PROGRESS, applicant, Arrays.asList(resource, admin, manager), "ATR application is Case in Progress", "Dear Applicant\n"
                + "\n"
                + "The application submitted for @ApplicantName: @ARRefNum has moved into Case in Progress. The days indicated on the Letter of Engagement will take effect from today.\n"
                + "\n"
                + "Kindly note, this is a system-generated email. Please do not reply to this message. Should you require any assistance please send your query to ATRinfo@sars.gov.za."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.ZERO_ACCOUNT_REQUIRES_AUTHORISATION, seniorManager, Arrays.asList(admin), "Zero account requires your approval", "A zero account request for @ApplicantName: @ARRefNum requires your authorisation."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SYSTEM_QUERY_FROM_APPLICANT, manager, Arrays.asList(resource, admin), "System query from applicant", "System query received for @ApplicantName: @ARRefNum on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.SYSTEM_QUERY_FROM_ATR_UNIT, applicant, Arrays.asList(resource, manager, admin), "System query from ATR unit", "System query response received for @ApplicantName: @ARRefNum on @CreatedDate."));
        emailService.save(BootStrapHelper.getNotification(NotificationType.TIME_SHEET_SUBMISSION, admin, Arrays.asList(resource), "Timesheet submitted", "Timesheet submitted for @ApplicantName: @ARRefNum on @CreatedDate."));

    }

    @Test
//    @Ignore
    public void testR() {
        // Transfer Duty Act 40 of 1949
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "1", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "2", "Imposition of transfer duty"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "3", "By whom, when and to whom duty payable"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "3A", "Sharia compliant financing arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "4", "Penalty and interest on late payment of duty"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "5", "Value of property on which duty payable"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "6", "Certain payments to be added to the consideration payable in respect of property"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "7", "Certain payments excluded from the consideration payable in respect of property"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "8", "Valuation of consideration payable by way of rent, royalty, share of profits or any other periodical payment, or otherwise than in cash"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "9", "Exemptions from duty"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "10", "Administration of Act"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "11", "Powers of the Commissioner"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "12", "Registration of acquisition of property prohibited where duty not paid"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "13", "Commissioner to recover amount of duty underpaid"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "14", "Declarations to be furnished to Commissioner"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "15", "Records of certain sales of property to be kept"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "16", "Persons who acquire property on behalf of others shall disclose names of their principals"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "18", "Objection and Appeal procedures"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.TRANSFER_DUTY_ACT, "20B", "Transactions, operations, schemes or understanding for obtaining undue tax benefits"));

        // Securities Transfer Tax Act 25 of 2007
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "1", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "2", "Imposition of tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "3", "Purchase of listed securities through or from member"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "4", "Transfer of listed securities effected by participant"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "5", "Other transfers of listed securities"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "6", "Transfer of unlisted securities"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "7", "Tax recoverable from person to whom security is transferred"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "8", "Exemptions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "8A", "Sharia compliant financing arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "9", "Schemes for obtaining undue tax benefits"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "10", "Effect of certain exemptions from taxes"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "11", "Repeal of Act"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.SECURITIES_TRANSFER_TAX_ACT, "12", "Short title and commencement"));

        // Value Added Tax Act 89 of 1991
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "1", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "2", "Financial services"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "3", "Determination of “open market value”"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "4", "Administration of Act"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "5", "Exercise of powers and performance of duties"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "7", "Imposition of value-added tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "8", "Certain supplies of goods or services deemed to be made or not made"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "8A", "Sharia compliant financing arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "9", "Time of supply"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "10", "Value of supply of goods or services"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "11", "Zero rating"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "12", "Exempt supplies"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "13", "Collection of tax on importation of goods, determination of value thereof and exemptions from tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "14", "Collection of value-added tax on imported services, determination of value thereof and exemptions from tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "15", "Accounting basis"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "16", "Calculation of tax payable"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "17", "Permissible deductions in respect of input tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "18", "Change in use adjustments"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "18A", "Adjustments in consequence of acquisition of going concern wholly or partly for purposes other than making taxable supplies"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "18B", "Temporary letting of residential fixed property"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "18C", "Adjustments for leasehold improvements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "18D", "Temporary letting of residential property"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "19", "Goods or services acquired before incorporation"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "20", "Tax invoices"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "21", "Credit and debit notes"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "22", "Irrecoverable debts"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "23", "Registration of persons making supplies in the course of enterprises"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "24", "Cancellation of registration"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "25", "Vendor to notify change of status"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "26", "Liabilities not affected by person ceasing to be vendor"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "27", "Tax period"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "28", "Returns and payments of tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "29", "Special records and payments"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "31", "Assessments"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "32", "Objections to certain decisions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "36", "Payment of tax pending objection and appeal"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "38", "Manner in which tax shall be paid"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "39", "Penalty for failure to pay tax when due"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "40C", "Liability of bargaining councils or political parties for tax and limitation of refunds"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "40D", "Liability for tax and limitation of refunds in respect of National Housing Programmes"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "41", "Liability for tax in respect of certain past supplies or importations"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "41B", "VAT class ruling and VAT ruling"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "44", "Refunds"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "45", "Interest on delayed refunds"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "45A", "Calculation of interest payable under this Act"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "46", "Persons acting in a representative capacity"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "50", "Separate enterprises, branches and divisions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "50A", "Separate persons carrying on same enterprise under certain circumstances deemed to be single person."));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "51", "Bodies of persons, corporate or unincorporate (other than companies)"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "52", "Pooling arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "53", "Death or insolvency of vendor"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "54", "Agents and auctioneers"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "55", "Records"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "58", "Offences"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "61", "Recovery of tax from recipient"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "64", "Prices deemed to include tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "65", "Prices advertised or quoted to include tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "66", "Rounding-off of tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "67", "Contract price or consideration may be varied according to rate of value-added tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "67A", "Application of increased or reduced tax rate"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "67B", "Registration of motor vehicles prohibited in certain circumstances"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "68", "Tax relief allowable to certain diplomats and diplomatic and consular missions"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "72", "Decisions to overcome difficulties, anomalies or incongruities"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "73", "Schemes for obtaining undue tax benefits"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "74", "Schedules and regulations"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "75", "Tax agreements"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "78", "Transitional matters"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "78A", "Transitional matters: Turnover tax"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "79", "Amendment of section 9 of Act 40 of 1949"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "80", "Amendment of section 12 of Act 40 of 1949"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "81", "Amendment of section 23 of Act 77 of 1968"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "82", "Amendment of section 24 of Act 77 of 1968"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "83", "Amendment of Item 15 of Schedule 1 by Act 77 of 1968"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "84", "Amendment of Item 18 of Schedule 1 by Act 77 of 1968"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "85", "Repeal of laws"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "86", "Act binding on State, and effect of certain exemptions from taxes"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "86A", "Provisions relating to special economic zones"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "87", "Short title"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "Schedule 1", "Exemption: certain goods imported into the Republic"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "Schedule 2", "Zero rate: supply of goods used or consumed for agricultural, pastoral or other farming purposes and supply of goods consisting of certain foodstuffs"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "Schedule 3", "Zero rate: Supply of goods consisting of certain foodstuffs"));
        actSectionService.save(BootStrapHelper.getActSection(
                TaxAct.VAT_TAX_ACT, "Schedule 4", "Laws repealed"));

        // Income Tax Act 58 of 1962 - sections
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "1", "Interpretation"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "2", "Administration of Act"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "3", "Exercise of powers and performance of duties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "4A", "Exercise of powers and performance of duties by Minister"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "5", "Levy of normal tax and rates thereof"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6", "Normal tax rebates"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6A", "Medical scheme fees tax credit"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6B", "Additional medical expenses tax credit"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6C", "Solar energy tax credit"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6quat", "Rebate or deduction in respect of foreign taxes on income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "6quin", "Rebate in respect of foreign taxes on income from source within Republic"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7", "When income is deemed to have accrued or to have been received"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7A", "Date of receipt or accrual of antedated salaries or pensions and of certain retirement gratuities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7B", "Timing of accrual and incurral of variable remuneration"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7C", "Loan, advance or credit granted to trust by connected person"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7D", "Calculation of amount of interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7E", "Time of accrual of interest payable by SARS"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "7F", "Deduction of interest repaid to SARS"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8", "Certain amounts to be included in income or taxable income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8A", "Gains made by directors of companies or by employees in respect of rights to acquire marketable securities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8B", "Taxation of amounts derived from broad-based employee share plan"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8C", "Taxation of directors and employees on vesting of equity instruments"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8E", "Dividends derived from certain shares and equity instruments deemed to be income in relation to recipients thereof"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8EA", "Dividends on third-party backed shares deemed to be income in relation to recipients thereof"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8F", "Interest on hybrid debt instruments deemed to be dividends in specie"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8FA", "Hybrid interest deemed to be dividends in specie"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "8G", "Determination of contributed tax capital in respect of shares issued to a group company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9", "Source of income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9A", "Blocked foreign funds"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9C", "Circumstances in which certain amounts received or accrued from disposal of shares are deemed to be of a capital nature"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9D", "Net income of controlled foreign companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9H", "Change of residence, ceasing to be controlled foreign company or becoming headquarter company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9HA", "Disposal by deceased person"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9HB", "Transfer of asset between spouses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9I", "Headquarter companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9J", "Interest of non-resident persons in immovable property"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "9K", "Listing of security on exchange outside Republic"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "10", "Exemptions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "10A", "Exemption of capital element of purchased annuities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "10B", "Exemption of foreign dividends and dividends paid or declared by headquarter companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "10C", "Exemption of non-deductible element of qualifying annuities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11", "General deductions allowed in determination of taxable income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11A", "Deductions in respect of expenditure and losses incurred prior to commencement of trade"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11D", "Deductions in respect of scientific or technological research and development"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11E", "Deduction of certain expenditure incurred by sporting bodies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11F", "Deduction in respect of contributions to retirement funds"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11G", "Deduction of expenses incurred in production of interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "11sex", "Deduction of compensation for railway operating losses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12B", "Deduction in respect of certain machinery, plant, implements, utensils and articles used in farming or production of renewable energy"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12BA", "Enhanced deduction in respect of certain machinery, plant, implements, utensils and articles used in production of renewable energy"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12C", "Deduction in respect of assets used by manufacturers or hotel keepers and in respect of aircraft and ships, and in respect of assets used for storage and packing of agricultural products"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12D", "Deduction in respect of certain pipelines, transmission lines and railway lines"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12DA", "Deduction in respect of rolling stock"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12E", "Deductions in respect of small business corporations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12F", "Deduction in respect of airport and port assets"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12H", "Additional deduction in respect of learnership agreements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12I", "Additional investment and training allowances in respect of industrial policy projects"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12J", "Deductions in respect of expenditure incurred in exchange for issue of venture capital company shares"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12L", "Deduction in respect of energy efficiency savings"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12M", "Deduction of medical lump sum payments"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12N", "Deductions in respect of improvements not owned by taxpayer"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12NA", "Deductions in respect of improvements on property in respect of which government holds a right of use or occupation"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12O", "Exemption in respect of films"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12P", "Exemption of amounts received or accrued in respect of government grants"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12Q", "Exemption of income in respect of ships used in international shipping"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12R", "Special economic zones"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12S", "Deduction in respect of buildings in special economic zones"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12T", "Exemption of amounts received or accrued in respect of tax free investments"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12U", "Additional deduction in respect of roads and fences in respect of production of renewable energy"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "12V", "Deduction in respect of production of battery electric and hydrogen-powered vehicles"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13", "Deductions in respect of buildings used in a process of manufacture"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13bis", "Deductions in respect of buildings used by hotel keepers"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13ter", "Deductions in respect of residential buildings"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13quat", "Deductions in respect of erection or improvement of buildings in urban development zones"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13quin", "Deduction in respect of commercial buildings"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13sex", "Deduction in respect of certain residential units"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "13sept", "Deduction in respect of sale of low-cost residential units on loan account"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "15", "Deductions from income derived from mining operations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "15A", "Amounts to be taken into account in respect of trading stock derived from mining operations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "17A", "Expenditure incurred by a lessor of land let for farming purposes, in respect of soil erosion works"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "18A", "Deduction of donations to certain organisations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "19", "Concession or compromise in respect of a debt"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "20", "Set-off of assessed losses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "20A", "Ring-fencing of assessed losses of certain trades"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "20B", "Limitation of losses from disposal of certain assets"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "20C", "Ring-fencing of interest and royalties incurred by headquarter companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "21", "Deduction of alimony, allowance or maintenance"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "22", "Amounts to be taken into account in respect of values of trading stocks"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "22A", "Schemes of arrangement involving trading stock"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "22B", "Dividends treated as income on disposal of certain shares"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23", "Deductions not allowed in determination of taxable income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23A", "Limitation of allowances granted to lessors of certain assets"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23B", "Prohibition of double deductions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23C", "Reduction of cost or market value of certain assets"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23D", "Limitation of allowances granted in respect of certain assets"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23F", "Acquisition or disposal of trading stock"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23G", "Sale and leaseback arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23H", "Limitation of certain deductions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23I", "Prohibition of deductions in respect of certain intellectual property"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23K", "Limitation of deductions in respect of reorganisation and acquisition transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23L", "Limitation of deductions in respect of certain short-term insurance policies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23M", "Limitation of interest deductions in respect of debts owed to persons not subject to tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23N", "Limitation of interest deductions in respect of reorganisation and acquisition transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "23O", "Limitation of deductions by small, medium or micro-sized enterprises in respect of amounts received or accrued from small business funding entities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24", "Credit agreements and debtors allowance"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24A", "Transactions whereby fixed property is or company shares are exchanged for shares"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24BA", "Transactions where assets are acquired as consideration for shares issued"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24C", "Allowance in respect of future expenditure on contracts"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24D", "Deduction of certain expenditure incurred in respect of any National Key Point or specified important place or area"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24E", "Allowance in respect of future expenditure by sporting bodies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24G", "Taxable income of toll road operators"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24H", "Persons carrying on trade or business in partnership"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24I", "Gains or losses on foreign exchange transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24J", "Incurral and accrual of interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24JA", "Sharia compliant financing arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24JB", "Taxation in respect of financial assets and liabilities of certain persons"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24K", "Incurral and accrual of amounts in respect of interest rate agreements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24L", "Incurral and accrual of amounts in respect of option contracts"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24M", "Incurral and accrual of amounts in respect of assets acquired or disposed of for unquantified amount"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24N", "Incurral and accrual of amounts in respect of disposal or acquisition of equity shares"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24O", "Incurral of interest in respect of certain debts deemed to be in the production of income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "24P", "Allowance in respect of future repairs to certain ships"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25", "Taxation of deceased estates"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25A", "Determination of taxable incomes of permanently separated spouses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25B", "Taxation of trusts and beneficiaries of trusts"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25BA", "Amounts received by or accrued to certain portfolios of collective investment schemes and holders of participatory interests in portfolios"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25BB", "Taxation of REITs"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25C", "Income of insolvent estates"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25D", "Determination of taxable income in foreign currency"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "25E", "Determination of contributed tax capital in foreign currency"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "26", "Determination of taxable income derived from farming"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "26A", "Inclusion of taxable capital gain in taxable income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "26B", "Taxation of oil and gas companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "27", "Determination of taxable income of co-operative societies and companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "28", "Taxation of short-term insurance business"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "29A", "Taxation of long-term insurers"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "29B", "Mark-to-market taxation in respect of long-term insurers"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "30", "Public benefit organisations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "30A", "Recreational clubs"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "30B", "Associations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "30C", "Small business funding entities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "31", "Tax payable in respect of international transactions to be based on arm’s length principle"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "33", "Assessment of owners or charterers of ships or aircraft who are not residents of the Republic"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "35A", "Withholding of amounts from payments to non-resident sellers of immovable property"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "36", "Calculation of redemption allowance and unredeemed balance of capital expenditure in connection with mining operations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37", "Calculation of capital expenditure on sale, transfer, lease or cession of mining property"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37A", "Closure rehabilitation company or trust"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37B", "Deductions in respect of environmental expenditure"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37C", "Deductions in respect of environmental conservation and maintenance"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37D", "Allowance in respect of land conservation in respect of nature reserves or national parks"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37F", "Determination of taxable income derived by persons previously assessable under certain other laws"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "37G", "Determination of taxable income derived from small business undertakings"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "38", "Classification of companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "39", "Redetermination of company’s status"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40A", "Close corporations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40B", "Conversion of co-operative to company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40C", "Issue of shares or granting of options or rights for no consideration"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40CA", "Acquisitions of assets in exchange for shares"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40D", "Communications licence conversions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "40E", "Ceasing to be controlled foreign company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "41", "General"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "42", "Asset-for-share transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "43", "Substitutive share-for-share transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "44", "Amalgamation transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "45", "Intra-group transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "46", "Unbundling transactions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "46A", "Limitation of expenditure incurred in respect of shares held in an unbundling company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47", "Transactions relating to liquidation, winding-up and deregistration"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47A", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47B", "Imposition of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47C", "Liability for payment of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47D", "Withholding of amounts of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47E", "Payment of amounts of tax deducted or withheld"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47F", "Submission of return"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47G", "Personal liability of resident"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47J", "Currency of payments made to Commissioner"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "47K", "Notification of specified activity"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "48", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "48A", "Imposition of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "48B", "Rates"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "48C", "Transitional provisions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49A", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49B", "Levy of withholding tax on royalties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49C", "Liability for tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49D", "Exemption from withholding tax on royalties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49E", "Withholding of withholding tax on royalties by payers of royalties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49F", "Payment and recovery of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49G", "Refund of withholding tax on royalties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "49H", "Currency of payments made to Commissioner"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50A", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50B", "Levy of withholding tax on interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50C", "Liability for tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50D", "Exemption from withholding tax on interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50E", "Withholding of withholding tax on interest by payers of interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50F", "Payment and recovery of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50G", "Refund of withholding tax on interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "50H", "Currency of payments made to Commissioner"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "54", "Levy of donations tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "55", "Definitions for purposes of this Part"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "56", "Exemptions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "57", "Disposals by companies under donations at the instance of any person"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "57A", "Donations by spouses married in community of property"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "57B", "Disposal of the right to receive an asset which would otherwise have been acquired in consequence of services rendered or to be rendered"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "58", "Property disposed of under certain transactions deemed to have been disposed of under a donation"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "59", "Persons liable for the tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "60", "Payment and assessment of the tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "61", "Extension of scope of certain provisions of Act for purposes of donations tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "62", "Value of property disposed of under donations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64", "Rate of donations tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64D", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64E", "Levy of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64EA", "Liability for tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64EB", "Deemed beneficial owners of dividends"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64F", "Exemption from tax in respect of dividends other than dividends comprising distribution of assets in specie"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64FA", "Exemption from and reduction of tax in respect of dividends in specie"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64G", "Withholding of dividends tax by companies declaring and paying dividends"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64H", "Withholding of dividends tax by regulated intermediaries"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64I", "Withholding of dividends tax by insurers"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64K", "Payment and recovery of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64L", "Refund of tax in respect of dividends declared and paid by companies"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64LA", "Refund of tax in respect of dividends in specie"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64M", "Refund of tax in respect of dividends paid by regulated intermediaries"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "64N", "Rebate in respect of foreign taxes on dividends"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "66", "Notice by Commissioner requiring returns for assessment of normal tax under this Act"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "67", "Registration as taxpayer"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "68", "Income and capital gain of married persons and minor children"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "72A", "Return relating to controlled foreign company"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76A", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76B", "Purpose"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76C", "Persons eligible to apply"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76D", "Fees for advance pricing agreements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76E", "Pre-application consultation"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76F", "Application for advance pricing agreement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76G", "Amendments to advance pricing agreement application"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76H", "Withdrawal of advance pricing agreement application"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76I", "Rejection of advance pricing agreement application"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76J", "Processing of advance pricing agreement application"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76K", "Finalisation of advance pricing agreement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76L", "Compliance report"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76M", "Extension of advance pricing agreement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76N", "Termination of advance pricing agreement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76O", "Record retention"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "76P", "Procedures and guidelines"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80A", "Impermissible tax avoidance arrangements"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80B", "Tax consequences of impermissible tax avoidance"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80C", "Lack of commercial substance"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80D", "Round trip financing"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80E", "Accommodating or tax-indifferent parties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80F", "Treatment of connected persons and accommodating or tax-indifferent parties"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80G", "Presumption of purpose"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80H", "Application to steps in or parts of an arrangement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80I", "Use in the alternative"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80J", "Notice"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80K", "Interest"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "80L", "Definitions"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "88", "Payment of tax pending objection and appeal"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "89", "Appointment of day for payment of tax and interest on overdue payments"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "89bis", "Payments of employees’ tax and provisional tax and interest on overdue payments of such taxes"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "89quat", "Interest on underpayments and overpayments of provisional tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "89quin", "Calculation of interest payable under this Act"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "90", "Persons by whom normal tax payable"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "91", "Recovery of tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "102", "Refunds"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "103", "Transactions, operations or schemes for purposes of avoiding or postponing liability for or reducing amounts of taxes on income"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "107", "Regulations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "108", "Prevention of or relief from double taxation"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "111", "Repeal of laws"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "112", "Short title and commencement"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "First Schedule", "Computation of taxable income derived from pastoral, agricultural or other farming operations"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Second Schedule", "Computation of gross income derived by way of lump sum benefits"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Third Schedule", "Laws repealed"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Fourth Schedule", "Amounts to be deducted or withheld by employers and provisional payments in respect of normal tax"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Sixth Schedule", "Determination of turnover tax payable by micro businesses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Seventh Schedule", "Benefits or advantages derived by reason of employment or the holding of any office"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Eighth Schedule", "Determination of taxable capital gains and assessed capital losses"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Ninth Schedule", "Public benefit activities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Tenth Schedule", "Oil and gas activities"));
        actSectionService.save(BootStrapHelper.getActSection(TaxAct.INCOME_TAX_ACT, "Eleventh Schedule", "Government grants exempt from normal tax"));

    }
}
