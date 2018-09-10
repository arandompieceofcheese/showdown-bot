package com.arandompieceofcheese.bot;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import org.json.*;

public class Bot extends WebSocketClient{
	private String name;
	private String pass;
	private String roomname;
	private Room room;
	
	private FileOutputStream stream;
	
	private final String eol = System.getProperty("line.separator");
	
	private List<String> blacklist;
	
	public Bot(String server, String name, String pass, String room, String logfile, String blacklist) throws URISyntaxException, FileNotFoundException {
		super(new URI(server));
		this.name = name;
		this.pass = pass;
		this.roomname = room;
		
		this.stream = new FileOutputStream(logfile, true);
		try {
			this.blacklist = Files.readAllLines(Paths.get(blacklist), StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onOpen( ServerHandshake handshakedata ) {
		try {
			log("[ LOGIN ]" + eol);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		send("|/autojoin " + roomname);
	}

	@Override
	public void onMessage( String message ) {
		//System.out.println( message );
		if(message.startsWith("|challstr|")) {
			try {
				String json = getAssertion(message.substring(10)).substring(1);
				JSONObject obj = new JSONObject(json);

				send("|/trn " + name + ",0," + obj.getString("assertion"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(message.contains("|init|chat")) {
			room = new Room(this, message);
		}
		
		else if(message.startsWith(">" + roomname)) {
			try {
				room.parse(message.substring(message.indexOf("\n") + 1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClose( int code, String reason, boolean remote ) {
		// The codecodes are documented in class org.java_websocket.framing.CloseFrame
		System.out.println( "Connection closed by " + ( remote ? "remote peer" : "us" ) + " Code: " + code + " Reason: " + reason );
	}

	@Override
	public void onError( Exception ex ) {
		ex.printStackTrace();
		// if the error is fatal then onClose will be called additionally
	}
	
	public void log(String logstr) throws IOException {
		//Timestamp
		byte[] bytes = ("[ " + new SimpleDateFormat("HH.mm.ss").format(new Date()) + " ] " + logstr + eol).getBytes();
		stream.write(bytes);
	}
	
	public List<String> getBlacklist(){
		return blacklist;
	}
	
	private String getAssertion( String challstr ) throws Exception {

		String url = "https://play.pokemonshowdown.com/action.php";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		String urlParameters = "act=login&name=" + name + "&pass=" + pass + "&challstr=" + challstr;
		
		con.setRequestProperty("Content-Length",  Integer.toString(urlParameters.length()));
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		return response.toString();

	}
}
