package pl.ljedrzynski.iparkapp.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static Double geometricSequenceSum(double a1, double q, int n) {
        BigDecimal qDec = BigDecimal.valueOf(q);
        return BigDecimal.valueOf(a1)
                .multiply(BigDecimal.ONE
                        .subtract(qDec.pow(n))
                        .divide(BigDecimal.ONE
                                .subtract(qDec), RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}