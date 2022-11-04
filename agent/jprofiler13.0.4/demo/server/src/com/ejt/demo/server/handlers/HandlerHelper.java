package com.ejt.demo.server.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.ThreadLocalRandom;

public class HandlerHelper {

    private HandlerHelper() {
    }

    public static void makeRmiCall(int rmiRegistryPort) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", rmiRegistryPort);
            RmiHandler rmiHandler = (RmiHandler)registry.lookup(RmiHandler.NAME);
            for (int i = 0; i < 3; i++) {
                rmiHandler.remoteOperation();
            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    public static void makeHttpCall() {
        for (int i = 0; i < 3; i++) {
            getExchangeRate("USD", "EUR");
        }
        getExchangeRate("EUR", "GBP");
        getExchangeRate("GBP", "JPY");
    }

    @SuppressWarnings("UnusedReturnValue")
    public static double getExchangeRate(String arg0, String arg1) {
        try {
            StringBuilder buffer = new StringBuilder();
            buffer.append("http://localhost:" + DemoHttpServer.PORT + DemoHttpServer.PATH + "?" + DemoHttpServer.PARAMETER_FROM + "=");
            buffer.append(arg0).append("&").append(DemoHttpServer.PARAMETER_TO).append("=").append(arg1);
            buffer.append("&").append("requestId").append("=").append(ThreadLocalRandom.current().nextInt(100000));

            URL url = new URL(buffer.toString());
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode != 200) {
                throw new RuntimeException("responseCode of HTTP request must be 200");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String input = reader.readLine();
            if (input == null) {
                throw new RuntimeException("invalid response body in HTTP request");
            }
            return Double.parseDouble(input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
