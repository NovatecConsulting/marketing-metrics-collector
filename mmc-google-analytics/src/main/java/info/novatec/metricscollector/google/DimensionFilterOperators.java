package info.novatec.metricscollector.google;

/**
 * This enum supports the filter operators defined by Google Analytics Reporting API.
 */
public enum DimensionFilterOperators {
    EQUAL("EQUAL"),
    REGEXP("REGEXP"),
    EXACT("EXACT"),
    NOT("NOT"),
    BEGINS_WITH("BEGINS_WITH"),
    ENDS_WITH("ENDS_WITH");

    private String operatorName;

    DimensionFilterOperators(String operatorName) {
        this.operatorName = operatorName;
    }

    @Override
    public String toString() {
        return operatorName;
    }
}
