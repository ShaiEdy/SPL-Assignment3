import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.MessageEncoderDecoderImp;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

import java.io.IOException;
import java.util.function.Supplier;

public class temp {
    public static void main(String[] args) {

        Server baseServer = Server.threadPerClient(7000,
                new Supplier<BidiMessagingProtocol<Message>>() {
                    @Override
                    public BidiMessagingProtocol<Message> get() {
                        BidiMessagingProtocol<Message> bidiMessagingProtocol = new BidiMessagingProtocolImpl<>();
                        return bidiMessagingProtocol;
                    }
                },
                new Supplier<MessageEncoderDecoder<Message>>() {
                    @Override
                    public MessageEncoderDecoder<Message> get() {
                        MessageEncoderDecoderImp<Message> encoderDecoder = new MessageEncoderDecoderImp<>();
                        return encoderDecoder;
                    }
                });
    }
}