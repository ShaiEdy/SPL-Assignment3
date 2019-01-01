import bgu.spl.net.api.Customer;
import bgu.spl.net.api.DataBase;
import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.api.bidi.BidiMessagingProtocolImpl;
import bgu.spl.net.api.bidi.MessageEncoderDecoderImp;
import bgu.spl.net.impl.messages.Message;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.Server;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class temp {
    public static void main(String[] args) {
        /*
        DataBase dataBase = new DataBase();
        Server baseServer = Server.threadPerClient(7000,
                () -> {
                    BidiMessagingProtocol<Message> bidiMessagingProtocol = new BidiMessagingProtocolImpl<>(dataBase);
                    return bidiMessagingProtocol;
                },
                () -> {
                    MessageEncoderDecoderImp<Message> encoderDecoder = new MessageEncoderDecoderImp<>();
                    return encoderDecoder;
                });
                */
        short x=2;
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((x >> 8) & 0xFF);
        bytesArr[1] = (byte)(x & 0xFF);

        short y = 2;
        String yy = Short.toString(y);
        byte[] bytes = yy.getBytes();

        String ss= "sadasdnasodasnmdoasmdoasdm";
        byte[] dd= ss.getBytes();

        x=1;

    }
}