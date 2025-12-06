import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

public class InputHandler {

    private final Terminal terminal;
    private final NonBlockingReader reader;
    private volatile boolean running = true;

    public InputHandler() throws Exception {
        terminal = TerminalBuilder.builder()
                .system(true)
                .jna(true)        // quan trọng, cho phép raw mode
                .build();

        terminal.enterRawMode();   // chế độ raw: nhận phím ngay lập tức
        reader = terminal.reader();
    }

    /**
     * Đọc phím theo kiểu non-blocking (không cần Enter)
     * @return mã phím, hoặc -1 nếu không có phím
     */
    public int readKeyNonBlocking() throws Exception {
        return reader.read(10); // timeout 10ms → không block
    }
    public void stopListening() {
        running = false;
    }
}
