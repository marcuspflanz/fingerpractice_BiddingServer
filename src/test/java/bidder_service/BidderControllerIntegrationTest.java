package bidder_service;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.Assert.assertEquals;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(Application.class)
@WebIntegrationTest
public final class BidderControllerIntegrationTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void createAndGetTransaction() throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get("src/test/java/bidder_service/validRequestBody01.json"));
        String validContent = new String(encoded, StandardCharsets.UTF_8);
        int scaleToMatchUpTo = 8;
        RoundingMode roundingMode = RoundingMode.HALF_UP;
        BigDecimal validResult = new BigDecimal("0.0016306374750820983").setScale(scaleToMatchUpTo, roundingMode);

        MvcResult result = mvc.perform(post("/")
                .content(validContent)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        BigDecimal contentNumber = new BigDecimal(content).setScale(scaleToMatchUpTo, roundingMode);
        assertEquals(validResult, contentNumber);
    }
}

