package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import common.Constants;

public class ClientApp {
    public static void main(String[] args) {
        // 서버의 IP주소, 포트에 연결하는 Socket 객체 생성
        try (Socket socket = new Socket(Constants.SERVER_ADDR, Constants.SERVER_PORT);
                /*
                 * BufferedReader 보조 스트림으로 InputStreamReader 기반 스트림 감싸기는 매우 권장된다.
                 * InputStreamReader 단독으로도 UTF-8 인코딩을 지원하므로 한글 표현에 지장은 없으나
                 * 기반 스트림은 바이트 단위로 읽는 것에 비해 Buffered 스트림은 8KB 단위로 읽어 효율적이다.
                 */
                // 클라이언트 Socket의 입력 스트림: 서버 → 클라이언트 전송 시 사용
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                // 클라이언트 Socket의 출력 스트림: 클라이언트 → 서버 전송 시 사용
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            while (true) {
                BufferedReader userInput = new BufferedReader(
                        new InputStreamReader(System.in, StandardCharsets.UTF_8));
                // readLine(): 스트림에 들어온 것이 없다면 null을 반환한다.
                System.out.println(reader.readLine());
                System.out.print("＞ ");
                writer.write(userInput.readLine());
            }

        } catch (ConnectException e) {
            System.out.println(Constants.SYSTEM_NAME + "서버를 찾지 못했습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}