package Tarea;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class Main {

	public static void main(String[] args) {
		Scanner input;
		Document doc = null;

		input = new Scanner(System.in);

		@SuppressWarnings("unused")
		File file = new File("C:\\Users\\Jorge\\Documents\\Eclipse\\Tarea 1\\src\\main\\resources\\asd.html");

		while (true) {
			try {
				doc = Jsoup.connect(enterURL(input)).get();
				// doc = Jsoup.parse(file, "utf-8");

				countLines(doc);
				countP(doc);
				countImg(doc);
				countForms(doc);
				showInputType(doc);
				responseServerPost(doc);
			} catch (UnknownHostException e) {
				System.out.println("Unknown host");
			} catch (MalformedURLException | IllegalArgumentException e) {
				System.out.println("URL no valida");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void responseServerPost(Document doc) {
		int formCount = 0;
		Connection connection;
		Connection.Response response;

		for (Element form : doc.getElementsByTag("form")) {

			formCount++;
			System.out.println("\nForm #" + formCount);

			if (form.attr("method").equalsIgnoreCase("POST")) {

				connection = ((FormElement) form).submit();
				connection.data("asignatura", "practica1");
				connection.header("matricula", "2016-0876");
				System.out.println("Request parameters:" + "\n" + connection.request().data());
				System.out.println("Request headers:" + "\n" + connection.request().headers() + "\n");

				try {
					response = connection.execute();
	                System.out.println("Response:" + "\n" + response.statusMessage());
	                System.out.println("Response body:" + "\n" + response.body() + "\n");
				} catch (IOException e) {
					System.out.println("Main.responseServerPost()");
				}
			}
		}
	}

	private static void showInputType(Document doc) {
		int formCount = 0;
		String type;

		for (Element form : doc.getElementsByTag("form")) {
			System.out.println("Form " + ++formCount);

			for (Element input : form.getElementsByTag("input")) {
				type = input.attr("type");
				System.out.println("<input> type = " + type);
			}
		}
	}

	private static void countForms(Document doc) {
		int get = 0, post = 0;

		for (Element form : doc.getElementsByTag("form")) {
			if (form.attr("method").equalsIgnoreCase("GET")) {
				get++;
			}

			if (form.attr("method").equalsIgnoreCase("POST")) {
				post++;
			}
		}

		System.out.println("Cantidad de <form>: GET " + get + " POST " + post);
	}

	private static void countImg(Document doc) {
		int cant;

		cant = doc.getElementsByTag("p").select("img").size();
		System.out.println("Cantidad de <img> dentro de <p>: " + cant);
	}

	private static void countP(Document doc) {
		int cant;

		cant = doc.getElementsByTag("p").size();
		System.out.println("Cantidad de <p>: " + cant);
	}

	private static void countLines(Document doc) {
		long cant;

		cant = doc.body().toString().split("\n").length;
		// cant = doc.html().lines().count();
		System.out.println("Cantidad de lineas: " + cant);
	}

	private static String enterURL(Scanner input) {
		String url;

		System.out.print("\n\n" + "Ingrese una URL: ");
		if (input.hasNextLine()) {
			url = input.nextLine();
		} else {
			url = "http://jsoup.org/";
		}

		return url;
	}
}