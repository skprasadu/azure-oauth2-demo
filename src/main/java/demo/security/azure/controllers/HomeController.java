package demo.security.azure.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class HomeController {
	@Autowired
	private OAuth2AuthorizedClientService clientService;

	@GetMapping("home")
	public String home(@AuthenticationPrincipal(expression = "claims['name']") String name) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) securityContext.getAuthentication();

		OAuth2AuthorizedClient client = clientService
				.loadAuthorizedClient(oauth2Token.getAuthorizedClientRegistrationId(), oauth2Token.getName());

		System.out.println("token=" + client );
		System.out.println(" " + client.getAccessToken().getTokenValue());
		
        getData(client.getAccessToken().getTokenValue());

		return String.format("Hello %s!  welcome to the Security app", name);
	}
	
	private static void getData(String token) {
		// TODO Auto-generated method stub
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer "+ token);
		headers.set("Connection", "keep-alive");
		headers.set("Content-type", "application/json");
		
		HttpEntity<String> entity = new HttpEntity<>(headers);

		ResponseEntity<String> response = restTemplate.exchange("https://graph.microsoft.com/v1.0/drives/b!TuQfhSf0eES78hdFH0_ngn3pm3meigpCqYW0YfQ7tIIXqrspq7mVRqeUpnGSGJSg/items/01BXWRSJOMNAJVMFGMYZHZIK5YYQKENRGG/analytics/allTime?$expand=activities($filter=access ne null)", 
			HttpMethod.GET, entity, String.class);

		System.out.println(response.getBody());
	}

}
