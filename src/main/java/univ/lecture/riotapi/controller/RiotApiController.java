package univ.lecture.riotapi.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import univ.lecture.riotapi.model.JSONResult;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by tchi on 2017. 4. 1..
 */
@RestController
@RequestMapping("/api/v1")
@Log4j
public class RiotApiController {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${riot.api.endpoint}")
    private String riotApiEndpoint;

    @Value("${riot.api.key}")
    private String riotApiKey;

    @RequestMapping(value = "/calc/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public JSONResult queryResult(@PathVariable("name") @RequestBody String expression) throws UnsupportedEncodingException {
        final String url = "http://52.79.162.52:8080/api/v1/answer";
        final int teamId = 8; //조번호(8조) 
        double mathResult;
        
        String response = restTemplate.postForObject(url, null, String.class);
        
        //Map<String, Object> parsedMap = new JacksonJsonParser().parseMap(response);
        //parsedMap.forEach((key, value) -> log.info(String.format("key [%s] type [%s] value [%s]", key, value.getClass(), value)));
        //Map<String, Object> summonerDetail = (Map<String, Object>) parsedMap.values().toArray()[0];
        //String queriedName = (String)summonerDetail.get("msg");
        //int queriedLevel = (Integer)summonerDetail.get("summonerLevel");
        
        /* 수식을 계산 */
        CalcApp app = new CalcApp(expression);
        mathResult = app.getResult();
        
        /* 현재를 나타내는 시간값을 long형으로 반환 */
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddkkmm");
		String strTime = dateFormat.format((Calendar.getInstance()).getTime());
		long now = Long.parseLong(strTime);
		System.out.println(now);
		
		JSONResult result = new JSONResult(teamId, now, mathResult, response);

		
		
        return result;
    }
}
