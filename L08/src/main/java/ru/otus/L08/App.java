package ru.otus.L08;

import com.google.gson.Gson;
import ru.otus.L08.jsontool.JsonTool;
import ru.otus.L08.jsontool.JsonToolImpl;
import ru.otus.L08.testclasses.Client;
import ru.otus.L08.testclasses.ClientFactory;

/**
 * Created by dzvyagin on 01.06.2017.
 */
public class App {

    public static void main(String[] args) {

        JsonTool mapper = new JsonToolImpl();
        Gson gson = new Gson();
        ClientFactory cf = ClientFactory.getInstance();
        Client client = cf.getClient();

        String json = mapper.toJson(client);
        System.out.println(json);
        System.out.println(gson.toJson(client));

        Client client1 = gson.fromJson(json, Client.class);
        System.out.println(client.equals(client1));
        System.out.println(client.equals(gson.fromJson(gson.toJson(client), Client.class)));

    }

}
