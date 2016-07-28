package streams.checks;

/**
 * Created by mikhail.davydov on 2016/7/28.
 */
public class Checks {

    public static int isBufferInputCorrect(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static boolean isInputNull(String input, String output, String buffer) {
        return input.isEmpty() || output.isEmpty() || buffer.isEmpty();
    }
}
