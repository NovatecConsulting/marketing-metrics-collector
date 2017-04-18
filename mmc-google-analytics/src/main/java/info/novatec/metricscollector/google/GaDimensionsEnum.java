package info.novatec.metricscollector.google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This enum supports the dimensions defined by Google Analytics for quering the GA Reporting API based on dimensions.
 * When a new dimension is need it has to be added to the enum.
 */
public enum GaDimensionsEnum {
    GA_HOST_NAME("ga:hostname"),
    GA_PAGE_PATH("ga:pagePath"),
    GA_PAGE_TITLE("ga:pageTitle");

    private String dimensionName;

    GaDimensionsEnum(String dimensionName) {
        this.dimensionName = dimensionName;
    }

    @Override
    public String toString() {
        return dimensionName;
    }

    public List<GaDimensionsEnum> getDimensionsAsList() {
        return new ArrayList<>(Arrays.asList(GaDimensionsEnum.values()));
    }
}
