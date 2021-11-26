package com.prueba.alexa.handler;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class HelloWorldIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("HelloWorldIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = "Hola a todo el mundo. ¿Qué tal te encuentras hoy? También me puedes decir que tipo de comida te gusta.";
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HolaMundo", speechText)
                .withReprompt(speechText) // 'reprompt' es para que mantenga la sesión abierta
                .build();
    }

}
