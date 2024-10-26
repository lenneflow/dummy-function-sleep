package de.lenneflow.dummyfunctionsleep.controller;

import de.lenneflow.dummyfunctionsleep.dto.FunctionPayload;
import de.lenneflow.dummyfunctionsleep.enums.RunStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dummy")
@EnableAsync
public class FunctionController {

    private static final Logger logger = LoggerFactory.getLogger(FunctionController.class);

    final RestTemplate restTemplate;

    public FunctionController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/sleep")
    @Async
    public void sleep(@RequestBody FunctionPayload functionPayload){
        String callBackUrl = functionPayload.getCallBackUrl();
        try {
            String keySleepTime = "sleepTimeInSeconds";
            int sleepTime = (int) functionPayload.getInputData().get(keySleepTime);
            logger.info("Sleeping for " + sleepTime + " seconds");
            int sleepTimeInMillis = sleepTime * 1000;
            Thread.sleep(sleepTimeInMillis);
            logger.info("finished sleeping for " + sleepTime + " seconds");
            functionPayload.setRunStatus(RunStatus.COMPLETED);
            Map<String, Object> output = new HashMap<>();
            output.put("sleepTimeInMillis" , sleepTimeInMillis);
            output.put("sleepTimeInSeconds", sleepTime);
            functionPayload.setOutputData(output);
            restTemplate.postForObject(callBackUrl, functionPayload, Void.class);
        }catch (Exception e){
            logger.error(e.getMessage());
            functionPayload.setRunStatus(RunStatus.FAILED);
            functionPayload.setFailureReason(e.getLocalizedMessage());
            restTemplate.postForObject(callBackUrl, functionPayload, Void.class);
        }
    }
}
