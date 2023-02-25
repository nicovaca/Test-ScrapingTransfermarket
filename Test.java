import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;






public class Test {

	/*
		L'obiettivo di questo esercizio di scraping è quello di navigare sul sito web transfermarkt.it, sito che permette di 
		leggere per ciascuna squadra di calcio dei principali campionati, informazioni quali i  componenti 
		di ciascun team e i rispettivi valori di mercato ed estrapolare alcuni dati (nome calciatore, numero di maglia, 
		età e data di nascita, ruolo, valore di mercato), ipotizzando uno scenario in cui uno scouting voglia prelevare 
		queste informazioni per utilizzarle ad esempio durante una finestra di mercato per suggerire gli atleti da comprare 
		alla squadra per cui lavora con i rispettivi prezzi di acquisto. Lo script permette di entrare nel sito e 
		fare ricerca del club di cui si vogliono successivamente estrarre i dati citati. 

	 */


	//Creo un oggetto WebDriver chiamato  genericamente driver e lo facciamo puntare ad un oggetto concreto di tipo chromedriver.
	public WebDriver driver= new ChromeDriver();

	//Creo un metodo che apre la pagina web passando come parametro l'indirizzo URL del sito da visitare.
	public void apriSito() {
		driver.get("https://www.transfermarkt.it/");

	}


	public void cerca(String cerca) throws InterruptedException {

		//Apro la pagina e chiudo la finestra del Cookie.
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id=\"notice\"]/div[3]/div[2]/button")).click();

		//Cerco il club di mio interesse. Identifico l'oggetto specifico nella pagina tramite il suo percorso xpath.
		Thread.sleep(2000);
		driver.findElement(By.xpath("//*[@id=\"schnellsuche\"]/input[1]")).clear();

		Thread.sleep(1000);
		driver.findElement(By.xpath("//*[@id=\"schnellsuche\"]/input[1]")).sendKeys(cerca);

		Thread.sleep(1000);

		driver.findElement(By.xpath("//*[@id=\"schnellsuche\"]/input[2]")).click();

		Thread.sleep(2000);

		driver.findElement(By.xpath("//*[@id=\"yw0\"]/table/tbody/tr[1]/td[2]/table/tbody/tr[1]/td/a")).click();


		Thread.sleep(500);

	}


	public void ottieni_informazioni() throws InterruptedException , IOException {

		// Creo il file .csv di destinazione.
		BufferedWriter writer = new BufferedWriter(new FileWriter("/Users/nicolavacalebre/Desktop/Data Analytics/Test/test.csv"));

		//Fisso il valore massimo di righe per la tabella considerando un numero massimo di 33 giocatori per squadra.
		int lastrow=33;

		writer.write("Nome"+ "," +"Numero di Maglia" +","+"Data di Nascita" +","+ "Ruolo" +"," +"Valore di Mercato" +"\n");

		//All'interno del ciclo for prelevo per ogni riga il corrispondente elemento come Stringa di testo.
		try {
			for(int i=1; i<lastrow; i++) {
				String nome = driver.findElement(By.xpath("//*[@id=\"yw1\"]/table/tbody/tr["+i+"]/td[2]/table/tbody/tr[1]/td[2]/div[1]/span/a")).getText();

				String dataNascita = driver.findElement(By.xpath("//*[@id=\"yw1\"]/table/tbody/tr["+i+"]/td[4]")).getText();

				String ruolo= driver.findElement(By.xpath("//*[@id=\"yw1\"]/table/tbody/tr["+i+"]/td[2]/table/tbody/tr[2]/td")).getText();

				String numMaglia = driver.findElement(By.xpath("//*[@id=\"yw1\"]/table/tbody/tr["+i+"]/td[1]/div")).getText();

				String valoreMercato = driver.findElement(By.xpath("//*[@id=\"yw1\"]/table/tbody/tr["+i+"]/td[6]/a")).getText().replace("," , ".");

				//Stampo ciascun valore 
				writer.write(nome+ "," +numMaglia +","+dataNascita +","+ ruolo +"," +valoreMercato +"\n");


			}

		}catch (Exception e) {
			e.printStackTrace();
		} 

		//Prelevo inoltre il valore totale di mercato di ciascuna rosa, informazione posta in alto a destra accanto al nome di ogni squadra.
		String totale= driver.findElement(By.xpath("//*[@id=\"verein_head\"]/div/div[1]/div[5]")).getText().replace(",", ".");


		//Stampo il valore totale. 
		writer.write("Valore totale di mercato=" +totale);
		writer.close();

		//Stampo su console la Stringa "Informazioni prelevate" così da essere sicuro della corretta esecuzione.
		System.out.println("Informazioni prelevate");	
	}

	//Dopo aver completato le operazioni chiudo il browser. 
	public void chiudiBrowser() {
		driver.close();
	}

	//Metodo che permette di memorizzare uno screenshot della pagina visitata all'interno di un file png.
	public void salvaScreenshot() throws IOException {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileHandler.copy(scrFile, new File("/Users/nicolavacalebre/Desktop/Data Analytics/Test/screenshot.png"));



	}



	/* 
	Dentro la classe Test definisco l'insieme di metodi che il main va a chiamare in sequenza.
	In questo caso si è scelti, per il metoto test.cerca, di passare in maniera parametrica e non esplicita 
	il nome della squadra da cercare, in modo che il codice possa essere utilizzato più volte per ricercare 
	le informazioni	di qualiasi squadra	si voglia.

	 */
	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("webdriver.chrome.driver", "/Users/nicolavacalebre/Desktop/Data Analytics/chromedriver");
		Test test= new Test();
		test.apriSito();
		test.cerca("Ac Milan");
		test.ottieni_informazioni();
		test.salvaScreenshot();
		test.chiudiBrowser();

	}



}
