import grpc
import user_pb2
import user_pb2_grpc

def run():
    channel = grpc.insecure_channel('localhost:9099')
    stub = user_pb2_grpc.userStub(channel)
    login_request = user_pb2.LoginRequest(username='user',password='user')
    login_response = stub.login(login_request)

    print(login_response)

if __name__ == "__main__":
    run()