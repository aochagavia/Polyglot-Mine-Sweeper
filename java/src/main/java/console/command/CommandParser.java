package console.command;

import java.security.InvalidParameterException;
import java.util.Optional;

public class CommandParser {
    public static Optional<Command> parse(String str) {
        try {
            return Optional.of(privParse(str));
        } catch(Exception e) {
            return Optional.empty();
        }
    }

    private static Command privParse(String str) throws Exception {
        String[] parts = str.split(" ");
        if (parts.length < 3)
            throw new InvalidParameterException();

        // The format is <letter> <x> <y>
        // We subtract 1 from each variable, since indexing is 0 based
        int x = Integer.parseInt(parts[1]) - 1;
        int y = Integer.parseInt(parts[2]) - 1;

        switch (parts[0]) {
            case "s": return new ShowCommand(x, y);
            case "m": return new MarkCommand(x, y);
            default: throw new InvalidParameterException();
        }
    }
}
