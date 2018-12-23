package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.function.Supplier;

public abstract class BaseServer<T> implements Server<T> {

    private final int port;
    private final Supplier<BidiMessagingProtocol<T>> bidiProtocolFactory; //todo tell shai
    private final Supplier<MessageEncoderDecoder<T>> encdecFactory;
    private ServerSocket sock;

    public BaseServer(
            int port,
            Supplier<BidiMessagingProtocol<T>> bidiprotocolFactory,
            Supplier<MessageEncoderDecoder<T>> encdecFactory) {

        this.port = port;
        this.bidiProtocolFactory = bidiprotocolFactory;
        this.encdecFactory = encdecFactory;
		this.sock = null;
    }

    @Override
    public void serve() {

        try (ServerSocket serverSock = new ServerSocket(port)) {
			System.out.println("Server started");

            this.sock = serverSock; //just to be able to close (the close is to sock)

            while (!Thread.currentThread().isInterrupted()) {

                Socket clientSock = serverSock.accept(); //wait till a client connect with the server

                BlockingConnectionHandler<T> handler= new BlockingConnectionHandler<>( //kind of creating customer
                        clientSock,
                        encdecFactory.get(),
                        bidiProtocolFactory.get());
                execute(handler);
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
