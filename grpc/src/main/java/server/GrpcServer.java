package server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import user.UserService;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws InterruptedException, IOException {

        Server server = ServerBuilder.forPort(9099).addService(new UserService()).build();
        server.start();

        System.out.println("server started on port");
        server.awaitTermination();
    }
}
