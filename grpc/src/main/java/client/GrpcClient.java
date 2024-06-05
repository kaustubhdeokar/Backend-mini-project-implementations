package client;

import com.example.User;
import com.example.userGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {

    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9099).usePlaintext().build();
        //stubs
        userGrpc.userBlockingStub userStub = userGrpc.newBlockingStub(channel);
        User.LoginRequest loginRequest = User.LoginRequest.newBuilder().setUsername("a").setPassword("a").build();
        User.APIResponse login = userStub.login(loginRequest);
        System.out.println(login.getResponseCode());


    }

}
