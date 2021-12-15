package com.prueba.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.PermissionStatus;
import com.amazon.ask.model.Permissions;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.geolocation.Coordinate;
import com.amazon.ask.model.interfaces.geolocation.GeolocationState;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.response.ResponseBuilder;

import java.util.Arrays;
import java.util.Optional;


public class LocationIntentHandler implements RequestHandler {

    private static final String GEO_PERMISSION = "alexa::devices:all:geolocation:read";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("LocationIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        ResponseBuilder response = input.getResponseBuilder();

        if (deviceSupportsGeolocation(input)) {
            return handleAtCurrentLocation(input, response);
        } else {
            return handleAskForLocation(response);
        }
    }

    private Optional<Response> handleAskForLocation(ResponseBuilder response) {
        return say("El dispositivo no soporta geolocalización, ¿qué playa?", response);
    }

    private Optional<Response> handleAtCurrentLocation(HandlerInput input, ResponseBuilder response) {
        if (hasGeolocationPermission(input)) {
            return say(getCurrentLocation(input).map(this::searchBeach)
                    .orElse("No pudimos obtener tu localización, ¿qué playa?"), response);
        } else {
            askForPermissionIfDenied(response);
            return say("No tenemos permiso para obtener tu localización. Concede el permiso o dinos qué playa.",
                    response);
        }
    }

    private Optional<Response> say(String speechText, ResponseBuilder response) {
        response.withSpeech(speechText).withSimpleCard("Playa", speechText).withReprompt(speechText);
        return response.build();
    }

    private boolean deviceSupportsGeolocation(HandlerInput input) {
        return input.getRequestEnvelope().getContext().getSystem().getDevice().getSupportedInterfaces()
                .getGeolocation() != null;
    }

    private boolean hasGeolocationPermission(HandlerInput input) {
        Permissions permissions = input.getRequestEnvelope().getSession().getUser().getPermissions();
        return permissions != null && permissions.getScopes() != null
                && permissions.getScopes().get(GEO_PERMISSION).getStatus() == PermissionStatus.GRANTED;
    }

    private void askForPermissionIfDenied(ResponseBuilder response) {
        response.withAskForPermissionsConsentCard(Arrays.asList(GEO_PERMISSION));
    }

    private Optional<Coordinate> getCurrentLocation(HandlerInput input) {
        GeolocationState geo = input.getRequestEnvelope().getContext().getGeolocation();
        return Optional.ofNullable(geo.getCoordinate());
    }

    private String searchBeach(Coordinate location) {
        // ... we use the OPEN AEMET API here.
        return "Tú localización actual es, latitud " + location.getLatitudeInDegrees() + " y longitud: " + location.getLongitudeInDegrees();
    }
}
