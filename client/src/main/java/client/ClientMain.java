package client;

import model.*;
import java.util.*;

public class ClientMain {

    static void main(String[] args) {
        ClientLoopService clientLoopService = new ClientLoopService();
        clientLoopService.runLoop();
    }
}
