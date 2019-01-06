package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.MessageEncoderDecoderImp;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.srv.Server;


public class ReactorMain {
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        Server reactor = Server.reactor(Integer.parseInt(args[1]),
                Integer.parseInt(args[0]),
                () -> {
                    BidiMessagingProtocol<Message> bidiMessagingProtocol = new BidiMessagingProtocolImpl(dataBase);
                    return bidiMessagingProtocol;
                },
                ()->{
                    MessageEncoderDecoder<Message> encoderDecoder = new MessageEncoderDecoderImp();
                    return encoderDecoder;
                });
        reactor.serve();


    }

}
