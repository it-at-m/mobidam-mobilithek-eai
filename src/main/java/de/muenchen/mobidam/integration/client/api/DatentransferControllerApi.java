package de.muenchen.mobidam.integration.client.api;

import de.muenchen.mobidam.integration.client.ApiClient;

import de.muenchen.mobidam.integration.client.domain.DatentransferCreateDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2024-07-03T08:29:51.493688800+02:00[Europe/Berlin]")
public class DatentransferControllerApi {
    private ApiClient apiClient;

    public DatentransferControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public DatentransferControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Creating a Datentransfer for an existing Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param datentransferCreateDTO The datentransferCreateDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec createDatentransferRequestCreation(DatentransferCreateDTO datentransferCreateDTO) throws WebClientResponseException {
        Object postBody = datentransferCreateDTO;
        // verify the required parameter 'datentransferCreateDTO' is set
        if (datentransferCreateDTO == null) {
            throw new WebClientResponseException("Missing the required parameter 'datentransferCreateDTO' when calling createDatentransfer",
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/hal+json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {
                "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {};

        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return apiClient.invokeAPI("/clients/api/datentransfer", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams,
                localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Creating a Datentransfer for an existing Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param datentransferCreateDTO The datentransferCreateDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> createDatentransfer(DatentransferCreateDTO datentransferCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return createDatentransferRequestCreation(datentransferCreateDTO).bodyToMono(localVarReturnType);
    }

    /**
     * Creating a Datentransfer for an existing Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param datentransferCreateDTO The datentransferCreateDTO parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> createDatentransferWithHttpInfo(DatentransferCreateDTO datentransferCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return createDatentransferRequestCreation(datentransferCreateDTO).toEntity(localVarReturnType);
    }

    /**
     * Creating a Datentransfer for an existing Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param datentransferCreateDTO The datentransferCreateDTO parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec createDatentransferWithResponseSpec(DatentransferCreateDTO datentransferCreateDTO) throws WebClientResponseException {
        return createDatentransferRequestCreation(datentransferCreateDTO);
    }

    /**
     * Get all Datentransfers for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @param page The page parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getBySchnittstelleRequestCreation(String schnittstelleId, Integer page) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'schnittstelleId' is set
        if (schnittstelleId == null) {
            throw new WebClientResponseException("Missing the required parameter 'schnittstelleId' when calling getBySchnittstelle",
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'page' is set
        if (page == null) {
            throw new WebClientResponseException("Missing the required parameter 'page' when calling getBySchnittstelle", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("schnittstelleId", schnittstelleId);
        pathParams.put("page", page);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/hal+json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {};

        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return apiClient.invokeAPI("/clients/api/datentransfer/{schnittstelleId}/{page}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams,
                cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all Datentransfers for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @param page The page parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> getBySchnittstelle(String schnittstelleId, Integer page) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getBySchnittstelleRequestCreation(schnittstelleId, page).bodyToMono(localVarReturnType);
    }

    /**
     * Get all Datentransfers for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @param page The page parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> getBySchnittstelleWithHttpInfo(String schnittstelleId, Integer page) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getBySchnittstelleRequestCreation(schnittstelleId, page).toEntity(localVarReturnType);
    }

    /**
     * Get all Datentransfers for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @param page The page parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getBySchnittstelleWithResponseSpec(String schnittstelleId, Integer page) throws WebClientResponseException {
        return getBySchnittstelleRequestCreation(schnittstelleId, page);
    }

    /**
     * Get first Datentransfer result state for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getLatestResultStateBySchnittstelleRequestCreation(String schnittstelleId) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'schnittstelleId' is set
        if (schnittstelleId == null) {
            throw new WebClientResponseException("Missing the required parameter 'schnittstelleId' when calling getLatestResultStateBySchnittstelle",
                    HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("schnittstelleId", schnittstelleId);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] localVarAccepts = {
                "application/hal+json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {};

        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return apiClient.invokeAPI("/clients/api/datentransfer/latestResultState/{schnittstelleId}", HttpMethod.GET, pathParams, queryParams, postBody,
                headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get first Datentransfer result state for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> getLatestResultStateBySchnittstelle(String schnittstelleId) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getLatestResultStateBySchnittstelleRequestCreation(schnittstelleId).bodyToMono(localVarReturnType);
    }

    /**
     * Get first Datentransfer result state for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> getLatestResultStateBySchnittstelleWithHttpInfo(String schnittstelleId) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getLatestResultStateBySchnittstelleRequestCreation(schnittstelleId).toEntity(localVarReturnType);
    }

    /**
     * Get first Datentransfer result state for Schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleId The schnittstelleId parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getLatestResultStateBySchnittstelleWithResponseSpec(String schnittstelleId) throws WebClientResponseException {
        return getLatestResultStateBySchnittstelleRequestCreation(schnittstelleId);
    }
}
