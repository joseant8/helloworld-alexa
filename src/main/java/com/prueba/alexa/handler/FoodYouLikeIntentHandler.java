package com.prueba.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;

import java.util.Map;
import java.util.Optional;

public class FoodYouLikeIntentHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("FoodYouLikeIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        Map<String, Slot> slots = intentRequest.getIntent().getSlots();  // slots del json de la petición

        String speechText = "La comida que te gusta es " + slots.get("DulceSalada").getValue() + ", de origen "
                + slots.get("Origen").getValue() + " y de temperatura " + slots.get("Temperatura").getValue();

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HolaMundo Comida Favorita", speechText)
                .withReprompt(speechText) // 'reprompt' es para que mantenga la sesión abierta
                .build();
    }
}
