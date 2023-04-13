package org.laokou.common.core.utils;
import org.springframework.util.Assert;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
public class NumberUtil {

    public static double add(float v1, float v2) {
        return add(Float.toString(v1), Float.toString(v2)).doubleValue();
    }

    public static double add(float v1, double v2) {
        return add(Float.toString(v1), Double.toString(v2)).doubleValue();
    }

    public static double add(double v1, float v2) {
        return add(Double.toString(v1), Float.toString(v2)).doubleValue();
    }

    public static double add(double v1, double v2) {
        return add(Double.toString(v1), Double.toString(v2)).doubleValue();
    }

    public static double add(Double v1, Double v2) {
        return add(v1, (Number)v2).doubleValue();
    }

    public static BigDecimal add(Number... values) {
        if (ArrayUtil.isEmpty(values)) {
            return BigDecimal.ZERO;
        } else {
            Number value = values[0];
            BigDecimal result = toBigDecimal(value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.add(toBigDecimal(value));
                }
            }

            return result;
        }
    }

    public static BigDecimal add(String... values) {
        if (ArrayUtil.isEmpty(values)) {
            return BigDecimal.ZERO;
        } else {
            String value = values[0];
            BigDecimal result = toBigDecimal(value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (StringUtil.isNotBlank(value)) {
                    result = result.add(toBigDecimal(value));
                }
            }

            return result;
        }
    }

    public static BigDecimal add(BigDecimal... values) {
        if (ArrayUtil.isEmpty(values)) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal value = values[0];
            BigDecimal result = toBigDecimal((Number)value);

            for(int i = 1; i < values.length; ++i) {
                value = values[i];
                if (null != value) {
                    result = result.add(value);
                }
            }

            return result;
        }
    }

    public static double mul(double v1, float v2) {
        return mul(Double.toString(v1), Float.toString(v2)).doubleValue();
    }
    public static BigDecimal mul(Number v1, Number v2) {
        return mul(v1, v2);
    }

    public static BigDecimal mul(String v1, String v2) {
        return mul(new BigDecimal(v1), new BigDecimal(v2));
    }

    public static double div(float v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(double v1, float v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(double v1, double v2, int scale) {
        return div(v1, v2, scale, RoundingMode.HALF_UP);
    }

    public static double div(float v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Float.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(double v1, float v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Float.toString(v2), scale, roundingMode).doubleValue();
    }

    public static double div(double v1, double v2, int scale, RoundingMode roundingMode) {
        return div(Double.toString(v1), Double.toString(v2), scale, roundingMode).doubleValue();
    }
    public static BigDecimal div(String v1, String v2, int scale, RoundingMode roundingMode) {
        return div(toBigDecimal(v1), toBigDecimal(v2), scale, roundingMode);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
        Assert.notNull(v2, "Divisor must be not null !");
        if (null == v1) {
            return BigDecimal.ZERO;
        } else {
            if (scale < 0) {
                scale = -scale;
            }

            return v1.divide(v2, scale, roundingMode);
        }
    }

    public static BigDecimal round(String numberStr, int scale) {
        return round(numberStr, scale, RoundingMode.HALF_UP);
    }
    public static BigDecimal round(String numberStr, int scale, RoundingMode roundingMode) {
        Assert.notNull(numberStr,"data is not null");
        if (scale < 0) {
            scale = 0;
        }

        return round(toBigDecimal(numberStr), scale, roundingMode);
    }

    public static BigDecimal round(BigDecimal number, int scale, RoundingMode roundingMode) {
        if (null == number) {
            number = BigDecimal.ZERO;
        }

        if (scale < 0) {
            scale = 0;
        }

        if (null == roundingMode) {
            roundingMode = RoundingMode.HALF_UP;
        }

        return number.setScale(scale, roundingMode);
    }

    public static boolean equals(double num1, double num2) {
        return Double.doubleToLongBits(num1) == Double.doubleToLongBits(num2);
    }

    public static boolean equals(float num1, float num2) {
        return Float.floatToIntBits(num1) == Float.floatToIntBits(num2);
    }

    public static boolean equals(long num1, long num2) {
        return num1 == num2;
    }

    public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
        if (bigNum1.equals(bigNum2)) {
            return true;
        } else if (bigNum2 != null) {
            return 0 == bigNum1.compareTo(bigNum2);
        } else {
            return false;
        }
    }

    public static boolean equals(char c1, char c2, boolean caseInsensitive) {
        if (caseInsensitive) {
            return Character.toLowerCase(c1) == Character.toLowerCase(c2);
        } else {
            return c1 == c2;
        }
    }

    public static long max(long... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static int max(int... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static short max(short... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static double max(double... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static float max(float... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static BigDecimal max(BigDecimal... numberArray) {
        return ArrayUtil.max(numberArray);
    }

    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        } else if (number instanceof BigDecimal) {
            return (BigDecimal)number;
        } else if (number instanceof Long) {
            return new BigDecimal((Long)number);
        } else if (number instanceof Integer) {
            return new BigDecimal((Integer)number);
        } else {
            return number instanceof BigInteger ? new BigDecimal((BigInteger)number) : toBigDecimal(number.toString());
        }
    }

    public static BigDecimal toBigDecimal(String numberStr) {
        if (StringUtil.isBlank(numberStr)) {
            return BigDecimal.ZERO;
        } else {
            try {
                Number number = parseNumber(numberStr);
                return number instanceof BigDecimal ? (BigDecimal)number : new BigDecimal(number.toString());
            } catch (Exception var2) {
                return new BigDecimal(numberStr);
            }
        }
    }

    public static Number parseNumber(String numberStr) throws NumberFormatException {
        if (StringUtil.startWithIgnoreCase(numberStr, "0x")) {
            return Long.parseLong(numberStr.substring(2), 16);
        } else {
            try {
                NumberFormat format = NumberFormat.getInstance();
                if (format instanceof DecimalFormat) {
                    ((DecimalFormat)format).setParseBigDecimal(true);
                }

                return format.parse(numberStr);
            } catch (ParseException var3) {
                NumberFormatException nfe = new NumberFormatException(var3.getMessage());
                nfe.initCause(var3);
                throw nfe;
            }
        }
    }
}
