package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.MessageEncoderDecoderImp;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.impl.rci.ObjectEncoderDecoder;
import bgu.spl.net.impl.rci.RemoteCommandInvocationProtocol;
import bgu.spl.net.srv.Server;

import java.util.function.Supplier;

public class ReactorMain {
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        Server reactor = Server.reactor(3,
                7000,
                () -> {
                    BidiMessagingProtocol<Message> bidiMessagingProtocol = new BidiMessagingProtocolImpl(dataBase);
                    return bidiMessagingProtocol;
                },
                ()->{
                    MessageEncoderDecoder<Message> encoderDecoder = new MessageEncoderDecoderImp();
                    return encoderDecoder;
                });
        reactor.serve();

        /*
        Server.reactor(
                3,
                7000, //port
                () -> BidiMessagingProtocol<Message>BidiMessagingProtocol(), //protocol factory
                ObjectEncoderDecoder::new //message encoder decoder factory
        ).serve();
        */
    }

}
