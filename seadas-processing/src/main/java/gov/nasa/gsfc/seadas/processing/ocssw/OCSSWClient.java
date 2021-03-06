package gov.nasa.gsfc.seadas.processing.ocssw;


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jsonp.JsonProcessingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import javax.json.stream.JsonGenerator;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 * Created by IntelliJ IDEA.
 * User: Aynur Abdurazik (aabduraz)
 * Date: 12/9/14
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class OCSSWClient {

    public static final String defaultServer ="localhost";
    public static final String defaultPort = "6400";
    final static String OCSSW_REST_SERVICES_CONTEXT_PATH = "ocsswws";

    private WebTarget target;
    private String resourceBaseUri;

    public OCSSWClient(String serverIPAddress, String portNumber) {
        resourceBaseUri = getResourceBaseUri(serverIPAddress, portNumber);
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(MultiPartFeature.class);
        clientConfig.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
        Client c = ClientBuilder.newClient(clientConfig);
        target = c.target(resourceBaseUri);
    }

    public OCSSWClient(String resourceBaseUri) {
        this.resourceBaseUri = resourceBaseUri;
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.register(MultiPartFeature.class);
        clientConfig.register(JsonProcessingFeature.class).property(JsonGenerator.PRETTY_PRINTING, true);
        Client c = ClientBuilder.newClient(clientConfig);
        target = c.target(resourceBaseUri);
    }

    public OCSSWClient(){
      this(defaultServer, defaultPort);
    }

    public WebTarget getOcsswWebTarget() {
        return target;
    }
    public WebTarget getServicePathForFileVerification(String jobId){
        return target.path("fileServices").path("fileVerification").path(jobId);
    }

    public WebTarget getServicePathForFileCharSet(String jobId){
        return target.path("ocssw").path("getFileCharSet").path(jobId);
    }

    private String getResourceBaseUri(String serverIPAddress, String portNumber){
        resourceBaseUri = "http://" + serverIPAddress + ":" + portNumber + "/" + OCSSW_REST_SERVICES_CONTEXT_PATH + "/";
        return  resourceBaseUri;
    }

}

