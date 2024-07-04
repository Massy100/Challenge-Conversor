/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.conversor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

/**
 *
 * Author: Massielle
 */
public class Conversor {
    private static final String API_KEY = "1ebe1b589504e9b7ce3ff8ce";

    public static void main(String[] args) {
        Scanner leer = new Scanner(System.in);
        while (true) {
            System.out.println("Conversor de Monedas");
            System.out.println("Ingrese el codigo de la moneda de origen (por ejemplo, USD):");
            String fromCurrency = leer.next().toUpperCase();
            System.out.println("Ingrese el codigo de la moneda de destino (por ejemplo, EUR):");
            String toCurrency = leer.next().toUpperCase();
            System.out.println("Ingrese la cantidad de " + fromCurrency + " a convertir:");
            double amount = leer.nextDouble();
            
            try {
                double result = convertir(fromCurrency, toCurrency, amount);
                System.out.println("------------------------------");
                System.out.println("Tienes " + result + " " + toCurrency);
                System.out.println("------------------------------");
            } catch (IOException e) {
                System.out.println("Error al obtener los datos de conversion: " + e.getMessage());
            }

            System.out.println("¿Desea realizar otra conversion? (s/n): ");
            String respuesta = leer.next();
            if (respuesta.equalsIgnoreCase("n")) {
                break;
            }
        }
    }
    
    static double convertir(String fromCurrency, String toCurrency, double amount) throws IOException {
        String urlStr = "https://api.exchangerate-api.com/v4/latest/" + fromCurrency;
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new IOException("Unexpected code " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String responseData = response.toString();
        System.out.println("Respuesta JSON: " + responseData); 

        JSONObject json = new JSONObject(responseData);
        double exchangeRate = json.getJSONObject("rates").getDouble(toCurrency);

        return amount * exchangeRate;
    }
}
