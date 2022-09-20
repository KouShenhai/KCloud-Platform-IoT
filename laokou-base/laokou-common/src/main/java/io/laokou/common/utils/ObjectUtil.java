package io.laokou.common.utils;

import lombok.experimental.UtilityClass;
import org.springframework.util.ObjectUtils;

import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class ObjectUtil extends ObjectUtils {

    @SafeVarargs
    public <T> boolean allChecked(Predicate<T> predicate, T... ts) {
        return Stream.of(ts).allMatch(predicate);
    }

    @SafeVarargs
    public <T> boolean anyChecked(Predicate<T> predicate, T... ts) {
        return Stream.of(ts).anyMatch(predicate);
    }

}
