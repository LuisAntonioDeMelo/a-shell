package javaShell.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class Command {

    private final String input;
    private final String[] arguments;

    public Command(String input) {
        this.input = input;
        this.arguments = parse(input);
    }

    public String getInput() {
        return input;
    }

    public String getName() {
        return arguments.length == 0 ? "" : arguments[0];
    }

    public String[] getArguments() {
        return arguments.clone();
    }

    public String[] getParameters() {
        return Arrays.copyOfRange(arguments, Math.min(1, arguments.length), arguments.length);
    }

    public Optional<Type> getType() {
        return Type.from(getName());
    }

    public boolean isEmpty() {
        return arguments.length == 0;
    }

    private static String[] parse(String input) {
        List<String> arguments = new ArrayList<>();
        StringBuilder word = new StringBuilder();
        boolean argumentStarted = false;
        char quote = '\0';
        boolean escaped = false;

        for (char character : input.toCharArray()) {
            if (escaped) {
                word.append(character);
                escaped = false;
            } else if (character == '\\' && quote != '\'') {
                escaped = true;
                argumentStarted = true;
            } else if (character == '\'' || character == '"') {
                if (quote == '\0') {
                    quote = character;
                    argumentStarted = true;
                } else if (quote == character) {
                    quote = '\0';
                } else {
                    word.append(character);
                    argumentStarted = true;
                }
            } else if (Character.isWhitespace(character) && quote == '\0') {
                if (argumentStarted) {
                    arguments.add(word.toString());
                    word.setLength(0);
                    argumentStarted = false;
                }
            } else {
                word.append(character);
                argumentStarted = true;
            }
        }

        if (escaped) {
            word.append('\\');
        }
        if (argumentStarted) {
            arguments.add(word.toString());
        }
        return arguments.toArray(String[]::new);
    }
}
