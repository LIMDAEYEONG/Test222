package univ.lecture.riotapi.controller;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import univ.lecture.riotapi.model.Result;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

    @RequestMapping(value = "/summoner/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result queryResult(@PathVariable("name") String expression) throws UnsupportedEncodingException {
        final String url = riotApiEndpoint + "/summoner/by-name/" +
        		expression +
                "?api_key=" +
                riotApiKey;
        final int teamId = 8; //조번호(8조) 
        double mathResult;
        
        //String response = restTemplate.getForObject(url, String.class);
        //Map<String, Object> parsedMap = new JacksonJsonParser().parseMap(response);
        //parsedMap.forEach((key, value) -> log.info(String.format("key [%s] type [%s] value [%s]", key, value.getClass(), value)));
        //Map<String, Object> summonerDetail = (Map<String, Object>) parsedMap.values().toArray()[0];
        //String queriedName = (String)summonerDetail.get("name");
        //int queriedLevel = (Integer)summonerDetail.get("summonerLevel");
        
        /* 수식을 계산 */
        CalcApp app = new CalcApp(expression);
        mathResult = app.getResult();
        
        /* 현재를 나타내는 시간값을 long형으로 반환 */
		DateFormat dateFormat = new SimpleDateFormat("YYYYHHmmss");
		String str = dateFormat.format(System.currentTimeMillis());
		long now = Long.parseLong(str);
		
        Result result = new result(teamId, now, mathResult);

        return result;
    }
}
