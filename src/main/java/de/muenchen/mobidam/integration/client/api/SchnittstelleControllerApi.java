package de.muenchen.mobidam.integration.client.api;

import de.muenchen.mobidam.integration.client.ApiClient;

import de.muenchen.mobidam.integration.client.domain.SchnittstelleCreateDTO;
import de.muenchen.mobidam.integration.client.domain.SchnittstelleDTO;

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
public class SchnittstelleControllerApi {
    private ApiClient apiClient;

    public SchnittstelleControllerApi() {
        this(new ApiClient());
    }

    @Autowired
    public SchnittstelleControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Creates schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleCreateDTO The schnittstelleCreateDTO parameter
     * @return SchnittstelleDTO
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec create1RequestCreation(SchnittstelleCreateDTO schnittstelleCreateDTO) throws WebClientResponseException {
        Object postBody = schnittstelleCreateDTO;
        // verify the required parameter 'schnittstelleCreateDTO' is set
        if (schnittstelleCreateDTO == null) {
            throw new WebClientResponseException("Missing the required parameter 'schnittstelleCreateDTO' when calling create1", HttpStatus.BAD_REQUEST.value(),
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

        ParameterizedTypeReference<SchnittstelleDTO> localVarReturnType = new ParameterizedTypeReference<SchnittstelleDTO>() {
        };
        return apiClient.invokeAPI("/clients/api/schnittstelle", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams,
                localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Creates schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleCreateDTO The schnittstelleCreateDTO parameter
     * @return SchnittstelleDTO
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<SchnittstelleDTO> create1(SchnittstelleCreateDTO schnittstelleCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<SchnittstelleDTO> localVarReturnType = new ParameterizedTypeReference<SchnittstelleDTO>() {
        };
        return create1RequestCreation(schnittstelleCreateDTO).bodyToMono(localVarReturnType);
    }

    /**
     * Creates schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleCreateDTO The schnittstelleCreateDTO parameter
     * @return ResponseEntity&lt;SchnittstelleDTO&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<SchnittstelleDTO>> create1WithHttpInfo(SchnittstelleCreateDTO schnittstelleCreateDTO) throws WebClientResponseException {
        ParameterizedTypeReference<SchnittstelleDTO> localVarReturnType = new ParameterizedTypeReference<SchnittstelleDTO>() {
        };
        return create1RequestCreation(schnittstelleCreateDTO).toEntity(localVarReturnType);
    }

    /**
     * Creates schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleCreateDTO The schnittstelleCreateDTO parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec create1WithResponseSpec(SchnittstelleCreateDTO schnittstelleCreateDTO) throws WebClientResponseException {
        return create1RequestCreation(schnittstelleCreateDTO);
    }

    /**
     * Deletes the schnittstelle with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec deleteById1RequestCreation(String id) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new WebClientResponseException("Missing the required parameter 'id' when calling deleteById1", HttpStatus.BAD_REQUEST.value(),
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
        return apiClient.invokeAPI("/clients/api/schnittstelle/{id}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams,
                formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Deletes the schnittstelle with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Void> deleteById1(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return deleteById1RequestCreation(id).bodyToMono(localVarReturnType);
    }

    /**
     * Deletes the schnittstelle with the given ID
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param id The id parameter
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Void>> deleteById1WithHttpInfo(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<Void>() {
        };
        return deleteById1RequestCreation(id).toEntity(localVarReturnType);
    }

    /**
     * Deletes the schnittstelle with the given ID
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
    public ResponseSpec deleteById1WithResponseSpec(String id) throws WebClientResponseException {
        return deleteById1RequestCreation(id);
    }

    /**
     * Get all schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec getAllRequestCreation() throws WebClientResponseException {
        Object postBody = null;
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
        final String[] localVarContentTypes = {};
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {};

        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return apiClient.invokeAPI("/clients/api/schnittstelle", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams,
                localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> getAll() throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getAllRequestCreation().bodyToMono(localVarReturnType);
    }

    /**
     * Get all schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> getAllWithHttpInfo() throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getAllRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get all schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec getAllWithResponseSpec() throws WebClientResponseException {
        return getAllRequestCreation();
    }

    /**
     * Get status of schnittstelle
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
    private ResponseSpec getStatusRequestCreation(String id) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new WebClientResponseException("Missing the required parameter 'id' when calling getStatus", HttpStatus.BAD_REQUEST.value(),
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
        return apiClient.invokeAPI("/clients/api/schnittstelle/{id}/status", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams,
                formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get status of schnittstelle
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
    public Mono<Object> getStatus(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getStatusRequestCreation(id).bodyToMono(localVarReturnType);
    }

    /**
     * Get status of schnittstelle
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
    public Mono<ResponseEntity<Object>> getStatusWithHttpInfo(String id) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return getStatusRequestCreation(id).toEntity(localVarReturnType);
    }

    /**
     * Get status of schnittstelle
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
    public ResponseSpec getStatusWithResponseSpec(String id) throws WebClientResponseException {
        return getStatusRequestCreation(id);
    }

    /**
     * Updates the schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleDTO The schnittstelleDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec updateRequestCreation(SchnittstelleDTO schnittstelleDTO) throws WebClientResponseException {
        Object postBody = schnittstelleDTO;
        // verify the required parameter 'schnittstelleDTO' is set
        if (schnittstelleDTO == null) {
            throw new WebClientResponseException("Missing the required parameter 'schnittstelleDTO' when calling update", HttpStatus.BAD_REQUEST.value(),
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
        return apiClient.invokeAPI("/clients/api/schnittstelle/update", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams,
                formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Updates the schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleDTO The schnittstelleDTO parameter
     * @return Object
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<Object> update(SchnittstelleDTO schnittstelleDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return updateRequestCreation(schnittstelleDTO).bodyToMono(localVarReturnType);
    }

    /**
     * Updates the schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleDTO The schnittstelleDTO parameter
     * @return ResponseEntity&lt;Object&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<Object>> updateWithHttpInfo(SchnittstelleDTO schnittstelleDTO) throws WebClientResponseException {
        ParameterizedTypeReference<Object> localVarReturnType = new ParameterizedTypeReference<Object>() {
        };
        return updateRequestCreation(schnittstelleDTO).toEntity(localVarReturnType);
    }

    /**
     * Updates the schnittstelle
     *
     * <p>
     * <b>400</b> - Bad Request
     * <p>
     * <b>200</b> - OK
     *
     * @param schnittstelleDTO The schnittstelleDTO parameter
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec updateWithResponseSpec(SchnittstelleDTO schnittstelleDTO) throws WebClientResponseException {
        return updateRequestCreation(schnittstelleDTO);
    }
}
