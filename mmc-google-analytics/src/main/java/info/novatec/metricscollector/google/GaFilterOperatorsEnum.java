package info.novatec.metricscollector.google;

/**
 * This enum supports the filter operators defined by Google Analytics Reporting API.
 */
public enum GaFilterOperatorsEnum {
    EQUAL("EQUAL"),
    AND("AND"),
    REGEXP("REGEXP"),
    EXACT("EXACT");

    private String operatorName;

    GaFilterOperatorsEnum(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return operatorName;
    }
}
