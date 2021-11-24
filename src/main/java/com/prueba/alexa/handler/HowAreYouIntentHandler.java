package com.prueba.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.dispatcher.request.handler.impl.IntentRequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.request.Predicates;

import java.util.Map;
import java.util.Optional;

public class HowAreYouIntentHandler implements RequestHandler {

    public static final String COUNTER_KEY = "counter";

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("HowAreYouIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Map<String, Object> sessionAttributes = input.getAttributesManager().getSessionAttributes();
        Integer counter = (Integer) sessionAttributes.get(COUNTER_KEY);
        if(counter == null){
            counter = 1;
        }else{
            counter++;
        }
        sessionAttributes.put(COUNTER_KEY, counter);

        String itemName = "";

        IntentRequest intentRequest = (IntentRequest) input.getRequestEnvelope().getRequest();
        final Slot itemSlot = intentRequest.getIntent().getSlots().get("Status");
        if (itemSlot != null) {
            //itemName = itemSlot.getValue().toLowerCase();   // el estado introducido por el usuario
            itemName = itemSlot.getResolutions().getResolutionsPerAuthority().get(0).getValues().get(0).getValue().getName();  // // el estado genérico (de los sinónimos) introducido por el usuario
        }

        String speechText = "Así que te encuentras " + itemName + ". \n" + "Se ha preguntado el estado " + counter + " veces en total esta sesión.";

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HolaMundo", speechText)
                .withReprompt(speechText) // 'reprompt' es para que mantenga la sesión abierta
                .build();
    }

}
