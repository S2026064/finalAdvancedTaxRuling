package za.gov.sars.atr.mb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.PrimeFaces;
import org.springframework.beans.factory.annotation.Autowired;
import za.gov.sars.common.ActivityCategory;
import za.gov.sars.common.ChargeCategory;
import static za.gov.sars.common.ChargeCategory.COMPLEX;
import static za.gov.sars.common.ChargeCategory.EXTRAORDINARY;
import static za.gov.sars.common.ChargeCategory.INVOLVED;
import static za.gov.sars.common.ChargeCategory.STANDARD;
import static za.gov.sars.common.ChargeCategory.URGENT_APPLICATION;
import za.gov.sars.common.EstimatePlanStatus;
import za.gov.sars.common.EstimatePlanType;
import za.gov.sars.common.ResponseOption;
import za.gov.sars.domain.ATRApplication;
import za.gov.sars.domain.Activity;
import za.gov.sars.domain.ChargeOutRateCategory;
import za.gov.sars.domain.EstimatePlan;
import za.gov.sars.domain.EstimateRecord;
import za.gov.sars.domain.service.ActivityServiceLocal;
import za.gov.sars.service.AtrApplicationServiceLocal;
import za.gov.sars.service.ChargeOutRateCategoryServiceLocal;
import za.gov.sars.service.EstimatePlanServiceLocal;

/**
 *
 * @author S2026015
 */
@ManagedBean
@ViewScoped
public class EstimateBean extends BaseBean<EstimatePlan> {

    @Autowired
    private AtrApplicationServiceLocal atrApplicationService;
    @Autowired
    private ChargeOutRateCategoryServiceLocal chargeOutRateCategoryService;
    @Autowired
    private ActivityServiceLocal activityService;
    @Autowired
    private EstimatePlanServiceLocal estimatePlanService;

    private ATRApplication application;
    private List<ChargeOutRateCategory> chargeOutRateCategories;
    private boolean isUrgentOrExtraordinary;
    private List<SelectItem> responsesList = new ArrayList<>();
    private String selectedComplexityLevel;
    private ChargeOutRateCategory category;
    private List<Activity> activities = new ArrayList<>();
    private EstimatePlan existingEstimatePlan;

    @PostConstruct
    public void init() {
        Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        EstimatePlan estimatePlan = (EstimatePlan) sessionMap.get("estimateKey");
        sessionMap.remove("estimateKey");
        Long applicationId = (Long) sessionMap.get("applicationKey");
        sessionMap.remove("applicationKey");
        if (applicationId != null) {
            setApplication(atrApplicationService.findById(applicationId));
        }
        chargeOutRateCategories = chargeOutRateCategoryService.findAll();
        activities = activityService.findAll();
        Arrays.asList(ResponseOption.values()).stream()
                .forEach(responseOption -> {
                    responsesList.add(new SelectItem(responseOption, responseOption.toString()));
                });
        if (estimatePlan != null) {
            estimatePlan.setUpdatedBy(getActiveUser().getSid());
            estimatePlan.setUpdatedDate(new Date());
        } else {
            estimatePlan = new EstimatePlan();
            estimatePlan.setCreatedBy(getActiveUser().getSid());
            estimatePlan.setCreatedDate(new Date());
            existingEstimatePlan = estimatePlanService.findByPlanTypeAndAtrApplication(EstimatePlanType.ACTIVE, application);
            if (application.getEstimationPlans().isEmpty()) {
                estimatePlan.setPlanType(EstimatePlanType.ACTIVE);
            } else if (existingEstimatePlan != null) {

                existingEstimatePlan.setPlanType(EstimatePlanType.NOT_ACTIVE);
                estimatePlan.setPlanType(EstimatePlanType.ACTIVE);
            }

        }
        estimatePlan.synchronizeWithActivities(activityService);
        getItemsGroupedByCategory(estimatePlan);
        addEntity(estimatePlan);
    }

    public Map<ActivityCategory, List<EstimateRecord>> getItemsGroupedByCategory(EstimatePlan plan) {
        return plan.getEstimateRecords().stream()
                .collect(Collectors.groupingBy(
                        EstimateRecord::getActivityCategory,
                        TreeMap::new, // Sorts by category enum order
                        Collectors.toList()
                ));
    }

    public void onChargeCategoryListener() {

        EstimatePlan plan = entity;

        // Reset simple values
        plan.setTotalHours(0.0);
        plan.setSubtotalFee(0.0);
        plan.setGrandTotal(0.0);
        plan.setTravelExp(0.0);
        plan.setExternalResource(0.0);
        plan.setDepositAmount(0.0);
        plan.setDaysToComplete(null);

        // Reset all items
        if (plan.getEstimateRecords() != null) {
            for (EstimateRecord item : plan.getEstimateRecords()) {
                item.setConsultantHours(0.0);
                item.setManagerHours(0.0);
                item.setTotalHours(0.0);
                item.setTotalAmount(0.0);
                item.setHourlyRate(getEntity().getChargeOutRateCategory().getHourlyRate());
            }
        }

        // Add message
        int daysToComplete = getEntity().getChargeOutRateCategory().getPredefinedDays();

        switch (getEntity().getChargeOutRateCategory().getChargeCategory()) {
            case STANDARD:
                Double depositPercent = getEntity().getChargeOutRateCategory().getDepositPercentage();
                Double maxRangeFee = getEntity().getChargeOutRateCategory().getFeeRangeMax();
                getEntity().setDepositAmount(depositPercent * maxRangeFee);
                getEntity().setDaysToComplete(getEntity().getChargeOutRateCategory().getPredefinedDays());

                break;
            case INVOLVED:
                Double invodepositPercent = getEntity().getChargeOutRateCategory().getDepositPercentage();
                Double involvedmaxRangeFee = getEntity().getChargeOutRateCategory().getFeeRangeMax();
                getEntity().setDepositAmount(invodepositPercent * involvedmaxRangeFee);
                getEntity().setDaysToComplete(getEntity().getChargeOutRateCategory().getPredefinedDays());

                break;
            case COMPLEX:
                Double compdepositPercent = getEntity().getChargeOutRateCategory().getDepositPercentage();
                Double complexmaxRangeFee = getEntity().getChargeOutRateCategory().getFeeRangeMax();
                getEntity().setDepositAmount(compdepositPercent * complexmaxRangeFee);
                getEntity().setDaysToComplete(getEntity().getChargeOutRateCategory().getPredefinedDays());

                break;
            case EXTRAORDINARY:
                Double extredepositPercent = getEntity().getChargeOutRateCategory().getDepositPercentage();
                getEntity().setDepositAmount(extredepositPercent * getEntity().getGrandTotal());
                getEntity().setDaysToComplete(getEntity().getChargeOutRateCategory().getPredefinedDays());

                isUrgentOrExtraordinary = Boolean.TRUE;

                break;
            case URGENT_APPLICATION:
                Double urgdepositPercent = getEntity().getChargeOutRateCategory().getDepositPercentage();
                getEntity().setDepositAmount(urgdepositPercent * getEntity().getGrandTotal());
                getEntity().setDaysToComplete(getEntity().getChargeOutRateCategory().getPredefinedDays());
                isUrgentOrExtraordinary = Boolean.TRUE;

                break;
            default:
                break;
        }
    }

    public void onKeyUpListener() {
        double totalConsultantHours = getEntity().getEstimateRecords().stream()
                .mapToDouble(item -> Optional.ofNullable(item.getConsultantHours()).orElse(0.0))
                .sum();

        double totalManagerHours = getEntity().getEstimateRecords().stream()
                .mapToDouble(item -> Optional.ofNullable(item.getManagerHours()).orElse(0.0))
                .sum();

        double combinedTotal = totalConsultantHours + totalManagerHours;
        getEntity().setTotalHours(combinedTotal);

//      
        double totalBillableHours = getEntity().getEstimateRecords().stream()
                .filter(item -> "Billable".equalsIgnoreCase(item.getActivityType().toString())) // Only Billable items
                .mapToDouble(item
                        -> Optional.ofNullable(item.getManagerHours()).orElse(0.0)
                + Optional.ofNullable(item.getConsultantHours()).orElse(0.0)
                )
                .sum();
        getEntity().setTotalHours(totalBillableHours);

        double combinedHourTotal = totalBillableHours * getEntity().getChargeOutRateCategory().getHourlyRate();
        getEntity().setSubtotalFee(combinedHourTotal);

        double thisGrandTotal = getEntity().getEstimateRecords().stream()
                .mapToDouble(item -> Optional.ofNullable(item.getTotalAmount()).orElse(0.0))
                .sum();
        double travelExp = getEntity().getTravelExp();
        double otherExp = getEntity().getExternalResource();
        double totalSum = thisGrandTotal + travelExp + otherExp;
        getEntity().setGrandTotal(totalSum);

        if (!(getEntity().getChargeOutRateCategory().getChargeCategory().equals(ChargeCategory.EXTRAORDINARY) || getEntity().getChargeOutRateCategory().getChargeCategory().equals(ChargeCategory.URGENT_APPLICATION))) {
            getEntity().setDepositPercentage(getEntity().getChargeOutRateCategory().getDepositPercentage());
            getEntity().setDepositAmount(getEntity().getChargeOutRateCategory().getFeeRangeMax() * getEntity().getDepositPercentage());
        } else {
            this.category = getEntity().getChargeOutRateCategory();
            getEntity().setDepositPercentage(getEntity().getChargeOutRateCategory().getDepositPercentage());
            getEntity().getChargeOutRateCategory().setFeeRangeMax(totalSum);
            getEntity().setDepositAmount(totalSum * getEntity().getDepositPercentage());

        }
    }

    public void cancelEstimateFromDialog(EstimatePlan estimatePlan) {
        PrimeFaces.current().dialog().closeDynamic(this);
    }

    public void selectEstimatePlanFromDialog(EstimatePlan estimatePlan) {

        if (this.getErrorCollectionMsg().isEmpty()) {
            PrimeFaces.current().dialog().closeDynamic(estimatePlan);
            //atrApplicationService.update(estimatePlan.getAtrApplication());

        } else {
            Iterator<String> iterator = this.getErrorCollectionMsg().iterator();
            while (iterator.hasNext()) {
                addErrorMessage(iterator.next());

            }
            getErrorCollectionMsg().clear();
        }

    }

    public void approveEstimatePlanFromDialog(EstimatePlan estimatePlan) {

        if (this.getErrorCollectionMsg().isEmpty()) {
            //We need to update status to approve status
            estimatePlan.setStatus(EstimatePlanStatus.ESTIMATE_PUBLISHED);
            PrimeFaces.current().dialog().closeDynamic(estimatePlan);

        } else {
            Iterator<String> iterator = this.getErrorCollectionMsg().iterator();
            while (iterator.hasNext()) {
                addErrorMessage(iterator.next());

            }
            getErrorCollectionMsg().clear();
        }

    }

    public void rejectEstimatePlanFromDialog(EstimatePlan estimatePlan) {

        if (this.getErrorCollectionMsg().isEmpty()) {
            //We need to update status to reject status
            estimatePlan.setStatus(EstimatePlanStatus.ESTIMATE_REJECTED);
            PrimeFaces.current().dialog().closeDynamic(estimatePlan);

        } else {
            Iterator<String> iterator = this.getErrorCollectionMsg().iterator();
            while (iterator.hasNext()) {
                addErrorMessage(iterator.next());

            }
            getErrorCollectionMsg().clear();
        }

    }

    public void cancelEstimatePlanFromDialog() {
        PrimeFaces.current().dialog().closeDynamic(null);
    }

    public ATRApplication getApplication() {
        return application;
    }

    public void setApplication(ATRApplication application) {
        this.application = application;
    }

    public List<ChargeOutRateCategory> getChargeOutRateCategories() {
        return chargeOutRateCategories;
    }

    public void setChargeOutRateCategories(List<ChargeOutRateCategory> chargeOutRateCategories) {
        this.chargeOutRateCategories = chargeOutRateCategories;
    }

    public boolean isIsUrgentOrExtraordinary() {
        return isUrgentOrExtraordinary;
    }

    public void setIsUrgentOrExtraordinary(boolean isUrgentOrExtraordinary) {
        this.isUrgentOrExtraordinary = isUrgentOrExtraordinary;
    }

    public List<SelectItem> getResponsesList() {
        return responsesList;
    }

    public void setResponsesList(List<SelectItem> responsesList) {
        this.responsesList = responsesList;
    }

    public String getSelectedComplexityLevel() {
        return selectedComplexityLevel;
    }

    public void setSelectedComplexityLevel(String selectedComplexityLevel) {
        this.selectedComplexityLevel = selectedComplexityLevel;
    }

    public ChargeOutRateCategory getCategory() {
        return category;
    }

    public void setCategory(ChargeOutRateCategory category) {
        this.category = category;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

}
