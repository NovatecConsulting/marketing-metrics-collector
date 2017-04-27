package info.novatec.metricscollector.commons;

import java.lang.reflect.Field;

import org.springframework.stereotype.Component;


@Component
public class MetricsResultCheck {

    public boolean hasNullValues(Object targetObject) {
        try {
            Field[] fields = targetObject.getClass().getDeclaredFields();
            for (Field field : fields){
                field.setAccessible(true);
                if(field.get(targetObject)==null){
                    return true;
                }
            }
        }catch(IllegalAccessException e){
            //do nothing. block is never accessed
        }

        return false;
    }
}
