/*
    zencat - a zenoss irc bot

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; version 2 of the GPL only, not 3 :P

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package com.ls.zencat;

import java.io.*; 

// hands off cmd to shell script and returns stdout to the requester
class Scripter extends Thread {
        ZenCat bot;
        String nick, channel, returnName, cmd;

        Scripter(String nk, String ch, String r, String c, ZenCat b){
            nick = nk;
            channel = ch;
            cmd = c;
            returnName = r;
            bot = b;
        }

        public void run(){
            try{
                Runtime runtime = Runtime.getRuntime();
                Process process = runtime.exec(new String[]{bot.getCmdScript() ,nick + " " + channel + " " + returnName+" "+cmd});
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line;
                int i=0;
                while ((line = br.readLine()) != null) {
                    bot.sendMsg(returnName, line);
                    if(++i==bot.getCmdMaxResponseLines()){
                        bot.sendMsg(returnName, "<truncated, too many lines>");
                        break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
                        
        }
}

