/*
 * Copyright (c) 2010-2018 Nathan Rajlich
 *
 *  Permission is hereby granted, free of charge, to any person
 *  obtaining a copy of this software and associated documentation
 *  files (the "Software"), to deal in the Software without
 *  restriction, including without limitation the rights to use,
 *  copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the
 *  Software is furnished to do so, subject to the following
 *  conditions:
 *
 *  The above copyright notice and this permission notice shall be
 *  included in all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 *  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 *  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 *  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 *  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 *  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 *  OTHER DEALINGS IN THE SOFTWARE.
 */

package com.arandompieceofcheese.bot;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;

/** This example demonstrates how to create a websocket connection to a server. Only the most important callbacks are overloaded. */
public class Main {

	public static void main( String[] args ) throws URISyntaxException, FileNotFoundException {
		String server = "wss://sim2.psim.us";
		String user = args[args.length - 2];
		String password = args[args.length - 1];
		String room = "techcode";
		String logfile = "./log.txt";
		String blacklist = "./blacklist.txt";
		
		//I don't know Java calling convention so I actually have no idea what's *in* args[], so this might be very wrong.  This will probably error really bad.
		int mode = 0;
		boolean help = args.length < 3;
		for(int i = 0; i < args.length - 2; i++) {
			if(args[i].equals("-h") || args[i].equals("--help")) help = true;
			if(mode == 1) server = args[i];
			if(mode == 2) room = args[i];
			if(mode == 3) logfile = args[i];
			if(mode == 4) blacklist = args[i];
			if(args[i].equals("-s")) mode = 1;
			else if(args[i].equals("-r")) mode = 2;
			else if(args[i].equals("-l")) mode = 3;
			else if(args[i].equals("-b")) mode = 4;
			else mode = 0;
		}
		
		if(help) {
			System.out.println("Usage: bot [-s server] [-r room] [-l logfile] [-b blacklist] username password\n\tServer: Websocket server to connect to.  Default sim2.psim.us, official ps server\n\tRoom: Chatroom to join.  Defaults to 'techcode'.\n\tLogfile: File to log chat to.  Defaults to './log.txt'.\n\tBlacklist: List of words seperated by newlines to warn on usage.  Defaults to './blacklist.txt'.");
			return;
		}
		
		Bot c = new Bot(server, user, password, room, logfile, blacklist );
		c.connect();
	}

}