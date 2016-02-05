package bidder_service;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.Map;

@Controller
public class BidderAgent {
    Jedis jedis = new Jedis("localhost");

    /**
     * Calculate click through rate for given features and categories based on historical data.
     * @param featureCategoryPairing Map with categories as keys and features as values.
     * @return Calculated CTR.
     */
    public double calculateCTR(Map<String, String> featureCategoryPairing) {
        double categorySum = featureCategoryPairing.entrySet().stream()
                // create keys for database
                .map((entry) -> String.format("%s=%s", entry.getKey(), entry.getValue()))
                // get values from database
                .mapToDouble((databaseKey) -> {
                    String databaseValue = jedis.hget("model", databaseKey);
                    if (!StringUtils.isEmpty(databaseValue)) {
                        return Double.parseDouble(databaseValue);
                    } else {
                        return 0d;
                    }
                })
                .sum();

        double biasValue = Double.parseDouble(jedis.hget("model", "bias"));
        double result = sigmoid(biasValue + categorySum);
        return result;
    }

    public static double sigmoid(double x) {
        return (1 / (1 + Math.pow(Math.E, (-1 * x))));
    }
}
