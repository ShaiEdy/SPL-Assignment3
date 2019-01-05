package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.MessageEncoderDecoderImp;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.srv.Server;

public class TPCMain {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        Server baseServer = Server.threadPerClient(7000,
                () -> {
                    BidiMessagingProtocol<Message> bidiMessagingProtocol = new BidiMessagingProtocolImpl(dataBase);
                    return bidiMessagingProtocol;
                },
                () -> {
                    MessageEncoderDecoder<Message> encoderDecoder = new MessageEncoderDecoderImp();
                    return encoderDecoder;
                });
        baseServer.serve();
    }
}
