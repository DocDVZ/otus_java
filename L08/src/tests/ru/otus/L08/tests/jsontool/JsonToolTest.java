package ru.otus.L08.tests.jsontool;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;
import ru.otus.L08.jsontool.JsonTool;
import ru.otus.L08.jsontool.JsonToolImpl;
import ru.otus.L08.testclasses.*;

/**
 * Created by DocDVZ on 01.06.2017.
 */
public class JsonToolTest extends Assert {


    @Test
    public void testPlainObject(){
        JsonTool jsonMapper = new JsonToolImpl();
        Gson gson = new Gson();
        Address address = AddressFactory.getInstance().generateAddress();
        String json = jsonMapper.toJson(address);
        assertEquals(address, gson.fromJson(json, Address.class));
    }

    @Test
    public void testComplexObject(){
        JsonTool jsonMapper = new JsonToolImpl();
        Gson gson = new Gson();
        Client client = ClientFactory.getInstance().getClient();
        String json = jsonMapper.toJson(client);
        // Equals method returns false on date comparison
        // Even client.equals(gson.fromJson(gson.toJson(client),Client.class)) -> false
        Client jclient = gson.fromJson(json, Client.class);
        assertNotEquals(client, jclient);
        long time = new Date().getTime();
        client.setBirthDate(new Date(time));
        jclient.setBirthDate(new Date(time));
        assertEquals(client, jclient);
    }

    @Test
    public void testCollectionContainerObject(){
        JsonTool jsonMapper = new JsonToolImpl();
        Gson gson = new Gson();
        AddressCollectionContainer container = AddressCollectionContainerFactory.getInstance().getAddressCollectionContainer();
        String json = jsonMapper.toJson(container);
        assertEquals(container, gson.fromJson(json, AddressCollectionContainer.class));
    }

}
