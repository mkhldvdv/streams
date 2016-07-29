package streams.checks;

/**
 * Created by mikhail.davydov on 2016/7/28.
 */
public class Checks {

    /**
     * check buffer input correct value entered
     *
     * @param input value entered
     * @return value or -1 if incorrect
     */
    public static int isBufferInputCorrect(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * check if any of input values is null
     *
     * @param input file destination
     * @param output file destination
     * @param buffer size
     * @return true or false
     */
    public static boolean isInputNull(String input, String output, String buffer) {
        return input.isEmpty() || output.isEmpty() || buffer.isEmpty();
    }
}
