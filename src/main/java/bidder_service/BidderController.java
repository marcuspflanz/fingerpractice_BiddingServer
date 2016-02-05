package bidder_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST service endpoint as specified by code challenge doc.
 */
@RestController
@RequestMapping("/")
public final class BidderController {
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final class MissingDataException extends RuntimeException {
    }

    @Autowired
    BidderAgent bidderAgent;
    private ObjectMapper mapper = new ObjectMapper();

    /**
     * Create a new transaction.
     *
     * Example request:
     *
     */
    @RequestMapping(
            value = "/",
            method = RequestMethod.POST
    )
    @ResponseStatus( HttpStatus.OK )
    public double predictCTR(
            @RequestBody String categories
    ) throws Exception {
        Map<String, String> categoryCollection = mapper.readValue(categories, Map.class);
        if (categoryCollection.size() == 0) {
            throw new MissingDataException();
        }

        double result = bidderAgent.calculateCTR(categoryCollection);
        return result;
    }
}