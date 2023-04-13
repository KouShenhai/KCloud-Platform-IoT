package org.laokou.common.core.utils;
import java.lang.reflect.Array;
import java.util.Comparator;

public class ArrayUtil {

    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Object array) {
        if (array != null) {
            if (isArray(array)) {
                return 0 == Array.getLength(array);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    public static int max(int... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            int max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }
    public static short max(short... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            short max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static char max(char... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            char max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static byte max(byte... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            byte max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static double max(double... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            double max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static float max(float... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            float max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static long max(long... numberArray) {
        if (isEmpty(numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            long max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (max < numberArray[i]) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }
    public static <T extends Comparable<? super T>> T max(T[] numberArray, Comparator<T> comparator) {
        if (isEmpty((Object[])numberArray)) {
            throw new IllegalArgumentException("Number array must not empty !");
        } else {
            T max = numberArray[0];

            for(int i = 1; i < numberArray.length; ++i) {
                if (compare(max, numberArray[i], comparator) < 0) {
                    max = numberArray[i];
                }
            }

            return max;
        }
    }

    public static <T extends Comparable<? super T>> T max(T[] numberArray) {
        return max(numberArray, null);
    }

    public static <T> int compare(T c1, T c2, Comparator<T> comparator) {
        return null == comparator ? compare((Comparable)c1, (Comparable)c2) : comparator.compare(c1, c2);
    }

    public static <T extends Comparable<? super T>> int compare(T c1, T c2) {
        return compare(c1, c2, false);
    }
    public static <T extends Comparable<? super T>> int compare(T c1, T c2, boolean isNullGreater) {
        if (c1 == c2) {
            return 0;
        } else if (c1 == null) {
            return isNullGreater ? 1 : -1;
        } else if (c2 == null) {
            return isNullGreater ? -1 : 1;
        } else {
            return c1.compareTo(c2);
        }
    }
}
