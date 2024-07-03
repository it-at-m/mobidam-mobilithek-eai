package de.muenchen.mobidam.integration.client.api;

import de.muenchen.mobidam.integration.client.ApiClient;

import de.muenchen.mobidam.integration.client.domain.ZuordnungCreateDTO;

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
public class ZuordnungControllerApi {
    private ApiClient apiClient;

    public ZuordnungControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public ZuordnungControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Generates a zuordnung
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param zuordnungCreateDTO The zuordnungCreateDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec createRequestCreation(ZuordnungCreateDTO zuordnungCreateDTO) throws WebClientResponseException {
        Object postBody = zuordnungCreateDTO;
        // verify the required parameter 'zuordnungCreateDTO' is set
        if (zuordnungCreateDTO == null) {
            throw new WebClientResponseException("Missing the required parameter 'zuordnungCreateDTO' when calling create", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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
        return apiClient.invokeAPI("/clients/api/zuordnung", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams,
                localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generates a zuordnung
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param zuordnungCreateDTO The zuordnungCreateDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> create(ZuordnungCreateDTO zuordnungCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return createRequestCreation(zuordnungCreateDTO).bodyToMono(localVarReturnType);
    }

    /**
     * Generates a zuordnung
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param zuordnungCreateDTO The zuordnungCreateDTO parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> createWithHttpInfo(ZuordnungCreateDTO zuordnungCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return createRequestCreation(zuordnungCreateDTO).toEntity(localVarReturnType);
    }

    /**
     * Generates a zuordnung
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param zuordnungCreateDTO The zuordnungCreateDTO parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec createWithResponseSpec(ZuordnungCreateDTO zuordnungCreateDTO) throws WebClientResponseException {
        return createRequestCreation(zuordnungCreateDTO);
    }

    /**
     * Deletes the zuordnung with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteByIdRequestCreation(String id) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new WebClientResponseException("Missing the required parameter 'id' when calling deleteById", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("id", id);

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

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return apiClient.invokeAPI("/clients/api/zuordnung/{id}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams,
                localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Deletes the zuordnung with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteById(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return deleteByIdRequestCreation(id).bodyToMono(localVarReturnType);
    }

    /**
     * Deletes the zuordnung with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> deleteByIdWithHttpInfo(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return deleteByIdRequestCreation(id).toEntity(localVarReturnType);
    }

    /**
     * Deletes the zuordnung with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec deleteByIdWithResponseSpec(String id) throws WebClientResponseException {
        return deleteByIdRequestCreation(id);
    }

    /**
     * Returns the list of all zuordnungen of a schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAllByIdRequestCreation(String id) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new WebClientResponseException("Missing the required parameter 'id' when calling getAllById", HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        pathParams.put("id", id);

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
        return apiClient.invokeAPI("/clients/api/zuordnung/bySchnittstelle/{id}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams,
                formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Returns the list of all zuordnungen of a schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> getAllById(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getAllByIdRequestCreation(id).bodyToMono(localVarReturnType);
    }

    /**
     * Returns the list of all zuordnungen of a schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> getAllByIdWithHttpInfo(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getAllByIdRequestCreation(id).toEntity(localVarReturnType);
    }

    /**
     * Returns the list of all zuordnungen of a schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getAllByIdWithResponseSpec(String id) throws WebClientResponseException {
        return getAllByIdRequestCreation(id);
    }
}
