import fp.FunctionalRouter;
import static fp.FunctionalRouter.route;
import static fp.FunctionalRouter.defaultRoute;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;

public class App {

    public static void main (String[] args) {
        String[] input = {"10", "20", null, "30"};

        Function<String, Integer>
        safeParseInt = string -> {
            try {
                return Integer.parseInt(string);
            } catch (Exception ignored) {
                return 0;
            }
        };

        Predicate<String>
        hasMoreThan3Chars = n -> n.length() > 3;

        Function<String, String>
        trimmedToFirst2Chars = n -> n.substring(0, 2);

        Function<String, Integer>
        sumOfChars = n -> {
            char[] chars = n.toCharArray();
            int sum = 0;
            for (char c : chars)
                if (Character.isDigit(c))
                    sum += safeParseInt.apply(String.valueOf(c));
            return sum;
        };

        List<Integer> sums =
            Arrays
                .stream(input)
                .map(
                    FunctionalRouter.of(
                        route(
                            Objects::isNull,
                            value -> 0),
                        route(
                            hasMoreThan3Chars,
                            sumOfChars
                                .compose(trimmedToFirst2Chars)),
                        defaultRoute(sumOfChars)))
                .collect(Collectors.toList());
        // 1, 2, 0, 3
        System.out.println(sums);

        List<Integer> sumsWithSwitch =
            Arrays
                .stream(input)
                .map(value -> {
                    if (Objects.isNull(value))
                        return 0;

                    if (hasMoreThan3Chars.test(value))
                        return trimmedToFirst2Chars
                            .andThen(sumOfChars)
                            .apply(value);

                    return sumOfChars.apply(value);
                })
                .collect(Collectors.toList());
        // 1, 2, 0, 3
        System.out.println(sumsWithSwitch);
    }
}
