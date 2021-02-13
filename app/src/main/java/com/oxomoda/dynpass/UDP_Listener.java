package com.oxomoda.dynpass;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public abstract class UDP_Listener {

    private DatagramSocket socket; // recivir informacion
    private byte[] buf = new byte[128];

    private DatagramPacket packet;

    /**
     * puerto al que es enviado el DynPass desde OxoModaApp
     */
    private final int UDP_PORT_BIND = 11751;


    /**
     * inicializa el socket
     * @throws SocketException
     */
    public UDP_Listener() throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(this.UDP_PORT_BIND);
        //this.socket.connect(InetAddress.getByName("255.255.255.255"), this.UDP_PORT_BIND);
        this.socket.setBroadcast(true);
        this.packet = new DatagramPacket(buf, buf.length);
    }

    /**
     * se queda es
     */
    public void Listen(){
        System.out.println("CICLO --------------------------------------");
        while (true) {
            try {
                socket.receive(packet);
                InetAddress address = packet.getAddress();
                int port = packet.getPort();
                packet = new DatagramPacket(buf, buf.length, address, port);
                String received = new String(packet.getData(), packet.getOffset(), packet.getLength()); // received reply.
                String[] res = received.split(":");
                if (res[0].equals("OxoModaDynPass")) {// if split returns in [0] 'OxoModaDynPass' then is from Oxo App
                    // split in [1] es la clave dinamica
                    OnDynPassReceive(res[1]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void OnDynPassReceive(String pass);
}
