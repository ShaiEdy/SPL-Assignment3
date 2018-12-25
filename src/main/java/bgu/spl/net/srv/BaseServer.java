package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.Connections;
import bgu.spl.net.api.bidi.ConnectionsImpl;
import bgu.spl.net.impl.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> bidiProtocolFactory;
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;// todo check if its ok you have here Message instade of T
    private ServerSocket sock;
    private ConnectionsImpl connections;
    private int connectionID; //Unique ID for each customer.

    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> bidiprotocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory) {
        this.port = port;
        this.bidiProtocolFactory = bidiprotocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
		this.connections = new ConnectionsImpl();
		this.connectionID = 0;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close (the close is to sock)

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept(); //wait till a client connect with the server

                BlockingConnectionHandler<T> handler= new BlockingConnectionHandler<T>( //kind of creating customer.
                        clientSock,
                        encdecFactory.get(),
                        bidiProtocolFactory.get());
                execute(handler);

                connections.addNewConnection(connectionID,handler);
                connectionID++;
            }
        } catch (IOException ex) {
        }

        System.out.println("server closed!!!"); //when someone command me to close (interrupted)
    }

    @Override
    public void close() throws IOException {
		if (sock != null)
			sock.close();
    }

    protected abstract void execute(BlockingConnectionHandler<T>  handler);
    //two types of execute
}
