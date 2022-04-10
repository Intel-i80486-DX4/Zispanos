//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "/Users/imac/Downloads/Minecraft-Deobfuscator3000-master/1.12 stable mappings"!

//Decompiled by Procyon!

package javassist.tools.web;

import java.net.*;
import java.io.*;

class ServiceThread extends Thread
{
    Webserver web;
    Socket sock;
    
    public ServiceThread(final Webserver w, final Socket s) {
        this.web = w;
        this.sock = s;
    }
    
    @Override
    public void run() {
        try {
            this.web.process(this.sock);
        }
        catch (IOException ex) {}
    }
}
